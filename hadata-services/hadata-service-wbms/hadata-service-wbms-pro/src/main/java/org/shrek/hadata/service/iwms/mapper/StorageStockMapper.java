package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.*;

import java.util.List;

public interface StorageStockMapper extends BasicMapper<StorageStock> {
    public List<StorageStock> queryStorageStockByWhId(TOrder order);
    public List<TblOrderSynWisdom> queryTblOrderSynWisdomByWhId(TblOrderSynWisdom tblOrderSynWisdom);
    public List<TblItemSynWisdom> getTblItemSynWisdom(TblItemSynWisdom tblItemSynWisdom);
    public List<TStoredItem> queryTStoredItemByWhId(TStoredItem storedItem);
}
