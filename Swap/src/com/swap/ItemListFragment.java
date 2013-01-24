package com.swap;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemListFragment extends ListFragment implements DBAccessDelegate {
	
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DBAccess.getAllItems(this);
		
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO: go to next screen when item is selected
	}


	@Override
	public void downloadedResult(List<Item> data) {
		
		List<String> stringsToDisplay = new ArrayList<String>();
		for(int i = 0; i < data.size(); i++) {
			stringsToDisplay.add( data.get(i).title );
			// TODO: add rest of fields
		}
		
		String[] stringVersion = stringsToDisplay.toArray(new String[stringsToDisplay.size()]);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, stringVersion);
		setListAdapter(adapter);
		
	}
}
