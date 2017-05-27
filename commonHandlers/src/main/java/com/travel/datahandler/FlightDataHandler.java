package com.travel.datahandler;

import com.travel.model.FlightPaxModel;
import com.travel.model.FlightPaxModel.BaggageList;
import com.travel.model.FlightPaxModel.BoardingDetails;
import com.travel.model.FlightPaxSubmissionModel;
import com.travel.model.FlightResultItem;
import com.travel.model.FlightResultModel;
import com.travel.model.FlightResultModel.AllJourney;
import com.travel.model.FlightResultModel.FareQuoteDetails;
import com.travel.model.ListFlight;
import com.travel.model.ListFlight.SegmentDetails;
import com.travel.model.PaymentModel;
import com.travel.model.PaymentModel.AvailablePaymentGateways;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class FlightDataHandler {

    public FlightDataHandler() { }

    public ArrayList<FlightResultItem> parseFlightResult(String result)
            throws JSONException, NullPointerException, Exception {
        // TODO parse flight result from response string

        ArrayList<FlightResultItem> arrayFlightResult = new ArrayList<FlightResultItem>();
        ArrayList<String> AirlineNames;
        String departureDateTime, departureTimeString;
        String arrivalDateTime, arrivalTimeString, totalDurationInMinutes;
        int stops;

        if (result != null) {
            JSONArray jarray = null;
            // Parse String TODO JSON object
            jarray = new JSONArray(result);

            if (jarray.length() == 0) {
                return null;
            } else {
                JSONObject c = null, allJourney = null, listFlight = null;
                FlightResultItem fItem = null;

                int length = jarray.length();

                for (int i = 0; i < length; i++) {
                    fItem = new FlightResultItem();
                    c = jarray.getJSONObject(i);
                    AirlineNames = new ArrayList<String>();

                    JSONArray allJourneyArray = c.getJSONArray("AllJourney");
                    for (int j = 0; j < allJourneyArray.length(); j++) {
                        allJourney = allJourneyArray.getJSONObject(j);
                        JSONArray listFlightArray = allJourney
                                .getJSONArray("ListFlight");
                        listFlight = listFlightArray.getJSONObject(0);

                        AirlineNames.add(listFlight.getString("FlightName"));
                        departureDateTime = listFlight
                                .getString("DepartureDateTime");
                        departureTimeString = listFlight
                                .getString("DepartureTimeString");

                        listFlight = listFlightArray
                                .getJSONObject(listFlightArray.length() - 1);
                        arrivalDateTime = listFlight
                                .getString("ArrivalDateTime");
                        arrivalTimeString = listFlight
                                .getString("ArrivalTimeString");

                        totalDurationInMinutes = allJourney
                                .getString("TotalDurationInMinutes");
                        stops = Integer.parseInt(allJourney.getString("stops"));

                        departureDateTime = departureDateTime.substring(6,
                                departureDateTime.length() - 2);
                        arrivalDateTime = arrivalDateTime.substring(6,
                                arrivalDateTime.length() - 2);

                        switch (j) {
                            case 0:
                                fItem.setDepartTimeOne(departureTimeString);
                                fItem.setArrivalTimeOne(arrivalTimeString);
                                fItem.setIntFlightStopsOne(stops);

                                fItem.setLongLayoverTimeInMins(listFlightArray
                                        .getJSONObject(0).getLong(
                                                "LayoverTimeMinutes"));

                                if (stops > 0) {
                                    fItem.setStrLayoverAirport(listFlightArray
                                            .getJSONObject(0).getString(
                                                    "ArrivalAirportName"));
                                }

                                fItem.setDepartDateTimeOne(Long
                                        .parseLong(departureDateTime));
                                fItem.setArrivalDateTimeOne(Long
                                        .parseLong(arrivalDateTime));
                                fItem.setLongFlightDurationInMinsOne(Long
                                        .parseLong(totalDurationInMinutes));

                                break;
                            case 1:
                                fItem.setDepartTimeTwo(departureTimeString);
                                fItem.setArrivalTimeTwo(arrivalTimeString);
                                // fItem.strFlightStopsTwo = stops;
                                // fItem.strFlightDurationTwo = totalDuration;
                                fItem.setDepartDateTimeTwo(Long
                                        .parseLong(departureDateTime));
                                fItem.setArrivalDateTimeTwo(Long
                                        .parseLong(arrivalDateTime));
                                fItem.setLongFlightDurationInMinsTwo(Long
                                        .parseLong(totalDurationInMinutes));

                                break;
                            case 2:
                                fItem.setDepartTimeThree(departureTimeString);
                                fItem.setArrivalTimeThree(arrivalTimeString);
                                // fItem.strFlightStopsThree = stops;
                                // fItem.strFlightDurationThree = totalDuration;
                                fItem.setDepartDateTimeThree(Long
                                        .parseLong(departureDateTime));
                                fItem.setArrivalDateTimeThree(Long
                                        .parseLong(arrivalDateTime));
                                fItem.setLongFlightDurationInMinsThree(Long
                                        .parseLong(totalDurationInMinutes));

                                break;
                            case 3:
                                fItem.setDepartTimeFour(departureTimeString);
                                fItem.setArrivalTimeFour(arrivalTimeString);
                                // fItem.strFlightStopsFour = stops;
                                // fItem.strFlightDurationFour = totalDuration;
                                fItem.setDepartDateTimeFour(Long
                                        .parseLong(departureDateTime));
                                fItem.setArrivalDateTimeFour(Long
                                        .parseLong(arrivalDateTime));
                                fItem.setLongFlightDurationInMinsFour(Long
                                        .parseLong(totalDurationInMinutes));

                                break;
                        }
                    }
                    fItem.setIntApiId(c.getInt("ApiId"));
                    fItem.setStrTripId(c.getString("TripId"));
                    fItem.setDoubleFlightPrice(Double.parseDouble(c
                            .getString("FinalTotalFare")));
                    fItem.setStrDisplayRate(c.getString("FinalTotalFare"));
                    if (c.has("Discount"))
                        fItem.setDiscount(c.getDouble("Discount"));
                    fItem.setStrDeepLink(c.getString("deeplinkURL"));
                    fItem.setBlRefundType(c.getBoolean("IsRefundable"));
                    fItem.setAirlineNames(AirlineNames);
                    fItem.setFareQuoteArray(c.getJSONArray("FareQuoteDetails"));
                    fItem.setJarray(allJourneyArray);
                    arrayFlightResult.add(fItem);

                }
            }

        }
        System.out.println("------------------Parsing finished-------------");
        return arrayFlightResult;
    }

    public ArrayList<FlightResultModel> parseFlightResult1(String result)
            throws JSONException, NullPointerException, Exception {
        // TODO parse flight result TODO flight model from response string

        if (result != null) {
            JSONArray jResultArray = null;
            // Parse String TODO JSON object
            jResultArray = new JSONArray(result);
            int length = jResultArray.length();

            if (length == 0) {
                return null;
            } else {
                ArrayList<FlightResultModel> flightResult = new ArrayList<>();

                JSONArray jAllJourneyArr = null, jListFlightArr = null, jArr = null;
                JSONObject jResultObj = null, jAllJourneyObj = null, jListFlightObj = null, jObj = null;

                int count = 0, arrlength, j = 0, listLfightCount, k, segmentCount, cnt = 0;
                FlightResultModel fModel;
                AllJourney[] allJourneys;
                ListFlight[] listFlights;
                FareQuoteDetails[] fareQuoteDetails;

                for (count = 0; count < length; ++count) {
                    fModel = new FlightResultModel();
                    jResultObj = jResultArray.getJSONObject(count);
                    fModel.setApiId(jResultObj.getInt("ApiId"));
                    fModel.setTripId(jResultObj.getInt("TripId"));
                    fModel.setIsRefundable(jResultObj
                            .getBoolean("IsRefundable"));
                    fModel.setSeatsLeft(jResultObj.getInt("SeatsLeft"));
                    fModel.setOperatingVender(jResultObj
                            .getString("OperatingVender"));
                    fModel.setFinalTotalFare(jResultObj
                            .getDouble("FinalTotalFare"));
                    fModel.setCurrency(jResultObj.getString("Currency"));
                    fModel.setDeeplinkURL(jResultObj.getString("deeplinkURL"));

                    jAllJourneyArr = jResultObj.getJSONArray("AllJourney");
                    arrlength = jAllJourneyArr.length();
                    allJourneys = new AllJourney[arrlength];
                    for (j = 0; j < arrlength; j++) {
                        jAllJourneyObj = jAllJourneyArr.getJSONObject(j);
                        allJourneys[j] = fModel.new AllJourney();
                        allJourneys[j].setTotalDurationInMinutes(jAllJourneyObj
                                .getInt("TotalDurationInMinutes"));
                        allJourneys[j].setTotalDuration(jAllJourneyObj
                                .getString("TotalDuration"));
                        allJourneys[j].setStops(jAllJourneyObj.getInt("stops"));

                        jListFlightArr = jAllJourneyObj
                                .getJSONArray("ListFlight");
                        listLfightCount = jListFlightArr.length();
                        listFlights = new ListFlight[listLfightCount];
                        for (k = 0; k < listLfightCount; ++k) {
                            listFlights[k] = new ListFlight();
                            jListFlightObj = jListFlightArr.getJSONObject(k);
                            listFlights[k].setFlightLogo(jListFlightObj
                                    .getString("FlightLogo"));
                            listFlights[k].setFlightCode(jListFlightObj
                                    .getString("FlightCode"));
                            listFlights[k].setFlightName(jListFlightObj
                                    .getString("FlightName"));
                            listFlights[k].setFlightNumber(jListFlightObj
                                    .getString("FlightNumber"));
                            listFlights[k].setDepartureCityCode(jListFlightObj
                                    .getString("DepartureCityCode"));
                            listFlights[k].setDepartureCityName(jListFlightObj
                                    .getString("DepartureCityName"));
                            listFlights[k].setArrivalCityCode(jListFlightObj
                                    .getString("ArrivalCityCode"));
                            listFlights[k].setArrivalCityName(jListFlightObj
                                    .getString("ArrivalCityName"));
                            listFlights[k].setDepartureDateTime(jListFlightObj
                                    .getString("DepartureDateTime"));
                            listFlights[k].setArrivalDateTime(jListFlightObj
                                    .getString("ArrivalDateTime"));
                            listFlights[k]
                                    .setDepartureDateString(jListFlightObj
                                            .getString("DepartureDateString"));
                            listFlights[k].setArrivalDateString(jListFlightObj
                                    .getString("ArrivalDateString"));
                            listFlights[k]
                                    .setDepartureTimeString(jListFlightObj
                                            .getString("DepartureTimeString"));
                            listFlights[k].setArrivalTimeString(jListFlightObj
                                    .getString("ArrivalTimeString"));
                            listFlights[k]
                                    .setDepartureAirportName(jListFlightObj
                                            .getString("DepartureAirportName"));
                            listFlights[k].setArrivalAirportName(jListFlightObj
                                    .getString("ArrivalAirportName"));
                            listFlights[k].setDurationPerLeg(jListFlightObj
                                    .getString("DurationPerLeg"));
                            listFlights[k].setTransitTime(jListFlightObj
                                    .getString("TransitTime"));
                            listFlights[k].setLayoverTimeMinutes(jListFlightObj
                                    .getInt("LayoverTimeMinutes"));
                            listFlights[k].setEquipmentNumber(jListFlightObj
                                    .getString("EquipmentNumber"));
                            listFlights[k].setBookingCode(jListFlightObj
                                    .getString("BookingCode"));
                            listFlights[k].setMealCode(jListFlightObj
                                    .getString("MealCode"));

                            jArr = jListFlightObj
                                    .getJSONArray("SegmentDetails");
                            segmentCount = jArr.length();
                            SegmentDetails[] segmentDetails = new SegmentDetails[segmentCount];
                            for (cnt = 0; cnt < segmentCount; ++cnt) {
                                jObj = jArr.getJSONObject(cnt);
                                segmentDetails[cnt] = listFlights[k].new SegmentDetails();
                                segmentDetails[cnt].setBaggage(jObj
                                        .getString("Baggage"));
                            }
                        }
                        allJourneys[j].setListFlight(listFlights);
                    }
                    fModel.setAllJourneys(allJourneys);
                    jArr = jResultObj.getJSONArray("FareQuoteDetails");
                    arrlength = jArr.length();
                    fareQuoteDetails = new FareQuoteDetails[arrlength];
                    for (cnt = 0; cnt < arrlength; ++cnt) {
                        fareQuoteDetails[cnt] = fModel.new FareQuoteDetails();
                        jObj = jArr.getJSONObject(cnt);
                        fareQuoteDetails[cnt].setPassengerType(jObj
                                .getString("PassengerType"));
                        fareQuoteDetails[cnt].setCurrency(jObj
                                .getString("Currency"));
                        fareQuoteDetails[cnt].setCount(jObj.getInt("Count"));
                        fareQuoteDetails[cnt].setBaseFare(jObj
                                .getDouble("BaseFare"));
                        fareQuoteDetails[cnt].setAddiitonalCharges(jObj
                                .getDouble("AddiitonalCharges"));
                        fareQuoteDetails[cnt].setDiscount(jObj
                                .getDouble("Discount"));
                        fareQuoteDetails[cnt].setTotalAmount(jObj
                                .getDouble("TotalAmount"));
                        fareQuoteDetails[cnt].setTotalAmountForCount(jObj
                                .getDouble("TotalAmountForCount"));
                    }
                    fModel.setFareQuoteDetails(fareQuoteDetails);

                    flightResult.add(fModel);
                }
                return flightResult;
            }
        }
        return null;
    }

    public ArrayList<FlightResultModel> groupResult1(
            ArrayList<FlightResultModel> arrayFlightResult) {
        // TODO group flight result based on price

        int sameCount = 1;
        ArrayList<FlightResultModel> groupedResult = new ArrayList<FlightResultModel>();
        ArrayList<Double> priceArray = new ArrayList<Double>();
        FlightResultModel fItemTemp = new FlightResultModel();
        Double priceTemp = null;
        for (FlightResultModel fitem : arrayFlightResult) {

            if (!String.valueOf(priceTemp).equalsIgnoreCase(
                    String.valueOf(fitem.getFinalTotalFare()))) {
                if (priceArray.contains(fItemTemp.getFinalTotalFare())) {
                    fItemTemp.samePriceCount = sameCount;
                    sameCount = 1;
                    groupedResult.add(fItemTemp);
                }
                fItemTemp = fitem;
                priceTemp = fitem.getFinalTotalFare();
                priceArray.add(fitem.getFinalTotalFare());

            } else {
                sameCount++;
            }
        }
        if (priceArray.contains(fItemTemp.getFinalTotalFare())) {
            fItemTemp.samePriceCount = sameCount;
            sameCount = 1;
            groupedResult.add(fItemTemp);
        }
        System.out.println("groupedResult" + priceArray);
        return groupedResult;
    }

    public ArrayList<FlightResultItem> groupResult(
            ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO group flight result based on price

        int sameCount = 1;
        ArrayList<FlightResultItem> groupedResult = new ArrayList<FlightResultItem>();
        ArrayList<Double> priceArray = new ArrayList<Double>();
        FlightResultItem fItemTemp = new FlightResultItem();
        Double priceTemp = null;
        for (FlightResultItem fitem : arrayFlightResult) {

            if (!String.valueOf(priceTemp).equalsIgnoreCase(
                    String.valueOf(fitem.getDoubleFlightPrice()))) {
                if (priceArray.contains(fItemTemp.getDoubleFlightPrice())) {
                    fItemTemp.samePriceCount = sameCount;
                    sameCount = 1;
                    groupedResult.add(fItemTemp);
                }
                fItemTemp = fitem;
                priceTemp = fitem.getDoubleFlightPrice();
                priceArray.add(fitem.getDoubleFlightPrice());

            } else {
                sameCount++;
            }
        }
        if (priceArray.contains(fItemTemp.getDoubleFlightPrice())) {
            fItemTemp.samePriceCount = sameCount;
            sameCount = 1;
            groupedResult.add(fItemTemp);
        }
        System.out.println("groupedResult" + priceArray);
        return groupedResult;
    }

    public ArrayList<FlightResultItem> groupThreeResult(
            ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO group flight result into block of three based on price

        int sameCount = 1;
        ArrayList<FlightResultItem> groupedResult = new ArrayList<>();
        ArrayList<Double> priceArray = new ArrayList<>();
        FlightResultItem fItemTemp = new FlightResultItem();
        Double priceTemp = null;
        for (FlightResultItem fitem : arrayFlightResult) {

            if (!String.valueOf(priceTemp).equalsIgnoreCase(
                    String.valueOf(fitem.getDoubleFlightPrice()))) {
                if (priceArray.contains(fItemTemp.getDoubleFlightPrice())) {
                    fItemTemp.samePriceCount = sameCount;
                    if(sameCount > 3)
                        groupedResult.add(fItemTemp);
                }
                if (!priceArray.contains(fitem.getDoubleFlightPrice())){
                    sameCount = 1;
                    groupedResult.add(fitem);
                    fItemTemp = fitem;
                    priceTemp = fitem.getDoubleFlightPrice();
                    priceArray.add(fitem.getDoubleFlightPrice());
                }
            } else {
                sameCount++;
                if(sameCount == 2)
                    groupedResult.add(fitem);
                else if (sameCount == 3)
                    fItemTemp = fitem;
            }
        }
        if (priceArray.contains(fItemTemp.getDoubleFlightPrice())) {
            fItemTemp.samePriceCount = sameCount;
            sameCount = 1;
            groupedResult.add(fItemTemp);
        }
        System.out.println("groupedResult" + priceArray);
        return groupedResult;
    }

    public ArrayList<String> getAirlineList(
            ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO get unique airline list from flight result

        ArrayList<String> arrayAirline = new ArrayList<String>();

        for (FlightResultItem fItem : arrayFlightResult) {

            if (!arrayAirline.contains(fItem.getAirlineNames().get(0))
                    && !fItem.getAirlineNames().get(0).equalsIgnoreCase(""))
                arrayAirline.add(fItem.getAirlineNames().get(0));
        }

        return arrayAirline;

    }

    public ArrayList<String> getLayoverAirlineList(
            ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO get unique layover airport list from flight result

        ArrayList<String> arrayAirports = new ArrayList<String>();

        for (FlightResultItem fItem : arrayFlightResult) {
            if (fItem.getIntFlightStopsOne() > 0) {
                if (!arrayAirports.contains(fItem.getStrLayoverAirport())
                        && !fItem.getStrLayoverAirport().equalsIgnoreCase(""))
                    arrayAirports.add(fItem.getStrLayoverAirport());
            }
        }

        return arrayAirports;

    }

    public ArrayList<FlightResultItem> sortFlightResult(
            ArrayList<FlightResultItem> arrayFlightResult, boolean blSortPrice,
            boolean blSortDep, boolean blSortArrival, boolean blSortDuration,
            boolean blSortAirName, String strSortPriceType,
            String strSortDepType, String strSortArrivalType,
            String strSortDurationType, String strSortAirNameType) {
        // TODO sort flight result based TODO supplied params

        if (blSortPrice) {
            if (arrayFlightResult.size() > 0) {
                Collections.sort(arrayFlightResult,
                        new Comparator<FlightResultItem>() {

                            @Override
                            public int compare(FlightResultItem lhs,
                                               FlightResultItem rhs) {
                                // TODO Auto-generated method stub
                                return (lhs.getDoubleFlightPrice())
                                        .compareTo(rhs.getDoubleFlightPrice());
                            }
                        });
                if (strSortPriceType.equalsIgnoreCase("high")) {
                    Collections.reverse(arrayFlightResult);
                }
            }
        } else if (blSortDep) {
            if (arrayFlightResult.size() > 0) {
                Collections.sort(arrayFlightResult,
                        new Comparator<FlightResultItem>() {

                            @Override
                            public int compare(FlightResultItem lhs,
                                               FlightResultItem rhs) {
                                // TODO Auto-generated method stub
                                return (lhs.getDepartDateTimeOne())
                                        .compareTo(rhs.getDepartDateTimeOne());
                            }
                        });
                if (strSortDepType.equalsIgnoreCase("high")) {
                    Collections.reverse(arrayFlightResult);
                }
            }
        } else if (blSortArrival) {
            if (arrayFlightResult.size() > 0) {
                Collections.sort(arrayFlightResult,
                        new Comparator<FlightResultItem>() {

                            @Override
                            public int compare(FlightResultItem lhs,
                                               FlightResultItem rhs) {
                                // TODO Auto-generated method stub
                                return (lhs.getArrivalDateTimeOne())
                                        .compareTo(rhs.getArrivalDateTimeOne());
                            }
                        });
                if (strSortArrivalType.equalsIgnoreCase("high")) {
                    Collections.reverse(arrayFlightResult);
                }
            }
        } else if (blSortDuration) {
            if (arrayFlightResult.size() > 0) {
                Collections.sort(arrayFlightResult,
                        new Comparator<FlightResultItem>() {

                            @Override
                            public int compare(FlightResultItem lhs,
                                               FlightResultItem rhs) {
                                // TODO Auto-generated method stub
                                return (lhs.getLongFlightDurationInMinsOne()).compareTo(rhs
                                        .getLongFlightDurationInMinsOne());
                            }
                        });
                if (strSortDurationType.equalsIgnoreCase("high")) {
                    Collections.reverse(arrayFlightResult);
                }
            }

        } else {
            if (arrayFlightResult.size() > 0) {
                Collections.sort(arrayFlightResult,
                        new Comparator<FlightResultItem>() {

                            @Override
                            public int compare(FlightResultItem lhs,
                                               FlightResultItem rhs) {
                                // TODO Auto-generated method stub
                                return lhs
                                        .getAirlineNames()
                                        .get(0)
                                        .compareToIgnoreCase(
                                                rhs.getAirlineNames().get(0));
                            }
                        });
                if (strSortAirNameType.equalsIgnoreCase("high")) {
                    Collections.reverse(arrayFlightResult);
                }
            }

        }
        return arrayFlightResult;
    }

    public double[] getMinMaxPrice(ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO get minimum and maximum price from flight result
        return new double[]{
                (arrayFlightResult).get(0).getDoubleFlightPrice(),
                (arrayFlightResult).get(arrayFlightResult.size() - 1)
                        .getDoubleFlightPrice()};
    }

    public long[] getMinMaxLayover(ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO get minimum and maximum layover time from flight result

        ArrayList<Long> temp = new ArrayList<Long>();
        for (FlightResultItem fItem : arrayFlightResult) {
            // al.add(fItem.DepartDateTimeOne);
            // a2.add(fItem.ArrivalDateTimeOne);
            temp.add(fItem.getLongLayoverTimeInMins());
        }

        Collections.sort(temp);
        return new long[]{temp.get(0), temp.get(temp.size() - 1)};
    }

    public HashMap<String, Boolean> getStopDetails(
            ArrayList<FlightResultItem> arrayFlightResult) {
        // TODO check flight stops in flight result

        ArrayList<Long> temp = new ArrayList<Long>();
        for (FlightResultItem fItem : arrayFlightResult) {
            temp.add((long) fItem.getIntFlightStopsOne());
        }

        HashMap<String, Boolean> map = new HashMap<String, Boolean>();

        map.put("blHasNonStop", temp.contains((long) 0) ? true : false);
        map.put("blHasOneStop", temp.contains((long) 1) ? true : false);
        map.put("blHasMultStop",
                (temp.contains((long) 2) || temp.contains((long) 3)
                        || temp.contains((long) 4) || temp.contains((long) 5)) ? true
                        : false);

        return map;

    }

    public String[] getTimigsArrayOutbound(
            ArrayList<FlightResultItem> arrayFlightResult) {

        // TODO get price based on time intervals from flight result

        Locale locale = Locale.getDefault();

        Boolean bl12a6aFrom = false, bl6a12pFrom = false, bl12p6pFrom = false, bl6p12aFrom = false, bl12a6aTo = false, bl6a12pTo = false, bl12p6pTo = false, bl6p12aTo = false;
        int minutes = -1;
        String[] timingArray = {"", "", "", "", "", "", "", ""};
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", locale);
        Calendar cal = Calendar.getInstance();

        for (FlightResultItem fItem : arrayFlightResult) {

            try {
                cal.setTime(dateFormat.parse(fItem.getDepartTimeOne()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aFrom && minutes > 0 && minutes < 360) {
                timingArray[0] = fItem.getStrDisplayRate();
                bl12a6aFrom = true;
            }
            if (!bl6a12pFrom && minutes > 360 && minutes < 720) {
                timingArray[1] = fItem.getStrDisplayRate();
                bl6a12pFrom = true;
            }
            if (!bl12p6pFrom && minutes > 720 && minutes < 1080) {
                timingArray[2] = fItem.getStrDisplayRate();
                bl12p6pFrom = true;
            }
            if (!bl6p12aFrom && minutes > 1080 && minutes < 1440) {
                timingArray[3] = fItem.getStrDisplayRate();
                bl6p12aFrom = true;
            }

            try {
                cal.setTime(dateFormat.parse(fItem.getArrivalTimeOne()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aTo && minutes > 0 && minutes < 360) {
                timingArray[4] = fItem.getStrDisplayRate();
                bl12a6aTo = true;
            }
            if (!bl6a12pTo && minutes > 360 && minutes < 720) {
                timingArray[5] = fItem.getStrDisplayRate();
                bl6a12pTo = true;
            }
            if (!bl12p6pTo && minutes > 720 && minutes < 1080) {
                timingArray[6] = fItem.getStrDisplayRate();
                bl12p6pTo = true;
            }
            if (!bl6p12aTo && minutes > 1080 && minutes < 1440) {
                timingArray[7] = fItem.getStrDisplayRate();
                bl6p12aTo = true;
            }
        }
        return timingArray;
    }

    public String[] getTimigsArrayReturn(
            ArrayList<FlightResultItem> arrayFlightResult) {

        // TODO get minimum and maximum price from flight result

        Locale locale = Locale.getDefault();
        Boolean bl12a6aFrom = false, bl6a12pFrom = false, bl12p6pFrom = false, bl6p12aFrom = false, bl12a6aTo = false, bl6a12pTo = false, bl12p6pTo = false, bl6p12aTo = false;
        int minutes = -1;
        String[] timingArray = {"", "", "", "", "", "", "", ""};
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", locale);
        Calendar cal = Calendar.getInstance();

        for (FlightResultItem fItem : arrayFlightResult) {

            try {
                cal.setTime(dateFormat.parse(fItem.getDepartTimeTwo()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aFrom && minutes > 0 && minutes < 360) {
                timingArray[0] = fItem.getStrDisplayRate();
                bl12a6aFrom = true;
            }
            if (!bl6a12pFrom && minutes > 360 && minutes < 720) {
                timingArray[1] = fItem.getStrDisplayRate();
                bl6a12pFrom = true;
            }
            if (!bl12p6pFrom && minutes > 720 && minutes < 1080) {
                timingArray[2] = fItem.getStrDisplayRate();
                bl12p6pFrom = true;
            }
            if (!bl6p12aFrom && minutes > 1080 && minutes < 1440) {
                timingArray[3] = fItem.getStrDisplayRate();
                bl6p12aFrom = true;
            }

            try {
                cal.setTime(dateFormat.parse(fItem.getArrivalTimeTwo()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aTo && minutes > 0 && minutes < 360) {
                timingArray[4] = fItem.getStrDisplayRate();
                bl12a6aTo = true;
            }
            if (!bl6a12pTo && minutes > 360 && minutes < 720) {
                timingArray[5] = fItem.getStrDisplayRate();
                bl6a12pTo = true;
            }
            if (!bl12p6pTo && minutes > 720 && minutes < 1080) {
                timingArray[6] = fItem.getStrDisplayRate();
                bl12p6pTo = true;
            }
            if (!bl6p12aTo && minutes > 1080 && minutes < 1440) {
                timingArray[7] = fItem.getStrDisplayRate();
                bl6p12aTo = true;
            }
        }
        return timingArray;
    }

    public String[] getTimigsArrayOutbound24(
            ArrayList<FlightResultItem> arrayFlightResult) {

        // TODO get price based on time intervals from flight result

        Locale locale = Locale.getDefault();

        Boolean bl12a6aFrom = false, bl6a12pFrom = false, bl12p6pFrom = false, bl6p12aFrom = false, bl12a6aTo = false, bl6a12pTo = false, bl12p6pTo = false, bl6p12aTo = false;
        int minutes = -1;
        String[] timingArray = {"", "", "", "", "", "", "", ""};
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", locale);
        Calendar cal = Calendar.getInstance();

        for (FlightResultItem fItem : arrayFlightResult) {

            try {
                cal.setTime(dateFormat.parse(fItem.getDepartTimeOne()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aFrom && minutes > 0 && minutes < 360) {
                timingArray[0] = fItem.getStrDisplayRate();
                bl12a6aFrom = true;
            }
            if (!bl6a12pFrom && minutes > 360 && minutes < 720) {
                timingArray[1] = fItem.getStrDisplayRate();
                bl6a12pFrom = true;
            }
            if (!bl12p6pFrom && minutes > 720 && minutes < 1080) {
                timingArray[2] = fItem.getStrDisplayRate();
                bl12p6pFrom = true;
            }
            if (!bl6p12aFrom && minutes > 1080 && minutes < 1440) {
                timingArray[3] = fItem.getStrDisplayRate();
                bl6p12aFrom = true;
            }

            try {
                cal.setTime(dateFormat.parse(fItem.getArrivalTimeOne()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aTo && minutes > 0 && minutes < 360) {
                timingArray[4] = fItem.getStrDisplayRate();
                bl12a6aTo = true;
            }
            if (!bl6a12pTo && minutes > 360 && minutes < 720) {
                timingArray[5] = fItem.getStrDisplayRate();
                bl6a12pTo = true;
            }
            if (!bl12p6pTo && minutes > 720 && minutes < 1080) {
                timingArray[6] = fItem.getStrDisplayRate();
                bl12p6pTo = true;
            }
            if (!bl6p12aTo && minutes > 1080 && minutes < 1440) {
                timingArray[7] = fItem.getStrDisplayRate();
                bl6p12aTo = true;
            }
        }
        return timingArray;
    }

    public String[] getTimigsArrayReturn24(
            ArrayList<FlightResultItem> arrayFlightResult) {

        // TODO get minimum and maximum price from flight result

        Locale locale = Locale.getDefault();
        Boolean bl12a6aFrom = false, bl6a12pFrom = false, bl12p6pFrom = false, bl6p12aFrom = false, bl12a6aTo = false, bl6a12pTo = false, bl12p6pTo = false, bl6p12aTo = false;
        int minutes = -1;
        String[] timingArray = {"", "", "", "", "", "", "", ""};
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", locale);
        Calendar cal = Calendar.getInstance();

        for (FlightResultItem fItem : arrayFlightResult) {

            try {
                cal.setTime(dateFormat.parse(fItem.getDepartTimeTwo()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aFrom && minutes > 0 && minutes < 360) {
                timingArray[0] = fItem.getStrDisplayRate();
                bl12a6aFrom = true;
            }
            if (!bl6a12pFrom && minutes > 360 && minutes < 720) {
                timingArray[1] = fItem.getStrDisplayRate();
                bl6a12pFrom = true;
            }
            if (!bl12p6pFrom && minutes > 720 && minutes < 1080) {
                timingArray[2] = fItem.getStrDisplayRate();
                bl12p6pFrom = true;
            }
            if (!bl6p12aFrom && minutes > 1080 && minutes < 1440) {
                timingArray[3] = fItem.getStrDisplayRate();
                bl6p12aFrom = true;
            }

            try {
                cal.setTime(dateFormat.parse(fItem.getArrivalTimeTwo()));
                minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                        + cal.get(Calendar.MINUTE);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!bl12a6aTo && minutes > 0 && minutes < 360) {
                timingArray[4] = fItem.getStrDisplayRate();
                bl12a6aTo = true;
            }
            if (!bl6a12pTo && minutes > 360 && minutes < 720) {
                timingArray[5] = fItem.getStrDisplayRate();
                bl6a12pTo = true;
            }
            if (!bl12p6pTo && minutes > 720 && minutes < 1080) {
                timingArray[6] = fItem.getStrDisplayRate();
                bl12p6pTo = true;
            }
            if (!bl6p12aTo && minutes > 1080 && minutes < 1440) {
                timingArray[7] = fItem.getStrDisplayRate();
                bl6p12aTo = true;
            }
        }
        return timingArray;
    }

    public ArrayList<FlightResultItem> filterStops(
            ArrayList<FlightResultItem> arrayFlightResult, boolean blNonStop,
            Boolean blOneStop, Boolean blMultiStop) {
        // TODO filter flight result based on stops

        if (blNonStop || blOneStop || blMultiStop) {
            ArrayList<FlightResultItem> filteredResult = new ArrayList<FlightResultItem>();
            for (FlightResultItem fitem : arrayFlightResult) {
                if (fitem.getIntFlightStopsOne() == 0 && blNonStop)
                    filteredResult.add(fitem);
                else if (fitem.getIntFlightStopsOne() == 1 && blOneStop)
                    filteredResult.add(fitem);
                else if (fitem.getIntFlightStopsOne() > 1 && blMultiStop)
                    filteredResult.add(fitem);
            }
            return filteredResult;
        } else
            return arrayFlightResult;
    }

    public ArrayList<FlightResultItem> filterPrice(
            ArrayList<FlightResultItem> flightResult, double filterMinPrice,
            double filterMaxPrice) {
        // TODO filter flight result based on price
        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        for (FlightResultItem fitem : flightResult) {
            if (fitem.getDoubleFlightPrice() >= filterMinPrice
                    && fitem.getDoubleFlightPrice() <= filterMaxPrice) {
                temp.add(fitem);
            }
        }
        return temp;

    }

    public ArrayList<FlightResultItem> filterDepartTime(
            ArrayList<FlightResultItem> arrayFlightResult, double filterMinDep,
            double filterMaxDep, String lang) {
        // TODO filter flight result based on depart time

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",
                new Locale(lang));
        Calendar cal = Calendar.getInstance();
        for (FlightResultItem fitem : arrayFlightResult) {

            try {
                cal.setTime(dateFormat.parse(fitem.getDepartTimeOne()));
                if (cal.getTimeInMillis() >= filterMinDep
                        && cal.getTimeInMillis() <= filterMaxDep) {
                    temp.add(fitem);
                }
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterArrivalTime(
            ArrayList<FlightResultItem> arrayFlightResult, double filterMinArr,
            double filterMaxArr, String lang) {
        // TODO filter flight result based on arrival time

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",
                new Locale(lang));
        Calendar cal = Calendar.getInstance();
        for (FlightResultItem fitem : arrayFlightResult) {
            try {
                cal.setTime(dateFormat.parse(fitem.getDepartTimeTwo()));
                if (cal.getTimeInMillis() >= filterMinArr
                        && cal.getTimeInMillis() <= filterMaxArr) {
                    temp.add(fitem);
                }
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterDepartTime24(
            ArrayList<FlightResultItem> arrayFlightResult, double filterMinDep,
            double filterMaxDep, String lang) {
        // TODO filter flight result based on depart time

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", new Locale(
                lang));
        Calendar cal = Calendar.getInstance();
        for (FlightResultItem fitem : arrayFlightResult) {

            try {
                cal.setTime(dateFormat.parse(fitem.getDepartTimeOne()));
                if (cal.getTimeInMillis() >= filterMinDep
                        && cal.getTimeInMillis() <= filterMaxDep) {
                    temp.add(fitem);
                }
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterArrivalTime24(
            ArrayList<FlightResultItem> arrayFlightResult, double filterMinArr,
            double filterMaxArr, String lang) {
        // TODO filter flight result based on arrival time

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm", new Locale(
                lang));
        Calendar cal = Calendar.getInstance();
        for (FlightResultItem fitem : arrayFlightResult) {
            try {
                cal.setTime(dateFormat.parse(fitem.getDepartTimeTwo()));
                if (cal.getTimeInMillis() >= filterMinArr
                        && cal.getTimeInMillis() <= filterMaxArr) {
                    temp.add(fitem);
                }
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterAirlines(
            ArrayList<FlightResultItem> arrayFlightResult,
            ArrayList<String> checkedAirlines) {
        // TODO filter flight result based on airline

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        for (FlightResultItem fitem : arrayFlightResult) {
            for (String airline : checkedAirlines) {
                if (fitem.getAirlineNames().size() == getCountInArray(fitem.getAirlineNames(), airline)) {
                    temp.add(fitem);
                }
            }
        }
        return temp;
    }

    private int getCountInArray(ArrayList<String> array, String str) {
        int count = 0;
        for (String airline : array) {
            if (airline.equalsIgnoreCase(str))
                count++;
        }
        return count;
    }

    public ArrayList<FlightResultItem> filterLayoverTime(
            ArrayList<FlightResultItem> arrayFlightResult, double filterMinLay,
            double filterMaxLay) {
        // TODO filter flight result based on layover time

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        for (FlightResultItem fitem : arrayFlightResult) {
            if (fitem.getLongLayoverTimeInMins() >= filterMinLay
                    && fitem.getLongLayoverTimeInMins() <= filterMaxLay) {
                temp.add(fitem);
            }
        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterLayoverAirports(
            ArrayList<FlightResultItem> arrayFlightResult,
            ArrayList<String> checkedAirports) {
        // TODO filter flight result based on airports

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        for (FlightResultItem fitem : arrayFlightResult) {
            if (checkedAirports.contains(fitem.getStrLayoverAirport())) {
                temp.add(fitem);
            }
        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterRefundType(
            ArrayList<FlightResultItem> arrayFlightResult,
            boolean blFilterRefundable, boolean blFilterNonRefundable) {
        // TODO filter flight result based on refund type

        ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
        for (FlightResultItem fitem : arrayFlightResult) {
            if (fitem.getBlRefundType() && blFilterRefundable) {
                temp.add(fitem);
            } else if (!fitem.getBlRefundType() && blFilterNonRefundable) {
                temp.add(fitem);
            }
        }
        return temp;
    }

    public ArrayList<FlightResultItem> filterFlightTiming(
            ArrayList<FlightResultItem> arrayFlightResult, boolean blOutbound,
            boolean blReturn, boolean bl12a6aFrom, boolean bl6a12pFrom,
            boolean bl12p6pFrom, boolean bl6p12aFrom, boolean bl12a6aTo,
            boolean bl6a12pTo, boolean bl12p6pTo, boolean bl6p12aTo, String lang) {
        // TODO filter flight result based on timing

        if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
                || bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo) {
            ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",
                    new Locale(lang));
            Calendar cal = Calendar.getInstance();
            int minutes = -1;
            if (blOutbound) {
                if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom) {
                    for (FlightResultItem fitem : arrayFlightResult) {

                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getDepartTimeOne()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aFrom && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pFrom && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pFrom && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aFrom && minutes > 1080
                                && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }

                if (bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo) {
                    temp.clear();
                    for (FlightResultItem fitem : arrayFlightResult) {
                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getArrivalTimeOne()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aTo && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pTo && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pTo && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aTo && minutes > 1080 && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }
            } else if (blReturn) {
                if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom) {
                    for (FlightResultItem fitem : arrayFlightResult) {

                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getDepartTimeTwo()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aFrom && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pFrom && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pFrom && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aFrom && minutes > 1080
                                && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }
                if (bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo) {
                    temp.clear();
                    for (FlightResultItem fitem : arrayFlightResult) {
                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getArrivalTimeTwo()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aTo && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pTo && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pTo && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aTo && minutes > 1080 && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }
            }

        }
        return arrayFlightResult;
    }

    public ArrayList<FlightResultItem> filterFlightTiming24(
            ArrayList<FlightResultItem> arrayFlightResult, boolean blOutbound,
            boolean blReturn, boolean bl12a6aFrom, boolean bl6a12pFrom,
            boolean bl12p6pFrom, boolean bl6p12aFrom, boolean bl12a6aTo,
            boolean bl6a12pTo, boolean bl12p6pTo, boolean bl6p12aTo, String lang) {
        // TODO filter flight result based on timing

        if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom
                || bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo) {
            ArrayList<FlightResultItem> temp = new ArrayList<FlightResultItem>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm",
                    new Locale(lang));
            Calendar cal = Calendar.getInstance();
            int minutes = -1;
            if (blOutbound) {
                if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom) {
                    for (FlightResultItem fitem : arrayFlightResult) {

                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getDepartTimeOne()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aFrom && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pFrom && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pFrom && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aFrom && minutes > 1080
                                && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }

                if (bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo) {
                    temp.clear();
                    for (FlightResultItem fitem : arrayFlightResult) {
                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getArrivalTimeOne()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aTo && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pTo && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pTo && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aTo && minutes > 1080 && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }
            } else if (blReturn) {
                if (bl12a6aFrom || bl6a12pFrom || bl12p6pFrom || bl6p12aFrom) {
                    for (FlightResultItem fitem : arrayFlightResult) {

                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getDepartTimeTwo()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aFrom && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pFrom && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pFrom && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aFrom && minutes > 1080
                                && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }
                if (bl12a6aTo || bl6a12pTo || bl12p6pTo || bl6p12aTo) {
                    temp.clear();
                    for (FlightResultItem fitem : arrayFlightResult) {
                        try {
                            cal.setTime(dateFormat.parse(fitem
                                    .getArrivalTimeTwo()));
                            minutes = cal.get(Calendar.HOUR_OF_DAY) * 60
                                    + cal.get(Calendar.MINUTE);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bl12a6aTo && minutes > 0 && minutes < 360)
                            temp.add(fitem);
                        else if (bl6a12pTo && minutes > 360 && minutes < 720)
                            temp.add(fitem);
                        else if (bl12p6pTo && minutes > 720 && minutes < 1080)
                            temp.add(fitem);
                        else if (bl6p12aTo && minutes > 1080 && minutes < 1440)
                            temp.add(fitem);
                    }
                    arrayFlightResult.clear();
                    arrayFlightResult.addAll(temp);
                }
            }

        }
        return arrayFlightResult;
    }

    public FlightPaxModel[] getPaxFlightItems(String result)
            throws JSONException {

        // TODO parse pax details from string TODO pax model

        FlightPaxModel[] flightPaxItem = null;

        JSONObject jPassList, jObj = new JSONObject(result);
        jObj = jObj.getJSONObject("data");

        if (jObj.getBoolean("Isvalid")) {

            jObj = jObj.getJSONObject("Item");
            JSONArray jArrayBoarding, jArrayPass = jObj
                    .getJSONArray("PassengersInfo");
            int j = 0, length = jArrayPass.length();
            flightPaxItem = new FlightPaxModel[length];
            for (j = 0; j < length; j++) {

                flightPaxItem[j] = new FlightPaxModel();
                jPassList = jArrayPass.getJSONObject(j);
                flightPaxItem[j].setFirstname(jPassList.getString("Firstname"));
                flightPaxItem[j].setMiddleName(jPassList
                        .getString("MiddleName"));
                flightPaxItem[j].setLastName(jPassList.getString("LastName"));
                flightPaxItem[j].setEmail(jPassList.getString("Email"));
                flightPaxItem[j].setGender(jPassList.getString("Gender"));
                flightPaxItem[j].setPhoneNumber(jPassList
                        .getString("PhoneNumber"));
                flightPaxItem[j].setDateOfBirth(jPassList
                        .getString("DateOfBirth"));
                flightPaxItem[j].setMobileNumber(jPassList
                        .getString("MobileNumber"));
                flightPaxItem[j].setMobileCode(jPassList
                        .getString("MobileCode"));
                flightPaxItem[j].setPassportNumber(jPassList
                        .getString("PassportNumber"));
                flightPaxItem[j].setPassportExpiryDate(jPassList
                        .getString("PassportExpiryDate"));
                flightPaxItem[j].setPassportPlaceOfIssue(jPassList
                        .getString("PassportPlaceOfIssue"));
                flightPaxItem[j].setCitizenship(jPassList
                        .getString("Citizenship"));
                flightPaxItem[j].setTitle(jPassList.getString("Title"));
                flightPaxItem[j].setPassengerType(jPassList
                        .getString("PassengerType"));

                if (!jPassList.getString("PassengerType").contains("infant")) {
                    jArrayBoarding = jPassList.getJSONArray("BoardingDetails");

                    int i = 0, boardLen = jArrayBoarding.length();

                    BoardingDetails[] boardingDetails = new BoardingDetails[jArrayBoarding
                            .length()];

                    JSONObject jBoardObj;

                    for (i = 0; i < boardLen; ++i) {
                        jBoardObj = jArrayBoarding.getJSONObject(i);
                        boardingDetails[i] = flightPaxItem[j].new BoardingDetails();
                        boardingDetails[i].setAllowBaggages(jBoardObj
                                .getBoolean("AllowBaggages"));
                        if (jBoardObj.getBoolean("AllowBaggages")) {
                            JSONArray jList = jBoardObj
                                    .getJSONArray("BaggageList");
                            BaggageList[] baggageList = new BaggageList[jList
                                    .length()];
                            for (int len = 0; len < jList.length(); ++len) {
                                JSONObject jBagg = jList.getJSONObject(len);
                                baggageList[len] = flightPaxItem[j].new BaggageList();
                                baggageList[len]
                                        .setId(jBagg.getString("Value"));
                                baggageList[len].setValue(jBagg
                                        .getString("Text"));
                            }
                            boardingDetails[i].setBaggageList(baggageList);
                        }

                        boardingDetails[i].setAllowCheckIn(jBoardObj
                                .getBoolean("AllowCheckIn"));
                        // if (jBoardObj.getBoolean("AllowCheckIn")) {
                        // JSONArray jList = jBoardObj
                        // .getJSONArray("CheckInList");
                        // CheckInList[] checkInList = new CheckInList[jList
                        // .length()];
                        // for (int len = 0; len < jList.length(); ++len) {
                        // JSONObject jcheckIn = jList.getJSONObject(len);
                        // checkInList[len] = flightPaxItem[j].new
                        // CheckInList();
                        // }
                        // boardingDetails[i].setCheckInList(checkInList);
                        // }
                    }

                    flightPaxItem[j].setBoradingDetails(boardingDetails);
                }

            }
        }
        return flightPaxItem;
    }

    public String createPaxDetailsString(
            FlightPaxSubmissionModel[] flightPaxItem) throws JSONException {
        // TODO create params for API from pax objects

        JSONArray PassengerInfo = new JSONArray();
        JSONObject obj = new JSONObject();
        String day, month, year;

        int count = 0, length = flightPaxItem.length;
        for (count = 0; count < length; ++count) {
            obj.put("ContactDetail", null);
            obj.put("CompanyGenQuoteDetails", "");
            obj.put("TnCChecked", false);
            obj.put("TotalAmountForDisplay", 0);
            obj.put("TripId", flightPaxItem[count].getTripId());
            obj.put("IsLoggedIn", flightPaxItem[count].isIsLoggedIn());
            obj.put("CancellationPolicy", "");
            obj.put("IsPassportOptional", true);
            obj.put("IsEmailOptional", false);
            obj.put("IsFlightHotel", false);
            obj.put("ApiId", 0);
            obj.put("IsRoundTrip", false);
            obj.put("LeadPassengerchangeable", false);
            obj.put("IscontactInfofromPaymentPage", false);
            obj.put("RoomTypeDetails", null);
            obj.put("Rooms", null);
            JSONObject json = new JSONObject();

            json.put("PassengerId",
                    String.valueOf(flightPaxItem[count].getPassengerId()));
            json.put("Email", flightPaxItem[count].getEmail());
            json.put("FirstName", flightPaxItem[count].getFirstName());
            json.put("LastName", flightPaxItem[count].getLastName());
            json.put("MiddleName", "");
            json.put("Age", 0);
            json.put("Gender", flightPaxItem[count].getGender());
            json.put("MobileNumber", flightPaxItem[count].getMobileNumber());
            json.put("PassengerType", flightPaxItem[count].getPassengerType());
            json.put("FrequentFlyerNo",
                    flightPaxItem[count].getFrequentFlyerNo());
            json.put("travelDate", "2016-08-23T00:00:00");
            json.put("PassportNumber", flightPaxItem[count].getPassportNumber());
            json.put("IsCivilId", false);
            json.put("DateOfBirth", flightPaxItem[count].getDateOfBirth());
            json.put("PassportExpiryDate",
                    flightPaxItem[count].getPassportExpiryDate());
            json.put("PassportPlaceOfIssue",
                    flightPaxItem[count].getPassportPlaceOfIssue());
            json.put("PassportIssueCountryCode", null);
            json.put("Citizenship", flightPaxItem[count].getCitizenship());
            json.put("preferenceDetail", null);
            json.put("IsLeadPassenger", false);
            json.put("IsPassenger", false);
            json.put("HasVisa", false);
            json.put("VisaNumber", null);
            json.put("VisaCity", null);
            json.put("VisaIssuedCountry", null);
            json.put("VisaIssuedDate", "0001-01-01T00:00:00");
            json.put("VisaValidCountry", null);
            json.put("MobileCode", flightPaxItem[count].getMobileCode());
            json.put("Password", null);
            json.put("ConfirmPassword", null);
            json.put("Tittle", flightPaxItem[count].getTitle());

            String[] items1 = flightPaxItem[count].getDateOfBirth().split("/");

            day = items1[0];
            month = items1[1];
            year = items1[2];

            json.put("DOBDate", day);
            json.put("DOBMonth", month);
            json.put("DOBYear", year);

            items1 = flightPaxItem[count].getPassportExpiryDate().split("/");

            day = items1[0];
            month = items1[1];
            year = items1[2];

            json.put("PassportDay", day);
            json.put("PassportMonth", month);
            json.put("PassportYear", year);
            json.put("addtraveller", false);
            json.put("TravellerId", 0);
            json.put("SpecialRequests", null);

            if (flightPaxItem[count].getBaggageList() != null) {
                JSONArray BoardingTripList = new JSONArray();
                for (int cnt = 0; cnt < flightPaxItem[count].getBaggageList().length; ++cnt) {
                    if (flightPaxItem[count].getBaggageList()[cnt] == null)
                        break;

                    JSONObject newobj = new JSONObject();

                    newobj.put("AllowBaggages", false);
                    newobj.put("AllowPriorityBoarding", false);
                    newobj.put("AllowCheckIn", false);
                    newobj.put("AllowHandBaggage", false);
                    newobj.put("BaggageList", null);
                    newobj.put("CheckInList", null);
                    newobj.put("BoardingText", null);
                    newobj.put("HandBagText", null);
                    newobj.put("BaggageId",
                            flightPaxItem[count].getBaggageList()[cnt]);
                    newobj.put("CheckInId", null);
                    newobj.put("IsPriorityBoarding", false);
                    newobj.put("IsHandBaggage", false);
                    newobj.put("SameCheckinForAllPassenger", false);
                    BoardingTripList.put(newobj);
                }
                json.put("BoardingTripList", BoardingTripList);

            } else {
                json.put("BoardingTripList", null);
            }
            JSONArray years = new JSONArray();

            json.put("Years", years);
            json.put("Dates", years);
            json.put("Months", years);
            json.put("DOBYears", years);
            PassengerInfo.put(json);
            obj.put("PassengerInfo", PassengerInfo);
        }

        return obj.toString();

    }

    public PaymentModel getFlightPaymentDetails(String strJson)
            throws JSONException {
        // TODO parse flight payment details TODO Payment model

        PaymentModel flightPaymentItem = new PaymentModel();
        JSONObject jObj, jsnObj;
        jObj = new JSONObject(strJson);
        flightPaymentItem.setResponseType(jObj.getString("ResponseType"));
        flightPaymentItem.setDeeplinkUrl(jObj.getString("DeeplinkUrl"));
        flightPaymentItem.setConfirmationmessage(jObj
                .getString("Confirmationmessage"));
        jsnObj = jObj.getJSONObject("Item");
        flightPaymentItem.setCurrency(jsnObj.getString("Currency"));
        flightPaymentItem.setTotalAmount(jsnObj.getDouble("TotalAmount"));
        flightPaymentItem.setIsMigsPaymentGatewayActive(jsnObj
                .getBoolean("IsMigsPaymentGatewayActive"));
        flightPaymentItem
                .setBoardingFares(jsnObj.getDouble("TotalBoardingFee"));
        // JSONArray jArray = jsnObj.getJSONArray("faresummary");
        // JSONObject jObjtemp = jArray.getJSONObject(0);
        // jObjtemp = jObjtemp.getJSONObject("BoardingFares");
        // if (jObjtemp.length() > 0) {
        // flightPaymentItem.setBoardingFares(jObjtemp.getDouble("BaggageFee")
        // + jObjtemp.getDouble("HandBagFee")
        // + jObjtemp.getDouble("CheckInFee")
        // + jObjtemp.getDouble("BoardingFee")
        // + jObjtemp.getDouble("SmsNotifyFee")
        // + jObjtemp.getDouble("SupplierPaymentFee"));
        // }

        JSONArray jArray = jsnObj.getJSONArray("AvailablePaymentGateways");
        JSONObject jObjtemp;
        int length = jArray.length(), i = 0;
        AvailablePaymentGateways[] availablePaymentGateways = new AvailablePaymentGateways[length];
        for (i = 0; i < jArray.length(); ++i) {
            jObjtemp = jArray.getJSONObject(i);
            availablePaymentGateways[i] = flightPaymentItem.new AvailablePaymentGateways();
            availablePaymentGateways[i].setPaymentGateWayId(jObjtemp
                    .getInt("PaymentGateWayId"));
            availablePaymentGateways[i].setServiceCharge(jObjtemp
                    .getDouble("ServiceCharge"));
            availablePaymentGateways[i].setIsPercentage(jObjtemp
                    .getBoolean("IsPercentage"));
        }

        flightPaymentItem.setConvertionRate(jsnObj.getDouble("ConversionRate"));
        flightPaymentItem.setAvailablePaymentGateways(availablePaymentGateways);

        if (jsnObj.getBoolean("IsHandTwoHandActive")) {
            flightPaymentItem.setIsHandTwoHandActive(true);
            final JSONObject jObjHand = jsnObj
                    .getJSONObject("ObjHandTwoHand");
            flightPaymentItem.setHandTwoHandCharge(jObjHand
                    .getDouble("HandTwoHandCharge"));
        }

        return flightPaymentItem;
    }

}
