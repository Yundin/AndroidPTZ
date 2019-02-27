package com.yundin.androidptz.onvif.request;

public class GetCapabilitiesRequest implements OnvifRequest {

    @Override
    public String getXml() {
        return "<tds:GetCapabilities>\n" +
               "      <tds:Category>All</tds:Category>\n" +
               "</tds:GetCapabilities>";
    }
}
