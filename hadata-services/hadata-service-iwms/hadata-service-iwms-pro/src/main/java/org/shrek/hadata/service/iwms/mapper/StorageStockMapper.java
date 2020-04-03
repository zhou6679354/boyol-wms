package org.shrek.hadata.service.iwms.mapper;

import org.apache.ibatis.annotations.Param;
import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.*;

import java.util.List;
import java.util.Map;

public interface StorageStockMapper extends BasicMapper<StorageStock> {
    public List<StorageStock> queryStorageStockByWhId(Map map);
    public List<TblItemSynWisdom> getTblItemSynWisdom(TblItemSynWisdom tblItemSynWisdom);
    public List<TStoredItem> queryTStoredItemByWhId(TStoredItem storedItem);

}
