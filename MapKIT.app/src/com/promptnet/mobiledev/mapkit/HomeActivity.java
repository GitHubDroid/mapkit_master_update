package com.promptnet.mobiledev.mapkit;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;

public class HomeActivity extends Activity {
	GridView gridView;
	ArrayList<HomeItem> gridArray = new ArrayList<HomeItem>();
	 CustomGridViewAdapter customGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		//set grid view item
		Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.home);
		Bitmap MapViewIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.map);
		
		gridArray.add(new HomeItem(homeIcon,"Home"));
		gridArray.add(new HomeItem(MapViewIcon,"MapView"));
		gridArray.add(new HomeItem(homeIcon,"House"));
//		gridArray.add(new HomeItem(userIcon,"Friend"));
//		gridArray.add(new HomeItem(homeIcon,"Home"));
//		gridArray.add(new HomeItem(userIcon,"Personal"));
//		gridArray.add(new HomeItem(homeIcon,"Home"));
//		gridArray.add(new HomeItem(userIcon,"User"));
//		gridArray.add(new HomeItem(homeIcon,"Building"));
//		gridArray.add(new HomeItem(userIcon,"User"));
//		gridArray.add(new HomeItem(homeIcon,"Home"));
//		gridArray.add(new HomeItem(userIcon,"xyz"));
		
		
		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.home_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
	}

}
