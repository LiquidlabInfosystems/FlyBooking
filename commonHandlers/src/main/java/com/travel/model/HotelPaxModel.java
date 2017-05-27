package com.travel.model;

public class HotelPaxModel {

	String FirstName;
	String MiddleName = "";
	String LastName;
	String Email;
	String MobileNumber;
	String MobileCode;
	String Citizenship = "KW";
	String Title;
	String PassengerType;
	String DateOfBirth = "09/16/1990";
	int Age = 26;

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
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

	public String getPassengerType() {
		return PassengerType;
	}

	public void setPassengerType(String passengerType) {
		PassengerType = passengerType;
	}

	public String getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

}
