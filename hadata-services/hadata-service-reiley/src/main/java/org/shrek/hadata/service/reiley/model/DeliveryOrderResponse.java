package org.shrek.hadata.service.reiley.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import org.shrek.hadata.commons.util.DateUtil;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月23日 16:40
 */
@Data
@JacksonXmlRootElement(localName = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryOrderResponse {
    private String flag;
    private String code;
    private String message;
    private String createTime;
    private String deliveryOrderId;
    private String warehouseCode;
    private String logisticsCode;
    private List<DeliveryOrderBean> deliveryOrders;

    public static DeliveryOrderResponse fail(String message) {
        DeliveryOrderResponse r = new DeliveryOrderResponse();
        r.setFlag("failure");
        r.setCode("50");
        r.setMessage(message);
        r.setCreateTime(DateUtil.getShortDateStr());
        return r;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryOrderBean {
        private String deliveryOrderId;
        private String warehouseCode;
        private String logisticsCode;
        private List<OrderLineBean> orderLines;
        private String createTime;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class OrderLineBean {
            private String orderLineNo;
            private String itemCode;
            private String itemId;
            private String quantity;
        }
    }
}


