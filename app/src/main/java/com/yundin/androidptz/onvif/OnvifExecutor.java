package com.yundin.androidptz.onvif;

import com.yundin.androidptz.onvif.request.GetCapabilitiesRequest;
import com.yundin.androidptz.onvif.request.OnvifRequest;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
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
            "xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\"" +
            "xmlns:tt=\"http://www.onvif.org/ver10/schema\"" +
            "xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\"" +
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

    public static void getDeviceCapabilities(OnvifDevice device) {
        sendRequest(device, new GetCapabilitiesRequest());
    }

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
                        if (request instanceof GetCapabilitiesRequest) {
                            deserializeCapabilities(device, response);
                        }
                        String body = response.body().string();
                        int i = 42;
                    }
                });
    }

    private static String getUrlForRequest(OnvifDevice device, OnvifRequest request) {
        return device.hostName + device.PTZAddress;
    }

    private static void deserializeCapabilities(OnvifDevice device, Response response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(response.body().string()));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("ptz")) {
                    xpp.next();
                    xpp.next();

                    String text = xpp.getText();
                    device.PTZAddress = text.replace(device.hostName, "");
                    break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {}
    }
}
