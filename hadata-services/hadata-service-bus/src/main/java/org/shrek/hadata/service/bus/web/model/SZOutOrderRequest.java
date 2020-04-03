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
public class SZOutOrderRequest {

    //单据类型
    int billTypeId;
    //仓科ID
    String whId;
    //单据编号
    String erp_no;
    //出库日期
    Date billlDate;
    //货主
    String storageLocation;
    //收货地址
    String deliveryAddress;
    //收货仓
    String deliveryStorage;
    //收货人
    String deliverycon;
    //收货人联系方式
    String deliverytel;
    //预计到货日期
    Date deliveryDate;
    //备注
    String note;
    List<DetailsItem>  grid;


    @Data
    public static  class DetailsItem{
        //单据编号
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
