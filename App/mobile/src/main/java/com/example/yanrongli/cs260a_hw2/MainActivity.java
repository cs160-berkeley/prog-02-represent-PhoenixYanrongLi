package com.example.yanrongli.cs260a_hw2;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private GoogleApiClient mGoogleApiClient;
    private String latitude = "";
    private String longitude = "";
    private String location_county = "";
    private String location_state = "";
    private String zipcode = "";

    //Google API
    String site = "https://maps.googleapis.com/maps/api/geocode/json?";
    //String apikey = "&key=AIzaSyC4ejrsUZXmtYLCMmgEFBjkduOolOavt90";
    String apikey = "&key=AIzaSyC4ejrsUZXmtYLCMmgEFBjkduOolOavt90";

    //Twitter API
    private static final String TWITTER_KEY = "DmmfIS6oa75ZT1Lc5FNjG2jr7";
    private static final String TWITTER_SECRET = "IS7kVbG8uNW4vxMnjUj1AFupYFCgl4RjiIuBTrvaOPrIgeCy9s";
    TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize twitter api:
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Initialize google api;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //Set view and layout
        setContentView(R.layout.activity_main);
        Button search = (Button) findViewById(R.id.main_button_search);
        search.setVisibility(View.INVISIBLE);
        EditText edit = (EditText) findViewById(R.id.main_editText);
        edit.setVisibility(View.INVISIBLE);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        //Twitter login button callback
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String username = result.data.getUserName();
                Toast.makeText(MainActivity.this, "Logged in as " + username, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void failure(TwitterException e) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickZipcodeSearch(View view){
        Button button = (Button) view;
        button.setVisibility(View.INVISIBLE);
        EditText edit = (EditText) findViewById(R.id.main_editText);
        Button innerButton = (Button) findViewById(R.id.main_button_search);
        edit.setVisibility(View.VISIBLE);
        innerButton.setVisibility(View.VISIBLE);
    }

    public void onClickSearch(View view){
        EditText edit = (EditText) findViewById(R.id.main_editText);
        if (edit.getText().toString().matches(""))
        {
            Toast.makeText(MainActivity.this, "Please input a zipcode!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Button innerButton = (Button) view;
            innerButton.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            Button button = (Button) findViewById(R.id.main_button_byZip);
            button.setVisibility(View.VISIBLE);
            zipcode = edit.getText().toString();

            //get data from api
            String url = site + "address=" + zipcode + "&sensor=false" + apikey;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jLocation) {
                            JSONArray results = jLocation.optJSONArray("results");
                            try{
                                JSONObject address_components = results.getJSONObject(0);
                                JSONArray address = address_components.optJSONArray("address_components");
                                for (int i = 0; i < address.length(); i++) {
                                    JSONObject tmpObj = address.getJSONObject(i);
                                    if(tmpObj.getString("long_name").contains("County")) {
                                        location_county = tmpObj.getString("long_name");
                                        location_state = address.getJSONObject(i+1).getString("short_name");
                                    }
                                }
                                turnToNewIntent("zipcode", zipcode);
                            } catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    public void onClickLocationSearch(View view){

        String url = site + "latlng=" + latitude + "," + longitude + apikey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jLocation) {
                        System.out.println("uuuuuu");
                        JSONArray results = jLocation.optJSONArray("results");
                        try{
                            JSONObject address_components = results.getJSONObject(0);
                            System.out.println("vvvvvvvvv");
                            JSONArray address = address_components.optJSONArray("address_components");
                            for (int i = 0; i < address.length(); i++) {
                                JSONObject tmpObj = address.getJSONObject(i);
                                if(tmpObj.getString("long_name").contains("County")) {
                                    location_county = tmpObj.getString("long_name");
                                    location_state = address.getJSONObject(i+1).getString("short_name");
                                    System.out.println("m,mmmmmmmmmmmmmm");
                                    System.out.println(location_county);
                                    System.out.println(location_state);
                                }
                            }
                            //ifDataLoaded = true;
                            turnToNewIntent("location", "");
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //System.out.println("Error response!!!!!");
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    public void turnToNewIntent(String searchMode, String zipcode)
    {
        Intent getProfileScreenIntent = new Intent(this, ProfileActivity.class);
        getProfileScreenIntent.putExtra("callingActivity", "MainActivity");
        getProfileScreenIntent.putExtra("searchMode", searchMode);
        getProfileScreenIntent.putExtra("zipcode", zipcode);
        getProfileScreenIntent.putExtra("location", location_county + ", " + location_state);
        System.out.println("location_county sent: " + location_county);
        System.out.println("location_state sent: " + location_state);
        getProfileScreenIntent.putExtra("coordinates", longitude + "_" + latitude);

        startActivity(getProfileScreenIntent);
    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        System.out.println("mGoogleApiClient connected..");
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        System.out.println("mGoogleApiClient disconnected..");
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            System.out.println(String.valueOf(mLastLocation.getLatitude()));
            latitude = String.valueOf(mLastLocation.getLatitude());
            System.out.println(String.valueOf(mLastLocation.getLongitude()));
            longitude = String.valueOf(mLastLocation.getLongitude());
            //getAddressFromLocation(mLastLocation, this, new GeocoderHandler());
        } else {
            System.out.println("null location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

}
