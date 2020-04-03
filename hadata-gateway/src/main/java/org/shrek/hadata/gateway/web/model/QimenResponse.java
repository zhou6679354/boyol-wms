package org.shrek.hadata.gateway.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月11日 10:25
 */
@Data
@JacksonXmlRootElement(localName = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QimenResponse {
    String flag;
    String code;
    String message;

    public static QimenResponse success() {
        QimenResponse r = new QimenResponse();
        r.setFlag("success");
        r.setCode("0");
        r.setMessage("invalid appkey");
        return r;
    }

    public static QimenResponse fail(String message) {
        QimenResponse r = new QimenResponse();
        r.setFlag("failure");
        r.setCode("50");
        r.setMessage(message);
        return r;
    }
}
