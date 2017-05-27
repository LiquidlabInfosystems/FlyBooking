package com.travel.model;

public class FlightPaxSubmissionModel {

	int PassengerId;
	String TripId;
	String FirstName;
	String LastName;
	String MiddileName = "";
	String Email;
	String MobileNumber;
	String MobileCode;
	String Gender;
	String PassengerType;
	String FrequentFlyerNo;
	String PassportNumber;
	String DateOfBirth;
	String PassportExpiryDate;
	String PassportPlaceOfIssue = "";
	String Citizenship = "";
	String Title = "";
	boolean IsLoggedIn;
	String[] BaggageList;

	public int getPassengerId() {
		return PassengerId;
	}

	public void setPassengerId(int passengerId) {
		PassengerId = passengerId;
	}

	public String getTripId() {
		return TripId;
	}

	public void setTripId(String tripId) {
		TripId = tripId;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getMiddileName() {
		return MiddileName;
	}

	public void setMiddileName(String middileName) {
		MiddileName = middileName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}

	public String getMobileCode() {
		return MobileCode;
	}

	public void setMobileCode(String mobileCode) {
		MobileCode = mobileCode;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getPassengerType() {
		return PassengerType;
	}

	public void setPassengerType(String passengerType) {
		PassengerType = passengerType;
	}

	public String getFrequentFlyerNo() {
		return FrequentFlyerNo;
	}

	public void setFrequentFlyerNo(String frequentFlyerNo) {
		FrequentFlyerNo = frequentFlyerNo;
	}

	public String getPassportNumber() {
		return PassportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		PassportNumber = passportNumber;
	}

	public String getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public String getPassportExpiryDate() {
		return PassportExpiryDate;
	}

	public void setPassportExpiryDate(String passportExpiryDate) {
		PassportExpiryDate = passportExpiryDate;
	}

	public String getPassportPlaceOfIssue() {
		return PassportPlaceOfIssue;
	}

	public void setPassportPlaceOfIssue(String passportPlaceOfIssue) {
		PassportPlaceOfIssue = passportPlaceOfIssue;
	}

	public String getCitizenship() {
		return Citizenship;
	}

	public void setCitizenship(String citizenship) {
		Citizenship = citizenship;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public boolean isIsLoggedIn() {
		return IsLoggedIn;
	}

	public void setIsLoggedIn(boolean isLoggedIn) {
		IsLoggedIn = isLoggedIn;
	}

	public String[] getBaggageList() {
		return BaggageList;
	}

	public void setBaggageList(String[] baggageList) {
		BaggageList = baggageList;
	}

}
