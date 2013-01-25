package com.swap;

import java.text.SimpleDateFormat;
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
	
}
