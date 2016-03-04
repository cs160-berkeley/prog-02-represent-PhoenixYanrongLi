package com.example.yanrongli.cs260a_hw2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanrongli on 2/27/16.
 */
public class ProfileActivity extends Activity {
    private String myLocation = "";
    private String myZipcode = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String previousActivity = intent.getExtras().getString("callingActivity");
        if(previousActivity.equals("PhoneListenerService"))
        {
            String message = intent.getExtras().getString("message_from_watch");
            if(message.equals("random"))
            {
                setContentView(R.layout.layout_congre);
                TextView searchBy = (TextView) findViewById(R.id.congre_textView_searchBy);
                double randNum = Math.random();
                searchBy.setText("Search randomly: " + String.valueOf(randNum));
                //may need other operation to fill in the data.
                //Also need to send an intent to PhoneToWatchService!!!!!
                populateProfileList();
                populateListView();
                sendProfileToWatch();
            }
            else
            {
                int position = 0;
                for(int i = 0; i < PublicData.myProfiles.size(); i++)
                {
                    if(message.equals(PublicData.myProfiles.get(i).getName()))
                    {
                        position = i;
                    }
                }

                Intent nextIntent = new Intent(ProfileActivity.this, DetailActivity.class);
                nextIntent.putExtra("name", PublicData.myProfiles.get(position).getName());
                nextIntent.putExtra("party", PublicData.myProfiles.get(position).getParty());
                nextIntent.putExtra("iconID", (int)(PublicData.myProfiles.get(position).getIconID()));
                startActivity(nextIntent);
                //nextIntent.putExtra("",)
                //....here put more if needed.
            }
        }
        else if(previousActivity.equals("MainActivity")){
            setContentView(R.layout.layout_congre);
            myZipcode = intent.getExtras().getString("zipcode");
            myLocation = intent.getExtras().getString("location");

            TextView searchBy = (TextView) findViewById(R.id.congre_textView_searchBy);
            if(myZipcode.equals(""))
                searchBy.setText("Search by location: " + myLocation);
            else
                searchBy.setText("Search by zipcode: " + myZipcode);

            populateProfileList();
            populateListView();
            sendProfileToWatch();
        }
    }

    private void populateProfileList() {
        PublicData.myProfiles = new ArrayList<Profile>();
        //Here we might need to generate several more profile items due to a hardware issue
        //May need to adapt to API. The profiles should be read from API data. The API data should be stored in a private variable.
        PublicData.myProfiles.add(new Profile("Barack Obama", R.drawable.obama, "Democrat", "obama@gmail.com", "www.obama.com", "Yes we can!"));
        PublicData.myProfiles.add(new Profile("Albus Dumbledore", R.drawable.albus, "Republican", "albus@gmail.com", "www.albus.com","Deathly Hallows."));
        PublicData.myProfiles.add(new Profile("Gandalf", R.drawable.gandalf, "Independent", "gandalf@sina.com", "www.gandalf.cn", "You shall not pass!"));
        PublicData.myProfiles.add(new Profile("Jon Snow", R.drawable.snow, "Republican", "snow@gmail.com", "www.snow.com","Winter is coming"));
        PublicData.myProfiles.add(new Profile("Saruman", R.drawable.saruman, "Independent", "saruman@sina.com", "www.saruman.cn","This is the doom of mankind!"));
        PublicData.myProfiles.add(new Profile("Tyrion Lennister", R.drawable.tyrion, "Democrat", "tyrion@gmail.com", "www.tlenni.com","A dwarf is the same as a bastard in their father's eyes."));
    }

    private void populateListView() {
        ArrayAdapter<Profile> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.congre_listView);
        list.setAdapter(adapter);
    }

    private void sendProfileToWatch() {
        List<String> nameList = new ArrayList<String>();
        List<String> partyList = new ArrayList<String>();

        for(int i = 0; i < PublicData.myProfiles.size(); i++)
        {
            nameList.add(PublicData.myProfiles.get(i).getName());
            partyList.add(PublicData.myProfiles.get(i).getParty());
        }

        String finalNameList = TextUtils.join("_", nameList);
        String finalPartyList = TextUtils.join("_", partyList);

        System.out.println(finalNameList);
        System.out.println(finalPartyList);

        Intent intent = new Intent(ProfileActivity.this, PhoneToWatchService.class);
        intent.putExtra("name_list", finalNameList);
        intent.putExtra("party_list", finalPartyList);
        intent.putExtra("location", myLocation);
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

            //Fill the view
            ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.congre_imageButton);
            imageButton.setImageResource(currentProfile.getIconID());
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent nextIntent = new Intent(ProfileActivity.this, DetailActivity.class);
                    nextIntent.putExtra("name", currentProfile.getName());
                    nextIntent.putExtra("party", currentProfile.getParty());
                    nextIntent.putExtra("iconID", (int)(currentProfile.getIconID()));
                    startActivity(nextIntent);
                }
            });

            Button button = (Button) itemView.findViewById(R.id.congre_button_detail);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy the onClick above
                    Intent nextIntent = new Intent(ProfileActivity.this, DetailActivity.class);
                    nextIntent.putExtra("name", currentProfile.getName());
                    nextIntent.putExtra("party", currentProfile.getParty());
                    nextIntent.putExtra("iconID", (int)(currentProfile.getIconID()));
                    startActivity(nextIntent);
                }
            });

            TextView nameText = (TextView) itemView.findViewById(R.id.congre_textView_name);
            TextView partyText = (TextView) itemView.findViewById(R.id.congre_textView_party);
            TextView emailText = (TextView) itemView.findViewById(R.id.congre_textView_email);
            TextView webText = (TextView) itemView.findViewById(R.id.congre_textView_web);
            TextView tweetText = (TextView) itemView.findViewById(R.id.congre_textView_lastTweetText);

            nameText.setText(currentProfile.getName());
            partyText.setText(currentProfile.getParty());
            emailText.setText(currentProfile.getEmail());
            webText.setText(currentProfile.getWebsite());
            tweetText.setText(currentProfile.getTweet());

            ImageView nameImage = (ImageView) itemView.findViewById(R.id.congre_imageView_name);
            ImageView partyImage = (ImageView) itemView.findViewById(R.id.congre_imageView_party);
            ImageView emailImage = (ImageView) itemView.findViewById(R.id.congre_imageView_email);
            ImageView webImage = (ImageView) itemView.findViewById(R.id.congre_imageView_web);

            RelativeLayout tweetLayout = (RelativeLayout) itemView.findViewById(R.id.congre_relativeLayout2);

            if(currentProfile.getParty().equals("Democrat"))
            {
                nameImage.setImageResource(R.drawable.name_red);
                partyImage.setImageResource(R.drawable.party_red);
                emailImage.setImageResource(R.drawable.email_red);
                webImage.setImageResource(R.drawable.web_red);
                button.setBackgroundResource(R.drawable.border_red);
                tweetLayout.setBackgroundResource(R.drawable.border_red);
            }
            else if(currentProfile.getParty().equals("Republican"))
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

            return super.getView(position, convertView, parent);
        }
    }
}
