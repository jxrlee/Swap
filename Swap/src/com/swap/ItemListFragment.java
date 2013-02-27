package com.swap;

import java.util.ArrayList;
import java.util.List;

import com.swap.DBAccess.ItemsQueryOption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemListFragment extends ListFragment implements DBAccessDelegate {
	
	public static final String ARG_SECTION_NUMBER = "section_number"; 
	
	private int fragmentSectionNumber;
	private List<Item> itemData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragmentSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

		if (fragmentSectionNumber == 1)
		{
			DBAccess.getAllItems(this);
		}
		else if (fragmentSectionNumber == 2)
		{
			DBAccess.getItemsBySearchWithOptions(this, "search", ItemsQueryOption.PRICE);
		}
		else
		{			
			String[] soon = new String[] { "Coming soon..." };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			        android.R.layout.simple_list_item_1, soon);
			setListAdapter(adapter);
		}
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {				
		Item selectedItem = itemData.get(position);
		
		Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
		intent.putExtra(ItemDetailActivity.ARG_ITEM_DATA, selectedItem);
		startActivity(intent);
		
	}
	
	public void displayMapWithItems() {
		if (itemData != null)
		{
			ArrayList<String> locationArrayList = new ArrayList<String>(1);
			for (Item currentItem : itemData)
			{
				locationArrayList.add(currentItem.location);
			}
			
			Intent intent = new Intent(getActivity(), MapActivity.class);
			intent.putStringArrayListExtra(MapActivity.ARG_ITEMS_DATA, locationArrayList);
			startActivity(intent);
		}
	}


	@Override
	public void downloadedResult(List<Item> data) {
		
		this.itemData = data;
		
		Item[] itemArray = data.toArray(new Item[data.size()]);
		
		ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.item_list_row, itemArray);
		
		setListAdapter(adapter);
		
	}
}
