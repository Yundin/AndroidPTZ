package com.yundin.androidptz.onvif.request;

public class GotoPresetRequest implements OnvifRequest {

    private String profileToken;
    private String presetToken;

    public GotoPresetRequest(String profileToken, String presetToken) {
        this.profileToken = profileToken;
        this.presetToken = presetToken;
    }

    @Override
    public String getXml() {
        return "<tptz:GotoPreset>\n" +
                "      <tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>\n" +
                "      <tptz:PresetToken>" + presetToken + "</tptz:PresetToken>\n" +
                "</tptz:GotoPreset>";
    }

    @Override
    public RequestType getType() {
        return RequestType.PTZ;
    }
}
