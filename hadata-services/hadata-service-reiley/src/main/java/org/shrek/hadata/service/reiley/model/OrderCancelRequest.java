package org.shrek.hadata.service.reiley.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月27日 19:35
 */
@Data
@JacksonXmlRootElement(localName ="request")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCancelRequest {
    String warehouseCode;
    String ownerCode;
    String orderCode;
    String orderId;
    String orderType;
    String cancelReason;
}
