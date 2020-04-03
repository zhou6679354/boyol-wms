package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 入库结果
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年11月13日 12:51
 */
@Data
public class InBoundResult {


    String BillID;
    String BillTypeID;
    String CompanyCode;
    String B_No;
    Date BilllDate;
    String SupplyID;
    String Note;
    Date reqDate;
    String reqFrom;
    String reqId;

    List<InBoundResultDetail> details;


    @Data
    public static class InBoundResultDetail {

        String BillDtlID;
        String SKUCode;
        String BatchCode;
        Date MakeDate;
        int BaseQuantity;
        int Quantity;
        String MaterialType;
        String StorageLocation;
        String Notedtl;
    }

}
