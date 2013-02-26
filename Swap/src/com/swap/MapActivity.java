package com.swap;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}
	
	 @Override
	 public void onWindowFocusChanged(boolean hasFocus) {
		 super.onWindowFocusChanged(hasFocus);
		 loadMap();
	 }
	 
	 private void loadMap() {
		 ImageView mapImageView = (ImageView) findViewById(R.id.mapImage);
		 int mapImageWidth = mapImageView.getWidth();
		 int mapImageHeight = mapImageView.getHeight();
			
		 String mapAPIURL = "http://maps.googleapis.com/maps/api/staticmap?sensor=true&key=AIzaSyA1S4o8Mr8CQMX3AvWGiTemQ9Z9HMwlyiw&size=" + Integer.toString(mapImageWidth) + "x" + Integer.toString(mapImageHeight);
		 
		 String longitude = "32.88106";
		 String latitude = "-117.23755";
		 mapAPIURL += "&markers=" + longitude + "," + latitude;
		 
		 ImageDownloader mDownload = ImageDownloader.getInstance();
		 mDownload.download(mapAPIURL, mapImageView);
	 }

}
