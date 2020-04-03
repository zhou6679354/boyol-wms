package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.SZSignUtil;
import org.shrek.hadata.service.bus.web.model.SZReturnInOrder;
import org.shrek.hadata.service.bus.web.model.SZReturnOutOrder;
import org.shrek.hadata.service.bus.web.model.SZStockRequest;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.InBoundService;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class SZScheduled {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    private static String SZSECRECT="MDVhYThjMzctNjAxNC00YWJkLThjMzktM2VkNTFlZTZhZmYy";
    private static String SID="2";
    private static String APPID="ZC00M2MwLThhOTgtZGEyODZiOTU3NjAx";
    /**
     * 库存查询
     * @param
     * @return
     */
    public void StockQuery(){
        String stockQueryUrl = "http://shareetech.synology.me:9300/api/StockNotify";
        try {
            List<String> list=new ArrayList<String>();
            list.add("110");
            list.add("111");
            list.add("112");
            list.add("113");
            List<TStoredItem> storedItem = inBoundService.queryStoredItemByWhId(list);
            if(storedItem.size()>0){
                List<SZStockRequest> stockRequests=new ArrayList<SZStockRequest>();
                for (int y = 0; y < storedItem.size(); y++) {
                    SZStockRequest stockRequest = new SZStockRequest();
                    stockRequest.setBatchCode(storedItem.get(y).getLotNumber());
                    stockRequest.setItemCode(storedItem.get(y).getItemNumber().substring(5));
                    stockRequest.setQty(storedItem.get(y).getActualQty());
                    stockRequest.setStore(storedItem.get(y).getWhId());
                    stockRequest.setType(storedItem.get(y).getType());
                    stockRequest.setExpirationDate(storedItem.get(y).getExpirationDate());
                    stockRequest.setPreScanCust(storedItem.get(y).getReservedFor());
                    stockRequests.add(stockRequest);
                }
                Date date=new Date();
                UUID uuid=UUID.randomUUID();
                String jsonStr = JacksonUtil.nonEmpty().toJson(stockRequests);
                String signStr= SZSignUtil.signSZRequest(jsonStr,SID,APPID,String.valueOf(date.getTime()),uuid.toString(),SZSECRECT);
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
                Request request = new Request.Builder()
                        .url(stockQueryUrl)
                        .post(body)
                        .addHeader("sid",SID)
                        .addHeader("appid",APPID)
                        .addHeader("nonce",uuid.toString())
                        .addHeader("timestamp",String.valueOf(date.getTime()))
                        .addHeader("signature",signStr)
                        .build();
                Response response = client.newCall(request).execute();
                String responses = response.body().string();
                JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
                HashMap results = js.fromJson(responses, HashMap.class);
                if(results.get("IsSuccess").toString().equals("true")){
                    log.info("SZ库存数据回传成功,报文："+jsonStr);
                }else{
                    log.error("SZ库存数据回传失败,失败原因："+results.get("Message"));
                }
            }
        }catch (Exception e){
            log.info("SZ库存数据回传失败,原因"+e.getMessage());
        }
    }
    /**
     * 入库单反馈
     * @param
     * @return
     */
    public void inOrderReturn(){
        String inOrderUrl = "http://shareetech.synology.me:9300/api/StockInNotify";
        try {
            List<String> list=new ArrayList<String>();
            list.add("110");
            list.add("111");
            list.add("112");
            list.add("113");
            List<TPoMaster> poMaster=inBoundService.queryConfirmInBoundsByWhses(list);
            for (int y = 0; y < poMaster.size(); y++) {
                List<TReceipt> receipts=inBoundService.queryTReceiptByPoNumberAndWhId(poMaster.get(y).getPoNumber(),poMaster.get(y).getWhId());
                if(receipts.size()>0){
                    SZReturnInOrder returnInOrder=new SZReturnInOrder();
                    returnInOrder.setB_no(poMaster.get(y).getPoNumber());
                    returnInOrder.setBillTypeId(poMaster.get(y).getTypeId());
                    returnInOrder.setBillDate(poMaster.get(y).getCreateDate());
                    returnInOrder.setClient(poMaster.get(y).getClientCode());
                    returnInOrder.setStore(poMaster.get(y).getWhId());
                    returnInOrder.setNote(poMaster.get(y).getServiceLevel());
                    List<SZReturnInOrder.DetailsItem> details=new ArrayList<SZReturnInOrder.DetailsItem>();
                    for (int i = 0; i < poMaster.get(y).getDetailList().size(); i++) {
                        SZReturnInOrder.DetailsItem dtl=new SZReturnInOrder.DetailsItem();
                        for(int x = 0; x < receipts.size(); x++){
                            if(poMaster.get(y).getDetailList().get(i).getLineNumber().equals(receipts.get(x).getLineNumber())){
                                dtl.setLineNumber(receipts.get(x).getLineNumber());
                                dtl.setBatchCode(receipts.get(x).getLotNumber());
                                if(receipts.get(x).getExpirationDate()!=null){
                                    dtl.setExpiryDate(receipts.get(x).getExpirationDate());
                                }
                            }
                        }
                        dtl.setB_no(poMaster.get(y).getPoNumber());
                        dtl.setSkuCode(poMaster.get(y).getDetailList().get(i).getItemNumber().substring(5));
                        if(StringUtils.isNotBlank(poMaster.get(y).getDetailList().get(i).getSoNumber())){
                            dtl.setPreScanned("是");
                            dtl.setPreScanCust(poMaster.get(y).getDetailList().get(i).getSoNumber());
                        }
                        dtl.setBaseQuantity(poMaster.get(y).getDetailList().get(i).getQty());
                        dtl.setQuantity(Integer.valueOf(poMaster.get(y).getDetailList().get(i).getStCode()));
                        dtl.setMaterialType(poMaster.get(y).getDetailList().get(i).getStName());
                        dtl.setNoteDtl(poMaster.get(y).getDetailList().get(i).getRemark());
                        details.add(dtl);
                    }
                    returnInOrder.setDetails(details);
                    Date date=new Date();
                    UUID uuid=UUID.randomUUID();
                    String jsonStr = JacksonUtil.nonEmpty().toJson(returnInOrder);
                    String signStr= SZSignUtil.signSZRequest(jsonStr,SID,APPID,String.valueOf(date.getTime()),uuid.toString(),SZSECRECT);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
                    Request request = new Request.Builder()
                            .url(inOrderUrl)
                            .post(body)
                            .addHeader("sid",SID)
                            .addHeader("appid",APPID)
                            .addHeader("nonce",uuid.toString())
                            .addHeader("timestamp",String.valueOf(date.getTime()))
                            .addHeader("signature",signStr)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responses = response.body().string();
                    JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
                    HashMap results = js.fromJson(responses, HashMap.class);
                    if(results.get("IsSuccess").toString().equals("true")){
                        inBoundService.updateInBoundsBack(poMaster.get(y).getWhId(), poMaster.get(y).getPoNumber(),"1","回传成功",date);
                        log.info("入库数据回传成功，单号："+poMaster.get(y).getPoNumber());
                    }else{
                        inBoundService.updateInBoundsUnback(poMaster.get(y).getWhId(), poMaster.get(y).getPoNumber(),"0",results.get("Message").toString(),date);
                        log.error("入库数据回传失败，单号："+poMaster.get(y).getPoNumber()+"失败原因："+results.get("Message"));
                    }
                }
            }
        }catch (Exception e){
            log.error("入库数据回传失败,原因"+e.getMessage());
        }
    }
    /**
     * 出库单反馈
     * @param
     * @return
     */
    public void outOrderReturn(){
        String outOrderUrl = "http://shareetech.synology.me:9300/api/StockOutNotify";
        try {
            List<String> list=new ArrayList<String>();
            list.add("110");
            list.add("111");
            list.add("112");
            list.add("113");
            List<TOrder> orders=outBoundService.queryConfirmOutBoundsByWhses(list);
            for (int y = 0; y < orders.size(); y++) {
                List<TPickDetail> pickDetails=outBoundService.queryTPickDetailByWhIdAndOrderNumber(orders.get(y).getWhId(),orders.get(y).getOrderNumber());
                if(pickDetails.size()>0){
                    SZReturnOutOrder returnOutOrder=new SZReturnOutOrder();
                    returnOutOrder.setB_no(orders.get(y).getOrderNumber());
                    returnOutOrder.setBillTypeId(orders.get(y).getTypeId());
                    returnOutOrder.setBillDate(orders.get(y).getCreateDate());
                    returnOutOrder.setClient(orders.get(y).getClientCode());
                    returnOutOrder.setStore(orders.get(y).getWhId());
                    returnOutOrder.setNote(orders.get(y).getServiceLevel());
                    List<SZReturnOutOrder.DetailsItem> details=new ArrayList<SZReturnOutOrder.DetailsItem>();
                    for (int i = 0; i < orders.get(y).getOrderDetailList().size(); i++) {
                        SZReturnOutOrder.DetailsItem dtl=new SZReturnOutOrder.DetailsItem();
                            for(int x = 0; x < pickDetails.size(); x++){
                                if(orders.get(y).getOrderDetailList().get(i).getLineNumber().equals(pickDetails.get(x).getLineNumber())){
                                    dtl.setLineNumber(pickDetails.get(x).getLineNumber());
                                    dtl.setBatchCode(pickDetails.get(x).getLotNumber());
                                }
                            }
                            if(orders.get(y).getOrderDetailList().get(i).getExpirationDate()!=null){
                                dtl.setExpiryDate(orders.get(y).getOrderDetailList().get(i).getExpirationDate());
                            }
                        dtl.setB_no(orders.get(y).getOrderNumber());
                        dtl.setSkuCode(orders.get(y).getOrderDetailList().get(i).getItemNumber().substring(5));
                        if(StringUtils.isNotBlank(orders.get(y).getOrderDetailList().get(i).getCustPart())){
                            dtl.setPreScanned("是");
                            dtl.setPreScanCust(orders.get(y).getOrderDetailList().get(i).getCustPart());
                        }
                        dtl.setBaseQuantity(orders.get(y).getOrderDetailList().get(i).getQty());
                        dtl.setQuantity(Integer.valueOf(orders.get(y).getOrderDetailList().get(i).getStCode()));
                        dtl.setMaterialType(orders.get(y).getOrderDetailList().get(i).getStName());
                        dtl.setNoteDtl(orders.get(y).getOrderDetailList().get(i).getItemDescription());
                        details.add(dtl);
                    }
                    returnOutOrder.setDetails(details);
                    Date date=new Date();
                    UUID uuid=UUID.randomUUID();
                    String jsonStr = JacksonUtil.nonEmpty().toJson(returnOutOrder);
                    String signStr= SZSignUtil.signSZRequest(jsonStr,SID,APPID,String.valueOf(date.getTime()),uuid.toString(),SZSECRECT);
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
                    Request request = new Request.Builder()
                            .url(outOrderUrl)
                            .post(body)
                            .addHeader("sid",SID)
                            .addHeader("appid",APPID)
                            .addHeader("nonce",uuid.toString())
                            .addHeader("timestamp",String.valueOf(date.getTime()))
                            .addHeader("signature",signStr)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responses = response.body().string();
                    JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
                    HashMap results = js.fromJson(responses, HashMap.class);
                    if(results.get("IsSuccess").toString().equals("true")){
                        outBoundService.updateOutBoundsBackTrue(orders.get(y).getOrderId(),"1","回传成功",date);
                        log.info("出库数据回传成功，单号："+orders.get(y).getOrderNumber());
                    }else{
                        outBoundService.updateOutBoundsBackFlase(orders.get(y).getOrderId(),"0",results.get("Message").toString(),date);
                        log.error("出库数据回传失败，单号："+orders.get(y).getOrderNumber()+"失败原因："+results.get("Message"));
                    }
                }
            }
        }catch (Exception e){
            log.error("出库数据回传失败,原因"+e.getMessage());
        }
    }
}
