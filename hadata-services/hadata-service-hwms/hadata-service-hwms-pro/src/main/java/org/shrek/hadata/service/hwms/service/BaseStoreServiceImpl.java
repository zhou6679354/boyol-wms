package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.hwms.mapper.TStoredItemMapper;
import org.shrek.hadata.service.hwms.model.QimenStoredInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 07:13
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseStoreServiceImpl implements StoreService {

    @Autowired
    TStoredItemMapper tStoredItemMapper;

    @Override
    public List<HashMap<String, String>> queryStoreInfo(HashMap<String, String> params) {
        List<HashMap<String, String>> res = tStoredItemMapper.selectStoredItem(params);
        res = Optional.ofNullable(res).orElseGet(() -> Lists.newArrayList());
        return res;
    }

    @Override
    public BaseResponse<HashMap<String, QimenStoredInfo>> queryQimenStoreInfo(String client, String whse, String itemCode) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("item_number", itemCode);
        params.put("wh_id", whse);
        params.put("client_code", client);
        List<QimenStoredInfo> storedInfos = tStoredItemMapper.selectStoreInfo(params);
        HashMap<String, QimenStoredInfo> storeMap = Maps.newHashMap();
        storedInfos.forEach(store -> {
            QimenStoredInfo qimenStoredInfo = storeMap.get(store.getMapKey());
            if (qimenStoredInfo == null) {
                qimenStoredInfo = new QimenStoredInfo();
                qimenStoredInfo.setWarehouseCode(store.getWarehouseCode());
                qimenStoredInfo.setItemCode(store.getItemCode());
                qimenStoredInfo.setInventoryType(store.getInventoryType());
                qimenStoredInfo.setBatchCode(store.getBatchCode());
                qimenStoredInfo.setProductDate(store.getProductDate());
                qimenStoredInfo.setExpireDate(store.getExpireDate());
                qimenStoredInfo.setProduceCode(store.getProduceCode());
                qimenStoredInfo.setQuantity(0D);
                qimenStoredInfo.setLockQuantity(0D);
            }
            if (store.getInventoryType() == 0L || store.getInventoryType() == 9L) {
                qimenStoredInfo.setQuantity(qimenStoredInfo.getQuantity() + store.getQuantity());
            } else {
                qimenStoredInfo.setLockQuantity(qimenStoredInfo.getLockQuantity() + store.getQuantity());
            }
            storeMap.put(store.getMapKey(), qimenStoredInfo);
        });

        return BaseResponse.success(storeMap);
    }

}
