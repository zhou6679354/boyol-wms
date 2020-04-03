package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 16:37
 */
@Data
public class StoreItem extends BasicEntity {

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
