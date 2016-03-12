package com.example.yanrongli.cs260a_hw2;

/**
 * Created by yanrongli on 2/28/16.
 */
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String NAME_LIST = "/name_list";
    private static final String PARTY_LIST = "/party_list";
    private static final String MY_2012_VOTE = "/2012vote";
    private static final String START_INTENT = "/start_intent";
    private static String name_list = "No_name_received";
    private static String party_list = "No_party_received";
    private static String my2012vote = "No_location_received";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        if( messageEvent.getPath().equalsIgnoreCase( NAME_LIST ) ) {
            name_list = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        }
        else if (messageEvent.getPath().equalsIgnoreCase( PARTY_LIST )) {
            party_list = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        }
        else if (messageEvent.getPath().equalsIgnoreCase( MY_2012_VOTE )) {
            my2012vote = new String(messageEvent.getData(), StandardCharsets.UTF_8);
        }
        else if (messageEvent.getPath().equalsIgnoreCase( START_INTENT )){
            Intent intent = new Intent(this, SwipeActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("name_list", name_list);
            intent.putExtra("party_list", party_list);
            intent.putExtra("2012vote", my2012vote);
            //System.out.println(name_list);
            //System.out.println(party_list);
            //System.out.println(my2012vote);
            Log.d("T", "about to start watch MainActivity with new profile list");
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
