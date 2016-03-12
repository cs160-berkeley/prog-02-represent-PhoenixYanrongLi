package com.example.yanrongli.cs260a_hw2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yanrongli on 2/27/16.
 */
public class DetailActivity extends Activity {

    private String userId = "";
    private String name = "";
    private String party = "";
    private String termEnd = "";
    private String imgURL = "";
    private String site = "http://congress.api.sunlightfoundation.com/";
    private String apikey = "&apikey=b56168748ed24025a1c714a4b4ea6d70";
    //congress.api.sunlightfoundation.com/committees?member_ids=1&apikey=b56168748ed24025a1c714a4b4ea6d70
    //congress.api.sunlightfoundation.com/bills?sponsor_id=1&apikey=b56168748ed24025a1c714a4b4ea6d70


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get bundle to read the data and set the private variables
        Intent lastIntent = getIntent();
        if(lastIntent.getExtras().getString("callingActivity").equals("ProfileActivity")) {
            userId = lastIntent.getExtras().getString("userId");
            name = lastIntent.getExtras().getString("name");
            party = lastIntent.getExtras().getString("party");
            termEnd = lastIntent.getExtras().getString("termEnd");
            imgURL = lastIntent.getExtras().getString("imgURL");

            setContentView(R.layout.layout_detail);

            populateProfile();
            populateTextViewCommittees();
            populateTextViewSponsors();
        }
        else
        {
            name = lastIntent.getExtras().getString("message_from_watch");
            int position = 0;
            for(int i = 0; i < PublicData.myProfiles.size(); i++)
            {
                if(name.equals(PublicData.myProfiles.get(i).getName()))
                {
                    position = i;
                }
            }
            userId = PublicData.myProfiles.get(position).getUserId();
            party = PublicData.myProfiles.get(position).getParty();
            termEnd = PublicData.myProfiles.get(position).getTermEnd();
            imgURL = PublicData.myProfiles.get(position).getImgURL();

            setContentView(R.layout.layout_detail);

            populateProfile();
            populateTextViewCommittees();
            populateTextViewSponsors();

        }
    }

    private void populateProfile(){
        //use private variables to set the profile
        ImageView personImage = (ImageView) findViewById(R.id.detail_imageView);
        personImage.setTag(imgURL);
        new DownloadImageTask().execute(personImage);

        TextView textViewName = (TextView) findViewById(R.id.detail_textView_name);
        TextView textViewParty = (TextView) findViewById(R.id.detail_textView_party);
        TextView textViewDate = (TextView) findViewById(R.id.detail_endDate);

        textViewName.setText(name);
        textViewParty.setText(party);
        textViewDate.setText(termEnd);

        RelativeLayout dateLayout = (RelativeLayout) findViewById(R.id.detail_layout_date);
        RelativeLayout committeeLayout = (RelativeLayout) findViewById(R.id.detail_layout_committee);
        RelativeLayout sponsorLayout = (RelativeLayout) findViewById(R.id.detail_layout_sponsor);
        if(party.equals("Republican")) {
            dateLayout.setBackgroundResource(R.drawable.border_red);
            committeeLayout.setBackgroundResource(R.drawable.border_red);
            sponsorLayout.setBackgroundResource(R.drawable.border_red);
        }
        else if(party.equals("Democrat")) {
            dateLayout.setBackgroundResource(R.drawable.border_blue);
            committeeLayout.setBackgroundResource(R.drawable.border_blue);
            sponsorLayout.setBackgroundResource(R.drawable.border_blue);

        }
        else {
            dateLayout.setBackgroundResource(R.drawable.border_white);
            committeeLayout.setBackgroundResource(R.drawable.border_white);
            sponsorLayout.setBackgroundResource(R.drawable.border_white);
        }
    }

    private void populateTextViewCommittees() {

        String requestCommittees = site + "committees?member_ids=" + userId + apikey;
        final TextView myCommittees = (TextView) findViewById(R.id.detail_textView_committees);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, requestCommittees, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jCommittees) {
                try{
                    int committeeCount = jCommittees.getInt("count");
                    JSONArray results = jCommittees.optJSONArray("results");
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 0; i < committeeCount; i++) {
                        JSONObject committee = results.getJSONObject(i);
                        stringBuilder.append(Integer.toString(i+1) + ". " + committee.getString("name")+"\n\n");
                    }
                    myCommittees.setText(stringBuilder.toString());
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void populateTextViewSponsors() {
        String requestBills = site + "bills?sponsor_id=" + userId + apikey;
        final TextView mySponsors = (TextView) findViewById(R.id.detail_textView_sponsors);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, requestBills, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jBills) {
                        try{
                            int billCount = jBills.getInt("count");
                            if (billCount > 20) billCount = 20;
                            JSONArray results = jBills.optJSONArray("results");
                            StringBuilder stringBuilder = new StringBuilder();
                            for(int i = 0; i < billCount; i++) {
                                if(results.getJSONObject(i) != null) {
                                    JSONObject bill = results.getJSONObject(i);
                                    stringBuilder.append("(" + bill.getString("introduced_on") + ") " + bill.getString("official_title") + "\n\n");
                                }
                            }
                            mySponsors.setText(stringBuilder.toString());
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}

