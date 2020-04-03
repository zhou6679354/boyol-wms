package org.shrek.hadata.service.bus.web.model;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SZStockRequest {
        //批次号
        String batchCode;
        //产品代码
        String itemCode;
        //基本数量
        double qty;
        //仓库
        String store;
        //库存状态
        long type;
        //库存状态
        Date expirationDate;
        //预扫码客户编码
        String preScanCust;

}
