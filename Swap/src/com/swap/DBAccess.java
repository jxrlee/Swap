package com.swap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.swap.HTTPDownloadTaskArgument.Task;

public class DBAccess {
	
	public static final String API_URL = "http://purple.dotgeek.org/swapapi.php";
	public static final String ALL_ITEMS_ACTION = "getAllItems";
	public static final String SEARCH_ACTION = "getItemsBySearch";
	public static final String NEW_ITEM_ACTION = "insertNewItem";
	public static final String ITEMS_BY_ID_ACTION = "getItemsById";
	public static final String HISTORY_ITEMS_BY_ID_ACTION = "getHistoryItemsByUserId";
	
	public enum ItemsQueryOption {
		RECENT(0),
		PRICE(1),
		FEATURED(2),
		NEARBY(3);
		
		private final int index;   

		ItemsQueryOption(int index) {
	        this.index = index;
	    }

	    public int index() { 
	        return index; 
	    }
	}
	
	public static void getAllItems(DBAccessDelegate delegate)
	{
		
		HTTPDownloadTaskArgument arg = new HTTPDownloadTaskArgument();
		arg.delegate = delegate;
		arg.url = API_URL + "?action=" + ALL_ITEMS_ACTION;
		arg.task = Task.RETRIEVAL;
		
		new HTTPDownloadTask().execute(arg);
	}
	
	public static void getItemsBySearchWithOptions(DBAccessDelegate delegate, String search, ItemsQueryOption option)
	{
		
		HTTPDownloadTaskArgument arg = new HTTPDownloadTaskArgument();
		arg.delegate = delegate;
		arg.url = API_URL + "?action=" + SEARCH_ACTION + "&option=" + Integer.toString(option.index());
		arg.task = Task.RETRIEVAL;
		
		new HTTPDownloadTask().execute(arg);
	}
	
	public Item getItemById(int id)
	{
		return null;
	}
	
	public static void createItem(DBAccessDelegate delegate, Item newItem)
	{
		
		StringBuilder parameters = new StringBuilder();
		
		try
		{
			parameters.append("&title="+URLEncoder.encode(newItem.title,"utf-8"));
			parameters.append("&description="+URLEncoder.encode(newItem.description,"utf-8"));
			parameters.append("&price="+String.valueOf(newItem.price));
			if (newItem.featured)
			{
				parameters.append("&featured=1" );
			}
			else
			{
				parameters.append("&featured=0" );
			}
			parameters.append("&rating="+String.valueOf(newItem.rating));
			parameters.append("&imagesnum="+String.valueOf(newItem.imagesnum));
			parameters.append("&location="+URLEncoder.encode(newItem.location,"utf-8"));
			parameters.append("&sellerid="+newItem.sellerid);
			if (newItem.available)
			{
				parameters.append("&available=1" );
			}
			else
			{
				parameters.append("&available=0" );
			}
		}
		catch (UnsupportedEncodingException e1)
		{
			e1.printStackTrace();
		}
		
		
		HTTPDownloadTaskArgument arg = new HTTPDownloadTaskArgument();
		arg.delegate = delegate;
		arg.url = API_URL + "?action=" + NEW_ITEM_ACTION + parameters.toString();
		arg.task = Task.INSERT;
		
		new HTTPDownloadTask().execute(arg);
		
	}
	
	public boolean updateItem(Item item)
	{
		return true;
	}
	
	public boolean deleteItem(int id)
	{
		return true;
	}
	
	public boolean createNewUser(User newUser)
	{
		return true;
	}
	
	public static void parseItemDownload(DBAccessDelegate delegate, String data)
	{
		List<Item> list = new ArrayList<Item>();
				
		try {
			JSONArray jsonArray = new JSONArray(data);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				Item currentItem = new Item();
				currentItem.id = jsonObject.getInt("id");
				currentItem.title = jsonObject.getString("title");
				currentItem.price = (float)jsonObject.getDouble("price");
				currentItem.description = jsonObject.getString("description");
				currentItem.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonObject.getString("date"));
				currentItem.featured = (jsonObject.getInt("featured") == 1) ? true : false;
				currentItem.rating = jsonObject.getInt("rating");
				currentItem.location = jsonObject.getString("location");
				currentItem.available = (jsonObject.getInt("available") == 1) ? true : false;
				currentItem.sellerid = jsonObject.getString("sellerid");
				currentItem.imagesnum = jsonObject.getInt("imagesnum");
				
				list.add(currentItem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		delegate.downloadedResult(list);
	}
	
	public static void parseItemInsert(DBAccessDelegate delegate, String data)
	{
		List<Item> list = new ArrayList<Item>();
		
		try
		{
			JSONObject jsonObject = new JSONObject(data);
					
			Item currentItem = new Item();
			currentItem.id = jsonObject.getInt("id");
			
			list.add(currentItem);
		}
		catch (Exception e) {
			Log.e("DBAccess", "PARSE ITEM INSERT CATCH" + e.getMessage());
			e.printStackTrace();
		}
		
		
		delegate.downloadedResult(list);
		
	}

	public static void getItemsByUser(
			DBAccessDelegate delegate, String userid) {
		
		HTTPDownloadTaskArgument arg = new HTTPDownloadTaskArgument();
		arg.delegate = delegate;
		arg.url = API_URL + "?action=" + ITEMS_BY_ID_ACTION + "&id="+userid;
		arg.task = Task.RETRIEVAL;
		
		new HTTPDownloadTask().execute(arg);
	}

	public static void getHistoryItemsByUserId(
			DBAccessDelegate delegate, String userid) {
		HTTPDownloadTaskArgument arg = new HTTPDownloadTaskArgument();
		arg.delegate = delegate;
		arg.url = API_URL + "?action=" + HISTORY_ITEMS_BY_ID_ACTION + "&id="+userid;
		arg.task = Task.RETRIEVAL;
		
		new HTTPDownloadTask().execute(arg);
		
	}
	
}
