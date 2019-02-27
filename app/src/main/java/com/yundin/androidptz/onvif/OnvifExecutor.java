package com.yundin.androidptz.onvif;

import com.yundin.androidptz.onvif.request.GetCapabilitiesRequest;
import com.yundin.androidptz.onvif.request.GetProfilesRequest;
import com.yundin.androidptz.onvif.request.OnvifRequest;
import com.yundin.androidptz.onvif.request.RequestCallback;
import com.yundin.androidptz.onvif.request.RequestType;

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
            "xmlns:media=\"http://www.onvif.org/ver10/media/wsdl\"" +
            "xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" >";

    private final static String SOAP_BODY_BEGIN = "<soap:Body>";

    private final static String SOAP_END = "</soap:Body></soap:Envelope>";

    public static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(10000, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build();
    private static MediaType reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");
    public static RequestCallback requestCallback;

    public static void getDeviceCapabilities(OnvifDevice device) {
        sendRequest(device, new GetCapabilitiesRequest());
    }

    public static void getProfiles(OnvifDevice device) {
        sendRequest(device, new GetProfilesRequest());
    }

    public static void sendRequest(OnvifDevice device, OnvifRequest request) {
        RequestBody requestBody = RequestBody.create(reqBodyType, SOAP_BEGIN + device.authorizationHeader + SOAP_BODY_BEGIN + request.getXml() + SOAP_END);

        Request httpRequest = new Request.Builder()
                .url(getUrl(device, request))
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .post(requestBody)
                .build();

        client.newCall(httpRequest)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        if (requestCallback != null) {
                            requestCallback.onFailure(request, e);
                        }
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) {
                        if (request instanceof GetCapabilitiesRequest) {
                            deserializeCapabilities(device, response);
                        } else if (request instanceof GetProfilesRequest) {
                            deserializeProfiles(device, response);
                        }
                        if (requestCallback != null) {
                            requestCallback.onResponse(request, response);
                        }
                    }
                });
    }

    private static String getUrl(OnvifDevice device, OnvifRequest request) {
        return device.hostName + device.path.get(request.getType());
    }

    private static void deserializeCapabilities(OnvifDevice device, Response response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            assert response.body() != null;
            xpp.setInput(new StringReader(response.body().string()));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("ptz")) {
                    xpp.next();
                    xpp.next();

                    String text = xpp.getText();
                    device.path.put(RequestType.PTZ, text.replace(device.hostName, ""));
                    break;
                } else if (eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("media")) {
                    xpp.next();
                    xpp.next();

                    String text = xpp.getText();
                    device.path.put(RequestType.MEDIA, text.replace(device.hostName, ""));
                }
                eventType = xpp.next();
            }
        } catch (Exception e) { /**/ }
    }

    private static void deserializeProfiles(OnvifDevice device, Response response) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            assert response.body() != null;
            xpp.setInput(new StringReader(response.body().string()));
            int eventType = xpp.getEventType();

            device.profiles.clear();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && xpp.getName().equalsIgnoreCase("profiles")) {
                    String token = xpp.getAttributeValue(null, "token");
                    device.profiles.add(token);
                }
                eventType = xpp.next();
            }
        } catch (Exception e) { /**/ }
    }
}
