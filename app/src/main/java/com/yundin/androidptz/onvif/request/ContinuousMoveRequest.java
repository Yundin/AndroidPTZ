package com.yundin.androidptz.onvif.request;


public final class ContinuousMoveRequest implements OnvifRequest {

    private float x, y, z;
    private String profileToken;

    public ContinuousMoveRequest(float x, float y, float z, String profileToken) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.profileToken = profileToken;
    }

    @Override
    public String getXml() {
        return "<tptz:ContinuousMove>\n" +
                "      <tptz:ProfileToken>" + profileToken + "</tptz:ProfileToken>\n" +
                "      <tptz:Velocity>\n" +
                "        <tt:PanTilt x=\"" + x + "\" y=\"" + y + "\"/>\n" +
                "        <tt:Zoom x=\"" + z + "\"/>\n" +
                "      </tptz:Velocity>\n" +
                "</tptz:ContinuousMove>";
    }
}