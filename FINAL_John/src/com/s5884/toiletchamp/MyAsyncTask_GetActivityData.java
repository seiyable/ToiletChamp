package com.s5884.toiletchamp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyAsyncTask_GetActivityData extends AsyncTask<ActivityData_Get, Void, HttpResponse> implements Serializable {
	
	private static final long serialVersionUID = -908304863152244064L;
	//in-class variables
	private Context context;
	private ListView activityListView;
	private Bitmap image;
	
	//constructor
	public MyAsyncTask_GetActivityData(Context _context, ListView _activityListView, Bitmap _image) {
		super();
		this.context = _context;
		this.activityListView = _activityListView;		
		this.image = _image;
	}
	
	@Override
	protected HttpResponse doInBackground(ActivityData_Get... _data) {
            //Access to server in POST request with parameters
        	String url = "http://toilet-champ.herokuapp.com/get_activity";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            
            //Activity data that's going to be sent to server
            String udid = _data[0].getUdid();
            //String locationFilter = _data[0].getLocationFilter();
            //String timeFilter = String.valueOf(_data[0].getTimeFilter());
            //String itemNumber = String.valueOf(_data[0].getItemNumber());
            
            Log.v("UDID", udid);
            //Log.v("locationFilter", locationFilter);
            //Log.v("timeFilter", timeFilter);
            //Log.v("itemNumber", itemNumber);
            
            ArrayList <NameValuePair> params = new ArrayList <NameValuePair>();
            params.add( new BasicNameValuePair("udid", udid));
            //params.add( new BasicNameValuePair("location", locationFilter));
            //params.add( new BasicNameValuePair("timeFilter", timeFilter));
            //params.add( new BasicNameValuePair("itemNumber", itemNumber));
            
            
            HttpResponse res = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                res = httpClient.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return res;
	}

    @Override
    protected void onPostExecute(HttpResponse _res) {

    	//parse the JSON data         
        int status = _res.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK == status) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                _res.getEntity().writeTo(outputStream);
                String jsonData;
                jsonData = outputStream.toString(); // JSON data
                JSONObject rootObject = new JSONObject(jsonData);
                JSONArray itemArray = rootObject.getJSONArray("items");
                
                //set each data into each row of listview
                List<CustomActivityData> objects = new ArrayList<CustomActivityData>();
                for (int i = itemArray.length() - 1; i >= 0; i--) {
                //for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject jsonObject = itemArray.getJSONObject(i);
            		CustomActivityData data = new CustomActivityData();
            		data.setDeleteButtonImage(image);
            		data.setRecentWhereText(jsonObject.getString("location"));
            		
            		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a   E, M'/'d'/'yyyy", Locale.US);
            		//sdf.parse("2000-01-01 00:00");
            		Date time = new Date(Long.parseLong(jsonObject.getString("timestamp"))*1000L);
            		//time = sdf.parse(sdf.format(time));
            		data.setRecentWhenText(sdf.format(time));
            		objects.add(data);
                }
                
                CustomActivityAdapter customAdapater = new CustomActivityAdapter(context, 0, objects);		
        		activityListView.setAdapter(customAdapater);
                
            } catch (Exception e) {
                  Log.d("JSON Parcing", "Error" + e);
            }
        } else {
            Log.d("JSON Parcing", "Status" + status);
            //return null;
        }
    	        
    }
}
