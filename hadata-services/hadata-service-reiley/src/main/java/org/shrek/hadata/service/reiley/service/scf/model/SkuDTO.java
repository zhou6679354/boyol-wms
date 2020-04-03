package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 10:17
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkuDTO {


    private String ownerUserId;
    private String storeCode;
    private List<Item> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String itemCode;
        private String length;
        private String weight;
        private String barCode;
        private String logisticsSupportFee;
        private String volume;
        private String itemId;
        private String itemName;
        private String consumable;
        private String width;
        private String purchaseFee;
        private String category;
        private String brand;
        private String valuable;
        private String productType;
        private String sellFee;
        private String status;
        private String height;
        private String bePeriodGuarantee;
        private String beImport;
    }
}
