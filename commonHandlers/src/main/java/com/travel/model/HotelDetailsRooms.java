package com.travel.model;

public class HotelDetailsRooms {

	String RoomName;
	int RoomId;
	String RoomTag;
	String Description;
	String BoardType;
	double PerNightPrice;
	double TotalAmount;
	String Currency;
	int AdultCount;
	int ChildCount;
	int InfantCount;
	String CancellationPolicyUrl;

	public String getRoomName() {
		return RoomName;
	}

	public void setRoomName(String roomName) {
		RoomName = roomName;
	}

	public int getRoomId() {
		return RoomId;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
	}

	public String getRoomTag() {
		return RoomTag;
	}

	public void setRoomTag(String roomTag) {
		RoomTag = roomTag;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getBoardType() {
		return BoardType;
	}

	public void setBoardType(String boardType) {
		BoardType = boardType;
	}

	public double getPerNightPrice() {
		return PerNightPrice;
	}

	public void setPerNightPrice(double perNightPrice) {
		PerNightPrice = perNightPrice;
	}

	public double getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

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

	public int getInfantCount() {
		return InfantCount;
	}

	public void setInfantCount(int infantCount) {
		InfantCount = infantCount;
	}

	public String getCancellationPolicyUrl() {
		return CancellationPolicyUrl;
	}

	public void setCancellationPolicyUrl(String cancellationPolicyUrl) {
		CancellationPolicyUrl = cancellationPolicyUrl;
	}

}
