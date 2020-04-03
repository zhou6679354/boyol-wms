package org.shrek.hadata.service.reiley.service.scf.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月09日 18:15
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KdniaoDistRequest {
    @JsonProperty("EBusinessID")
    private String EBusinessID;
    @JsonProperty("PushTime")
    private String pushTime;
    @JsonProperty("Count")
    private String count;
    @JsonProperty("Data")
    private List<KdniaoDistRequestData> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KdniaoDistRequestData {

        @JsonProperty("EBusinessID")
        private String eBusinessID;
        @JsonProperty("ShipperCode")
        private String shipperCode;
        @JsonProperty("LogisticCode")
        private String logisticCode;
        @JsonProperty("Success")
        private boolean success;
        @JsonProperty("Reason")
        private String reason;
        @JsonProperty("State")
        private String state;
        @JsonProperty("CallBack")
        private String callBack;
        @JsonProperty("Traces")
        private List<Trace> traces;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Trace {
            @JsonProperty("AcceptTime")
            private String acceptTime;
            @JsonProperty("AcceptStation")
            private String acceptStation;
            @JsonProperty("Remark")
            private String remark;
        }
    }
}
