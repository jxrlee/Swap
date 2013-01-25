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

import android.os.AsyncTask;

public class HTTPDownloadTask extends AsyncTask<HTTPDownloadTaskArgument, Void, String> {

	DBAccessDelegate delegate;
	
	@Override
	protected String doInBackground(HTTPDownloadTaskArgument... args) {
				
		StringBuilder builder = new StringBuilder();
		
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet(args[0].url);
	    
	    this.delegate = args[0].delegate;
	    
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
	
	@Override
	protected void onPostExecute(String result) {
       DBAccess.parseItemDownload(delegate, result);
    }

}