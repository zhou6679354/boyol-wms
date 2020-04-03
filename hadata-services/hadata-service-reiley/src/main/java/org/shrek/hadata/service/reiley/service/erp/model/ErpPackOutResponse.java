package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 包材领用
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年11月13日 12:51
 */
@Data
public class ErpPackOutResponse {
    int status;
    String message;
    List<PackOutItem> items;

    @Data
    public static class PackOutItem{
        Date BilllDate;
        String SKUCode;
        String BatchCode;
        int BaseQuantity;
        String store;
        String client;
        String Note;
    }

}
