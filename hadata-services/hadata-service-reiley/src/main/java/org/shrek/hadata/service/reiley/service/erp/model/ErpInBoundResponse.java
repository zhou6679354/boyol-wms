package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 入库消息返回
 *
 * @author wyyy
 * @version 1.0
 * @date 2018年11月28日 10:51
 */
@Data
public class ErpInBoundResponse {


    String BillID;
    String BillTypeID;
    String CompanyCode;
    String B_No;
    Date   BilllDate;
    String SupplyID;
    String Note;


    List<InBoundResponseDetail> Grid;


    @Data
    public static class InBoundResponseDetail {

        String BillDtlID;
        String SKUCode;
        String BatchCode;
        Date MakeDate;//非
        int BaseQuantity;
        int Quantity;
        String MaterialType;
        String Notedtl;//非
        String StorageLocation;
        Date expirydate;

        Date   reqDate;
        String reqFrom;
        String reqId;
        String type;
        String store;//仓库
        String client;//货主
        String zone;//库区
        String preScanned;//N表示否，Y表示是,默认传N
        String preScanCust;//回传空即可



    }

}
