package com.travel.flybooking;

import java.io.IOException;
import java.util.Locale;

import org.json.JSONException;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.ImageDownloaderTask;
import com.travel.datahandler.HotelDataHandler;
import com.travel.common_handlers.*;
import com.travel.model.HotelDetailsItem;
import com.travel.model.HotelDetailsItem.RoomCombination;
import com.travel.model.HotelDetailsRooms;
import com.travel.model.RoomTypeDetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class HotelDetailsActivity extends Activity {

	private Locale myLocale;
	String strRequestUrl = "", strImgUrl = null, strReqPaxUrl = "",
			strRoomCombination = "";
	Double pri;
	boolean blCancellation = false;

	private ViewFlipper mViewFlipper;
	ImageView ivHotelDesc, ivHotelFacility;
	TextView tvHotelDesc, tvHotelFacility;

	Button btnContinue;

	LinearLayout llHotelRoomsList;
	Dialog loaderDialog;

	static Bitmap bmp = null;

	int[] roomCom = new int[5];
	String[] roomPrice = new String[5];
	int roomCount = 0, tempCount = 0;;

	String strSessionId = null, strNightCount;
	String strHotelName = null, strHotelAddress = null;

	public static Activity activityHoteDetails;
	GestureDetector gestureDetector;

	public static RoomTypeDetails[] roomTypeDetails;
	
	HotelDetailsItem hDetailsItem;

	class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 25;
		private static final int SWIPE_MAX_OFF_PATH = 300;
		private static final int SWIPE_THRESHOLD_VELOCITY = 50;

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			System.out.println(" in onFling() :: ");
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				mViewFlipper.setInAnimation(HotelDetailsActivity.this,
						R.anim.left_in);
				mViewFlipper.setOutAnimation(HotelDetailsActivity.this,
						R.anim.left_out);
				mViewFlipper.showNext();
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				mViewFlipper.setInAnimation(HotelDetailsActivity.this,
						R.anim.right_in);
				mViewFlipper.setOutAnimation(HotelDetailsActivity.this,
						R.anim.right_out);

				mViewFlipper.showPrevious();
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		activityHoteDetails = this;

		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_hotel_details);
		setViewValues();

		strRequestUrl = getIntent().getExtras().getString("url", "");
		strImgUrl = getIntent().getExtras().getString("img_url", "");
		strSessionId = getIntent().getExtras().getString("sessionID", "");

		loaderDialog = new Dialog(HotelDetailsActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		// ((ImageView) loaderDialog.findViewById(R.id.iv_loader))
		// .setImageResource(R.drawable.hotel_loader);

		mViewFlipper = (ViewFlipper) findViewById(R.id.vf_hotel_images);

		btnContinue = (Button) findViewById(R.id.btn_continue);

		ivHotelDesc = (ImageView) findViewById(R.id.iv_hotel_description);
		ivHotelFacility = (ImageView) findViewById(R.id.iv_hotel_facility);

		tvHotelDesc = (TextView) findViewById(R.id.tv_hotel_description);
		tvHotelFacility = (TextView) findViewById(R.id.tv_hotel_facility);

		if (!strRequestUrl.equalsIgnoreCase("")) {
			new backMethod().execute();
			loaderDialog.show();
		} else
			finishAffinity();
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		mViewFlipper.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return false;
				} else {
					return true;
				}
			}
		});
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
				HotelResultActivity.strCheckinDate.length() - 5));
		tvpassengerCount.setText(String
				.valueOf(HotelResultActivity.passengerCount));
		tvroomCount.setText(String.valueOf(HotelResultActivity.roomCount));
	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;

		case R.id.iv_hotel_description:
			tvHotelDesc
					.setVisibility(tvHotelDesc.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);
			ivHotelDesc
					.setImageResource(tvHotelDesc.getVisibility() == View.GONE ? R.mipmap.plus
							: R.mipmap.minus);

			if (tvHotelDesc.getVisibility() == View.VISIBLE) {
				tvHotelDesc.setFocusableInTouchMode(true);
				tvHotelFacility.requestFocusFromTouch();
			}
			break;

		case R.id.iv_hotel_facility:
			tvHotelFacility
					.setVisibility(tvHotelFacility.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);
			ivHotelFacility
					.setImageResource(tvHotelFacility.getVisibility() == View.GONE ? R.mipmap.plus
							: R.mipmap.minus);
			if (tvHotelFacility.getVisibility() == View.VISIBLE) {
				tvHotelFacility.setFocusableInTouchMode(true);
				tvHotelFacility.requestFocusFromTouch();
			}
			break;

		case R.id.btn_continue:
			Intent hotelpax = new Intent(HotelDetailsActivity.this,
					HotelPaxActivity.class);
			hotelpax.putExtra("strHotelName", strHotelName);
			hotelpax.putExtra("strHotelAddress", strHotelAddress);
			hotelpax.putExtra("strImgUrl", strImgUrl);
			hotelpax.putExtra("total_price", pri);
			hotelpax.putExtra("NightCount", strNightCount);
			hotelpax.putExtra("roomCombination", strRoomCombination);
			hotelpax.putExtra("request_url", strReqPaxUrl);
			startActivity(hotelpax);
			break;

		case R.id.ib_next:
			mViewFlipper.setInAnimation(this, R.anim.left_in);
			mViewFlipper.setOutAnimation(this, R.anim.left_out);
			mViewFlipper.showNext();
			break;
		case R.id.ib_previous:
			mViewFlipper.setInAnimation(this, R.anim.right_in);
			mViewFlipper.setOutAnimation(this, R.anim.right_out);
			mViewFlipper.showPrevious();
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	private class backMethod extends AsyncTask<Void, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			loaderDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String resultString = makePostRequest();

			return resultString != null ? resultString : null;

		}

		@Override
		protected void onPostExecute(final String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null && !blCancellation)
				printResult(result);
			else if (result != null && blCancellation)
				parseCancellation(result);
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
		}

	}

	private String makePostRequest() {

		// making POST request.
		try {
			String resultString = new HttpHandler()
					.makeServiceCall(strRequestUrl);

			System.out.println("------------------Received result-------------"
					+ resultString);

			return resultString;
		} catch (NullPointerException e) {
			// Log exception
			e.printStackTrace();
			handler.sendEmptyMessage(3);
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handler.sendEmptyMessage(2);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private void printResult(String result) {
		try {
			if (result != null) {

				hDetailsItem = new HotelDataHandler()
						.parseHotelDetails(result);

				String temp = null;
				int i = 0;
				Resources resources = getResources();
				DisplayMetrics metrics = resources.getDisplayMetrics();
				float px = 8 * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
				Bitmap bmp = HotelResultActivity
						.getBitmapFromMemCache(strImgUrl);

				strHotelAddress = hDetailsItem.getHotelAddress();

				((TextView) findViewById(R.id.tv_hotel_name))
						.setText(hDetailsItem.getHotelName());
				((TextView) findViewById(R.id.tv_hotel_address))
						.setText(strHotelAddress);

				int length = Math.min(5, hDetailsItem.getImageUrls().length);
				for (i = 0; i < length; ++i) {
					ImageView image = new ImageView(getApplicationContext());
					image.setScaleType(ImageView.ScaleType.FIT_XY);
					new ImageDownloaderTask(image).execute(hDetailsItem
							.getImageUrls()[i]);
					mViewFlipper.addView(image);
				}

				temp = hDetailsItem.getHotelDescription();
				((LinearLayout) findViewById(R.id.ll_hotel_desc))
						.setVisibility(temp == null || temp.equals("") ? View.GONE
								: View.VISIBLE);
				tvHotelDesc.setText(temp);

				temp = hDetailsItem.getHotelFacility();

				((LinearLayout) findViewById(R.id.ll_hotel_facility))
						.setVisibility(temp == null || temp.equals("")
								|| temp.equals("null") ? View.GONE
								: View.VISIBLE);
				tvHotelFacility.setText(temp);

				strReqPaxUrl = hDetailsItem.getProceedPaxUrl();

				ImageView ivRoomLogo = null;
				ImageView ivAdult = null;
				ImageView ivChild = null;

				HotelDetailsRooms[] Rooms = hDetailsItem.getRooms();

				roomTypeDetails = hDetailsItem.getRoomTypeDetails();

				if (hDetailsItem.isIsCombination()) {
					llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_1);
					RoomCombination[] roomCombination = hDetailsItem
							.getRoomcombination();
					// for separate block count
					roomCount = roomCombination.length;

					LinearLayout[] parent = new LinearLayout[roomCount];
					LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					lparams.setMargins(5, 5, 5, 5);

					int tempLenth, count = 0;
					LinearLayout parentLayout = null;

					System.out.println("roomCount" + roomCount);

					for (count = 0; count < roomCount; ++count) {

						parent[count] = new LinearLayout(this); // base layout
						parent[count].setLayoutParams(lparams);
						parent[count].setOrientation(LinearLayout.VERTICAL);
						parent[count].setTag(roomCombination[count]
								.getRoomCombination());
						parent[count]
								.setBackgroundResource(R.drawable.white_bag_curved_border);

						llHotelRoomsList.addView(parent[count]);
					}

					parent = null;
					tempLenth = roomTypeDetails.length;

					View vRooms = null;
					for (i = 0; i < Rooms.length; ++i) {

						parentLayout = (LinearLayout) llHotelRoomsList
								.findViewWithTag(Rooms[i].getRoomTag());

						vRooms = getLayoutInflater().inflate(
								R.layout.hotel_details_item, null);

						ivRoomLogo = (ImageView) vRooms
								.findViewById(R.id.iv_hotel_logo);

						if (bmp == null)
							ivRoomLogo.setImageResource(R.mipmap.ic_no_image);
						else
							ivRoomLogo.setImageBitmap(bmp);

						((TextView) vRooms.findViewById(R.id.tv_room_name))
								.setText(Rooms[i].getRoomName());

						final LinearLayout llPassenger = (LinearLayout) vRooms
								.findViewById(R.id.ll_passenger_icons);

						int tempNos = 0;
						for (tempNos = 0; tempNos < Rooms[i].getAdultCount(); ++tempNos) {
							ivAdult = new ImageView(getApplicationContext());
							ivAdult.setScaleType(ImageView.ScaleType.FIT_XY);
							ivAdult.setImageResource(R.mipmap.ic_adult);

							llPassenger.addView(ivAdult);
						}

						for (tempNos = 0; tempNos < Rooms[i].getChildCount(); ++tempNos) {
							ivChild = new ImageView(getApplicationContext());
							ivChild.setScaleType(ImageView.ScaleType.FIT_CENTER);
							ivChild.setImageResource(R.mipmap.ic_child);
							ivChild.setPadding(0, (int) px, 0, 0);
							llPassenger.addView(ivChild);
						}

						// ((TextView)
						// vRooms.findViewById(R.id.tv_room_boardtype))
						// .setText(jRooms.getString("BoardType"));

						temp = String.format(new Locale("en"), "%.3f",
								Rooms[i].getPerNightPrice());
						temp = Rooms[i].getCurrency()
								+ "<strong><font color='#F36000' size='3'>"
								+ " " + temp + "</font></strong>";
						((TextView) vRooms
								.findViewById(R.id.tv_room_price_per_night))
								.setText(Html.fromHtml(temp));

						temp = String.format(new Locale("en"), "%.3f",
								Rooms[i].getTotalAmount());
						temp = Rooms[i].getCurrency()
								+ "<strong><font color='#F36000' size='3'>"
								+ " " + temp + "</font></strong>";
						((TextView) vRooms
								.findViewById(R.id.tv_room_total_price))
								.setText(Html.fromHtml(temp));

						temp = String.format(
								getResources().getString(
										R.string.total_price_night),
								(int) (Rooms[i].getTotalAmount() / Rooms[i]
										.getPerNightPrice()));
						((TextView) vRooms
								.findViewById(R.id.tv_room_total_price_per_night))
								.setText(temp);

						final Button btnBuy = (Button) vRooms
								.findViewById(R.id.btn_buy);

						final Button btnCancellation = (Button) vRooms
								.findViewById(R.id.btn_cancellation_policy);

						if (tempLenth != (parentLayout.getChildCount() + 1)) {
							btnCancellation.setVisibility(View.GONE);
							btnBuy.setVisibility(View.GONE);
						} else {
							final String str = Rooms[i]
									.getCancellationPolicyUrl();

							btnCancellation
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated
											// method
											// stub
											blCancellation = true;
											strRequestUrl = str;
											new backMethod().execute();
										}
									});

							btnBuy.setTag(Rooms[i].getRoomTag());
							btnBuy.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method
									// stub
									handleCombinationBuy(btnBuy.getTag()
											.toString());

								}
							});
						}

						parentLayout.addView(vRooms);
					}
					vRooms = null;
				} else {

					roomCount = roomTypeDetails.length;
					for (int j = 0; j < roomCount; ++j) {

						switch (j) {
						case 0:
							llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_1);
							llHotelRoomsList.setTag(roomTypeDetails[j]
									.getRoomTypeIdentifier());
							break;
						case 1:
							((TextView) findViewById(R.id.tv_choose_first_room))
									.setVisibility(View.VISIBLE);
							((LinearLayout) findViewById(R.id.ll_room2))
									.setVisibility(View.VISIBLE);
							llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_2);
							llHotelRoomsList.setTag(roomTypeDetails[j]
									.getRoomTypeIdentifier());
							break;
						case 2:
							llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_3);
							((LinearLayout) findViewById(R.id.ll_room3))
									.setVisibility(View.VISIBLE);
							llHotelRoomsList.setTag(roomTypeDetails[j]
									.getRoomTypeIdentifier());
							break;
						case 3:
							llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_4);
							((LinearLayout) findViewById(R.id.ll_room4))
									.setVisibility(View.VISIBLE);
							llHotelRoomsList.setTag(roomTypeDetails[j]
									.getRoomTypeIdentifier());
							break;
						case 4:
							llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_5);
							((LinearLayout) findViewById(R.id.ll_room5))
									.setVisibility(View.VISIBLE);
							llHotelRoomsList.setTag(roomTypeDetails[j]
									.getRoomTypeIdentifier());
							break;
						default:
							break;
						}

						llHotelRoomsList
								.setBackground(getResources().getDrawable(
										R.drawable.white_bag_curved_border));

						for (i = 0; i < Rooms.length; ++i) {

							if (Rooms[i].getRoomTag().contains(
									roomTypeDetails[j].getRoomTypeIdentifier())) {
								final View vRooms = getLayoutInflater()
										.inflate(R.layout.hotel_details_item,
												null);

								ivRoomLogo = (ImageView) vRooms
										.findViewById(R.id.iv_hotel_logo);

								if (bmp == null)
									ivRoomLogo
											.setImageResource(R.mipmap.ic_no_image);
								else
									ivRoomLogo.setImageBitmap(bmp);

								((TextView) vRooms
										.findViewById(R.id.tv_room_name))
										.setText(Rooms[i].getRoomName());
								vRooms.setTag(Rooms[i].getRoomId()
										+ llHotelRoomsList.getTag().toString());

								final String str = Rooms[i]
										.getCancellationPolicyUrl();

								((Button) vRooms
										.findViewById(R.id.btn_cancellation_policy))
										.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												blCancellation = true;
												strRequestUrl = str;
												new backMethod().execute();
											}
										});

								// ((TextView) vRooms
								// .findViewById(R.id.tv_room_boardtype))
								// .setText(jRooms.getString("BoardType"));

								final LinearLayout llPassenger = (LinearLayout) vRooms
										.findViewById(R.id.ll_passenger_icons);

								int tempNos = 0;
								for (tempNos = 0; tempNos < Rooms[i]
										.getAdultCount(); ++tempNos) {
									ivAdult = new ImageView(
											getApplicationContext());
									ivAdult.setScaleType(ImageView.ScaleType.FIT_XY);
									ivAdult.setImageResource(R.mipmap.ic_adult);

									llPassenger.addView(ivAdult);
								}

								for (tempNos = 0; tempNos < Rooms[i]
										.getChildCount(); ++tempNos) {
									ivChild = new ImageView(
											getApplicationContext());
									ivChild.setScaleType(ImageView.ScaleType.FIT_CENTER);
									ivChild.setImageResource(R.mipmap.ic_child);
									ivChild.setPadding(0, (int) px, 0, 0);

									llPassenger.addView(ivChild);
								}

								temp = String.format(new Locale("en"), "%.3f",
										Rooms[i].getPerNightPrice());
								temp = Rooms[i].getCurrency()
										+ "<strong><font color='#F36000' size='3'>"
										+ " " + temp + "</font></strong>";
								((TextView) vRooms
										.findViewById(R.id.tv_room_price_per_night))
										.setText(Html.fromHtml(temp));

								temp = String.format(new Locale("en"), "%.3f",
										Rooms[i].getTotalAmount());
								temp = Rooms[i].getCurrency()
										+ "<strong><font color='#F36000' size='3'>"
										+ " " + temp + "</font></strong>";
								((TextView) vRooms
										.findViewById(R.id.tv_room_total_price))
										.setText(Html.fromHtml(temp));

								temp = String
										.format(getResources().getString(
												R.string.total_price_night),
												(int) (Rooms[i]
														.getTotalAmount() / Rooms[i]
														.getPerNightPrice()));
								((TextView) vRooms
										.findViewById(R.id.tv_room_total_price_per_night))
										.setText(temp);

								final Button btnBuy = (Button) vRooms
										.findViewById(R.id.btn_buy);
								btnBuy.setId(Rooms[i].getRoomId());
								btnBuy.setTag(llHotelRoomsList.getTag());
								btnBuy.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if (btnBuy
												.getText()
												.toString()
												.equals(getResources()
														.getString(R.string.buy))) {
											++tempCount;
											handleNonCombinationBuy(((View) vRooms.getParent()).getId(),
													vRooms.getTag().toString(),
													true,
													btnBuy.getId(),
													((TextView) vRooms
															.findViewById(R.id.tv_room_price_per_night))
															.getTag()
															.toString(),
													((TextView) vRooms
															.findViewById(R.id.tv_room_name))
															.getText()
															.toString(), v
															.getTag()
															.toString());
											btnBuy.setText(getResources()
													.getString(R.string.change));
										} else {
											--tempCount;
											handleNonCombinationBuy(((View) vRooms.getParent()).getId(),
													vRooms.getTag().toString(),
													false,
													btnBuy.getId(),
													((TextView) vRooms
															.findViewById(R.id.tv_room_price_per_night))
															.getTag()
															.toString(),
													((TextView) vRooms
															.findViewById(R.id.tv_room_name))
															.getText()
															.toString(), v
															.getTag()
															.toString());
											btnBuy.setText(getResources()
													.getString(R.string.buy));
										}

									}
								});

								llHotelRoomsList.addView(vRooms);
							}
						}
					}
				}

				strNightCount = String.valueOf((int) (Rooms[i - 1]
						.getTotalAmount() / Rooms[i - 1].getPerNightPrice()));

			}
			System.out
					.println("------------------Parsing finished-------------");
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();

		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			handler.sendEmptyMessage(3);
		} catch (NullPointerException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(3);
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(2);
		}
	}

	protected void handleCombinationBuy(String strRoomCombination) {
		// TODO Auto-generated method stub

		LinearLayout parentLayout = null;
		parentLayout = (LinearLayout) llHotelRoomsList
				.findViewWithTag(strRoomCombination);
		int i = 0, lenth = parentLayout.getChildCount();
		String temp = null;
		pri = 0.0;
		for (i = 0; i < lenth; ++i) {
			View v = parentLayout.getChildAt(i);
			temp = ((TextView) v.findViewById(R.id.tv_room_total_price))
					.getText().toString();
			temp = temp.substring(4, temp.length());
			pri = Double.parseDouble(temp);
		}

		strReqPaxUrl = hDetailsItem.getProceedPaxUrl().replace("{0},", strRoomCombination);
		strRoomCombination = strRoomCombination.substring(0,
				strRoomCombination.length() - 1);

		Intent hotelpax = new Intent(HotelDetailsActivity.this,
				HotelPaxActivity.class);
		hotelpax.putExtra("strHotelName", strHotelName);
		hotelpax.putExtra("strHotelAddress", strHotelAddress);
		hotelpax.putExtra("strImgUrl", strImgUrl);
		hotelpax.putExtra("total_price", pri);
		hotelpax.putExtra("NightCount", strNightCount);
		hotelpax.putExtra("roomCombination", strRoomCombination);
		hotelpax.putExtra("request_url", strReqPaxUrl);
		startActivity(hotelpax);

	}

	private void handleNonCombinationBuy(int layoutId, String id, boolean hide, int RoomId,
			String price, String strRoomName, String roomTypeIndentifier) {
		LinearLayout llTemp = (LinearLayout) findViewById(layoutId);
		price = price.substring(4, price.length());
		
		switch (layoutId) {
		case R.id.ll_hotel_list_1:
			roomCom[0] = hide ? RoomId : -1;
			roomPrice[0] = hide ? price : null;
			break;

		case R.id.ll_hotel_list_2:
			roomCom[1] = hide ? RoomId : -1;
			roomPrice[1] = hide ? price : null;
			break;
			
		case R.id.ll_hotel_list_3:
			roomCom[2] = hide ? RoomId : -1;
			roomPrice[2] = hide ? price : null;
			break;
			
		case R.id.ll_hotel_list_4:
			roomCom[3] = hide ? RoomId : -1;
			roomPrice[3] = hide ? price : null;
			break;
			
		case R.id.ll_hotel_list_5:
			roomCom[4] = hide ? RoomId : -1;
			roomPrice[4] = hide ? price : null;
			break;
			
		default:
			break;
		}
		
		for (int i = 0; i < llTemp.getChildCount(); ++i) {
			View vRooms = llTemp.getChildAt(i);
			if (!vRooms.getTag().toString().equalsIgnoreCase(id) && hide)
				vRooms.setVisibility(View.GONE);
			else
				vRooms.setVisibility(View.VISIBLE);
		}

		handleApi(hide, id, RoomId, price, strRoomName, roomTypeIndentifier);

		if (hide && tempCount == roomCount) {
			pri = 0.0;
			for (int no = 0; no < roomCount; ++no) {
				strRoomCombination = strRoomCombination + roomCom[no] + ",";
				pri = pri + Double.parseDouble(roomPrice[no]);
			}

			strRoomCombination = (String) strRoomCombination.subSequence(0,
					strRoomCombination.length() - 1);

			strReqPaxUrl = hDetailsItem.getProceedPaxUrl().replace("{0},", strRoomCombination);

			btnContinue.setVisibility(View.VISIBLE);
		} else
			btnContinue.setVisibility(View.GONE);
	}

	private void handleApi(boolean hide, String id, int RoomId, String price,
			String strRoomName, String roomTypeIndentifier) {
		switch (hDetailsItem.getAPIID()) {
		case 12:

			tempCount = hide ? tempCount - 1 : tempCount + 1;
			
			for (int j = 0; j < roomCount; ++j) {

				switch (j) {
				case 0:
					llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_1);
					break;
				case 1:
					llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_2);
					break;
				case 2:
					llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_3);
					break;
				case 3:
					llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_4);
					break;
				case 4:
					llHotelRoomsList = (LinearLayout) findViewById(R.id.ll_hotel_list_5);
					break;
				default:
					break;
				}

				if (llHotelRoomsList.getTag().toString()
						.equalsIgnoreCase(roomTypeIndentifier)) {
					int cnt = 0;
					for (cnt = 0; cnt < llHotelRoomsList.getChildCount(); cnt++) {
						final View view = llHotelRoomsList.getChildAt(cnt);
						if (view.getTag().toString().substring(0, 1)
								.contains(String.valueOf(RoomId))) {
							for (int i = 0; i < llHotelRoomsList
									.getChildCount(); ++i) {
								View vRooms = llHotelRoomsList.getChildAt(i);
								if (!vRooms.getTag().toString()
										.equalsIgnoreCase(id)
										&& hide)
									vRooms.setVisibility(View.GONE);
								else if (hide){
									roomCom[j] = RoomId;
									roomPrice[j] = price;
									tempCount++;
									((Button) vRooms
									.findViewById(RoomId)).setText(getResources()
											.getString(R.string.change));
									vRooms.setVisibility(View.VISIBLE);
								} else if(!vRooms.getTag().toString()
										.equalsIgnoreCase(id)
										&& !hide){
									vRooms.setVisibility(View.VISIBLE);
								} else {
									roomCom[j] = -1;
									roomPrice[j] = null;
									tempCount--;
									((Button) vRooms
									.findViewById(RoomId)).setText(getResources()
											.getString(R.string.buy));
									vRooms.setVisibility(View.VISIBLE);
								}

							}
						}
					}
				}

			}
			break;

		default:
			break;
		}
	}
	
	private void parseCancellation(String result) {
		try {
			if (result != null) {
				showAlert(new HotelDataHandler().getCancellationPolicy(result,
						HotelDetailsActivity.this));
			}

			System.out
					.println("------------------Parsing finished-------------");

		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			handler.sendEmptyMessage(3);
		} catch (NullPointerException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(3);
		} catch (Exception e) {
			e.printStackTrace();
			handler.sendEmptyMessage(2);
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

		if (blCancellation)
			alertDialog.setTitle(getResources().getString(
					R.string.cancellation_policy));
		alertDialog.setMessage(msg);

		alertDialog.setPositiveButton(getResources().getString(R.string.ok),
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (!blCancellation)
							finish();
					}
				});

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

}
