package com.promptnet.mobiledev.mapkit;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.promptnet.mobiledev.mapkit.mapdrawer.MapDrawerActivity;
import com.promptnet.mobiledev.mapkit.notes.NotesActivity;

public class DashboardActivity2 extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
                 
        
        
        /**
         * Creating all buttons instances
         * */
        // Dashboard Map button
        Button btn_map = (Button) findViewById(R.id.btn_map);
         
        // Dashboard POI Search button
        Button btn_search = (Button) findViewById(R.id.btn_search);
         
        // Dashboard Map Drawer button
        Button btn_mapdrawer = (Button) findViewById(R.id.btn_mapdrawer);
         
        // Dashboard Notes button
        Button btn_notes = (Button) findViewById(R.id.btn_notes);
         
        // Dashboard About button
        Button btn_about = (Button) findViewById(R.id.btn_about);
         
        // Dashboard Exit button
        Button btn_exit = (Button) findViewById(R.id.btn_exit);
        
               /**
         * Handling all button click events
         * */
         
        // Listening to Map View button click
        btn_map.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching Map View Screen
                Intent i = new Intent(getApplicationContext(), GPSLocation.class);
                startActivity(i);
            }
        });
         
       // Listening POI Search button click
        btn_search.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching POI Search Screen
                Intent i = new Intent(getApplicationContext(), AddressSearch.class);
                startActivity(i);
            }
        });
         
        // Listening mapdrawer button click
        btn_mapdrawer.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching mapdrawer Screen
                Intent i = new Intent(getApplicationContext(), MapDrawerActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to notebook button click
        btn_notes.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching notebook Screen
                Intent i = new Intent(getApplicationContext(), NotesActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to About button click
        btn_about.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                // Launching About Screen
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
            }
        });
         
        // Listening to Exit button click
        btn_exit.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View view) {
                //  Exit
               finish();
            System.exit(0);
            }
        });
    }
}