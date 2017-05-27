package com.travel.flybooking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.CustomDatePickerDialog;
import com.travel.flybooking.adapter.FlightDetailsInflator;
import com.travel.model.FlightPaxModel;
import com.travel.model.FlightPaxSubmissionModel;
import com.travel.model.PaymentModel;
import com.travel.model.FlightResultItem;
import com.travel.model.FlightPaxModel.BaggageList;
import com.travel.model.FlightPaxModel.BoardingDetails;
import com.travel.model.PaymentModel.AvailablePaymentGateways;
import com.travel.common_handlers.HttpHandler;
import com.travel.common_handlers.UrlParameterBuilder;
import com.travel.datahandler.FlightDataHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class FlightPaxActivity extends Activity {
	Calendar dateSelected = Calendar.getInstance();
	private DatePickerDialog datePickerDialog;

	int Adultcount, Childcount, Infantcount;
	String strAdult, strChild, strInfant;
	String[] Countrycode;
	AssetManager am;
	ArrayList<String> arrayCountry;
	LinearLayout llLoginLayout;
	LinearLayout llDetails;
	Spinner spCountrycode;
	TextView txt_continewithoutlogin, txt_Email, txt_countrycode, txt_mobileno,
			mDateDisplay, tvPrice;
	EditText etEmailAddress, EtMobileNo, ll_layoutetEmailAddress,
			ll_layoutetPassword;
	String JSonPassengerDetails = null;
	Dialog loaderDialog;

	final Calendar c = Calendar.getInstance();
	int mYear = c.get(Calendar.YEAR);
	int mMonth = c.get(Calendar.MONTH);
	int mDay = c.get(Calendar.DAY_OF_MONTH);
	int maxmnth = c.get(Calendar.DECEMBER);

	int maxYear = mYear - 12;
	int maxMonth = mMonth;
	int maxDay = mDay;

	int minYear = mYear - 100;
	int minMonth = mMonth;
	int minDay = mDay;

	int childmaxYear = mYear - 2;
	int childmaxMonth = mMonth;
	int childmaxDay = mDay + 1;

	int childminYear = mYear - 12;
	int childminMonth = mMonth;
	int childminDay = mDay + 2;

	int infantmaxYear = mYear;
	int infantmaxMonth = mMonth;
	int infantmaxDay = mDay;

	int infantminYear = mYear - 2;
	int infantminMonth = mMonth;
	int infantminDay = mDay + 2;

	int passportmaxYear = mYear + 25;
	int passportmaxMonth = mMonth;
	int passportmaxDay = mDay;

	int passportminYear = mYear;
	int passportminMonth = mMonth;
	int passportminDay = mDay;

	String s = null, strCntryCode, sID = null, tripId = null;
	int y = 1980, m = 01, d = 01;
	JSONObject json, fareObj;
	JSONArray postjson, jarray;
	public String[] arrTitle = { "Title", "Male", "Female" };
	ArrayList<String> arrayChild, arrayAdult;
	Double dblBaggage = 0.0, dblFlightPrice = 0.0;

	static Activity activityFlightPax;
	FlightResultItem fItem;

	FlightDataHandler flightDataHandler;
	FlightPaxModel[] flightPaxItem = null;
	PaymentModel flightPaymentItem;

	// Payment section
	private Locale myLocale;
	String strSessionId = null;
	String strJson, strCurrency, proceedUrl = null;
	String confirmMsg = null, confirmMsg1 = null;
	boolean blIsRoundTrip;
	ImageView ivKnet, ivMigs;
	String selectedPayment = "2";
	CommonFunctions cf;

	Double totalPriceKnet, totalPriceMigs, ServKnet, servMigs,
			baggageFee = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activityFlightPax = this;
		flightDataHandler = new FlightDataHandler();
		cf = new CommonFunctions(this);
		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_flight_paxpage);

		sID = getIntent().getExtras().getString("sID", null);
		tripId = getIntent().getExtras().getString("tripID", null);

		final boolean isGroup = getIntent().getBooleanExtra("isGroup", false);
		fItem = new FlightResultItem();
		fItem = isGroup ? FlightResultGroupActivity.selectedFItem
				: FlightResultActivity.selectedFItem;

		jarray = fItem.getFareQuoteArray();
		setHeader();
		initilize();
		printFareSummary();
		loadAssets();

		new GetPaxDetails().execute();

	}

	private void printFareSummary() {
		// TODO Auto-generated method stub
		int i = 0;

		try {
			for (i = 0; i < jarray.length(); i++) {
				fareObj = jarray.getJSONObject(i);

				if (!FlightResultActivity.blChild && i == 1)
					++i;
				switch (i) {
				case 0:
					strAdult = fareObj.getString("PassengerType");
					Adultcount = fareObj.getInt("Count");
					break;
				case 1:
					strChild = fareObj.getString("PassengerType");
					Childcount = fareObj.getInt("Count");
					break;
				case 2:
					strInfant = fareObj.getString("PassengerType");
					Infantcount = fareObj.getInt("Count");
					break;
				default:
					break;
				}
			}

			tvPrice = (TextView) findViewById(R.id.tv_price);
			dblFlightPrice = Double.parseDouble(fItem.getStrDisplayRate());
			String price = String.format(new Locale("en"), "%.3f",
					dblFlightPrice);
			tvPrice.setText(CommonFunctions.strCurrency + " " + price);

			final LinearLayout llFlightItem = ((LinearLayout) findViewById(R.id.ll_flight_details));
			(new FlightDetailsInflator()).showFlightDetails(fItem.getJarray(),
					llFlightItem, FlightPaxActivity.this);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void initilize() {

		loaderDialog = new Dialog(FlightPaxActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		// ((ImageView) loaderDialog.findViewById(R.id.iv_loader))
		// .setImageResource(R.drawable.flight_loader);

		txt_continewithoutlogin = (TextView) findViewById(R.id.txt_contine);
		txt_Email = (TextView) findViewById(R.id.txt_email);
		txt_countrycode = (TextView) findViewById(R.id.txt_ccode);
		txt_mobileno = (TextView) findViewById(R.id.txt_mobileno);
		etEmailAddress = (EditText) findViewById(R.id.et_emailid);
		EtMobileNo = (EditText) findViewById(R.id.et_mobilnos);

		arrayAdult = new ArrayList<String>();
		arrayChild = new ArrayList<String>();

		llDetails = (LinearLayout) findViewById(R.id.ll_details);
		llLoginLayout = (LinearLayout) findViewById(R.id.ll_loginlayout);
		ll_layoutetEmailAddress = (EditText) findViewById(R.id.edt_email);
		ll_layoutetPassword = (EditText) findViewById(R.id.edt_password);

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

		String textemail = "<font color=#000000>Email ID</font><font color=#e32c18> *</font>";
		txt_Email.setText(Html.fromHtml(textemail));

		String textccode = "<font color=#000000>Country Code</font><font color=#e32c18> *</font>";
		txt_countrycode.setText(Html.fromHtml(textccode));

		String textmobileno = "<font color=#000000>Mobile Number</font><font color=#e32c18> *</font>";
		txt_mobileno.setText(Html.fromHtml(textmobileno));

		if (!CommonFunctions.lang.equals("en")) {
			String textar = "أو  الاستمرار من دون تسجيل دخول";

			txt_continewithoutlogin.setText(Html.fromHtml(textar));

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
				FlightPaxActivity.this, R.layout.tv_spinner, Countrycode);
		adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		spCountrycode.setAdapter(adapter);
		spCountrycode
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						strCntryCode = spCountrycode.getSelectedItem()
								.toString();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		spCountrycode.setSelection(((ArrayAdapter<String>) spCountrycode
				.getAdapter()).getPosition("+965"));

		ivKnet = (ImageView) findViewById(R.id.iv_knet);
		ivMigs = (ImageView) findViewById(R.id.iv_migs);

		OnClickListener payMethodListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.iv_knet:
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

					ivKnet.setBackgroundResource(R.drawable.orange_button_curved_edge);
					ivMigs.setBackgroundColor(Color.TRANSPARENT);
					break;
				case R.id.iv_migs:
					temp = String.format(new Locale("en"), "%.3f", servMigs);
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

					ivKnet.setBackgroundColor(Color.TRANSPARENT);
					ivMigs.setBackgroundResource(R.drawable.orange_button_curved_edge);
					break;
				default:
					break;
				}
			}
		};

		ivKnet.setOnClickListener(payMethodListener);
		ivMigs.setOnClickListener(payMethodListener);

	}

	private void setHeader() {
		// TODO Auto-generated method stub
		Bundle bundle = FlightResultActivity.bundle;

		LinearLayout llHeader = (LinearLayout) findViewById(R.id.ll_header_city);
		TextView tvFlightDates = (TextView) findViewById(R.id.tv_date);

		String strFlightType = bundle.getString("flight_type", "");
		int tripNo = bundle.getInt("trip_nos");

		Resources resources = getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int px = 5 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		LayoutParams llParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		llParams.setMargins(px, px, px, px);

		if (!strFlightType.equalsIgnoreCase("Multicity")) {

			String strFromCode = bundle.getString("from1", "");
			String strToCode = bundle.getString("to1", "");

			TextView tvFrom = new TextView(this);
			tvFrom.setText(strFromCode);
			tvFrom.setTypeface(Typeface.create("sans-serif-condensed",
					Typeface.NORMAL));
			tvFrom.setTextColor(Color.WHITE);
			tvFrom.setTextSize(16f);

			TextView tv = new TextView(this);
			tv.setLayoutParams(llParams);
			tv.setText(getResources().getString(R.string.to));
			tv.setTypeface(Typeface.create("sans-serif-condensed",
					Typeface.NORMAL));
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(16f);

			TextView tvTo = new TextView(this);
			tvTo.setText(strToCode);
			tvTo.setTypeface(Typeface.create("sans-serif-condensed",
					Typeface.NORMAL));
			tvTo.setTextColor(Color.WHITE);
			tvTo.setTextSize(16f);

			llHeader.addView(tvFrom);
			llHeader.addView(tv);
			llHeader.addView(tvTo);

			int padding = 30 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
			((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
					padding, 0);

		} else {
			String temp1, temp2, temp3, temp4;
			temp1 = bundle.getString("from1", "");
			temp2 = bundle.getString("to1", "");
			temp3 = bundle.getString("from2", "");
			temp4 = bundle.getString("to2", "");
			if (tripNo == 2) {

				TextView tvFrom1 = new TextView(this);
				tvFrom1.setText(temp1);
				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom1.setTextColor(Color.WHITE);
				tvFrom1.setTextSize(16f);

				TextView tv1 = new TextView(this);
				tv1.setLayoutParams(llParams);
				tv1.setText(getResources().getString(R.string.to));
				tv1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv1.setTextColor(Color.WHITE);
				tv1.setTextSize(16f);

				TextView tvTo1 = new TextView(this);
				tvTo1.setText(temp2 + ",");
				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo1.setTextColor(Color.WHITE);
				tvTo1.setTextSize(16f);

				TextView tvFrom2 = new TextView(this);
				tvFrom2.setLayoutParams(llParams);
				tvFrom2.setText(temp3);
				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom2.setTextColor(Color.WHITE);
				tvFrom2.setTextSize(16f);

				TextView tv2 = new TextView(this);
				tv2.setText(getResources().getString(R.string.to));
				tv2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv2.setTextColor(Color.WHITE);
				tv2.setTextSize(16f);

				TextView tvTo2 = new TextView(this);
				tvTo2.setLayoutParams(llParams);
				tvTo2.setText(temp4);
				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo2.setTextColor(Color.WHITE);
				tvTo2.setTextSize(16f);

				llHeader.addView(tvFrom1);
				llHeader.addView(tv1);
				llHeader.addView(tvTo1);
				llHeader.addView(tvFrom2);
				llHeader.addView(tv2);
				llHeader.addView(tvTo2);

				int margin = 30 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
						margin, 0);

			} else if (tripNo == 3) {

				px = 3 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				llParams.setMargins(px, px, px, px);

				TextView tvFrom1 = new TextView(this);
				tvFrom1.setText(temp1);
				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom1.setTextColor(Color.WHITE);
				tvFrom1.setTextSize(15f);

				TextView tv1 = new TextView(this);
				tv1.setLayoutParams(llParams);
				tv1.setText(getResources().getString(R.string.to));
				tv1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv1.setTextColor(Color.WHITE);
				tv1.setTextSize(16f);

				TextView tvTo1 = new TextView(this);
				tvTo1.setText(temp2 + ",");
				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo1.setTextColor(Color.WHITE);
				tvTo1.setTextSize(15f);

				TextView tvFrom2 = new TextView(this);
				tvFrom2.setLayoutParams(llParams);
				tvFrom2.setText(temp3);
				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom2.setTextColor(Color.WHITE);
				tvFrom2.setTextSize(15f);

				TextView tv2 = new TextView(this);
				tv2.setText(getResources().getString(R.string.to));
				tv2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv2.setTextColor(Color.WHITE);
				tv2.setTextSize(16f);

				TextView tvTo2 = new TextView(this);
				tvTo2.setLayoutParams(llParams);
				tvTo2.setText(temp4 + ",");
				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo2.setTextColor(Color.WHITE);
				tvTo2.setTextSize(15f);

				TextView tvFrom3 = new TextView(this);
				tvFrom3.setText(bundle.getString("from3", ""));
				tvFrom3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom3.setTextColor(Color.WHITE);
				tvFrom3.setTextSize(15f);

				TextView tv3 = new TextView(this);
				tv3.setLayoutParams(llParams);
				tv3.setText(getResources().getString(R.string.to));
				tv3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv3.setTextColor(Color.WHITE);
				tv3.setTextSize(16f);

				TextView tvTo3 = new TextView(this);
				tvTo3.setText(bundle.getString("to3", ""));
				tvTo3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo3.setTextColor(Color.WHITE);
				tvTo3.setTextSize(15f);

				llHeader.addView(tvFrom1);
				llHeader.addView(tv1);
				llHeader.addView(tvTo1);
				llHeader.addView(tvFrom2);
				llHeader.addView(tv2);
				llHeader.addView(tvTo2);
				llHeader.addView(tvFrom3);
				llHeader.addView(tv3);
				llHeader.addView(tvTo3);

				int margin = 10 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
						margin, 0);

			} else {
				px = 2 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				llParams.setMargins(px, px, px, px);

				TextView tvFrom1 = new TextView(this);
				tvFrom1.setText(temp1);
				tvFrom1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom1.setTextColor(Color.WHITE);
				tvFrom1.setTextSize(14f);

				TextView tv1 = new TextView(this);
				tv1.setLayoutParams(llParams);
				tv1.setText(getResources().getString(R.string.to));
				tv1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv1.setTextColor(Color.WHITE);
				tv1.setTextSize(16f);

				TextView tvTo1 = new TextView(this);
				tvTo1.setText(temp2 + ",");
				tvTo1.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo1.setTextColor(Color.WHITE);
				tvTo1.setTextSize(14f);

				TextView tvFrom2 = new TextView(this);
				tvFrom2.setLayoutParams(llParams);
				tvFrom2.setText(temp3);
				tvFrom2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom2.setTextColor(Color.WHITE);
				tvFrom2.setTextSize(14f);

				TextView tv2 = new TextView(this);
				tv2.setText(getResources().getString(R.string.to));
				tv2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv2.setTextColor(Color.WHITE);
				tv2.setTextSize(16f);

				TextView tvTo2 = new TextView(this);
				tvTo2.setLayoutParams(llParams);
				tvTo2.setText(temp4 + ",");
				tvTo2.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo2.setTextColor(Color.WHITE);
				tvTo2.setTextSize(14f);

				TextView tvFrom3 = new TextView(this);
				tvFrom3.setText(bundle.getString("from3", ""));
				tvFrom3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom3.setTextColor(Color.WHITE);
				tvFrom3.setTextSize(14f);

				TextView tv3 = new TextView(this);
				tv3.setLayoutParams(llParams);
				tv3.setText(getResources().getString(R.string.to));
				tv3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv3.setTextColor(Color.WHITE);
				tv3.setTextSize(16f);

				TextView tvTo3 = new TextView(this);
				tvTo3.setText(bundle.getString("to3", "") + ",");
				tvTo3.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo3.setTextColor(Color.WHITE);
				tvTo3.setTextSize(14f);

				TextView tvFrom4 = new TextView(this);
				tvFrom4.setLayoutParams(llParams);
				tvFrom4.setText(bundle.getString("from4", ""));
				tvFrom4.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvFrom4.setTextColor(Color.WHITE);
				tvFrom4.setTextSize(14f);

				TextView tv4 = new TextView(this);
				tv4.setText(getResources().getString(R.string.to));
				tv4.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tv4.setTextColor(Color.WHITE);
				tv4.setTextSize(16f);

				TextView tvTo4 = new TextView(this);
				tvTo4.setLayoutParams(llParams);
				tvTo4.setText(bundle.getString("to4", ""));
				tvTo4.setTypeface(Typeface.create("sans-serif-condensed",
						Typeface.NORMAL));
				tvTo4.setTextColor(Color.WHITE);
				tvTo4.setTextSize(14f);

				llHeader.addView(tvFrom1);
				llHeader.addView(tv1);
				llHeader.addView(tvTo1);
				llHeader.addView(tvFrom2);
				llHeader.addView(tv2);
				llHeader.addView(tvTo2);
				llHeader.addView(tvFrom3);
				llHeader.addView(tv3);
				llHeader.addView(tvTo3);
				llHeader.addView(tvFrom4);
				llHeader.addView(tv4);
				llHeader.addView(tvTo4);

				int margin = 5 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				((LinearLayout) findViewById(R.id.ll_header)).setPadding(0, 0,
						margin, 0);
			}

		}
		String details = FlightResultActivity.strDetails;

		tvFlightDates.setText(details);

	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		case R.id.btn_pay:
			if (((CheckBox) findViewById(R.id.cb_terms)).isChecked())
				submit();
			else
				cf.showAlertDialog(getResources().getString(
						R.string.error_check_box));
			break;

		case R.id.txt_forgotpassword:
			Intent forgot = new Intent(FlightPaxActivity.this,
					LoginActivity.class);
			forgot.putExtra("from_pax", true);
			startActivity(forgot);
			break;

		case R.id.tv_signup:
			Intent register = new Intent(FlightPaxActivity.this,
					RegisterActivity.class);
			register.putExtra("from_pax", true);
			startActivity(register);
			break;

		case R.id.btn_login:
			if (!Loginvalidate()) {
				Toast.makeText(getApplicationContext(),
						"Something went wrong. Please try again later",
						Toast.LENGTH_LONG).show();

			} else {
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

		case R.id.tv_rules:
			Intent rules = new Intent(FlightPaxActivity.this, WebActivity.class);
			rules.putExtra("url", CommonFunctions.main_url
					+ CommonFunctions.lang + "/Shared/Terms");
			startActivity(rules);

		default:
			break;
		}
	}

	// private void setBaggage() {
	//
	// tvPrice.setText(CommonFunctions.strCurrency
	// + " "
	// + String.format(new Locale("en"), "%.3f", dblFlightPrice
	// + dblBaggage));
	// }

	public void layoutView() {
		// TODO Auto-generated method stub

		int id = 1;
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				FlightPaxActivity.this, R.array.title_spinner_items,
				R.layout.tv_spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);

		final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
				FlightPaxActivity.this, R.layout.tv_spinner, arrayCountry);
		adapter2.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);

		Spinner Spn = null;
		LinearLayout ll;
		LinearLayout llBaggage;
		TextView tv;
		String strTemp = null;

		for (int i = 1; i <= Adultcount; i++) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_flight_pax_details, null);

			view.setId(id);
			id++;
			view.setTag(strAdult);

			((TextView) view.findViewById(R.id.txt_caption)).setText(strAdult
					+ " " + i);
			if (!CommonFunctions.lang.equals("en")) {

				((TextView) view.findViewById(R.id.txt_caption))
						.setText("بالغ " + " " + i);
			}
			BoardingDetails[] boardingDetails = flightPaxItem[i - 1]
					.getBoradingDetails();
			for (int count = 0; count < boardingDetails.length; ++count) {
				if (boardingDetails[count].getAllowBaggages()) {

					ArrayList<String> arrayAdult = new ArrayList<String>();
					BaggageList[] baggageList = boardingDetails[count]
							.getBaggageList();
					arrayAdult.add(getResources().getString(
							R.string.checked_baggage_option_1));
					for (int x = 0; x < baggageList.length; ++x) {
						arrayAdult.add(baggageList[x].getValue());
					}

					if (count == 0) {
						Spn = (Spinner) view.findViewById(R.id.selectbaggage);
						llBaggage = (LinearLayout) view
								.findViewById(R.id.ll_checkedbaggage);
						llBaggage.setVisibility(View.VISIBLE);
					} else if (count == 1) {
						((TextView) view
								.findViewById(R.id.tv_select_baggage_hd))
								.setText(getResources().getString(
										R.string.checked_baggage_onward));
						Spn = (Spinner) view
								.findViewById(R.id.selectbaggage_round);
						((LinearLayout) view
								.findViewById(R.id.ll_baggage_round))
								.setVisibility(View.VISIBLE);
					}

					ArrayAdapter<String> Adultadapter = new ArrayAdapter<String>(
							FlightPaxActivity.this, R.layout.tv_spinner,
							arrayAdult);
					Adultadapter
							.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
					Spn.setAdapter(Adultadapter);

					Spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							if (position != 0) {
								String baggage = parent.getSelectedItem()
										.toString();
								baggage = baggage.substring(
										baggage.indexOf(CommonFunctions.strCurrency) + 4,
										baggage.length());
								dblBaggage = dblBaggage
										+ Double.parseDouble(baggage);
							}
							// setBaggage();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

				}

			}

			String Strpmonth = String.valueOf(passportminMonth + 1);
			if (Strpmonth.length() == 1) {
				Strpmonth = "0" + String.valueOf(passportminMonth + 1);
			}
			String strCuurentDay = String.valueOf(mDay);
			if (strCuurentDay.length() == 1) {
				strCuurentDay = "0" + String.valueOf(mDay);
			}

			Spn = (Spinner) view.findViewById(R.id.Spn_title);
			Spn.setAdapter(adapter);

			Spn = (Spinner) view.findViewById(R.id.spn_nationality);
			Spn.setAdapter(adapter2);

			ll = (LinearLayout) view.findViewById(R.id.ll_dateofbirth);
			ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					datePickerDialog = new CustomDatePickerDialog(
							FlightPaxActivity.this,
							new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker datepicker,
										int year, int monthOfYear,
										int dayOfMonth) {
									// TODO Auto-generated method stub
									try {
										String str_day = String
												.valueOf(dayOfMonth);
										if (str_day.length() == 1) {
											str_day = "0"
													+ String.valueOf(dayOfMonth);
										}
										String str_month = String
												.valueOf(monthOfYear + 1);
										if (str_month.length() == 1) {
											str_month = "0"
													+ String.valueOf(monthOfYear + 1);
										}
										String str_year = String.valueOf(year);
										if (str_year.length() == 1) {
											str_day = "0"
													+ String.valueOf(year);
										}
										s = String.valueOf(str_day + "/"
												+ str_month + "/" + str_year);

										((TextView) view
												.findViewById(R.id.edt_day))
												.setText(s);
									} catch (Exception e) {

									}

								}

							}, minYear, minMonth, minDay, maxYear, maxMonth,
							maxDay);
					datePickerDialog.show();
					TextView tv = (TextView) view.findViewById(R.id.edt_day);
					String strDOBview = tv.getText().toString();

					String[] dateParts = strDOBview.split("/");
					String StrDays = dateParts[0];
					String StrMonths = dateParts[1];
					String StrYears = dateParts[2];
					System.out.println(StrDays);
					System.out.println(StrMonths);
					System.out.println(StrYears);
					datePickerDialog.updateDate(Integer.parseInt(StrYears),
							Integer.parseInt(StrMonths) - 1,
							Integer.parseInt(StrDays));

				}
			});
			tv = (TextView) view.findViewById(R.id.edt_day);
			tv.setText("01/01/1980");
			tv = (TextView) view.findViewById(R.id.txt_title);
			strTemp = "<font color=#000000>Title</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_fname);
			strTemp = "<font color=#000000>First Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_lname);
			strTemp = "<font color=#000000>Last Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_nationality);
			strTemp = "<font color=#000000>Nationality</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_dob);
			strTemp = "<font color=#000000>Date Of Birth</font><font color=#e32c18> *</font>";
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

				tv = (TextView) view.findViewById(R.id.txt_nationality);
				strTemp = "<font color=#000000>الجنسية</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_dob);
				strTemp = "<font color=#000000>تاريخ الميلاد</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

			}
			llDetails.addView(view);
		}
		for (int j = 1; j <= Childcount; j++) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_flight_pax_details, null);

			view.setId(id);
			id++;
			view.setTag(strChild);
			((TextView) view.findViewById(R.id.txt_caption)).setText(strChild
					+ " " + j);
			if (!CommonFunctions.lang.equals("en")) {

				((TextView) view.findViewById(R.id.txt_caption))
						.setText("طفل  " + " " + j);
			}

			BoardingDetails[] boardingDetails = flightPaxItem[j - 1]
					.getBoradingDetails();

			for (int count = 0; count < boardingDetails.length; ++count) {

				if (boardingDetails[count].getAllowBaggages()) {

					ArrayList<String> arrayChild = new ArrayList<String>();
					BaggageList[] baggageList = boardingDetails[count]
							.getBaggageList();

					arrayChild.add(getResources().getString(
							R.string.checked_baggage_option_1));

					for (int x = 0; x < baggageList.length; ++x) {
						arrayChild.add(baggageList[x].getValue());
					}

					if (count == 0) {
						Spn = (Spinner) view.findViewById(R.id.selectbaggage);
						llBaggage = (LinearLayout) view
								.findViewById(R.id.ll_checkedbaggage);
						llBaggage.setVisibility(View.VISIBLE);
					} else if (count == 1) {
						((TextView) view
								.findViewById(R.id.tv_select_baggage_hd))
								.setText(getResources().getString(
										R.string.checked_baggage_onward));
						Spn = (Spinner) view
								.findViewById(R.id.selectbaggage_round);
						((LinearLayout) view
								.findViewById(R.id.ll_baggage_round))
								.setVisibility(View.VISIBLE);
					}

					ArrayAdapter<String> Childadapter = new ArrayAdapter<String>(
							FlightPaxActivity.this, R.layout.tv_spinner,
							arrayChild);
					Childadapter
							.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
					Spn.setAdapter(Childadapter);
					Spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							if (position != 0) {
								String baggage = parent.getSelectedItem()
										.toString();
								baggage = baggage.substring(
										baggage.indexOf(CommonFunctions.strCurrency) + 4,
										baggage.length());
								dblBaggage = dblBaggage
										+ Double.parseDouble(baggage);
							}
							// setBaggage();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
						}
					});
				}

			}

			((TextView) view.findViewById(R.id.edt_day)).setText("01/01/"
					+ String.valueOf(mYear - 2));

			Spn = (Spinner) view.findViewById(R.id.Spn_title);
			Spn.setAdapter(adapter);

			Spn = (Spinner) view.findViewById(R.id.spn_nationality);
			Spn.setAdapter(adapter2);

			ll = (LinearLayout) view.findViewById(R.id.ll_dateofbirth);
			ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					datePickerDialog = new CustomDatePickerDialog(
							FlightPaxActivity.this,
							new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker datepicker,
										int year, int monthOfYear,
										int dayOfMonth) {
									// TODO Auto-generated method stub
									try {
										String str_day = String
												.valueOf(dayOfMonth);
										if (str_day.length() == 1) {
											str_day = "0"
													+ String.valueOf(dayOfMonth);
										}
										String str_month = String
												.valueOf(monthOfYear + 1);
										if (str_month.length() == 1) {
											str_month = "0"
													+ String.valueOf(monthOfYear + 1);
										}
										String str_year = String.valueOf(year);
										if (str_year.length() == 1) {
											str_day = "0"
													+ String.valueOf(year);
										}
										TextView txt_date = (TextView) view
												.findViewById(R.id.edt_day);

										s = String.valueOf(str_day + "-"
												+ str_month + "-" + str_year);

										txt_date.setText(s);

									} catch (Exception e) {

									}

								}

							}, childminYear, childminMonth, childminDay,
							childmaxYear, childmaxMonth, childmaxDay);
					datePickerDialog.show();
					TextView tv = (TextView) view.findViewById(R.id.edt_day);
					String strDOBview = tv.getText().toString();

					String[] dateParts = strDOBview.split("/");
					String StrDays = dateParts[0];
					String StrMonths = dateParts[1];
					String StrYears = dateParts[2];
					System.out.println(StrDays);
					System.out.println(StrMonths);
					System.out.println(StrYears);
					datePickerDialog.updateDate(Integer.parseInt(StrYears),
							Integer.parseInt(StrMonths) - 1,
							Integer.parseInt(StrDays));

				}
			});

			tv = (TextView) view.findViewById(R.id.txt_title);
			strTemp = "<font color=#000000>Title</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_fname);
			strTemp = "<font color=#000000>First Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_lname);
			strTemp = "<font color=#000000>Last Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_nationality);
			strTemp = "<font color=#000000>Nationality</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_dob);
			strTemp = "<font color=#000000>Date Of Birth</font><font color=#e32c18> *</font>";
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

				tv = (TextView) view.findViewById(R.id.txt_nationality);
				strTemp = "<font color=#000000>الجنسية</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_dob);
				strTemp = "<font color=#000000>تاريخ الميلاد</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

			}
			llDetails.addView(view);
		}

		for (int k = 1; k <= Infantcount; k++) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_flight_pax_details, null);

			view.setId(id);
			id++;
			view.setTag(strInfant);

			((TextView) view.findViewById(R.id.txt_caption)).setText(strInfant
					+ " " + k);
			if (!CommonFunctions.lang.equals("en")) {

				((TextView) view.findViewById(R.id.txt_caption))
						.setText("الرضيع  " + " " + k);
			}

			String Strpmonth = String.valueOf(passportminMonth + 1);
			if (Strpmonth.length() <= 1) {
				Strpmonth = "0" + String.valueOf(passportminMonth + 1);
			}

			String strCuurentDay = String.valueOf(mDay);
			if (strCuurentDay.length() == 1) {
				strCuurentDay = "0" + String.valueOf(mDay);
			}

			((TextView) view.findViewById(R.id.edt_day)).setText("01/01/"
					+ String.valueOf(mYear));

			Spn = (Spinner) view.findViewById(R.id.Spn_title);
			Spn.setAdapter(adapter);

			Spn = (Spinner) view.findViewById(R.id.spn_nationality);
			Spn.setAdapter(adapter2);

			ll = (LinearLayout) view.findViewById(R.id.ll_dateofbirth);
			ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// TODO Auto-generated method stub
					datePickerDialog = new CustomDatePickerDialog(
							FlightPaxActivity.this,
							new OnDateSetListener() {
								@Override
								public void onDateSet(DatePicker datepicker,
										int year, int monthOfYear,
										int dayOfMonth) {
									// TODO Auto-generated method stub
									try {
										String str_day = String
												.valueOf(dayOfMonth);
										if (str_day.length() == 1) {
											str_day = "0"
													+ String.valueOf(dayOfMonth);
										}
										String str_month = String
												.valueOf(monthOfYear + 1);
										if (str_month.length() == 1) {
											str_month = "0"
													+ String.valueOf(monthOfYear + 1);
										}
										String str_year = String.valueOf(year);
										if (str_year.length() == 1) {
											str_day = "0"
													+ String.valueOf(year);
										}
										TextView txt_date = (TextView) view
												.findViewById(R.id.edt_day);

										s = String.valueOf(str_day + "-"
												+ str_month + "-" + str_year);
										txt_date.setText(s);
									} catch (Exception e) {

									}

								}

							}, infantminYear, infantminMonth, infantminDay,
							infantmaxYear, infantmaxMonth, infantmaxDay);
					datePickerDialog.show();
					TextView tv = (TextView) view.findViewById(R.id.edt_day);
					String strDOBview = tv.getText().toString();

					String[] dateParts = strDOBview.split("/");
					String StrDays = dateParts[0];
					String StrMonths = dateParts[1];
					String StrYears = dateParts[2];
					System.out.println(StrDays);
					System.out.println(StrMonths);
					System.out.println(StrYears);
					datePickerDialog.updateDate(Integer.parseInt(StrYears),
							Integer.parseInt(StrMonths) - 1,
							Integer.parseInt(StrDays));
				}
			});

			tv = (TextView) view.findViewById(R.id.txt_title);
			strTemp = "<font color=#000000>Title</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_fname);
			strTemp = "<font color=#000000>First Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_lname);
			strTemp = "<font color=#000000>Last Name</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_nationality);
			strTemp = "<font color=#000000>Nationality</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_dob);
			strTemp = "<font color=#000000>Date Of Birth</font><font color=#e32c18> *</font>";
			tv.setText(Html.fromHtml(strTemp));

			tv = (TextView) view.findViewById(R.id.txt_passportno);

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

				tv = (TextView) view.findViewById(R.id.txt_nationality);
				strTemp = "<font color=#000000>الجنسية</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

				tv = (TextView) view.findViewById(R.id.txt_dob);
				strTemp = "<font color=#000000>تاريخ الميلاد</font><font color=#e32c18> *</font>";
				tv.setText(Html.fromHtml(strTemp));

			}
			llDetails.addView(view);
		}
	}

	@SuppressWarnings("deprecation")
	private void submit() {

		ArrayList<String> PassportNoList = new ArrayList<String>();
		ArrayList<String> arrayFLList = new ArrayList<String>();

		PassportNoList.clear();
		int flag = 0;
		String strFirstName, strLastName, strFLName, strDOB, strNationality;
		EditText etFirstName = null, etLastName = null;
		TextView tvDOB = null;

		Spinner spGender = null, spNationality = null;
		LinearLayout llBaggage = null;
		int intGender = 0, i;

		String BaggageIds[] = new String[2];
		int passCount = Adultcount + Childcount + Infantcount;
		FlightPaxSubmissionModel[] flightPaxItems = new FlightPaxSubmissionModel[passCount];

		for (i = 0; i < passCount; i++) {

			View view = llDetails.findViewById(i + 1);
			String strPassengerType = view.getTag().toString();

			etFirstName = (EditText) view.findViewById(R.id.edt_firstname);
			strFirstName = etFirstName.getText().toString();
			etLastName = (EditText) view.findViewById(R.id.edt_lastname);
			strLastName = etLastName.getText().toString();
			strFLName = strFirstName + " " + strLastName;

			tvDOB = (TextView) view.findViewById(R.id.edt_day);
			strDOB = tvDOB.getText().toString();

			strDOB = new SimpleDateFormat("dd/MM/yyyy", new Locale("en"))
					.format(new Date(strDOB));

			spGender = (Spinner) view.findViewById(R.id.Spn_title);
			intGender = spGender.getSelectedItemPosition();
			String strGender = arrTitle[intGender];

			spNationality = (Spinner) view.findViewById(R.id.spn_nationality);
			String strNation = spNationality.getSelectedItem().toString();

			strNationality = strNation.substring(Math.max(
					strNation.length() - 2, 0));

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
					spGender.requestFocus();
					Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.err_title_req),
							Toast.LENGTH_SHORT).show();
				}
				flag = 1;
			}

			// if (spNationality.getSelectedItemPosition() == 0) {
			// flag = 1;
			// }

			if (strNation
					.equals(getResources().getString(R.string.nationality))) {
				if (flag == 0) {
					spNationality.requestFocus();
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(
									R.string.err_msg_select_nation),
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

				llBaggage = (LinearLayout) view
						.findViewById(R.id.ll_checkedbaggage);

				String strGenderUrl = strGender.substring(0, 1);
				strGenderUrl = strPassengerType.toLowerCase()
						.contains("infant") ? strGenderUrl + "i" : strGenderUrl;

				if (strPassengerType.equalsIgnoreCase("adult"))
					strGender = strGender.equalsIgnoreCase("male") ? "Mr"
							: "Ms";
				else
					strGender = strGender.equalsIgnoreCase("male") ? "Mstr"
							: "Miss";

				flightPaxItems[i] = new FlightPaxSubmissionModel();
				flightPaxItems[i].setPassengerId(i);
				flightPaxItems[i].setTripId(tripId);
				flightPaxItems[i].setFirstName(strFirstName);
				flightPaxItems[i].setMiddileName("");
				flightPaxItems[i].setLastName(strLastName);
				flightPaxItems[i].setEmail(email);
				flightPaxItems[i].setMobileNumber(mobileno);
				flightPaxItems[i].setMobileCode(strCntryCode);
				flightPaxItems[i].setGender(strGenderUrl);
				flightPaxItems[i].setPassengerType(strPassengerType);
				flightPaxItems[i].setFrequentFlyerNo(null);
				flightPaxItems[i].setPassportNumber("");
				flightPaxItems[i].setDateOfBirth(strDOB);
				flightPaxItems[i].setPassportExpiryDate("01/01/2016");
				flightPaxItems[i].setPassportPlaceOfIssue("");
				flightPaxItems[i].setCitizenship(strNationality);
				flightPaxItems[i].setTitle(strGender);
				flightPaxItems[i].setIsLoggedIn(CommonFunctions.loggedIn);
				if (llBaggage.getVisibility() == View.VISIBLE) {
					BaggageIds[0] = String.valueOf(((Spinner) view
							.findViewById(R.id.selectbaggage))
							.getSelectedItemPosition());

					if (((LinearLayout) view
							.findViewById(R.id.ll_baggage_round))
							.getVisibility() == View.VISIBLE) {
						BaggageIds[1] = String.valueOf(((Spinner) view
								.findViewById(R.id.selectbaggage_round))
								.getSelectedItemPosition());
					}
					flightPaxItems[i].setBaggageList(BaggageIds);
				} else {
					flightPaxItems[i].setBaggageList(null);
				}
			}
		}
		if (flag == 0) {
			try {
				JSonPassengerDetails = new FlightDataHandler()
						.createPaxDetailsString(flightPaxItems);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new PaxDetailSubmission().execute();
		}

	}

	public class PaxDetailSubmission extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub+
			loaderDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				String urlParameters = "responseType="
						+ URLEncoder.encode("PaymentRequestFromPax", "UTF-8")
						+ "&passengerDetails="
						+ URLEncoder.encode(JSonPassengerDetails, "UTF-8")
						+ "&PaymentGatewayID="
						+ URLEncoder.encode(selectedPayment, "UTF-8");

				String request = CommonFunctions.main_url
						+ CommonFunctions.lang
						+ "/FlightApi/PaymentRequestFromPax?IsPassportOptional="
						+ true + "&sID=" + sID;

				System.out.println("url request " + request);

				System.out.println("urlParameters " + urlParameters);

				String res = new HttpHandler().makeServiceCallWithParams(
						request, urlParameters);

				System.out.println("res" + res);

				JSONObject json = new JSONObject(res);

				json = json.getJSONObject("data");

				if (json.getBoolean("Isvalid")
						&& json.getString("messages").equalsIgnoreCase(
								"success")) {
					if (json.getString("ResponseType").equalsIgnoreCase("Pay")) {
						proceedUrl = json.getString("DeeplinkUrl");
						proceedUrl = proceedUrl.replace("{0}", selectedPayment);
						return "url";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"FareChange")) {
						flightPaymentItem = flightDataHandler
								.getFlightPaymentDetails(json.toString());
						confirmMsg = json.getString("Confirmationmessage");
						return "fare change";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"ProceedBook")) {
						proceedUrl = json.getString("DeeplinkUrl");
						return "url";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"BookFareChange")) {
						flightPaymentItem = flightDataHandler
								.getFlightPaymentDetails(json.toString());
						confirmMsg = json.getString("Confirmationmessage");
						proceedUrl = json.getString("DeeplinkUrl");
						proceedUrl = proceedUrl.replace("{0}", selectedPayment);
						return "book fare change";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"BookingFailed")) {
						confirmMsg1 = json.getString("Confirmationmessage");
						return "booking failed";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"FlyDubaiTimeOut")) {
						confirmMsg1 = json.getString("Confirmationmessage");
						return "redirect";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"bookFareUnavail")) {
						confirmMsg = json.getString("Confirmationmessage");
						confirmMsg1 = "bookFareUnavail";
						return "bookFareUnavail";
					}
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

				if (result.equalsIgnoreCase("redirect")) {
					showAlert(confirmMsg1);
				} else if (result.equalsIgnoreCase("fare change")) {
					showAlert(confirmMsg);
				} else if (result.equalsIgnoreCase("url")) {
					Intent web = new Intent(FlightPaxActivity.this,
							WebActivity.class);
					web.putExtra("url", proceedUrl);
					startActivity(web);
				} else if (result.equalsIgnoreCase("booking failed")) {
					showAlert(confirmMsg1);
				} else if (result.equalsIgnoreCase("bookFareUnavail")) {
					showAlert(confirmMsg);
				} else if (result.equalsIgnoreCase("book fare change")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							FlightPaxActivity.this);

					alertDialog.setMessage(confirmMsg);

					alertDialog.setPositiveButton(
							getResources().getString(R.string.ok),
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									showSummary();
									Intent web = new Intent(
											FlightPaxActivity.this,
											WebActivity.class);
									web.putExtra("url", proceedUrl);
									startActivity(web);
								}
							});

					alertDialog.setNegativeButton(
							getResources().getString(R.string.cancel),
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
									FlightPaxActivity.activityFlightPax
											.finish();
									FlightDetails.activityFlightDetails
											.finish();
								}
							});

					alertDialog.setCancelable(false);
					alertDialog.show();
				}

			} else
				Toast.makeText(getApplicationContext(),
						"Some thing went wrong", Toast.LENGTH_SHORT).show();

			super.onPostExecute(result);
		}

	}

	private void loadAssets() {
		// TODO Auto-generated method stub
		am = getAssets();
		String countrylist = null;
		InputStream file1 = null;
		try {
			file1 = am.open("countrylist.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file1));
			StringBuilder builder1 = new StringBuilder();
			String line1 = null;
			while ((line1 = reader.readLine()) != null) {
				builder1.append(line1);
			}
			countrylist = builder1.toString();
			arrayCountry = new ArrayList<String>();
			if (countrylist != null) {
				JSONObject json1 = new JSONObject(countrylist);
				JSONArray airlinelist = json1.getJSONArray("countrylist");
				JSONObject c1 = null;
				for (int i = 0; i < airlinelist.length(); i++) {

					c1 = airlinelist.getJSONObject(i);
					if (i == 0) {
						arrayCountry.add(getResources().getString(
								R.string.nationality));
					} else {
						arrayCountry.add(c1.getString("CountryName") + " - "
								+ c1.getString("CountryCode"));
					}
					// Log.e("CountryName ", arrayCountry.toString());
				}
				airlinelist = null;
			}
			countrylist = null;
			file1.close();
			reader.close();
			builder1 = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class GetPaxDetails extends AsyncTask<Void, Void, String> {

		Boolean blIsloggedIn = false;

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
				String request = CommonFunctions.main_url
						+ CommonFunctions.lang
						+ "/FlightApi/GetPaxDetailsAndPaymentDetails?tripId="
						+ tripId + "&responseType=ServiceResponse&sID=" + sID;

				String res = new HttpHandler().makeServiceCall(request);
				System.out.println("res getpaxdetails=" + res);

				JSONObject jObj = new JSONObject(res);
				String tmp = jObj.getJSONObject("Paymentdetails").toString();
				jObj = jObj.getJSONObject("data");

				if (jObj.getBoolean("Isvalid")) {
					flightPaxItem = flightDataHandler.getPaxFlightItems(res);
					blIsloggedIn = jObj.getBoolean("IsLoggedIn");
					flightPaymentItem = flightDataHandler
							.getFlightPaymentDetails(tmp);

					return "success";
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

			layoutView();

			if (result != null && blIsloggedIn) {
				LinearLayout ll_LogHead = (LinearLayout) findViewById(R.id.ll_login_header);
				ll_LogHead.setVisibility(View.GONE);
				llLoginLayout.setVisibility(View.GONE);

				etEmailAddress.setText(flightPaxItem[0].getEmail());
				EtMobileNo.setText(flightPaxItem[0].getMobileNumber());

				String strMobCode = flightPaxItem[0].getMobileCode().contains(
						"+") ? flightPaxItem[0].getMobileCode() : "+"
						+ flightPaxItem[0].getMobileCode().replace(" ", "");

				spCountrycode
						.setSelection(((ArrayAdapter<String>) spCountrycode
								.getAdapter()).getPosition(strMobCode));

				View view = llDetails.findViewById(1);
				if (view.getTag().toString().equalsIgnoreCase("adult")) {
					((EditText) view.findViewById(R.id.edt_firstname))
							.setText(flightPaxItem[0].getFirstname());
					((EditText) view.findViewById(R.id.edt_lastname))
							.setText(flightPaxItem[0].getLastName());

					Spinner Spn;
					if (flightPaxItem[0].getGender() != null
							&& !flightPaxItem[0].getGender().equals("")) {
						Spn = (Spinner) view.findViewById(R.id.Spn_title);

						Spn.setSelection(flightPaxItem[0].getGender()
								.toLowerCase().contains("m") ? 1 : 2);
					}

					Spn = (Spinner) view.findViewById(R.id.spn_nationality);

					for (String listItem : arrayCountry) {
						if (listItem.substring(
								Math.max(listItem.length() - 2, 0)).contains(
								flightPaxItem[0].getCitizenship())) {
							Spn.setSelection(((ArrayAdapter<String>) Spn
									.getAdapter()).getPosition(listItem));
							break;
						}
					}

					// Spn = (Spinner) view
					// .findViewById(R.id.spn_passport_issued);
					//
					// for (String listItem : arrayCountry) {
					// if (listItem.substring(Math.max(listItem.length() -
					// 2,
					// 0)).contains(jPassList
					// .getString("PassportPlaceOfIssue"))) {
					// Spn.setSelection(((ArrayAdapter<String>) Spn
					// .getAdapter()).getPosition(listItem));
					// break;
					// }
					// }

				}
			}
			showSummary();
			super.onPostExecute(result);
		}

	}

	private class LoginService extends AsyncTask<Void, Void, String> {
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
				String urlParameters = new UrlParameterBuilder()
						.getLoginParams(ll_layoutetEmailAddress.getText()
								.toString(), ll_layoutetPassword.getText()
								.toString(), true, "flight");

				String request = CommonFunctions.main_url
						+ CommonFunctions.lang + "/MyAccountApi/AppLogIn?sID="
						+ sID;

				String res = new HttpHandler().makeServiceCallWithParams(
						request, urlParameters);

				System.out.println("res" + res);

				JSONObject jPassList = null;
				JSONObject json = new JSONObject(res);

				if (json.getBoolean("IsValid") && json.getBoolean("IsLoggedIn")) {
					blIsloggedIn = json.getBoolean("IsLoggedIn");
					JSONArray jsonItem = json.getJSONArray("pax");
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
			// Intent i = new Intent(MainActivity.this, SearchPage.class);
			// startActivity(i);
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
			if (result != null && blIsloggedIn) {
				try {
					LinearLayout ll_LogHead = (LinearLayout) findViewById(R.id.ll_login_header);
					ll_LogHead.setVisibility(View.GONE);
					llLoginLayout.setVisibility(View.GONE);
					JSONObject jPassList = new JSONObject(result);

					etEmailAddress.setText(jPassList.getString("Email"));
					EtMobileNo.setText(jPassList.getString("MobileNumber"));

					String strMobCode = jPassList.getString("MobileCode")
							.contains("+") ? jPassList.getString("MobileCode")
							: "+"
									+ jPassList.getString("MobileCode")
											.replace(" ", "");

					spCountrycode
							.setSelection(((ArrayAdapter<String>) spCountrycode
									.getAdapter()).getPosition(strMobCode));

					View view = llDetails.findViewById(1);
					if (view.getTag().toString().equalsIgnoreCase("adult")) {
						((EditText) view.findViewById(R.id.edt_firstname))
								.setText(jPassList.getString("Firstname"));
						((EditText) view.findViewById(R.id.edt_lastname))
								.setText(jPassList.getString("LastName"));

						Spinner Spn;
						if (jPassList.getString("Gender") != null
								&& !jPassList.getString("Gender").equals("")) {
							Spn = (Spinner) view.findViewById(R.id.Spn_title);

							Spn.setSelection(jPassList.getString("Gender")
									.toLowerCase().contains("m") ? 1 : 2);
						}

						Spn = (Spinner) view.findViewById(R.id.spn_nationality);

						for (String listItem : arrayCountry) {
							if (listItem.substring(
									Math.max(listItem.length() - 2, 0))
									.contains(
											jPassList.getString("Citizenship"))) {
								Spn.setSelection(((ArrayAdapter<String>) Spn
										.getAdapter()).getPosition(listItem));
								break;
							}
						}

						// Spn = (Spinner) view
						// .findViewById(R.id.spn_passport_issued);
						//
						// for (String listItem : arrayCountry) {
						// if (listItem.substring(Math.max(listItem.length() -
						// 2,
						// 0)).contains(jPassList
						// .getString("PassportPlaceOfIssue"))) {
						// Spn.setSelection(((ArrayAdapter<String>) Spn
						// .getAdapter()).getPosition(listItem));
						// break;
						// }
						// }

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

	/* Payment section codes */
	private void showSummary() {
		boolean isKnetAcitve = false, isMigsActive = false;
		((TextView) findViewById(R.id.tv_class))
				.setText(FlightResultActivity.flightClass);

		strCurrency = flightPaymentItem.getCurrency();
		String temp = String.format(new Locale("en"), "%.3f",
				flightPaymentItem.getTotalAmount());

		((TextView) findViewById(R.id.tv_price)).setText(strCurrency + " "
				+ temp);

		baggageFee = flightPaymentItem.getBoardingFares();

		((LinearLayout) findViewById(R.id.ll_baggage))
				.setVisibility(baggageFee > 0 ? View.VISIBLE : View.GONE);

		((TextView) findViewById(R.id.tv_baggage_fees))
				.setText(baggageFee > 0 ? strCurrency + " "
						+ String.format(new Locale("en"), "%.3f", baggageFee)
						: null);

		AvailablePaymentGateways[] availablePaymentGateways = flightPaymentItem
				.getAvailablePaymentGateways();

		for (int i = 0; i < availablePaymentGateways.length; ++i) {
			if (availablePaymentGateways[i].getPaymentGateWayId() == 2) {
				ServKnet = availablePaymentGateways[i].isIsPercentage() ? availablePaymentGateways[i]
						.getServiceCharge()
						* (flightPaymentItem.getTotalAmount() + baggageFee)
						/ 100
						: availablePaymentGateways[i].getServiceCharge()
								* flightPaymentItem.getConvertionRate();
				totalPriceKnet = ServKnet + baggageFee
						+ flightPaymentItem.getTotalAmount();
				isKnetAcitve = true;

			} else if (availablePaymentGateways[i].getPaymentGateWayId() == 8) {
				servMigs = availablePaymentGateways[i].isIsPercentage() ? availablePaymentGateways[i]
						.getServiceCharge()
						* (flightPaymentItem.getTotalAmount() + baggageFee)
						/ 100
						: availablePaymentGateways[i].getServiceCharge()
								* flightPaymentItem.getConvertionRate();
				totalPriceMigs = servMigs + baggageFee
						+ flightPaymentItem.getTotalAmount();
				isMigsActive = true;
			}
		}

		ivMigs.setVisibility(flightPaymentItem.isIsMigsPaymentGatewayActive() ? View.VISIBLE
				: View.GONE);

		ivMigs.setVisibility(isMigsActive ? View.VISIBLE : View.GONE);

		ivKnet.setVisibility(isKnetAcitve ? View.VISIBLE : View.GONE);

		if (!isKnetAcitve && isMigsActive)
			selectedPayment = "8";

		if (Integer.parseInt(selectedPayment) == 2) {
			ivKnet.performClick();
		} else {
			ivMigs.performClick();
		}
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

	private class BackService extends AsyncTask<Void, Void, String> {

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
				String tempURL = flightPaymentItem.getDeeplinkUrl().replace(
						"{0}", selectedPayment);

				String resultString = new HttpHandler()
						.makeServiceCall(tempURL);

				System.out
						.println("------------------Received result-------------"
								+ resultString);

				JSONObject json = new JSONObject(resultString);
				json = json.getJSONObject("data");
				if (json.getBoolean("Isvalid")) {
					if (json.getString("ResponseType").equalsIgnoreCase("Pay")) {
						proceedUrl = json.getString("DeeplinkUrl");
						proceedUrl = proceedUrl.replace("{0}", selectedPayment);
						return "url";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"FareChange")) {
						flightPaymentItem = flightDataHandler
								.getFlightPaymentDetails(json.toString());
						confirmMsg = json.getString("Confirmationmessage");
						return "fare change";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"ProceedBook")) {
						proceedUrl = json.getString("DeeplinkUrl");
						return "url";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"BookFareChange")) {
						flightPaymentItem = flightDataHandler
								.getFlightPaymentDetails(json.toString());
						confirmMsg = json.getString("Confirmationmessage");
						proceedUrl = json.getString("DeeplinkUrl");
						proceedUrl = proceedUrl.replace("{0}", selectedPayment);
						return "book fare change";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"BookingFailed")) {
						confirmMsg1 = json.getString("Confirmationmessage");
						return "booking failed";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"FlyDubaiTimeOut")) {
						confirmMsg1 = json.getString("Confirmationmessage");
						return "redirect";
					} else if (json.getString("ResponseType").equalsIgnoreCase(
							"bookFareUnavail")) {
						confirmMsg = json.getString("Confirmationmessage");
						confirmMsg1 = "bookFareUnavail";
						return "bookFareUnavail";
					}
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

				if (result.equalsIgnoreCase("redirect")) {
					showAlert(confirmMsg1);
				} else if (result.equalsIgnoreCase("fare change")) {
					showAlert(confirmMsg);
				} else if (result.equalsIgnoreCase("url")) {
					Intent web = new Intent(FlightPaxActivity.this,
							WebActivity.class);
					web.putExtra("url", proceedUrl);
					startActivity(web);
				} else if (result.equalsIgnoreCase("booking failed")) {
					showAlert(confirmMsg1);
				} else if (result.equalsIgnoreCase("bookFareUnavail")) {
					showAlert(confirmMsg);
				} else if (result.equalsIgnoreCase("book fare change")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							FlightPaxActivity.this);

					alertDialog.setMessage(confirmMsg);

					alertDialog.setPositiveButton(
							getResources().getString(R.string.ok),
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									showSummary();
									Intent web = new Intent(
											FlightPaxActivity.this,
											WebActivity.class);
									web.putExtra("url", proceedUrl);
									startActivity(web);
								}
							});

					alertDialog.setNegativeButton(
							getResources().getString(R.string.cancel),
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									finish();
								}
							});

					alertDialog.setCancelable(false);
					alertDialog.show();
				}

			} else
				Toast.makeText(getApplicationContext(),
						"Some thing went wrong", Toast.LENGTH_SHORT).show();

			super.onPostExecute(result);
		}

	}

	public void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setMessage(msg);

		alertDialog.setPositiveButton(getResources().getString(R.string.ok),
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (confirmMsg != null && confirmMsg1 != null) {

							finish();

						} else if (confirmMsg != null) {
							showSummary();
							new BackService().execute();
						} else {
							Intent home = new Intent(FlightPaxActivity.this,
									MainActivity.class);
							home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(home);
							finishAffinity();
						}
					}
				});

		if (confirmMsg != null && confirmMsg1 == null)

			alertDialog.setNegativeButton(
					getResources().getString(R.string.cancel),
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
							FlightPaxActivity.activityFlightPax.finish();
							FlightDetails.activityFlightDetails.finish();
						}
					});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

}
