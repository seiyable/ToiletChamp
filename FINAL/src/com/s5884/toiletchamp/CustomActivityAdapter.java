package com.s5884.toiletchamp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomActivityAdapter extends ArrayAdapter<CustomActivityData> {
	 private LayoutInflater layoutInflater_;
	 
	 public CustomActivityAdapter(Context context, int textViewResourceId, List<CustomActivityData> objects) {
	 super(context, textViewResourceId, objects);
	 layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	 
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
	 // get particular row's data
	 CustomActivityData item = (CustomActivityData)getItem(position);
	 
	 // generate convertView only when it doesn't exit
	 if (null == convertView) {
	 convertView = layoutInflater_.inflate(R.layout.list_content_of_activity_data, null);
	 }
	 
	 // set CustomData to views
	 ImageView imageView;
	 imageView = (ImageView)convertView.findViewById(R.id.recentDel);
	 imageView.setImageBitmap(item.getDeleteButtonImage());
	 
	 TextView recentWhereTextView;
	 recentWhereTextView = (TextView)convertView.findViewById(R.id.recentWhere);
	 recentWhereTextView.setText(item.getRecentWhereText());

	 TextView recentWhenTextView;
	 recentWhenTextView = (TextView)convertView.findViewById(R.id.recentWhen);
	 recentWhenTextView.setText(item.getRecentWhenText());	 
	 
	 return convertView;
	 }
}
