package org.shrek.hadata.gateway.webservice.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月16日 14:15
 */
@Data
public class WebServiceRequest implements Serializable {
    String reqId;
    String reqMethod;
    String reqFrom;
    String reqDate;
    String signType;
    String sign;
    String content;
}
