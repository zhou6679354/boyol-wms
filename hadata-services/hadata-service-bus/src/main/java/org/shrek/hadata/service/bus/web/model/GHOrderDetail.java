package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GHOrderDetail {
    private List<BillDetail> billDetail;
    private Bill bill;
    @Data
    public class Bill {
        private int id;
        private String billNo;
        private Date billDate;
        private String traderNo;
        private String traderName;
        private String warehouse;
        private String salesman;
        private int billType;
        private String auditStatus;
        private String originator;
        private String creationDate;
        private String exec_state;
        private String deliveryMethod;
        private double discount;
        private String cust_orderNo;
    }

    @Data
    public class BillDetail {
        private int id;
        private String productNo;
        private String batchNo;
        private String productionDate;
        private String productName;
        private String models;
        private double qty;
        private double unitPrice;
    }

}
