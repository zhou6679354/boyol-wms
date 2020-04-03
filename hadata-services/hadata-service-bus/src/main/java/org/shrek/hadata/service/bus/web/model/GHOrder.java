package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.text.DateFormat;
import java.util.Date;

@Data
public class GHOrder {
    private int id;
    private Date billDate;
    private String billNo;
    private String traderNo;
    private String traderName;
    private String warehouse;
    private String salesman;
    private String deliveryAddress;
    private int billType;
    private String originator;
    private String creationDate;
}
