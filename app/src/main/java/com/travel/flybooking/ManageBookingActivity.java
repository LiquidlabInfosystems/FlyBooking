package com.travel.flybooking;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.travel.flybooking.support.CommonFunctions;
import com.travel.common_handlers.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ManageBookingActivity extends Activity {
	private Locale myLocale;
	EditText etEmailAddress, etPassword;
	TextView tvLogintxt;
	EditText etBookingmailid, etBookingrefno, etBooking;
	EditText etReferncno, etEmailid, etPhoneno;
	Spinner spCountrycode;
	String[] Countrycode;
	String ccode;

	Dialog loaderDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		loadLocale();
		setContentView(R.layout.activity_manage_booking);

		if (CommonFunctions.loggedIn) {
			finish();
			Intent mybooking = new Intent(ManageBookingActivity.this,
					MyBookingActivity.class);
			startActivity(mybooking);
		}

		initialize();
	}

	@SuppressWarnings("unchecked")
	private void initialize() {

		loaderDialog = new Dialog(this, android.R.style.Theme_Translucent);
		loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		loaderDialog.getWindow().setGravity(Gravity.TOP);
		loaderDialog.setContentView(R.layout.dialog_loader);
		loaderDialog.setCancelable(false);

		etReferncno = (EditText) findViewById(R.id.edt_refno);
		etEmailid = (EditText) findViewById(R.id.edt_mybookingmailid);
		spCountrycode = (Spinner) findViewById(R.id.spn_bookingcountrycode);
		etPhoneno = (EditText) findViewById(R.id.edt_bookingphoneno);

		etEmailAddress = (EditText) findViewById(R.id.edt_emailid);
		etPassword = (EditText) findViewById(R.id.edt_password);

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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.tv_spinner, Countrycode);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
		spCountrycode.setAdapter(adapter);
		spCountrycode
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						ccode = spCountrycode.getSelectedItem().toString();
						Log.e("Countrycode", ccode);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		spCountrycode.setSelection(((ArrayAdapter<String>) spCountrycode
				.getAdapter()).getPosition("+965"));

		tvLogintxt = (TextView) findViewById(R.id.tv_text);
		String text = CommonFunctions.lang.equalsIgnoreCase("en") ? "<font color=#E6F36000>Login</font> If you already have an account Please Login."
				: "<font color=#E6F36000>دخول</font>إذا كان لديك بالفعل حساب الرجاء تسجيل الدخول.";
		tvLogintxt.setText(Html.fromHtml(text));
	}

	public void clicker(View v) {
		switch (v.getId()) {
		case R.id.btn_view_booking:
			final String Rno = etReferncno.getText().toString();
			final String Rmailaddr = etEmailid.getText().toString();
			final String phonno = etPhoneno.getText().toString();
			if (!Rno.isEmpty() && !Rmailaddr.isEmpty()) {
				Intent i = new Intent(ManageBookingActivity.this,
						ViewBookigActivity.class);
				i.putExtra("email", Rmailaddr);
				i.putExtra("phonno", "");
				i.putExtra("refno", Rno);
				startActivity(i);
				finish();

			} else if (!Rno.isEmpty() && !phonno.isEmpty()) {
				Intent i = new Intent(ManageBookingActivity.this,
						ViewBookigActivity.class);
				i.putExtra("email", "");
				i.putExtra("phonno", phonno);
				i.putExtra("ctrycode", ccode.replace("+", ""));
				i.putExtra("refno", Rno);
				startActivity(i);
				finish();
			}
			break;

		case R.id.btn_login:
			if (validate()) {
				new LoginBackService().execute();
			}
			break;

		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

	public boolean validate() {
		boolean valid = true;

		String email = etEmailAddress.getText().toString();
		String password = etPassword.getText().toString();

		if (email.isEmpty()
				|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
						.matches()) {
			etEmailAddress.setError(getString(R.string.error_invalid_email));
			valid = false;
		} else {
			etEmailAddress.setError(null);
		}

		if (password.isEmpty()) {
			etPassword.setError(getString(R.string.error_invalid_pass));
			valid = false;
		} else {
			etPassword.setError(null);
		}

		return valid;
	}

	private class LoginBackService extends AsyncTask<Void, Void, String> {

		String strUser = null, strPass = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loaderDialog.show();
			strUser = etEmailAddress.getText().toString();
			strPass = etPassword.getText().toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				
				UrlParameterBuilder urlObj = new UrlParameterBuilder();
				
				String urlParameters = urlObj.getLoginParams(strUser, strPass, false, "0");

				String request = CommonFunctions.main_url
						+ CommonFunctions.lang + urlObj.getLoginUrl();
				String res = new HttpHandler().makeServiceCallWithParams(
						request, urlParameters);

				System.out.println("res" + res);

				if (res != null)
					return res;

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
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				handler.sendEmptyMessage(2);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if (result != null) {
				try {
					JSONObject json = new JSONObject(result);
					Boolean temp = json.getBoolean("IsValid");
					if (temp) {
						temp = json.getBoolean("IsLoggedIn");
						if (temp) {
							SharedPreferences pref = getApplicationContext()
									.getSharedPreferences("MyLoginPref", 0);
							SharedPreferences.Editor logineditor = pref.edit();
							logineditor.putString("Email", etEmailAddress
									.getText().toString());
							logineditor.commit();

							CommonFunctions.loggedIn = temp;
							Toast.makeText(getApplicationContext(),
									getString(R.string.success_msg_login),
									Toast.LENGTH_SHORT).show();
							finish();
							Intent mybookings = new Intent(
									getApplicationContext(),
									MyBookingActivity.class);
							startActivity(mybookings);
						}
					} else {
						temp = json.getBoolean("IsUserNameValid");
						if (!temp)
							Toast.makeText(getApplicationContext(),
									json.getString("LogInMessage"),
									Toast.LENGTH_SHORT).show();
						else
							etPassword.setError(json.getString("LogInMessage"));

					}
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
					handler.sendEmptyMessage(1);
				}
			}
			if (loaderDialog.isShowing())
				loaderDialog.dismiss();
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
				showAlert(getResources().getString(R.string.error_common));
			}

		}
	};

	public void showAlert(String msg) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(getResources().getString(R.string.error_title));
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
