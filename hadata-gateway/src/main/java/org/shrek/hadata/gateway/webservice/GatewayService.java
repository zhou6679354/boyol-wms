package org.shrek.hadata.gateway.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月16日 14:21
 */
@WebService(targetNamespace = "http://service.ws.boyol/", name = "gateway")
public interface GatewayService {

    @WebMethod
    @WebResult(name = "return", targetNamespace = "http://service.ws.boyol/")
    String execute(
            @WebParam(name = "reqId", targetNamespace = "http://service.ws.boyol/") String reqId,
            @WebParam(name = "reqMethod", targetNamespace = "http://service.ws.boyol/") String reqMethod,
            @WebParam(name = "reqFrom", targetNamespace = "http://service.ws.boyol/") String reqFrom,
            @WebParam(name = "reqDate", targetNamespace = "http://service.ws.boyol/") String reqDate,
            @WebParam(name = "signType", targetNamespace = "http://service.ws.boyol/") String signType,
            @WebParam(name = "sign", targetNamespace = "http://service.ws.boyol/") String sign,
            @WebParam(name = "content", targetNamespace = "http://service.ws.boyol/") String content);
}
