package com.swap;

import java.util.List;

import com.swap.DBAccess.ItemsQueryOption;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class HomeActivity extends Activity implements DBAccessDelegate {
	
	static int ITEMS_IN_GRID = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/Lobster.otf");
        TextView tv = (TextView) findViewById(R.id.HomeLogo);
        tv.setTypeface(tf);
        
        tf = Typeface.createFromAsset(getAssets(),
                "fonts/Cabin-Bold-TTF.ttf");
        tv = (TextView) findViewById(R.id.button_buy);
        tv.setTypeface(tf);
        tv = (TextView) findViewById(R.id.button_sell);
        tv.setTypeface(tf);
        
        DBAccess.getItemsBySearchWithOptions(this, " ", ItemsQueryOption.FEATURED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}
	
	public void buyButtonTap(View view) {
		Intent intent = new Intent(this, BuyActivity.class);
		startActivity(intent);
	}
	
	public void sellButtonTap(View view) {
		Intent intent = new Intent(this, SellSummaryActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void downloadedResult(List<Item> data) {
		
		// Fill in featured grid with images
		ImageLoader imageLoader = new ImageLoader(this);
		int itemsWithImagesDrawn = 0;
		int i = 0;
		
		while (itemsWithImagesDrawn < ITEMS_IN_GRID && i < data.size())
		{
			final Item item = data.get(i);
			
			if (item.imagesnum > 0)
			{
				int resID = getResources().getIdentifier("thumb" + (itemsWithImagesDrawn + 1), "id", "com.swap");
				ImageView imageInGrid = (ImageView) findViewById(resID);
	        	imageLoader.DisplayImage(ItemDetailActivity.IMAGES_FOLDER + Integer.toString(item.id) + "_1.jpg", imageInGrid);
	        	
	        	imageInGrid.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
						intent.putExtra(ItemDetailActivity.ARG_ITEM_DATA, item);
						startActivity(intent);
					}
					
				});
	        	
	        	itemsWithImagesDrawn++;
			}
			
			i++;
		}
		
	}


}
