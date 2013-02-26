package com.swap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.swap.HTTPDownloadTaskArgument.Task;

import android.os.AsyncTask;
import android.util.Log;

public class HTTPDownloadTask extends AsyncTask<HTTPDownloadTaskArgument, Void, String> {

	HTTPDownloadTaskArgument argument;
	
	@Override
	protected String doInBackground(HTTPDownloadTaskArgument... args) {
				
		StringBuilder builder = new StringBuilder();
		
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(args[0].url);
	    
	    this.argument = args[0];
	    
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
	    		
				// valid ID, item was inserted correctly... now start to upload
				// pictures
				if (argument.task == Task.INSERT) {
					JSONObject jsonObject = new JSONObject(builder.toString());
					
					if (jsonObject.getInt("id") != -1) {
							UploadImagesTask upload = new UploadImagesTask();
							upload.id = jsonObject.getInt("id");
							upload.start();
					}
				}
	      }
	      else {
	    	  Log.e("HTTPDownloadTask", "Http Response Error :(");
	      }
	    }
	    catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return builder.toString();
			    
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		if (argument.task == Task.RETRIEVAL)
		{
			DBAccess.parseItemDownload(argument.delegate, result);
		}
		else if (argument.task == Task.INSERT)
		{
			DBAccess.parseItemInsert(argument.delegate, result);
		}
		else if (argument.task == Task.UPDATE)
		{
			DBAccess.parseItemUpdate(argument.delegate, result);
		}
    }

}