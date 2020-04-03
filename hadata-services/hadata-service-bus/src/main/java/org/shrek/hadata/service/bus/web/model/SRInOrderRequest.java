package org.shrek.hadata.service.bus.web.model;

import lombok.Data;


import java.util.List;

@Data
public class SRInOrderRequest {
    private String projectNo;
    private int totalRecordNum;
    private List<SRInOrderDetail> products;

}
