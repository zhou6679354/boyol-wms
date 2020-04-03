package org.shrek.hadata.gateway.webservice.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月16日 14:16
 */
@Data
public class WebServiceResponse<T> implements Serializable {
    boolean success;
    String errCode;
    String errMessage;
    T content;

    public static <T> WebServiceResponse<T> success(T data) {
        WebServiceResponse<T> response = new WebServiceResponse<>();
        response.setSuccess(true);
        response.setContent(data);
        return response;
    }

    public static <T> WebServiceResponse<T> fail(String errCode, String errMessage) {
        WebServiceResponse<T> response = new WebServiceResponse<>();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }
}
