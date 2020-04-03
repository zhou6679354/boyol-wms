package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.List;

@Data
public class StockPicking {
    private List<Data> data;
    @lombok.Data
    public static class Data {
        private  String orderNo;
        private  String sku;
        private  String opttp;
        private  String bincode;
        private  double  pkg;
        private  double pickFlag;
    }
}
