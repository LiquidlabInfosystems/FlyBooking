package com.travel.model;

public class DirectPaymentModel {

	private PaymentModel paymentModel;
	private HotelPaxModel[] PassengerList;

	public PaymentModel getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(PaymentModel paymentModel) {
		this.paymentModel = paymentModel;
	}

	public HotelPaxModel[] getPassengerList() {
		return PassengerList;
	}

	public void setPassengerList(HotelPaxModel[] passengerList) {
		PassengerList = passengerList;
	}

}
