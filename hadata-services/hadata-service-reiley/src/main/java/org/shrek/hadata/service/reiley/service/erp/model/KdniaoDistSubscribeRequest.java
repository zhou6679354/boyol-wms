package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 16:37
 */
@Data
public class KdniaoDistSubscribeRequest {
    String ShipperCode;
    String OrderCode;
    String LogisticCode;
    String PayType;
    String ExpType;
    String CustomerName;
    String CustomerPwd;
    String MonthCode;
    String IsNotice;
    SenderDto Sender;
    SenderDto Receiver;
    List<CommodityDto> Commodity;
    @Data
    public static class SenderDto{
        String Name;
        String Tel;
        String Mobile;
        String ProvinceName;
        String CityName;
        String ExpAreaName;
        String Address;
    }
    @Data
    public static class CommodityDto{
        String GoodsName;
    }
}
