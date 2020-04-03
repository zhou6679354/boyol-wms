package org.shrek.hadata.gateway.web;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月14日 16:42
 */
public abstract class GateWayConstants {

    public static final String SERVICE_CALL_EXCEPTION =
            "<error_response>\n" +
            "    <code>50</code>\n" +
            "    <msg>Remote service error</msg>\n" +
            "    <sub_code>edi.service-error</sub_code>\n" +
            "    <sub_msg>子服务调用异常</sub_msg>\n" +
            "</error_response>";
}
