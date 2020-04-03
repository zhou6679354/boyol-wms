package org.shrek.hadata.service.reiley.service.waybill.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月10日 11:34
 */
@Data
public class WaybillPrintContentDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String signature;
    @JsonProperty(value = "templateURL")
    String template;

    WaybillPrintDataDto data;
}
