package com.curbmap.android;

import com.curbmap.android.models.db.Polyline;
import com.curbmap.android.models.db.Restriction;

import org.junit.Test;

import static com.curbmap.android.controller.addRestriction.CheckExceptions.checkWarning;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CheckWarningTest {
    static final String TAG = "CheckWarningTest";
    static final int OKAY_RESTRICTIONS = -1;

    //in this test, the restriction is null
    @Test
    public void checkWarningTestOne() throws Exception {
        boolean typeIsSelected = false;
        boolean lengthIsSelected = false;
        boolean angleIsSelected = false;
        String length = "";
        Restriction restriction = null;

        int result = checkWarning(
                typeIsSelected,
                lengthIsSelected,
                angleIsSelected,
                length,
                restriction
        );
        assertNotEquals(result, OKAY_RESTRICTIONS);
    }


    //we have a restriction, but all the values are empty
    @Test
    public void checkWarningTestTwo() throws Exception {
        boolean typeIsSelected = false;
        boolean lengthIsSelected = false;
        boolean angleIsSelected = false;
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
                typeIsSelected,
                lengthIsSelected,
                angleIsSelected,
                length,
                restriction
        );
        assertNotEquals(result, OKAY_RESTRICTIONS);
    }

    //the first example of actually having no warnings
    @Test
    public void checkWarningTestThree() throws Exception {
        boolean typeIsSelected = true;
        boolean lengthIsSelected = true;
        boolean angleIsSelected = true;
        String length = "All Day";

        Restriction restriction = new Restriction(
                new Polyline(),
                "Fire Hydrant",
                "1111111",
                "00:00",
                "24:00",
                0,
                20,
                83,
                3,
                ""
        );

        int result = checkWarning(
                typeIsSelected,
                lengthIsSelected,
                angleIsSelected,
                length,
                restriction
        );
        assertEquals(result, OKAY_RESTRICTIONS);
    }

    //the first example of realistic input
    @Test
    public void checkWarningTestFour() throws Exception {
        boolean typeIsSelected = true;
        boolean lengthIsSelected = true;
        boolean angleIsSelected = true;
        String length = "Within hours of:";
        Restriction restriction = new Restriction(
                new Polyline(),
                "Fire Hydrant",
                "1111111",
                "00:00",
                "24:00",
                0,
                20,
                83,
                3,
                ""
        );

        int result = checkWarning(
                typeIsSelected,
                lengthIsSelected,
                angleIsSelected,
                length,
                restriction
        );
        assertEquals(result, OKAY_RESTRICTIONS);
    }

}