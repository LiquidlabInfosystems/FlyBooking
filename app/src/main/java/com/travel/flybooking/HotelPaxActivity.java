package com.travel.flybooking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.datahandler.HotelDataHandler;
import com.travel.common_handlers.*;
import com.travel.model.HotelPaxModel;
import com.travel.model.PaymentModel;
import com.travel.model.RoomTypeDetails;
import com.travel.model.PaymentModel.AvailablePaymentGateways;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HotelPaxActivity extends Activity {

	private Locale myLocale;
	TextView txt_continewithoutlogin, txt_Email, txt_countrycode, txt_mobileno;
	String strRequestUrl, strRoomCom, strProceedUrl = null;
	JSONObject response;
	LinearLayout llHotelDetails = null;
	int room = 1, TotalAdultCount = 0, TotalChildCount = 0;
	String[] Countrycode;
	Spinner spCountrycode;
	String strCtryCode, sID;
	String JSonHotelDetails = null;
	TextView tvPassengers, tvRoomcount, tvPrice;
	String strCheckin, strCheckout, strPassengers, strRoomCount, strPrice,
			strImgUrl, strNights, strHotelName, strHotelAddress;
	Dialog loaderDialog;

	LinearLayout llLoginLayout;
	EditText etEmailAddress, EtMobileNo, ll_layoutetEmailAddress,
			ll_layoutetPassword;

	public String[] arrTitle = { "Title", "Male", "Female" };

	HotelDataHandler hotelDataHandler;
	PaymentModel paymentModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_hotel_paxpage);

		hotelDataHandler = new HotelDataHandler();

		setViewValues();
		initilize();
		parseJson();
		new GetPaxDetails().execute();
	}

	private void setViewValues() {
		TextView tvCity = (TextView) findViewById(R.id.tv_Hotel_city);
		TextView tvCheckinDate = (TextView) findViewById(R.id.tv_checkin_date);
		TextView tvCheckoutDate = (TextView) findViewById(R.id.tv_checkout_date);
		TextView tvpassengerCount = (TextView) findViewById(R.id.tv_passenger_count);
		TextView tvroomCount = (TextView) findViewById(R.id.tv_room_count);
		tvCity.setText(HotelResultActivity.strCity);
		tvCheckinDate.setText(HotelResultActivity.strCheckinDate.substring(0,
				HotelResultActivity.strCheckinDate.length() - 5));
		tvCheckoutDate.setText(HotelResultActivity.strCheckoutDate.substring(0,
				HotelResultActivity.strCheckoutDate.length() - 5));
		tvpassengerCount.setText(String
				.valueOf(HotelResultActivity.passengerCount));
		tvroomCount.setText(String.valueOf(HotelResultActivity.roomCount));
	}

	@SuppressWarnings("unchecked")
	public void initilize() {

		loaderDialog = new Dialog(HotelPaxActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		// ((ImageView) loaderDialog.findViewById(R.id.iv_loader))
		// .setImageResource(R.drawable.hotel_loader);

		Double temp;
		strRequestUrl = getIntent().getExtras().getString("request_url", null);
		temp = getIntent().getExtras().getDouble("total_price");
		strPrice = String.format(new Locale("en"), "%.3f", temp);
		strImgUrl = getIntent().getExtras().getString("strImgUrl", null);
		strRoomCom = getIntent().getExtras().getString("roomCombination", null);
		strNights = getIntent().getExtras().getString("NightCount", null);
		strHotelName = getIntent().getExtras().getString("strHotelName", null);
		strHotelAddress = getIntent().getExtras().getString("strHotelAddress",
				null);

		sID = strRequestUrl.substring(Math.max(strRequestUrl.length() - 11, 0));

		txt_continewithoutlogin = (TextView) findViewById(R.id.txt_contine);
		txt_countrycode = (TextView) findViewById(R.id.txt_ccode);
		txt_mobileno = (TextView) findViewById(R.id.txt_mobileno);
		txt_Email = (TextView) findViewById(R.id.txt_email);
		etEmailAddress = (EditText) findViewById(R.id.et_emailid);
		EtMobileNo = (EditText) findViewById(R.id.et_mobilnos);

		llLoginLayout = (LinearLayout) findViewById(R.id.ll_loginlayout);
		ll_layoutetEmailAddress = (EditText) findViewById(R.id.edt_email);
		ll_layoutetPassword = (EditText) findViewById(R.id.edt_password);

		// tvCheckin = (TextView) findViewById(R.id.tv_check_in_date);
		// tvCheckout = (TextView) findViewById(R.id.tv_check_out_date);
		tvPassengers = (TextView) findViewById(R.id.tv_passengers);
		tvRoomcount = (TextView) findViewById(R.id.txt_roomcount);
		tvPrice = (TextView) findViewById(R.id.tv_price);
		// tvHotelname = (TextView) findViewById(R.id.tv_hotel_name);
		// tvHotelAddr = (TextView) findViewById(R.id.tv_hotel_address);
		//
		// tvHotelname.setText(strHotelName);
		// tvHotelAddr.setText(strHotelAddress);

		strCheckin = HotelResultActivity.strCheckinDate;
		strCheckout = HotelResultActivity.strCheckoutDate;

		// SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		// SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
		//
		// Date date;
		// try {
		// date = inputFormat.parse(strCheckin);
		// strCheckin = outputFormat.format(date);
		//
		// date = inputFormat.parse(strCheckout);
		// strCheckout = outputFormat.format(date);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		TextView check_in = (TextView) findViewById(R.id.txt_checkin);
		check_in.setText(strCheckin);
		TextView check_out = (TextView) findViewById(R.id.txt_checkout);
		check_out.setText(strCheckout);

		TextView Roomcount = (TextView) findViewById(R.id.txt_roomcount);
		Roomcount.setText(getResources().getString(R.string.rooms) + ": "
				+ strRoomCount);

		final String blockCharacterSet = " ";

		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				if (source != null && blockCharacterSet.contains(("" + source))) {
					return "";
				}
				return null;
			}
		};

		etEmailAddress.setFilters(new InputFilter[] { filter });
		final String blockCharacterSet1 = " `~!@#$%^&*()_={}|[]:'<>?,/*';\\¡¢£¤¥¦§¨©ª«®¯°±²³´µ¶·¹º»¼½¾¿÷×€ƒ¬™";
		filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				if (source != null
						&& blockCharacterSet1.contains(("" + source))) {
					return "";
				}
				return null;
			}
		};
		EtMobileNo.setFilters(new InputFilter[] { filter });

		// String text =
		// "<font color=#000000>Or</font><font color=#0072bc> Continue without Login</font>";
		// txt_continewithoutlogin.setText(Html.fromHtml(text));

		String textemail = "<font color=#000000>Email ID</font><font color=#e32c18> *</font>";
		txt_Email.setText(Html.fromHtml(textemail));

		String textccode = "<font color=#000000>Country Code</font><font color=#e32c18> *</font>";
		txt_countrycode.setText(Html.fromHtml(textccode));

		String textmobileno = "<font color=#000000>Mobile Number</font><font color=#e32c18> *</font>";
		txt_mobileno.setText(Html.fromHtml(textmobileno));

		if (!CommonFunctions.lang.equals("en")) {
			// String textar =
			// "<font color=#000000>أو </font><font color=#0072bc> الاستمرار من دون تسجيل دخول</font>";
			//
			// txt_continewithoutlogin.setText(Html.fromHtml(textar));

			String textemailar = "<font color=#000000>عنوان البريد الالكتروني</font><font color=#e32c18> *</font>";
			txt_Email.setText(Html.fromHtml(textemailar));

			String textccodear = "<font color=#000000>رقم البلد</font><font color=#e32c18> *</font>";
			txt_countrycode.setText(Html.fromHtml(textccodear));

			String textmobilenoar = "<font color=#000000>رقم المحمول</font><font color=#e32c18> *</font>";
			txt_mobileno.setText(Html.fromHtml(textmobilenoar));
		}
		spCountrycode = (Spinner) findViewById(R.id.spn_countrycode);
		Countrycode = new String[] { "+1", "+1 284", "+1 340", "+1 345",
				"+1 649", "+1 670", "+1 758", "+1 784", "+1 869", "+1242",
				"+1246", "+1264", "+1268", "+1441", "+1473", "+1664", "+1671",
				"+1684", "+1767", "+1809", "+1876", "+20", "+212", "+213",
				"+216", "+218", "+220", "+221", "+222", "+223", "+224", "+225",
				"+226", "+227", "+228", "+229", "+230", "+231", "+232", "+233",
				"+234", "+235", "+236", "+237", "+238", "+239", "+240", "+241",
				"+242", "+243", "+244", "+245", "+246", "+248", "+249", "+250",
				"+251", "+252", "+253", "+254", "+255", "+256", "+257", "+258",
				"+260", "+261", "+262", "+263", "+264", "+265", "+266", "+267",
				"+268", "+269", "+27", "+290", "+291", "+297", "+298", "+299",
				"+30", "+31", "+32", "+33", "+34", "+350", "+351", "+352",
				"+353", "+354", "+355", "+356", "+357", "+358", "+359", "+36",
				"+370", "+371", "+372", "+373", "+374", "+375", "+376", "+378",
				"+380", "+381", "+382", "+385", "+386", "+387", "+389", "+39",
				"+40", "+41", "+420", "+421", "+423", "+43", "+44", "+45",
				"+46", "+47", "+48", "+49", "+500", "+501", "+502", "+503",
				"+504", "+505", "+506", "+507", "+508", "+509", "+51", "+52",
				"+53", "+54", "+55", "+56", "+57", "+58", "+590", "+591",
				"+592", "+593", "+594", "+595", "+596", "+597", "+598", "+599",
				"+60", "+61", "+62", "+63", "+64", "+65", "+66", "+670",
				"+672", "+673", "+674", "+675", "+676", "+677", "+678", "+679",
				"+680", "+681", "+682", "+683", "+685", "+686", "+687", "+688",
				"+689", "+690", "+691", "+692", "+699", "+7", "+81", "+82",
				"+84", "+850", "+852", "+853", "+855", "+856", "+86", "+880",
				"+886", "+90", "+91", "+92", "+93", "+94", "+95", "+960",
				"+961", "+962", "+963", "+964", "+965", "+966", "+967", "+968",
				"+970", "+971", "+972", "+973", "+974", "+975", "+976", "+977",
				"+98", "+992", "+993", "+994", "+995", "+996", "+998" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				HotelPaxActivity.this, R.layout.tv_spinner, Countrycode);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spCountrycode.setAdapter(adapter);
		spCountrycode
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						strCtryCode = spCountrycode.getSelectedItem()
								.toString();
						Log.e("Countrycode", strCtryCode);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		spCountrycode.setSelection(((ArrayAdapter<String>) spCountrycode
				.getAdapter()).getPosition("+965"));
	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		case R.id.txt_forgotpassword:
			Intent forgot = new Intent(HotelPaxActivity.this,
					LoginActivity.class);
			forgot.putExtra("from_pax", true);
			startActivity(forgot);
			break;

		case R.id.tv_signup:
			Intent register = new Intent(HotelPaxActivity.this,
					RegisterActivity.class);
			register.putExtra("from_pax", true);
			startActivity(register);
			break;

		case R.id.btn_login:
			if (Loginvalidate()) {
				new LoginService().execute();
			}
			break;

		case R.id.txt_pls_login:
			if (llLoginLayout.getVisibility() == View.VISIBLE) {
				// Its visible
				llLoginLayout.setVisibility(View.GONE);
			} else {
				// Either gone or invisible
				llLoginLayout.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.btn_pay:
			submit();
			break;

		case R.id.tv_rules:
			Intent rules = new Intent(HotelPaxActivity.this, WebActivity.class);
			rules.putExtra("url", CommonFunctions.main_url
					+ CommonFunctions.lang + "/Shared/Terms");
			startActivity(rules);
			break;

		default:
			break;
		}
	}

	public boolean Loginvalidate() {
		boolean valid = true;

		String email = ll_layoutetEmailAddress.getText().toString();
		String password = ll_layoutetPassword.getText().toString();

		if (email.isEmpty()
				|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
						.matches()) {
			ll_layoutetEmailAddress.setError("enter a valid email address");
			valid = false;
		} else {
			ll_layoutetEmailAddress.setError(null);
		}

		if (password.isEmpty()) {
			ll_layoutetPassword
					.setError("between 4 and 10 alphanumeric characters");
			valid = false;
		} else {
			ll_layoutetPassword.setError(null);
		}

		return valid;
	}

	public void parseJson() {
		// TODO Auto-generated method stub
		LinearLayout llHeader = null;

		int tempAdultCount = 0, tempChildCount = 0;

		RoomTypeDetails[] roomTypeDetails = HotelDetailsActivity.roomTypeDetails;
		int len = roomTypeDetails.length;
		for (int j = 0; j < len; j++) {
			String AdultCount = String.valueOf(roomTypeDetails[j]
					.getAdultCount());
			String ChildCount = String.valueOf(roomTypeDetails[j]
					.getChildCount());
			Log.e("AdultCount  ", AdultCount + "" + ChildCount);

			tempAdultCount = tempAdultCount + Integer.valueOf(AdultCount);
			tempChildCount = tempChildCount + Integer.valueOf(ChildCount);
			switch (j) {
			case 0:
				llHeader = (LinearLayout) findViewById(R.id.ll_paxheader1);
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax1);
				layoutview(AdultCount, ChildCount, llHotelDetails);
				break;
			case 1:
				llHeader = (LinearLayout) findViewById(R.id.ll_paxheader2);
				llHeader.setVisibility(View.VISIBLE);
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax2);
				layoutview(AdultCount, ChildCount, llHotelDetails);
				break;
			case 2:
				llHeader = (LinearLayout) findViewById(R.id.ll_paxheader3);
				llHeader.setVisibility(View.VISIBLE);
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax3);
				layoutview(AdultCount, ChildCount, llHotelDetails);
				break;
			case 3:
				llHeader = (LinearLayout) findViewById(R.id.ll_paxheader4);
				llHeader.setVisibility(View.VISIBLE);
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax4);
				layoutview(AdultCount, ChildCount, llHotelDetails);
				break;
			case 4:
				llHeader = (LinearLayout) findViewById(R.id.ll_paxheader5);
				llHeader.setVisibility(View.VISIBLE);
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax5);
				layoutview(AdultCount, ChildCount, llHotelDetails);
				break;
			default:
				break;
			}

		}

		if (tempChildCount != 0)
			strPassengers = tempAdultCount + " " + getString(R.string.adults)
					+ " " + tempChildCount + " "
					+ getString(R.string.child_ren);
		else
			strPassengers = tempAdultCount + " " + getString(R.string.adult);
		strRoomCount = String.valueOf(len);

		tvPassengers.setText(strPassengers);
		tvRoomcount.setText(getResources().getString(R.string.rooms) + " "
				+ strRoomCount);
		tvPrice.setText(CommonFunctions.strCurrency + " " + strPrice);

	}

	public void layoutview(String adultCount, String childCount,
			LinearLayout HotelllDetails) {
		// TODO Auto-generated method stub

		int Adultcount = Integer.valueOf(adultCount);
		int Childcount = Integer.valueOf(childCount);
		TotalAdultCount += Adultcount;
		TotalChildCount += Childcount;

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				HotelPaxActivity.this, R.array.title_spinner_items,
				R.layout.tv_spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);

		int id = 1;
		TextView tv;
		String strTemp = null;

		for (int i = 1; i <= Adultcount; i++) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_hotel_pax_details, null);
			id++;
			view.setTag("Adult");

			((TextView) view.findViewById(R.id.txt_caption))
					.setText(getString(R.string.adult) + " " + i);

			((Spinner) view.findViewById(R.id.Spn_title)).setAdapter(adapter);

			tv = (TextView) view.findViewById(R.id.txt_title);
			strTemp = "<font color=#000000>Title</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_fname);
			strTemp = "<font color=#000000>First Name</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_lname);
			strTemp = "<font color=#000000>Last Name</font>";
			tv.setText(Html.fromHtml(strTemp));
			if (!CommonFunctions.lang.equals("en")) {
				tv = (TextView) view.findViewById(R.id.txt_title);
				strTemp = "<font color=#000000>اللقب</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_fname);
				strTemp = "<font color=#000000>الاسم الأول </font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_lname);
				strTemp = "<font color=#000000>اسم العائلة</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));
			}
			HotelllDetails.addView(view);
		}
		for (int i = 1; i <= Childcount; i++) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_hotel_pax_details, null);
			id++;
			view.setTag("Child");

			((TextView) view.findViewById(R.id.txt_caption))
					.setText(getString(R.string.children) + " " + i);

			((Spinner) view.findViewById(R.id.Spn_title)).setAdapter(adapter);

			tv = (TextView) view.findViewById(R.id.txt_title);
			strTemp = "<font color=#000000>Title</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_fname);
			strTemp = "<font color=#000000>First Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_lname);
			strTemp = "<font color=#000000>Last Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));
			if (!CommonFunctions.lang.equals("en")) {
				tv = (TextView) view.findViewById(R.id.txt_title);
				strTemp = "<font color=#000000>اللقب</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_fname);
				strTemp = "<font color=#000000>الاسم الأول </font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_lname);
				strTemp = "<font color=#000000>اسم العائلة</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));
			}
			HotelllDetails.addView(view);
		}
	}

	private void submit() {
		String strFirstName, strLastName, strFLName, strPassengerType;
		EditText etFirstName = null, etLastName = null;
		Spinner spGender = null;

		int flag = 0;
		LinearLayout llHotelDetails = null;

		String strGender;
		ArrayList<String> arrayFLList = new ArrayList<String>();

		RoomTypeDetails[] roomTypeDetails = HotelDetailsActivity.roomTypeDetails;
		int len = roomTypeDetails.length;
		int k = 0, j = 0, count = 0, intGender, paxCount = 0;
		HotelPaxModel[] hotelPaxModel = new HotelPaxModel[HotelResultActivity.passengerCount];

		for (j = 0; j < len; j++) {
			int AdultCountI = roomTypeDetails[j].getAdultCount();
			int ChildCountI = roomTypeDetails[j].getChildCount();
			count = AdultCountI + ChildCountI;
			Log.e("AdultCount  ", AdultCountI + " ChildCount " + ChildCountI);

			switch (j) {
			case 0:
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax1);
				break;
			case 1:
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax2);
				break;
			case 2:
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax3);
				break;
			case 3:
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax4);
				break;
			case 4:
				llHotelDetails = (LinearLayout) findViewById(R.id.ll_hotelpax5);
				break;
			default:
				break;
			}

			for (k = 0; k < count; k++) {

				View view1 = llHotelDetails.getChildAt(k);
				strPassengerType = view1.getTag().toString();

				spGender = (Spinner) view1.findViewById(R.id.Spn_title);
				intGender = spGender.getSelectedItemPosition();
				strGender = arrTitle[intGender];
				Log.e("checking ", strGender);

				etFirstName = (EditText) view1.findViewById(R.id.edt_firstname);
				strFirstName = etFirstName.getText().toString();
				etLastName = (EditText) view1.findViewById(R.id.edt_lastname);
				strLastName = etLastName.getText().toString();
				strFLName = strFirstName + " " + strLastName;

				String email = etEmailAddress.getText().toString();
				String mobileno = EtMobileNo.getText().toString();

				if (email.isEmpty()
						|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
								.matches()) {
					etEmailAddress.setError(getResources().getString(
							R.string.error_invalid_email));
					if (flag == 0)
						etEmailAddress.requestFocus();
					flag = 1;
				} else {
					etEmailAddress.setError(null);
				}

				if (mobileno.isEmpty() || mobileno.length() < 5
						|| mobileno.length() > 15) {
					EtMobileNo.setError(getResources().getString(
							R.string.error_invalid_number));
					if (flag == 0)
						EtMobileNo.requestFocus();
					flag = 1;
				} else {
					EtMobileNo.setError(null);
				}

				if (intGender == 0) {
					if (flag == 0) {
						etFirstName.requestFocus();
						Toast.makeText(
								getApplicationContext(),
								getResources()
										.getString(R.string.err_title_req),
								Toast.LENGTH_SHORT).show();
					}
					flag = 1;
				}

				if (strFirstName.equals("") || strFirstName.length() < 2
						|| strFirstName.length() > 19) {
					if (flag == 0)
						etFirstName.requestFocus();
					flag = 1;
					etFirstName.setError(getResources().getString(
							R.string.error_firstname_length));

				}
				if (strLastName.equals("") || strLastName.length() < 2
						|| strLastName.length() > 21) {
					if (flag == 0)
						etLastName.requestFocus();
					flag = 1;
					etLastName.setError(getResources().getString(
							R.string.error_lastname_length));
				}
				if (strFLName.length() > 23) {
					if (flag == 0)
						etFirstName.requestFocus();
					flag = 1;
					etLastName.setError(getResources().getString(
							R.string.err_fl_name_length));
				}

				if (!arrayFLList.contains(strFLName)) {
					arrayFLList.add(strFLName);
				} else {
					if (flag == 0)
						etFirstName.requestFocus();
					flag = 1;
					etFirstName.setError(getResources().getString(
							R.string.err_same_fl_name));
					etLastName.setError(getResources().getString(
							R.string.err_same_fl_name));
				}

				if (flag == 0) {
					hotelPaxModel[paxCount] = new HotelPaxModel();

					if (strPassengerType.equalsIgnoreCase("adult"))
						strGender = strGender.equalsIgnoreCase("male") ? "Mr"
								: "Ms";
					else
						strGender = strGender.equalsIgnoreCase("male") ? "Mstr"
								: "Miss";

					hotelPaxModel[paxCount].setFirstName(strFirstName);
					hotelPaxModel[paxCount].setLastName(strLastName);
					hotelPaxModel[paxCount].setEmail(email);
					hotelPaxModel[paxCount].setMobileCode(strCtryCode);
					hotelPaxModel[paxCount].setMobileNumber(mobileno);
					hotelPaxModel[paxCount].setTitle(strGender);
					hotelPaxModel[paxCount].setPassengerType(strPassengerType);
					paxCount++;
				}
			}
		}
		if (flag == 0) {
			if (((CheckBox) findViewById(R.id.cb_terms)).isChecked()) {
				JSonHotelDetails = hotelDataHandler
						.createHotelPaxString(hotelPaxModel);
				new PaxDetailSubmission().execute();
			} else
				new CommonFunctions(HotelPaxActivity.this)
						.showAlertDialog(getResources().getString(
								R.string.error_check_box));
		}
	}

	private class PaxDetailSubmission extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String urlParameters = "paxListString="
						+ URLEncoder.encode(JSonHotelDetails, "UTF-8");

				String res = new HttpHandler().makeServiceCallWithParams(
						strProceedUrl, urlParameters);
				Log.d("Pax Submission params", JSonHotelDetails);
				Log.d("Pax Submission Url", strProceedUrl);
				Log.d("Pax Submission response", res);

				JSONObject json = new JSONObject(res);

				if (json.getBoolean("IsValid")) {
					return json.getString("RequestPayUrl");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				new HotelPaymentService().execute();
			}

			super.onPostExecute(result);
		}

	}

	private class GetPaxDetails extends AsyncTask<Void, Void, String> {
		Boolean blIsloggedIn = false;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loaderDialog.show();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String res = new HttpHandler().makeServiceCall(strRequestUrl);
				System.out.println("res getpaxdetails=" + res);

				JSONObject json = new JSONObject(res);
				JSONObject jPassList = null;
				if (json.getBoolean("IsValid")) {
					blIsloggedIn = json.getBoolean("IsLoggedIn");
					strProceedUrl = json.getString("ProceedPaxUrl");

					JSONArray jsonItem = json.getJSONArray("pax");

					for (int j = 0; j < jsonItem.length(); j++) {
						jPassList = jsonItem.getJSONObject(j);
					}
					jPassList = jsonItem.getJSONObject(0);
					return jPassList.toString();
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();

			if (result != null && blIsloggedIn) {
				try {
					LinearLayout ll_LogHead = (LinearLayout) findViewById(R.id.ll_login_header);
					ll_LogHead.setVisibility(View.GONE);
					llLoginLayout.setVisibility(View.GONE);

					HotelPaxModel hotelPax = hotelDataHandler
							.setHotelPax(result);

					etEmailAddress.setText(hotelPax.getEmail());
					EtMobileNo.setText(hotelPax.getMobileNumber());

					String strMobCode = hotelPax.getMobileCode().contains("+") ? hotelPax
							.getMobileCode() : "+"
							+ hotelPax.getMobileCode().replace(" ", "");

					spCountrycode
							.setSelection(((ArrayAdapter<String>) spCountrycode
									.getAdapter()).getPosition(strMobCode));

					View view = llHotelDetails.getChildAt(0);
					if (view.getTag().toString().equalsIgnoreCase("adult")) {
						((EditText) view.findViewById(R.id.edt_firstname))
								.setText(hotelPax.getFirstName());
						((EditText) view.findViewById(R.id.edt_lastname))
								.setText(hotelPax.getLastName());

						Spinner Spn = (Spinner) view
								.findViewById(R.id.Spn_title);
						if (hotelPax.getTitle() != null
								&& !hotelPax.getTitle().equals("")) {
							Spn.setSelection(hotelPax.getTitle()
									.equalsIgnoreCase("mr") ? 1 : 2);
						}
					}
					new GetPaymentDetails().execute();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (result != null)
				new GetPaymentDetails().execute();
			super.onPostExecute(result);
		}

	}

	private class LoginService extends AsyncTask<Void, Void, String> {
		Boolean blIsloggedIn = false;
		String strUser = null, strPass = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			loaderDialog.show();
			strUser = ll_layoutetEmailAddress.getText().toString();
			strPass = ll_layoutetPassword.getText().toString();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String urlParameters = new UrlParameterBuilder()
						.getLoginParams(strUser, strPass, true, "hotel");

				String request = CommonFunctions.main_url
						+ CommonFunctions.lang + "/MyAccountApi/AppLogIn?sID="
						+ sID;
				String res = new HttpHandler().makeServiceCallWithParams(
						request, urlParameters);

				Log.d("Login Url", request);
				Log.d("Login response", res);

				JSONObject jPassList = null;
				JSONObject json = new JSONObject(res);

				if (json.getBoolean("IsValid") && json.getBoolean("IsLoggedIn")) {
					blIsloggedIn = json.getBoolean("IsLoggedIn");
					JSONObject jsonpax = json.getJSONObject("pax");
					JSONArray jsonItem = jsonpax.getJSONArray("pax");
					for (int j = 0; j < jsonItem.length(); j++) {
						jPassList = jsonItem.getJSONObject(j);
					}
					jPassList = jsonItem.getJSONObject(0);
					return jPassList.toString();
				} else {
					blIsloggedIn = false;
					return json.getString("LogInMessage");
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
			if (result != null && blIsloggedIn) {
				try {
					LinearLayout ll_LogHead = (LinearLayout) findViewById(R.id.ll_login_header);
					ll_LogHead.setVisibility(View.GONE);
					llLoginLayout.setVisibility(View.GONE);

					HotelPaxModel hotelPax = hotelDataHandler
							.setHotelPax(result);

					etEmailAddress.setText(hotelPax.getEmail());
					EtMobileNo.setText(hotelPax.getMobileNumber());

					String strMobCode = hotelPax.getMobileCode().contains("+") ? hotelPax
							.getMobileCode() : "+"
							+ hotelPax.getMobileCode().replace(" ", "");

					spCountrycode
							.setSelection(((ArrayAdapter<String>) spCountrycode
									.getAdapter()).getPosition(strMobCode));

					View view = llHotelDetails.getChildAt(0);
					if (view.getTag().toString().equalsIgnoreCase("adult")) {
						((EditText) view.findViewById(R.id.edt_firstname))
								.setText(hotelPax.getFirstName());
						((EditText) view.findViewById(R.id.edt_lastname))
								.setText(hotelPax.getLastName());

						Spinner Spn = (Spinner) view
								.findViewById(R.id.Spn_title);
						if (hotelPax.getTitle() != null
								&& !hotelPax.getTitle().equals("")) {
							Spn.setSelection(hotelPax.getTitle()
									.equalsIgnoreCase("mr") ? 1 : 2);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (result != null && !blIsloggedIn) {
				Toast.makeText(getApplicationContext(), result,
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Something went wrong. Please try again later",
						Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				showAlert("There is a problem on your Network. Please try again later.");

			} else if (msg.what == 2) {

				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				showAlert("There is a problem on your application. Please report it.");

			} else if (msg.what == 3) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				showAlert("Something went wrong. Please try again later");
			}

		}
	};

	// payment section

	String strJson, strCurrency, strRooms, strRedirectUrl = null;
	int adultCount, childCount;
	boolean blIsRoundTrip;
	JSONObject jObj;
	ImageView ivHotelLogo;
	String selectedPayment = "2";
	String strResponseType = null;

	Double totalPriceKnet, totalPriceMigs, ServKnet, servMigs,
			dblConversionRate = 1.0;

	private class GetPaymentDetails extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO GetPaymentDetails
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String requestUrl = CommonFunctions.main_url + ""
						+ CommonFunctions.lang
						+ "/HotelApi/RequestPaymentMethods?sID=" + sID;

				String res = new HttpHandler().makeServiceCall(requestUrl);
				Log.e("GetPaymentDetails Url", requestUrl);
				Log.e("GetPaymentDetails res", res);

				JSONObject jobj = new JSONObject(res);

				if (jobj.getBoolean("IsValid")) {
					paymentModel = hotelDataHandler.getHotelPaymentDetails(res);
					return res;
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendEmptyMessage(2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendEmptyMessage(1);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				handler.sendEmptyMessage(3);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				showSummary();
			}
			super.onPostExecute(result);
		}

	}

	private void showSummary() {
		final ImageView ivKnet, ivVisa_Master;
		boolean isKnetActive = false, isMigsActive = false;
		ivKnet = (ImageView) findViewById(R.id.iv_knet);
		ivVisa_Master = (ImageView) findViewById(R.id.iv_migs);

		strCurrency = paymentModel.getCurrency();

		AvailablePaymentGateways[] availablePaymentGateways = paymentModel
				.getAvailablePaymentGateways();

		for (int i = 0; i < availablePaymentGateways.length; ++i) {

			if (availablePaymentGateways[i].getPaymentGateWayId() == 2) {

				ServKnet = availablePaymentGateways[i].isIsPercentage() ? availablePaymentGateways[i]
						.getServiceCharge() * paymentModel.getTotalAmount()
				// * dblConversionRate
						/ 100
						: availablePaymentGateways[i].getServiceCharge()
								* paymentModel.getConvertionRate();
				totalPriceKnet = ServKnet + paymentModel.getTotalAmount();
				// * dblConversionRate;
				isKnetActive = true;
			} else if (availablePaymentGateways[i].getPaymentGateWayId() == 8) {

				servMigs = availablePaymentGateways[i].isIsPercentage() ? availablePaymentGateways[i]
						.getServiceCharge() * paymentModel.getTotalAmount()
				// * dblConversionRate
						/ 100
						: availablePaymentGateways[i].getServiceCharge()
								* paymentModel.getConvertionRate();
				totalPriceMigs = servMigs + paymentModel.getTotalAmount();
				// * dblConversionRate;
				isMigsActive = true;
			}
		}

		((TextView) findViewById(R.id.tv_price)).setText(strCurrency
				+ " "
				+ String.format(new Locale("en"), "%.3f",
						paymentModel.getTotalAmount()));

		if (isKnetActive) {
			ivKnet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ivKnet.setBackgroundResource(R.drawable.orange_button_curved_edge);
					ivVisa_Master.setBackgroundColor(Color.TRANSPARENT);

					String temp = String.format(new Locale("en"), "%.3f",
							ServKnet);
					((TextView) findViewById(R.id.tv_trans_fees))
							.setText(strCurrency + " " + temp);

					((LinearLayout) findViewById(R.id.ll_transaction_fees))
							.setVisibility(ServKnet > 0 ? View.VISIBLE
									: View.GONE);

					temp = String.format(new Locale("en"), "%.3f",
							totalPriceKnet);
					((TextView) findViewById(R.id.tv_total_price))
							.setText(strCurrency + " " + temp);

					selectedPayment = "2";
				}
			});
		} else {
			ivKnet.setVisibility(View.GONE);
			ivKnet.setClickable(false);
		}

		if (isMigsActive)
			isMigsActive = paymentModel.isIsMigsPaymentGatewayActive();

		if (isMigsActive)
			ivVisa_Master.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ivVisa_Master
							.setBackgroundResource(R.drawable.orange_button_curved_edge);
					ivKnet.setBackgroundColor(Color.TRANSPARENT);
					String temp = String.format(new Locale("en"), "%.3f",
							servMigs);
					((TextView) findViewById(R.id.tv_trans_fees))
							.setText(strCurrency + " " + temp);

					((LinearLayout) findViewById(R.id.ll_transaction_fees))
							.setVisibility(servMigs > 0 ? View.VISIBLE
									: View.GONE);

					temp = String.format(new Locale("en"), "%.3f",
							totalPriceMigs);
					((TextView) findViewById(R.id.tv_total_price))
							.setText(strCurrency + " " + temp);
					selectedPayment = "8";
				}
			});
		else {
			ivVisa_Master.setVisibility(View.GONE);
			ivVisa_Master.setClickable(false);
		}

		if (!isKnetActive && isMigsActive)
			selectedPayment = "8";

		if (Integer.parseInt(selectedPayment) == 2) {
			ivKnet.performClick();
		} else
			ivVisa_Master.performClick();
		if (isKnetActive) {
			ivKnet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ivKnet.setBackgroundResource(R.drawable.orange_button_curved_edge);
					ivVisa_Master.setBackgroundColor(Color.TRANSPARENT);

					String temp = String.format(new Locale("en"), "%.3f",
							ServKnet);
					((TextView) findViewById(R.id.tv_trans_fees))
							.setText(strCurrency + " " + temp);

					temp = String.format(new Locale("en"), "%.3f",
							totalPriceKnet);
					((TextView) findViewById(R.id.tv_total_price))
							.setText(strCurrency + " " + temp);
					selectedPayment = "2";
				}
			});
		} else {
			ivKnet.setVisibility(View.GONE);
			ivKnet.setClickable(false);
		}

		if (isMigsActive)
			isMigsActive = paymentModel.isIsMigsPaymentGatewayActive();

		if (isMigsActive)
			ivVisa_Master.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ivVisa_Master
							.setBackgroundResource(R.drawable.orange_button_curved_edge);
					ivKnet.setBackgroundColor(Color.TRANSPARENT);
					String temp = String.format(new Locale("en"), "%.3f",
							servMigs);
					((TextView) findViewById(R.id.tv_trans_fees))
							.setText(strCurrency + " " + temp);

					temp = String.format(new Locale("en"), "%.3f",
							totalPriceMigs);
					((TextView) findViewById(R.id.tv_total_price))
							.setText(strCurrency + " " + temp);
					selectedPayment = "8";
				}
			});
		else {
			ivVisa_Master.setVisibility(View.GONE);
			ivVisa_Master.setClickable(false);
		}

		if (!isKnetActive && isMigsActive)
			selectedPayment = "8";

		if (Integer.parseInt(selectedPayment) == 2) {
			ivKnet.performClick();
		} else
			ivVisa_Master.performClick();

	}

	private class HotelPaymentService extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			loaderDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String tempURL = paymentModel
						.getDeeplinkUrl()
						.replace(
								"{0}",
								"{\"TotalAmount\":0,\"TotalBoardingFee\":0,\"TotalAmountInBaseCurrency\""
										+ ":0,\"BaseFare\":0,\"ServiceCharge\":0,\"RoomCount\":0,\""
										+ "Currency\":null,\"DecimalPoint\":0,\"PaymentGateway\":\""
										+ selectedPayment
										+ "\",\""
										+ "PaymentGatewayType\":null,\"IsMigsPaymentGatewayActive\":false,\""
										+ "IsPayTabsPaymentGatewayActive\":false,\"IsPaymentAtHomeActive\":false,\""
										+ "IsPaymentAtStoreActive\":false,\"TnCChecked\":true,\""
										+ "IsInsurance\":false,\"faresummary\":null,\"AvailablePaymentGateways\":null,\""
										+ "IsCashOnDeliveryActive\":false,\"IsCashOnDelivery\":false,\""
										+ "CashOnDeliveryInfo\":null,\"CodServiceList\":null,\""
										+ "CompanyGenQuoteDetailsModel\":null,\"CompanyGenQuoteForHotel\":null,\""
										+ "KentCharge\":0,\"MigsCharge\":0,\"PayTabsCharge\":0,\"CashUCharge\":0,\""
										+ "IsFlightExcluded\":false,\"NeedInsurance\":false,\"NeedVisa\":false,\""
										+ "ApiId\":0,\"InsuranceAmount\":0,\"CardDetails\":null,\"ConversionRate\":0,\""
										+ "Address1\":null,\"Address2\":null,\"RedeemPoint\":0,\"IsRedeemPoint\":false,\""
										+ "TransactionTypeId\":3}");

				String resultString = new HttpHandler()
						.makeServiceCall(tempURL);

				Log.e("HotelPayment Url", tempURL);
				Log.e("HotelPayment res", resultString);

				JSONObject json = new JSONObject(resultString);
				if (json.getBoolean("IsValid")) {
					if (json.getString("RequestType").equalsIgnoreCase(
							"Redirect")) {
						strResponseType = "success";
						strRedirectUrl = json.getString("ReturnUrl");
					}
					return "success";
				} else if (!json.getBoolean("IsValid")) {
					if (json.has("IsFareUpdate")
							&& json.getBoolean("IsFareUpdate")) {
						strResponseType = "Fare update";
						strJson = resultString;

						paymentModel = hotelDataHandler
								.getHotelPaymentDetails(resultString);

						return paymentModel.getCurrency()
								+ " "
								+ String.format(new Locale("en"), "%.3f",
										paymentModel.getTotalAmount());
					} else {
						strResponseType = "Hotel unavailable";
						return json.getString("Message");
					}
				} else {
					strResponseType = "failed";
					return json.getString("Message");
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();

			if (result != null) {
				if (result.equalsIgnoreCase("success")) {
					Intent web = new Intent(HotelPaxActivity.this,
							WebActivity.class);
					web.putExtra("url", strRedirectUrl);
					startActivity(web);
				} else if (strResponseType.equalsIgnoreCase("Fare update")) {
					showAlertPayment(getResources().getString(
							R.string.hotel_fare_update, result));
				} else if (strResponseType
						.equalsIgnoreCase("Hotel unavailable")) {
					showAlertPayment(result);
				} else {
					showAlertPayment(result);
				}
			} else
				Toast.makeText(getApplicationContext(),
						"Some thing went wrong", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

	}

	public void showAlertPayment(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setMessage(msg);

		alertDialog.setPositiveButton(getResources().getString(R.string.ok),
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (strResponseType.equalsIgnoreCase("Fare update")) {
							showSummary();
							new HotelPaymentService().execute();
						} else if (strResponseType
								.equalsIgnoreCase("Hotel unavailable")) {
							HotelDetailsActivity.activityHoteDetails.finish();
							finish();
						} else {
							finish();
						}
					}
				});

		if (strResponseType.equalsIgnoreCase("Fare update"))
			alertDialog.setNegativeButton(
					getResources().getString(R.string.cancel),
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
							HotelDetailsActivity.activityHoteDetails.finish();
						}
					});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	private void loadLocale() {
		// TODO Auto-generated method stub
		SharedPreferences sharedpreferences = this.getSharedPreferences(
				"CommonPrefs", Context.MODE_PRIVATE);
		String lang = sharedpreferences.getString("Language", "en");
		System.out.println("Default lang: " + lang);
		if (lang.equalsIgnoreCase("ar")) {
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext()
					.getResources()
					.updateConfiguration(
							config,
							this.getBaseContext().getResources()
									.getDisplayMetrics());
			CommonFunctions.lang = "ar";
		} else {
			myLocale = new Locale(lang);
			saveLocale(lang);
			Locale.setDefault(myLocale);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			this.getBaseContext()
					.getResources()
					.updateConfiguration(
							config,
							this.getBaseContext().getResources()
									.getDisplayMetrics());
			CommonFunctions.lang = "en";
		}
	}

	public void saveLocale(String lang) {
		CommonFunctions.lang = lang;
		String langPref = "Language";
		SharedPreferences prefs = this.getSharedPreferences("CommonPrefs",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(langPref, lang);
		editor.commit();
	}

	public void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setMessage(msg);

		alertDialog.setPositiveButton(getResources().getString(R.string.ok),
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

}
