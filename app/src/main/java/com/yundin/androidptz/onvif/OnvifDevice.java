package com.yundin.androidptz.onvif;

import android.util.Base64;

import com.google.common.hash.Hashing;
import com.yundin.androidptz.onvif.request.RequestType;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class OnvifDevice {

    public String hostName;
    public HashMap<RequestType, String> path = new HashMap<>();
    public String authorizationHeader;
    public List<String> profiles = new ArrayList<>();

    public OnvifDevice(String hostName, String login, String password) {
        this.hostName = hostName;
        authorizationHeader = getAuthorizationHeader(login, password);
        for (RequestType type: RequestType.values()) {
            path.put(type, "/onvif/device_service");
        }
    }

    private String getAuthorizationHeader(String login, String password) {
        String charPool = "0123456789abcdef";
        StringBuilder nonceSB = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            nonceSB.append(charPool.charAt(random.nextInt(charPool.length())));
        }
        String slashNNonce = Base64.encodeToString(nonceSB.toString().getBytes(), Base64.DEFAULT);
        String nonce = slashNNonce.substring(0, slashNNonce.length() - 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        String date = dateFormat.format(new Date());

        String decodedNonce = new String(Base64.decode(nonce.getBytes(), Base64.DEFAULT));

        String preSHADigest = decodedNonce + date + password;
        byte[] SHADigest = Hashing.sha1().hashString(preSHADigest, Charset.defaultCharset()).asBytes();
        byte[] slashNDigest = Base64.encode(SHADigest, Base64.DEFAULT);
        String slashNDigestStr = new String(slashNDigest);
        String finalDigest = slashNDigestStr.substring(0, slashNDigestStr.length() - 1);
        return
                "<soap:Header>\n" +
                "   <Security soap:mustUnderstand=\"1\" xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
                "       <UsernameToken>\n" +
                "           <Username>" + login + "</Username>\n" +
                "           <Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + finalDigest + "</Password>\n" +
                "           <Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">" + nonce + "</Nonce>\n" +
                "           <Created xmlns=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" + date + "</Created>\n" +
                "       </UsernameToken>\n" +
                "   </Security>\n" +
                "</soap:Header>";
    }
}
