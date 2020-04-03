package org.shrek.hadata.service.reiley.service.waybill.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月10日 10:06
 */
@Data
public class WaybillPrintDto {
    String cmd;
    @JsonProperty(value = "requestID")
    String id;
    String version;
    WaybillPrintTask task;

    @Data
    public static class WaybillPrintTask {
        @JsonProperty(value = "taskID")
        String id;
        boolean preview = false;
        String printer;
        String previewType;
        int firstDocumentNumber;
        int totalDocumentCount;
        List<WaybillPrintTaskDocument> documents;
    }
    @Data
    public static class WaybillPrintTaskDocument {
        @JsonProperty(value = "documentID")
        String id;
        List<WaybillPrintContentDto> contents;
    }
}
