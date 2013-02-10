package com.swap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SellActivity extends Activity implements DBAccessDelegate {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	private Uri fileUri;
	private Location LastLocation = null;
	
	private static LocationListener locationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
		startListeningGPS();
		
	    
	}

	private void startListeningGPS() {
		LocationManager locationManager =
		        (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		LocationProvider provider =
		        locationManager.getProvider(LocationManager.GPS_PROVIDER);
		
		final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!gpsEnabled) {
	        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	        startActivity(settingsIntent);
	    }
	    
	    locationListener = new LocationListener() {
	    	@Override  
	    	public void onLocationChanged(Location location) {
	    	    Toast msg = Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT);
	    	    msg.show();
	    		LastLocation = location;
	    	  }
	    	@Override
	    	public void onProviderEnabled(String provider)
	    	{}
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
	    };
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		
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
		if(LastLocation != null)
		{
			newItem.location = String.valueOf(LastLocation.getLatitude()) + " " + String.valueOf(LastLocation.getLongitude());
		}
		else
		{
			newItem.location = "0.0 0.0";
		}
		
		newItem.available = true;
		newItem.sellerid = getPhoneNumber(10);
		newItem.imagesnum = 0;
		
		
		DBAccess.createItem(this, newItem);
		
	}
	
	private String getPhoneNumber(int digitCount) {

	    TelephonyManager mTelephonyMgr;
	    mTelephonyMgr = (TelephonyManager)
	        getSystemService(Context.TELEPHONY_SERVICE); 
	    String num = mTelephonyMgr.getLine1Number();
	    if(num!=null && num.length()>digitCount)
	    {
	    	num= num.substring(num.length()-digitCount);
	    }
	    return num;
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
	
	public void launchCamera(View view)
	{
		
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	    
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            //Toast.makeText(this, "Image saved to:\n" +  data.getData(), Toast.LENGTH_LONG).show();
	            ImageView image= (ImageView)findViewById(R.id.imageView1);
	    	    image.setImageURI(fileUri);
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	 }

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.isDirectory()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
}