package org.shrek.hadata.service.reiley.service.waybill.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月10日 11:38
 */
@Data
public class WaybillPrintDataDto {
    private String waybillCode;
    private String goodsDesc;

    //发件人信息
    private Contacts sender;
    //收件人信息
    private Contacts recipient;
    // 路由信息
    private RoutingInfoBean routingInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ShippingOption shippingOption;

    @Data
    public static class Contacts {
        private AddressBean address;
        private String mobile;
        private String name;
        private String phone;
    }
    @Data
    public static class AddressBean {
        private String city;
        private String detail;
        private String district;
        private String province;
        private String town;
    }


    @Data
    public static class RoutingInfoBean {
        private ConsolidationBean consolidation;
        private OriginBean origin;
        private SortationBean sortation;
        private String routeCode;
        @Data
        public static class ConsolidationBean {
            private String name;
            private String code;
        }
        @Data
        public static class OriginBean {
            private String name;
            private String code;
        }
        @Data
        public static class SortationBean {
            private String name;
        }
    }

    @Data
    public static class ShippingOption{
        String code;
        HashMap<String,HashMap<String,String>> services;
        String title;
    }
}
