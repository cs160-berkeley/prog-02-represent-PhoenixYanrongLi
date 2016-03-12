package com.example.yanrongli.cs260a_hw2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

/**
 * Created by yanrongli on 2/28/16.
 */
public class SwipeActivity extends FragmentActivity implements SwipeFragment.OnFragmentInteractionListener {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    ViewPager viewPager;
    String[] NameList, PartyList;
    String County;
    String State;
    String ObamaVote;
    String RomneyVote;

    public void onFragmentInteraction(Uri uri){

    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String tempNameList = extras.getString("name_list");
            String tempPartyList = extras.getString("party_list");
            String temp2012Vote = extras.getString("2012vote");
            if(tempNameList != null) {
                NameList = tempNameList.split("_");
            }
            if(tempPartyList != null) {
                PartyList = tempPartyList.split("_");
            }
            County = temp2012Vote.split("_")[0];
            State = temp2012Vote.split("_")[1];
            ObamaVote = temp2012Vote.split("_")[2];
            RomneyVote = temp2012Vote.split("_")[3];
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), NameList, PartyList, County, State, ObamaVote, RomneyVote);
        viewPager.setAdapter(swipeAdapter);

        //setAmbientEnabled();

        //Shake related
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(SwipeActivity.this, "Shake!", Toast.LENGTH_SHORT).show();

                String random = "random";
                Intent sendMessageRandom = new Intent(SwipeActivity.this, WatchToPhoneService.class);
                sendMessageRandom.putExtra("string", random);
                startService(sendMessageRandom);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

}