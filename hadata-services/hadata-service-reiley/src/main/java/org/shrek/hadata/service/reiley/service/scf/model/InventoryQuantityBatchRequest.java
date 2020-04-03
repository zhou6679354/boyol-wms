package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 *
 * 金融库存查询请求参数
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 11:24
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryQuantityBatchRequest {
    private String ownerUserId;
    private String cpStoreCode;
    private List<String> itemCodes;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItemCodesBean {
        private String itemCode;
    }
    /*private List<WmsInventoryQuantityQueryBean> wmsInventoryQuantityQueryList;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WmsInventoryQuantityQueryBean {
        private String itemId;
        private String inventoryType;
        private String itemCode;
        private String ownerUserId;
        private String storeCode;
    }*/
}
