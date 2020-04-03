package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
public class TblOrderSynWisdom extends BasicEntity {
    private String whId;
    private String orderNumber;
    private String procType;
    private Integer synFlag;
    private Date initDate;
    private Date synDate;
    private String itemNumber;
    private Double qtyShipped;
    private String clientCode;
    private String shipToCity;
    private  String waveId;
}
