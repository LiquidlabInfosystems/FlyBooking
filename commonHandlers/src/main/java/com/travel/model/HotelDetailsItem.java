package com.travel.model;

public class HotelDetailsItem {

	String HotelName;
	int HotelUniqueId;
	String HotelCode;
	int APIID;
	String HotelAddress;
	float HotelStar;
	String HotelLocation;
	double Latitude;
	double Longitude;
	boolean IsCombination;
	HotelDetailsRooms Rooms[];
	RoomCombination Roomcombination[];
	RoomTypeDetails RoomTypeDetails[];
	String ImageUrls[];
	String HotelDescription;
	String HotelFacility;
	String CancellationPolicyUrl;
	String ProceedPaxUrl;
	String sID;

	public String getHotelName() {
		return HotelName;
	}

	public void setHotelName(String hotelName) {
		HotelName = hotelName;
	}

	public int getHotelUniqueId() {
		return HotelUniqueId;
	}

	public void setHotelUniqueId(int hotelUniqueId) {
		HotelUniqueId = hotelUniqueId;
	}

	public String getHotelCode() {
		return HotelCode;
	}

	public void setHotelCode(String hotelCode) {
		HotelCode = hotelCode;
	}

	public int getAPIID() {
		return APIID;
	}

	public void setAPIID(int aPIID) {
		APIID = aPIID;
	}

	public String getHotelAddress() {
		return HotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		HotelAddress = hotelAddress;
	}

	public float getHotelStar() {
		return HotelStar;
	}

	public void setHotelStar(float hotelStar) {
		HotelStar = hotelStar;
	}

	public String getHotelLocation() {
		return HotelLocation;
	}

	public void setHotelLocation(String hotelLocation) {
		HotelLocation = hotelLocation;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public boolean isIsCombination() {
		return IsCombination;
	}

	public void setIsCombination(boolean isCombination) {
		IsCombination = isCombination;
	}

	public HotelDetailsRooms[] getRooms() {
		return Rooms;
	}

	public void setRooms(HotelDetailsRooms[] rooms) {
		Rooms = rooms;
	}

	public RoomCombination[] getRoomcombination() {
		return Roomcombination;
	}

	public void setRoomcombination(RoomCombination[] roomcombination) {
		Roomcombination = roomcombination;
	}

	public RoomTypeDetails[] getRoomTypeDetails() {
		return RoomTypeDetails;
	}

	public void setRoomTypeDetails(RoomTypeDetails[] roomTypeDetails) {
		RoomTypeDetails = roomTypeDetails;
	}

	public String[] getImageUrls() {
		return ImageUrls;
	}

	public void setImageUrls(String[] imageUrls) {
		ImageUrls = imageUrls;
	}

	public String getHotelDescription() {
		return HotelDescription;
	}

	public void setHotelDescription(String hotelDescription) {
		HotelDescription = hotelDescription;
	}

	public String getHotelFacility() {
		return HotelFacility;
	}

	public void setHotelFacility(String hotelFacility) {
		HotelFacility = hotelFacility;
	}

	public String getCancellationPolicyUrl() {
		return CancellationPolicyUrl;
	}

	public void setCancellationPolicyUrl(String cancellationPolicyUrl) {
		CancellationPolicyUrl = cancellationPolicyUrl;
	}

	public String getProceedPaxUrl() {
		return ProceedPaxUrl;
	}

	public void setProceedPaxUrl(String proceedPaxUrl) {
		ProceedPaxUrl = proceedPaxUrl;
	}

	public String getsID() {
		return sID;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public class RoomCombination {

		String RoomCombination;

		public String getRoomCombination() {
			return RoomCombination;
		}

		public void setRoomCombination(String roomCombination) {
			RoomCombination = roomCombination;
		}

	}
}
