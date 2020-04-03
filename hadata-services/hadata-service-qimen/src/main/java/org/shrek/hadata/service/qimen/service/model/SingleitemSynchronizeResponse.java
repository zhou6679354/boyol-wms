package org.shrek.hadata.service.qimen.service.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.qimen.api.QimenResponse;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月20日 11:23
 */
@Data
@JacksonXmlRootElement(localName = "response")
public class SingleitemSynchronizeResponse extends QimenResponse {
    private String itemId;
}
