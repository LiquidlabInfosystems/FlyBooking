package com.travel.model;

public class RoomTypeDetails {

	int AdultCount;
	int ChildCount;
	int FirstChildAge;
	int SecondChildAge;
	String RoomTypeIdentifier;

	public int getAdultCount() {
		return AdultCount;
	}

	public void setAdultCount(int adultCount) {
		AdultCount = adultCount;
	}

	public int getChildCount() {
		return ChildCount;
	}

	public void setChildCount(int childCount) {
		ChildCount = childCount;
	}

	public int getFirstChildAge() {
		return FirstChildAge;
	}

	public void setFirstChildAge(int firstChildAge) {
		FirstChildAge = firstChildAge;
	}

	public int getSecondChildAge() {
		return SecondChildAge;
	}

	public void setSecondChildAge(int secondChildAge) {
		SecondChildAge = secondChildAge;
	}

	public String getRoomTypeIdentifier() {
		return RoomTypeIdentifier;
	}

	public void setRoomTypeIdentifier(String roomTypeIdentifier) {
		RoomTypeIdentifier = roomTypeIdentifier;
	}

}
