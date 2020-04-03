package org.shrek.hadata.service.iwms.mapper;

import org.apache.ibatis.annotations.Param;
import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TStoredItemMapper extends BasicMapper<TStoredItem> {

    public List<QimenStoredInfo> selectStoreInfo(Map<String, String> param);
    public List<StoreItem> selectInventoryList(HashMap<String, String> params);
    public List<TStoredCount> queryTStoredItemList(@Param("whId")String whId);
    public List<ScfStoredBatchInfo> selectStoreBatchInfo(Map<String, String> param);
    public List<TStoredItem> queryTStoredItemByItemNumber(TStoredItem storedItem);
    public List<TStoredItem> queryStoredItemByWhId(@Param("list")List<String> list);
}