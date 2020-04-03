package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import java.io.Serializable;
import java.util.Date;

@Data
public class TReceiptCount extends BasicEntity {
    private String poNumber;
    private String lineNumber;
    private String itemNumber;
    private Date expirationDate;
    private String lotNumber;
    private String receiptUom;
    private String storageType;
    private String whId;
    private Long storedAttributeId;
    private Double qtyReceived;

}
