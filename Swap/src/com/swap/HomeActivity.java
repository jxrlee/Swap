package com.swap;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends Activity {

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


}
