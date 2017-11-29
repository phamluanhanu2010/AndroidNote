package com.strategy.intecom.vtc.vtctracking.connection;

import com.strategy.intecom.vtc.vtctracking.enums.TypeActionConnection;

import org.json.JSONObject;

/**
 * Created by Thuy Chi on 7/7/16.
 */
public class VtcModelConnect {
    private TypeActionConnection actionConnection;
    private String API;
    private boolean isPost;
    private boolean isShowProcess;
    private JSONObject parameter;
    private String authorization;

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }


    public TypeActionConnection getActionConnection() {
        return actionConnection;
    }

    public void setActionConnection(TypeActionConnection actionConnection) {
        this.actionConnection = actionConnection;
    }

    public String getAPI() {
        return API;
    }

    public void setAPI(String API) {
        this.API = API;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public boolean isShowProcess() {
        return isShowProcess;
    }

    public void setShowProcess(boolean showProcess) {
        isShowProcess = showProcess;
    }

    public JSONObject getParameter() {
        return parameter;
    }

    public void setParameter(JSONObject parameter) {
        this.parameter = parameter;
    }
}
