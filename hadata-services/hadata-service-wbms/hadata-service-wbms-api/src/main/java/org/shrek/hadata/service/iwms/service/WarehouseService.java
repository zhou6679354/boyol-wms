package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.service.iwms.model.TWhse;
import org.shrek.hadata.service.iwms.model.TWhseControl;

/**
 * 查询仓库信息
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 10:50
 */
public interface WarehouseService {

    public TWhse getWarehouseByCode(String code);
    public TWhse getWarehouseByWhId(String whId);
	TWhse getWarehouseByCity(String city);
	int addWarehouse(TWhse whse);
    int addWarehouseControl(TWhseControl whseControl);
}
