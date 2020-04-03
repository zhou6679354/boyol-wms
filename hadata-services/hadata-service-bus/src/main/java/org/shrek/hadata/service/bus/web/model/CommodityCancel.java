package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

@Data
public class CommodityCancel {

    private String warehouseId;
    private String customerId;
    private String orderNo;
    private String orderType;
    private String reason;
    
}
