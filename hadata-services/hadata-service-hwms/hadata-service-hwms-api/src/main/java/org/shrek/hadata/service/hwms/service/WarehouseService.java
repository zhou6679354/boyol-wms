package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.service.hwms.model.TWhse;

/**
 * 查询仓库信息
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 10:50
 */
public interface WarehouseService {

    public String getWarehouseIdByCode(String code);

    public String getWarehouseCodeById(String whId);

    public TWhse getWarehouseByCode(String code);
}
