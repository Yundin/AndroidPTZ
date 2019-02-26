package com.yundin.androidptz.onvif;

import com.yundin.androidptz.onvif.request.OnvifRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OnvifExecutor {

    private final static String SOAP_BEGIN =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
            "<soap:Envelope " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
            "xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" >";

    private final static String SOAP_BODY_BEGIN = "<soap:Body>";

    private final static String SOAP_END = "</soap:Body></soap:Envelope>";

    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build();
    private static MediaType reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");

    public static void sendRequest(OnvifDevice device, OnvifRequest request) {
        RequestBody requestBody = RequestBody.create(reqBodyType, SOAP_BEGIN + device.authorizationHeader + SOAP_BODY_BEGIN + request.getXml() + SOAP_END);

        Request httpRequest = new Request.Builder()
                .url(getUrlForRequest(device, request))
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .post(requestBody)
                .build();

        client.newCall(httpRequest)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        int i = 42;
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String body = response.body().string();
                        int i = 42;
                    }
                });
    }

    private static String getUrlForRequest(OnvifDevice device, OnvifRequest request) {
        return device.hostName + "/onvif/devise_service";
    }
}
