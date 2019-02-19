package com.yundin.androidptz;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Action().execute("up");
            }
        });

        findViewById(R.id.down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Action().execute("down");
            }
        });

        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Action().execute("left");
            }
        });

        findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Action().execute("right");
            }
        });
    }

    static class Action extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String req = strings[0];

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .build();

            MediaType reqBodyType = MediaType.parse("application/soap+xml; charset=utf-8;");

            float x = 0;
            float y = 0;

            switch (req) {
                case "up":
                    y = 1;
                    break;
                case "down":
                    y = -1;
                    break;
                case "left":
                    x = -1;
                    break;
                default:
                    x = 1;
                    break;
            }

            RequestBody reqBody = RequestBody.create(reqBodyType,
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"\n" +
                            " xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\"\n" +
                            " xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                            "  <soap:Body>\n" +
                            "    <tptz:ContinuousMove>\n" +
                            "      <tptz:ProfileToken>protoken_1</tptz:ProfileToken>\n" +
                            "      <tptz:Velocity>\n" +
                            "        <tt:PanTilt x=\"" + x + "\" y=\"" + y + "\"/>\n" +
                            "        <tt:Zoom x=\"-1\"/>\n" +
                            "      </tptz:Velocity>\n" +
                            "    </tptz:ContinuousMove>\n" +
                            "  </soap:Body>\n" +
                            "</soap:Envelope>");

            Request request = null;
            request = new Request.Builder()
                    .url("http://192.168.15.43:80/onvif/device_service")
                    .addHeader("Content-Type", "text/xml; charset=utf-8")
                    .addHeader("Authorization", "Digest username=\"student\", realm=\"onvif\", nonce=\"5c6c3321ca72737b8ddc\", uri=\"/onvif/device_service\", response=\"b65dce855d144627e01d487c2e1ec61c\", cnonce=\"a1b390a149f9085d64598b75f3a9e0f1\", nc=00000001, qop=\"auth,auth-int\"")
                    .post(reqBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                int i = 42;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
