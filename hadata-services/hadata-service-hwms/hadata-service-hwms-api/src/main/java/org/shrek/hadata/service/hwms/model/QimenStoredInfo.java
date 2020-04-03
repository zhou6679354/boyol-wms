package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月26日 09:16
 */
@Data
public class QimenStoredInfo extends BasicEntity {
    String warehouseCode;
    String itemCode;
    String storeType;
    int inventoryType;
    Double quantity;
    Double lockQuantity;
    String batchCode;
    String productDate;
    String expireDate;
    String produceCode;

    public String getMapKey(){
        return this.storeType+this.batchCode+this.productDate+this.expireDate+this.produceCode;
    }
}
