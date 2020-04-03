package org.shrek.hadata.service.bus.web.model;


import lombok.Data;

import java.util.List;

@Data
public class StockReturn {
    private StockReturn.Xmldata xmldata;
    @Data
    public static class Xmldata {
        private StockReturn.Header header;
    }
    @Data
    public static class Header {
        private StockReturn.Items items;
    }
    @Data
    public static class Items {
        private List<StockReturn.Item> item;
    }
    @Data
    public static class Item {
        String warehouseId;
        String customerId;
        String skuCode;
        double qty;
        String isInferior;
    }

}
