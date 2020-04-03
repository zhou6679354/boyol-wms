package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

@Data
public class TStoredCount extends BasicEntity {
    private String itemNumber;
    private Double actualQty;
    private String whId;
    private Long type;
}
