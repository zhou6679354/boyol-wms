package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

@Data
public class DataResource {
    String orderno;
    String sku;
    String waveId;
    Double pkgs;
    boolean flag;
}
