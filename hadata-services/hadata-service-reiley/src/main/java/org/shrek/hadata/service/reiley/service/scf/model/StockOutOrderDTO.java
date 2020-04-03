package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 17:22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockOutOrderDTO {

    private String finishTime;
    private String tradeOrder;
    private String stockOutOrderCode;
    private String expressCompany;
    private String ownerUserId;
    private String expressCode;
    private String ownerUserName;
    private String stockOutType;
    private String storeName;
    private String tradePlatform;
    private String storeCode;
    private List<ItemsBean> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemsBean {

        private String itemName;
        private long quantity;
        private String itemCode;


    }
}
