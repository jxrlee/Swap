package com.swap;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SellActivity extends Activity implements DBAccessDelegate {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
		
		LocationManager locationManager =
		        (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		LocationProvider provider =
		        locationManager.getProvider(LocationManager.GPS_PROVIDER);
		
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!gpsEnabled) {
	        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	        startActivity(settingsIntent);
	    }
	    
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sell, menu);
		return true;
	}

	public void sendButtonClicked(View view)
	{
		Item newItem = new Item();
		
		EditText tmp = (EditText)findViewById(R.id.txtTitle);
		newItem.title = tmp.getText().toString();
		
		tmp = (EditText)findViewById(R.id.txtPrice);
		newItem.price = Float.parseFloat(tmp.getText().toString());
		
		tmp = (EditText)findViewById(R.id.txtDescription);
		newItem.description = tmp.getText().toString();
		
		CheckBox featured = (CheckBox)findViewById(R.id.checkFeatured);
		newItem.featured = featured.isChecked();
		
		// TODO: UI for these properties
		newItem.rating = 5;
		newItem.location = "00.00 00.00";
		newItem.available = true;
		newItem.sellerid = "6193257394";
		newItem.imagesnum = 0;
		
		
		DBAccess.createItem(this, newItem);
		
	}
	
	public void downloadedResult(List<Item> data)
	{
		Item newItem = data.get(0);
		
		if (newItem.id != -1)
		{
			// TODO: we have a good result, do something
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Item created with ID="+String.valueOf(newItem.id));
	       
		
			AlertDialog dialog = builder.create();
		
			dialog.show();
		}
	}
}
