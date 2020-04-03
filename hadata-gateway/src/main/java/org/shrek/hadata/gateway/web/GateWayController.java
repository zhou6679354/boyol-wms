package org.shrek.hadata.gateway.web;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.IOUtils;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.exception.HadataException;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.SignUtil;
import org.shrek.hadata.gateway.config.QimenConfig;
import org.shrek.hadata.gateway.web.model.*;
import org.shrek.hadata.gateway.web.util.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 请求入口
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月06日 10:26
 */
@Slf4j
@RestController
@RequestMapping("/gateway")
public class GateWayController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    QimenConfig qimenConfig;

    @RequestMapping("/index")
    public BaseResponse index() {
        return BaseResponse.success("Hadata数据交换平台:Develop");
    }

    @RequestMapping(value = "/exchange", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public BaseResponse exchangeUrlencodedFormMessage(HttpServletRequest request) {

        String context = request.getParameter("context");
        if (context.equals("11111")) {
            throw new HadataException("xitong");
        }
        return BaseResponse.success();
    }

    @RequestMapping(value = "/exchange.json", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String exchangeJsonMessage(GateWayRequest request, @RequestBody String body) {
        log.warn(JacksonUtil.nonEmpty().toJson(request));
        return RestTemplateUtil.post(restTemplate, request.getUrl(), body, String.class);
    }

    @RequestMapping(value = "/exchange.xml", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_XML_VALUE})
    public String exchangeXmlMessage(GateWayRequest request, @RequestBody String body) {
        log.warn(JacksonUtil.nonEmpty().toJson(request));
        return RestTemplateUtil.post(restTemplate, request.getUrl(), body, String.class);
    }

    /**
     * 根据请求URL中信息获取具体服务，并转发请求。
     *
     * @param partner
     * @param notifytype
     * @param httpRequest
     * @return
     */
    @RequestMapping(value = "/{partner}/{notifytype}.str", method = {RequestMethod.POST, RequestMethod.GET}, consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String exchangeStringMessage(@PathVariable String partner, @PathVariable String notifytype, HttpServletRequest httpRequest) {
        String content = "";
        try {
            InputStream is = httpRequest.getInputStream();
            content = IOUtils.toString(is, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = "http://hadata-service-" + partner + "/" + notifytype;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> parammap = new LinkedMultiValueMap<>();
        Enumeration paramNames = httpRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String paramValue = httpRequest.getParameter(paramName);
            parammap.add(paramName, paramValue);
        }
        Optional.ofNullable(content).ifPresent(value -> {
            log.warn("请求内容 : " + value.replaceAll("(\r\n|\r|\n|\n\r)", ""));
            parammap.add("content", value);
        });
        HttpEntity<Map> entity = new HttpEntity<>(parammap, headers);
        return restTemplate.postForObject(url, entity, String.class);
    }
    @RequestMapping(value = "/{partner}/{notifytype}.xml", method = {RequestMethod.POST, RequestMethod.GET}, consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public String exchangeStringMessage2(@PathVariable String partner, @PathVariable String notifytype, @RequestBody String body, HttpServletRequest httpRequest) {
        String content = "";
        try {
            InputStream is = httpRequest.getInputStream();
            content = IOUtils.toString(is, "utf-8");
            XMLSerializer xmlSerializer = new XMLSerializer();
            content = xmlSerializer.read(body).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = "http://hadata-service-" + partner+ "/" + notifytype;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> parammap = new LinkedMultiValueMap<>();
        log.warn("请求内容 : " + content.replaceAll("(\r\n\t|\r\n|\r\n\t\t|\r\n\t\t\t)", ""));
        parammap.add("content", content);
        HttpEntity<Map> entity = new HttpEntity<>(parammap, headers);
        return restTemplate.postForObject(url, entity, String.class);
    }

    @RequestMapping(value = "/exchange/qimen", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public String exchangeQimenMessage(QimenRequest request, @RequestBody String body) {
        log.warn(JacksonUtil.nonEmpty().toJson(body));
        System.out.println("奇门请求报文：" + JacksonUtil.nonEmpty().toJson(body));
        log.info("奇门请求报文：" + JacksonUtil.nonEmpty().toJson(body));
        HashMap<String, String> params = Maps.newHashMap();
        params.put("method", request.getMethod());
        params.put("format", request.getFormat());
        params.put("app_key", request.getApp_key());
        params.put("v", request.getV());
        params.put("timestamp", request.getTimestamp());
        params.put("customerId", request.getCustomerId());
        params.put("sign_method", request.getSign_method());
        JacksonUtil.Type type = request.getFormat().toUpperCase().equals("XML") ? JacksonUtil.Type.XML : JacksonUtil.Type.JSON;
        QimenConfig.Qimen qimen= qimenConfig.getApps().get(request.getApp_key());
        try {
            //String sign = SignUtil.qimenSign(params, body, qimen.getSecret(), request.getSign_method());
            //if (sign.equals(request.getSign())) {
            if (true) {
                if(qimen.getVersion().equals("base")) {
                    return RestTemplateUtil.post(restTemplate, request.getUrl(qimen.getAppKey()), body, String.class);
                }else{
                    return RestTemplateUtil.post(restTemplate, request.getQimenUrl(qimen.getVersion()), body, String.class);
                }
            } else {
                return JacksonUtil.nonEmpty(type).toJson(QimenResponse.fail("加密验证失败"));
            }
        } catch (Exception e) {
            //log.warn(JacksonUtil.nonEmpty().toJson(e));
            log.warn("奇门接口错误：" + e.getMessage());
            return JacksonUtil.nonEmpty(type).toJson(QimenResponse.fail("RPC服务调用异常"));
        }
    }

    @RequestMapping(value = "/exchange/cainiao", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String exchangeCainiaoMessage(CainiaoRequest request) {
        log.warn(JacksonUtil.nonEmpty().toJson(request));
        String content = request.getLogistics_interface().replaceAll("\\s*|\t|\r|\n","");
        return RestTemplateUtil.post(restTemplate, request.getUrl(), content, String.class);
    }

    @RequestMapping(value = "/exchange/kdniao", method = {RequestMethod.POST}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String exchangeKdniaoMessage(KdniaoRequest request) {
        log.warn(JacksonUtil.nonEmpty().toJson(request));
        log.warn(request.getRequestData());
        //String content = request.getRequestData().replaceAll("\\s*|\t|\r|\n","");
        String content = request.getRequestData();
        log.warn(content);
        return RestTemplateUtil.post(restTemplate, request.getUrl(), content, String.class);
    }
}
