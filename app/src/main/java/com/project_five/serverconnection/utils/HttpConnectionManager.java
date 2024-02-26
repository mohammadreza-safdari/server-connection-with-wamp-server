package com.project_five.serverconnection.utils;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionManager {
    private String uri;
    public HttpConnectionManager(String uri){
        this.uri = uri;
    }
    public InputStream httpConnect(){
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        URL url = null;
        String charset = "UTF-8";
        int response_code;
        String content = null;
        try {
            url = new URL(uri);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept-Charset", charset);
            httpURLConnection.connect();
            response_code = httpURLConnection.getResponseCode();
            Log.d("RESPONSE_CODE_", "response_code : \n" + response_code);
            if (response_code >= 100 && response_code <= 399){
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                return inputStream;
            } else {
                Log.d("_ERROR_", "ERROR CODE : " + response_code);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("_ERROR_", e.toString());
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return null;
    }
}
