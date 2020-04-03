package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.MaterielService;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.shrek.hadata.service.bus.web.model.PickOrder;
import org.shrek.hadata.service.bus.web.model.StorageStockReturn;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhouwenheng
 * @version 1.0
 * @date 2019年04月15日 11:05
 */
@Slf4j
@Component
public class StorageScheduled {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    @Value("${storage.inOrder.url}")
    public  String  inOrderUrl;
    @Value("${storage.outOrder.url}")
    public  String outOrderUrl;
    @Value("${storage.storck.url}")
    public  String storckUrl;



    /**
     * 出库单创建
     */
    public void stockPicking()  {
        List<StorageStock> pickOrder = outBoundService.queryStorageStockByWhId("101","7522");
        PickOrder outOrder=new PickOrder();
        List<PickOrder.Data> dataList=new ArrayList<PickOrder.Data>();
        List<String> orderNumber=new ArrayList<String>();
        for (int x = 0; x < pickOrder.size(); x++) {
            try {
            if(!orderNumber.contains(pickOrder.get(x).getOrderNumber())){
                orderNumber.add(pickOrder.get(x).getOrderNumber());
            }
                PickOrder.Data data=new PickOrder.Data();
                int plannedQuantity=(int) pickOrder.get(x).getPlannedQuantity();//总数量
                TItemUom itemUompl=materielService.getItemUomByWhIdandItemNumberAndUom("101",pickOrder.get(x).getItemNumber(),"PL");
                TItemUom itemUomcs=materielService.getItemUomByWhIdandItemNumberAndUom("101",pickOrder.get(x).getItemNumber(),"CS");
                int conversionFactorpl=itemUompl.getConversionFactor().intValue();//托规
                int conversionFactorcs=itemUomcs.getConversionFactor().intValue();//箱规
                if(conversionFactorpl>0){
                    data.setPltNum(Math.floor(plannedQuantity/conversionFactorpl));//托数向下取整
                }
                if(conversionFactorcs>0){
                    data.setOrdBoxQty(Math.floor( plannedQuantity/conversionFactorcs));//箱数向下取整
                    data.setBoxNum(Math.floor(Math.floorMod(plannedQuantity,conversionFactorpl)/conversionFactorcs));//除去整托后箱数
                    data.setOddQty(Math.floorMod(plannedQuantity,conversionFactorcs));//除去箱数后散件
                }
                data.setSku(pickOrder.get(x).getItemNumber().substring(5));
                data.setOrdPcsQty(pickOrder.get(x).getPlannedQuantity());
                data.setOrderno(pickOrder.get(x).getOrderNumber());
                data.setCustcode(pickOrder.get(x).getClientCode());
                data.setTrway(pickOrder.get(x).getShipToCity());
                data.setWaveId(pickOrder.get(x).getWaveId());
                data.setBincode(pickOrder.get(x).getLocationId());
                data.setPalletSpecs(conversionFactorpl);
                dataList.add(data);
            }catch (Exception e){
                log.error("出库单拣货计划回传定时任务失败:"+e.getMessage()+pickOrder.get(x).getOrderNumber());
            }
        }
        if(dataList.size()>0){
            outOrder.setData(dataList);
            outOrder.setReqcode(UUID.randomUUID().toString());
            String json = JacksonUtil.nonAlways().toJson(outOrder);
            log.info("出库单拣货计划回传发送的JSON串为"+json);
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                Request request = new Request.Builder().url(outOrderUrl).post(body).build();
                Response response = client.newCall(request).execute();
                String responses = response.body().string();
                JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
                HashMap results = js.fromJson(responses, HashMap.class);
                if ("0".equals(results.get("code"))) {
                    for(int y = 0; y < orderNumber.size(); y++){
                        outBoundService.updateTPickDetailByOrderNumberAndWhId(orderNumber.get(y),"101");
                        log.info("出库单拣货计划发送成功，单号："+orderNumber.get(y));
                    }
                } else if("89".equals(results.get("code"))){
                    log.error("出库单拣货计划发送部分失败,失败报文："+results.get("message").toString());
                }else{
                    log.error("出库单拣货计划发送全部失败,发送报文："+json);
                }
            } catch (Exception e) {
                log.error("出库拣货回传失败,失败原因"+e.getMessage());
            }
        }
    }


    public void cancleWaId() {
        List<String> waId = outBoundService.queryWaIdByWhId("101","0","9","A");
        for (int x = 0; x < waId.size(); x++) {
            log.info("取消拣货计划请求开始，波次号："+waId.get(x));
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), waId.get(x));
                Request request = new Request.Builder().url(outOrderUrl).post(body).build();
                Response response = client.newCall(request).execute();
                String responses = response.body().string();
                JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
                HashMap results = js.fromJson(responses, HashMap.class);
                if ("0".equals(results.get("code"))) {
                    outBoundService.updateWaIdStatusTrue(waId.get(x),"101","A","1");
                    log.info("取消拣货计划请求通过，波次号："+waId.get(x));
                    String result=outBoundService.doCancleWave("101",waId.get(x),"plily");
                    if(result.equals("取消完成")){
                        outBoundService.updateWaveCancelStatusDoing(waId.get(x),"101","A","1","取消波次完成");
                        log.info("取消拣货计划完成，波次号："+waId.get(x));
                        outBoundService.updateWaIdStatusTrue(waId.get(x),"101","A","0");
                        log.info("重置取消拣货计划同步状态，波次号："+waId.get(x));
                    }
                } else {
                    outBoundService.updateWaIdStatusTrue(waId.get(x),"101","A","1");
                    outBoundService.updateWaveCancelStatusDoing(waId.get(x),"101","A","2","取消波次失败，请求未通过");
                    log.error("取消拣货计划请求未通过，波次号："+waId.get(x)+"未通过原因："+results.get("message").toString());
                }
            } catch (Exception e) {
                log.error("取消拣货计划失败,失败原因"+e.getMessage());
            }
        }
    }

}