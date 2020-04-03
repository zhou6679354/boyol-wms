package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.service.iwms.model.TClientRule;
import org.shrek.hadata.service.iwms.model.TblOrderTypeMapping;

public interface OrderTypeService {
    public int addOrderType(TblOrderTypeMapping tblOrderTypeMapping);
}
