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

import java.util.ArrayList;

/**
 * Houses information about a restriction
 * along with its coordinates
 */
public class RestrictionText {
    public ArrayList<double[]> line;
    public RestrictionTextInfo restrictionTextInfo;

    /**
     * Creates a RestrictionText object
     *
     * @param line                The ArrayList of coordinates indicating the polyline along which
     *                            the restriction is effective
     * @param restrictionTextInfo The RestrictionTextInfo object containing information about
     *                            the restriction
     */
    public RestrictionText(ArrayList<double[]> line, RestrictionTextInfo restrictionTextInfo) {
        this.line = line;
        this.restrictionTextInfo = restrictionTextInfo;
    }

    public ArrayList<double[]> getLine() {
        return line;
    }

    public RestrictionTextInfo getRestrictionTextInfo() {
        return restrictionTextInfo;
    }

}
