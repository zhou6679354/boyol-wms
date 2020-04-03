package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TReceipt;
import org.shrek.hadata.service.iwms.model.TReceiptCount;

import java.util.List;

public interface TReceiptMapper extends BasicMapper<TReceipt> {
    public List<TReceiptCount> selectTReceiptInfo(TReceipt tReceipt);
    public List<TReceipt> selectTReceipt(TReceipt tReceipt);
    public List<TReceipt> selectTReceiptGroupByLocationId(TReceipt tReceipt);
}