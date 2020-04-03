package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;
@Data
public class SRInOrderDetail {


        private String whNo;
        private String whPostionNo;
        private String productSkuNo;
        private String productNo;
        private String productBatchNo;
        private Date productDate;
        private int productNum;
        private int totalNum;
        private Date exWhDate;
        private double  productPrice;
        private double  totalAmount;
        private int perNum;
        private String orderNo;

}
