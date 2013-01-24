package com.swap;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBAccess {
	
	public static void getAllItems(DBAccessDelegate delegate)
	{
		
		HTTPDownloadTaskArgument arg = new HTTPDownloadTaskArgument();
		arg.delegate = delegate;
		arg.url = "http://purple.dotgeek.org/swapapi.php?action=getAllItems";
		
		new HTTPDownloadTask().execute(arg);
	}
	
	public List<Item> getFeaturedItems()
	{
		return null;
	}
	
	public List<Item> getItemsByUser(String userId)
	{
		return null;
	}
	
	public List<Item> getItemsBySearchText(String text)
	{
		return null;
	}
	
	public Item getItemById(int id)
	{
		return null;
	}
	
	public int createItem(Item newItem)
	{
		//return  id if success, return -1 if failed
		return -1;
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
	
	public static void parseDownload(DBAccessDelegate delegate, String data)
	{
		List<Item> list = new ArrayList<Item>();
				
		try {
			JSONArray jsonArray = new JSONArray(data);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				Item currentItem = new Item();
				currentItem.title = jsonObject.getString("title");
				// TODO: add in rest of fields
				
				list.add(currentItem);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		delegate.downloadedResult(list);
	}
	
}
