package org.shrek.hadata.service.reiley.service.waybill.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月10日 09:43
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaybillDto {
    private int waybillType=1;
    private int waybillCount=1;
    private String providerCode;
    private String branchCode;
    private String platformOrderNo;
    private String vendorCode;
    private String vendorName;
    private String vendorOrderCode;
    private String salePlatform;
    private WaybillAddress fromAddress;
    private WaybillAddress toAddress;
    private int weight;
    private int volume;
    private int payType=0;
    private int shouldPayMoney=0;
    private boolean needGuarantee=false;
    private int guaranteeMoney=0;

    private String settlementCode;
    private String expressPayMethod;
    private String expressType;
    @JsonIgnore
    private String printTemplate;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WaybillAddress {
        private int provinceId;
        private String provinceName;
        private int cityId;
        private String cityName;
        private int countryId;
        private String countryName;
        private int countrysideId;
        private String countrysideName;
        private String address;
        private String contact;
        private String phone;
        private String mobile;
    }
}
