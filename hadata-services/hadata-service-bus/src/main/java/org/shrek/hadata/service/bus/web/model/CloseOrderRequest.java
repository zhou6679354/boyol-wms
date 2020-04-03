package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

@Data
public class CloseOrderRequest {
    //单据编号
    String erp_no;
    //单据类型
    int billTypeId;
    //备注
    String note;
    //仓库
    String whId;
}
