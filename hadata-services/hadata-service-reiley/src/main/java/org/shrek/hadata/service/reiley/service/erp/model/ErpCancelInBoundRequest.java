package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 取消入库
 *
 * @author wyyy
 * @version 1.0
 * @date 2018年11月28日 10:51
 */
@Data
public class ErpCancelInBoundRequest {

    String billid;
    String billtypeid;
    String b_no;
    String erp_no;
    String storagelocation;
    String note;

}
