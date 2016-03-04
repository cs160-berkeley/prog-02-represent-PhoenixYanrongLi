package com.example.yanrongli.cs260a_hw2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanrongli on 2/27/16.
 */
public class DetailActivity extends Activity {

    //private List<String> myCommittees = new ArrayList<String>();
    //private List<String> mySponsors = new ArrayList<String>();
    private String name = "";
    private String party = "";
    private int iconID = 0;
    private String[] inputCommittees = {"Committee A", "Committee B", "Committee C"};
    private String[] mySponsors = {"Sponsor A", "Sponsor B", "Sponsor C"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get bundle to read the data and set the private variables
        Intent lastIntent = getIntent();
        name = lastIntent.getExtras().getString("name");
        party = lastIntent.getExtras().getString("party");
        iconID = lastIntent.getExtras().getInt("iconID");

        setContentView(R.layout.layout_detail);

        populateProfile();
        populateListViewCommittees();
        populateListViewSponsors();
    }

    private void populateProfile(){
        //use private variables to set the profile
        TextView textViewName = (TextView) findViewById(R.id.detail_textView_name);
        TextView textViewParty = (TextView) findViewById(R.id.detail_textView_party);
        ImageView imageView = (ImageView) findViewById(R.id.detail_imageView);

        textViewName.setText(name);
        textViewParty.setText(party);
        imageView.setImageResource(iconID);

        RelativeLayout dateLayout = (RelativeLayout) findViewById(R.id.detail_layout_date);
        RelativeLayout committeeLayout = (RelativeLayout) findViewById(R.id.detail_layout_committee);
        RelativeLayout sponsorLayout = (RelativeLayout) findViewById(R.id.detail_layout_sponsor);
        if(party.equals("Democrat")) {
            dateLayout.setBackgroundResource(R.drawable.border_red);
            committeeLayout.setBackgroundResource(R.drawable.border_red);
            sponsorLayout.setBackgroundResource(R.drawable.border_red);
        }
        else if(party.equals("Republican")) {
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

    private void populateListViewCommittees() {

        List<String> myCommittees = new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,               //Context for the activity
                R.layout.text_item,   //Layout to use(create)
                myCommittees
        );


        for (int i = 0; i < inputCommittees.length; i++)
            myCommittees.add(inputCommittees[i]);
        ListView list = (ListView) findViewById(R.id.detail_listView_committees);
        list.setAdapter(adapter);
    }

    private void populateListViewSponsors() {

        List<String> mySponsors2 = new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,               //Context for the activity
                R.layout.text_item,   //Layout to use(create)
                mySponsors2
        );
        for (int i = 0; i < mySponsors.length; i++)
            mySponsors2.add(mySponsors[i]);

        ListView list = (ListView) findViewById(R.id.detail_listView_sponsors);
        list.setAdapter(adapter);
    }

}

