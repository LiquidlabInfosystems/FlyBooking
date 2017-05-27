package com.travel.common_handlers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class UrlParameterBuilder extends UrlBuilder{

	public UrlParameterBuilder() {
	}

	public String getLoginParams(String userName, String password,
			boolean isPax, String TransType)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		//To generate login parameters
		
		StringBuilder sb = new StringBuilder();
		sb.append("UserName=" + URLEncoder.encode(userName, "UTF-8"));
		sb.append("&Password="
				+ URLEncoder.encode(CommonFunctions.SHA1Encrypt(password),
						"UTF-8"));
		sb.append("&ReturnPaxDetails="
				+ URLEncoder.encode(isPax ? "true" : "false", "UTF-8"));
		sb.append("&TransType="
				+ URLEncoder.encode(TransType.equalsIgnoreCase("0") ? TransType
						: (TransType.equalsIgnoreCase("flight") ? "1" : "3"),
						"UTF-8"));
		return sb.toString();

	}
	
	public String getRegisterParams(String FirstName, String LastName, String MobileNumber,
			String CountryCode, String MiddleName, String Email, String Password, String ConfirmPassword) throws JSONException, NoSuchAlgorithmException, UnsupportedEncodingException {
		//To generate register parameters
		
		JSONObject json = new JSONObject();

		json.put("FirstName", FirstName);
		json.put("LastName", LastName);
		json.put("MobileNumber", MobileNumber);
		json.put("CountryCode", CountryCode);
		json.put("MiddleName", MiddleName);
		json.put("Email", Email);
		json.put("Password", CommonFunctions.SHA1Encrypt(Password));
		json.put("ConfirmPassword", CommonFunctions.SHA1Encrypt(ConfirmPassword));

		return json.toString();
	}
	
	public String getUpdatePassParams(String pass) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		pass = "password="
				+ URLEncoder.encode(CommonFunctions.SHA1Encrypt(pass)
						, "UTF-8");
		return pass;
				
	}
	
	public String getProfileParams(HashMap<String, String> map) throws JSONException {
		//To generate my profile parameters
		
		JSONObject json = new JSONObject();

		json.put("Tittle", map.get("Tittle"));
		json.put("FirstName", map.get("FirstName"));
		json.put("MiddleName", "");
		json.put("LastName", map.get("LastName"));
		json.put("DateOfBirth", map.get("DateOfBirth"));
		json.put("Gender", map.get("Gender"));
		json.put("MobileCode", map.get("MobileCode"));
		json.put("MobileNumber", map.get("MobileNumber"));
		json.put("PassportNumber", map.get("PassportNumber"));
		json.put("Citizenship", map.get("Citizenship"));
		json.put("PassengerId", map.get("PassengerId"));
		json.put("PassportExpiryDate", map.get("PassportExpiryDate"));
		json.put("PassportPlaceOfIssue", map.get("PassportPlaceOfIssue"));
		System.out.println(json.toString());
		
		return json.toString();
	}

}
