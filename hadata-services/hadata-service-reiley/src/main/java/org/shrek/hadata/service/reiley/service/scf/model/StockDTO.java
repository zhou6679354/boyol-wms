package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 11:11
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockDTO {
    private String itemId;
    private String inventoryType;
    private String quantity;
    private String itemCode;
    private String lockQuantity;
    private String storeCode;
}
