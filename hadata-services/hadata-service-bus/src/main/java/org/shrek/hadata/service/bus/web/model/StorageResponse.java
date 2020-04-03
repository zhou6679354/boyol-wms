package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

/**
 * @author zhouwenheng
 * @version 1.0
 * @date 2019年04月15日 11:05
 */
@Data
public class StorageResponse {
   private String  flag;
   private String code;
   private String message;

    public static StorageResponse success(String message) {
        StorageResponse r = new StorageResponse();
        r.setFlag("success");
        r.setCode("0");
        r.setMessage(message);
        return r;
    }

    public static StorageResponse fail(String message) {
        StorageResponse r = new StorageResponse();
        r.setFlag("failure");
        r.setCode("1");
        r.setMessage(message);
        return r;
    }
}
