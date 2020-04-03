package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 16:48
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MassiveStockOutReponse {
    private boolean success;
    private String errorCode;
    private String errorMsg;
}
