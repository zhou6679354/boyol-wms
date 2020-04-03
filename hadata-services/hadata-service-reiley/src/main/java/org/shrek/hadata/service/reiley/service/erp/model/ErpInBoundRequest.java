package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 入库通知
 *
 * @author wyyy
 * @version 1.0
 * @date 2018年11月28日 10:51
 */
@Data
public class ErpInBoundRequest {


    String billid;
    String vendorbillno;
    Date billldate;
    String billtypeid;
    String companycode;
    String erp_no;
    String note;
    String supplyid;
    String storagelocation;
    List<InBoundResultDetail> grid;

    String deliveryaddress;
    String deliverystorage;
    String deliverycon;


    @Data
    public static class InBoundResultDetail {

        int basequantity;
        String billdtlid;
        Date deliverydate;
        String materialtype;
        int quantity;
        String skucode;
        String notedtl;
        String customerordernumber;
        String toprescanned     ;
        String toprescancust   ;
        String tozone    ;
    }

}
