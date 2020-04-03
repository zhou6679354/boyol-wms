package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhouwenheng
 * @version 1.0
 * @date 2019年04月15日 11:05
 */
@Data
public class SZInOrderRequest {


        //单据类型
          int billTypeId;
//仓科ID
    String whId;
        //客户单据编号
        String erp_no;
        //单据日期
        Date billlDate;
    //货主代码
    String storageLocation;
    //货物预计到仓时间
    Date deliveryDate;
    //备注
    String note;
          List<DetailsItem>  grid;


    @Data
    public static  class DetailsItem{
    //客户单据编号
      String erp_no;
    //商品编码
    String skuCode;
      //商品属性
        String materialType;
       //基本数量
        double baseQuantity;
        //箱数
        int quantity;
        //预扫码客户编码
        String preScanCust;

        //备注
        String noteDtl;

    }
}
