package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;
@Data
public class StorageStock extends BasicEntity {
    private String orderNumber;
    private String itemNumber;
    private String waveId;
    private String clientCode;
    private String shipToCity;
    private String locationId;
    private double plannedQuantity;



}
