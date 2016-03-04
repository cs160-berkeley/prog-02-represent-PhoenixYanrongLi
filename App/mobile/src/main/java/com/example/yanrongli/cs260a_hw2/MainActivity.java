package com.example.yanrongli.cs260a_hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Button search = (Button) findViewById(R.id.main_button_search);
        search.setVisibility(View.INVISIBLE);
        EditText edit = (EditText) findViewById(R.id.main_editText);
        edit.setVisibility(View.INVISIBLE);

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

            Intent getProfileScreenIntent = new Intent(this, ProfileActivity.class);
            //final int result = 1;

            String zipcode = edit.getText().toString();
            getProfileScreenIntent.putExtra("callingActivity", "MainActivity");
            getProfileScreenIntent.putExtra("zipcode", zipcode);
            getProfileScreenIntent.putExtra("location", "");

            startActivity(getProfileScreenIntent);
        }
    }

    public void onClickLocationSearch(View view){
        Intent getProfileScreenIntent = new Intent(this, ProfileActivity.class);

        String location = "Berkeley, CA";   //********************** leave for changing by API
        getProfileScreenIntent.putExtra("callingActivity", "MainActivity");
        getProfileScreenIntent.putExtra("zipcode", "");
        getProfileScreenIntent.putExtra("location", location);
        //Toast.makeText(MainActivity.this, "XXX", Toast.LENGTH_SHORT).show();
        startActivity(getProfileScreenIntent);

    }










//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
