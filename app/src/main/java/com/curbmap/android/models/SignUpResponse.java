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

package com.curbmap.android.models;

import com.google.gson.annotations.SerializedName;

public class SignUpResponse {

    @SerializedName("success")
    Integer success;

    /**
     * Hardcodes the meaning of the status message received from the server
     * as a response to sending a sign-up request to the server
     * This is dependent on the changes made on the server code at
     * https://github.com/curbmap/curbmapbackend-js/tree/master/routes
     *
     * @return meaning of response of server to our sign-up request
     */
    public String getStatusMessage() {
        if (success == null) {
            return "No response was received.";
        }

        switch (success) {
            case 1:
                return "Success! Please activate account by clicking " +
                        "the activation link sent to your email " +
                        "address.";
            case 0:
                return "Username, password and email were blank. " +
                        "Please enter username, password and email.";
            case -1:
                return "Username exists. " +
                        "Please choose a different username.";
            case -2:
                return "Email exists. " +
                        "Please choose a different email.";
            case -3:
                return "Password does not meet criteria. " +
                        "Length of password must be between " +
                        "9 and 40 inclusive." +
                        "Password must have at least one each of " +
                        "special, capital, lower and number " +
                        "characters.";
            case -4:
                return "Email does not meet criteria. " +
                        "Please fix your email address.";
            case -5:
                return "There was an error signing up. " +
                        "Please check your network connection or " +
                        "try again later.";
        }

        return "Response did not match any criteria.";

    }

    public boolean isSuccess() {
        return success == 1;
    }

}
