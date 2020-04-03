package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;
@Data
public class StorageStock extends BasicEntity {
    private String orderNumber;
    private String itemNumber;
    private String locationId;
    private Double actualQty;
    private Double unavailableQty;
    private String waveId;

}
