package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.InBoundService;
import org.shrek.hadata.service.bus.web.model.StockInfo;
import org.shrek.hadata.service.bus.web.model.StorageResponse;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouwenheng
 * @date 2019年04月15日 11:05
 */
@Slf4j
@Service
public class StorageService {


    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;


    /**
     * 查询库存接口
     */
    public StorageResponse queryStock(){
        String message="";
        List<StockInfo> stockInfoList=new ArrayList<StockInfo>();
        try {
            List<TStoredItem> storedItems=inBoundService.queryTStoredItemByWhId("101");
            for(int x = 0; x < storedItems.size(); x++){
                StockInfo stockInfo=new StockInfo();
                stockInfo.setSku(storedItems.get(x).getItemNumber().substring(5));
                stockInfo.setBincode(storedItems.get(x).getLocationId());
                stockInfo.setPkgs(storedItems.get(x).getActualQty()-storedItems.get(x).getUnavailableQty());
                stockInfoList.add(stockInfo);
            }
            message = JacksonUtil.nonAlways().toJson(stockInfoList);
        }catch (Exception e){
            message="查询异常，异常原因："+e.getMessage();
        }
        return StorageResponse.success(message);
    }
    /**
     * 库存查询
     */
    public StorageResponse queryInfo(QueryCity queryCity){
        String message="";
        try {
            List<BigScreenStorck> bigScreenStorck=inBoundService.queryBigScreenStorck(queryCity);
            message = JacksonUtil.nonAlways().toJson(bigScreenStorck);
        }catch (Exception e){
            message="查询异常，异常原因："+e.getMessage();
        }
        return StorageResponse.success(message);
    }
    /**
     * 吞吐量
     */
    public StorageResponse queryThroughput(QueryCity queryCity){
        String message="";
        try {
            List<BigScreen> bigScreen=inBoundService.queryBigScreen(queryCity);
            message = JacksonUtil.nonAlways().toJson(bigScreen);
        }catch (Exception e){
            message="查询异常，异常原因："+e.getMessage();
        }
        return StorageResponse.success(message);
    }


    /**
     * 过账接口
     */
    public StorageResponse reconciliation(String waId){
        String message="";
        try {
            String allOutMsg=outBoundService.doAllPick("101",waId,"plily");
            if(allOutMsg.equals("总拣完成")){
                    String partOutMsg=outBoundService.doPartPick("101",waId);
                    if(partOutMsg.equals("分拣完成")){
                        log.info(partOutMsg+"波次号："+waId);
                        message = "该波次过账成功，波次号："+waId;
                    }else{
                        log.error(partOutMsg+"波次号："+waId);
                        message = "该波次过账异常分拣失败，波次号："+waId;
                    }
                }
        }catch (Exception e){
            message="该波次过账异常，波次号："+waId+"异常原因："+e.getMessage();
        }
        return StorageResponse.success(message);
    }
}
