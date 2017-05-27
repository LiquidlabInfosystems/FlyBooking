package com.travel.flybooking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.common_handlers.*;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelPaymentActivity extends Activity {

	private Locale myLocale;
	String strSessionId = null;
	String sID = null, strJson, deepUrl, strCurrency, strImageUrl,
			strHotelName, strHotelAddress, strRooms, strNights, strCheckin,
			strCheckout, strRedirectUrl = null;
	int adultCount, childCount;
	boolean blIsRoundTrip;
	JSONObject jobj, jObj;
	ImageView ivHotelLogo;
	String selectedPayment = "2";
	String strResponseType = null;

	Dialog loaderDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_hotel_payment);

		loaderDialog = new Dialog(HotelPaymentActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);
//		((ImageView) loaderDialog.findViewById(R.id.iv_loader))
//				.setImageResource(R.drawable.hotel_loader);

		setViewValues();
		getIntentValues();
		showSummary();
	}

	private void getIntentValues() {
		strImageUrl = getIntent().getExtras().getString("img_url");
		strHotelName = getIntent().getExtras().getString("strHotelName");
		strHotelAddress = getIntent().getExtras().getString("strHotelAddress");
		strRooms = getIntent().getExtras().getString("RoomCount");
		strNights = getIntent().getExtras().getString("NightCount");
		strCheckin = getIntent().getExtras().getString("Checkin");
		strCheckout = getIntent().getExtras().getString("Checkout");
		adultCount = getIntent().getExtras().getInt("TotalAdultCount");
		childCount = getIntent().getExtras().getInt("TotalChildCount");

		strJson = getIntent().getExtras().getString("json", "");
		try {
			jobj = new JSONObject(strJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setViewValues() {
		TextView tvCity = (TextView) findViewById(R.id.tv_Hotel_city);
		TextView tvCheckinDate = (TextView) findViewById(R.id.tv_checkin_date);
		TextView tvCheckoutDate = (TextView) findViewById(R.id.tv_checkout_date);
		TextView tvpassengerCount = (TextView) findViewById(R.id.tv_passenger_count);
		TextView tvroomCount = (TextView) findViewById(R.id.tv_room_count);
		tvCity.setText(HotelResultActivity.strCity);
		tvCheckinDate.setText(HotelResultActivity.strCheckinDate);
		tvCheckoutDate.setText(HotelResultActivity.strCheckoutDate);
		tvpassengerCount.setText(String
				.valueOf(HotelResultActivity.passengerCount));
		tvroomCount.setText(String.valueOf(HotelResultActivity.roomCount));
	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		case R.id.btn_pay:
			if (((CheckBox) findViewById(R.id.cb_terms)).isChecked())
				new HotelPaymentService().execute();
			else
				new CommonFunctions(this).showAlertDialog(getResources()
						.getString(R.string.error_check_box));
			break;

		case R.id.tv_rules:
			Intent rules = new Intent(HotelPaymentActivity.this,
					WebActivity.class);
			rules.putExtra("url", CommonFunctions.main_url
					+ CommonFunctions.lang + "/Shared/Terms");
			startActivity(rules);
			break;

		default:
			break;
		}
	}

	Double totalPriceKnet, totalPriceMigs, ServKnet, servMigs,
			dblConversionRate = 1.0;

	private void showSummary() {
		final ImageView ivKnet, ivVisa_Master;
		boolean isKnetActive = false, isMigsActive = false;
		ivKnet = (ImageView) findViewById(R.id.iv_knet);
		ivVisa_Master = (ImageView) findViewById(R.id.iv_migs);

		try {

			deepUrl = jobj.getString("ProceedPayUrl");
			strCurrency = jobj.getString("DisplayCurrency");

			JSONArray jArray = jobj.getJSONArray("AvailablePaymentGateways");
			for (int i = 0; i < jArray.length(); ++i) {
				jObj = jArray.getJSONObject(i);
				if (jObj.getInt("PaymentGateWayId") == 2) {

					dblConversionRate = jObj.getDouble("ConvertionRate");

					ServKnet = jObj.getBoolean("IsPercentage") ? jObj
							.getDouble("ServiceCharge")
							* jobj.getDouble("TotalAmount")
							* dblConversionRate
							/ 100 : jObj.getDouble("ServiceCharge")
							* dblConversionRate;
					totalPriceKnet = ServKnet + jobj.getDouble("BaseFare")
							* dblConversionRate;
					isKnetActive = true;
				} else if (jObj.getInt("PaymentGateWayId") == 8) {

					dblConversionRate = jObj.getDouble("ConvertionRate");

					servMigs = jObj.getBoolean("IsPercentage") ? jObj
							.getDouble("ServiceCharge")
							* jobj.getDouble("TotalAmount")
							* dblConversionRate
							/ 100 : jObj.getDouble("ServiceCharge")
							* dblConversionRate;
					totalPriceMigs = servMigs + jobj.getDouble("BaseFare")
							* dblConversionRate;
					isMigsActive = true;
				}
			}

			((TextView) findViewById(R.id.tv_price)).setText(strCurrency
					+ " "
					+ String.format(new Locale("en"), "%.3f",
							jobj.getDouble("BaseFare") * dblConversionRate));

			if (isKnetActive) {
				ivKnet.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ivKnet.setBackgroundResource(R.drawable.orange_button_curved_edge);
						ivVisa_Master.setBackgroundColor(color.transparent);

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
				isMigsActive = jobj.getBoolean("IsMigsPaymentGatewayActive");

			if (isMigsActive)
				ivVisa_Master.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ivVisa_Master
								.setBackgroundResource(R.drawable.orange_button_curved_edge);
						ivKnet.setBackgroundColor(color.transparent);
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

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				String tempURL = deepUrl
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
				System.out
						.println("------------------Received result-------------"
								+ resultString);

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
						jobj = new JSONObject(strJson);

						return json.getString("DisplayCurrency")
								+ " "
								+ String.format(new Locale("en"), "%.3f",
										jobj.getDouble("TotalAmount")
												* dblConversionRate);
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
					Intent web = new Intent(HotelPaymentActivity.this,
							WebActivity.class);
					web.putExtra("url", strRedirectUrl);
					startActivity(web);
				} else if (strResponseType.equalsIgnoreCase("Fare update")) {
					showAlert(getResources().getString(
							R.string.hotel_fare_update, result));
				} else if (strResponseType
						.equalsIgnoreCase("Hotel unavailable")) {
					showAlert(result);
				} else {
					showAlert(result);
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

}
