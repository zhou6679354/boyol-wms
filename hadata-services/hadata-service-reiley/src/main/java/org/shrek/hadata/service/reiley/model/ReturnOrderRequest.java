package org.shrek.hadata.service.reiley.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月27日 19:03
 */
@Data
@JacksonXmlRootElement(localName ="request")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReturnOrderRequest {

    ReturnOrderBean returnOrder;
    @JacksonXmlElementWrapper(localName ="orderLines")
    @JacksonXmlProperty(localName = "orderLine")
    List<OrderLinesBean> orderLines;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReturnOrderBean {
        String returnOrderCode;
        String warehouseCode;
        String orderType;
        String orderFlag;
        String preDeliveryOrderCode;
        String preDeliveryOrderId;
        String logisticsCode;
        String logisticsName;
        String expressCode;
        String returnReason;
        String buyerNick;
        ExtendPropsBean extendProps;
        SenderInfoBean senderInfo;
        String remark;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ExtendPropsBean{
            String shop_code;
            String shop_name;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SenderInfoBean {
            String company;
            String name;
            String zipCode;
            String tel;
            String mobile;
            String email;
            String countryCode;
            String province;
            String city;
            String area;
            String town;
            String detailAddress;
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderLinesBean {
        String orderLineNo;
        String sourceOrderCode;
        String subSourceOrderCode;
        String ownerCode;
        String itemCode;
        String itemId;
        String inventoryType;
        String planQty;
        String batchCode;
        String productDate;
        String expireDate;
        String produceCode;
        String[] sn;
    }
}
