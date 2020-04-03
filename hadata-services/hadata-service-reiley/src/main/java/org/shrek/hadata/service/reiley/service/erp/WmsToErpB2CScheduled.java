package org.shrek.hadata.service.reiley.service.erp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.KdniaoReqUtil;
import org.shrek.hadata.commons.util.WebServiceUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.InBoundService;
import org.shrek.hadata.service.hwms.service.OutBoundService;
import org.shrek.hadata.service.hwms.service.StoreService;
import org.shrek.hadata.service.hwms.model.TblCmRequest;
import org.shrek.hadata.service.reiley.service.erp.model.*;
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
public class WmsToErpB2CScheduled {
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 150000)
    InBoundService inBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 150000)
    OutBoundService outBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 150000)
    StoreService storeService;

    /**
    *test port is 8289
     */
    public static final WebServiceUtil webServiceUtil = new WebServiceUtil("http://bokesoft.com/dee/service/",
            "http://180.167.196.116:9001/BokeDee/services/deeSpServiceWrapped?wsdl",
            "BokeDeeServiceWSService", "deeSpServiceWrapped",
            "deeServiceHv", "deeServiceHvResponse");

    public static final KdniaoReqUtil kdniaoReqUtil = new KdniaoReqUtil("1457183","1008",
            "37dd9a41-bd1f-40bf-8cf5-47a08eb6a45d","http://api.kdniao.com/api/dist");

    /**
     * 调拨入库
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackInBoundResponse() {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if ((!tClients.isEmpty()) && tClients.size() > 0) {
            String clientCode = "";
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                clientCode = tClients.get(x).getClientCode();
                whse = tClients.get(x).getWhId();
                List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByWhseAndType2(clientCode, whse, 1120);
                System.out.println(tPoMasters);
                for (int i = 0; i < tPoMasters.size(); i++) {
                    TPoMaster tPoMaster = tPoMasters.get(i);
                    ErpInBoundResponse erpInBoundResponse = new ErpInBoundResponse();
                    erpInBoundResponse.setBillID(tPoMaster.getPoNumber());
                    erpInBoundResponse.setBillTypeID("入库");
                    erpInBoundResponse.setCompanyCode("");//客户
                    erpInBoundResponse.setSupplyID("");//非必填 传过 没存表
                    erpInBoundResponse.setB_No(tPoMaster.getPoNumber());
                    erpInBoundResponse.setBilllDate(tPoMaster.getCreateDate());//不确定
                    erpInBoundResponse.setNote("");//非必填 传过 没存表
                    List<ErpInBoundResponse.InBoundResponseDetail> inBoundResponseDetails = Lists.newArrayList();
                    for (int j = 0; j < tPoMasters.get(i).getDetailList().size(); j++) {
                        TPoDetail detail = tPoMasters.get(i).getDetailList().get(j);
                        ErpInBoundResponse.InBoundResponseDetail inBoundResponseDetail = new ErpInBoundResponse.InBoundResponseDetail();
                        inBoundResponseDetail.setBaseQuantity(detail.getQty().intValue()); //基本数量
                        inBoundResponseDetail.setQuantity(0);//实收箱数
                        inBoundResponseDetail.setBillDtlID(detail.getSoLineNumber());
                        inBoundResponseDetail.setBatchCode(detail.getLotNumber2());//批次号
                        inBoundResponseDetail.setExpirydate(detail.getExpirationDate2());
                        inBoundResponseDetail.setMakeDate(new Date());//非必填
                        inBoundResponseDetail.setMaterialType(detail.getOriginator()==null?"":detail.getOriginator().toString());//传过 没存表  SP商品 YP样品 ZP赠品
                        inBoundResponseDetail.setStore(tPoMaster.getWhId());
                        inBoundResponseDetail.setClient(tPoMaster.getClientCode());
                        String zone = inBoundService.queryConfirmInBoundsByZone(detail.getLocationId(), tPoMaster.getWhId());
                        inBoundResponseDetail.setZone(zone);
                        String type = inBoundService.queryConfirmInBoundsByItemAndWhse(detail.getItemNumber(), tPoMaster.getWhId());
                        inBoundResponseDetail.setType(type);
                        inBoundResponseDetail.setNotedtl("");//备注 非必填
                        inBoundResponseDetail.setPreScanCust(detail.getSoNumber()==null?"":detail.getSoNumber());
                        if(detail.getSpecialProcessing()==null)
                            inBoundResponseDetail.setPreScanned("N");
                        else
                            inBoundResponseDetail.setPreScanned(detail.getSpecialProcessing().toString().equals("YES")?"Y":"N");
                        inBoundResponseDetail.setReqDate(new Date());//非必填
                        inBoundResponseDetail.setReqFrom("WMSTEST");
                        inBoundResponseDetail.setReqId("WMSTEST"); //UUID.randomUUID().toString()
                        inBoundResponseDetail.setStorageLocation(tPoMaster.getClientCode());
                        inBoundResponseDetail.setSKUCode(detail.getItemNumber().replace(tPoMaster.getClientCode() + '-', ""));
                        inBoundResponseDetails.add(inBoundResponseDetail);
                    }
                    erpInBoundResponse.setGrid(inBoundResponseDetails);
                    String json = JacksonUtil.nonAlways().toJson(erpInBoundResponse);
                    System.out.println(json);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("xml", json);
                    params.put("action", "AddStockIn");
                    try {
                        String ret = webServiceUtil.sendSoap11Message(params);
                        System.out.println(ret.toString()); // 没有对结果做处理
                        List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                        if ("1".equals(result.get(0).get("Status").toString())) {
                            inBoundService.updateInBoundsBack(tPoMaster.getWhId(), tPoMaster.getPoNumber());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 入库
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackThInBoundResponse() {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if ((!tClients.isEmpty()) && tClients.size() > 0) {
            String clientCode = "";
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                clientCode = tClients.get(x).getClientCode();
                whse = tClients.get(x).getWhId();
                List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByWhseAndType2(clientCode, whse, 1121);
                System.out.println(tPoMasters);
                for (int i = 0; i < tPoMasters.size(); i++) {
                    TPoMaster tPoMaster = tPoMasters.get(i);
                    ErpInBoundResponse erpInBoundResponse = new ErpInBoundResponse();
                    erpInBoundResponse.setBillID(tPoMaster.getPoNumber());
                    erpInBoundResponse.setBillTypeID("销售退货入库");
                    erpInBoundResponse.setCompanyCode("");//客户
                    erpInBoundResponse.setSupplyID("");//非必填 传过 没存表
                    erpInBoundResponse.setB_No(tPoMaster.getPoNumber());
                    erpInBoundResponse.setBilllDate(tPoMaster.getCreateDate());//不确定
                    erpInBoundResponse.setNote("");//非必填 传过 没存表
                    List<ErpInBoundResponse.InBoundResponseDetail> inBoundResponseDetails = Lists.newArrayList();
                    for (int j = 0; j < tPoMasters.get(i).getDetailList().size(); j++) {
                        TPoDetail detail = tPoMasters.get(i).getDetailList().get(j);
                        ErpInBoundResponse.InBoundResponseDetail inBoundResponseDetail = new ErpInBoundResponse.InBoundResponseDetail();
                        inBoundResponseDetail.setBaseQuantity(detail.getQty().intValue()); //基本数量
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
                        inBoundResponseDetail.setPreScanCust("");
                        inBoundResponseDetail.setPreScanned("N");
                        inBoundResponseDetail.setReqDate(new Date());//非必填
                        inBoundResponseDetail.setReqFrom("WMSTEST");
                        inBoundResponseDetail.setReqId("WMSTEST"); //UUID.randomUUID().toString()
                        inBoundResponseDetail.setStorageLocation(tPoMaster.getClientCode());
                        inBoundResponseDetail.setSKUCode(detail.getItemNumber().replace(tPoMaster.getClientCode() + '-', ""));
                        inBoundResponseDetails.add(inBoundResponseDetail);
                    }
                    erpInBoundResponse.setGrid(inBoundResponseDetails);
                    String json = JacksonUtil.nonAlways().toJson(erpInBoundResponse);
                    System.out.println(json);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("xml", json);
                    params.put("action", "AddStockIn");
                    try {
                        String ret = webServiceUtil.sendSoap11Message(params);
                        System.out.println(ret.toString()); // 没有对结果做处理
                        List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                        if ("1".equals(result.get(0).get("Status").toString())) {
                            inBoundService.updateInBoundsBack2(tPoMaster.getWhId(), tPoMaster.getPoNumber());
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
        if ((!tClients.isEmpty()) && tClients.size() > 0) {
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                whse = tClients.get(x).getWhId();
                List<TPoDetail> tPoDetails = outBoundService.queryOutBoundsByWhse2(whse);//查询条件为WhId
                for (int i = 0; i < tPoDetails.size(); i++) {
                    ErpOutB2CResponse erpOutB2CResponse = new ErpOutB2CResponse();
                    erpOutB2CResponse.setStatus(0);
                    erpOutB2CResponse.setMessage("");
                    List<ErpOutB2CResponse.StoreItem> storeItems = Lists.newArrayList();
                    TPoDetail detail = tPoDetails.get(i);
                    ErpOutB2CResponse.StoreItem storeItem = new ErpOutB2CResponse.StoreItem();
                    storeItem.setBatchCode(detail.getLotNumber());//批次号  待确认
                    storeItem.setISRETURN(0); //固定值  0否 1是
                    storeItem.setItemCode(detail.getItemNumber());
                    storeItem.setPrice(detail.getPrice()); // 实际销售价
                    storeItem.setPrice2(detail.getPrice2()); // 平台价
                    storeItem.setQUANTITY(detail.getQty().intValue()); //基本数量
                    storeItem.setStore(detail.getWhId());// 仓库
                    storeItem.setClient(detail.getLocationId());// 货主
                    storeItem.setZone(detail.getSpecialProcessing());
                    storeItem.setType(detail.getDistroProcess());
                    storeItem.setPreScanned("N");// N否 Y是
                    storeItem.setPreScanCust("");// 预扫码客户编号
                    storeItem.setNotedtl("");//备注 非必填
//          List<TZoneLoca> tZoneLocas = inBoundService.queryConfirmZoneLocaByWhseAndloca(detail.getLocationId(), "601");
//          storeItem.setZone(tZoneLocas.get(0).getZone());//字段待确认（正常品区、退货区..)
//          List<TItemUom> tItemUoms = inBoundService.queryConfirmItemUomByItemNumber(detail.getItemNumber());
//          storeItem.setType(tItemUoms.get(0).getClassId());//字段待确认（商品、赠品、样品..）
                    storeItems.add(storeItem);
                    erpOutB2CResponse.setItems(storeItems);
                    String json = JacksonUtil.nonAlways().toJson(erpOutB2CResponse);
                    System.out.println(json);
                    log.warn("B2C出库报文回传：" + json);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("xml", json);
                    params.put("action", "AddStockOutB2C");
                    try {
                        String ret = webServiceUtil.sendSoap11Message(params);
                        System.out.println(ret.toString()); // 没有对结果做处理
                        List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                        if ("1".equals(result.get(0).get("Status").toString())) {
                            outBoundService.updateOutBoundsBackByOrderNo(detail.getWhId(),detail.getPoNumber());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*begin comment on 20190226TOrder tOrder = tOrders.get(i);
                    ErpOutBoundResponse erpOutBoundResponse = new ErpOutBoundResponse();
                    erpOutBoundResponse.setBillID(tOrder.getOrderNumber());
                    if (tOrder.getTypeId().toString().equals("2456")) {
                        erpOutBoundResponse.setBillTypeID("出库");
                    } else if (tOrder.getTypeId().toString().equals("1155")) {
                        erpOutBoundResponse.setBillTypeID("采购退货出库");
                    } else {
                        erpOutBoundResponse.setBillTypeID("");
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
                        outBoundResponseDetail.setBillDtlID(orderConfirm.getPoLineNumber());
                        outBoundResponseDetail.setBatchCode(orderConfirm.getLotNumber());//批次号
                        outBoundResponseDetail.setMakeDate(orderConfirm.getProdDate());//非必填
                        TOrderDetail tOrderDetail = outBoundService.queryConfirmOutBoundsByWhseAndNoAndId(orderConfirm.getLineNumber(), tOrder.getOrderNumber(), tOrder.getWhId());
                        String materialType = tOrderDetail.getBoDescription()==null?"":tOrderDetail.getBoDescription().toString();
                        outBoundResponseDetail.setMaterialType(materialType);//传过 没存表  SP商品 YP样品 ZP赠品
                        outBoundResponseDetail.setStore(tOrder.getWhId());
                        outBoundResponseDetail.setClient(tOrder.getClientCode());
                        outBoundResponseDetail.setZone(orderConfirm.getZone());
                        String type = outBoundService.queryConfirmOutBoundsByItemAndWhse(tOrder.getClientCode() + "-" + orderConfirm.getDisplayItemNumber(), tOrder.getWhId());
                        outBoundResponseDetail.setType(type);
                        outBoundResponseDetail.setPreScanned("N");
                        String storageType = tOrderDetail.getStorageType().toString();
                        if ((!storageType.isEmpty()) && storageType == "9") {
                            outBoundResponseDetail.setPreScanned("Y");
                        }
                        String custPart = tOrderDetail.getCustPart();
                        outBoundResponseDetail.setPreScanCust("");
                        if (!storageType.isEmpty()) {
                            if (storageType == "9" && (!custPart.isEmpty())) {
                                outBoundResponseDetail.setPreScanCust(custPart);
                            } else if (storageType == "9" && (custPart.isEmpty())) {
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
                    System.out.println(json);
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("xml", json);
                    //params.put("action", "AddStockOut");//20190226
                    params.put("action", "AddStockOutB2C");
                    try {
                        String ret = webServiceUtil.sendSoap11Message(params);
                        System.out.println(ret.toString()); // 没有对结果做处理
                        List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                        if ("1".equals(result.get(0).get("Status").toString())) {
                            outBoundService.updateOutBoundsBack(tOrder.getOrderId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }end comment on 20190226*/
                }
            }
        }
    }

    /**
     * 订阅快递鸟物流跟踪接口
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void subscribeKdniaoDist() {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("longqin");
        log.warn("快递鸟物流节点订阅-任务启动");
        if ((!tClients.isEmpty()) && tClients.size() > 0) {
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                whse = tClients.get(x).getWhId();
                log.warn("快递鸟物流节点订阅-任务启动:" + whse);
                List<TOrder> orders = outBoundService.querySubscribeOutBounds(whse,tClients.get(x).getClientCode());
                log.warn("快递鸟物流节点订阅-获取订单数:" + orders.size());
                for(int i=0;i<orders.size();i++) {
                    KdniaoDistSubscribeRequest kdniaoDistSubscribeRequest = new KdniaoDistSubscribeRequest();
                    KdniaoDistSubscribeRequest.CommodityDto commodity = new KdniaoDistSubscribeRequest.CommodityDto();
                    KdniaoDistSubscribeRequest.SenderDto sender = new KdniaoDistSubscribeRequest.SenderDto();
                    KdniaoDistSubscribeRequest.SenderDto receiver = new KdniaoDistSubscribeRequest.SenderDto();
                    kdniaoDistSubscribeRequest.setShipperCode(orders.get(i).getCarrierScac()=="YUNDA"?"YD":orders.get(i).getCarrierScac());
                    kdniaoDistSubscribeRequest.setOrderCode(orders.get(i).getOrderNumber());
                    kdniaoDistSubscribeRequest.setLogisticCode(orders.get(i).getCarrierNumber());
                    kdniaoDistSubscribeRequest.setPayType("3");
                    kdniaoDistSubscribeRequest.setExpType("1");
                    kdniaoDistSubscribeRequest.setIsNotice("0");
                    kdniaoDistSubscribeRequest.setMonthCode("");
                    kdniaoDistSubscribeRequest.setCustomerName("20125599900");
                    kdniaoDistSubscribeRequest.setCustomerPwd("b9Ut3GwfAsvxFkpBZrCmW5yR8KM4dI");
                    sender.setName(orders.get(i).getDeliveryName());
                    sender.setMobile(orders.get(i).getDeliveryPhone());
                    sender.setTel(orders.get(i).getDeliveryPhone());
                    sender.setProvinceName(orders.get(i).getDeliveryState());
                    sender.setCityName(orders.get(i).getDeliveryCity());
                    sender.setExpAreaName(orders.get(i).getDeliveryAddr2());
                    sender.setAddress(orders.get(i).getDeliveryAddr1());
                    kdniaoDistSubscribeRequest.setSender(sender);
                    receiver.setName(orders.get(i).getShipToName());
                    receiver.setMobile(orders.get(i).getShipToPhone());
                    receiver.setTel(orders.get(i).getShipToPhone());
                    receiver.setProvinceName(orders.get(i).getShipToState());
                    receiver.setCityName(orders.get(i).getShipToCity());
                    receiver.setExpAreaName(orders.get(i).getShipToAddr2());
                    receiver.setAddress(orders.get(i).getShipToAddr1());
                    kdniaoDistSubscribeRequest.setReceiver(receiver);
                    commodity.setGoodsName("商品");
                    List<KdniaoDistSubscribeRequest.CommodityDto> coms = Lists.newArrayList();
                    coms.add(commodity);
                    kdniaoDistSubscribeRequest.setCommodity(coms);

                    String json = JacksonUtil.nonAlways().toJson(kdniaoDistSubscribeRequest);
                    System.out.println(json);
                    log.warn("快递鸟物流节点订阅：" + json);

                    try{
                        String retStr = kdniaoReqUtil.orderTracesSubByJson(json);
                        HashMap result = JacksonUtil.nonEmpty().fromJson(retStr, HashMap.class);
                        log.warn("快递鸟物流节点订阅返回：" + retStr);
                        log.warn("快递鸟物流节点处理结果：" + result.get("Success").toString());
                        if ("true".equals(result.get("Success").toString())) {
                            outBoundService.updateOutBoundsSubByOrderNo(orders.get(i).getWhId(),orders.get(i).getOrderNumber());
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    /**
     * B2c出库退库
     *
     * @param
     * @return
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackOutB2C() {
        List<TClient> tClients = inBoundService.queryInBoundsByFax("reiley");
        if ((!tClients.isEmpty()) && tClients.size() > 0) {
            String clientCode = "";
            String whse = "";
            for (int x = 0; x < tClients.size(); x++) {
                clientCode = tClients.get(x).getClientCode();
                whse = tClients.get(x).getWhId();
        List<TPoDetail> tPoDetails = inBoundService.queryConfirmInBoundsByWhseAndCodeAndType2(clientCode, whse, 1121);
        for (int i = 0; i < tPoDetails.size(); i++) {
            ErpOutB2CResponse erpOutB2CResponse = new ErpOutB2CResponse();
            erpOutB2CResponse.setStatus(0);
            erpOutB2CResponse.setMessage("");
            List<ErpOutB2CResponse.StoreItem> storeItems = Lists.newArrayList();
            TPoDetail detail = tPoDetails.get(i);
            ErpOutB2CResponse.StoreItem storeItem = new ErpOutB2CResponse.StoreItem();
            storeItem.setBatchCode(detail.getLotNumber());//批次号  待确认
            storeItem.setISRETURN(1); //固定值  0否 1是
            storeItem.setItemCode(detail.getItemNumber());
            storeItem.setPrice(detail.getPrice()); // 平台价
            storeItem.setPrice2(detail.getPrice2()); // 实际销售价
            storeItem.setQUANTITY(detail.getQty().intValue()); //基本数量
            storeItem.setStore(detail.getWhId());// 仓库
            storeItem.setClient(detail.getLocationId());// 货主
            storeItem.setZone(detail.getSpecialProcessing());
            storeItem.setType(detail.getDistroProcess());
            storeItem.setPreScanned("N");// N否 Y是
            storeItem.setPreScanCust("");// 预扫码客户编号
            storeItem.setNotedtl("");//备注 非必填
//          List<TZoneLoca> tZoneLocas = inBoundService.queryConfirmZoneLocaByWhseAndloca(detail.getLocationId(), "601");
//          storeItem.setZone(tZoneLocas.get(0).getZone());//字段待确认（正常品区、退货区..)
//          List<TItemUom> tItemUoms = inBoundService.queryConfirmItemUomByItemNumber(detail.getItemNumber());
//          storeItem.setType(tItemUoms.get(0).getClassId());//字段待确认（商品、赠品、样品..）
            storeItems.add(storeItem);
            erpOutB2CResponse.setItems(storeItems);
            String json = JacksonUtil.nonAlways().toJson(erpOutB2CResponse);
            System.out.println(json);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("xml", json);
            params.put("action", "AddStockOutB2C");
            try {
                String ret = webServiceUtil.sendSoap11Message(params);
                System.out.println(ret.toString()); // 没有对结果做处理
                List<HashMap> result = JacksonUtil.nonEmpty().fromJson(ret.toString(), List.class, HashMap.class);
                if ("1".equals(result.get(0).get("Status").toString())) {
                    inBoundService.updateInBoundsBack2(detail.getWhId(), detail.getPoNumber());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
    }


    /**
     * 包材领用 AddPackOut
     * @return
     */
    public void AddPackOut(){
        List<TblCmRequest> tblCmRequests = outBoundService.queryConfirmTblcmByWhseAndClient();
        ErpPackOutResponse erpPackOutResponse = new ErpPackOutResponse();
        erpPackOutResponse.setStatus(0);
        erpPackOutResponse.setMessage("");
        List<ErpPackOutResponse.PackOutItem> packOutItems = Lists.newArrayList();
        for(int i=0;i<tblCmRequests.size();i++) {
            TblCmRequest tblCmRequest = tblCmRequests.get(i);
            ErpPackOutResponse.PackOutItem packOutItem = new ErpPackOutResponse.PackOutItem ();
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
        params.put("xml",json);
        params.put("action", "AddPackOut");
        try {
            String ret = webServiceUtil.sendSoap11Message(params);
            System.out.println(ret.toString()); // 没有对结果做处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
