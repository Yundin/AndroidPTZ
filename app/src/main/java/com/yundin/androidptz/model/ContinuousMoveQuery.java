package com.yundin.androidptz.model;

public final class ContinuousMoveQuery extends Query {

    public ContinuousMoveQuery(float x, float y, float z) {
        xmlBody = createXmlBody(x, y, z);
    }

    private String createXmlBody (float x, float y, float z) {
        return "<tptz:ContinuousMove>\n" +
               " <tptz:ProfileToken>protoken_1</tptz:ProfileToken>\n" +
               " <tptz:Velocity>\n" +
               "  <tt:PanTilt x=\"" + x + "\" y=\"" + y + "\"/>\n" +
               "  <tt:Zoom x=\"" + z + "\"/>\n" +
               " </tptz:Velocity>\n" +
               "</tptz:ContinuousMove>\n";
    }
}
