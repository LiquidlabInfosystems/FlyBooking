package com.travel.datahandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.travel.model.HotelDetailsItem;
import com.travel.model.HotelDetailsItem.RoomCombination;
import com.travel.model.PaymentModel.AvailablePaymentGateways;
import com.travel.model.HotelDetailsRooms;
import com.travel.model.HotelPaxModel;
import com.travel.model.HotelResultItem;
import com.travel.model.PaymentModel;
import com.travel.model.RoomTypeDetails;

public class HotelDataHandler {

	public HotelDataHandler() {
	}

	public ArrayList<HotelResultItem> parseHotelResult(String result)
			throws JSONException, NullPointerException, Exception {

		// To parse hotel result from server response string

		ArrayList<HotelResultItem> arrayHotelResult = new ArrayList<HotelResultItem>();
		if (result != null) {
			JSONArray jarray = null;
			// Parse String to JSON object
			jarray = new JSONArray(result);

			JSONObject hotelObj = null, allHotels = null;
			HotelResultItem hItem = null;

			hotelObj = jarray.getJSONObject(0);
			// URL url;
			JSONArray allHotelsArray = hotelObj.getJSONArray("Hotels");

			int resultCount = allHotelsArray.length();

			for (int j = 0; j < resultCount; j++) {
				allHotels = allHotelsArray.getJSONObject(j);
				hItem = new HotelResultItem();
				hItem.setHotelName(allHotels.getString("HotelName"));
				hItem.setHotelDescription(allHotels
						.getString("HotelDescription"));
				hItem.setHotelAddress(allHotels.getString("HotelAddress"));
				try {
					if (!allHotels.getString("HotelRating").equals(""))
						hItem.setHotelRating(Float.parseFloat(allHotels
								.getString("HotelRating")));
				} catch (Exception e) {

					e.printStackTrace();
				}
				hItem.setHotelThumbImage(allHotels.getString("HotelThumbImage"));

				hItem.setHotelDisplayRate(allHotels
						.getDouble("HotelDisplayRate"));
				hItem.setDisplayRate(allHotels.getString("HotelDisplayRate"));
				try {
					if (!allHotels.getString("HotelLattitude").equals(""))
						hItem.setHotelLattitude(Double.parseDouble(allHotels
								.getString("HotelLattitude")));
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if (!allHotels.getString("HotelLongitude").equals(""))
						hItem.setHotelLongitude(Double.parseDouble(allHotels
								.getString("HotelLongitude")));
				} catch (Exception e) {
					e.printStackTrace();
				}

				hItem.setHotelLocation(allHotels.getString("HotelLocation"));
				hItem.setDeepLinkUrl(allHotels.getString("DeepLinkUrl"));
				hItem.setBoardTypes(allHotels.getString("BoardTypes")
						.toUpperCase());
				hItem.setNativeDeepUrl(allHotels.getString("NativeDeepLinkUrl"));
				
				if(allHotels.has("UserRating")) {
					hItem.setUserRating(allHotels.getInt("UserRating"));
				}
				
				// hItem.strLastBooked =
				// allHotels.getString("LastBooked");
				// hItem.strWatchingCount =
				// allHotels.getString("watchingCount");

				// url = new URL(hItem.strHotelThumbImage);
				// try {
				// hItem.imgHotelLogo =
				// BitmapFactory.decodeStream(url.openConnection().getInputStream());
				// } catch(NullPointerException e) {
				// } catch (Exception e) {
				// Log.w("ImageDownloader",
				// "Error downloading image from " + url);
				// }

				arrayHotelResult.add(hItem);
			}

		}
		System.out.println("------------------Parsing finished-------------");
		return arrayHotelResult;

	}

	public double getMinPrice(ArrayList<HotelResultItem> arrayHotelResult) {
		// To get minimum hotel price from hotel result array
		return (arrayHotelResult).get(0).getHotelDisplayRate();
	}

	public double getMaxPrice(ArrayList<HotelResultItem> arrayHotelResult) {
		// To get maximum hotel price from hotel result array
		return (arrayHotelResult).get(arrayHotelResult.size() - 1)
				.getHotelDisplayRate();
	}

	public int[] getStarCounts(ArrayList<HotelResultItem> arrayHotelResult) {
		// To get hotel result count based on star rating from hotel result
		// array
		int[] starCounts = new int[6];

		for (HotelResultItem hItem : arrayHotelResult) {
			switch ((int) hItem.getHotelRating()) {
			case 0:
				starCounts[0]++;
				break;
			case 1:
				starCounts[1]++;
				break;
			case 2:
				starCounts[2]++;
				break;
			case 3:
				starCounts[3]++;
				break;
			case 4:
				starCounts[4]++;
				break;
			case 5:
				starCounts[5]++;
				break;
			default:
				break;
			}
		}

		return starCounts;
	}

	public ArrayList<String> getBoardTypeArray(
			ArrayList<HotelResultItem> arrayHotelResult) {
		// To get distinct board types from hotel result

		ArrayList<String> arrayBoardTypes = new ArrayList<String>();

		for (HotelResultItem hItem : arrayHotelResult) {
			if (!arrayBoardTypes.contains(hItem.getBoardTypes())
					&& !hItem.getBoardTypes().equalsIgnoreCase(""))
				arrayBoardTypes.add(hItem.getBoardTypes());
		}

		return arrayBoardTypes;
	}

	public boolean hasBreakfast(ArrayList<HotelResultItem> arrayHotelResult) {
		// To check hotel result result for breakfast boardtype
		for (HotelResultItem hItem : arrayHotelResult) {
			if (hItem.getBoardTypes().toLowerCase().contains("breakfast")
					|| hItem.getBoardTypes().toLowerCase().contains("الافطار")
					|| hItem.getBoardTypes().toLowerCase().contains("افطار"))
				return true;
		}

		return false;
	}

	public ArrayList<HotelResultItem> sortHotelData(
			ArrayList<HotelResultItem> arrayHotelResult, boolean blSortPrice,
			boolean blSortRating, boolean blSortHotelName,
			String strSortPriceType, String strSortRatingType,
			String strSortHotelNameType) {

		// To sort hotel result based on supplied params

		if (blSortPrice) {
			if (arrayHotelResult.size() > 0) {
				Collections.sort(arrayHotelResult,
						new Comparator<HotelResultItem>() {

							@Override
							public int compare(HotelResultItem lhs,
									HotelResultItem rhs) {
								// TODO Auto-generated method stub
								return (lhs.getHotelDisplayRate())
										.compareTo(rhs.getHotelDisplayRate());
							}
						});

				if (strSortPriceType.equalsIgnoreCase("high")) {
					Collections.reverse(arrayHotelResult);
				}
			}
		} else if (blSortRating) {
			if (arrayHotelResult.size() > 0) {
				quickSort(arrayHotelResult, 0, arrayHotelResult.size() - 1);
				if (strSortRatingType.equalsIgnoreCase("high")) {
					Collections.reverse(arrayHotelResult);
				}
			}
		} else {
			if (arrayHotelResult.size() > 0) {
				Collections.sort(arrayHotelResult,
						new Comparator<HotelResultItem>() {

							@Override
							public int compare(HotelResultItem lhs,
									HotelResultItem rhs) {
								// TODO Auto-generated method stub
								return lhs.getHotelName().compareToIgnoreCase(
										rhs.getHotelName());
							}
						});
				if (strSortHotelNameType.equalsIgnoreCase("high")) {
					Collections.reverse(arrayHotelResult);
				}
			}

		}
		return arrayHotelResult;
	}

	/* * This method implements in-place quicksort algorithm recursively. */
	private void quickSort(ArrayList<HotelResultItem> temp, int low, int high) {
		int i = low;
		int j = high;
		// pivot is middle index
		HotelResultItem pivot = temp.get(low + (high - low) / 2);

		// Divide into two arrays
		while (i <= j) {
			/**
			 * * As shown in above image, In each iteration, we will identify a
			 * * number from left side which is greater then the pivot value,
			 * and * a number from right side which is less then the pivot
			 * value. Once * search is complete, we can swap both numbers.
			 */
			while (temp.get(i).getHotelRating() < pivot.getHotelRating()) {
				i++;
			}

			while (temp.get(j).getHotelRating() > pivot.getHotelRating()) {
				j--;
			}

			if (i <= j) {
				Collections.swap(temp, i, j);
				// move index to next position on both sides
				i++;
				j--;
			}
		}
		// calls quickSort() method recursively
		if (low < j) {
			quickSort(temp, low, j);
		}
		if (i < high) {
			quickSort(temp, i, high);
		}
	}

	public ArrayList<HotelResultItem> filterStars(
			ArrayList<HotelResultItem> arrayHotelResult, boolean blNoStar,
			boolean blOneStar, boolean blTwoStar, boolean blThreeStar,
			boolean blFourStar, boolean blFiveStar) {

		// To filter hotel result based on star rating

		if ((blNoStar || blOneStar || blTwoStar || blThreeStar || blFourStar || blFiveStar)
				&& !(blNoStar && blOneStar && blTwoStar && blThreeStar
						&& blFourStar && blFiveStar)) {
			ArrayList<HotelResultItem> filteredResult = new ArrayList<HotelResultItem>();
			for (HotelResultItem hitem : arrayHotelResult) {
				if (hitem.getHotelRating() == 0 && blNoStar)
					filteredResult.add(hitem);
				else if (hitem.getHotelRating() == 1 && blOneStar)
					filteredResult.add(hitem);
				else if (hitem.getHotelRating() == 2 && blTwoStar)
					filteredResult.add(hitem);
				else if (hitem.getHotelRating() == 3 && blThreeStar)
					filteredResult.add(hitem);
				else if (hitem.getHotelRating() == 4 && blFourStar)
					filteredResult.add(hitem);
				else if (hitem.getHotelRating() == 5 && blFiveStar)
					filteredResult.add(hitem);
			}
			return filteredResult;
		} else
			return arrayHotelResult;
	}

	public ArrayList<HotelResultItem> filterPrice(
			ArrayList<HotelResultItem> arrayHotelResult, double filterMinPrice,
			double filterMaxPrice) {

		// To filter hotel result based on price range

		ArrayList<HotelResultItem> tempArray = new ArrayList<HotelResultItem>();
		for (HotelResultItem hitem : arrayHotelResult) {
			if (hitem.getHotelDisplayRate() >= filterMinPrice
					&& hitem.getHotelDisplayRate() <= filterMaxPrice) {
				tempArray.add(hitem);
			}
		}

		return tempArray;
	}

	public ArrayList<HotelResultItem> filterName(
			ArrayList<HotelResultItem> arrayHotelResult, String strSearchName) {

		// To filter hotel result based on hotel name

		ArrayList<HotelResultItem> tempArray = new ArrayList<HotelResultItem>();
		for (HotelResultItem hitem : arrayHotelResult) {
			if ((hitem.getHotelName().toLowerCase()).contains(strSearchName
					.toLowerCase())) {
				tempArray.add(hitem);
			}
		}

		return tempArray;
	}

	public ArrayList<HotelResultItem> filterBoardType(
			ArrayList<HotelResultItem> arrayHotelResult) {
		// To filter hotel result based on board type

		ArrayList<HotelResultItem> tempArray = new ArrayList<HotelResultItem>();
		for (HotelResultItem hItem : arrayHotelResult) {
			if (hItem.getBoardTypes().toLowerCase().contains("breakfast")
					|| hItem.getBoardTypes().toLowerCase().contains("الافطار")
					|| hItem.getBoardTypes().toLowerCase().contains("افطار")) {
				tempArray.add(hItem);
			}
		}

		return tempArray;
	}

	public HotelDetailsItem parseHotelDetails(String result)
			throws JSONException, NullPointerException, Exception {

		// To parse hotel details from server response string

		if (result != null) {

			HotelDetailsItem hotelDetailsItem = new HotelDetailsItem();
			final JSONObject jObj = new JSONObject(result);

			int i = 0, length;
			hotelDetailsItem.setHotelName(jObj.getString("PropName"));
			hotelDetailsItem.setHotelUniqueId(jObj.getInt("HotelUniqueId"));
			hotelDetailsItem.setHotelCode(jObj.getString("HotelCode"));
			hotelDetailsItem.setAPIID(jObj.getInt("APIID"));
			hotelDetailsItem.setHotelAddress(jObj.getString("HotelAddress"));

			try {
				if (!jObj.getString("HotelStar").equals(""))
					hotelDetailsItem.setHotelStar(Float.parseFloat(jObj
							.getString("HotelStar")));
			} catch (Exception e) {

				e.printStackTrace();
			}
			hotelDetailsItem.setHotelLocation(jObj.getString("HotelLocation"));

			try {

				hotelDetailsItem.setLatitude(jObj.getDouble("Latitude"));
				hotelDetailsItem.setLongitude(jObj.getDouble("Longitude"));

			} catch (Exception e) {
			}

			hotelDetailsItem.setIsCombination(jObj.getBoolean("IsCombination"));

			JSONArray jArray = null;
			jArray = jObj.getJSONArray("Rooms");
			length = jArray.length();
			HotelDetailsRooms[] hotelRooms = new HotelDetailsRooms[length];

			for (i = 0; i < length; ++i) {
				JSONObject jObjRoom = jArray.getJSONObject(i);
				hotelRooms[i] = new HotelDetailsRooms();
				hotelRooms[i].setRoomName(jObjRoom.getString("RoomName"));
				hotelRooms[i].setRoomId(jObjRoom.getInt("RoomId"));
				hotelRooms[i].setRoomTag(jObjRoom.getString("RoomTag"));
				hotelRooms[i].setBoardType(jObjRoom.getString("BoardType"));
				hotelRooms[i].setPerNightPrice(jObjRoom
						.getDouble("PerNightPrice"));
				hotelRooms[i].setTotalAmount(jObjRoom.getDouble("TotalAmount"));
				hotelRooms[i].setCurrency(jObjRoom.getString("Currency"));
				hotelRooms[i].setAdultCount(jObjRoom.getInt("AdultCount"));
				hotelRooms[i].setChildCount(jObjRoom.getInt("ChildCount"));
				hotelRooms[i].setInfantCount(jObjRoom.getInt("InfantCount"));
				hotelRooms[i].setCancellationPolicyUrl(jObjRoom
						.getString("CancellationPolicyUrl"));
			}

			hotelDetailsItem.setRooms(hotelRooms);

			jArray = jObj.getJSONArray("Roomcombination");
			length = jArray.length();
			RoomCombination[] roomCombination = new RoomCombination[length];
			for (i = 0; i < length; ++i) {
				JSONObject jObjCombinations = jArray.getJSONObject(i);
				roomCombination[i] = hotelDetailsItem.new RoomCombination();
				roomCombination[i].setRoomCombination(jObjCombinations
						.getString("RoomCombination"));
			}
			hotelDetailsItem.setRoomcombination(roomCombination);

			jArray = jObj.getJSONArray("RoomTypeDetails");
			length = jArray.length();
			RoomTypeDetails[] roomTypeDetails = new RoomTypeDetails[length];
			for (i = 0; i < length; ++i) {
				roomTypeDetails[i] = new RoomTypeDetails();
				JSONObject jObjRoomTypeDetails = jArray.getJSONObject(i);
				roomTypeDetails[i].setAdultCount(jObjRoomTypeDetails
						.getInt("AdultCount"));
				roomTypeDetails[i].setChildCount(jObjRoomTypeDetails
						.getInt("ChildCount"));
				roomTypeDetails[i].setFirstChildAge(jObjRoomTypeDetails
						.getInt("FirstChildAge"));
				roomTypeDetails[i].setSecondChildAge(jObjRoomTypeDetails
						.getInt("SecondChildAge"));
				roomTypeDetails[i].setRoomTypeIdentifier(jObjRoomTypeDetails
						.getString("RoomTypeIdentifier"));
			}
			hotelDetailsItem.setRoomTypeDetails(roomTypeDetails);

			jArray = jObj.getJSONArray("ImageUrls");
			length = Math.min(5, jArray.length());
			String[] imageUrls = new String[length];
			for (i = 0; i < length; ++i) {
				imageUrls[i] = jArray.getString(i);
			}
			hotelDetailsItem.setImageUrls(imageUrls);

			hotelDetailsItem.setHotelDescription(jObj
					.getString("HotelDescription"));

			String temp = "";

			if (jObj.getString("FacilityItems") != null
					&& !jObj.getString("FacilityItems")
							.equalsIgnoreCase("null")) {
				JSONArray jAr;
				jArray = jObj.getJSONArray("FacilityItems");
				for (i = 0; i < jArray.length(); ++i) {
					JSONObject jFacility = jArray.getJSONObject(i);
					temp = temp + jFacility.getString("FacilitiesName") + "\n";
					jAr = jFacility.getJSONArray("FacilitiesList");
					for (int j = 0; j < jAr.length(); ++j) {
						jFacility = jAr.getJSONObject(j);
						temp = temp + jFacility.getString("DescriptionText")
								+ ", ";
					}
					temp = temp + "\n\n";
				}
			}

			hotelDetailsItem.setHotelFacility(temp);

			hotelDetailsItem.setCancellationPolicyUrl(jObj
					.getString("CancellationPolicyUrl"));

			hotelDetailsItem.setProceedPaxUrl(jObj.getString("ProceedPaxUrl"));

			hotelDetailsItem.setsID(jObj.getString("sID"));

			return hotelDetailsItem;
		}

		return null;

	}

	public String getCancellationPolicy(String strJsonData, Context context)
			throws JSONException, NullPointerException, Exception {
		// To get cancellation policy message from server response

		String temp = null;
		if (strJsonData != null) {
			JSONObject jObj = new JSONObject(strJsonData);
			JSONArray jArray = jObj.getJSONArray("room");
			jObj = jArray.getJSONObject(0);

			temp = jObj.getString("CancellationPolicy");

			if (jObj.has("TariffNotes")) {

				int resId = context.getResources().getIdentifier(
						"tariff_notes", "string", context.getPackageName());
				temp = temp + "\n\n" + context.getResources().getString(resId)
						+ "\n\n" + jObj.getString("TariffNotes");
			}

			if (jObj.has("Remarks")) {
				int resId = context.getResources().getIdentifier(
						"hotel_remarks", "string", context.getPackageName());
				temp = temp + "\n\n" + context.getResources().getString(resId)
						+ "\n\n" + jObj.getString("Remarks");

			}
		}
		return temp;
	}

	public HotelPaxModel setHotelPax(String strJson) throws JSONException {
		// To parse hotel pax response to HotelPaxModel

		JSONObject jPassList = new JSONObject(strJson);
		HotelPaxModel hotelPax = new HotelPaxModel();
		hotelPax.setFirstName(jPassList.getString("FirstName"));
		hotelPax.setLastName(jPassList.getString("LastName"));
		hotelPax.setEmail(jPassList.getString("Email"));
		hotelPax.setMobileNumber(jPassList.getString("MobileNumber"));
		String strMobCode = jPassList.getString("MobileCode").contains("+") ? jPassList
				.getString("MobileCode") : "+"
				+ jPassList.getString("MobileCode").replace(" ", "");
		hotelPax.setMobileCode(strMobCode);
		hotelPax.setTitle(jPassList.getString("tittle"));
		hotelPax.setPassengerType(jPassList.getString("PassengerType"));

		return hotelPax;
	}

	public String createHotelPaxString(HotelPaxModel[] hotelPaxModel) {
		// To create hotel pax params string from HotelPaxModel
		JSONArray HotelInfo = new JSONArray();
		JSONObject obj;

		for (HotelPaxModel hotelPaxItems : hotelPaxModel) {
			obj = new JSONObject();
			try {

				obj.put("FirstName", hotelPaxItems.getFirstName());
				obj.put("MiddleName", "");
				obj.put("LastName", hotelPaxItems.getLastName());
				obj.put("Email", hotelPaxItems.getEmail());
				obj.put("MobileNumber", hotelPaxItems.getMobileNumber());
				obj.put("MobileCode", hotelPaxItems.getMobileCode());
				obj.put("Citizenship", "KW");
				obj.put("Title", hotelPaxItems.getTitle());
				obj.put("PassengerType", hotelPaxItems.getPassengerType());
				obj.put("DateOfBirth", "09/16/1990");
				obj.put("Age", 26);
				HotelInfo.put(obj);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return HotelInfo.toString();
	}

	public PaymentModel getHotelPaymentDetails(String strJson)
			throws JSONException {
		// To parse payment response from server response string

		PaymentModel paymentModel = new PaymentModel();
		JSONObject jObj;
		jObj = new JSONObject(strJson);
		paymentModel.setResponseType(jObj.getString("RequestType"));
		paymentModel.setDeeplinkUrl(jObj.getString("ProceedPayUrl"));
		paymentModel.setCurrency(jObj.getString("Currency"));
		paymentModel.setTotalAmount(jObj.getDouble("TotalAmount"));
		paymentModel.setIsMigsPaymentGatewayActive(jObj
				.getBoolean("IsMigsPaymentGatewayActive"));

		JSONArray jArray = jObj.getJSONArray("AvailablePaymentGateways");
		JSONObject jObjtemp;
		int length = jArray.length(), i = 0;
		AvailablePaymentGateways[] availablePaymentGateways = new AvailablePaymentGateways[length];
		for (i = 0; i < jArray.length(); ++i) {
			jObjtemp = jArray.getJSONObject(i);
			availablePaymentGateways[i] = paymentModel.new AvailablePaymentGateways();
			availablePaymentGateways[i].setPaymentGateWayId(jObjtemp
					.getInt("PaymentGateWayId"));
			availablePaymentGateways[i].setServiceCharge(jObjtemp
					.getDouble("ServiceCharge"));
			availablePaymentGateways[i].setIsPercentage(jObjtemp
					.getBoolean("IsPercentage"));
			paymentModel
					.setConvertionRate(jObjtemp.getDouble("ConvertionRate"));
		}
		
		paymentModel.setAvailablePaymentGateways(availablePaymentGateways);
		paymentModel.setIsCashOnDeliveryActive(jObj
				.getBoolean("IsCashOnDeliveryActive"));
		if (jObj.has("IsInvoiceActive"))
			paymentModel.setIsInvoiceActive(jObj.getBoolean("IsInvoiceActive"));
		return paymentModel;
	}

}
