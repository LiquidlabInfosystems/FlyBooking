package com.travel.model;

import java.util.ArrayList;

import org.json.JSONArray;

public class FlightResultItem {

	String strTripId = null;
	int intApiId;

	ArrayList<String> AirlineNames = null;

	String DepartTimeOne = null;
	String ArrivalTimeOne = null;

	Long DepartDateTimeOne = null;
	Long ArrivalDateTimeOne = null;

	String DepartTimeTwo = null;
	String ArrivalTimeTwo = null;

	Long DepartDateTimeTwo = null;
	Long ArrivalDateTimeTwo = null;

	String DepartTimeThree = null;
	String ArrivalTimeThree = null;

	Long DepartDateTimeThree = null;
	Long ArrivalDateTimeThree = null;

	String DepartTimeFour = null;
	String ArrivalTimeFour = null;

	Long DepartDateTimeFour = null;
	Long ArrivalDateTimeFour = null;

	Long longFlightDurationInMinsOne;
	Long longFlightDurationInMinsTwo;
	Long longFlightDurationInMinsThree;
	Long longFlightDurationInMinsFour;

	int intFlightStopsOne = 0;

	Long longLayoverTimeInMins = null;
	String strLayoverAirport = null;

	Double doubleFlightPrice = null;
	double discount;
	
	String strDisplayRate = null;
	Boolean blRefundType;
	String strDeepLink = null;

	JSONArray jarray = null;
	JSONArray fareQuoteArray = null;

	public int samePriceCount = 1;

	public String getStrTripId() {
		return strTripId;
	}

	public void setStrTripId(String strTripId) {
		this.strTripId = strTripId;
	}

	public int getIntApiId() {
		return intApiId;
	}

	public void setIntApiId(int intApiId) {
		this.intApiId = intApiId;
	}

	public ArrayList<String> getAirlineNames() {
		return AirlineNames;
	}

	public void setAirlineNames(ArrayList<String> airlineNames) {
		this.AirlineNames = airlineNames;
	}

	public String getDepartTimeOne() {
		return DepartTimeOne;
	}

	public void setDepartTimeOne(String departTimeOne) {
		DepartTimeOne = departTimeOne;
	}

	public String getArrivalTimeOne() {
		return ArrivalTimeOne;
	}

	public void setArrivalTimeOne(String arrivalTimeOne) {
		ArrivalTimeOne = arrivalTimeOne;
	}

	public Long getDepartDateTimeOne() {
		return DepartDateTimeOne;
	}

	public void setDepartDateTimeOne(Long departDateTimeOne) {
		DepartDateTimeOne = departDateTimeOne;
	}

	public Long getArrivalDateTimeOne() {
		return ArrivalDateTimeOne;
	}

	public void setArrivalDateTimeOne(Long arrivalDateTimeOne) {
		ArrivalDateTimeOne = arrivalDateTimeOne;
	}

	public String getDepartTimeTwo() {
		return DepartTimeTwo;
	}

	public void setDepartTimeTwo(String departTimeTwo) {
		DepartTimeTwo = departTimeTwo;
	}

	public String getArrivalTimeTwo() {
		return ArrivalTimeTwo;
	}

	public void setArrivalTimeTwo(String arrivalTimeTwo) {
		ArrivalTimeTwo = arrivalTimeTwo;
	}

	public Long getDepartDateTimeTwo() {
		return DepartDateTimeTwo;
	}

	public void setDepartDateTimeTwo(Long departDateTimeTwo) {
		DepartDateTimeTwo = departDateTimeTwo;
	}

	public Long getArrivalDateTimeTwo() {
		return ArrivalDateTimeTwo;
	}

	public void setArrivalDateTimeTwo(Long arrivalDateTimeTwo) {
		ArrivalDateTimeTwo = arrivalDateTimeTwo;
	}

	public String getDepartTimeThree() {
		return DepartTimeThree;
	}

	public void setDepartTimeThree(String departTimeThree) {
		DepartTimeThree = departTimeThree;
	}

	public String getArrivalTimeThree() {
		return ArrivalTimeThree;
	}

	public void setArrivalTimeThree(String arrivalTimeThree) {
		ArrivalTimeThree = arrivalTimeThree;
	}

	public Long getDepartDateTimeThree() {
		return DepartDateTimeThree;
	}

	public void setDepartDateTimeThree(Long departDateTimeThree) {
		DepartDateTimeThree = departDateTimeThree;
	}

	public Long getArrivalDateTimeThree() {
		return ArrivalDateTimeThree;
	}

	public void setArrivalDateTimeThree(Long arrivalDateTimeThree) {
		ArrivalDateTimeThree = arrivalDateTimeThree;
	}

	public String getDepartTimeFour() {
		return DepartTimeFour;
	}

	public void setDepartTimeFour(String departTimeFour) {
		DepartTimeFour = departTimeFour;
	}

	public String getArrivalTimeFour() {
		return ArrivalTimeFour;
	}

	public void setArrivalTimeFour(String arrivalTimeFour) {
		ArrivalTimeFour = arrivalTimeFour;
	}

	public Long getDepartDateTimeFour() {
		return DepartDateTimeFour;
	}

	public void setDepartDateTimeFour(Long departDateTimeFour) {
		DepartDateTimeFour = departDateTimeFour;
	}

	public Long getArrivalDateTimeFour() {
		return ArrivalDateTimeFour;
	}

	public void setArrivalDateTimeFour(Long arrivalDateTimeFour) {
		ArrivalDateTimeFour = arrivalDateTimeFour;
	}

	public Long getLongFlightDurationInMinsOne() {
		return longFlightDurationInMinsOne;
	}

	public void setLongFlightDurationInMinsOne(Long longFlightDurationInMinsOne) {
		this.longFlightDurationInMinsOne = longFlightDurationInMinsOne;
	}

	public Long getLongFlightDurationInMinsTwo() {
		return longFlightDurationInMinsTwo;
	}

	public void setLongFlightDurationInMinsTwo(Long longFlightDurationInMinsTwo) {
		this.longFlightDurationInMinsTwo = longFlightDurationInMinsTwo;
	}

	public Long getLongFlightDurationInMinsThree() {
		return longFlightDurationInMinsThree;
	}

	public void setLongFlightDurationInMinsThree(
			Long longFlightDurationInMinsThree) {
		this.longFlightDurationInMinsThree = longFlightDurationInMinsThree;
	}

	public Long getLongFlightDurationInMinsFour() {
		return longFlightDurationInMinsFour;
	}

	public void setLongFlightDurationInMinsFour(
			Long longFlightDurationInMinsFour) {
		this.longFlightDurationInMinsFour = longFlightDurationInMinsFour;
	}

	public int getIntFlightStopsOne() {
		return intFlightStopsOne;
	}

	public void setIntFlightStopsOne(int intFlightStopsOne) {
		this.intFlightStopsOne = intFlightStopsOne;
	}

	public Long getLongLayoverTimeInMins() {
		return longLayoverTimeInMins;
	}

	public void setLongLayoverTimeInMins(Long longLayoverTimeInMins) {
		this.longLayoverTimeInMins = longLayoverTimeInMins;
	}

	public String getStrLayoverAirport() {
		return strLayoverAirport;
	}

	public void setStrLayoverAirport(String strLayoverAirport) {
		this.strLayoverAirport = strLayoverAirport;
	}

	public Double getDoubleFlightPrice() {
		return doubleFlightPrice;
	}

	public void setDoubleFlightPrice(Double doubleFlightPrice) {
		this.doubleFlightPrice = doubleFlightPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public String getStrDisplayRate() {
		return strDisplayRate;
	}

	public void setStrDisplayRate(String strDisplayRate) {
		this.strDisplayRate = strDisplayRate;
	}

	public Boolean getBlRefundType() {
		return blRefundType;
	}

	public void setBlRefundType(Boolean blRefundType) {
		this.blRefundType = blRefundType;
	}

	public String getStrDeepLink() {
		return strDeepLink;
	}

	public void setStrDeepLink(String strDeepLink) {
		this.strDeepLink = strDeepLink;
	}

	public JSONArray getJarray() {
		return jarray;
	}

	public void setJarray(JSONArray jarray) {
		this.jarray = jarray;
	}

	public JSONArray getFareQuoteArray() {
		return fareQuoteArray;
	}

	public void setFareQuoteArray(JSONArray fareQuoteArray) {
		this.fareQuoteArray = fareQuoteArray;
	}

	public int getSamePriceCount() {
		return samePriceCount;
	}

	public void setSamePriceCount(int samePriceCount) {
		this.samePriceCount = samePriceCount;
	}

}
