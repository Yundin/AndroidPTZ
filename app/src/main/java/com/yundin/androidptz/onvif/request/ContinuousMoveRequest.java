package com.yundin.androidptz.onvif.request;


import com.yundin.androidptz.onvif.OnvifDevice;

public final class ContinuousMoveRequest implements OnvifRequest {

    private float x, y, z;
    private String profileToken;

    public ContinuousMoveRequest(float x, float y, float z, OnvifDevice device) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.profileToken = device.profiles.get(0);
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

    @Override
    public RequestType getType() {
        return RequestType.PTZ;
    }
}