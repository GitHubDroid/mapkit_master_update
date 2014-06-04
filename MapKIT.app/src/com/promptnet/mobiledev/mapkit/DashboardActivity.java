package com.promptnet.mobiledev.mapkit;

import java.util.ArrayList;

import com.promptnet.mobiledev.mapkit.locator.MapKITLocatorActivity;
import com.promptnet.mobiledev.mapkit.track.GPSTrackingActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class DashboardActivity extends Activity {
	
		
	GridView gridView;
	ArrayList<DashItem> gridArray = new ArrayList<DashItem>();
	 CustomGridViewAdapter customGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		
		//set grid view item

		Bitmap MapViewIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.map);
		Bitmap SearchIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.search);
		Bitmap SettingsIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.settings);
		Bitmap NoteIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.notebook);
		Bitmap MapDrawerIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.mapdrawer);
		Bitmap EmailIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.mail);
		Bitmap AboutIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.about);
		Bitmap ExitIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.exit);
		
		gridArray.add(new DashItem(MapViewIcon,"Map View"));
		gridArray.add(new DashItem(SearchIcon, "GPS Info"));
		gridArray.add(new DashItem(NoteIcon, "Take Note"));
		gridArray.add(new DashItem(MapDrawerIcon, "Map Drawer"));
		gridArray.add(new DashItem(SettingsIcon, "Settings"));
		gridArray.add(new DashItem(EmailIcon, "Email"));
        gridArray.add(new DashItem(AboutIcon, "About"));
        gridArray.add(new DashItem(ExitIcon, "Exit"));
	
		
		
		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.dash_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			//Dash Board Menu Stuff

            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

                switch (position) {
                //Map View
                case 0:

                    Intent i = new Intent(getApplicationContext(), MapKITMapActivity.class);
                    i.putExtra("id", position);
                    startActivity(i);
                    break;
                // Search
                case 1:
                   
                    Intent j = new Intent(getApplicationContext(), GPSTrackingActivity.class);
                    j.putExtra("id", position);
                    startActivity(j);
                    break;
//                case 2:
//                    //Use some different intent here
//                    Intent k = new Intent(getApplicationContext(), );
//                    k.putExtra("id", position);
//                    startActivity(k);
//                   break;                    
//                  case 3:
//                  //Use some different intent here
//                  Intent l = new Intent(getApplicationContext(), );
//                  l.putExtra("id", position);
//                  startActivity(l);
//                  break;
//                  case 4:
//                  //Use some different intent here
//                  Intent m = new Intent(getApplicationContext(), );
//                  m.putExtra("id", position);
//                  startActivity(m);
//                  break;
//                  case 5:
//                  //Use some different intent here
//                  Intent n = new Intent(getApplicationContext(), );
//                  n.putExtra("id", position);
//                  startActivity(n);
//                  break;
//                  case 6:
//                  //Use some different intent here
//                  Intent o = new Intent(getApplicationContext(), );
//                  o.putExtra("id", position);
//                  startActivity(o);
//                  break;
                  case 7:
                  //Exit Button
                 
                  finish();
                  break;
                
                default:
                    break;
                }



            }
            });
			
	            
		}
}
		
		
	
