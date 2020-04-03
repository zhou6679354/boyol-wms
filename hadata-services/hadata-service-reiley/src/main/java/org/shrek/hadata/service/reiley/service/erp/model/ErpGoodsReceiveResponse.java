package org.shrek.hadata.service.reiley.service.erp.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 到货
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年11月13日 12:51
 */
@Data
public class ErpGoodsReceiveResponse {

    String BillTypeID;
    String ERP_No;
    Date ReceiveDate;
    String Satus;
    String Note;

}
