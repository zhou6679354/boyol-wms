package org.shrek.hadata.service.hwms.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月20日 18:12
 */
@Data
public class TOrderConfirm implements Serializable{
    String tranQty;
    String lineNumber;
    String lotNumber;
    String endTranDate;
    String displayItemNumber;
    String description;
    String prodDate;
    String zone;
    String poLineNumber;
}
