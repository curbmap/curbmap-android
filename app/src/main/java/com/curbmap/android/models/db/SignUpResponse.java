package com.curbmap.android.models.db;

import com.google.gson.annotations.SerializedName;

public class SignUpResponse {


    @SerializedName("success")
    Integer success;

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
            case  -4:
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

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }



}
