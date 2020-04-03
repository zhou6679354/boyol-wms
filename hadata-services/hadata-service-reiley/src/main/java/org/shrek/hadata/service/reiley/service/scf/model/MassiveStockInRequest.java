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
public class MassiveStockInRequest {
    private String orderCode;
    //private String storeCode;
    private String cpStoreCode;
    private String ownerUserId;
    private String orderConfirmTime;
    private int orderType;
    private String status;
    private String orderCreateTime;
    private String outBizCode;
    private List<StockInCheckItem> stockInItems;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StockInCheckItem {
        private String orderItemId;
        private String itemCode;
        private String itemName;
        private String inventoryType;
        private long quantity;
        private int planquantity;
        private String remark;
    }
}
