package com.curbmap.android;

import com.curbmap.android.models.db.Restriction;

import org.junit.Test;

import static com.curbmap.android.controller.addRestriction.CheckExceptions.checkWarning;
import static org.junit.Assert.assertNotEquals;

public class CheckWarningTest {
    static final String TAG = "CheckWarningTest";
    static final int OKAY_RESTRICTIONS = -1;

    //in this test, the restriction is null
    @Test
    public void checkWarningTestOne() throws Exception {
        String parkingMeter = "";
        String timeLimitParking = "";
        String permitParkingDistricts = "";
        String withinHoursOf = "";
        int selectedTypeId = 0;
        int selectedLengthId = 0;
        int selectedAngleId = 0;
        String length = "";
        Restriction restriction = null;

        int result = checkWarning(
                parkingMeter,
                timeLimitParking,
                permitParkingDistricts,
                withinHoursOf,
                selectedTypeId,
                selectedLengthId,
                selectedAngleId,
                length,
                restriction
        );
        assertNotEquals(result, OKAY_RESTRICTIONS);
    }


    //we have a restriction, but all the values are empty
    @Test
    public void checkWarningTestTwo() throws Exception {
        String parkingMeter = "";
        String timeLimitParking = "";
        String permitParkingDistricts = "";
        String withinHoursOf = "";
        int selectedTypeId = 0;
        int selectedLengthId = 0;
        int selectedAngleId = 0;
        String length = "";
        Restriction restriction = new Restriction(
                "",
                "",
                "",
                "",
                "",
                0,
                0,
                0,
                0,
                ""
        );

        int result = checkWarning(
                parkingMeter,
                timeLimitParking,
                permitParkingDistricts,
                withinHoursOf,
                selectedTypeId,
                selectedLengthId,
                selectedAngleId,
                length,
                restriction
        );
        assertNotEquals(result, OKAY_RESTRICTIONS);
    }

}