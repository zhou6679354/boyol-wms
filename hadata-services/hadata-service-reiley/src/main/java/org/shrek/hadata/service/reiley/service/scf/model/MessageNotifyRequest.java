package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 18:15
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageNotifyRequest {


    private String msgCode;
    private String msgContent;
    private String bizKey;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private String ownerUserId;
        private String orderCode;
        private String orderType;
    }
}
