package org.shrek.hadata.gateway.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 09:01
 */
@Data
public class CainiaoRequest implements Serializable {
    String msg_type;
    String msg_id;
    String from_code;
    String partner_code;
    String data_digest;
    String logistics_interface;

    public String getUrl() {
        return "http://hadata-service-reiley/" + this.msg_type;
    }


}
