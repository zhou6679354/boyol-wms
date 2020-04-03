package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.iwms.model.QimenStoredInfo;
import org.shrek.hadata.service.iwms.model.ScfStoredBatchInfo;
import org.shrek.hadata.service.iwms.model.StoreItem;
import org.shrek.hadata.service.iwms.model.TStoredItem;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 07:12
 */
public interface StoreService {
    public BaseResponse<HashMap<String, Double>> queryCsfStoreInfo(String client, String whse, String item);
    public BaseResponse<HashMap<String, QimenStoredInfo>> queryQimenStoreInfo(String client, String whse, String item);
    public List<StoreItem> queryQimenStoreInfo2(String client, String whse);
    public BaseResponse<HashMap<String, ScfStoredBatchInfo>> queryCsfStoreBatchInfo(String client, String whse, String item);
    public List<TStoredItem> queryStoreItemList(String whse);
}
