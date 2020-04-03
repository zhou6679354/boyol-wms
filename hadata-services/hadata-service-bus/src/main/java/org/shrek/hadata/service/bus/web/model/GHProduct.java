package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;

@Data
public class GHProduct {
    private int id;
    private String productNo;
    private String models;
    private String productName;
    private String unit;
    private double tradePrice;
    private String barcode;
    private String brand;
    private double retailPrice;
    private int sl_days;
    private int pkg_ratio;
    private Date createDate;
}
