package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.iwms.mapper.TStoredItemMapper;
import org.shrek.hadata.service.iwms.mapper.TZoneLocaMapper;
import org.shrek.hadata.service.iwms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 07:13
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseStoreServiceImpl implements StoreService {

    @Autowired
    TStoredItemMapper tStoredItemMapper;
    @Autowired
    TZoneLocaMapper tZonelocaMapper;


    @Override
    public BaseResponse<HashMap<String, Double>> queryCsfStoreInfo(String clientCode, String whse, String itemCode) {
        Example example = new Example(TStoredItem.class);
        //Iterable iterable = ;

        /*20190712 created For zone not pass
        List lstNotPassZone = Lists.newArrayList();
        lstNotPassZone.add("BJK");
        lstNotPassZone.add("SDC");

        Example zoneExample = new Example(TZoneLoca.class);
        zoneExample.createCriteria()
                .andEqualTo("wh_id",whse)
                .andIn("zone",lstNotPassZone);

        List<TZoneLoca> lstZoneLocas = tZonelocaMapper.selectByExample(zoneExample);

        List lstLocas = Lists.newArrayList();

        lstZoneLocas.forEach(zoneLoca ->{
                lstLocas.add(zoneLoca.getLocationId());
        });*/

        example.createCriteria()
                .andEqualTo("whId", whse)
                .andEqualTo("itemNumber", Joiner.on('-').join(clientCode, itemCode))
                .andGreaterThanOrEqualTo("type", 0);
                //.andNotIn("location_id",lstLocas);

        List<TStoredItem> tStoredItems = tStoredItemMapper.selectByExample(example);


        /*for (int i=0 ;i<tStoredItemsSrc.size();i++)
        {
            tStoredItemsSrc.get(i).
        }*/

        DoubleSummaryStatistics lockSunmary = tStoredItems.stream().filter(item -> item.getType() != 0L && item.getType() != 9L)
                .collect(Collectors.summarizingDouble(TStoredItem::getActualQty));
        DoubleSummaryStatistics unlockSunmary = tStoredItems.stream().filter(item -> item.getType() == 0L || item.getType() == 9L)
                .collect(Collectors.summarizingDouble(TStoredItem::getActualQty));
        HashMap<String, Double> out = Maps.newHashMap();
        out.put("lockSunmary", lockSunmary.getSum());
        out.put("unlockSunmary", unlockSunmary.getSum());
        return BaseResponse.success(out);
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
                qimenStoredInfo.setItemId(store.getItemId());
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

    @Override
    public List<StoreItem> queryQimenStoreInfo2(String client, String whse) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("wh_id", whse);
        params.put("client_code", client);
        List<StoreItem> storedInfos = tStoredItemMapper.selectInventoryList(params);
        return storedInfos;
    }
    @Override
    public List<TStoredItem> queryStoreItemList(String whse) {
        TStoredItem storeItem=new TStoredItem();
        storeItem.setWhId(whse);
        return tStoredItemMapper.selectByExample(storeItem);
    }
    @Override
    public BaseResponse<HashMap<String, ScfStoredBatchInfo>> queryCsfStoreBatchInfo(String clientCode, String whse, String itemCode) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("item_number", Joiner.on('-').join(clientCode, itemCode));
        params.put("wh_id", whse);
        params.put("client_code", clientCode);
        List<ScfStoredBatchInfo> storedInfos = tStoredItemMapper.selectStoreBatchInfo(params);
        HashMap<String, ScfStoredBatchInfo> storeMap = Maps.newHashMap();
        storedInfos.forEach(store -> {
            ScfStoredBatchInfo scfStoredInfo = storeMap.get(store.getMapKey());
            if (scfStoredInfo == null) {
                scfStoredInfo = new ScfStoredBatchInfo();
                scfStoredInfo.setItemCode(store.getItemCode());
                scfStoredInfo.setInventoryType(store.getInventoryType());
                scfStoredInfo.setBatchCode(store.getBatchCode()==null?"/":store.getBatchCode());
                scfStoredInfo.setProduceDate(store.getProduceDate());
                scfStoredInfo.setOverdueDate(store.getOverdueDate()==null?"/":store.getOverdueDate());
                scfStoredInfo.setGuaranteePeriod(store.getGuaranteePeriod());
                scfStoredInfo.setQuantity(store.getQuantity());
                scfStoredInfo.setLockQuantity(store.getLockQuantity());
            }
            storeMap.put(store.getMapKey(), scfStoredInfo);
        });
        return BaseResponse.success(storeMap);
    }
}
