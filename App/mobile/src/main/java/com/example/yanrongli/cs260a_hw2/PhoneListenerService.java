package com.example.yanrongli.cs260a_hw2;

import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    //private static final String TOAST = "/send_toast";
    private static final String MESSAGE = "/message_from_watch";
    //Google API
    String site = "https://maps.googleapis.com/maps/api/geocode/json?";
    //String apikey = "&key=AIzaSyC4ejrsUZXmtYLCMmgEFBjkduOolOavt90";
    String apikey = "&key=AIzaSyC4ejrsUZXmtYLCMmgEFBjkduOolOavt90";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        //Modify this whole things to start DetailedActivity using data from ProfileActivity

        if( messageEvent.getPath().equalsIgnoreCase(MESSAGE) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            if(message.equals("random"))
            {
                Random rand = new Random();
                int latiInt = rand.nextInt(9) + 33;
                int latiFrac = rand.nextInt(1000000);
                int longiInt = rand.nextInt(34) + 83;
                int longiFrac = rand.nextInt(1000000);
                final String latitude = Integer.toString(latiInt) + "." + Integer.toString(latiFrac);
                final String longitude = "-" + Integer.toString(longiInt) + "." + Integer.toString(longiFrac);

                String url = site + "latlng=" + latitude + "," + longitude + apikey;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jLocation) {
                                JSONArray results = jLocation.optJSONArray("results");
                                try{
                                    JSONObject address_components = results.getJSONObject(0);
                                    JSONArray address = address_components.optJSONArray("address_components");
                                    String location_county = "";
                                    String location_state = "";
                                    for (int i = 0; i < address.length(); i++) {
                                        JSONObject tmpObj = address.getJSONObject(i);
                                        if(tmpObj.getString("long_name").contains("County")) {
                                            location_county = tmpObj.getString("long_name");
                                            location_state = address.getJSONObject(i+1).getString("short_name");
                                        }
                                    }
                                    Intent intent = new Intent(PhoneListenerService.this, ProfileActivity.class);
                                    intent.putExtra("callingActivity", "PhoneListenerService");
                                    intent.putExtra("message_from_watch", "random");
                                    intent.putExtra("coordinates", longitude + "_" + latitude);
                                    intent.putExtra("searchMode", "random");
                                    intent.putExtra("location", location_county + ", " + location_state);
                                    intent.putExtra("zipcode", "");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                } catch(JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){

                            }
                        });
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
            }
            else {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //you need to add this flag since you're starting a new activity from a service
                intent.putExtra("callingActivity", "PhoneListenerService");
                intent.putExtra("message_from_watch", message);
                startActivity(intent);
            }

        }

        else {
            super.onMessageReceived(messageEvent);
        }

    }
}
