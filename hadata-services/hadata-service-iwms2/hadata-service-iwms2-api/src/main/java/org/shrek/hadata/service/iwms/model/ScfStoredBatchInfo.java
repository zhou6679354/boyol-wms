package org.shrek.hadata.service.iwms.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月26日 09:16
 */
@Data
public class ScfStoredBatchInfo implements Serializable {
    String itemCode;
    String batchCode;
    int quantity;
    int lockQuantity;
    int inventoryType;
    String produceDate;
    String overdueDate;
    int guaranteePeriod;

    public String getMapKey(){
        return this.itemCode+this.batchCode+this.produceDate+this.overdueDate+String.valueOf(guaranteePeriod);
    }
}
