package com.strategy.intecom.vtc.vtctracking.interfaces;


import com.strategy.intecom.vtc.vtctracking.enums.TypeActionConnection;

/**
 * Created by Thuy Chi on 7/7/16.
 */
public interface RequestListener {

//    void onPostExecuteStart(TypeActionConnection keyType, String sApi);

    void onPostExecuteError(TypeActionConnection keyType, String errorcode, String msg, String info, String reponseFullData);

    void onPostExecuteSuccess(TypeActionConnection keyType, String response, String reponseFullData);

    String API_CONNECTION_LOGIN = "/sdk/login";


}
