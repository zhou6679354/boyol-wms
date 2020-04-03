package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

@Data
public class StockInfo {
    private  String sku;
    private  double  pkgs;
    private  String bincode;
}
