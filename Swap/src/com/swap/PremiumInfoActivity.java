package com.swap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PremiumInfoActivity extends Activity {
	User userinfo;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userinfo = new User();
		userinfo.phone = (String) getIntent().getSerializableExtra("phoneNumber");
		setContentView(R.layout.activity_premium_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_premium_info, menu);
		return true;
	}

	public void becomePremiumUserClicked(View view){
		new BecomePremiumUserTask().execute(userinfo.phone);
	}
	
	class BecomePremiumUserTask extends AsyncTask<String, Void, String> {
		public static final String API_URL = "http://purple.dotgeek.org/swapapi.php";
		
		@Override
		protected String doInBackground(String... params) {
			
			StringBuilder builder = new StringBuilder();
			
		    HttpClient client = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(API_URL+"?action=becomePremiumUser&id="+params[0]);
		   
		       
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
			
			try
			{

				JSONObject jsonObject = new JSONObject(result);
				
				userinfo.phone = jsonObject.getString("phone");
				int x = jsonObject.getInt("premium");
				if(x ==1 && jsonObject.getString("startdate") != null)
				{
					Toast msg = Toast.makeText(getApplicationContext(), "Thank You! you are now a PREMIUM member", Toast.LENGTH_LONG);
		    	    msg.show();
				}
				else
				{
					Toast msg = Toast.makeText(getApplicationContext(), "Sorry, unable to process your request\nPlease try again later.", Toast.LENGTH_LONG);
		    	    msg.show();
				}
				
			}
			catch (Exception e) {
				Log.e("Userinfo", "User info retrieval failed" + e.getMessage());
				e.printStackTrace();
			}
			
	    }
	}
}
