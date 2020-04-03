package org.shrek.hadata.service.reiley.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月27日 19:35
 */
@Data
@JacksonXmlRootElement(localName = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCancelResponse implements Serializable {
    String  flag;
    String code;
    String message;

    public static OrderCancelResponse success() {
        OrderCancelResponse r = new OrderCancelResponse();
        r.setFlag("success");
        r.setCode("0");
        r.setMessage("invalid appkey");
        return r;
    }

    public static OrderCancelResponse fail(String message) {
        OrderCancelResponse r = new OrderCancelResponse();
        r.setFlag("failure");
        r.setCode("50");
        r.setMessage(message);
        return r;
    }
}
