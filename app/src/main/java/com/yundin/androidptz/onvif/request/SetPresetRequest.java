package com.yundin.androidptz.onvif.request;

public class SetPresetRequest implements OnvifRequest {

    private String profileToken;
    private String presetName;

    public SetPresetRequest(String profileToken, String presetName) {
        this.profileToken = profileToken;
        this.presetName = presetName;
    }

    @Override
    public String getXml() {
        return "<tptz:SetPreset>\n" +
                "      <tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>\n" +
                "      <tptz:PresetName>" + presetName + "</tptz:PresetName>\n" +
                "</tptz:SetPreset>";
    }

    @Override
    public RequestType getType() {
        return RequestType.PTZ;
    }
}
