package com.strategy.intecom.vtc.vtctracking.connection;

import android.content.Context;

import com.strategy.intecom.vtc.vtctracking.BuildConfig;
import com.strategy.intecom.vtc.vtctracking.common.Common;
import com.strategy.intecom.vtc.vtctracking.enums.TypeErrorConnection;
import com.strategy.intecom.vtc.vtctracking.interfaces.RequestListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Thuy Chi on 7/7/16.
 */
public class VtcHttpConnection {

    private static String URL_CONNECT_SERVER = "";

    private static final String URL_CONNECT_SERVER_DEBUG = "http://117.103.207.109:8080";

    private static final String URL_CONNECT_SERVER_RELEASE = "http://117.103.207.109:8080";

    private final int CONNECT_TIME_OUT = 1000 * 30;

    private Context context;
    private RequestListener requestConnection;

    private TypeErrorConnection errorConnection = TypeErrorConnection.TYPE_CONNECTION;

    // start set Queue
    private BlockingQueue<String> pollQueue = new LinkedBlockingQueue<>();
    private Map<String, VtcModelConnect> mapQueue = new HashMap<>();

    private static Socket socket;

    protected VtcModelConnect getVtcModelConnect(String sAPI) {

        if (mapQueue != null) {
            return mapQueue.get(sAPI);
        }
        return null;
    }

    protected String getApiQueue() {

        if (pollQueue != null) {
            return pollQueue.poll();
        }
        return "";
    }

    protected int getApiQueueSize() {

        if (pollQueue != null) {
            return pollQueue.size();
        }
        return 0;
    }

    protected void setPoolQueue(String sAPI, VtcModelConnect vtcModelConnect) {

        if (pollQueue != null) {
            pollQueue.add(sAPI);
        }

        if (mapQueue != null) {
            mapQueue.put(sAPI, vtcModelConnect);
        }
    }

    // End set Queue
    protected Context getContext() {
        return context;
    }

    protected RequestListener getRequestConnection() {
        return requestConnection;
    }

    protected TypeErrorConnection getErrorConnection() {
        return errorConnection;
    }

    protected void setErrorConnection(TypeErrorConnection errorConnection) {
        this.errorConnection = errorConnection;
    }

    protected VtcHttpConnection(Context context, RequestListener requestConnection) {
        this.context = context;
        this.requestConnection = requestConnection;

        if (BuildConfig.DEBUG) {
            VtcHttpConnection.URL_CONNECT_SERVER = VtcHttpConnection.URL_CONNECT_SERVER_DEBUG;
        } else {
            VtcHttpConnection.URL_CONNECT_SERVER = VtcHttpConnection.URL_CONNECT_SERVER_RELEASE;
        }
    }

    protected String initRequestConnection(String sApi, String urlParameters, boolean post_get, String authorization) {
        try {
            Common.showLog("sApi:" + "----" + sApi);
            Common.showLog("Authorization000" + "---" + authorization);
            Common.showLog("URL" + "---" + VtcHttpConnection.URL_CONNECT_SERVER + sApi);
            URL url = new URL(VtcHttpConnection.URL_CONNECT_SERVER + sApi);
            Common.showLog("Authorization111" + "---" + authorization);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            Common.showLog("Request:" + "--------GET_POST");
            if (post_get) {
                Common.showLog("Request:" + "--------POST");
                /*connection.setRequestMethod(String.valueOf("POST"));
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();*/
                connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
                connection.setRequestMethod(String.valueOf("POST"));
                connection.setDoOutput(true);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                bw.write(urlParameters);
                bw.flush();
                bw.close();
                /*byte[] postData = urlParameters.getBytes();
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);
                OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                outputStream.write(postData);*/

            } else {
                connection.setRequestMethod(String.valueOf("GET"));
                Common.showLog("Request:" + "--------GET");
            }

            int responseCode = connection.getResponseCode();

            Common.showLog("responseCode----- : " + responseCode + " ------ : " + connection.getRequestMethod());

            switch (responseCode) {

                case HttpURLConnection.HTTP_OK:

                    final StringBuilder output = new StringBuilder("Request URL " + url);
                    output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                    output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                    output.append(System.getProperty("line.separator") + "Type " + connection.getRequestMethod());
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();
                    //Common.showLog("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();

                    output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                    //Common.showLog("------ : " + output.toString());

                    connection.disconnect();
                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION); // fine, go on
                    return responseOutput.toString();
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:

                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_TIMEOUT); // retry

                    return "";
                case HttpURLConnection.HTTP_UNAVAILABLE:

                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_NOT_CONNECT_SERVER); // retry, server is unstable

                    return "";
                default:

                    setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR); // abort

                    return "";
            }
        } catch (ProtocolException e) {
            setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
        } catch (IOException e) {
            setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
        } catch (Exception e) {
            setErrorConnection(TypeErrorConnection.TYPE_CONNECTION_ERROR);
        }
        return "";
    }

    public String readUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {

        } finally {
            if (iStream != null) {
                iStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }

    public static byte[] urlParameterJson(Map<?, ?> map) {
        JSONObject jsonObject = new JSONObject();

        try {

            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    public static String urlEncodeUTF8(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        try {
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry != null) {
                        if (sb.length() > 0) {
                            sb.append("&");
                        } else {
                            sb.append("?");
                        }

                        String value = "";

                        String key = "";

                        if (entry.getValue() != null) {
                            value = String.valueOf(entry.getValue());
                        }
                        if (entry.getKey() != null) {
                            key = String.valueOf(entry.getKey());
                        }

                        String encodeValue = "";
                        String encodeKey = "";

                        if (null != value || !value.isEmpty()) {
                            encodeValue = urlEncodeUTF8(value);
                        }
                        if (null != key || !key.isEmpty()) {
                            encodeKey = urlEncodeUTF8(key);
                        }

                        sb.append(String.format("%s=%s", encodeKey, encodeValue));
                    }
                }
            }
        } catch (NumberFormatException e) {

        }
        return sb.toString();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            if (s != null && !s.isEmpty()) {
                s = URLEncoder.encode(s, "UTF-8");
            } else {
                s = "";
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
