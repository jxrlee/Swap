package com.swap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.StrictMode;

public class DBAccess {

	private static DBAccess instance = null;
	
	protected DBAccess() {
		// Exists only to defeat instantiation.
	}
	
	public static DBAccess getInstance() {
		if(instance == null) {
			instance = new DBAccess();
			
			// TODO: remove this!
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
		}
		return instance;
	}
	
	public List<Item> getAllItems()
	{
		List<Item> list = new ArrayList<Item>();
		
		String rawJSON = this.downloadJSONFile("http://purple.dotgeek.org/json.php");
		
		try {
			JSONArray jsonArray = new JSONArray(rawJSON);

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
		
		return list;
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
	
	private String downloadJSONFile(String url)
	{
		// TODO: download in background thread
		
		StringBuilder builder = new StringBuilder();
		
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(url);
	    
	    try {
	    	HttpResponse response = client.execute(httpGet);
	    	StatusLine statusLine = response.getStatusLine();
	      
	    	int statusCode = statusLine.getStatusCode();
	    	if (statusCode == 200) {
	    		HttpEntity entity = response.getEntity();
	    		InputStream content = entity.getContent();
	    		
	    		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	    		String line;
	    		while ((line = reader.readLine()) != null) {
	    			builder.append(line);
	    		}
	      }
	      else {
	    	  //fail
	      }
	    }
	    catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    }
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    return builder.toString();
	}
	
	
}
