package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 18:16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KdniaoDistResponse {
    private String EBusinessID;
    private boolean Success;
    private String UpdateTime;
    private String Reason;
}
