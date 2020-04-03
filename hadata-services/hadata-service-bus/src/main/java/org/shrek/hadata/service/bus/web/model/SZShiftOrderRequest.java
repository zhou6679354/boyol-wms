package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class SZShiftOrderRequest {
    //单据类型
    int billTypeId;
    //仓科ID
    String whId;
    //客户单据编号
    String erp_no;
    //单据日期
    Date billlDate;
    //原货主代码
    String fromClient;
    //新货主代码
    String toClient;
    //备注
    String note;
    List<SZShiftOrderRequest.DetailsItem> grid;


    @Data
    public static  class DetailsItem{
        //客户单据编号
        String erp_no;
        //行号
        String lineNumber;
        //原商品编码
        String fromSkuCode;
        //新商品编码
        String toSkuCode;
        //基本数量
        double baseQuantity;
        //箱数
        int quantity;
        //原预扫码客户编码
        String fromPreScanCust;
        //新预扫码客户编码
        String toPreScanCust;
        //备注
        String noteDtl;

    }
}
