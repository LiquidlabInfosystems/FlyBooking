package com.travel.flybooking;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.ImageDownloaderTask;
import com.travel.common_handlers.*;
import com.travel.datahandler.MyAccountDataHandler;
import com.travel.model.MyAccountItem.MyAccountFlightItem;
import com.travel.model.MyAccountItem.MyAccountHotelItem;
import com.travel.model.MyAccountItem.MyAccountFlightItem.ItenaryDetailsInfoList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewBookigActivity extends Activity {

	private Locale myLocale;
	Context ctx;
	LinearLayout lldeparture_1, lldeparture_2, lldeparture_3, lldeparture_4;
	String AirlineName, FromTime, FromDate, AirlineLogo, FlightNo, Origin,
			OriginCity, EndTime, EndDate, Destination, DestinationCity;
	JSONObject response;
	Dialog loaderDialog;

	String strRefNo, strEmail, strPhno, strSearchType, strCCode;

	MyAccountDataHandler myAccountDataHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_view_booking);
		
		myAccountDataHandler = new MyAccountDataHandler();

		strRefNo = getIntent().getStringExtra("refno");
		strEmail = getIntent().getStringExtra("email");
		strPhno = getIntent().getStringExtra("phonno");
		strCCode = getIntent().getStringExtra("ctrycode");

		lldeparture_1 = (LinearLayout) findViewById(R.id.departure_1);
		lldeparture_2 = (LinearLayout) findViewById(R.id.departure_2);
		lldeparture_3 = (LinearLayout) findViewById(R.id.departure_3);
		lldeparture_4 = (LinearLayout) findViewById(R.id.departure_4);

		loaderDialog = new Dialog(this, android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		new GetBookingService().execute();
	}

	public void clicker(View v) {
		if (v.getId() == R.id.iv_back)
			finish();
	}

	private void parseJsonData(String strJsonData) {
		// TODO Auto-generated method stub
		try {
			response = new JSONObject(strJsonData);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			JSONObject object = response.getJSONObject("data");
			Log.e("object  ", object.toString());

			JSONArray items = object.getJSONArray("Item");
			object = items.getJSONObject(1);

			strSearchType = object.getString("SearchType");

			if (strSearchType.equalsIgnoreCase("flight")) {

				MyAccountFlightItem myAccountFlightItem = myAccountDataHandler
						.getMyFlightDetails(items);
				
				String strTcktStatus = myAccountFlightItem.getTransactionStatus();

				if (strTcktStatus.equalsIgnoreCase("CancelAndRefundRequestedToHO"))
					((TextView) findViewById(R.id.tv_status))
							.setText(getResources().getString(
									R.string.waiting_cancellation));
				else if (strTcktStatus
						.equalsIgnoreCase("CancelAndRefundRequestedToSupplier"))
					((TextView) findViewById(R.id.tv_status))
							.setText(getResources().getString(
									R.string.waiting_refund));
				else
					((TextView) findViewById(R.id.tv_status))
							.setText(strTcktStatus);

				Button btnCancelTicket = (Button) findViewById(R.id.btn_cancel_ticket);
				Button btnViewTicket = (Button) findViewById(R.id.btn_view_ticket);

				if (strTcktStatus.toLowerCase().contains("ticketed")) {

					final String urlParams = "transDetailId="
							+ URLEncoder.encode(String.valueOf(myAccountFlightItem
									.getTransactionTypeDetailId()), "UTF-8")
							+ "&bookingId="
							+ URLEncoder.encode(String.valueOf(myAccountFlightItem
									.getFlightBookingId()), "UTF-8")
							+ "&supplierPnr="
							+ URLEncoder.encode(
									myAccountFlightItem.getSupplierPnr(), "UTF-8");

					btnCancelTicket.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							new CancelBackService().execute(urlParams);
						}
					});

					final int strSelectedTransactionID = myAccountFlightItem.getTransactionID();
					
					btnViewTicket.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent voucher = new Intent(getApplicationContext(),
									WebViewActivity.class);
							voucher.putExtra("url", CommonFunctions.main_url
									+ CommonFunctions.lang
									+ "/Flight/PrintTicketMyBooking?bookingId="
									+ strSelectedTransactionID);
							startActivity(voucher);
						}
					});

					btnCancelTicket.setVisibility(View.VISIBLE);
					btnViewTicket.setVisibility(View.VISIBLE);
				} else {
					btnCancelTicket.setVisibility(View.GONE);
					btnViewTicket.setVisibility(View.GONE);
				}

				ItenaryDetailsInfoList[] itenaryDetailsInfoList = myAccountFlightItem
						.getItenaryDetailsInfoList();
				
				parseFlightData(itenaryDetailsInfoList);
				
				((LinearLayout) findViewById(R.id.ll_flight_details_lv))
						.setVisibility(View.VISIBLE);

			} else {

				object = items.getJSONObject(0);

				object = object.getJSONObject("Data");

				object = object.getJSONObject("Voucher");
				MyAccountHotelItem myAccountHotelItem = myAccountDataHandler
						.getHotelDetails(object);
				parseHotelData(myAccountHotelItem);
				((LinearLayout) findViewById(R.id.ll_hotel_details_lv))
						.setVisibility(View.VISIBLE);
			}

			Log.e("else if", response.toString());
		} catch (Exception e) {

		}
	}

	private void parseFlightData(ItenaryDetailsInfoList[] itenaryDetailsInfoList) {
		try {
			
			LinearLayout llFlight = null;
			llFlight = (LinearLayout) findViewById(R.id.ll_flight4);
			llFlight.removeAllViews();
			llFlight = (LinearLayout) findViewById(R.id.ll_flight3);
			llFlight.removeAllViews();
			llFlight = (LinearLayout) findViewById(R.id.ll_flight2);
			llFlight.removeAllViews();
			llFlight = (LinearLayout) findViewById(R.id.ll_flight1);
			llFlight.removeAllViews();
			lldeparture_2.setVisibility(View.GONE);
			lldeparture_3.setVisibility(View.GONE);
			lldeparture_4.setVisibility(View.GONE);

			String[] classArray = this.getResources().getStringArray(
					R.array.class_spinner_items);
			String strClass = null;

			int k = 0, i = 0, length = itenaryDetailsInfoList.length;
			for (i = 0; i < length; i++) {

				final View view = getLayoutInflater().inflate(
						R.layout.item_mybooking_flight, null);

				if (!itenaryDetailsInfoList[i].getTransitTime()
						.equalsIgnoreCase("")) {
					((TextView) view.findViewById(R.id.tv_transit_time))
							.setText(getString(R.string.layover)
									+ " : "
									+ itenaryDetailsInfoList[i]
											.getTransitTime());
					((TextView) view.findViewById(R.id.tv_transit_time))
							.setVisibility(View.VISIBLE);
				} else {
					++k;
					switch (k) {
					case 1:
						llFlight = (LinearLayout) findViewById(R.id.ll_flight1);
						break;

					case 2:
						lldeparture_2.setVisibility(View.VISIBLE);
						llFlight = (LinearLayout) findViewById(R.id.ll_flight2);
						break;

					case 3:
						lldeparture_3.setVisibility(View.VISIBLE);
						llFlight = (LinearLayout) findViewById(R.id.ll_flight3);
						break;

					case 4:
						lldeparture_4.setVisibility(View.VISIBLE);
						llFlight = (LinearLayout) findViewById(R.id.ll_flight4);
						break;

					default:
						break;
					}
				}

				ImageView ivFlightLogo = (ImageView) view
						.findViewById(R.id.iv_flight_logo);
				InputStream ims;
				Drawable d;
				try {
					// get input stream
					ims = getAssets().open(
							itenaryDetailsInfoList[i].getAirlineLogo());
					// load image as Drawable
					d = Drawable.createFromStream(ims, null);
					// set image to ImageView
					ivFlightLogo.setImageDrawable(d);
					ims.close();
					d = null;
				} catch (IOException ex) {
					ex.printStackTrace();
					ivFlightLogo.setImageResource(R.mipmap.ic_no_image);
				}

				((TextView) view.findViewById(R.id.tv_flight_name))
						.setText(itenaryDetailsInfoList[i].getAirlineName()
								+ " " + itenaryDetailsInfoList[i].getFlightNo());
				((TextView) view.findViewById(R.id.tv_depart_time))
						.setText(itenaryDetailsInfoList[i].getFromTime());
				((TextView) view.findViewById(R.id.tv_depart_date))
						.setText(itenaryDetailsInfoList[i].getFromDate());
				((TextView) view.findViewById(R.id.tv_depart_airport))
						.setText(itenaryDetailsInfoList[i].getOrigin() + " "
								+ itenaryDetailsInfoList[i].getOriginCity());
				((TextView) view.findViewById(R.id.tv_arrival_time))
						.setText(itenaryDetailsInfoList[i].getEndTime());
				((TextView) view.findViewById(R.id.tv_arrival_date))
						.setText(itenaryDetailsInfoList[i].getEndDate());
				((TextView) view.findViewById(R.id.tv_arrival_airport))
						.setText(itenaryDetailsInfoList[i].getDestination()
								+ " "
								+ itenaryDetailsInfoList[i]
										.getDestinationCity());

				if (itenaryDetailsInfoList[i].isReturnFlightIndicator()) {
					((TextView) findViewById(R.id.tv_flight_type))
							.setText(getString(R.string.retur));
					((ImageView) findViewById(R.id.iv_flight_type))
							.setImageResource(R.mipmap.ic_arrival);
					((ImageView) view.findViewById(R.id.flightlogo))
							.setImageResource(R.mipmap.ic_arrival);
				}

				switch (itenaryDetailsInfoList[i].getFlightSearchClass()) {
				case "Y":
					strClass = classArray[0];
					break;
				case "W":
					strClass = classArray[1];
					break;

				case "C":
					strClass = classArray[2];
					break;

				case "F":
					strClass = classArray[3];
					break;

				default:
					break;
				}

				((TextView) view.findViewById(R.id.tv_flight_class))
						.setText(strClass);

				llFlight.addView(view);

			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("@json_parser", e.getMessage());
			handler.sendEmptyMessage(4);
		}
	}

	@SuppressWarnings("deprecation")
	private void parseHotelData(MyAccountHotelItem myAccountHotelItem) {

		try {

			ImageView ivRoomLogo = (ImageView) findViewById(R.id.iv_hotel_logo);
			ivRoomLogo.setImageBitmap(null);
			if (myAccountHotelItem.getImageUrl() != null
					&& !myAccountHotelItem.getImageUrl().contains("no_image"))
				new ImageDownloaderTask(ivRoomLogo).execute(myAccountHotelItem
						.getImageUrl());
			else
				ivRoomLogo.setImageResource(R.mipmap.ic_no_image);

			((TextView) findViewById(R.id.tv_hotel_name))
					.setText(myAccountHotelItem.getPropName());
			((TextView) findViewById(R.id.tv_hotel_address))
					.setText(myAccountHotelItem.getHotelAddress());
			((TextView) findViewById(R.id.tv_check_in_date))
					.setText(myAccountHotelItem.getCheckin());
			((TextView) findViewById(R.id.tv_check_out_date))
					.setText(myAccountHotelItem.getCheckOut());
			((TextView) findViewById(R.id.tv_room_count))
					.setText(String.valueOf(myAccountHotelItem.getNumOfRooms()));
			((TextView) findViewById(R.id.tv_night_count))
					.setText(String.valueOf(myAccountHotelItem.getNumOfNights()));
			((TextView) findViewById(R.id.tv_adult_count))
					.setText(String.valueOf(myAccountHotelItem.getNumOfAdult()));
			((TextView) findViewById(R.id.tv_child_count))
					.setText(String.valueOf(myAccountHotelItem.getNumOfChild()));
			((TextView) findViewById(R.id.tv_price)).setText(myAccountHotelItem
					.getCurency() + " " + myAccountHotelItem.getTotalAmount());
			((TextView) findViewById(R.id.tv_status_hotel))
					.setText(myAccountHotelItem.getVoucherstatus());

			((TextView) findViewById(R.id.tv_cancel_not_allowed_hotel))
					.setVisibility(View.GONE);

			Button btnCancelTicket = (Button) findViewById(R.id.btn_cancel_ticket_hotel);
			final Button btnViewTicket = (Button) findViewById(R.id.btn_view_ticket_hotel);
			if (myAccountHotelItem.getVoucherstatus().toLowerCase()
					.contains("booked")) {

				final String strCancelUrl = myAccountHotelItem
						.getCancelVoucherUrl();

				btnCancelTicket.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new CancelBackService().execute(strCancelUrl);
					}
				});

				btnViewTicket.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent voucher = new Intent(getApplicationContext(),
								WebViewActivity.class);
						voucher.putExtra(
								"url",
								CommonFunctions.main_url
										+ CommonFunctions.lang
										+ "/Hotel/ViewMyVoucher?transDetailId="
										+ strRefNo
										+ "&AmountVisibility=true&PrintwithFare=true&showzoption=");
						startActivity(voucher);
					}
				});

				String strcheckindate = myAccountHotelItem.getCheckin();
				strcheckindate = strcheckindate.replace(". ", "/");
				strcheckindate = strcheckindate.replace(", ", "/");

				String currentDateTimeString = DateFormat.getDateInstance(
						DateFormat.LONG, Locale.ENGLISH).format(new Date());

				currentDateTimeString = new SimpleDateFormat("MMM/dd/yyyy",
						new Locale("en"))
						.format(new Date(currentDateTimeString));

				SimpleDateFormat formatter = new SimpleDateFormat(
						"MMM/dd/yyyy", new Locale("en"));
				Date date1 = formatter.parse(strcheckindate);
				Date date2 = formatter.parse(currentDateTimeString);

				if (date2.compareTo(date1) < 0)
					btnCancelTicket.setVisibility(View.VISIBLE);
				else {
					btnCancelTicket.setVisibility(View.GONE);
					((TextView) findViewById(R.id.tv_cancel_not_allowed_hotel))
							.setVisibility(View.VISIBLE);
				}

				btnViewTicket.setVisibility(View.VISIBLE);
			} else {
				btnCancelTicket.setVisibility(View.GONE);
				btnViewTicket.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private class GetBookingService extends AsyncTask<Void, Void, String> {

		boolean blNoRecords = false;

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
				String urlParameters = null;
				if (!strEmail.equals("")) {
					urlParameters = "referenceNumber="
							+ URLEncoder.encode(strRefNo, "UTF-8")
							+ "&emailId="
							+ URLEncoder.encode(strEmail, "UTF-8");
				} else {
					urlParameters = "referenceNumber="
							+ URLEncoder.encode(strRefNo, "UTF-8")
							+ "&mobileNumber="
							+ URLEncoder.encode(strPhno, "UTF-8")
							+ "&countryCode="
							+ URLEncoder.encode(strCCode, "UTF-8");
				}

				String request = CommonFunctions.main_url
						+ CommonFunctions.lang
						+ "/MyAccountApi/ReferenceNumber";
				String res = new HttpHandler().makeServiceCallWithParams(request, urlParameters);
				
				System.out.println("Response"+res);

				JSONObject json = new JSONObject(res);

				JSONObject object = json.getJSONObject("data");
				Log.e("object  ", object.toString());

				if (object.getBoolean("Isvalid")) {
					return res;

				} else {
					Log.e("error  ", object.getString("messages"));
					blNoRecords = true;
					return object.getString("messages");
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendEmptyMessage(2);
			} catch (NullPointerException e) {
				// Log exception
				e.printStackTrace();
				handler.sendEmptyMessage(3);
			} catch (IOException e) {
				// Log exception
				e.printStackTrace();
				handler.sendEmptyMessage(1);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				handler.sendEmptyMessage(2);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			System.out.println("result" + result);
			if (result != null) {
				if (blNoRecords) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							ViewBookigActivity.this);

					alertDialog.setMessage(result);

					alertDialog.setPositiveButton(
							getResources().getString(R.string.ok),
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent manage = new Intent(
											ViewBookigActivity.this,
											ManageBookingActivity.class);
									startActivity(manage);
									finish();
								}
							});

					alertDialog.setCancelable(false);
					alertDialog.show();
				} else {
					parseJsonData(result);
				}
			}

			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
			super.onPostExecute(result);
		}

	}

	private class CancelBackService extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			loaderDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String request = null;

			try {

				if (strSearchType.equalsIgnoreCase("flight")) {
					request = CommonFunctions.main_url + CommonFunctions.lang
							+ "/MyAccountApi/CancelTicket?" + params[0];

				} else {
					request = params[0];
				}

				String res = new HttpHandler().makeServiceCall(request);

				System.out.println("res" + res);

				if (strSearchType.equalsIgnoreCase("flight")) {
					JSONObject jObj = new JSONObject(res);

					jObj = jObj.getJSONObject("data");
					Log.d("object  ", jObj.toString());

					if (jObj.getBoolean("Isvalid")) {
						return jObj.getString("Confirmationmessage");
					}
				} else {
					return res.replace("\"", "");
				}

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendEmptyMessage(2);
			} catch (NullPointerException e) {
				// Log exception
				e.printStackTrace();
				handler.sendEmptyMessage(3);
			} catch (IOException e) {
				// Log exception
				e.printStackTrace();
				handler.sendEmptyMessage(1);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handler.sendEmptyMessage(2);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			if (result != null) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						ViewBookigActivity.this);

				alertDialog.setMessage(result);

				alertDialog.setPositiveButton(
						getResources().getString(R.string.ok),
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								new GetBookingService().execute();
							}
						});

				alertDialog.setCancelable(false);
				alertDialog.show();
			} 

			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
			super.onPostExecute(result);
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				loaderDialog.dismiss();
				showAlert("There is a problem on your Network. Please try again later.", true);

			} else if (msg.what == 2) {

				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				showAlert("There is a problem on your application. Please report it.", true);

			} else if (msg.what == 3) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				showAlert(getResources().getString(R.string.no_result), true);
			} else if (msg.what == 4) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				showAlert("Something went wrong!! Please try again later..",
						false);
			}

		}
	};

	public void showAlert(String msg, final boolean isExit) {
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
