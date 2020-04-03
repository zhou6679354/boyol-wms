package org.shrek.hadata.gateway.web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月11日 11:12
 */
public class GateWayRequest {
    @Setter
    @Getter
    String method;
    @Setter
    @Getter
    String format;
    @Setter
    @Getter
    String appKey;
    @Setter
    @Getter
    String v;
    @Setter
    @Getter
    String sign;
    @Setter
    @Getter
    String signMethod;
    @Setter
    @Getter
    String timestamp;
    @Setter
    @Getter
    String server;


    public String getUrl() {
        return "http://hadata-service-" + this.server + "/" + this.method;
    }


}
