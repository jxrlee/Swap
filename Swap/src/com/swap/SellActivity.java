package com.swap;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class SellActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
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
		newItem.rating = 5;
		newItem.location = "00.00 00.00";
		newItem.available = true;
		newItem.sellerid = "6193257394";
		newItem.imagesnum = 0;
		
		
		int newID = DBAccess.createItem(newItem);
		
		if(newID != -1)
		{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Item created with ID="+String.valueOf(newID));
	       
		
		AlertDialog dialog = builder.create();
		
		dialog.show();
		}
		
		
	}
}
