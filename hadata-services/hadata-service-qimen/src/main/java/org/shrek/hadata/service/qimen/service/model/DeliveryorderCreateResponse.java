package org.shrek.hadata.service.qimen.service.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.qimen.api.QimenResponse;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月20日 11:26
 */
@Data
@JacksonXmlRootElement(localName = "response")
public class DeliveryorderCreateResponse extends QimenResponse {
    @ApiField("createTime")
    private String createTime;
    @ApiField("deliveryOrderId")
    private String deliveryOrderId;
    @ApiListField("deliveryOrders")
    @ApiField("deliveryOrder")
    private List<com.qimen.api.response.DeliveryorderCreateResponse.DeliveryOrder> deliveryOrders;
    @ApiField("logisticsCode")
    private String logisticsCode;
    @ApiField("warehouseCode")
    private String warehouseCode;
}
