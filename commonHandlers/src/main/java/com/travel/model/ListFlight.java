package com.travel.model;

public class ListFlight {
	String FlightLogo;
	String FlightCode;
	String FlightName;
	String FlightNumber;
	String DepartureCityCode;
	String DepartureCityName;
	String ArrivalCityCode;
	String ArrivalCityName;
	String DepartureDateTime;
	String ArrivalDateTime;
	String DepartureDateString;
	String ArrivalDateString;
	String DepartureTimeString;
	String ArrivalTimeString;
	String DepartureAirportName;
	String ArrivalAirportName;
	String DepartureTerminal;
	String ArrivalTerminal;
	String DurationPerLeg;
	String TransitTime;
	int LayoverTimeMinutes;
	String EquipmentNumber;
	String BookingCode;
	String MealCode;
	SegmentDetails[] segmentDetails;

	public String getFlightLogo() {
		return FlightLogo;
	}

	public void setFlightLogo(String flightLogo) {
		FlightLogo = flightLogo;
	}

	public String getFlightCode() {
		return FlightCode;
	}

	public void setFlightCode(String flightCode) {
		FlightCode = flightCode;
	}

	public String getFlightName() {
		return FlightName;
	}

	public void setFlightName(String flightName) {
		FlightName = flightName;
	}

	public String getFlightNumber() {
		return FlightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		FlightNumber = flightNumber;
	}

	public String getDepartureCityCode() {
		return DepartureCityCode;
	}

	public void setDepartureCityCode(String departureCityCode) {
		DepartureCityCode = departureCityCode;
	}

	public String getDepartureCityName() {
		return DepartureCityName;
	}

	public void setDepartureCityName(String departureCityName) {
		DepartureCityName = departureCityName;
	}

	public String getArrivalCityCode() {
		return ArrivalCityCode;
	}

	public void setArrivalCityCode(String arrivalCityCode) {
		ArrivalCityCode = arrivalCityCode;
	}

	public String getArrivalCityName() {
		return ArrivalCityName;
	}

	public void setArrivalCityName(String arrivalCityName) {
		ArrivalCityName = arrivalCityName;
	}

	public String getDepartureDateTime() {
		return DepartureDateTime;
	}

	public void setDepartureDateTime(String departureDateTime) {
		DepartureDateTime = departureDateTime;
	}

	public String getArrivalDateTime() {
		return ArrivalDateTime;
	}

	public void setArrivalDateTime(String arrivalDateTime) {
		ArrivalDateTime = arrivalDateTime;
	}

	public String getDepartureDateString() {
		return DepartureDateString;
	}

	public void setDepartureDateString(String departureDateString) {
		DepartureDateString = departureDateString;
	}

	public String getArrivalDateString() {
		return ArrivalDateString;
	}

	public void setArrivalDateString(String arrivalDateString) {
		ArrivalDateString = arrivalDateString;
	}

	public String getDepartureTimeString() {
		return DepartureTimeString;
	}

	public void setDepartureTimeString(String departureTimeString) {
		DepartureTimeString = departureTimeString;
	}

	public String getArrivalTimeString() {
		return ArrivalTimeString;
	}

	public void setArrivalTimeString(String arrivalTimeString) {
		ArrivalTimeString = arrivalTimeString;
	}

	public String getDepartureAirportName() {
		return DepartureAirportName;
	}

	public void setDepartureAirportName(String departureAirportName) {
		DepartureAirportName = departureAirportName;
	}

	public String getArrivalAirportName() {
		return ArrivalAirportName;
	}

	public void setArrivalAirportName(String arrivalAirportName) {
		ArrivalAirportName = arrivalAirportName;
	}

	public String getDepartureTerminal() {
		return DepartureTerminal;
	}

	public void setDepartureTerminal(String departureTerminal) {
		DepartureTerminal = departureTerminal;
	}

	public String getArrivalTerminal() {
		return ArrivalTerminal;
	}

	public void setArrivalTerminal(String arrivalTerminal) {
		ArrivalTerminal = arrivalTerminal;
	}

	public String getDurationPerLeg() {
		return DurationPerLeg;
	}

	public void setDurationPerLeg(String durationPerLeg) {
		DurationPerLeg = durationPerLeg;
	}

	public String getTransitTime() {
		return TransitTime;
	}

	public void setTransitTime(String transitTime) {
		TransitTime = transitTime;
	}

	public int getLayoverTimeMinutes() {
		return LayoverTimeMinutes;
	}

	public void setLayoverTimeMinutes(int layoverTimeMinutes) {
		LayoverTimeMinutes = layoverTimeMinutes;
	}

	public String getEquipmentNumber() {
		return EquipmentNumber;
	}

	public void setEquipmentNumber(String equipmentNumber) {
		EquipmentNumber = equipmentNumber;
	}

	public String getBookingCode() {
		return BookingCode;
	}

	public void setBookingCode(String bookingCode) {
		BookingCode = bookingCode;
	}

	public String getMealCode() {
		return MealCode;
	}

	public void setMealCode(String mealCode) {
		MealCode = mealCode;
	}

	public SegmentDetails[] getSegmentDetails() {
		return segmentDetails;
	}

	public void setSegmentDetails(SegmentDetails[] segmentDetails) {
		this.segmentDetails = segmentDetails;
	}

	public class SegmentDetails {
		String Baggage;

		public String getBaggage() {
			return Baggage;
		}

		public void setBaggage(String baggage) {
			Baggage = baggage;
		}

	}
}
