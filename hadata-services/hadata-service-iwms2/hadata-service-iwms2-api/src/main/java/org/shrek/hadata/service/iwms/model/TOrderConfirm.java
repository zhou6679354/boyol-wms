package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月20日 18:12
 */
@Data
public class TOrderConfirm extends BasicEntity {
    String tranQty;//base
    String lineNumber; //billdtlld
    String lotNumber;//批次号
    String endTranDate;
    String displayItemNumber;//sku
    String description;
    String prodDate;//makedate
    String zone;
}
