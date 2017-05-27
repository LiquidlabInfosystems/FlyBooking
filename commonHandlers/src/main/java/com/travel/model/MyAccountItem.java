package com.travel.model;

public class MyAccountItem {

	public MyAccountItem() {

	}

	public class MyAccountFlightItem {

		int TransactionID;
		int TransactionTypeDetailId;
		String SupplierPnr;
		String TotalAmount;
		ItenaryDetailsInfoList[] itenaryDetailsInfoList;
		// PassengerDetailsInfoList[]
		String ConfirmationNo;
		int FlightBookingId;
		String TransactionStatus;
		String Email;
		String PaymentDate;
		boolean isPaymentActive = false;
		String DirectPaymentLink;

		public int getTransactionID() {
			return TransactionID;
		}

		public void setTransactionID(int transactionID) {
			TransactionID = transactionID;
		}

		public int getTransactionTypeDetailId() {
			return TransactionTypeDetailId;
		}

		public void setTransactionTypeDetailId(int transactionTypeDetailId) {
			TransactionTypeDetailId = transactionTypeDetailId;
		}

		public String getSupplierPnr() {
			return SupplierPnr;
		}

		public void setSupplierPnr(String supplierPnr) {
			SupplierPnr = supplierPnr;
		}

		public String getTotalAmount() {
			return TotalAmount;
		}

		public void setTotalAmount(String totalAmount) {
			TotalAmount = totalAmount;
		}

		public ItenaryDetailsInfoList[] getItenaryDetailsInfoList() {
			return itenaryDetailsInfoList;
		}

		public void setItenaryDetailsInfoList(
				ItenaryDetailsInfoList[] itenaryDetailsInfoList) {
			this.itenaryDetailsInfoList = itenaryDetailsInfoList;
		}

		public String getConfirmationNo() {
			return ConfirmationNo;
		}

		public void setConfirmationNo(String confirmationNo) {
			ConfirmationNo = confirmationNo;
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

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getPaymentDate() {
			return PaymentDate;
		}

		public void setPaymentDate(String paymentDate) {
			PaymentDate = paymentDate;
		}

		public boolean isPaymentActive() {
			return isPaymentActive;
		}

		public void setPaymentActive(boolean isPaymentActive) {
			this.isPaymentActive = isPaymentActive;
		}

		public String getDirectPaymentLink() {
			return DirectPaymentLink;
		}

		public void setDirectPaymentLink(String directPaymentLink) {
			this.DirectPaymentLink = directPaymentLink;
		}

		public class ItenaryDetailsInfoList {

			String DepartDate;
			String ArrivalDate;
			String Origin;
			String ReferenceNumber;
			String FromDate;
			String FromTime;
			String AirlineLogo;
			String FlightNo;
			String AirlineName;
			String AirlinePnr;
			String EquipmentNumber;
			String FlightClass;
			String Destination;
			String EndDate;
			String EndTime;
			String FlightSearchClass;
			int TripCount;
			String OriginCity;
			String DestinationCity;
			String SegmentDuration;
			boolean FirstOnwardFlight;
			String NumberofStops;
			String Duration;
			String TransitTime;
			boolean FirstReturnFlight;
			boolean ReturnFlightIndicator;
			String TravelDirection;
			String ReturnRefundableStatus;
			String DepartureRefundableStatus;
			String TotalDuration;

			public String getDepartDate() {
				return DepartDate;
			}

			public void setDepartDate(String departDate) {
				DepartDate = departDate;
			}

			public String getArrivalDate() {
				return ArrivalDate;
			}

			public void setArrivalDate(String arrivalDate) {
				ArrivalDate = arrivalDate;
			}

			public String getOrigin() {
				return Origin;
			}

			public void setOrigin(String origin) {
				Origin = origin;
			}

			public String getReferenceNumber() {
				return ReferenceNumber;
			}

			public void setReferenceNumber(String referenceNumber) {
				ReferenceNumber = referenceNumber;
			}

			public String getFromDate() {
				return FromDate;
			}

			public void setFromDate(String fromDate) {
				FromDate = fromDate;
			}

			public String getFromTime() {
				return FromTime;
			}

			public void setFromTime(String fromTime) {
				FromTime = fromTime;
			}

			public String getAirlineLogo() {
				return AirlineLogo;
			}

			public void setAirlineLogo(String airlineLogo) {
				AirlineLogo = airlineLogo;
			}

			public String getFlightNo() {
				return FlightNo;
			}

			public void setFlightNo(String flightNo) {
				FlightNo = flightNo;
			}

			public String getAirlineName() {
				return AirlineName;
			}

			public void setAirlineName(String airlineName) {
				AirlineName = airlineName;
			}

			public String getAirlinePnr() {
				return AirlinePnr;
			}

			public void setAirlinePnr(String airlinePnr) {
				AirlinePnr = airlinePnr;
			}

			public String getEquipmentNumber() {
				return EquipmentNumber;
			}

			public void setEquipmentNumber(String equipmentNumber) {
				EquipmentNumber = equipmentNumber;
			}

			public String getFlightClass() {
				return FlightClass;
			}

			public void setFlightClass(String flightClass) {
				FlightClass = flightClass;
			}

			public String getDestination() {
				return Destination;
			}

			public void setDestination(String destination) {
				Destination = destination;
			}

			public String getEndDate() {
				return EndDate;
			}

			public void setEndDate(String endDate) {
				EndDate = endDate;
			}

			public String getEndTime() {
				return EndTime;
			}

			public void setEndTime(String endTime) {
				EndTime = endTime;
			}

			public String getFlightSearchClass() {
				return FlightSearchClass;
			}

			public void setFlightSearchClass(String flightSearchClass) {
				FlightSearchClass = flightSearchClass;
			}

			public int getTripCount() {
				return TripCount;
			}

			public void setTripCount(int tripCount) {
				TripCount = tripCount;
			}

			public String getOriginCity() {
				return OriginCity;
			}

			public void setOriginCity(String originCity) {
				OriginCity = originCity;
			}

			public String getDestinationCity() {
				return DestinationCity;
			}

			public void setDestinationCity(String destinationCity) {
				DestinationCity = destinationCity;
			}

			public String getSegmentDuration() {
				return SegmentDuration;
			}

			public void setSegmentDuration(String segmentDuration) {
				SegmentDuration = segmentDuration;
			}

			public boolean isFirstOnwardFlight() {
				return FirstOnwardFlight;
			}

			public void setFirstOnwardFlight(boolean firstOnwardFlight) {
				FirstOnwardFlight = firstOnwardFlight;
			}

			public String getNumberofStops() {
				return NumberofStops;
			}

			public void setNumberofStops(String numberofStops) {
				NumberofStops = numberofStops;
			}

			public String getDuration() {
				return Duration;
			}

			public void setDuration(String duration) {
				Duration = duration;
			}

			public String getTransitTime() {
				return TransitTime;
			}

			public void setTransitTime(String transitTime) {
				TransitTime = transitTime;
			}

			public boolean isFirstReturnFlight() {
				return FirstReturnFlight;
			}

			public void setFirstReturnFlight(boolean firstReturnFlight) {
				FirstReturnFlight = firstReturnFlight;
			}

			public boolean isReturnFlightIndicator() {
				return ReturnFlightIndicator;
			}

			public void setReturnFlightIndicator(boolean returnFlightIndicator) {
				ReturnFlightIndicator = returnFlightIndicator;
			}

			public String getTravelDirection() {
				return TravelDirection;
			}

			public void setTravelDirection(String travelDirection) {
				TravelDirection = travelDirection;
			}

			public String getReturnRefundableStatus() {
				return ReturnRefundableStatus;
			}

			public void setReturnRefundableStatus(String returnRefundableStatus) {
				ReturnRefundableStatus = returnRefundableStatus;
			}

			public String getDepartureRefundableStatus() {
				return DepartureRefundableStatus;
			}

			public void setDepartureRefundableStatus(
					String departureRefundableStatus) {
				DepartureRefundableStatus = departureRefundableStatus;
			}

			public String getTotalDuration() {
				return TotalDuration;
			}

			public void setTotalDuration(String totalDuration) {
				TotalDuration = totalDuration;
			}

		}
	}

	public class MyAccountHotelItem {

		String PropName;
		String HotelAddress;
		String Checkin;
		String CheckOut;
		int NumOfRooms;
		String NumOfNights;
		int NumOfAdult;
		int NumOfChild;
		String Curency;
		String TotalAmount;
		String Voucherstatus;
		int TransactionTypedetailId;
		int HotelBookingId;
		String ImageUrl;
		int HotelAPI;
		String PassengerEmail;
		String ViewVoucherUrl;
		String CancelVoucherUrl;

		public String getPropName() {
			return PropName;
		}

		public void setPropName(String propName) {
			PropName = propName;
		}

		public String getHotelAddress() {
			return HotelAddress;
		}

		public void setHotelAddress(String hotelAddress) {
			HotelAddress = hotelAddress;
		}

		public String getCheckin() {
			return Checkin;
		}

		public void setCheckin(String checkin) {
			Checkin = checkin;
		}

		public String getCheckOut() {
			return CheckOut;
		}

		public void setCheckOut(String checkOut) {
			CheckOut = checkOut;
		}

		public int getNumOfRooms() {
			return NumOfRooms;
		}

		public void setNumOfRooms(int numOfRooms) {
			NumOfRooms = numOfRooms;
		}

		public String getNumOfNights() {
			return NumOfNights;
		}

		public void setNumOfNights(String numOfNights) {
			NumOfNights = numOfNights;
		}

		public int getNumOfAdult() {
			return NumOfAdult;
		}

		public void setNumOfAdult(int numOfAdult) {
			NumOfAdult = numOfAdult;
		}

		public int getNumOfChild() {
			return NumOfChild;
		}

		public void setNumOfChild(int numOfChild) {
			NumOfChild = numOfChild;
		}

		public String getCurency() {
			return Curency;
		}

		public void setCurency(String curency) {
			Curency = curency;
		}

		public String getTotalAmount() {
			return TotalAmount;
		}

		public void setTotalAmount(String totalAmount) {
			TotalAmount = totalAmount;
		}

		public String getVoucherstatus() {
			return Voucherstatus;
		}

		public void setVoucherstatus(String voucherstatus) {
			Voucherstatus = voucherstatus;
		}

		public int getTransactionTypedetailId() {
			return TransactionTypedetailId;
		}

		public void setTransactionTypedetailId(int transactionTypedetailId) {
			TransactionTypedetailId = transactionTypedetailId;
		}

		public int getHotelBookingId() {
			return HotelBookingId;
		}

		public void setHotelBookingId(int hotelBookingId) {
			HotelBookingId = hotelBookingId;
		}

		public String getImageUrl() {
			return ImageUrl;
		}

		public void setImageUrl(String imageUrl) {
			ImageUrl = imageUrl;
		}

		public int getHotelAPI() {
			return HotelAPI;
		}

		public void setHotelAPI(int hotelAPI) {
			HotelAPI = hotelAPI;
		}

		public String getPassengerEmail() {
			return PassengerEmail;
		}

		public void setPassengerEmail(String passengerEmail) {
			PassengerEmail = passengerEmail;
		}

		public String getViewVoucherUrl() {
			return ViewVoucherUrl;
		}

		public void setViewVoucherUrl(String viewVoucherUrl) {
			ViewVoucherUrl = viewVoucherUrl;
		}

		public String getCancelVoucherUrl() {
			return CancelVoucherUrl;
		}

		public void setCancelVoucherUrl(String cancelVoucherUrl) {
			CancelVoucherUrl = cancelVoucherUrl;
		}

	}
}
