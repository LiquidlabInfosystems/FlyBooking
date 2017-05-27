package com.travel.flybooking;

import android.app.ActionBar;
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
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.travel.common_handlers.HttpHandler;
import com.travel.common_handlers.UrlParameterBuilder;
import com.travel.flybooking.support.CommonFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class LoginActivity extends Activity {

    EditText etEmailAddress, etPassword;
    LinearLayout llLoginLayout;
    // forgot
    LinearLayout llForgotLayout;
    EditText etFEmailAddress;
    Dialog loaderDialog;
    Boolean fromPax = false;
    UrlParameterBuilder urlObj;
    private Locale myLocale;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_login);

        urlObj = new UrlParameterBuilder();

        loadAppBar();
        initialize();

    }

    private void loadAppBar() {
        // ============== Define a Custom Header for Navigation
        // drawer=================//
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.header_1, null);

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(v);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                Toolbar parent = (Toolbar) v.getParent();
                parent.setContentInsetsAbsolute(0, 0);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        ImageView backBtn = (ImageView) v.findViewById(R.id.iv_back);
        ImageView homeBtn = (ImageView) v.findViewById(R.id.iv_home);
        homeBtn.setVisibility(View.INVISIBLE);

        // homeBtn.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // finish();
        // Intent intent = new Intent(SidePanelActivity.this,
        // MainActivity.class);
        // startActivity(intent);
        // }
        // });

        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }

    private void initialize() {

        loaderDialog = new Dialog(this, android.R.style.Theme_Translucent);
        loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loaderDialog.getWindow().setGravity(Gravity.TOP);
        loaderDialog.setContentView(R.layout.dialog_loader);
        loaderDialog.setCancelable(false);

        etEmailAddress = (EditText) findViewById(R.id.edt_email);
        etPassword = (EditText) findViewById(R.id.edt_password);
        llForgotLayout = (LinearLayout) findViewById(R.id.ll_forgotlayout);
        llLoginLayout = (LinearLayout) findViewById(R.id.ll_loginlayout);
        etFEmailAddress = (EditText) findViewById(R.id.edt_forgotemailid);

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

        etEmailAddress.setFilters(new InputFilter[]{filter});
        etFEmailAddress.setFilters(new InputFilter[]{filter});

        fromPax = getIntent().getBooleanExtra("from_pax", false);

        if (fromPax) {
            llForgotLayout.setVisibility(View.VISIBLE);
            llLoginLayout.setVisibility(View.GONE);
        }
    }

    public void clicker(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (validate()) {
                    new LoginBackService().execute();
                }
                break;

            case R.id.btn_forgotsubmit:
                functionForgot();
                break;

            case R.id.txt_forgotpassword:
                llForgotLayout.setVisibility(View.VISIBLE);
                llLoginLayout.setVisibility(View.GONE);
                break;

            case R.id.tv_signup:
                finish();
                Intent registerpage = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(registerpage);
                break;

            case R.id.tv_fsignin:
                if (fromPax)
                    finish();
                else {
                    llLoginLayout.setVisibility(View.VISIBLE);
                    llForgotLayout.setVisibility(View.GONE);
                }
            default:
                break;
        }
    }

    private void functionForgot() {
        // TODO Auto-generated method stub
        String email = etFEmailAddress.getText().toString();
        if (email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()) {
            etFEmailAddress.setError(getResources().getString(
                    R.string.error_invalid_email));

        } else {

            loaderDialog.show();

            String url = CommonFunctions.main_url + CommonFunctions.lang
                    + urlObj.getForgetUrl() + "?email="
                    + etFEmailAddress.getText().toString();

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    this);

            alertDialog.setPositiveButton(
                    getResources().getString(R.string.ok),
                    new AlertDialog.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            alertDialog.setCancelable(false);

            RequestQueue queue = Volley
                    .newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("response", response.toString());
                                String IsValid = response.getString("IsValid");

                                if (loaderDialog.isShowing())
                                    loaderDialog.dismiss();

                                if (IsValid.equals("true")) {
                                    if (!fromPax) {
                                        llLoginLayout
                                                .setVisibility(View.VISIBLE);
                                        llForgotLayout.setVisibility(View.GONE);
                                        Toast.makeText(
                                                getApplicationContext(),
                                                response.getString("LogInMessage"),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        alertDialog.setMessage(response
                                                .getString("LogInMessage"));
                                        alertDialog.show();
                                    }

                                } else {

                                    etFEmailAddress.setError(response
                                            .getString("LogInMessage"));
                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Warning: Invalid E-Mail Address.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(),
                            "Warning: Invalid E-Mail Address.",
                            Toast.LENGTH_SHORT).show();
                    VolleyLog.d("TAg", "Error: " + error.getMessage()); // hide
                    // the
                    // progress
                    // dialog
                }
            });
            queue.add(jsonObjReq);

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

    private class LoginBackService extends AsyncTask<Void, Void, String> {

        String strUser = null, strPass = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            loaderDialog.show();
            strUser = etEmailAddress.getText().toString();
            strPass = etPassword.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {

                UrlParameterBuilder urlObj = new UrlParameterBuilder();

                String urlParameters = urlObj.getLoginParams(strUser, strPass,
                        false, "0");

                String request = CommonFunctions.main_url
                        + CommonFunctions.lang + urlObj.getLoginUrl();

                String res = new HttpHandler().makeServiceCallWithParams(
                        request, urlParameters);

                System.out.println("Response" + res);

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
                            CommonFunctions.loggedIn = temp;
                            SharedPreferences pref = getApplicationContext()
                                    .getSharedPreferences("MyLoginPref", 0);
                            SharedPreferences.Editor logineditor = pref.edit();
                            logineditor.putString("Email", etEmailAddress
                                    .getText().toString());
                            logineditor.commit();
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.success_msg_login),
                                    Toast.LENGTH_SHORT).show();
                            finish();
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
            super.onPostExecute(result);
        }
    }
}
