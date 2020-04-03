package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;



@Data
public class BigScreenStorck extends BasicEntity {
    private double throughput;
    private String categoriesCommodities;
    private String whId;
    private String queryDate;
}
