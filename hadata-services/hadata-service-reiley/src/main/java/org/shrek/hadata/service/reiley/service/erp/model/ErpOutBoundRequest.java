package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 出库通知
 *
 * @author wyyy
 * @version 1.0
 * @date 2018年11月28日 10:51
 */
@Data
public class ErpOutBoundRequest {


    String billid;
    Date billldate;
    String billtypeid;
    String companycode;
    String erp_no;
    String supplyid;
    String storagelocation;
    String isAllocate;
    String deliveryaddress;
    String deliverycon;
    String deliverytel;
    List<OutBoundRequestDetail> grid;

    String appointmenttime;
    String appointmentnum;
    String customercity;
    String note;



    @Data
    public static class OutBoundRequestDetail {

        int basequantity;
        String billdtlid;
        Date deliverydate;
        String materialtype;
        int quantity;
        String skucode;
        String notedtl;

        String customerordernumber;
        String customerunit;
        int unitratio;
        String allocateno;
        String zone;
        String fromzone;
        String fromprescanned;
        String fromprescancust;
        String customercommoditycode;
        String customercommodityname;
    }

}
