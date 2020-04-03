package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 入库消息返回
 *
 * @author wyyy
 * @version 1.0
 * @date 2018年11月28日 10:51
 */
@Data
public class InventoryQueryResponse {


    String status;
    String message;
    List<InventoryQueryResponseItem> items;


    @Data
    public static class InventoryQueryResponseItem {

        String itemCode;
        String batchCode;
        int qty;
        int boxQty;
        String store;

    }

}
