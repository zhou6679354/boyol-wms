package org.shrek.hadata.service.qimen.service.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.qimen.api.QimenResponse;
import com.taobao.api.internal.mapping.ApiField;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月20日 11:25
 */
@Data
@JacksonXmlRootElement(localName = "response")
public class EntryorderCreateResponse extends QimenResponse {
    @ApiField("entryOrderId")
    private String entryOrderId;
}
