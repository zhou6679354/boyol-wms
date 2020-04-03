package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TblInfoImpReceiptPop;



public interface TblInfoImpReceiptPopMapper extends BasicMapper<TblInfoImpReceiptPop> {
    void updateById(TblInfoImpReceiptPop tReceipt);
}
