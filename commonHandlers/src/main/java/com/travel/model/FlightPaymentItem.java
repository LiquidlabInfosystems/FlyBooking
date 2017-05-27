package com.travel.model;

public class FlightPaymentItem {

	String ResponseType;
	String Confirmationmessage;
	String DeeplinkUrl;
	String Currency;
	double TotalAmount;
	double BoardingFares = 0.0;
	double ConvertionRate;
	boolean IsMigsPaymentGatewayActive;
	AvailablePaymentGateways[] availablePaymentGateways;

	public String getResponseType() {
		return ResponseType;
	}

	public void setResponseType(String responseType) {
		ResponseType = responseType;
	}

	public String getConfirmationmessage() {
		return Confirmationmessage;
	}

	public void setConfirmationmessage(String confirmationmessage) {
		Confirmationmessage = confirmationmessage;
	}

	public String getDeeplinkUrl() {
		return DeeplinkUrl;
	}

	public void setDeeplinkUrl(String deeplinkUrl) {
		DeeplinkUrl = deeplinkUrl;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public double getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		TotalAmount = totalAmount;
	}

	public double getBoardingFares() {
		return BoardingFares;
	}

	public void setBoardingFares(double boardingFares) {
		BoardingFares = boardingFares;
	}

	public double getConvertionRate() {
		return ConvertionRate;
	}

	public void setConvertionRate(double convertionRate) {
		ConvertionRate = convertionRate;
	}

	public boolean isIsMigsPaymentGatewayActive() {
		return IsMigsPaymentGatewayActive;
	}

	public void setIsMigsPaymentGatewayActive(boolean isMigsPaymentGatewayActive) {
		IsMigsPaymentGatewayActive = isMigsPaymentGatewayActive;
	}

	public AvailablePaymentGateways[] getAvailablePaymentGateways() {
		return availablePaymentGateways;
	}

	public void setAvailablePaymentGateways(
			AvailablePaymentGateways[] availablePaymentGateways) {
		this.availablePaymentGateways = availablePaymentGateways;
	}

	public class AvailablePaymentGateways {
		int PaymentGateWayId;
		double ServiceCharge;
		boolean IsPercentage;

		public int getPaymentGateWayId() {
			return PaymentGateWayId;
		}

		public void setPaymentGateWayId(int paymentGateWayId) {
			PaymentGateWayId = paymentGateWayId;
		}

		public double getServiceCharge() {
			return ServiceCharge;
		}

		public void setServiceCharge(double serviceCharge) {
			ServiceCharge = serviceCharge;
		}

		public boolean isIsPercentage() {
			return IsPercentage;
		}

		public void setIsPercentage(boolean isPercentage) {
			IsPercentage = isPercentage;
		}

	}

}
