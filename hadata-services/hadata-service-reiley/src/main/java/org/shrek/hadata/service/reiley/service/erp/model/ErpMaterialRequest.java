package org.shrek.hadata.service.reiley.service.erp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 09:22
 */
@Data
public class ErpMaterialRequest  {

    String code;
    String name;
    String sn;
    int type;
    String spec;
    int pcs;
    @JsonProperty("exp_date")
    int expDate;
    String util;
    @JsonProperty("ie_type")
    String ieType;
    Double volume;
    Double weight;
    int trail1;
    int trail2;
    int trail3;
}
