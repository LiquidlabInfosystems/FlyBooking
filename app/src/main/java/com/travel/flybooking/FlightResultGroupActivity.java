package com.travel.flybooking;

import java.util.ArrayList;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.flybooking.FlightResultActivity;
import com.travel.flybooking.adapter.FlightResultInflator;
import com.travel.model.FlightResultItem;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class FlightResultGroupActivity extends Activity {

	static LinearLayout llResult;
	Dialog loaderDialog;

	ArrayList<FlightResultItem> groupedResult;

	String strPrice;

	boolean isRoundTrip;
	OnClickListener resultClicker;
	public static FlightResultItem selectedFItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_flight_result_group);
		setHeader();

		loaderDialog = new Dialog(FlightResultGroupActivity.this,
				android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		strPrice = getIntent().getExtras().getString("price");
		isRoundTrip = getIntent().getBooleanExtra("isRoundTrip", false);

		groupResult();

		llResult = (LinearLayout) findViewById(R.id.ll_result);

		resultClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedFItem = groupedResult.get(v.getId());

				Intent details = new Intent(FlightResultGroupActivity.this,
						FlightPaxActivity.class);
				details.putExtra("sID",
						FlightResultActivity.strSessionId + CommonFunctions.getRandomString(4));
				details.putExtra("isRound", isRoundTrip);
				details.putExtra("isGroup", true);
				details.putExtra("tripID", selectedFItem.getStrTripId());
				startActivity(details);

			}
		};

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new FlightResultInflator(FlightResultGroupActivity.this,
						groupedResult, isRoundTrip, llResult,
						resultClicker, true);
				 
				((RelativeLayout) findViewById(R.id.rl_loader))
						.setVisibility(View.GONE);
			}
		}, 30);

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
		}
	}

	private void groupResult() {
		groupedResult = new ArrayList<FlightResultItem>();
		for (FlightResultItem fitem : FlightResultActivity.flightResultItem) {
			if (fitem.getDoubleFlightPrice() == Double.parseDouble(strPrice)) {
				groupedResult.add(fitem);
			}

		}
	}

}
