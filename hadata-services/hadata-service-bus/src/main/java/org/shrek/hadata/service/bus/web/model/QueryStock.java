package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryStock {
    private List<Data> data;
    @lombok.Data
    public static class Data {
        private  String sku;
        private  String bincode;
        private  double  pkgs;

    }
}
