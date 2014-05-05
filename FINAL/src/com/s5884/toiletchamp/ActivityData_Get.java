package com.s5884.toiletchamp;

public class ActivityData_Get {
	String udid;
	//String locationFilter;
	//long timeFilter;
	//int itemNumber;
	
	//ActivityData_Get(String _udid, String _locationFilter, long _timeFilter, int _itemNumber){
	ActivityData_Get(String _udid){
		udid = _udid;
		//locationFilter = _locationFilter;
	    //timeFilter = _timeFilter;
		//itemNumber = _itemNumber;
	}
	
	String getUdid(){
		return udid;
	}
	/*
	String getLocationFilter(){
		return locationFilter;
	}
	
	long getTimeFilter(){
	    return timeFilter;
	}
	int getItemNumber(){
	    return itemNumber;
	}
	*/

}