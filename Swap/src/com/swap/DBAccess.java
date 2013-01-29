package com.swap;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.StrictMode;

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
	
	public static int createItem(Item newItem)
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		
		StringBuilder builder = new StringBuilder();
		StringBuilder parameters = new StringBuilder();
		
		try {
			parameters.append("&title="+URLEncoder.encode(newItem.title,"utf-8"));
			parameters.append("&description="+URLEncoder.encode(newItem.description,"utf-8"));
			parameters.append("&price="+String.valueOf(newItem.price));
			if(newItem.featured)
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
			if(newItem.available)
			{
				parameters.append("&available=1" );
			}
			else
			{
				parameters.append("&available=0" );
			}
		} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
		}
		
		
		
		
		//add the rest of the properties
		
	   	
		 try {
				HttpClient client = new DefaultHttpClient();
			    HttpGet httpGet = new HttpGet("http://purple.dotgeek.org/swapapi.php?action=insertNewItem"+parameters.toString());
					
		    	HttpResponse response = client.execute(httpGet);
		    	StatusLine statusLine = response.getStatusLine();
		      
		    	int statusCode = statusLine.getStatusCode();
		    	if (statusCode == 200) {
		    		HttpEntity entity = response.getEntity();
		    		InputStream content = entity.getContent();
		    		
		    		BufferedReader  reader = new BufferedReader(new InputStreamReader(content));
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
		 	catch(Exception e)
		 	{
		 		System.out.print(e.getMessage());
		 	}
		
		//return  id if success, return -1 if failed
		return 1;
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
