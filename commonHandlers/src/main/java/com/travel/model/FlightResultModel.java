package com.travel.model;

public class FlightResultModel {

	int ApiId;
	int TripId;
	boolean IsRefundable;
	int SeatsLeft;
	String OperatingVender;
	double FinalTotalFare;
	String Currency;
	AllJourney[] allJourneys;
	FareQuoteDetails[] fareQuoteDetails;
	String deeplinkURL;
	public int samePriceCount = 1;

	public int getApiId() {
		return ApiId;
	}

	public void setApiId(int apiId) {
		ApiId = apiId;
	}

	public int getTripId() {
		return TripId;
	}

	public void setTripId(int tripId) {
		TripId = tripId;
	}

	public boolean isIsRefundable() {
		return IsRefundable;
	}

	public void setIsRefundable(boolean isRefundable) {
		IsRefundable = isRefundable;
	}

	public int getSeatsLeft() {
		return SeatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		SeatsLeft = seatsLeft;
	}

	public String getOperatingVender() {
		return OperatingVender;
	}

	public void setOperatingVender(String operatingVender) {
		OperatingVender = operatingVender;
	}

	public double getFinalTotalFare() {
		return FinalTotalFare;
	}

	public void setFinalTotalFare(double finalTotalFare) {
		FinalTotalFare = finalTotalFare;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public AllJourney[] getAllJourneys() {
		return allJourneys;
	}

	public void setAllJourneys(AllJourney[] allJourneys) {
		this.allJourneys = allJourneys;
	}

	public FareQuoteDetails[] getFareQuoteDetails() {
		return fareQuoteDetails;
	}

	public void setFareQuoteDetails(FareQuoteDetails[] fareQuoteDetails) {
		this.fareQuoteDetails = fareQuoteDetails;
	}

	public String getDeeplinkURL() {
		return deeplinkURL;
	}

	public void setDeeplinkURL(String deeplinkURL) {
		this.deeplinkURL = deeplinkURL;
	}

	public int getSamePriceCount() {
		return samePriceCount;
	}

	public void setSamePriceCount(int samePriceCount) {
		this.samePriceCount = samePriceCount;
	}

	public class AllJourney {
		ListFlight[] listFlight;
		int TotalDurationInMinutes;
		String TotalDuration;
		int stops;

		public ListFlight[] getListFlight() {
			return listFlight;
		}

		public void setListFlight(ListFlight[] listFlight) {
			this.listFlight = listFlight;
		}

		public int getTotalDurationInMinutes() {
			return TotalDurationInMinutes;
		}

		public void setTotalDurationInMinutes(int totalDurationInMinutes) {
			TotalDurationInMinutes = totalDurationInMinutes;
		}

		public String getTotalDuration() {
			return TotalDuration;
		}

		public void setTotalDuration(String totalDuration) {
			TotalDuration = totalDuration;
		}

		public int getStops() {
			return stops;
		}

		public void setStops(int stops) {
			this.stops = stops;
		}

	}

	public class FareQuoteDetails {
		String PassengerType;
		String Currency;
		int DecimalPoints;
		int Count;
		double BaseFare;
		double AddiitonalCharges;
		double Discount;
		double TotalAmount;
		double TotalAmountForCount;

		public String getPassengerType() {
			return PassengerType;
		}

		public void setPassengerType(String passengerType) {
			PassengerType = passengerType;
		}

		public String getCurrency() {
			return Currency;
		}

		public void setCurrency(String currency) {
			Currency = currency;
		}

		public int getDecimalPoints() {
			return DecimalPoints;
		}

		public void setDecimalPoints(int decimalPoints) {
			DecimalPoints = decimalPoints;
		}

		public int getCount() {
			return Count;
		}

		public void setCount(int count) {
			Count = count;
		}

		public double getBaseFare() {
			return BaseFare;
		}

		public void setBaseFare(double baseFare) {
			BaseFare = baseFare;
		}

		public double getAddiitonalCharges() {
			return AddiitonalCharges;
		}

		public void setAddiitonalCharges(double addiitonalCharges) {
			AddiitonalCharges = addiitonalCharges;
		}

		public double getDiscount() {
			return Discount;
		}

		public void setDiscount(double discount) {
			Discount = discount;
		}

		public double getTotalAmount() {
			return TotalAmount;
		}

		public void setTotalAmount(double totalAmount) {
			TotalAmount = totalAmount;
		}

		public double getTotalAmountForCount() {
			return TotalAmountForCount;
		}

		public void setTotalAmountForCount(double totalAmountForCount) {
			TotalAmountForCount = totalAmountForCount;
		}

	}

}
