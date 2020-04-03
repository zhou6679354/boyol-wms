package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 移仓调拨通知
 *
 * @author wyyy
 * @version 1.0
 * @date 2018年11月28日 10:51
 */
@Data
public class ErpNoticeAllocateRequest {


    String billid;
    String billtypeid;
    String companycode;
    String erp_no;
    Date billldate;
    String note;
    String toclient    ;
    String fromclient    ;

    String packageskucode;
    int packagequantity;

    String contact;
    String tel;
    String address;
//    String storagelocation_from;
//    String storagelocation_to;
    List<InBoundResultDetail> grid;


    @Data
    public static class InBoundResultDetail {


        String fromzone    ;
        String toskucode   ;
        String billdtlid   ;
        String notedtl     ;
        String fromtype    ;
        String fromprescancust    ;
        String toprescanned     ;
        String totype    ;
        String toprescancust   ;
        String fromclient       ;
        String fromskucode      ;
        int quantity   ;
        int basequantity     ;
        String tozone    ;
        String fromprescanned   ;
        String batchcode        ;
        int documenttype;
    }

}
