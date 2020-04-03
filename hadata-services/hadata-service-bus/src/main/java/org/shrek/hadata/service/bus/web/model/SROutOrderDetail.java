package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;

@Data
public class SROutOrderDetail {
    private String whNo;
    private String productNo;
    private int productNum;
    private String whPostionNo;

}
