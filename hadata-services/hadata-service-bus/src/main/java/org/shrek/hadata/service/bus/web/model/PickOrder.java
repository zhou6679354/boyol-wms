package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PickOrder {
    private String reqcode;
    private List<Data> data;
    @lombok.Data
    public static class Data {
        private  String sku;
        private  double  ordPcsQty;
        private double ordBoxQty;
        private String orderno;
        private double pltNum;
        private double oddQty;
        private double boxNum;
        private String custcode;
        private String trway;
        private String bincode;
        private  String waveId;
        private  String transportRoute;
        private  int palletSpecs ;
    }
}
