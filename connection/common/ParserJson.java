package com.strategy.intecom.vtc.vtctracking.common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr. Sup on 6/20/16.
 */
public class ParserJson {

    public static final String API_PARAMETER_SUCCESS = "error";
    public static final String API_PARAMETER_RESSPONSE_DATA = "responseData";
    public static final String API_PARAMETER_MESSAGE = "message";
    public static final String API_PARAMETER_ERROR_CODE = "error";
    public static final String API_PARAMETER_INFO = "info";

    public static final String API_PARAMETER_AUTH_TOKEN = "auth_token";



    public static boolean getStatusSuccess(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            if (jStatus.optInt(API_PARAMETER_SUCCESS) == 200) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }
    }

    public static String getStatusMsg(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optString(API_PARAMETER_MESSAGE);
        } catch (JSONException e) {
            return "";
        }
    }
    public static String getStatusErrorCode(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optString(API_PARAMETER_ERROR_CODE);
        } catch (JSONException e) {
            return "";
        }
    }

    public static String getResponse(String response) {

        try {
            JSONObject jStatus = new JSONObject(response);
            return jStatus.optString(API_PARAMETER_INFO);
        } catch (JSONException e) {
            return "";
        }
    }

    public static String getAuthToken(String s) {

        try {
            JSONObject jStatus = new JSONObject(s);
            return jStatus.optString(API_PARAMETER_AUTH_TOKEN);
        } catch (JSONException e) {
            return "";
        }
    }
}
