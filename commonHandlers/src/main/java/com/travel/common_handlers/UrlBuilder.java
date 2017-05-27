package com.travel.common_handlers;

public class UrlBuilder {

	String LoginUrl;
	String RegisterUrl;
	String ForgetUrl;
	String GetProfileDetailsUrl;
	String SaveProfileDetailsUrl;
	String ChangePasswordUrl;
	String LogoutUrl;
	String ManageBookingWithReferenceUrl;
	String FlightBookingListUrl;
	String FlightDetailWithBookingIDUrl;
	String ViewFlightTicketUrl;
	String CancelFlightTicketUrl;
	String HotelBookingListUrl;
	String HotelDetailsUrl;
	String ViewHotelVoucherUrl;
	String CancelHotelUrl;

	public UrlBuilder() {
		// Basic urls used for APi requests
		
		LoginUrl = "/MyAccountApi/AppLogIn/";
		RegisterUrl = "/MyAccountApi/AppRegister";
		ForgetUrl = "/MyAccountApi/ForgotPassword";
		GetProfileDetailsUrl = "/MyAccountApi/AccountProfileDetails";
		SaveProfileDetailsUrl = "/MyAccountApi/SaveAccountProfile";
		ChangePasswordUrl = "/MyAccountApi/ChangePassword";
		LogoutUrl = "/MyAccountApi/AppLogOut";
		ManageBookingWithReferenceUrl = "/MyAccountApi/ReferenceNumber";
		FlightBookingListUrl = "/MyAccountApi/GetBookedFlights";
		FlightDetailWithBookingIDUrl = "/MyAccountApi/FlightDetailWithBookingID";
		ViewFlightTicketUrl = "/Flight/PrintTicketMyBooking";
		CancelFlightTicketUrl = "/MyAccountApi/CancelTicket?";
		HotelBookingListUrl = "/MyAccountApi/HotelBookingList";
		HotelDetailsUrl = "/MyAccountApi/GetVoucherMinData";
		ViewHotelVoucherUrl = "/Hotel/ViewMyVoucher";
		CancelHotelUrl = "/MyAccountApi/CancelThisVoucher";

	}

	public String getLoginUrl() {
		return LoginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		LoginUrl = loginUrl;
	}

	public String getRegisterUrl() {
		return RegisterUrl;
	}

	public void setRegisterUrl(String registerUrl) {
		RegisterUrl = registerUrl;
	}

	public String getForgetUrl() {
		return ForgetUrl;
	}

	public void setForgetUrl(String forgetUrl) {
		ForgetUrl = forgetUrl;
	}

	public String getGetProfileDetailsUrl() {
		return GetProfileDetailsUrl;
	}

	public void setGetProfileDetailsUrl(String getProfileDetailsUrl) {
		GetProfileDetailsUrl = getProfileDetailsUrl;
	}

	public String getSaveProfileDetailsUrl() {
		return SaveProfileDetailsUrl;
	}

	public void setSaveProfileDetailsUrl(String saveProfileDetailsUrl) {
		SaveProfileDetailsUrl = saveProfileDetailsUrl;
	}

	public String getChangePasswordUrl() {
		return ChangePasswordUrl;
	}

	public void setChangePasswordUrl(String changePasswordUrl) {
		ChangePasswordUrl = changePasswordUrl;
	}

	public String getLogoutUrl() {
		return LogoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		LogoutUrl = logoutUrl;
	}

	public String getManageBookingWithReferenceUrl() {
		return ManageBookingWithReferenceUrl;
	}

	public void setManageBookingWithReferenceUrl(
			String manageBookingWithReferenceUrl) {
		ManageBookingWithReferenceUrl = manageBookingWithReferenceUrl;
	}

	public String getFlightBookingListUrl() {
		return FlightBookingListUrl;
	}

	public void setFlightBookingListUrl(String flightBookingListUrl) {
		FlightBookingListUrl = flightBookingListUrl;
	}

	public String getFlightDetailWithBookingIDUrl() {
		return FlightDetailWithBookingIDUrl;
	}

	public void setFlightDetailWithBookingIDUrl(
			String flightDetailWithBookingIDUrl) {
		FlightDetailWithBookingIDUrl = flightDetailWithBookingIDUrl;
	}

	public String getViewFlightTicketUrl() {
		return ViewFlightTicketUrl;
	}

	public void setViewFlightTicketUrl(String viewFlightTicketUrl) {
		ViewFlightTicketUrl = viewFlightTicketUrl;
	}

	public String getCancelFlightTicketUrl() {
		return CancelFlightTicketUrl;
	}

	public void setCancelFlightTicketUrl(String cancelFlightTicketUrl) {
		CancelFlightTicketUrl = cancelFlightTicketUrl;
	}

	public String getHotelBookingListUrl() {
		return HotelBookingListUrl;
	}

	public void setHotelBookingListUrl(String hotelBookingListUrl) {
		HotelBookingListUrl = hotelBookingListUrl;
	}

	public String getHotelDetailsUrl() {
		return HotelDetailsUrl;
	}

	public void setHotelDetailsUrl(String hotelDetailsUrl) {
		HotelDetailsUrl = hotelDetailsUrl;
	}

	public String getViewHotelVoucherUrl() {
		return ViewHotelVoucherUrl;
	}

	public void setViewHotelVoucherUrl(String viewHotelVoucherUrl) {
		ViewHotelVoucherUrl = viewHotelVoucherUrl;
	}

	public String getCancelHotelUrl() {
		return CancelHotelUrl;
	}

	public void setCancelHotelUrl(String cancelHotelUrl) {
		CancelHotelUrl = cancelHotelUrl;
	}

}
