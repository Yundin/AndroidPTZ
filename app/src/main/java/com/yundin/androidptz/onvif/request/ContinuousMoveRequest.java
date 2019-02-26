package com.yundin.androidptz.onvif.request;


import be.teletask.onvif.models.OnvifType;

public final class ContinuousMoveRequest implements OnvifRequest {

    private float x, y, z;

    public ContinuousMoveRequest(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String getXml() {
        return "<tptz:ContinuousMove\n" +
                "    xmlns:tptz=\"http://www.onvif.org/ver20/ptz/wsdl\"\n" +
                "    xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "      <tptz:ProfileToken>protoken_1</tptz:ProfileToken>\n" +
                "      <tptz:Velocity>\n" +
                "        <tt:PanTilt x=\"" + x + "\" y=\"" + y + "\"/>\n" +
                "        <tt:Zoom x=\"" + z + "\"/>\n" +
                "      </tptz:Velocity>\n" +
                "    </tptz:ContinuousMove>";
    }

    @Override
    public OnvifType getType() {
        return OnvifType.CUSTOM;
    }
}