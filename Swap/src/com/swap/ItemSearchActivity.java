package com.swap;

import java.util.ArrayList;
import java.util.List;

import com.swap.DBAccess.ItemsQueryOption;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class ItemSearchActivity extends ListActivity implements DBAccessDelegate {
	
	private List<Item> itemData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_item_search);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      
	      DBAccess.getItemsBySearchWithOptions(this, query, ItemsQueryOption.SEARCH);
	    }
	    else
		{			
			String[] soon = new String[] { "Error, try again" };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, soon);
			setListAdapter(adapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_item_search, menu);
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
	
	public void displayMapWithItems() {
		if (itemData != null)
		{
			ArrayList<String> locationArrayList = new ArrayList<String>(1);
			for (Item currentItem : itemData)
			{
				locationArrayList.add(currentItem.location);
			}
			
			Intent intent = new Intent(this, MapActivity.class);
			intent.putStringArrayListExtra(MapActivity.ARG_ITEMS_DATA, locationArrayList);
			startActivity(intent);
		}
	}
	
	@Override
	public void downloadedResult(List<Item> data) {
		
		if (data.size() == 0)
		{
			String[] soon = new String[] { "No results found" };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, soon);
			setListAdapter(adapter);
			return;
		}
		
		this.itemData = data;
		
		Item[] itemArray = data.toArray(new Item[data.size()]);
		
		ItemAdapter adapter = new ItemAdapter(this, R.layout.item_list_row, itemArray);
		
		setListAdapter(adapter);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {				
		Item selectedItem = itemData.get(position);
		
		Intent intent = new Intent(this, ItemDetailActivity.class);
		intent.putExtra(ItemDetailActivity.ARG_ITEM_DATA, selectedItem);
		startActivity(intent);
		
	}

}
