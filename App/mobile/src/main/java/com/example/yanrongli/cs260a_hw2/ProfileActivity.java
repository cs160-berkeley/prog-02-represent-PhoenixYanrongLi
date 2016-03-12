package com.example.yanrongli.cs260a_hw2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yanrongli on 2/27/16.
 */
public class ProfileActivity extends Activity {
    //Google API:
    private String site = "http://congress.api.sunlightfoundation.com/legislators/locate?";
    private String apikey = "&apikey=b56168748ed24025a1c714a4b4ea6d70";
    private String myLocation = "";
    private String myZipcode = "";
    private String myMode = "";
    private String myCoordinates = "";
    private String myLongitude = "";
    private String myLatitude = "";
    private String myCounty = "";
    private String myState = "";
    private int myProfileCount = 0;
    private TwitterApiClient myTwitterApiClient;
    //Twitter API
    private static final String TWITTER_KEY = "DmmfIS6oa75ZT1Lc5FNjG2jr7";
    private static final String TWITTER_SECRET = "IS7kVbG8uNW4vxMnjUj1AFupYFCgl4RjiIuBTrvaOPrIgeCy9s";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_congre);
        myTwitterApiClient = TwitterCore.getInstance().getApiClient();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        Intent intent = getIntent();
        String previousActivity = intent.getExtras().getString("callingActivity");
        if(previousActivity.equals("PhoneListenerService"))
        {
            String message = intent.getExtras().getString("message_from_watch");
            if(message.equals("random"))
            {
                myZipcode = intent.getExtras().getString("zipcode");
                myLocation = intent.getExtras().getString("location");
                myMode = intent.getExtras().getString("searchMode");
                myCoordinates = intent.getExtras().getString("coordinates");
                myCounty = myLocation.split(", ")[0];
                myState = myLocation.split(", ")[1];
                myLatitude = myCoordinates.split("_")[1];
                myLongitude = myCoordinates.split("_")[0];


                TextView searchBy = (TextView) findViewById(R.id.congre_textView_searchBy);
                if(myMode.equals("location")) {
                    searchBy.setText("Search by location: " + myLocation);
                }
                else if(myMode.equals("zipcode")) {
                    searchBy.setText("Search by zipcode: " + myZipcode);
                }
                else {
                    searchBy.setText("Search randomly: " + myLocation);
                }

                populateProfileList();

            }
        }
        else if(previousActivity.equals("MainActivity")){
            myZipcode = intent.getExtras().getString("zipcode");
            myLocation = intent.getExtras().getString("location");
            myMode = intent.getExtras().getString("searchMode");
            myCoordinates = intent.getExtras().getString("coordinates");
            myCounty = myLocation.split(", ")[0];
            myState = myLocation.split(", ")[1];
            myLatitude = myCoordinates.split("_")[1];
            myLongitude = myCoordinates.split("_")[0];


            TextView searchBy = (TextView) findViewById(R.id.congre_textView_searchBy);
            if(myMode.equals("location")) {
                searchBy.setText("Search by location: " + myLocation);
            }
            else if(myMode.equals("zipcode")) {
                searchBy.setText("Search by zipcode: " + myZipcode);
            }
            else {
                searchBy.setText("Search randomly: " + myLocation);
            }

            populateProfileList();

        }

    }

    private void populateProfileList() {
        PublicData.myProfiles = new ArrayList<Profile>();
        String request = "";
        if(myMode.equals("zipcode")){
            request = site + "zip=" + myZipcode + apikey;
        }
        else { // by location or randomly
            request = site + "latitude=" + myLatitude + "&longitude=" + myLongitude + apikey;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, request, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jProfileList) {
                        try{
                            myProfileCount = jProfileList.getInt("count");
                            JSONArray results = jProfileList.optJSONArray("results");

                            for(int i = 0; i < myProfileCount; i++)
                            {
                                JSONObject profile = results.getJSONObject(i);
                                String name = profile.getString("first_name") + " " + profile.getString("last_name");
                                String title = "";
                                if(profile.getString("title").equals("Sen")){
                                    title = "Sen.";
                                }
                                else{
                                    title = "Rep.";
                                }

                                String party = "";
                                if(profile.getString("party").equals("R")){
                                    party = "Republican";
                                }
                                else if(profile.getString("party").equals("D")){
                                    party = "Democrat";
                                }
                                else {
                                    party = "Independent";
                                }

                                String email = profile.getString("oc_email");
                                String website = profile.getString("website");
                                String twitterId = profile.getString("twitter_id");
                                String termEnd = profile.getString("term_end");
                                String userId = profile.getString("bioguide_id");
                                PublicData.myProfiles.add(new Profile(userId, name, title, party, email, website, twitterId, termEnd));

                                //populateListView();
                                System.out.println("what the fuck?????");
                            }
                            //fill in the dummy data
                            //PublicData.myProfiles.add(new Profile("dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata"));
                            //PublicData.myProfiles.add(new Profile("dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata"));
                            //PublicData.myProfiles.add(new Profile("dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata"));
                            //PublicData.myProfiles.add(new Profile("dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata","dummydata"));
                            PublicData.myProfiles.add(new Profile(" "," "," "," "," "," ","dummydata"," "));
                            PublicData.myProfiles.add(new Profile(" "," "," "," "," "," ","dummydata"," "));
                            PublicData.myProfiles.add(new Profile(" "," "," "," "," "," ","dummydata"," "));
                            PublicData.myProfiles.add(new Profile(" "," "," "," "," "," ","dummydata"," "));
                            populateListView();

                            sendProfileToWatch();

                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){

                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }

    private void populateListView() {
        ArrayAdapter<Profile> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.congre_listView);
        list.setAdapter(adapter);
    }

    private void sendProfileToWatch() {
        List<String> nameList = new ArrayList<String>();
        List<String> partyList = new ArrayList<String>();

        for(int i = 0; i < PublicData.myProfiles.size() - 4; i++)
        {
            nameList.add(PublicData.myProfiles.get(i).getName());
            partyList.add(PublicData.myProfiles.get(i).getParty());
        }

        String finalNameList = TextUtils.join("_", nameList);
        String finalPartyList = TextUtils.join("_", partyList);
        String final2012Vote = get2012Vote(myCounty, myState);

        Intent intent = new Intent(ProfileActivity.this, PhoneToWatchService.class);
        intent.putExtra("name_list", finalNameList);
        intent.putExtra("party_list", finalPartyList);
        intent.putExtra("2012vote", final2012Vote);
        startService(intent);

    }

    private class MyListAdapter extends ArrayAdapter<Profile> {
        public MyListAdapter()
        {
            super(ProfileActivity.this, R.layout.profile_view, R.id.fill_profile_view, PublicData.myProfiles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //Make sure we have a view to work with (may have been given null)
            View itemView = convertView;

            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.profile_view, parent, false);
            }

            //Find the profile to work with
            final Profile currentProfile = PublicData.myProfiles.get(position);
            //Retrieve the twitter information
            final String screen_name = currentProfile.getTwitterId();
            StatusesService statusesService = myTwitterApiClient.getStatusesService();
            statusesService.userTimeline(null, screen_name, 1, null, null, null, true, null, true, new Callback<List<Tweet>>() {
                //statusesService.show(524971209851543553L, null, null, null, new Callback<Tweet>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    //Do something with result, which provides a Tweet inside of result.data
                    currentProfile.setTweet(result.data.get(0).text);
                    String tmpUrl = result.data.get(0).user.profileImageUrl;
                    currentProfile.setImgURL(tmpUrl.replaceAll("normal", "bigger"));
                }
                @Override
                public void failure(TwitterException exception) {
                }
            });

            //Fill the view


            Button button = (Button) itemView.findViewById(R.id.congre_button_detail);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy the onClick above
                    Intent nextIntent = new Intent(ProfileActivity.this, DetailActivity.class);
                    nextIntent.putExtra("callingActivity", "ProfileActivity");
                    nextIntent.putExtra("userId", currentProfile.getUserId());
                    nextIntent.putExtra("name", currentProfile.getTitle() + currentProfile.getName());
                    nextIntent.putExtra("party", currentProfile.getParty());
                    nextIntent.putExtra("termEnd", currentProfile.getTermEnd());
                    nextIntent.putExtra("imgURL", currentProfile.getImgURL());
                    startActivity(nextIntent);
                }
            });

            TextView nameText = (TextView) itemView.findViewById(R.id.congre_textView_name);
            TextView partyText = (TextView) itemView.findViewById(R.id.congre_textView_party);
            TextView emailText = (TextView) itemView.findViewById(R.id.congre_textView_email);
            TextView webText = (TextView) itemView.findViewById(R.id.congre_textView_web);
            TextView tweetText = (TextView) itemView.findViewById(R.id.congre_textView_lastTweetText);

            nameText.setText(currentProfile.getTitle() + currentProfile.getName());
            partyText.setText(currentProfile.getParty());
            emailText.setText(currentProfile.getEmail());
            webText.setText(currentProfile.getWebsite());
            tweetText.setText(currentProfile.getTweet());

            if(currentProfile.getTwitterId().equals("dummydata"))
                tweetText.setText(" ");

            ImageView nameImage = (ImageView) itemView.findViewById(R.id.congre_imageView_name);
            ImageView partyImage = (ImageView) itemView.findViewById(R.id.congre_imageView_party);
            ImageView emailImage = (ImageView) itemView.findViewById(R.id.congre_imageView_email);
            ImageView webImage = (ImageView) itemView.findViewById(R.id.congre_imageView_web);

            RelativeLayout tweetLayout = (RelativeLayout) itemView.findViewById(R.id.congre_relativeLayout2);

            if(currentProfile.getParty().equals("Republican"))
            {
                nameImage.setImageResource(R.drawable.name_red);
                partyImage.setImageResource(R.drawable.party_red);
                emailImage.setImageResource(R.drawable.email_red);
                webImage.setImageResource(R.drawable.web_red);
                button.setBackgroundResource(R.drawable.border_red);
                tweetLayout.setBackgroundResource(R.drawable.border_red);
            }
            else if(currentProfile.getParty().equals("Democrat"))
            {
                nameImage.setImageResource(R.drawable.name_blue);
                partyImage.setImageResource(R.drawable.party_blue);
                emailImage.setImageResource(R.drawable.email_blue);
                webImage.setImageResource(R.drawable.web_blue);
                button.setBackgroundResource(R.drawable.border_blue);
                tweetLayout.setBackgroundResource(R.drawable.border_blue);
            }
            else
            {
                nameImage.setImageResource(R.drawable.name_white);
                partyImage.setImageResource(R.drawable.party_white);
                emailImage.setImageResource(R.drawable.email_white);
                webImage.setImageResource(R.drawable.web_white);
                button.setBackgroundResource(R.drawable.border_white);
                tweetLayout.setBackgroundResource(R.drawable.border_white);
            }

            ImageView personImage = (ImageView) itemView.findViewById(R.id.congre_imageView_person);
            //String URL = "http://pbs.twimg.com/profile_images/430378206353317888/3QKYak-Z_bigger.jpeg";
            if(currentProfile.getTwitterId().equals("dummydata"))
                personImage.setTag("http://www.freelanceme.net/Images/default%20profile%20picture.png");
            else
                personImage.setTag(currentProfile.getImgURL());
            new DownloadImageTask().execute(personImage);

            return super.getView(position, convertView, parent);
        }
    }

    //load Json from asset directory
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("election-county-2012.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //search for 2012 vote data based on County + State
    private String get2012Vote(String county, String state) {
        String voteString = loadJSONFromAsset();
        String concatVoteData = null;
        try {
            JSONArray jVote = new JSONArray(voteString);
            int jVoteLength = jVote.length();
            //search through match attributes
            //System.out.println(county.split(" ")[0]);
            //System.out.println(state);
            for(int i=0; i < jVoteLength; i++) {
                JSONObject loc = jVote.getJSONObject(i);
                //System.out.println(loc);
                if(loc.getString("county-name").equals(county.split(" ")[0]) && loc.getString("state-postal").equals(state)) {
                    //location match!
                    String obamaVote = Double.toString(loc.getDouble("obama-percentage"));
                    String romneyVote = Double.toString(loc.getDouble("romney-percentage"));
                    concatVoteData = county.split(" ")[0] + "_" + state + "_" + obamaVote + "_" + romneyVote;
                    System.out.println(concatVoteData);
                    return concatVoteData;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return concatVoteData;
    }
}
