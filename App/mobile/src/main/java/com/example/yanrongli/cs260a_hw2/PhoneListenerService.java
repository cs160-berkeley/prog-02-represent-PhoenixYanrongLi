package com.example.yanrongli.cs260a_hw2;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    //private static final String TOAST = "/send_toast";
    private static final String MESSAGE = "/message_from_watch";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        //Modify this whole things to start DetailedActivity using data from ProfileActivity

        if( messageEvent.getPath().equalsIgnoreCase(MESSAGE) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("callingActivity", "PhoneListenerService");
            intent.putExtra("message_from_watch", message);
            startActivity(intent);

        }

        else {
            super.onMessageReceived(messageEvent);
        }

    }
}
