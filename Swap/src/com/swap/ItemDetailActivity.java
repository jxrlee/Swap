package com.swap;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class ItemDetailActivity extends Activity {
	
	public static final String IMAGES_FOLDER = "http://purple.dotgeek.org/swapimages/";
	public static final String ARG_ITEM_DATA = "item_data"; 
	
	private Item itemData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//getActionBar().hide();
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		
		ImageView view = (ImageView) findViewById(R.id.imageView);
		view.getLayoutParams().width = width;
		view.getLayoutParams().height = width;
		
		
		
		itemData = (Item) getIntent().getSerializableExtra(ARG_ITEM_DATA);
		
		TextView distanceView = (TextView) findViewById(R.id.distanceView);
		distanceView.setText("0.5 miles");
		
		TextView priceView = (TextView) findViewById(R.id.priceView);
		priceView.setText("$" + ItemAdapter.dollarFormat(itemData.price));
		
		TextView titleView = (TextView) findViewById(R.id.titleView);
		titleView.setText(itemData.title);
		
		TextView descriptionView = (TextView) findViewById(R.id.descriptionView);
		descriptionView.setText(itemData.description);
		
		if (itemData.imagesnum > 0)
		{
			ImageView imageView = (ImageView) findViewById(R.id.imageView);
			
			ImageDownloader mDownload = ImageDownloader.getInstance();
			mDownload.download(IMAGES_FOLDER + Integer.toString(itemData.id) + "_1.jpg", imageView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void contactButtonTap(View view) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);

		intent.setData(Uri.parse("sms:" + itemData.sellerid));
		intent.putExtra("sms_body", "Hello I'm interested in your " + itemData.title);
        intent.putExtra("compose_mode", true);
		startActivity(intent);
	}

}
