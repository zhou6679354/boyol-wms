package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.QimenStoredInfo;
import org.shrek.hadata.service.hwms.model.StoreItem;
import org.shrek.hadata.service.hwms.model.TStoredItem;

import java.util.HashMap;
import java.util.List;

public interface TStoredItemMapper extends BasicMapper<TStoredItem> {

    public List<HashMap<String, String>> selectStoredItem(HashMap<String,String> params);

    List<QimenStoredInfo> selectStoreInfo(HashMap<String, String> params);

    List<StoreItem> selectInventoryList(HashMap<String, String> params);

}