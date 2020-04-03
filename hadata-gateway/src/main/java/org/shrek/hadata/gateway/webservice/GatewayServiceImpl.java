package org.shrek.hadata.gateway.webservice;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.SignUtil;
import org.shrek.hadata.gateway.web.util.RestTemplateUtil;
import org.shrek.hadata.gateway.webservice.model.WebServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.jws.WebService;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月16日 14:21
 */
@Slf4j
@Service
@WebService(serviceName = "GatewayService", portName = "GatewayPort",
        targetNamespace = "http://service.ws.boyol/",
        endpointInterface = "org.shrek.hadata.gateway.webservice.GatewayService")
public class GatewayServiceImpl implements GatewayService {

    private static String URL = "http://hadata-service-reiley/";

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String execute(String reqId, String reqMethod, String reqFrom, String reqDate, String signType, String sign, String content) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("reqId", reqId);
        params.put("reqMethod", reqMethod);
        params.put("reqFrom", reqFrom);
        params.put("reqDate", reqDate);
        params.put("signType", signType);
        params.put("content", content);
        log.warn(JacksonUtil.nonEmpty().toJson(params));
        try {
            String newSign = SignUtil.hadataSign(params, "5f8bcbc08c4246809a9198a2f73a28d1", signType);
            //if (newSign.equals(sign))
            if (true)
            {

                return RestTemplateUtil.post(restTemplate, URL+reqMethod, content, String.class);
            } else {
                return JacksonUtil.nonEmpty().toJson(WebServiceResponse.fail("500", "加密验证失败!"));
            }
        } catch (IOException e) {
            return JacksonUtil.nonEmpty().toJson(WebServiceResponse.fail("500",e.getMessage()));
        }
    }
}
