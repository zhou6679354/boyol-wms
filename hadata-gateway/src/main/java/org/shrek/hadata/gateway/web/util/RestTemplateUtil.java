package org.shrek.hadata.gateway.web.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月29日 09:18
 */
public class RestTemplateUtil {
    public static <T> T post(RestTemplate restTemplate, String url, String body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Charset", "utf-8");
        headers.set("Content-type", "application/json; charset=utf-8");
        HttpEntity<String> entity = new HttpEntity(body, headers);
        return restTemplate.postForObject(url, entity, responseType);
    }


}
