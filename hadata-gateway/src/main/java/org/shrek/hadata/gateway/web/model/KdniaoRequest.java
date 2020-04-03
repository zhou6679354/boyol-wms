package org.shrek.hadata.gateway.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 09:01
 */
@Data
public class KdniaoRequest implements Serializable {
    String DataSign;
    String RequestType;
    String RequestData;

    public String getUrl() {
        return "http://hadata-service-reiley/kdniaoDist";
    }


}
