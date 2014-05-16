package com.s5884.toiletchamp;

import android.graphics.Bitmap;

public class CustomActivityData {
	private Bitmap deleteButtonImage;
	private String recentWhereText;
	private String recentWhenText;
	
	public void setDeleteButtonImage(Bitmap _image) {
		deleteButtonImage = _image;
	}

	public Bitmap getDeleteButtonImage() {
		return deleteButtonImage;
	}

	public void setRecentWhereText(String _text) {
		recentWhereText = _text;
	}

	public String getRecentWhereText() {
		return recentWhereText;
	}
	
	public void setRecentWhenText(String _text) {
		recentWhenText = _text;
	}

	public String getRecentWhenText() {
		return recentWhenText;
	}
}
