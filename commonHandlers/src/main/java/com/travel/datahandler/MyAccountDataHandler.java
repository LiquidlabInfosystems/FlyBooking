package com.travel.datahandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travel.model.DirectPaymentModel;
import com.travel.model.HotelPaxModel;
import com.travel.model.MyAccountItem;
import com.travel.model.PaymentModel;
import com.travel.model.MyAccountItem.MyAccountFlightItem;
import com.travel.model.MyAccountItem.MyAccountFlightItem.ItenaryDetailsInfoList;
import com.travel.model.MyAccountItem.MyAccountHotelItem;
import com.travel.model.MyAccountTableItem;
import com.travel.model.MyAccountTableItem.MyFlightTableItem;
import com.travel.model.MyAccountTableItem.MyHotelTableItem;
import com.travel.model.PaymentModel.AvailablePaymentGateways;

public class MyAccountDataHandler {

	public MyAccountDataHandler() {
	}

	public MyFlightTableItem[] getFlightListData(JSONArray jFlightListArray)
			throws JSONException {
		// To create Flight booking table data model from server response
		// JSONArray
		int i = 0, len = jFlightListArray.length();
		JSONObject jObj = null;
		MyAccountTableItem.MyFlightTableItem[] flightList = new MyAccountTableItem.MyFlightTableItem[len];
		for (i = 0; i < len; ++i) {

			jObj = jFlightListArray.getJSONObject(i);
			flightList[i] = new MyAccountTableItem().new MyFlightTableItem();
			flightList[i].setTransactionTypeDetailId(jObj
					.getInt("TransactionTypeDetailId"));
			flightList[i].setTransactionID(jObj.getInt("TransactionID"));
			flightList[i].setDateOfTravel(jObj.getString("DateOfTravel"));
			flightList[i].setDateOfBooking(jObj.getString("DateOfBooking"));
			flightList[i].setOrigin(jObj.getString("Origin"));
			flightList[i].setDestination(jObj.getString("Destination"));
			flightList[i].setEmail(jObj.getString("Email"));
			flightList[i].setTotalAmount(jObj.getString("TotalAmount"));
			flightList[i].setFlightBookingId(jObj.getInt("FlightBookingId"));
			flightList[i].setTransactionStatus(jObj
					.getString("TransactionStatus"));
		}

		return flightList;
	}

	public MyHotelTableItem[] getHotelListData(JSONArray jHotelListArray)
			throws JSONException {
		// To create Hotel booking table data model from server response
		// JSONArray
		int i = 0, len = jHotelListArray.length();
		JSONObject jObj = null;
		MyHotelTableItem[] hotelList = new MyHotelTableItem[len];
		for (i = 0; i < len; ++i) {

			jObj = jHotelListArray.getJSONObject(i);
			hotelList[i] = new MyAccountTableItem().new MyHotelTableItem();
			hotelList[i].setTransactionID(jObj.getInt("TransactionID"));
			hotelList[i].setDestination(jObj.getString("Destination"));
			hotelList[i].setBookingDate(jObj.getString("BookingDate"));
			hotelList[i].setCheckInDate(jObj.getString("CheckInDate"));
			hotelList[i].setCheckOutDate(jObj.getString("CheckOutDate"));
			hotelList[i].setBookingStatus(jObj.getString("BookingStatus"));
			hotelList[i].setViewVoucherUrl(jObj.getString("ViewVoucherUrl"));
			hotelList[i].setGetVoucherDataUrl(jObj
					.getString("GetVoucherMinDataUrl"));
			hotelList[i]
					.setCancelVoucherUrl(jObj.getString("CancelVoucherUrl"));
		}

		return hotelList;
	}

	public MyAccountFlightItem getMyFlightDetails(JSONArray jArray)
			throws JSONException {
		// To create Flight individual data model from server response JSONArray

		int i = 0, length = jArray.length();
		MyAccountFlightItem myAccountFlightItem = new MyAccountItem().new MyAccountFlightItem();

		JSONObject jObj = jArray.getJSONObject(0);

		myAccountFlightItem.setTransactionID(jObj.getInt("TransactionID"));
		myAccountFlightItem.setTransactionTypeDetailId(jObj
				.getInt("TransactionTypeDetailId"));
		myAccountFlightItem.setSupplierPnr(jObj.getString("SupplierPnr"));
		myAccountFlightItem.setTotalAmount(jObj.getString("TotalAmount"));
		myAccountFlightItem.setConfirmationNo(jObj.getString("ConfirmationNo"));
		myAccountFlightItem.setFlightBookingId(jObj.getInt("FlightBookingId"));
		myAccountFlightItem.setTransactionStatus(jObj
				.getString("TransactionStatus"));
		myAccountFlightItem.setEmail(jObj.getString("Email"));
		myAccountFlightItem.setPaymentDate(jObj.getString("PaymentDate"));

		if (jObj.has("isPaymentActive")) {
			myAccountFlightItem.setPaymentActive(jObj
					.getBoolean("isPaymentActive"));
			if (myAccountFlightItem.isPaymentActive()
					&& jObj.has("directPaymentLink")) {
				myAccountFlightItem.setDirectPaymentLink(jObj
						.getString("directPaymentLink"));
			}
		}

		jArray = jObj.getJSONArray("ItenaryDetailsInfoList");
		length = jArray.length();

		ItenaryDetailsInfoList[] itenaryDetailsInfoList = new MyAccountFlightItem.ItenaryDetailsInfoList[length];
		for (i = 0; i < length; i++) {

			JSONObject object = jArray.getJSONObject(i);
			itenaryDetailsInfoList[i] = new MyAccountItem().new MyAccountFlightItem().new ItenaryDetailsInfoList();

			itenaryDetailsInfoList[i].setDepartDate(object
					.getString("DepartDate"));
			itenaryDetailsInfoList[i].setArrivalDate(object
					.getString("ArrivalDate"));
			itenaryDetailsInfoList[i].setOrigin(object.getString("Origin"));
			itenaryDetailsInfoList[i].setReferenceNumber(object
					.getString("ReferenceNumber"));
			itenaryDetailsInfoList[i].setFromDate(object.getString("FromDate"));
			itenaryDetailsInfoList[i].setFromTime(object.getString("FromTime"));
			itenaryDetailsInfoList[i].setAirlineLogo(object
					.getString("AirlineLogo"));
			itenaryDetailsInfoList[i].setFlightNo(object.getString("FlightNo"));
			itenaryDetailsInfoList[i].setAirlineName(object
					.getString("AirlineName"));
			itenaryDetailsInfoList[i].setAirlinePnr(object
					.getString("AirlinePnr"));
			itenaryDetailsInfoList[i].setEquipmentNumber(object
					.getString("EquipmentNumber"));
			itenaryDetailsInfoList[i].setFlightClass(object
					.getString("Baggage"));
			itenaryDetailsInfoList[i].setDestination(object
					.getString("Destination"));
			itenaryDetailsInfoList[i].setEndDate(object.getString("EndDate"));
			itenaryDetailsInfoList[i].setEndTime(object.getString("EndTime"));
			itenaryDetailsInfoList[i].setFlightSearchClass(object
					.getString("FlightSearchClass"));
			itenaryDetailsInfoList[i].setTripCount(object.getInt("TripCount"));
			itenaryDetailsInfoList[i].setOriginCity(object
					.getString("OriginCity"));
			itenaryDetailsInfoList[i].setDestinationCity(object
					.getString("DestinationCity"));
			itenaryDetailsInfoList[i].setSegmentDuration(object
					.getString("SegmentDuration"));
			itenaryDetailsInfoList[i].setFirstOnwardFlight(object
					.getBoolean("FirstOnwardFlight"));
			itenaryDetailsInfoList[i].setNumberofStops(object
					.getString("NumberofStops"));
			itenaryDetailsInfoList[i].setDuration(object.getString("Duration"));
			itenaryDetailsInfoList[i].setTransitTime(object
					.getString("TransitTime"));
			itenaryDetailsInfoList[i].setFirstReturnFlight(object
					.getBoolean("FirstReturnFlight"));
			itenaryDetailsInfoList[i].setReturnFlightIndicator(object
					.getBoolean("ReturnFlightIndicator"));
			itenaryDetailsInfoList[i].setTravelDirection(object
					.getString("TravelDirection"));
			itenaryDetailsInfoList[i].setReturnRefundableStatus(object
					.getString("ReturnRefundableStatus"));
			itenaryDetailsInfoList[i].setDepartureRefundableStatus(object
					.getString("DepartureRefundableStatus"));
			itenaryDetailsInfoList[i].setTotalDuration(object
					.getString("TotalDuration"));

		}
		myAccountFlightItem.setItenaryDetailsInfoList(itenaryDetailsInfoList);
		return myAccountFlightItem;
	}

	public MyAccountHotelItem getHotelDetails(JSONObject jObj)
			throws JSONException {
		// To create Hotel individual data model from server response JSONObject
		MyAccountHotelItem myAccountHotelItem = new MyAccountItem().new MyAccountHotelItem();

		myAccountHotelItem.setPropName(jObj.getString("PropName"));
		myAccountHotelItem.setHotelAddress(jObj.getString("HotelAddress"));
		myAccountHotelItem.setCheckin(jObj.getString("Checkin"));
		myAccountHotelItem.setCheckOut(jObj.getString("CheckOut"));
		myAccountHotelItem.setNumOfRooms(jObj.getInt("NumOfRooms"));
		myAccountHotelItem.setNumOfNights(jObj.getString("NumOfNights"));
		myAccountHotelItem.setNumOfAdult(jObj.getInt("NumOfAdult"));
		myAccountHotelItem.setNumOfChild(jObj.getInt("NumOfChild"));
		myAccountHotelItem.setCurency(jObj.getString("Curency"));
		myAccountHotelItem.setTotalAmount(jObj.getString("TotalAmount"));
		myAccountHotelItem.setVoucherstatus(jObj.getString("Voucherstatus"));
		myAccountHotelItem.setTransactionTypedetailId(jObj
				.getInt("TransactionTypedetailId"));
		myAccountHotelItem.setHotelBookingId(jObj.getInt("HotelBookingId"));
		myAccountHotelItem.setImageUrl(jObj.getString("ImageUrl"));
		myAccountHotelItem.setHotelAPI(jObj.getInt("HotelAPI"));
		myAccountHotelItem.setPassengerEmail(jObj.getString("PassengerEmail"));
		myAccountHotelItem.setViewVoucherUrl(jObj.getString("ViewVoucherUrl"));
		myAccountHotelItem.setCancelVoucherUrl(jObj
				.getString("CancelVoucherUrl"));

		return myAccountHotelItem;
	}

	public DirectPaymentModel getDirectPaymentDetails(String strJson)
			throws JSONException {

		DirectPaymentModel directPaymentModel = new DirectPaymentModel();
		
		JSONObject jObj, jPaymentObj, jPassengerObj;
		JSONArray jPassengerArr;
		jObj = new JSONObject(strJson);

		jPassengerArr = jObj.getJSONArray("PassengerDetails");
		int count = 0, len = jPassengerArr.length();

		HotelPaxModel[] PassengerList = new HotelPaxModel[len];

		for (count = 0; count < len; count++) {
			jPassengerObj = jPassengerArr.getJSONObject(count);
			PassengerList[count] = new HotelPaxModel();
			PassengerList[count].setFirstName(jPassengerObj.getString("Name"));
			PassengerList[count].setDateOfBirth(jPassengerObj.getString("DateOfBirth"));
			PassengerList[count].setTitle(jPassengerObj.getString("Gender"));
			PassengerList[count].setCitizenship(jPassengerObj.getString("Nationality"));
			PassengerList[count].setEmail(jPassengerObj.getString("Email"));
			PassengerList[count].setMobileNumber(jPassengerObj.getString("Mobile"));
			PassengerList[count].setPassengerType(jPassengerObj.getString("Type"));
		}

		directPaymentModel.setPassengerList(PassengerList);
		
		jPaymentObj = jObj.getJSONObject("PayementDetails");

		PaymentModel paymentModel = new PaymentModel();
		paymentModel
				.setResponseType(jPaymentObj.getString("RequestType"));
		paymentModel
				.setDeeplinkUrl(jPaymentObj.getString("ProceedPayUrl"));
		paymentModel.setCurrency(jPaymentObj.getString("Currency"));
		paymentModel.setTotalAmount(jPaymentObj.getDouble("TotalAmount"));

		JSONArray jArray = jPaymentObj.getJSONArray("AvailablePaymentGateways");
		JSONObject jObjtemp = null;
		int length = jArray.length(), i = 0;
		AvailablePaymentGateways[] availablePaymentGateways = new AvailablePaymentGateways[length];
		for (i = 0; i < jArray.length(); ++i) {
			jObjtemp = jArray.getJSONObject(i);
			availablePaymentGateways[i] = paymentModel.new AvailablePaymentGateways();
			availablePaymentGateways[i].setPaymentGateWayId(jObjtemp
					.getInt("PaymentGateWayId"));
			availablePaymentGateways[i].setServiceCharge(jObjtemp
					.getDouble("ServiceCharge"));
			availablePaymentGateways[i].setIsPercentage(jObjtemp
					.getBoolean("IsPercentage"));
			paymentModel.setConvertionRate(jObjtemp
					.getDouble("ConvertionRate"));
		}

		paymentModel.setAvailablePaymentGateways(availablePaymentGateways);

		if (jPaymentObj.getBoolean("IsHandTwoHandActive")) {
			paymentModel.setIsHandTwoHandActive(true);
			final JSONObject jObjHand = jPaymentObj
					.getJSONObject("ObjHandTwoHand");
			paymentModel.setHandTwoHandCharge(jObjHand
					.getDouble("HandTwoHandCharge"));
		}

		directPaymentModel.setPaymentModel(paymentModel);
		
		return directPaymentModel;
		
	}

}
