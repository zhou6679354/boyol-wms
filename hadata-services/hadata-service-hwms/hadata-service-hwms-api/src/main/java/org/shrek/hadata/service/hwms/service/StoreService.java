package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.hwms.model.QimenStoredInfo;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 07:12
 */
public interface StoreService {
    public List<HashMap<String, String>> queryStoreInfo(HashMap<String, String> params);

    public BaseResponse<HashMap<String, QimenStoredInfo>> queryQimenStoreInfo(String client, String whse, String itemCode);

}
