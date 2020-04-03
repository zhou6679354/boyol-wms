package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 16:37
 */
@Data
public class ErpStoreResponse  {
    int status;
    String message;
    List<StoreItem> items;
    @Data
    public static class StoreItem{
        String store;
        String client;
        String zone;
        String type;
        String itemCode;
        String batchCode;
        int qty;
        int boxQty;
        String preScanned;
        String preScanCust;
        String prodDate;
        String expDate;
    }
}
