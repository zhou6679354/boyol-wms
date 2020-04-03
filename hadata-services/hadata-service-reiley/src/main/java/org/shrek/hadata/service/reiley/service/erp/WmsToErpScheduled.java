package org.shrek.hadata.service.reiley.service.erp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.WebServiceUtil;
import org.shrek.hadata.service.hwms.model.TZoneLoca;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.InBoundService;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.shrek.hadata.service.iwms.service.StoreService;
import org.shrek.hadata.service.reiley.service.erp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月13日 12:48
 */
@Slf4j
@Component
public class WmsToErpScheduled {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 50000)
    InBoundService inBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 150000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 50000)
    StoreService storeService;


    /**
     * test port is 8289,changed to 9001
     */
    public static final WebServiceUtil webServiceUtil=  new WebServiceUtil("http://bokesoft.com/dee/service/",
                "http://180.167.196.116:9001/BokeDee/services/deeSpServiceWrapped?wsdl",
                "BokeDeeServiceWSService", "deeSpServiceWrapped",
                "deeServiceHv", "deeServiceHvResponse");

    public static final WebServiceUtil webServiceUtil_YZ=  new WebServiceUtil("http://bokesoft.com/dee/service/",
            "http://180.167.196.116:9002/BokeDee/services/deeSpServiceWrapped?wsdl",
            "BokeDeeServiceWSService", "deeSpServiceWrapped",
            "deeServiceHv", "deeServiceHvResponse");

    public void feedbackInBoundResult() {
        InBoundResult inBoundResult = new InBoundResult();
        inBoundResult.setBillID("");
        inBoundResult.setBillTypeID("");
        inBoundResult.setCompanyCode("");
        inBoundResult.setB_No("");
        inBoundResult.setBilllDate(new Date());
        inBoundResult.setSupplyID("");
        inBoundResult.setNote("");
        inBoundResult.setReqDate(new Date());
        inBoundResult.setReqFrom("WMSTEST");
        inBoundResult.setReqId(UUID.randomUUID().toString());

        InBoundResult.InBoundResultDetail inBoundResultDetail1 = new InBoundResult.InBoundResultDetail();
        inBoundResultDetail1.setBillDtlID("");
        inBoundResultDetail1.setSKUCode("");
        inBoundResultDetail1.setBatchCode("");
        inBoundResultDetail1.setMakeDate(new Date());
        inBoundResultDetail1.setBaseQuantity(1);
        inBoundResultDetail1.setQuantity(1);
        inBoundResultDetail1.setMaterialType("");
        inBoundResultDetail1.setStorageLocation("");
        inBoundResultDetail1.setNotedtl("");
        InBoundResult.InBoundResultDetail inBoundResultDetail2 = new InBoundResult.InBoundResultDetail();
        inBoundResultDetail2.setBillDtlID("");
        inBoundResultDetail2.setSKUCode("");
        inBoundResultDetail2.setBatchCode("");
        inBoundResultDetail2.setMakeDate(new Date());
        inBoundResultDetail2.setBaseQuantity(1);
        inBoundResultDetail2.setQuantity(1);
        inBoundResultDetail2.setMaterialType("");
        inBoundResultDetail2.setStorageLocation("");
        inBoundResultDetail2.setNotedtl("");
        inBoundResult.setDetails(Lists.newArrayList(inBoundResultDetail1, inBoundResultDetail2));
        String json = JacksonUtil.nonAlways().toJson(inBoundResult);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("xml",json);
        params.put("action", "AddStockIn");
        try {
            String ret = webServiceUtil.sendSoap11Message(params);
            System.out.println(ret.toString()); // 没有对结果做处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 入库
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackInBoundResponse() {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if((!tClients.isEmpty()) && tClients.size() >0 ){
            String clientCode = "";
            String whse = "";
            for(int x=0;x<tClients.size();x++){
                clientCode=tClients.get(x).getClientCode();
                whse = tClients.get(x).getWhId();
                List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByCodeWhse(clientCode, whse);
                for(int i=0;i<tPoMasters.size();i++){
                    TPoMaster tPoMaster = tPoMasters.get(i);
                    ErpInBoundResponse erpInBoundResponse = new ErpInBoundResponse();
                    erpInBoundResponse.setBillID(tPoMaster.getPoNumber());
                    if (tPoMaster.getTypeId().toString().equals("2453")) {
                        erpInBoundResponse.setBillTypeID("入库");
                    }else if (tPoMaster.getTypeId().toString().equals("1121")) {
                        erpInBoundResponse.setBillTypeID("销售退货入库");
                    } else if (tPoMaster.getTypeId().toString().equals("1119")) {
                        erpInBoundResponse.setBillTypeID("采购入库");
                    }  else {
                        erpInBoundResponse.setBillTypeID("入库");
                    }
                    erpInBoundResponse.setCompanyCode("");//客户
                    erpInBoundResponse.setSupplyID("");//非必填 传过 没存表
                    erpInBoundResponse.setB_No(tPoMaster.getPoNumber());
                    erpInBoundResponse.setBilllDate(tPoMaster.getCreateDate());//不确定
                    erpInBoundResponse.setNote("");//非必填 传过 没存表
                    List<ErpInBoundResponse.InBoundResponseDetail> inBoundResponseDetails = Lists.newArrayList();
                    for (int j = 0; j < tPoMasters.get(i).getDetailList().size(); j++) {
                        TPoDetail detail = tPoMasters.get(i).getDetailList().get(j);
                        ErpInBoundResponse.InBoundResponseDetail inBoundResponseDetail = new ErpInBoundResponse.InBoundResponseDetail();
                        inBoundResponseDetail.setBaseQuantity(detail.getActualQty().intValue()); //基本数量
                        inBoundResponseDetail.setQuantity(0);//实收箱数
                        inBoundResponseDetail.setBillDtlID(detail.getSoLineNumber());
                        inBoundResponseDetail.setBatchCode(detail.getLotNumber2());//批次号
                        inBoundResponseDetail.setExpirydate(detail.getExpirationDate2());
                        inBoundResponseDetail.setMakeDate(new Date());//非必填
                        inBoundResponseDetail.setMaterialType(detail.getOriginator());//传过 没存表  SP商品 YP样品 ZP赠品
                        inBoundResponseDetail.setStore(tPoMaster.getWhId());
                        inBoundResponseDetail.setClient(tPoMaster.getClientCode());
                        String zone = inBoundService.queryConfirmInBoundsByZone(detail.getLocationId(), tPoMaster.getWhId());
                        inBoundResponseDetail.setZone(zone);
                        String type = inBoundService.queryConfirmInBoundsByItemAndWhse(detail.getItemNumber(), tPoMaster.getWhId());
                        inBoundResponseDetail.setType(type);
                        inBoundResponseDetail.setNotedtl("");//备注 非必填
                        inBoundResponseDetail.setPreScanCust(detail.getSoNumber()==null?"":detail.getSoNumber().toString());
                        if (detail.getSpecialProcessing()==null)
                            inBoundResponseDetail.setPreScanned("N");
                        else
                            inBoundResponseDetail.setPreScanned(detail.getSpecialProcessing().toString().equals("YES")?"Y":"N");
                        inBoundResponseDetail.setReqDate(new Date());//非必填
                        inBoundResponseDetail.setReqFrom("WMSTEST");
                        inBoundResponseDetail.setReqId("WMSTEST"); //UUID.randomUUID().toString()
                        inBoundResponseDetail.setStorageLocation(tPoMaster.getClientCode());
                        inBoundResponseDetail.setSKUCode(detail.getItemNumber().replace(tPoMaster.getClientCode()+'-',""));
                        inBoundResponseDetails.add(inBoundResponseDetail);
                    }
                    erpInBoundResponse.setGrid(inBoundResponseDetails);
                    String json = JacksonUtil.nonAlways().toJson(erpInBoundResponse);
                    log.info("入库回传报文：" + json);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("xml",json);
                    params.put("action", "AddStockIn");
                    try {

                        String ret = "";
                        if("16003".equals(clientCode) || "16043".equals(clientCode)) {
                            ret = webServiceUtil_YZ.sendSoap11Message(params);
                        }
                        else {
                            ret = webServiceUtil.sendSoap11Message(params);
                        }
                        log.info("入库报文回传返回：" + ret);
                        List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                        if("1".equals(result.get(0).get("Status").toString())){
                            inBoundService.updateInBoundsBack(tPoMaster.getWhId(), tPoMaster.getPoNumber(),"1","RL单据回传成功",new Date());
                        }else{
                            inBoundService.updateInBoundsUnback(tPoMaster.getWhId(), tPoMaster.getPoNumber(),"0","RL单据回传失败",new Date());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 出库
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackOutBoundResponse() {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if((!tClients.isEmpty()) && tClients.size() >0 ) {
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                whse = tClients.get(x).getWhId();
                List<TOrder> tOrders = outBoundService.queryConfirmOutBoundsByWhse(whse);//查询条件为WhId
                for(int i=0;i<tOrders.size();i++){
                    TOrder tOrder = tOrders.get(i);
                    if(tOrder.getOrderConfirmList().size() > 0) {
                        if (tOrder.getOrderNumber().startsWith("3") || tOrder.getOrderNumber().startsWith("4") || tOrder.getOrderNumber().startsWith("5") || tOrder.getOrderNumber().startsWith("6") || tOrder.getOrderNumber().startsWith("7") || tOrder.getOrderNumber().startsWith("8") || tOrder.getOrderNumber().startsWith("9")) {
                            ErpOutBoundResponse erpOutBoundResponse = new ErpOutBoundResponse();
                            erpOutBoundResponse.setBillID(tOrder.getOrderNumber());
                            if (tOrder.getTypeId().toString().equals("2456")) {
                                erpOutBoundResponse.setBillTypeID("出库");
                            } else if (tOrder.getTypeId().toString().equals("1153")) {
                                erpOutBoundResponse.setBillTypeID("采购退货出库");
                            } else {
                                erpOutBoundResponse.setBillTypeID("其它");
                            }
                            erpOutBoundResponse.setCompanyCode("");//客户
                            erpOutBoundResponse.setSupplyID("");//非必填 传过 没存表
                            erpOutBoundResponse.setBilllDate(tOrder.getOrderDate());
                            erpOutBoundResponse.setNote("");//非必填 传过 没存表
                            erpOutBoundResponse.setB_No(tOrder.getOrderNumber());
                            List<ErpOutBoundResponse.OutBoundResponseDetail> outBoundResponseDetails = Lists.newArrayList();
                            for (int j = 0; j < tOrders.get(i).getOrderConfirmList().size(); j++) {
                                TOrderConfirm orderConfirm = tOrders.get(i).getOrderConfirmList().get(j);
                                ErpOutBoundResponse.OutBoundResponseDetail outBoundResponseDetail = new ErpOutBoundResponse.OutBoundResponseDetail();
                                outBoundResponseDetail.setBaseQuantity((int) (Double.valueOf(orderConfirm.getTranQty()).doubleValue())); //基本数量
                                outBoundResponseDetail.setQuantity(0);//实收箱数
                                outBoundResponseDetail.setBillDtlID(orderConfirm.getLineNumber());
                                outBoundResponseDetail.setBatchCode(orderConfirm.getLotNumber());//批次号
                                outBoundResponseDetail.setMakeDate(orderConfirm.getProdDate());//非必填
                                TOrderDetail tOrderDetail = outBoundService.queryConfirmOutBoundsByWhseAndNoAndId(orderConfirm.getLineNumber(), tOrder.getOrderNumber(), tOrder.getWhId());
                                String materialType = new String();
                                String storageType = new String();
                                String custPart = new String();
                                if (tOrderDetail != null) {
                                    materialType = (tOrderDetail.getBoDescription() == null ? "" : tOrderDetail.getBoDescription());
                                    storageType = tOrderDetail.getStorageType().toString() == null ? "" : tOrderDetail.getStorageType().toString();
                                    custPart = tOrderDetail.getCustPart() == null ? "" : tOrderDetail.getCustPart();
                                }

                                outBoundResponseDetail.setMaterialType(materialType);//传过 没存表  SP商品 YP样品 ZP赠品
                                outBoundResponseDetail.setStore(tOrder.getWhId());
                                outBoundResponseDetail.setClient(tOrder.getClientCode());
                                outBoundResponseDetail.setZone(orderConfirm.getZone());
                                String type = outBoundService.queryConfirmOutBoundsByItemAndWhse(tOrder.getClientCode() + "-" + orderConfirm.getDisplayItemNumber(), tOrder.getWhId());
                                outBoundResponseDetail.setType(type);
                                outBoundResponseDetail.setPreScanned("N");
                                log.info(tOrder.getOrderNumber() + "->" + storageType);
                                if ((!storageType.isEmpty()) && (storageType.equals("9") || storageType == "9" || storageType == "9L")) {
                                    outBoundResponseDetail.setPreScanned("Y");
                                }
                                outBoundResponseDetail.setPreScanCust("");
                                if (!storageType.isEmpty()) {
                                    if ((storageType.equals("9") || storageType == "9" || storageType == "9L") && (!custPart.isEmpty())) {
                                        outBoundResponseDetail.setPreScanCust(custPart);
                                    } else if ((storageType.equals("9") || storageType == "9" || storageType == "9L") && (custPart.isEmpty())) {
                                        String cusCode = outBoundService.queryConfirmOutBoundsByCusAndWhse(tOrder.getCustomerId(), tOrder.getWhId());
                                        outBoundResponseDetail.setPreScanCust(cusCode);
                                    }
                                }
                                outBoundResponseDetail.setNotedtl("");//备注 非必填
                                outBoundResponseDetail.setStorageLocation(tOrder.getClientCode());
                                outBoundResponseDetail.setSKUCode(orderConfirm.getDisplayItemNumber());
                                outBoundResponseDetails.add(outBoundResponseDetail);
                            }
                            erpOutBoundResponse.setGrid(outBoundResponseDetails);
                            String json = JacksonUtil.nonAlways().toJson(erpOutBoundResponse);
                            log.info("出库回传报文：" + json);
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("xml", json);
                            params.put("action", "AddStockOut");
                            try {
                                String ret = "";
                                if("16003".equals(tClients.get(x).getClientCode()) || "16043".equals(tClients.get(x).getClientCode())) {
                                    ret = webServiceUtil_YZ.sendSoap11Message(params);
                                }
                                else {
                                    ret = webServiceUtil.sendSoap11Message(params);
                                }
                                log.info("出库报文回传返回：" + ret);
                                List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                                if ("1".equals(result.get(0).get("Status").toString())) {
                                    outBoundService.updateOutBoundsBackTrue(tOrder.getOrderId(),"1","RL单据回传成功",new Date());
                                }else{
                                    outBoundService.updateOutBoundsBackTrue(tOrder.getOrderId(),"0","RL单据回传失败",new Date());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            outBoundService.updateOutBoundsBack(tOrder.getOrderId());
                        }
                    }
                }
            }
        }

    }

//    /**
//     * 库存查询  未完成版本
//     * @param
//     * @return
//     */
//    public void inventoryQuery () {
//            ErpStoreResponse erpStoreResponse = new ErpStoreResponse();
//            erpStoreResponse.setStatus(0);
//            erpStoreResponse.setMessage("");
//            List<ErpStoreResponse.StoreItem> item = Lists.newArrayList();
//            BaseResponse<HashMap<String, QimenStoredInfo>> resp = storeService.queryQimenStoreInfo("6001", "601", "6001-12348496");
//            System.out.println("resp:"+resp);
//            resp.getData().forEach((k, v) -> {
//                ErpStoreResponse.StoreItem storeItem = new ErpStoreResponse.StoreItem();
//                storeItem.setBatchCode(v.getBatchCode());
//                storeItem.setItemCode(v.getItemCode().replace("6001-",""));
//                storeItem.setQty(v.getQuantity().intValue());
//                storeItem.setBoxQty(0);
//                storeItem.setStore("601");
//                item.add(storeItem);
//            });
//            erpStoreResponse.setItems(item);
//            String json = JacksonUtil.nonAlways().toJson(erpStoreResponse);
//            System.out.println(json);
//            HashMap<String, String> params = new HashMap<String, String>();
//            params.put("xml",json);
//            params.put("action", "StockQuery");
//            try {
//                String ret = webServiceUtil.sendSoap11Message(params);
//                System.out.println(ret.toString()); // 没有对结果做处理
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }

    /**
     * 库存查询  新版本
     * @param
     * @return
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void inventoryQuery () {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if((!tClients.isEmpty()) && tClients.size() >0 ){
            String clientCode = "";
            String whse = "";
            for(int x=0;x<tClients.size();x++){
                clientCode=tClients.get(x).getClientCode();
                whse = tClients.get(x).getWhId();
                ErpStoreResponse erpStoreResponse = new ErpStoreResponse();
                erpStoreResponse.setStatus(0);
                erpStoreResponse.setMessage("");
                List<ErpStoreResponse.StoreItem> item = Lists.newArrayList();
                List<StoreItem> resp = storeService.queryQimenStoreInfo2(clientCode, whse);
                for(int i=0;i<resp.size();i++){
                    ErpStoreResponse.StoreItem storeItem = new ErpStoreResponse.StoreItem();
                    storeItem.setStore(resp.get(i).getStore());
                    storeItem.setBoxQty(resp.get(i).getBoxQty());
                    storeItem.setQty(resp.get(i).getQty());
                    storeItem.setBatchCode(resp.get(i).getBatchCode()==null?"":resp.get(i).getBatchCode().toString());
                    storeItem.setItemCode(resp.get(i).getItemCode());
                    storeItem.setClient(resp.get(i).getClient());
                    storeItem.setType(resp.get(i).getType());
                    storeItem.setZone(resp.get(i).getZone());
                    storeItem.setPreScanCust(resp.get(i).getPreScanCust());
                    storeItem.setPreScanned(resp.get(i).getPreScanned());
                    storeItem.setProdDate(resp.get(i).getProdDate());
                    storeItem.setExpDate(resp.get(i).getExpDate());
                    item.add(storeItem);
                }
                erpStoreResponse.setItems(item);
                String json = JacksonUtil.nonAlways().toJson(erpStoreResponse);
                log.info("库存查询报文：" + json);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("xml",json);
                params.put("action", "StockQuery");
                try {
                    if("16003".equals(clientCode) || "16043".equals(clientCode)) {
                         webServiceUtil_YZ.sendSoap11Message(params);
                    }
                    else {
                         webServiceUtil.sendSoap11Message(params);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 到货  GoodsReceive
     * @return
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void goodsReceive(){
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if((!tClients.isEmpty()) && tClients.size() >0 ) {
            String clientCode = "";
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                clientCode = tClients.get(x).getClientCode();
                whse = tClients.get(x).getWhId();
                List<TOrder> tOrders = outBoundService.queryConfirmGoodsReceiveByWhseAndClient(whse, clientCode);
                for(int i=0;i<tOrders.size();i++) {
                    TOrder tOrder = tOrders.get(i);
                    ErpGoodsReceiveResponse erpGoodsReceiveResponse = new ErpGoodsReceiveResponse();
                    if (tOrder.getTypeId().toString().equals("1154")) {
                        erpGoodsReceiveResponse.setBillTypeID("XSCK");
                    } else if(tOrder.getTypeId().toString().equals("1155")){
                        erpGoodsReceiveResponse.setBillTypeID("DBCK");
                    }
                    else if(tOrder.getTypeId().toString().equals("1153")){
                        erpGoodsReceiveResponse.setBillTypeID("CTCK");
                    }
                    else if(tOrder.getTypeId().toString().equals("2422")){
                        erpGoodsReceiveResponse.setBillTypeID("ZCCK");
                    }else{
                        erpGoodsReceiveResponse.setBillTypeID("QTCK");
                    }
                    erpGoodsReceiveResponse.setERP_No(tOrder.getStoreOrderNumber());
                    erpGoodsReceiveResponse.setReceiveDate(tOrder.getActualArrivalDate());
                    erpGoodsReceiveResponse.setSatus(tOrder.getStatus());
                    erpGoodsReceiveResponse.setNote(tOrder.getOrderNumber());
                    String json = JacksonUtil.nonAlways().toJson(erpGoodsReceiveResponse);
                    System.out.println("到货出库报文:" + json);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("xml",json);
                    params.put("action", "GoodsReceive");
                    try {
                        String ret = "";
                        if("16003".equals(clientCode) || "16043".equals(clientCode)) {
                            ret = webServiceUtil_YZ.sendSoap11Message(params);
                        }
                        else {
                            ret = webServiceUtil.sendSoap11Message(params);
                        }
                        System.out.println("到货出库返回:" + ret.toString()); // 没有对结果做处理
                        //回传成功后，需要将对应订单的load_seq的值变更为2
                        List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                        if("1".equals(result.get(0).get("Status").toString())){
                            outBoundService.updateOutBoundsGoodsBack(tOrder.getOrderId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("到货出库错误:" + e.getMessage());
                    }
                }
            }
        }


    }

    /**
     * 包材领用 AddPackOut
     * @return
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void AddPackOut(){
        List<TblCmRequest> tblCmRequests = outBoundService.queryConfirmTblcmByWhseAndClient();
        ErpPackOutResponse erpPackOutResponse = new ErpPackOutResponse();
        erpPackOutResponse.setStatus(0);
        erpPackOutResponse.setMessage("");
        List<ErpPackOutResponse.PackOutItem> packOutItems = Lists.newArrayList();
        if((!packOutItems.isEmpty()) && packOutItems.size() >0 ) {
            for (int i = 0; i < tblCmRequests.size(); i++) {
                TblCmRequest tblCmRequest = tblCmRequests.get(i);
                ErpPackOutResponse.PackOutItem packOutItem = new ErpPackOutResponse.PackOutItem();
                packOutItem.setBaseQuantity(tblCmRequest.getReqQty().intValue());
                packOutItem.setBilllDate(tblCmRequest.getReqDatetime());
                packOutItem.setClient(tblCmRequest.getClientCode());
                packOutItem.setSKUCode(tblCmRequest.getItemNumber());
                packOutItem.setStore(tblCmRequest.getWhId());
                packOutItem.setBatchCode("");
                packOutItem.setNote("");
                packOutItems.add(packOutItem);
            }
            erpPackOutResponse.setItems(packOutItems);
            String json = JacksonUtil.nonAlways().toJson(erpPackOutResponse);
            System.out.println(json);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("xml", json);
            params.put("action", "AddPackOut");
            try {
                String ret = webServiceUtil.sendSoap11Message(params);
                System.out.println(ret.toString()); // 没有对结果做处理
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
