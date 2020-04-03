package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.List;

@Data
public class SROutOrderRequest {
    private String projectNo;
    private String deliveryApplyNo;
    private List<SROutOrderDetail> products;
}
