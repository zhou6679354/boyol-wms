package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 16:37
 */
@Data
public class ErpOutB2CResponse {
    int status;
    String message;
    List<StoreItem> items;
    @Data
    public static class StoreItem{
        String batchCode;
        int ISRETURN; //是否退货 0否1是
        String itemCode;
        Double price;
        Double Price2;
        int QUANTITY;//基本数量 数量的单位EA
        String store;
        String client;
        String zone;//库区（正常品区、退货区..)
        String type;//状态（商品、赠品、样品..）
        String preScanned;//是否预扫码 N否 Y是
        String preScanCust;//	预扫码客户编号
        String Notedtl;//备注
    }
}
