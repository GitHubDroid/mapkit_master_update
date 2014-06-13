package com.promptnet.mobiledev.mapkit.mapdrawer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.promptnet.mobiledev.mapkit.R;

public class MapDrawerActivity extends Activity {
	
	private static final String TAG = "MapDrawerActivity";
	private static final int REQUEST_CODE = 6384;  //onActivityResult request
												   // code
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //Create a simple button to start the file browser process
		
      		Button btnbrowser = new Button(this);
      		btnbrowser.setText(R.string.choose_pdfmap);
      		btnbrowser.setOnClickListener(new OnClickListener() {
      			@Override
      			public void onClick(View v) {
      				//Display the File Browser Dialog
      				showFileBrowser();
      			}
      		});
      		
      		setContentView(btnbrowser);
      		
      	}
      	
      	
      	private void showFileBrowser() {
              // Use the GET_CONTENT intent from the utility class
              Intent target = FileUtils.createGetContentIntent();
              // Create the chooser Intent
              Intent intent = Intent.createChooser(
                      target, getString(R.string.chooser_title));
              try {
                  startActivityForResult(intent, REQUEST_CODE);
              } catch (ActivityNotFoundException e) {
                  // The reason for the existence of aFileChooser
              }
          }
      	@Override
          protected void onActivityResult(int requestCode, int resultCode, Intent data) {
              switch (requestCode) {
                  case REQUEST_CODE:
                      // If the file selection was successful
                      if (resultCode == RESULT_OK) {
                          if (data != null) {
                              // Get the URI of the selected file
                              final Uri uri = data.getData();
                              Log.i(TAG, "Uri = " + uri.toString());
                              try {
                                  // Get the file path from the URI
                                  final String path = FileUtils.getPath(this, uri);
                                  Toast.makeText(MapDrawerActivity.this,
                                          "File Selected: " + path, Toast.LENGTH_LONG).show();
                              } catch (Exception e) {
                                  Log.e("FileSelectorActivity", "Map File select error", e);
                              }
                          }
                      }
                      break;
              }
              super.onActivityResult(requestCode, resultCode, data);
          }
   
}
