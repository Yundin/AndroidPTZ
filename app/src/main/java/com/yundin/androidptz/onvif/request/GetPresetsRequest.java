package com.yundin.androidptz.onvif.request;

public class GetPresetsRequest implements OnvifRequest {

    private String profileToken;

    public GetPresetsRequest(String profileToken) {
        this.profileToken = profileToken;
    }

    @Override
    public String getXml() {
        return "<tptz:GetPresets>\n" +
                "      <tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>\n" +
                "</tptz:GetPresets>";
    }

    @Override
    public RequestType getType() {
        return RequestType.PTZ;
    }
}

