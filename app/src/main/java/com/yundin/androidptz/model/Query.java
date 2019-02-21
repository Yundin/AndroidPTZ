package com.yundin.androidptz.model;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class Query extends AsyncTask<Void, Void, String> {

    private String header =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap:Envelope \n" +
            " xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
            " xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\"\n" +
            " xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
            " <soap:Body>";

    private String footer =
            "</soap:Body>\n" +
            "</soap:Envelope>";

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .build();

    private MediaType reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");

    String xmlBody;

    @Override
    protected String doInBackground(Void... attrs) {
        RequestBody reqBody = RequestBody.create(reqBodyType, header + xmlBody + footer);

        Request request;
        request = new Request.Builder()
                .url("http://192.168.15.43:80/onvif/device_service")
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .addHeader("Authorization", "Digest username=\"student\", realm=\"onvif\", nonce=\"5c6c3321ca72737b8ddc\", uri=\"/onvif/device_service\", response=\"b65dce855d144627e01d487c2e1ec61c\", cnonce=\"a1b390a149f9085d64598b75f3a9e0f1\", nc=00000001, qop=\"auth,auth-int\"")
                .post(reqBody)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.toString();
        } else {
            return null;
        }
    }
}
