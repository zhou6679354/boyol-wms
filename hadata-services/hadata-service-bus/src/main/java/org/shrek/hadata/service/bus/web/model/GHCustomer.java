package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;

@Data
public class GHCustomer {
    private int id;
    private String category;
    private String customerNo;
    private String customerName;
    private String employee;
    private String tel;
    private String address;
    private String contact;
    private Date createDate;
}
