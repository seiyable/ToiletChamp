package com.s5884.toiletchamp_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MyAsyncTask extends AsyncTask<ActivityData, Void, Void> implements Serializable {
	
	private static final long serialVersionUID = -908304863152244064L;
	//in-class variables
	private TextView textView;
	
	//constructor
	public MyAsyncTask(TextView _textView) {
		super();
		this.textView = _textView;		
	}
	
	@Override
	protected Void doInBackground(ActivityData... _data) {
            //Access to server in POST request with parameters
        	String url = "http://toilet-champ.herokuapp.com/add_activity";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            
            //Activity data that's going to be sent to server
            String udid = _data[0].getUdid();
            String location = _data[0].getLocation();
            String timestamp = String.valueOf(_data[0].getTimestamp());
            
            Log.v("UDID", udid);
            Log.v("location", location);
            Log.v("timestamp", timestamp);
            
            ArrayList <NameValuePair> params = new ArrayList <NameValuePair>();
            params.add( new BasicNameValuePair("udid", udid));
            params.add( new BasicNameValuePair("location", location));
            params.add( new BasicNameValuePair("timestamp", timestamp));
            
            HttpResponse res = null;
            
            try {
                post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                res = httpClient.execute(post);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
	}

    @Override
    protected void onPostExecute(Void v) {
    	String str = "activity data posted!";
        textView.setText(str);
        
    }
}
