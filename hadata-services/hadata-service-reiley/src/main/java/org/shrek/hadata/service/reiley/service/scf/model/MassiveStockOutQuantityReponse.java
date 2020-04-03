package org.shrek.hadata.service.reiley.service.scf.model;

import lombok.Data;

/**
 * 大货出库数目查询返回值
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 09:58
 */
@Data
public class MassiveStockOutQuantityReponse {
    boolean success;
    String errorCode;
    String errorMsg;
    int quantity;
}
