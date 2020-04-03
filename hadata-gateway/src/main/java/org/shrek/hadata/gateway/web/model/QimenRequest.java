package org.shrek.hadata.gateway.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月14日 16:02
 */
public class QimenRequest implements Serializable {
    @Setter
    @Getter
    String method;
    @Setter
    @Getter
    String format;
    @Setter
    @Getter
    String app_key;
    @Setter
    @Getter
    String v;
    @Setter
    @Getter
    String sign;
    @Setter
    @Getter
    String sign_method;
    @Setter
    @Getter
    String timestamp;
    @Setter
    @Getter
    String customerId;


    public String getUrl(String url) {
        return "http://hadata-service-" + url + "/" + this.method;
    }

    public String getQimenUrl(String version) {
        return "http://hadata-service-qimen/" + version + "/" + this.method;
    }


}
