package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SZReturnOutOrder {

    //单据类型
    int billTypeId;
    //客户单据编号
    String b_no;
    //单据日期
    Date billDate;
    //货主代码
    String client;
    //仓库
    String store;
    //备注
    String note;
    List<SZReturnOutOrder.DetailsItem> details;


    @Data
    public static  class DetailsItem{
        //客户单据编号
        String b_no;
        //行号
        String lineNumber;
        //商品编码
        String skuCode;
        //批次号
        String batchCode;
        //失效日期
        Date expiryDate;
        //是否预扫码
        String preScanned;
        //基本数量
        double baseQuantity;
        //箱数
        int quantity;
        //商品属性
        String materialType;
        //预扫码客户编码
        String preScanCust;
        //备注
        String noteDtl;

    }
}
