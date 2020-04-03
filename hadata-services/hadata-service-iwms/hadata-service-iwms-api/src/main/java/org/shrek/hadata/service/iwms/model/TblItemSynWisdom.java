package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;


@Data
public class TblItemSynWisdom extends BasicEntity {

    private String itemNumber;
    private String whId;
    private Double unitWeight;
    private Double leng;
    private Double width;
    private Double height;
    private String procType;
    private Integer synFlag;
}