package com.swap;

import java.util.List;

import com.swap.DBAccess.ItemsQueryOption;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
		else if (fragmentSectionNumber == 3)
		{
			DBAccess.getItemsBySearchWithOptions(this, "search", ItemsQueryOption.FEATURED);
		}
		else
		{
			// TODO: data downloading for other pages
			
			String[] soon = new String[] { "Coming soon..." };
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			        android.R.layout.simple_list_item_1, soon);
			setListAdapter(adapter);
		}
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO: go to next screen when item is selected
				
		Item selectedItem = itemData.get(position);
		
		Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
		intent.putExtra(ItemDetailActivity.ARG_ITEM_DATA, selectedItem);
		startActivity(intent);
		
	}


	@Override
	public void downloadedResult(List<Item> data) {
		
		this.itemData = data;
		
		/*
		List<String> stringsToDisplay = new ArrayList<String>();
		for(int i = 0; i < data.size(); i++) {
			stringsToDisplay.add( data.get(i).title );
		}
		
		String[] stringVersion = stringsToDisplay.toArray(new String[stringsToDisplay.size()]);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, stringVersion);
		        
		*/
		
		Item[] itemArray = data.toArray(new Item[data.size()]);
		
		ItemAdapter adapter = new ItemAdapter(getActivity(), R.layout.item_list_row, itemArray);
		
		setListAdapter(adapter);
		
	}
}
