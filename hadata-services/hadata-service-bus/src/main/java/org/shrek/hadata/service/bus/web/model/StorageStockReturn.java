package org.shrek.hadata.service.bus.web.model;


import lombok.Data;

import java.util.List;

@Data
public class StorageStockReturn {
    private String reqcode;
    private List<Data> data;
    @lombok.Data
    public static class Data {
        private  String sku;
        private  double length;
        private  double  width;
        private  double  height;
        private  double  weight;
        private  String  opttp;
        private  String  userid;

    }

}
