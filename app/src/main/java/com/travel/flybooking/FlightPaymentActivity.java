package com.travel.flybooking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.model.PaymentModel;
import com.travel.model.PaymentModel.AvailablePaymentGateways;
import com.travel.common_handlers.*;
import com.travel.datahandler.FlightDataHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class FlightPaymentActivity extends Activity {

	private Locale myLocale;
	String strSessionId = null;
	String sID = null, strJson, strCurrency, proceedUrl = null;
	String confirmMsg = null, confirmMsg1 = null;
	boolean blIsRoundTrip;
	ImageView ivKnet, ivMigs;
	Dialog loaderDialog;
	String selectedPayment = "2";
	CommonFunctions cf;
	PaymentModel flightPaymentItem;
	FlightDataHandler flightDataHandler;

	Double totalPriceKnet, totalPriceMigs, ServKnet, servMigs,
	baggageFee = 0.0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_flight_payment);
		cf = new CommonFunctions(this);
		flightDataHandler = new FlightDataHandler();
		setHeader();

		loaderDialog = new Dialog(FlightPaymentActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);
//		((ImageView) loaderDialog.findViewById(R.id.iv_loader))
//				.setImageResource(R.drawable.flight_loader);

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

		strJson = getIntent().getExtras().getString("json", "");
		try {
			flightPaymentItem = flightDataHandler.getFlightPaymentDetails(strJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showSummary();

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
				new BackService().execute();
			else
				cf.showAlertDialog(getResources().getString(
						R.string.error_check_box));
			break;

		case R.id.tv_rules:
			Intent rules = new Intent(FlightPaymentActivity.this,
					WebViewActivity.class);
			rules.putExtra("url", CommonFunctions.main_url
					+ CommonFunctions.lang + "/Shared/Terms");
			startActivity(rules);
			break;

		default:
			break;
		}
	}

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
				.setText(baggageFee > 0 ? strCurrency
						+ " "
						+ String.format(new Locale("en"), "%.3f",
								baggageFee) : null);

		AvailablePaymentGateways[] availablePaymentGateways = flightPaymentItem.getAvailablePaymentGateways();
		
		for (int i = 0; i < availablePaymentGateways.length; ++i) {
			if (availablePaymentGateways[i].getPaymentGateWayId() == 2) {
				ServKnet = availablePaymentGateways[i].isIsPercentage() ? availablePaymentGateways[i].
						getServiceCharge()
						* (flightPaymentItem.getTotalAmount() + baggageFee)
						/ 100 : availablePaymentGateways[i].
						getServiceCharge()
						* flightPaymentItem.getConvertionRate();
				totalPriceKnet = ServKnet + baggageFee
						+ flightPaymentItem.getTotalAmount();
				isKnetAcitve = true;

			} else if (availablePaymentGateways[i].getPaymentGateWayId()== 8) {
				servMigs = availablePaymentGateways[i].isIsPercentage() ? availablePaymentGateways[i].
						getServiceCharge()
						* (flightPaymentItem.getTotalAmount() + baggageFee)
						/ 100 : availablePaymentGateways[i].
						getServiceCharge()
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
				String tempURL = flightPaymentItem.getDeeplinkUrl().replace("{0}", selectedPayment);
				
				String resultString = new HttpHandler().makeServiceCall(tempURL);

				System.out
						.println("------------------Received result-------------"
								+ resultString);

				JSONObject json = new JSONObject(resultString);
				json = json.getJSONObject("data");
				if (json.getBoolean("Isvalid")) {
					if (json.get("ResponseType").toString()
							.equalsIgnoreCase("Pay")) {
						proceedUrl = json.getString("DeeplinkUrl");
						proceedUrl = proceedUrl.replace("{0}", selectedPayment);
						return "url";
					} else if (json.get("ResponseType").toString()
							.equalsIgnoreCase("FareChange")) {
						flightPaymentItem = flightDataHandler.getFlightPaymentDetails(json.toString());
						confirmMsg = json.getString("Confirmationmessage");
						return "fare change";
					} else if (json.get("ResponseType").toString()
							.equalsIgnoreCase("ProceedBook")) {
						proceedUrl = json.getString("DeeplinkUrl");
						return "url";
					} else if (json.get("ResponseType").toString()
							.equalsIgnoreCase("BookFareChange")) {
						flightPaymentItem = flightDataHandler.getFlightPaymentDetails(json.toString());
						confirmMsg = json.getString("Confirmationmessage");
						proceedUrl = json.getString("DeeplinkUrl");
						proceedUrl = proceedUrl.replace("{0}", selectedPayment);
						return "book fare change";
					} else if (json.get("ResponseType").toString()
							.equalsIgnoreCase("BookingFailed")) {
						confirmMsg1 = json.getString("Confirmationmessage");
						return "booking failed";
					} else if (json.get("ResponseType").toString()
							.equalsIgnoreCase("FlyDubaiTimeOut")) {
						confirmMsg1 = json.getString("Confirmationmessage");
						return "redirect";
					} else if (json.get("ResponseType").toString()
							.equalsIgnoreCase("bookFareUnavail")) {
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
					Intent web = new Intent(FlightPaymentActivity.this,
							WebActivity.class);
					web.putExtra("url", proceedUrl);
					startActivity(web);
				} else if (result.equalsIgnoreCase("booking failed")) {
					showAlert(confirmMsg1);
				} else if (result.equalsIgnoreCase("bookFareUnavail")) {
					showAlert(confirmMsg);
				} else if (result.equalsIgnoreCase("book fare change")) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							FlightPaymentActivity.this);

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
											FlightPaymentActivity.this,
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
							FlightPaxActivity.activityFlightPax.finish();
							FlightDetails.activityFlightDetails.finish();

						} else if (confirmMsg != null) {
							showSummary();
							new BackService().execute();
						} else {
							Intent home = new Intent(
									FlightPaymentActivity.this,
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
