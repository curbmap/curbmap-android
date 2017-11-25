package com.curbmap.android;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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

    public String coordinates;

    public String type;

    public String days;

    public int start_time;

    public int end_time;

    public int angle;

    public int time_limit;

    public double cost;

    public int per;

    public Restriction(String coordinates,
                       String type,
                       String days,
                       int start_time,
                       int end_time,
                       int angle,
                       int time_limit,
                       double cost,
                       int per) {
        this.coordinates = coordinates;
        this.type = type;
        this.id = (int) System.currentTimeMillis();
        this.days = days;
        this.start_time = start_time;
        this.end_time = end_time;
        this.angle = angle;
        this.time_limit = time_limit;
        this.cost = cost;
        this.per = per;
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

    public String getCard() {
        String result = "";
        result += "Coordinates: " + coordinates + "\n";
        result += "Type: " + type + "\n";
        result += "Days: " + this.getDays() + "\n";
        result += "Time: " + Integer.toString(start_time);
        result += " to " + Integer.toString(end_time) + "\n";
        result += "Angle: " + Integer.toString(angle) + "\n";
        result += "Time Limit: " + Integer.toString(time_limit) + "minutes" + "\n";
        result += "Cost: $" + Double.toString(cost);
        result += "/" + Integer.toString(per) + " minutes" + "\n";

        return result;
    }
}