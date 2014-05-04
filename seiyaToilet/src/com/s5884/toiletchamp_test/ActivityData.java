package com.s5884.toiletchamp_test;

public class ActivityData {
	String udid;
	String location;
	long timestamp;
	
	ActivityData(String _udid, String _location, long _timestamp){
		udid = _udid;
		location = _location;
	    timestamp = _timestamp;	
	}
	
	String getUdid(){
		return udid;
	}
	
	String getLocation(){
		return location;
	}
	
	long getTimestamp(){
	    return timestamp;
	}

}
