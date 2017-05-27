package com.travel.model;

public class MyAccountTableItem {

	public class MyFlightTableItem {

		int TransactionTypeDetailId = 0;
		int TransactionID = 0;
		String DateOfTravel;
		String DateOfTravelToCheckinDate;
		String DateOfBooking;
		String Origin;
		String Destination;
		String Email;
		String SupplierPnr;
		String TravelDirection;
		String TotalAmount;
		int FlightBookingId;
		String TransactionStatus;

		public int getTransactionTypeDetailId() {
			return TransactionTypeDetailId;
		}

		public void setTransactionTypeDetailId(int transactionTypeDetailId) {
			TransactionTypeDetailId = transactionTypeDetailId;
		}

		public int getTransactionID() {
			return TransactionID;
		}

		public void setTransactionID(int transactionID) {
			TransactionID = transactionID;
		}

		public String getDateOfTravel() {
			return DateOfTravel;
		}

		public void setDateOfTravel(String dateOfTravel) {
			DateOfTravel = dateOfTravel;
		}

		public String getDateOfTravelToCheckinDate() {
			return DateOfTravelToCheckinDate;
		}

		public void setDateOfTravelToCheckinDate(
				String dateOfTravelToCheckinDate) {
			DateOfTravelToCheckinDate = dateOfTravelToCheckinDate;
		}

		public String getDateOfBooking() {
			return DateOfBooking;
		}

		public void setDateOfBooking(String dateOfBooking) {
			DateOfBooking = dateOfBooking;
		}

		public String getOrigin() {
			return Origin;
		}

		public void setOrigin(String origin) {
			Origin = origin;
		}

		public String getDestination() {
			return Destination;
		}

		public void setDestination(String destination) {
			Destination = destination;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getSupplierPnr() {
			return SupplierPnr;
		}

		public void setSupplierPnr(String supplierPnr) {
			SupplierPnr = supplierPnr;
		}

		public String getTravelDirection() {
			return TravelDirection;
		}

		public void setTravelDirection(String travelDirection) {
			TravelDirection = travelDirection;
		}

		public String getTotalAmount() {
			return TotalAmount;
		}

		public void setTotalAmount(String totalAmount) {
			TotalAmount = totalAmount;
		}

		public int getFlightBookingId() {
			return FlightBookingId;
		}

		public void setFlightBookingId(int flightBookingId) {
			FlightBookingId = flightBookingId;
		}

		public String getTransactionStatus() {
			return TransactionStatus;
		}

		public void setTransactionStatus(String transactionStatus) {
			TransactionStatus = transactionStatus;
		}

	}

	public class MyHotelTableItem {

		int TransactionID = 0;
		String Destination;
		String BookingDate;
		String CheckInDate;
		String CheckOutDate;
		String BookingStatus;
		String ViewVoucherUrl;
		String GetVoucherDataUrl;
		String GetVoucherMinDataUrl;
		String CancelVoucherUrl;

		public int getTransactionID() {
			return TransactionID;
		}

		public void setTransactionID(int transactionID) {
			TransactionID = transactionID;
		}

		public String getDestination() {
			return Destination;
		}

		public void setDestination(String destination) {
			Destination = destination;
		}

		public String getBookingDate() {
			return BookingDate;
		}

		public void setBookingDate(String bookingDate) {
			BookingDate = bookingDate;
		}

		public String getCheckInDate() {
			return CheckInDate;
		}

		public void setCheckInDate(String checkInDate) {
			CheckInDate = checkInDate;
		}

		public String getCheckOutDate() {
			return CheckOutDate;
		}

		public void setCheckOutDate(String checkOutDate) {
			CheckOutDate = checkOutDate;
		}

		public String getBookingStatus() {
			return BookingStatus;
		}

		public void setBookingStatus(String bookingStatus) {
			BookingStatus = bookingStatus;
		}

		public String getViewVoucherUrl() {
			return ViewVoucherUrl;
		}

		public void setViewVoucherUrl(String viewVoucherUrl) {
			ViewVoucherUrl = viewVoucherUrl;
		}

		public String getGetVoucherDataUrl() {
			return GetVoucherDataUrl;
		}

		public void setGetVoucherDataUrl(String getVoucherDataUrl) {
			GetVoucherDataUrl = getVoucherDataUrl;
		}

		public String getGetVoucherMinDataUrl() {
			return GetVoucherMinDataUrl;
		}

		public void setGetVoucherMinDataUrl(String getVoucherMinDataUrl) {
			GetVoucherMinDataUrl = getVoucherMinDataUrl;
		}

		public String getCancelVoucherUrl() {
			return CancelVoucherUrl;
		}

		public void setCancelVoucherUrl(String cancelVoucherUrl) {
			CancelVoucherUrl = cancelVoucherUrl;
		}

	}

}
