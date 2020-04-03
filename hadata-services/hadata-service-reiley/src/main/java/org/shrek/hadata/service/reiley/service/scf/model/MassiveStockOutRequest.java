package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 16:34
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MassiveStockOutRequest {
    private String orderType;
    private String tmsOrderCode;
    private String orderSource;
    private String ownerUserId;
    private String orderCode;
    private String erpOrderCode;
    private String storeCode;
    private String tmsServiceCode;
    private List<Item> items;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String itemId;
        private String orderLineNo;
        private String inventoryType;
        private int quantity;
        private String itemCode;
        private String orderSourceCode;
        private String subSourceCode;
    }
}
