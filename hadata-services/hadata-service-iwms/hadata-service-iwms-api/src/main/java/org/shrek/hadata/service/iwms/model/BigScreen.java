package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;
@Data
public class BigScreen extends BasicEntity {
    private double throughput;
    private double inorderthroughput;
    private double outorderthroughput;
    private String categoriesCommodities;
    private String whId;
    private String queryDate;
}
