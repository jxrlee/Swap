package com.swap;

import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;


public class UploadImagesTask {
	public int id;
	public String urlServer = "http://purple.dotgeek.org/newupload.php";
	private InputStream is;

	public void start() {

		try {
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"MyCameraApp");

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File directory, String fileName) {
					return fileName.endsWith(".jpg");
				}
			};

			File[] files = mediaStorageDir.listFiles(filter);

			for (int i = 0; i < files.length; i++) {

				Bitmap bitmapOrg = BitmapFactory.decodeFile(files[i].getPath());

				ByteArrayOutputStream bao = new ByteArrayOutputStream();

				bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 10, bao);

				byte[] ba = bao.toByteArray();

				String ba1 = Base64.encodeBytes(ba);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);

				nameValuePairs.add(new BasicNameValuePair("image", ba1));
				nameValuePairs.add(new BasicNameValuePair("id", String
						.valueOf(id)));
				nameValuePairs.add(new BasicNameValuePair("filename", String
						.valueOf(id) + "_" + String.valueOf(i + 1)));

				try {

					HttpClient httpclient = new DefaultHttpClient();

					HttpPost httppost = new HttpPost(urlServer);

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);

					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					 OutputStream out = new FileOutputStream(new
					 File(Environment.getExternalStoragePublicDirectory(
					 Environment.DIRECTORY_PICTURES),
					 "MyCameraApp/result"+files[i].getName()+".txt"));
					
					 int read = 0;
					 byte[] bytes = new byte[1024];
					
					 while ((read = is.read(bytes)) != -1) {
					 out.write(bytes, 0, read);
					 }
					
					 is.close();
					 out.flush();
					 out.close();

				} catch (Exception e) {

					Log.e("image_upload",
							"Error in http connection: " + e.getMessage());

				}

				files[i].delete();
			}
		} catch (Exception ex) {
			Log.e("image_upload",
					"Error during image upload: " + ex.getMessage());
		}

	}

}
