package com.travel.flybooking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.support.ImageDownloader;
import com.travel.flybooking.support.RangeSeekBar;
import com.travel.flybooking.support.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.travel.datahandler.HotelDataHandler;
import com.travel.flybooking.R;
import com.travel.flybooking.adapter.HotelResultAdapter;
import com.travel.common_handlers.*;
import com.travel.model.HotelResultItem;

public class HotelResultActivity extends Activity {

	private Locale myLocale;
	public static String strCity, strCheckinDate, strCheckoutDate;
	public static int resultCount, passengerCount, roomCount;
	int starCount_0 = 0, starCount_1 = 0, starCount_2 = 0, starCount_3 = 0,
			starCount_4 = 0, starCount_5 = 0;

	ImageView ivClearHotelName;
	TextView tvProgressText, tvResultCount; // tvCurrency
	// ScrollView svResult;
	EditText etFilterHotelName;
	static LinearLayout llSort, llMap;
	ListView lvHotelResult;
	LinearLayout llSortLayout, llFilterLayout, llSortDialogLayout;
	public static ArrayList<HotelResultItem> hotelResultItem;
	private static ArrayList<HotelResultItem> hotelResultItemTemp,
			filteredResult;
//	private static ArrayList<String> arrayBoardTypes;
	// , arrayCheckedBoardtypes;
	String str_url = "";
	String main_url = "";
	Dialog loaderDialog, dialogSort, curr, dialogFilter;

	// boolean for sort
	Boolean blSortPrice = true, blSortRating = false, blSortHotelName = false;
	String strSortPriceType = "Low", strSortRatingType = "Low",
			strSortHotelNameType = "Low";

	// boolean for filter
	Boolean blFilterPrice = false, blFilterName = false,
			blFilterBoardTypes = false, blHasBreakfast = false;;
	Double filterMinPrice, filterMaxPrice;
	Boolean blAllStar = true, blNoStar = false, blOneStar = false,
			blTwoStar = false, blThreeStar = false, blFourStar = false,
			blFiveStar = false;
	String strSearchName = "";
	static String strSessionId = null;
	boolean blCurr = false;

	public static LruCache<String, Bitmap> mMemoryCache;
	public static Bitmap bmp = null;
	CommonFunctions cf;

	String[] sortText, sortHeading;

	public static Activity activityHotelResult;

	HotelDataHandler hotelDataHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		activityHotelResult = this;

		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_search_result_hotel);

		initialize();
		getIntentValues();
		setViewValues();

		if (CommonFunctions.modify) {
			strSessionId = CommonFunctions.strSearchId;
			hotelResultItem.addAll(CommonFunctions.HotelResult);
			lvHotelResult.setAdapter(new HotelResultAdapter(
					HotelResultActivity.this, hotelResultItem, strSessionId));
			CommonFunctions.modify = false;
			CommonFunctions.strSearchId = null;
			CommonFunctions.HotelResult.clear();
//			for (HotelResultItem hItem : hotelResultItem) {
//				if (!arrayBoardTypes.contains(hItem.strBoardTypes)
//						&& !hItem.strBoardTypes.equalsIgnoreCase(""))
//					arrayBoardTypes.add(hItem.strBoardTypes);
//				if (hItem.strBoardTypes.toLowerCase().contains("breakfast")
//						|| hItem.strBoardTypes.toLowerCase().contains(
//								"الا�?طار")
//						|| hItem.strBoardTypes.toLowerCase().contains("ا�?طار"))
//					blHasBreakfast = true;
//			}
			setDefaultValues();
			printImage(hotelResultItem);
		} else {
			if (!str_url.equalsIgnoreCase(""))
				new backMethod().execute("");
			else
				finishAffinity();
		}

		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 8;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}
		};

	}

	private void initialize() {
		// TODO Auto-generated method stub
		cf = new CommonFunctions(this);
		hotelDataHandler = new HotelDataHandler();

		loaderDialog = new Dialog(HotelResultActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		// ((ImageView) loaderDialog.findViewById(R.id.iv_loader))
		// .setImageResource(R.drawable.hotel_loader);

		tvResultCount = new TextView(this);
		tvResultCount.setBackgroundResource(R.drawable.buttonfocus);
		tvResultCount.setTextColor(Color.WHITE);
		tvResultCount.setPadding(10, 10, 10, 10);
		// Defining the layout parameters of the TextView
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 10, 10, 10);

		if (CommonFunctions.lang.equalsIgnoreCase("en"))
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT
					| RelativeLayout.ALIGN_PARENT_TOP);

		tvResultCount.setLayoutParams(lp);
		tvResultCount.setVisibility(View.GONE);

		((RelativeLayout) findViewById(R.id.rl_result)).addView(tvResultCount);

		tvProgressText = (TextView) loaderDialog
				.findViewById(R.id.tv_progress_text);
		tvProgressText.setVisibility(View.VISIBLE);
		tvProgressText.setText(getResources().getString(
				R.string.searching_loader_text));

		dialogSort = new Dialog(HotelResultActivity.this,
				android.R.style.Theme_Translucent);
		dialogSort.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSort.setContentView(R.layout.dialog_sort);

		llSortDialogLayout = (LinearLayout) dialogSort
				.findViewById(R.id.ll_sort_dialog_layout);

		llSortDialogLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogSort.dismiss();
			}
		});

		dialogFilter = new Dialog(this, android.R.style.Theme_Translucent);
		dialogFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogFilter.getWindow().setGravity(Gravity.TOP);
		dialogFilter.setContentView(R.layout.dialog_filter_hotel);
		dialogFilter.setCancelable(false);

		llSort = (LinearLayout) dialogSort.findViewById(R.id.ll_sort_options);
		sortText = getResources().getStringArray(R.array.sort_items_hotel);
		sortHeading = getResources().getStringArray(
				R.array.sort_item_hotel_heading);

		lvHotelResult = (ListView) findViewById(R.id.lv_hotel_result);
		llSortLayout = (LinearLayout) findViewById(R.id.ll_sort);
		llFilterLayout = (LinearLayout) findViewById(R.id.ll_filter);
		etFilterHotelName = (EditText) findViewById(R.id.et_filter_hotel_name);
		ivClearHotelName = (ImageView) findViewById(R.id.iv_clear_name);
		hotelResultItem = new ArrayList<HotelResultItem>();
		hotelResultItemTemp = new ArrayList<HotelResultItem>();
		filteredResult = new ArrayList<HotelResultItem>();
//		arrayBoardTypes = new ArrayList<String>();

		main_url = CommonFunctions.main_url + CommonFunctions.lang
				+ "/HotelApi/HotelApiSearch/";

		etFilterHotelName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				filteredResult.clear();
				String filter = s.toString().toLowerCase();
				for (HotelResultItem listItem : hotelResultItem) {
					if (listItem.getHotelName().toLowerCase().contains(filter)) {
						filteredResult.add(listItem);
					}
				}
				if (s.length() == 0) {
					blFilterName = false;
					ivClearHotelName.setVisibility(View.GONE);
				} else {
					blFilterName = true;
					strSearchName = filter;
					ivClearHotelName.setVisibility(View.VISIBLE);

					// boolean for sort
					blSortPrice = true;
					blSortRating = false;
					blSortHotelName = false;
					strSortPriceType = "Low";
					strSortRatingType = "Low";
					strSortHotelNameType = "Low";

					// boolean for filter
					blFilterPrice = false;
					blFilterBoardTypes = false;
					blNoStar = false;
					blOneStar = false;
					blTwoStar = false;
					blThreeStar = false;
					blFourStar = false;
					blFiveStar = false;
					// arrayCheckedBoardtypes.clear();
				}

				lvHotelResult
						.setAdapter(new HotelResultAdapter(
								HotelResultActivity.this, filteredResult,
								strSessionId));
				printImage(filteredResult);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		bmp = BitmapFactory.decodeResource(getResources(),
				R.mipmap.ic_no_image);
	}

	private void getIntentValues() {
		// TODO Auto-generated method stub
		str_url = getIntent().getExtras().getString("url", "");
		str_url = str_url + "?searchID=";
		strCity = getIntent().getExtras().getString("city", "");
		strCheckinDate = getIntent().getExtras().getString("checkinDate", "");
		strCheckoutDate = getIntent().getExtras().getString("checkoutDate", "");
		passengerCount = getIntent().getExtras().getInt("passengers");
		roomCount = getIntent().getExtras().getInt("roomCount");
	}

	private void setViewValues() {
		TextView tvCity = (TextView) findViewById(R.id.tv_Hotel_city);
		TextView tvCheckinDate = (TextView) findViewById(R.id.tv_checkin_date);
		TextView tvCheckoutDate = (TextView) findViewById(R.id.tv_checkout_date);
		TextView tvpassengerCount = (TextView) findViewById(R.id.tv_passenger_count);
		TextView tvroomCount = (TextView) findViewById(R.id.tv_room_count);
		tvCity.setText(getResources().getString(R.string.hotels_in) + " "
				+ strCity);
		tvCheckinDate.setText(strCheckinDate.substring(0,
				strCheckinDate.length() - 5));
		tvCheckoutDate.setText(strCheckoutDate.substring(0,
				strCheckinDate.length() - 5));
		tvpassengerCount.setText(String.valueOf(passengerCount));
		tvroomCount.setText(String.valueOf(roomCount));
	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			finish();
			break;
		case R.id.ll_filter:
			showFilterDialog();
			break;
		case R.id.ll_sort:
			showSortDialog();
			break;

		case R.id.ll_modify:
			 finish();
			
		case R.id.iv_clear_name:
			ivClearHotelName.setVisibility(View.GONE);
			etFilterHotelName.setText(null);
			blFilterName = false;
			// boolean for sort
			blSortPrice = true;
			blSortRating = false;
			blSortHotelName = false;
			strSortPriceType = "Low";
			strSortRatingType = "Low";
			strSortHotelNameType = "Low";

			// boolean for filter
			blFilterPrice = false;
			blFilterName = false;
			blFilterBoardTypes = false;
			blNoStar = false;
			blOneStar = false;
			blTwoStar = false;
			blThreeStar = false;
			blFourStar = false;
			blFiveStar = false;
			strSearchName = "";
			// arrayCheckedBoardtypes.clear();

			lvHotelResult.setAdapter(new HotelResultAdapter(
					HotelResultActivity.this, hotelResultItem, strSessionId));
			printImage(hotelResultItem);
			break;
		default:
			break;
		}
	}

	private class backMethod extends AsyncTask<String, String, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			starCount_0 = 0;
			starCount_1 = 0;
			starCount_2 = 0;
			starCount_3 = 0;
			starCount_4 = 0;
			starCount_5 = 0;
			hotelResultItem.clear();
			hotelResultItemTemp.clear();

			loaderDialog.show();

			llSortLayout.setEnabled(false);
			llFilterLayout.setEnabled(false);
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (params[0].isEmpty()) {
				String resultString = makePostRequest(false, "");
				if (resultString != null)
					parseHotelResult(resultString);
			} else if (!params[0].equalsIgnoreCase(CommonFunctions.strCurrency)) {
				String resultString = makePostRequest(true, params[0]);
				if (resultString != null) {
					JSONObject jObj;
					try {
						CommonFunctions.strCurrency = params[0];
						jObj = new JSONObject(resultString);
						jObj = jObj.getJSONObject("data");
						parseHotelResult(jObj.getString("Item"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (hotelResultItem.size() > 0) {
				blCurr = false;
				// tvCurrency.setText(CommonFunctions.strCurrency);
				// printImage(hotelResultItem);
				// printResult(hotelResultItem);
				System.out
						.println("------------------Finished displaying-------------");
				// startRepeatingTask();
				setDefaultValues();
				llSortLayout.setEnabled(true);
				llFilterLayout.setEnabled(true);
				lvHotelResult
						.setAdapter(new HotelResultAdapter(
								HotelResultActivity.this, hotelResultItem,
								strSessionId));
				printImage(hotelResultItem);
				// new sort().execute();
			} else {
				// noResultAlert(false,
				// getResources().getString(R.string.no_result));
				((LinearLayout) findViewById(R.id.ll_filter)).setEnabled(false);
				((LinearLayout) findViewById(R.id.ll_sort)).setEnabled(false);
			}
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
		}

	}

	public void setDefaultValues() {
		hotelResultItemTemp.addAll(hotelResultItem);
		filterMinPrice = hotelDataHandler.getMinPrice(hotelResultItem);
		filterMaxPrice = hotelDataHandler.getMaxPrice(hotelResultItem);

		int[] starCounts = hotelDataHandler.getStarCounts(hotelResultItem);

		starCount_0 = starCounts[0];
		starCount_1 = starCounts[1];
		starCount_2 = starCounts[2];
		starCount_3 = starCounts[3];
		starCount_4 = starCounts[4];
		starCount_5 = starCounts[5];

	}

	private String makePostRequest(boolean blCurr, String strCurrency) {

		// making POST request.
		try {
			String request = null;
			if (blCurr) {
				request = CommonFunctions.main_url + CommonFunctions.lang
						+ "/HotelApi/CurrencyConvert?currency=" + strCurrency
						+ "&searchID=" + strSessionId;
			} else {
				strSessionId = CommonFunctions.getRandomString(6) + "_";
				str_url = str_url.replace("{0}", CommonFunctions.strCurrency);
				request = main_url + str_url + strSessionId;
			}

			String resultString = new HttpHandler().makeServiceCall(request);

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

	private void parseHotelResult(String result) {
		try {
			if (result != null) {
				JSONArray jarray = null;
				// Parse String to JSON object
				jarray = new JSONArray(result);

				if (jarray.length() == 0) {
					handler.sendEmptyMessage(3);
				} else {

					hotelResultItem = hotelDataHandler.parseHotelResult(result);
					resultCount = hotelResultItem.size();
//					arrayBoardTypes = hotelDataHandler
//							.getBoardTypeArray(hotelResultItem);
					blHasBreakfast = hotelDataHandler
							.hasBreakfast(hotelResultItem);
				}

			}
			System.out
					.println("------------------Parsing finished-------------");

		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			if (blCurr)
				handler.sendEmptyMessage(4);
			else
				handler.sendEmptyMessage(3);
		} catch (NullPointerException e) {
			e.printStackTrace();
			if (blCurr)
				handler.sendEmptyMessage(4);
			else
				handler.sendEmptyMessage(3);
		} catch (Exception e) {
			e.printStackTrace();
			if (blCurr)
				handler.sendEmptyMessage(4);
			else
				handler.sendEmptyMessage(2);
		}
	}

	private void showSortDialog() {
		llSort.removeAllViews();
		int i;
		for (i = 0; i < sortText.length; ++i) {
			final View view = getLayoutInflater().inflate(
					R.layout.item_sort_list, null);
			if (i % 2 == 0) {
				final TextView tvView = (TextView) getLayoutInflater().inflate(
						R.layout.tv_autocomplete, null);
				tvView.setText(sortHeading[i / 2]);
				llSort.addView(tvView);
			}
			final RadioButton rb = (RadioButton) view
					.findViewById(R.id.rb_sort_item);
			rb.setClickable(false);
			((TextView) view.findViewById(R.id.tv_sort_item_name))
					.setText(sortText[i]);

			switch (i) {
			case 0:
				rb.setChecked(blSortPrice
						&& strSortPriceType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 1:
				rb.setChecked(blSortPrice
						&& strSortPriceType.equalsIgnoreCase("High") ? true
						: false);
				break;
			case 2:
				rb.setChecked(blSortRating
						&& strSortRatingType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 3:
				rb.setChecked(blSortRating
						&& strSortRatingType.equalsIgnoreCase("High") ? true
						: false);
				break;
			case 4:
				rb.setChecked(blSortHotelName
						&& strSortHotelNameType.equalsIgnoreCase("Low") ? true
						: false);
				break;
			case 5:
				rb.setChecked(blSortHotelName
						&& strSortHotelNameType.equalsIgnoreCase("High") ? true
						: false);
				break;
			}

			view.setId(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (!rb.isChecked()) {
						rb.setChecked(true);

						switch (view.getId()) {

						case 0:
							blSortPrice = true;
							blSortRating = false;
							blSortHotelName = false;
							strSortPriceType = "Low";
							break;

						case 1:
							blSortPrice = true;
							blSortRating = false;
							blSortHotelName = false;
							strSortPriceType = "High";
							break;

						case 2:
							blSortPrice = false;
							blSortRating = true;
							blSortHotelName = false;
							strSortRatingType = "Low";
							break;

						case 3:
							blSortPrice = false;
							blSortRating = true;
							blSortHotelName = false;
							strSortRatingType = "High";
							break;

						case 4:
							blSortPrice = false;
							blSortRating = false;
							blSortHotelName = true;
							strSortHotelNameType = "Low";
							break;

						case 5:
							blSortPrice = false;
							blSortRating = false;
							blSortHotelName = true;
							strSortHotelNameType = "High";
							break;

						}
						new sort()
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
					dialogSort.dismiss();
				}
			});
			llSort.addView(view);

		}
		dialogSort.show();

	}

	boolean reset;

	private void showFilterDialog() {
		reset = false;

		final LinearLayout llTabRating, llTabPriceRange;
		final TextView tvTabRating, tvTabPriceRange;
		final ImageView ivTabRating, ivTabPriceRange;
		final ScrollView svRating;
		final LinearLayout llPriceRange;
		final LinearLayout llRangeBar;
		final RelativeLayout rlClose;
		final TextView tvPriceRangeMin, tvPriceRangeMax;
		final TextView tvStar0, tvStar1, tvStar2, tvStar3, tvStar4, tvStar5, tvAll;
		final CheckBox cbStar0, cbStar1, cbStar2, cbStar3, cbStar4, cbStar5, cbAll;
		final LinearLayout llApply, llReset;

		final Double minValuePrice = filterMinPrice;
		final Double maxValuePrice = filterMaxPrice;
		final boolean blAllStarTemp = blAllStar;
		final Boolean blNoStarTemp = blNoStar;
		final Boolean blOneStarTemp = blOneStar;
		final Boolean blTwoStarTemp = blTwoStar;
		final Boolean blThreeStarTemp = blThreeStar;
		final Boolean blFourStarTemp = blFourStar;
		final Boolean blFiveStarTemp = blFiveStar;
		final Boolean blFilterPriceTemp = blFilterPrice;

		llTabRating = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_rating);
		llTabPriceRange = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_tab_price);
		tvTabRating = (TextView) dialogFilter
				.findViewById(R.id.tv_filter_rating);
		tvTabPriceRange = (TextView) dialogFilter
				.findViewById(R.id.tv_filter_price);
		ivTabRating = (ImageView) dialogFilter
				.findViewById(R.id.iv_filter_rating);
		ivTabPriceRange = (ImageView) dialogFilter
				.findViewById(R.id.iv_filter_price);
		svRating = (ScrollView) dialogFilter
				.findViewById(R.id.sv_filter_rating);
		llPriceRange = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_filter_price_range);
		llApply = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_apply_filter);
		llReset = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_reset_filter);
		rlClose = (RelativeLayout) dialogFilter.findViewById(R.id.rl_back);
		llRangeBar = (LinearLayout) dialogFilter
				.findViewById(R.id.ll_range_bar);
		tvPriceRangeMin = (TextView) dialogFilter
				.findViewById(R.id.tv_range_min);
		tvPriceRangeMax = (TextView) dialogFilter
				.findViewById(R.id.tv_range_max);

		cbAll = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_all);
		cbStar0 = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_0);
		cbStar1 = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_1);
		cbStar2 = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_2);
		cbStar3 = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_3);
		cbStar4 = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_4);
		cbStar5 = (CheckBox) dialogFilter.findViewById(R.id.cb_rating_5);

		tvStar0 = (TextView) dialogFilter.findViewById(R.id.tv_0star_count);
		tvStar1 = (TextView) dialogFilter.findViewById(R.id.tv_1star_count);
		tvStar2 = (TextView) dialogFilter.findViewById(R.id.tv_2star_count);
		tvStar3 = (TextView) dialogFilter.findViewById(R.id.tv_3star_count);
		tvStar4 = (TextView) dialogFilter.findViewById(R.id.tv_4star_count);
		tvStar5 = (TextView) dialogFilter.findViewById(R.id.tv_5star_count);
		tvAll = (TextView) dialogFilter.findViewById(R.id.tv_allstar_count);

		cbAll.setChecked(blAllStar);
		cbStar0.setChecked(blNoStar);
		cbStar1.setChecked(blOneStar);
		cbStar2.setChecked(blTwoStar);
		cbStar3.setChecked(blThreeStar);
		cbStar4.setChecked(blFourStar);
		cbStar5.setChecked(blFiveStar);

		tvStar0.setText(String.valueOf(starCount_0));
		tvStar1.setText(String.valueOf(starCount_1));
		tvStar2.setText(String.valueOf(starCount_2));
		tvStar3.setText(String.valueOf(starCount_3));
		tvStar4.setText(String.valueOf(starCount_4));
		tvStar5.setText(String.valueOf(starCount_5));
		tvAll.setText(String.valueOf(resultCount));

		llRangeBar.removeAllViews();

		if (blNoStar || blOneStar || blTwoStar || blThreeStar || blFourStar
				|| blFiveStar || blFilterPrice || blFilterName
				|| blFilterBoardTypes)
			llReset.setBackgroundResource(R.drawable.buttonshape);
		else
			llReset.setBackgroundColor(Color.TRANSPARENT);

		llTabRating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				llTabRating.setBackgroundColor(Color.WHITE);
				llTabPriceRange.setBackgroundColor(Color.TRANSPARENT);
				svRating.setVisibility(View.VISIBLE);
				llPriceRange.setVisibility(View.GONE);
				tvTabRating.setTextColor(Color.parseColor("#E6F36000"));
				tvTabPriceRange.setTextColor(Color.BLACK);
				ivTabRating.setColorFilter(Color.parseColor("#E6F36000"));
				ivTabPriceRange.setColorFilter(Color.BLACK);
			}
		});

		llTabPriceRange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				llTabRating.setBackgroundColor(Color.TRANSPARENT);
				llTabPriceRange.setBackgroundColor(Color.WHITE);
				svRating.setVisibility(View.GONE);
				llPriceRange.setVisibility(View.VISIBLE);
				tvTabRating.setTextColor(Color.BLACK);
				tvTabPriceRange.setTextColor(Color.parseColor("#E6F36000"));
				ivTabRating.setColorFilter(Color.BLACK);
				ivTabPriceRange.setColorFilter(Color.parseColor("#E6F36000"));
			}
		});

		llTabRating.performClick();

		rlClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!reset) {
					filterMinPrice = minValuePrice;
					filterMaxPrice = maxValuePrice;
					blAllStar = blAllStarTemp;
					blNoStar = blNoStarTemp;
					blOneStar = blOneStarTemp;
					blTwoStar = blTwoStarTemp;
					blThreeStar = blThreeStarTemp;
					blFourStar = blFourStarTemp;
					blFiveStar = blFiveStarTemp;
					blFilterPrice = blFilterPriceTemp;
				}

				dialogFilter.dismiss();

			}
		});

		final RangeSeekBar<Double> priceBar = new RangeSeekBar<Double>(this);
		// Set the range
		priceBar.setRangeValues(
				(hotelResultItem).get(0).getHotelDisplayRate(),
				(hotelResultItem).get(hotelResultItem.size() - 1).getHotelDisplayRate());
		priceBar.setSelectedMinValue(filterMinPrice);
		priceBar.setSelectedMaxValue(filterMaxPrice);
		String price = String.format(new Locale("en"), "%.3f", filterMinPrice);
		tvPriceRangeMin.setText(CommonFunctions.strCurrency + " " + price);
		price = String.format(new Locale("en"), "%.3f", filterMaxPrice);
		tvPriceRangeMax.setText(CommonFunctions.strCurrency + " " + price);
		priceBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Double>() {

			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Double minValue, Double maxValue) {
				// handle changed range values
				String price = String
						.format(new Locale("en"), "%.3f", minValue);
				tvPriceRangeMin.setText(CommonFunctions.strCurrency + " "
						+ price);
				price = String.format(new Locale("en"), "%.3f", maxValue);
				tvPriceRangeMax.setText(CommonFunctions.strCurrency + " "
						+ price);
				filterMinPrice = minValue;
				filterMaxPrice = maxValue;
				if (filterMinPrice.equals((hotelResultItem).get(0).getHotelDisplayRate())
						&& filterMaxPrice.equals((hotelResultItem)
								.get(hotelResultItem.size() - 1).getHotelDisplayRate()))
					blFilterPrice = false;
				else
					blFilterPrice = true;

				System.out.println("filterMinPrice:"
						+ filterMinPrice
						+ "\nfilterMaxPrice:"
						+ filterMaxPrice
						+ "\n(hotelResultItem).get(0).doubleHotelDisplayRate:"
						+ (hotelResultItem).get(0).getHotelDisplayRate()
						+ "\n(hotelResultItem).get(hotelResultItem.size()-1).doubleHotelDisplayRate"
						+ (hotelResultItem).get(hotelResultItem.size() - 1).getHotelDisplayRate());

				if (!blAllStar || blFilterPrice || blFilterName
						|| blFilterBoardTypes)
					llReset.setBackgroundResource(R.drawable.buttonshape);
				else
					llReset.setBackgroundColor(Color.TRANSPARENT);
			}
		});
		llRangeBar.addView(priceBar);
		priceBar.setNotifyWhileDragging(true);

		if (filterMinPrice == filterMaxPrice)
			priceBar.setEnabled(false);

		OnClickListener cbListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switch (v.getId()) {
				case R.id.ll_rating_0:
					if (blNoStar)
						blNoStar = false;
					else
						blNoStar = true;
					cbStar0.setChecked(blNoStar);
					if (blNoStar || blOneStar || blTwoStar || blThreeStar
							|| blFourStar || blFiveStar)
						blAllStar = false;
					else
						blAllStar = true;
					cbAll.setChecked(blAllStar);
					break;
				case R.id.ll_rating_1:
					if (blOneStar)
						blOneStar = false;
					else
						blOneStar = true;
					cbStar1.setChecked(blOneStar);
					if (blNoStar || blOneStar || blTwoStar || blThreeStar
							|| blFourStar || blFiveStar)
						blAllStar = false;
					else
						blAllStar = true;
					cbAll.setChecked(blAllStar);
					break;
				case R.id.ll_rating_2:
					if (blTwoStar)
						blTwoStar = false;
					else
						blTwoStar = true;
					cbStar2.setChecked(blTwoStar);
					if (blNoStar || blOneStar || blTwoStar || blThreeStar
							|| blFourStar || blFiveStar)
						blAllStar = false;
					else
						blAllStar = true;
					cbAll.setChecked(blAllStar);
					break;
				case R.id.ll_rating_3:
					if (blThreeStar)
						blThreeStar = false;
					else
						blThreeStar = true;
					cbStar3.setChecked(blThreeStar);
					if (blNoStar || blOneStar || blTwoStar || blThreeStar
							|| blFourStar || blFiveStar)
						blAllStar = false;
					else
						blAllStar = true;
					cbAll.setChecked(blAllStar);
					break;
				case R.id.ll_rating_4:
					if (blFourStar)
						blFourStar = false;
					else
						blFourStar = true;
					cbStar4.setChecked(blFourStar);
					if (blNoStar || blOneStar || blTwoStar || blThreeStar
							|| blFourStar || blFiveStar)
						blAllStar = false;
					else
						blAllStar = true;
					cbAll.setChecked(blAllStar);
					break;
				case R.id.ll_rating_5:
					if (blFiveStar)
						blFiveStar = false;
					else
						blFiveStar = true;
					cbStar5.setChecked(blFiveStar);
					if (blNoStar || blOneStar || blTwoStar || blThreeStar
							|| blFourStar || blFiveStar)
						blAllStar = false;
					else
						blAllStar = true;
					cbAll.setChecked(blAllStar);
					break;
				default:
				}

				if (!blAllStar || blFilterPrice || blFilterName
						|| blFilterBoardTypes)
					llReset.setBackgroundResource(R.drawable.buttonshape);
				else
					llReset.setBackgroundColor(Color.TRANSPARENT);
			}
		};

		((LinearLayout) dialogFilter.findViewById(R.id.ll_rating_0))
				.setOnClickListener(cbListener);
		((LinearLayout) dialogFilter.findViewById(R.id.ll_rating_1))
				.setOnClickListener(cbListener);
		((LinearLayout) dialogFilter.findViewById(R.id.ll_rating_2))
				.setOnClickListener(cbListener);
		((LinearLayout) dialogFilter.findViewById(R.id.ll_rating_3))
				.setOnClickListener(cbListener);
		((LinearLayout) dialogFilter.findViewById(R.id.ll_rating_4))
				.setOnClickListener(cbListener);
		((LinearLayout) dialogFilter.findViewById(R.id.ll_rating_5))
				.setOnClickListener(cbListener);

		llApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				filter();
				dialogFilter.dismiss();
			}
		});

		llReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				blFilterBoardTypes = false;
				blFilterPrice = false;
				blFilterName = false;
				filterMinPrice = minValuePrice;
				filterMaxPrice = maxValuePrice;
				blAllStar = true;
				blNoStar = false;
				blOneStar = false;
				blTwoStar = false;
				blThreeStar = false;
				blFourStar = false;
				blFiveStar = false;
				strSearchName = null;

				cbAll.setChecked(blAllStar);
				cbStar0.setChecked(blNoStar);
				cbStar1.setChecked(blOneStar);
				cbStar2.setChecked(blTwoStar);
				cbStar3.setChecked(blThreeStar);
				cbStar4.setChecked(blFourStar);
				cbStar5.setChecked(blFiveStar);

				priceBar.setRangeValues(
						(hotelResultItem).get(0).getHotelDisplayRate(),
						(hotelResultItem).get(hotelResultItem.size() - 1).getHotelDisplayRate());
				filterMinPrice = (hotelResultItem).get(0).getHotelDisplayRate();
				filterMaxPrice = (hotelResultItem).get(hotelResultItem.size() - 1).getHotelDisplayRate();
				priceBar.setSelectedMinValue(filterMinPrice);
				priceBar.setSelectedMaxValue(filterMaxPrice);
				String price = String.format(new Locale("en"), "%.3f",
						filterMinPrice);
				tvPriceRangeMin.setText(CommonFunctions.strCurrency + " "
						+ price);
				price = String.format(new Locale("en"), "%.3f", filterMaxPrice);
				tvPriceRangeMax.setText(CommonFunctions.strCurrency + " "
						+ price);

				llReset.setBackgroundColor(Color.TRANSPARENT);

				reset = true;

				new sort().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		});

		dialogFilter.show();
	}

	private class sort extends AsyncTask<Void, Void, String> {

		ArrayList<HotelResultItem> temp = new ArrayList<HotelResultItem>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			tvProgressText.setVisibility(View.GONE);
			loaderDialog.show();
			llSortLayout.setEnabled(false);
			llFilterLayout.setEnabled(false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			lvHotelResult.setAdapter(new HotelResultAdapter(
					HotelResultActivity.this, temp, strSessionId));
			printImage(temp);
			// temp.clear();
			llSortLayout.setEnabled(true);
			llFilterLayout.setEnabled(true);
			if (loaderDialog.isShowing()) {
				loaderDialog.dismiss();
				tvProgressText.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if (blNoStar || blOneStar || blTwoStar || blThreeStar || blFourStar
					|| blFiveStar || blFilterPrice || blFilterName
					|| blFilterBoardTypes) {
				temp = hotelDataHandler.sortHotelData(filteredResult,
						blSortPrice, blSortRating, blSortHotelName,
						strSortPriceType, strSortRatingType,
						strSortHotelNameType);

			} else {
				temp = hotelDataHandler.sortHotelData(hotelResultItemTemp,
						blSortPrice, blSortRating, blSortHotelName,
						strSortPriceType, strSortRatingType,
						strSortHotelNameType);
			}
			return null;
		}

	}

	private void filter() {
		filteredResult = new ArrayList<HotelResultItem>();
		System.out.println("hotelResultItemTemp size"
				+ hotelResultItemTemp.size());

		filteredResult = hotelDataHandler.filterStars(hotelResultItemTemp,
				blNoStar, blOneStar, blTwoStar, blThreeStar, blFourStar,
				blFiveStar);

		if (blFilterPrice) {

			filteredResult = hotelDataHandler.filterPrice(filteredResult,
					filterMinPrice, filterMaxPrice);
		}

		if (blFilterName) {
			filteredResult = hotelDataHandler.filterName(filteredResult,
					strSearchName);
		}

		if (blFilterBoardTypes) {
			filteredResult = hotelDataHandler.filterBoardType(filteredResult);
		}

		if (filteredResult.size() > 0) {
			new sort().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			lvHotelResult.setAdapter(null);
			noResultAlert(true, getResources().getString(R.string.no_result));
		}

	}

	// to show images only in the visible part to optimize speed and avoid OOM
	boolean flag;

	private void printImage(final ArrayList<HotelResultItem> array) {
		final ImageDownloader imageDownloader = new ImageDownloader();
		flag = true;
		final ViewHolder viewHolder = new ViewHolder();
		lvHotelResult.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == SCROLL_STATE_IDLE) {
					for (int i = 0; i < view.getChildCount(); ++i) {
						View viewg = (View) view.getChildAt(i);
						HotelResultItem hItem = array.get(view
								.getPositionForView(viewg));
						ImageView iv = (ImageView) viewg
								.findViewById(R.id.iv_hotel_logo);
						if (!hItem.getHotelThumbImage().contains("no_image")) {
							if (getBitmapFromMemCache(hItem.getHotelThumbImage()) == null) {
								viewHolder.imageView = iv;
								imageDownloader.download(
										hItem.getHotelThumbImage(), iv);
							}
						} else {
							iv.setImageBitmap(bmp);
						}
					}
					tvResultCount.setVisibility(View.GONE);
				} else
					tvResultCount.setVisibility(View.VISIBLE);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				// to show images first time
				tvResultCount.setText(String.valueOf(firstVisibleItem + 1)
						+ "/" + String.valueOf(resultCount));
				if (flag) {
					for (int i = 0; i < view.getChildCount(); ++i) {
						View viewg = (View) view.getChildAt(i);
						HotelResultItem hItem = array.get(view
								.getPositionForView(viewg));
						ImageView iv = (ImageView) viewg
								.findViewById(R.id.iv_hotel_logo);
						if (!hItem.getHotelThumbImage().contains("no_image")) {
							if (getBitmapFromMemCache(hItem.getHotelThumbImage()) == null) {
								viewHolder.imageView = iv;
								imageDownloader.download(
										hItem.getHotelThumbImage(), iv);
							}
						} else
							iv.setImageBitmap(bmp);
						flag = false;
					}
				}
			}
		});

		lvHotelResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				HotelResultItem hItem = (HotelResultItem) lvHotelResult
						.getItemAtPosition(position);
				new backService().execute(hItem);
			}
		});
	}

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			if (bitmap == null)
				bitmap = bmp;
			mMemoryCache.put(key, bitmap);
		}
	}

	public static Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	static class ViewHolder {
		ImageView imageView;
	}

	private class backService extends AsyncTask<HotelResultItem, Void, String> {

		HotelResultItem hItem;
		String sessionResult;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			tvProgressText.setText(getResources().getString(
					R.string.checking_loader_text));
			if (tvProgressText.getVisibility() == View.GONE)
				tvProgressText.setVisibility(View.VISIBLE);
			loaderDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(HotelResultItem... params) {
			// TODO Auto-generated method stub
			String request = null;
			try {
				hItem = params[0];
				request = CommonFunctions.main_url
						+ "en/HotelApi/AvailResult?searchID=" + strSessionId;

				sessionResult = new HttpHandler().makeServiceCall(request);
				System.out.println("result" + sessionResult);
				JSONObject json = new JSONObject(sessionResult);
				sessionResult = json.getString("Status");

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// urlConnection.disconnect();
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return params[0].getNativeDeepUrl();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
			if (sessionResult.equalsIgnoreCase("true")) {
				tvProgressText.setText(getResources().getString(
						R.string.searching_loader_text));
				Intent details = new Intent(HotelResultActivity.this,
						HotelDetailsActivity.class);
				details.putExtra("url", hItem.getNativeDeepUrl());
				details.putExtra("img_url", hItem.getHotelThumbImage());
				details.putExtra("sessionID", strSessionId);
				details.putExtra("type", "hotel");
				details.putExtra("latitude", hItem.getHotelLattitude());
				details.putExtra("longitude", hItem.getHotelLongitude());
				details.putExtra("hotelRating", hItem.getHotelRating());
				startActivity(details);
			} else {
				tvProgressText.setText(getResources().getString(
						R.string.flight_expired));
				new backMethod().execute("");
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
				noResultAlert(false,
						"There is a problem on your Network. Please try again later.");

			} else if (msg.what == 2) {

				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						"There is a problem on your application. Please report it.");

			} else if (msg.what == 3) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						getResources().getString(R.string.no_result));
			} else if (msg.what == 4) {
				if (loaderDialog.isShowing())
					loaderDialog.dismiss();
				noResultAlert(false,
						"Something went wrong. Please try again later");
			}

		}
	};

	public void noResultAlert(final boolean filter, String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setMessage(msg);

		if (filter) {
			alertDialog.setNegativeButton(
					getResources().getString(R.string.reset_filter),
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							blFilterPrice = false;
							blFilterName = false;
							filterMinPrice = (hotelResultItem).get(0).getHotelDisplayRate();
							filterMaxPrice = (hotelResultItem)
									.get(hotelResultItem.size() - 1).getHotelDisplayRate();
							blAllStar = true;
							blNoStar = false;
							blOneStar = false;
							blTwoStar = false;
							blThreeStar = false;
							blFourStar = false;
							blFiveStar = false;
							strSearchName = "";
							blFilterBoardTypes = false;
							// arrayCheckedBoardtypes.clear();
							new sort()
									.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
					});
		} else {
			alertDialog.setPositiveButton(
					getResources().getString(R.string.ok),
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							if (!blCurr)
								finish();
						}
					});
		}

		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hotelResultItem.clear();
		hotelResultItemTemp.clear();
		mMemoryCache.evictAll();
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
