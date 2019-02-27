package com.yundin.androidptz.onvif.request;

public class GetProfilesRequest implements OnvifRequest {

    @Override
    public String getXml() {
        return "<media:GetProfiles/>";
    }

    @Override
    public RequestType getType() {
        return RequestType.MEDIA;
    }
}
