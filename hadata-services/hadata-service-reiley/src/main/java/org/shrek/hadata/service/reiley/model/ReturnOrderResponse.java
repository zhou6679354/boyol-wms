package org.shrek.hadata.service.reiley.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月27日 19:04
 */
@Data
@JacksonXmlRootElement(localName = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderResponse {

    String flag;
    String code;
    String message;
    String returnOrderId;

    public static ReturnOrderResponse success(String orderId) {
        ReturnOrderResponse r = new ReturnOrderResponse();
        r.setFlag("success");
        r.setCode("0");
        r.setMessage("invalid appkey");
        r.setReturnOrderId(orderId);
        return r;
    }

    public static ReturnOrderResponse fail(String orderId,String message) {
        ReturnOrderResponse r = new ReturnOrderResponse();
        r.setFlag("failure");
        r.setCode("50");
        r.setMessage(message);
        r.setReturnOrderId(orderId);
        return r;
    }
}
