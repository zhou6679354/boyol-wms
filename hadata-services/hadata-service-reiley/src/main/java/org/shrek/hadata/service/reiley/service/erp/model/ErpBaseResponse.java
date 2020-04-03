package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 16:18
 */
@Data
public abstract class ErpBaseResponse {
    int status;
    String message;
}
