package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 金融库存查询返回结果
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 10:06
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryQuantityBatchReponse {
    private boolean success;
    private String errorCode;
    private String errorMsg;
    private String ownerUserId;
    private String cpStoreCode;
    private List<Item> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        //private String itemId;
        //private String storeCode;
        private String itemCode;
        private String batchCode;

        private int quantity;
        private int lockQuantity;
        private int inventoryType;
        private String produceDate;
        private String overdueDate;
        private int guaranteePeriod;

    }
}
