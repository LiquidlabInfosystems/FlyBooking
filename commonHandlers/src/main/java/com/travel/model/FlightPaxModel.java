package com.travel.model;

public class FlightPaxModel {

	String Firstname = "";
	String MiddleName = "";
	String LastName = "";
	String Email = "";
	String Gender = "";
	String PhoneNumber = "";
	String MobileNumber = "";
	String PassportNumber = "";
	String DateOfBirth = "";
	String PassportExpiryDate = "";
	String PassportPlaceOfIssue = "";
	String Citizenship = "";
	String MobileCode = "";
	String Title = "";
	String PassengerType = "";
	BoardingDetails[] boardingDetails = null;

	public String getFirstname() {
		return Firstname;
	}

	public void setFirstname(String firstname) {
		Firstname = firstname;
	}

	public String getMiddleName() {
		return MiddleName;
	}

	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
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

	public String getMobileCode() {
		return MobileCode;
	}

	public void setMobileCode(String mobileCode) {
		MobileCode = mobileCode;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getPassengerType() {
		return PassengerType;
	}

	public void setPassengerType(String passengerType) {
		PassengerType = passengerType;
	}

	public BoardingDetails[] getBoradingDetails() {
		return boardingDetails;
	}

	public void setBoradingDetails(BoardingDetails[] boardingDetails) {
		this.boardingDetails = boardingDetails;
	}

	public class BoardingDetails {

		BaggageList[] baggageList;
		CheckInList[] checkInList;
		Boolean AllowBaggages;
		Boolean AllowCheckIn;

		public BaggageList[] getBaggageList() {
			return baggageList;
		}

		public void setBaggageList(BaggageList[] baggageList) {
			this.baggageList = baggageList;
		}

		public CheckInList[] getCheckInList() {
			return checkInList;
		}

		public void setCheckInList(CheckInList[] checkInList) {
			this.checkInList = checkInList;
		}

		public Boolean getAllowBaggages() {
			return AllowBaggages;
		}

		public void setAllowBaggages(Boolean allowBaggages) {
			AllowBaggages = allowBaggages;
		}

		public Boolean getAllowCheckIn() {
			return AllowCheckIn;
		}

		public void setAllowCheckIn(Boolean allowCheckIn) {
			AllowCheckIn = allowCheckIn;
		}

	}

	public class BaggageList {

		String value;
		String id;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	public class CheckInList {

	}

}
