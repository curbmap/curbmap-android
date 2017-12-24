package com.curbmap.android.models.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/*
id	A GUID for the restriction
type	"sweep"=street sweeping"red"=red zone, "hyd"=hydrant"ppd"=preferential parking district rule
days	An array of 7 boolean values: all days = [true, ..., true], monday = [false, true, false, ..., false]
start time	An integer count of minutes since midnight
end time	An integer count of minutes since midnight
angle	0 = Parallel, 45 = Acute Angled Parking, 90 = Head in parking
updated	UTC time in milliseconds from 1/1/1970
time limit	Optional so may be 0
cost(dollars)	Optional so may be 0.0
per(minutes)	Optional so may be 0
 */
@Entity
public class Restriction {
    @PrimaryKey
    public int id;

    public Polyline polyline;

    public String type;

    public String days;

    //todo: change start_time and end_time to be minutes from midnight
    //to interface with the web API
    public String start_time;

    public String end_time;

    public int angle;

    public int time_limit;

    public double cost;

    public int per;

    public String permitDistrict;

    /**
     * Create the Restriction object
     * The object containing information about a parking restriction at a polyline
     *
     * @param polyline       The line describing the location of the parking restriction.
     *                       This is a line with multiple straight segments.
     * @param type           The type of parking restriction.
     * @param days           The days that the parking restriction is in effect.
     * @param start_time     The time that the parking restriction starts.
     * @param end_time       The time that the parking restriction ends.
     * @param angle          The angle of parking.
     * @param time_limit     The time limit for the parking restriction, if any.
     * @param cost           The cost for the parking restriction, if any.
     * @param per            The duration used to count the cost of the parking restriction, if any.
     * @param permitDistrict The permit district for the parking restriction for
     *                       permit parking restrictions.
     */
    public Restriction(Polyline polyline,
                       String type,
                       String days,
                       String start_time,
                       String end_time,
                       int angle,
                       int time_limit,
                       double cost,
                       int per,
                       String permitDistrict) {
        this.polyline = polyline;
        this.type = type;
        this.id = (int) System.currentTimeMillis();
        this.days = days;
        this.start_time = start_time;
        this.end_time = end_time;
        this.angle = angle;
        this.time_limit = time_limit;
        this.cost = cost;
        this.per = per;
        this.permitDistrict = permitDistrict;
    }

    /**
     * Create the Restriction object
     * The object containing information about a parking restriction at a polyline
     *
     * @param polylineString The line describing the location of the parking restriction.
     *                       This is a line with multiple straight segments.
     * @param type           The type of parking restriction.
     * @param days           The days that the parking restriction is in effect.
     * @param start_time     The time that the parking restriction starts.
     * @param end_time       The time that the parking restriction ends.
     * @param angle          The angle of parking.
     * @param time_limit     The time limit for the parking restriction, if any.
     * @param cost           The cost for the parking restriction, if any.
     * @param per            The duration used to count the cost of the parking restriction, if any.
     * @param permitDistrict The permit district for the parking restriction for
     *                       permit parking restrictions.
     */
    public Restriction(String polylineString,
                       String type,
                       String days,
                       String start_time,
                       String end_time,
                       int angle,
                       int time_limit,
                       double cost,
                       int per,
                       String permitDistrict) {
        this.type = type;
        this.id = (int) System.currentTimeMillis();
        this.days = days;
        this.start_time = start_time;
        this.end_time = end_time;
        this.angle = angle;
        this.time_limit = time_limit;
        this.cost = cost;
        this.per = per;
        this.permitDistrict = permitDistrict;


        Gson gson = new Gson();
        Polyline polyline = gson.fromJson(polylineString, Polyline.class);
        this.polyline = polyline;


    }


    public String getDays() {
        int NUMBER_OF_DAYS = 7;
        List<String> listOfDays = new ArrayList<String>();

        if (days.charAt(0) == '1') {
            listOfDays.add("Sunday");
        }
        if (days.charAt(1) == '1') {
            listOfDays.add("Monday");
        }
        if (days.charAt(2) == '1') {
            listOfDays.add("Tuesday");
        }
        if (days.charAt(3) == '1') {
            listOfDays.add("Wednesday");
        }
        if (days.charAt(4) == '1') {
            listOfDays.add("Thursday");
        }
        if (days.charAt(5) == '1') {
            listOfDays.add("Friday");
        }
        if (days.charAt(6) == '1') {
            listOfDays.add("Saturday");
        }

        if (listOfDays.size() == 0) {
            return "No Days";
        } else if (listOfDays.size() == 1) {
            return listOfDays.get(0);
        } else {
            String stringOfDays = "";
            int nDay = 1;
            for (String day : listOfDays) {
                if (nDay < listOfDays.size()) {
                    stringOfDays += day + ", ";
                } else {
                    stringOfDays += "and " + day + ".";
                }

                nDay++;
            }

            return stringOfDays;
        }
    }

    public String getTimeLimit() {
        if (time_limit == 0) {
            return "";
        } else if (time_limit < 60) {
            return Integer.toString(time_limit) + " minutes";
        } else {
            return Double.toString((Math.floor(time_limit * 100 / 60) / 100)) + " hours";
        }
    }

    public String getTime() {
        if (start_time.equals("00:00") && end_time.equals("24:00")) {
            return "All Day";
        } else {
            return start_time + " to " + end_time;
        }
    }

    public String getAngle() {
        if (angle == 0) {
            return "angle_parallel";
        } else if (angle == 45) {
            return "angle_angled";
        } else if (angle == 90) {
            return "angle_angled";
        } else {
            return "invalid angle";
        }
    }

    public String getCoordinatesList() {
        String result = "";
        //we start the iteration with 1 instead of 0 because this is meant for human-friendly reading
        int nCoordinate = 1;
        for (LatLng coordinates : polyline.getPolyline()) {
            result += "Coordinate " + nCoordinate + ": ";
            result += "(" + coordinates.latitude + ", " + coordinates.longitude + ")\n";
            nCoordinate++;
        }
        return result;
    }

    public String getCard() {
        String result = "";
        result += getCoordinatesList();
        result += "Type: " + type + "\n";
        if (permitDistrict.length() > 0) {
            result += "District: " + permitDistrict + "\n";
        }
        result += "Days: " + this.getDays() + "\n";
        result += "Time: " + this.getTime() + "\n";
        result += "Angle: " + getAngle() + "\n";
        if (type.equals("Time Limit Parking")) {
            result += "Time Limit: " + this.getTimeLimit() + "\n";
        }
        if (type.equals("Parking Meter")) {
            result += "Cost: $" + Double.toString(cost);
            result += "/" + Integer.toString(per) + " minutes" + "\n";
        }

        return result;
    }
}