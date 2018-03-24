/*
 * Copyright (c) 2018 curbmap.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.curbmap.android.models.db;

/**
 * Houses the information about a restriction
 * Based on the curbmap Open Street Parking Restriction Specification
 * Which was released under the MIT License by curbmap
 * https://github.com/curbmap/Open-Street-Parking-Restriction-Specification/blob/master/
 * SPECIFICATIONS/Open_Street_Parking_Restriction_Specification.md
 */
public class RestrictionTextInfo {
    public int type;
    public int angle;
    public int start;
    public int end;
    public boolean[] days = new boolean[7];
    public boolean[] weeks = new boolean[4];
    public boolean[] months = new boolean[12];
    public int duration;
    public String permit;
    public float cost;
    public int per;
    public int vehicle;
    public int side;
    public boolean holiday;

    /**
     * Warning: "limit" on API docs must actually be "duration" as is used here.
     *
     * @param type     0	 Short time limit (AKA green, <1hr)
     *                 1	 Short time limit metered (AKA green with meter, <1hr)
     *                 2	 Time limit (>=1hr )
     *                 3	 Time limit metered (>=1hr)
     *                 4	 Time limit with permit exemption
     *                 5	 Time limit metered with permit exemption
     *                 6	 No parking
     *                 7	 No parking with permit exemption
     *                 8	 No stopping
     *                 9	 Street cleaning
     *                 10 Disabled parking
     *                 11 Passenger loading (AKA white)
     *                 12 Commercial loading (AKA yellow)
     *                 13 Other
     * @param angle    The orientation at which a driver may park
     *                 0 parallel
     *                 1 perpendicular (head in)
     *                 2 perpendicular (no restriction)
     *                 3 acute (45 degree)
     * @param start    local time of rule start as number of minutes
     *                 from midnight
     * @param end      local time of rule end as number of minutes
     *                 from midnight
     * @param days     days rule is active
     *                 first integer is Monday (starting on left)
     *                 0 not active
     *                 1 active
     * @param weeks    weeks rule is active
     *                 first integer is first week (starting on left)
     *                 0 not active
     *                 1 active
     * @param months   months rule is active
     *                 first integer is first week (starting on left)
     *                 0 not active
     *                 1 active
     * @param duration    time limit of parking in minutes if applicable
     * @param permit   write String describing the permit, for example:
     *                 Commercial
     *                 Taxi
     *                 Disabled
     *                 Other
     *                 Permit name for restriction
     * @param cost     the price of parking at the spot associated with the number of minutes
     *                 specified in per
     * @param per      the number of minutes associated with the cost specified in cost
     * @param vehicle  The type of vehicle that the restriction applies to
     *                 Warning: index starts at -1
     *                 -1 All vehicle types
     *                 0  Car, Truck (2 axel), Non-inhabiting
     *                 1	 Motorcycle
     *                 2	 Truck, Bus (3+ axel, including trailer), Non-inhabiting
     *                 3	 Vehicle for inhabitation (Camper, Car, Truck, etc.)
     * @param side     The compass direction that the car will face when parked at the curb,
     *                 measured relative to true north
     *                 0 north
     *                 1 south
     *                 2 east
     *                 3 west
     *                 4 northeast
     *                 5 northwest
     *                 6 southeast
     *                 7 southwest
     * @param holiday  true if its a holiday false otherwise
     * @param duration the number of minutes that the restriction goes on for
     */
    public RestrictionTextInfo(
            int type, int angle, int start, int end,
            boolean[] days, boolean[] weeks, boolean[] months,
            int duration, String permit, float cost, int per,
            int vehicle, int side, boolean holiday) {
        this.type = type;
        this.angle = angle;
        this.start = start;
        this.end = end;
        this.days = days;
        this.weeks = weeks;
        this.months = months;
        this.duration = duration;
        this.permit = permit;
        this.cost = cost;
        this.per = per;
        this.vehicle = vehicle;
        this.side = side;
        this.holiday = holiday;
    }

}
