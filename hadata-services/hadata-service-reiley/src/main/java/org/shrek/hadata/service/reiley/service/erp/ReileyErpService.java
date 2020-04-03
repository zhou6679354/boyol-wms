package org.shrek.hadata.service.reiley.service.erp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.qimen.api.request.InventoryQueryRequest;
import com.qimen.api.response.InventoryQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.*;
import org.shrek.hadata.service.reiley.model.OrderCancelResponse;
import org.shrek.hadata.service.reiley.service.erp.model.*;
import org.shrek.hadata.service.reiley.service.scf.model.KdniaoDistRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月28日 10:12
 */
@Service
@Slf4j
public class ReileyErpService {

    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 50000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    StoreService storeService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    CustomerService customerService;

    ReileyErpB2CService reileyErpB2CService;

    /**
     * 物料
     * @param materials
     * @return
     */
    public BaseResponse createMaterial(List<ErpMaterialRequest> materials) {
        List<TClient> clients = clientService.queryClientByExtCode("reiley_b");
        List<TItemMaster> masters = Lists.newArrayList();
        clients.forEach(client -> {
            materials.forEach(material -> {
                TItemMaster tItemMaster = new TItemMaster();
                tItemMaster.setItemNumber(Joiner.on('-').join(client.getClientCode(), material.getCode()));
                tItemMaster.setClientCode(client.getClientCode());
                tItemMaster.setWhId(client.getWhId());
                tItemMaster.setDisplayItemNumber(material.getCode());
                tItemMaster.setCommodityCode(material.getSn());
                tItemMaster.setAltItemNumber("");
                tItemMaster.setStdQtyUom(material.getSpec());
                tItemMaster.setDescription(material.getName());
                tItemMaster.setShelfLife(material.getExpDate());
                tItemMaster.setUnitWeight(material.getWeight());
                tItemMaster.setUnitVolume(material.getVolume());
                if (material.getType() == 1) {
                    tItemMaster.setClassId("MKGD");
                } else if (material.getType() == 2) {
                    tItemMaster.setClassId("MKPR");
                } else if (material.getType() == 3) {
                    tItemMaster.setClassId("MKSP");
                } else if (material.getType() == 4) {
                    tItemMaster.setClassId("WINE");
                }else if (material.getType() == 5) {
                    tItemMaster.setClassId("CM");
                }
                if (material.getIeType().equals("2")) {
                    tItemMaster.setInventoryType("FC");
                } else if (material.getIeType().equals("1")) {
                    tItemMaster.setInventoryType("CN");
                } else {
                    tItemMaster.setInventoryType("FG");
                }
                tItemMaster.setProductionDateControl(material.getTrail2() == 1 ? "Y" : "N");
                tItemMaster.setLotControl(material.getTrail1() == 1 ? "F" : "N");
                tItemMaster.setExpirationDateControl(material.getTrail3() == 1 ? "Y" : "N");
                TItemUom EaUom = new TItemUom();
                EaUom.setItemNumber(tItemMaster.getItemNumber());
                EaUom.setConversionFactor(1D);
                EaUom.setUom("EA");
                EaUom.setUomPrompt("件");
//            MKGD  MKSP  ->ExpChkLotFEFO
//            其余 ->CustFEFO\
                EaUom.setClassId(tItemMaster.getClassId());
                if (tItemMaster.getClassId().equals("MKSP") || tItemMaster.getClassId().equals("MKGD")) {
                    EaUom.setPickPutId("ExpChkLotFEFO");
                } else {
                    EaUom.setPickPutId("CustFEFO");
                }
                EaUom.setDefaultPickArea("CASE");
                EaUom.setWhId(client.getWhId());
                TItemUom ScUom = new TItemUom();
                ScUom.setItemNumber(tItemMaster.getItemNumber());
                ScUom.setConversionFactor((double) material.getPcs());
                ScUom.setUom("CS");
                ScUom.setUomPrompt("箱");
                ScUom.setClassId(tItemMaster.getClassId());
                if (tItemMaster.getClassId().equals("MKSP") || tItemMaster.getClassId().equals("MKGD")) {
                    ScUom.setPickPutId("ExpChkLotFEFO");
                } else {
                    ScUom.setPickPutId("CustFEFO");
                }
                ScUom.setDefaultPickArea("CASE");
                ScUom.setWhId(client.getWhId());
                List<TItemUom> uoms = Lists.newArrayList(EaUom, ScUom);
                tItemMaster.setUoms(uoms);
                masters.add(tItemMaster);
            });
        });
        log.info("日勒物流报文："+ JSON.toJSON(masters));
        boolean resp = materielService.createMaterials(masters);
        if (resp) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail("数据异常,物料信息创建失败!");
        }

    }

    /**
     * 入库通知
     * @param erpInBoundRequest
     * @return
     */
    public BaseResponse createErpInBound (ErpInBoundRequest erpInBoundRequest){
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setPoNumber(erpInBoundRequest.getBillid());//ERP单据 ID  getErp_no
        tPoMaster.setDisplayPoNumber(erpInBoundRequest.getVendorbillno());
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpInBoundRequest.getStoragelocation());
            tPoMaster.setClientCode(client.getClientCode());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }
        tPoMaster.setWhId(client.getWhId());
        //CGRK-采购,DBRK-调拨,QTRK-其他
        if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("DBRK") || erpInBoundRequest.getBilltypeid().toUpperCase().equals("B2B") || erpInBoundRequest.getBilltypeid().toUpperCase().equals("C2B")) {
            tPoMaster.setTypeId(1120);
        } else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("CGRK")) {
            tPoMaster.setTypeId(1119);
        } else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("YCDB")) {
            tPoMaster.setTypeId(2560);
        }else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("YZS")) {
            tPoMaster.setTypeId(2564);
        }else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("ZZ") || erpInBoundRequest.getBilltypeid().toUpperCase().equals("ZZCX")) {
            tPoMaster.setTypeId(2420);
        }else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("STO")) {
            tPoMaster.setTypeId(2566);
        }else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("XSTHD")) {
            tPoMaster.setTypeId(2570);
        }else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("CGSHD")) {
            tPoMaster.setTypeId(2572);
        }else if (erpInBoundRequest.getBilltypeid().toUpperCase().equals("XSTH")) {
            tPoMaster.setTypeId(1121);
            tPoMaster.setShipFromName(erpInBoundRequest.getSupplyid());
        } else {
            tPoMaster.setTypeId(2453);
        }
        tPoMaster.setVendorCode("7510");//默认供应商编码
        tPoMaster.setCreateDate(DateUtil.getCurTimestamp());
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");
        tPoMaster.setServiceLevel(erpInBoundRequest.getNote());
        tPoMaster.setShipFromName(erpInBoundRequest.getDeliverycon());
        tPoMaster.setShipFromAddr1(erpInBoundRequest.getDeliveryaddress());
        tPoMaster.setShipFromAddr2(erpInBoundRequest.getDeliverystorage());
        //明细
        List<TPoDetail> tPoDetailList = Lists.newArrayList();
        List<Integer> curLineNum = Lists.newArrayList();
        erpInBoundRequest.getGrid().forEach(detail -> {
            TPoDetail tPoDetail = new TPoDetail();
            tPoDetail.setDeliveryDate(detail.getDeliverydate());
            tPoDetail.setPoNumber(erpInBoundRequest.getBillid());
            //tPoDetail.setLineNumber(String.valueOf((int)(Math.random()*100)));
            //tPoDetail.setLineNumber(String.valueOf((int)((Math.random()*9+1)*10000)));
            Random random = new Random();
            int linenum = random.nextInt(99999) + 1;
            while(curLineNum != null && curLineNum.size()>0 && curLineNum.contains(linenum))
                linenum = random.nextInt(99999) + 1;
            tPoDetail.setLineNumber(String.valueOf(linenum));
            curLineNum.add(linenum);
            //tPoDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
            tPoDetail.setSoLineNumber(detail.getBilldtlid());
            tPoDetail.setWhId(client.getWhId());
            tPoDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(),detail.getSkucode()));
            tPoDetail.setScheduleNumber(Integer.parseInt(detail.getBilldtlid()));//同solineNumber
            tPoDetail.setOrderUom("EA");//默认EA
            tPoDetail.setOriginator(detail.getMaterialtype());
            tPoDetail.setQty((double)detail.getBasequantity());
            tPoDetail.setSoNumber(detail.getToprescancust());
            tPoDetail.setLocationId(detail.getTozone());
            if (("Y").equals(detail.getToprescanned())) {
                tPoDetail.setSpecialProcessing("YES");
            }
            tPoDetail.setRemark(detail.getNotedtl());
            tPoDetail.setVendorItemNumber(detail.getCustomerordernumber());
            tPoDetailList.add(tPoDetail);
        });
        tPoMaster.setDetailList(tPoDetailList);
        log.info("日勒入库单报文："+ JSON.toJSON(tPoMaster));
        BaseResponse basicResult = inBoundService.createInBoundOrder(tPoMaster);
        if (basicResult.isFlag()) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(basicResult.getSubMessage());
        }
    }

    /**
     * 出库通知
     * @param erpOutBoundRequest
     * @return
     */
    public BaseResponse createErpOutBound (ErpOutBoundRequest erpOutBoundRequest){
        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(erpOutBoundRequest.getBillid());//ID
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpOutBoundRequest.getStoragelocation());
            tOrder.setClientCode(client.getClientCode());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }
        tOrder.setWhId(client.getWhId());
        TCustomer customer = customerService.getCustomerByWhseAndCode(client.getWhId(),erpOutBoundRequest.getSupplyid());
        if(customer != null && customer.getCustomerId()!= null) {
            tOrder.setCustomerId(customer.getCustomerId());
            tOrder.setCustomerName(customer.getCustomerName());
        }
        //XSCK出库通知
        if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("XSCK")) {
            tOrder.setTypeId(1154);
        } else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("CGTH")) {
            tOrder.setTypeId(1153);
        } else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("XSPF")) {
            tOrder.setTypeId(2528);
        }else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("YCDB")) {
            tOrder.setTypeId(2558);
        }else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("YZS")) {
            tOrder.setTypeId(2562);
        }else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("ZZ") || erpOutBoundRequest.getBilltypeid().toUpperCase().equals("ZZCX")) {
            tOrder.setTypeId(2422);
        }else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("STO")) {
            tOrder.setTypeId(2568);
        } else if (erpOutBoundRequest.getBilltypeid().toUpperCase().equals("B2B") || erpOutBoundRequest.getBilltypeid().toUpperCase().equals("B2C")) {
            tOrder.setTypeId(1155);
        }else{
            tOrder.setTypeId(2456);
        }
        tOrder.setStatus("N");
        tOrder.setOrderDate(erpOutBoundRequest.getBillldate());
        tOrder.setStoreOrderNumber(erpOutBoundRequest.getErp_no());
        tOrder.setCreateDate(DateUtil.getCurTimestamp());
        tOrder.setBackorder("1".equals(erpOutBoundRequest.getIsAllocate())?"Y":"N");
        tOrder.setShipToAddr2(erpOutBoundRequest.getDeliveryaddress());
        tOrder.setShipToName(erpOutBoundRequest.getDeliverycon());
        tOrder.setShipToPhone(erpOutBoundRequest.getDeliverytel());
        tOrder.setShipToCity(erpOutBoundRequest.getCustomercity());
        tOrder.setProNumber(erpOutBoundRequest.getAppointmentnum());
        tOrder.setDeliveryAddr3(erpOutBoundRequest.getAppointmenttime());
        tOrder.setServiceLevel(erpOutBoundRequest.getNote());
        java.text.SimpleDateFormat simpleDateFormat=new java.text.SimpleDateFormat("yyyyMMdd");
        String orderSerialNumber = outBoundService.getOrderSerialNumber(client.getWhId()).replace(simpleDateFormat.format(new java.util.Date()).substring(2,7),simpleDateFormat.format(new java.util.Date()).substring(2,8));
        tOrder.setOrderSerialNumber(orderSerialNumber);
        //明细
        List<TOrderDetail> tOrderDetailList = Lists.newArrayList();
        erpOutBoundRequest.getGrid().forEach(detail -> {
            TOrderDetail tOrderDetail = new TOrderDetail();
            tOrderDetail.setWhId(client.getWhId());
            tOrderDetail.setOrderNumber(erpOutBoundRequest.getBillid());

            //tOrderDetail.setLineNumber(String.valueOf((int)(Math.random()*100)));
            tOrderDetail.setLineNumber(String.valueOf((int)((Math.random()*9+1)*10000)));
            //tOrderDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
            tOrderDetail.setPoLineNumber(detail.getBilldtlid());
            tOrderDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(),detail.getSkucode()));
            tOrderDetail.setOrderUom("EA");//默认EA
            tOrderDetail.setBoDescription(detail.getMaterialtype());
            tOrderDetail.setQty((double)detail.getBasequantity());
            tOrderDetail.setAfoPlanQty((double)detail.getBasequantity());
            //tOrderDetail.setQty((double)detail.getQuantity()==0?(double)detail.getBasequantity():(double)detail.getQuantity());
            //tOrderDetail.setAfoPlanQty((double)detail.getQuantity()==0?(double)detail.getBasequantity():(double)detail.getQuantity());
            tOrderDetail.setItemDescription(detail.getNotedtl());
            tOrderDetail.setUnitPack(detail.getCustomerunit());
            tOrderDetail.setBOLLine1(detail.getCustomerordernumber());
            tOrderDetail.setExtendedVolume((double)detail.getUnitratio());
            tOrderDetail.setBOLLine2(detail.getAllocateno());
            tOrderDetail.setZone(detail.getFromzone());//2019.4.8约定用fromzone代替zone表示出库指定库区
            if(detail.getFromprescanned() != null) {
                tOrderDetail.setStorageType(detail.getFromprescanned().equals("Y") ? 9L : 0L);
                tOrderDetail.setCustPart(detail.getFromprescancust());
            }
            tOrderDetail.setBOLPlacCode(detail.getCustomercommoditycode());
            tOrderDetail.setBOLPlacDesc(detail.getCustomercommodityname());
            /*if(erpOutBoundRequest.getBilltypeid().toUpperCase().equals("CGTH"))
                tOrderDetail.setZone("TH");*/
            tOrderDetailList.add(tOrderDetail);

        });
        tOrder.setOrderDetailList(tOrderDetailList);
        log.info("日勒出库单报文："+ JSON.toJSON(tOrder));
        BaseResponse basicResult = outBoundService.createOutBoundOrder(tOrder);
        if (basicResult.isFlag()) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(basicResult.getSubMessage());
        }
    }

    /**
     * 库存查询  未完成版本
     * @param request
     * @return
     */
    public InventoryQueryResponse inventoryQuery(InventoryQueryRequest request) {
        InventoryQueryResponse response=new InventoryQueryResponse();
        response.setFlag("success");
        response.setCode("0");
        response.setMessage("");
        try {
            List<InventoryQueryResponse.Item> items = Lists.newArrayList();
            request.getCriteriaList().forEach(criteria -> {

                BaseResponse<HashMap<String, QimenStoredInfo>> resp = storeService.queryQimenStoreInfo(criteria.getOwnerCode(), criteria.getWarehouseCode(), Joiner.on('-').join(criteria.getOwnerCode(), criteria.getItemCode()));
                resp.getData().forEach((k, v) -> {
                    InventoryQueryResponse.Item item = new InventoryQueryResponse.Item();
                    item.setWarehouseCode(v.getWarehouseCode());
                    item.setItemCode(v.getItemCode());
                    item.setInventoryType(v.getStoreType());
                    item.setQuantity(v.getQuantity().longValue());
                    item.setLockQuantity(v.getLockQuantity().longValue());
                    item.setBatchCode(v.getBatchCode());
                    item.setProductDate(v.getProductDate());
                    item.setExpireDate(v.getExpireDate());
                    item.setProduceCode(v.getProduceCode());
                    items.add(item);
                });
            });
            response.setItems(items);
        }catch (RuntimeException   e){
            response.setFlag("failure");
            response.setCode("500");
            response.setMessage("信息查询失败,请稍后重试!");
        }
        return response;

    }


    /**
     * 入/出库取消
     */
    public BaseResponse cancelOrder (ErpCancelInBoundRequest erpCancelInBoundRequest){
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpCancelInBoundRequest.getStoragelocation());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }
        if (erpCancelInBoundRequest.getBilltypeid().toUpperCase().equals("RKXQ")) {
            //入库订单取消
            BaseResponse<String> basicResult = inBoundService.cancelInBoundOrder(erpCancelInBoundRequest.getBillid(), client.getClientCode(), client.getWhId());
            if (basicResult.isFlag()) {
                return BaseResponse.success();
            } else {
                return BaseResponse.fail(basicResult.getSubMessage());
            }
        } else if (erpCancelInBoundRequest.getBilltypeid().toUpperCase().equals("CKXQ")) {
            //出库订单取消
            BaseResponse<String> basicResult = outBoundService.cancelOutBoundOrder(erpCancelInBoundRequest.getBillid(), client.getClientCode(), client.getWhId());
            if (basicResult.isFlag()) {
                return BaseResponse.success();
            } else {
                return BaseResponse.fail(basicResult.getSubMessage());
            }
        } else {
            return BaseResponse.fail("BillTypeID错误！");
        }
    }


//    /**
//     * 移仓调拨通知B2C
//     * @return
//     */
//    public BaseResponse noticeAllocateB2C(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
//        //出库
//        BaseResponse basicResult1 = noticeAllocateOut(erpNoticeAllocateRequest);
//        if (basicResult1.isFlag()) {
//            //入库
//            BaseResponse basicResult2 = reileyErpB2CService.noticeAllocateIn(erpNoticeAllocateRequest);
//            if (basicResult2.isFlag()) {
//                return BaseResponse.success();
//            } else {
//                return BaseResponse.fail(basicResult2.getSubMessage());
//            }
//        } else {
//            return BaseResponse.fail(basicResult1.getSubMessage());
//        }
//    }


    /**
     * 移仓调拨通知   出库
     * @param erpNoticeAllocateRequest
     * @return
     */
    public BaseResponse noticeAllocateOut(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(erpNoticeAllocateRequest.getBillid());//ID
        tOrder.setClientCode(erpNoticeAllocateRequest.getFromclient());
        tOrder.setServiceLevel(erpNoticeAllocateRequest.getNote());
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpNoticeAllocateRequest.getFromclient());
            tOrder.setWhId(client.getWhId());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }

        //XSCK出库单
        /*if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("XSCK")) {
            tOrder.setTypeId(1155);
        } else {
            tOrder.setTypeId(1155);
        }*/

        if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("XSCK")) {
            tOrder.setTypeId(1154);
        } else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("CGTH")) {
            tOrder.setTypeId(1153);
        } else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("XSPF")) {
            tOrder.setTypeId(2528);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("YCDB")) {
            tOrder.setTypeId(2558);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("YZS")) {
            tOrder.setTypeId(2562);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("ZZ") || erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("ZZCX")) {
            tOrder.setTypeId(2422);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("STO")) {
            tOrder.setTypeId(2568);
        } else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("B2B") || erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("B2C")) {
            tOrder.setTypeId(1155);
        }else{
            tOrder.setTypeId(2456);
        }

        tOrder.setStatus("N");
        tOrder.setOrderDate(erpNoticeAllocateRequest.getBillldate());
        tOrder.setStoreOrderNumber(erpNoticeAllocateRequest.getErp_no());
        tOrder.setCreateDate(DateUtil.getCurTimestamp());
        java.text.SimpleDateFormat simpleDateFormat=new java.text.SimpleDateFormat("yyyyMMdd");
        String orderSerialNumber = outBoundService.getOrderSerialNumber(client.getWhId()).replace(simpleDateFormat.format(new java.util.Date()).substring(2,7),simpleDateFormat.format(new java.util.Date()).substring(2,8));;
        tOrder.setOrderSerialNumber(orderSerialNumber);
        tOrder.setShipToName(erpNoticeAllocateRequest.getContact());
        tOrder.setShipToPhone(erpNoticeAllocateRequest.getTel());
        tOrder.setShipToAddr2(erpNoticeAllocateRequest.getAddress());
        //明细
        List<TOrderDetail> tOrderDetailList = Lists.newArrayList();
        if(erpNoticeAllocateRequest.getGrid().get(0).getDocumenttype() == 22){
            TOrderDetail tOrderDetail = new TOrderDetail();
            tOrderDetail.setWhId(client.getWhId());
            tOrderDetail.setOrderNumber(erpNoticeAllocateRequest.getBillid());
            //tOrderDetail.setLineNumber(String.valueOf((int)(Math.random()*100)));
            tOrderDetail.setLineNumber(String.valueOf((int) ((Math.random() * 9 + 1) * 10000)));
            //tOrderDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
            tOrderDetail.setPoLineNumber(erpNoticeAllocateRequest.getBillid());
            tOrderDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(), erpNoticeAllocateRequest.getPackageskucode()));
            tOrderDetail.setOrderUom("EA");//默认EA
            tOrderDetail.setQty((double) erpNoticeAllocateRequest.getPackagequantity());
            tOrderDetail.setAfoPlanQty((double) erpNoticeAllocateRequest.getPackagequantity());
            //tOrderDetail.setItemDescription(detail.getNotedtl());
            //tOrderDetail.setZone(detail.getFromzone());
            tOrderDetail.setCustPart(erpNoticeAllocateRequest.getGrid().get(0).getToprescancust());
            if (erpNoticeAllocateRequest.getGrid().get(0).getToprescanned().equals("Y")) {
                tOrderDetail.setStorageType(9L);
            }
            //tOrderDetail.setLotNumber(detail.getBatchcode());
            tOrderDetailList.add(tOrderDetail);
        }
        else {
            erpNoticeAllocateRequest.getGrid().forEach(detail -> {
                TOrderDetail tOrderDetail = new TOrderDetail();
                tOrderDetail.setWhId(client.getWhId());
                tOrderDetail.setOrderNumber(erpNoticeAllocateRequest.getBillid());
                //tOrderDetail.setLineNumber(String.valueOf((int)(Math.random()*100)));
                tOrderDetail.setLineNumber(String.valueOf((int) ((Math.random() * 9 + 1) * 10000)));
                //tOrderDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
                tOrderDetail.setPoLineNumber(detail.getBilldtlid());
                tOrderDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(), detail.getFromskucode()));
                tOrderDetail.setOrderUom("EA");//默认EA
                tOrderDetail.setQty((double) detail.getBasequantity());
                tOrderDetail.setAfoPlanQty((double) detail.getBasequantity());
                tOrderDetail.setItemDescription(detail.getNotedtl());
                tOrderDetail.setZone(detail.getFromzone());
                tOrderDetail.setCustPart(detail.getFromprescancust());
                if (detail.getFromprescanned().equals("Y")) {
                    tOrderDetail.setStorageType(9L);
                }
                tOrderDetail.setLotNumber(detail.getBatchcode());
                tOrderDetailList.add(tOrderDetail);
            });
        }
        tOrder.setOrderDetailList(tOrderDetailList);
        log.info("日勒移仓调拨出库单报文："+ JSON.toJSON(tOrder));
        BaseResponse basicResult = outBoundService.createOutBoundOrder(tOrder);
        if (basicResult.isFlag()) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(basicResult.getSubMessage());
        }
    }

    /**
     * 移仓调拨通知   入库
     * @param erpNoticeAllocateRequest
     * @return
     */
    public BaseResponse noticeAllocateIn(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setPoNumber(erpNoticeAllocateRequest.getBillid());//ERP单据 ID  getErp_no
        tPoMaster.setClientCode(erpNoticeAllocateRequest.getToclient());
        tPoMaster.setServiceLevel(erpNoticeAllocateRequest.getNote());
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpNoticeAllocateRequest.getToclient());
            tPoMaster.setWhId(client.getWhId());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }

        //CGRK-采购,DBRK-调拨,QTRK-其他
        /*if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("DBRK")) {
            tPoMaster.setTypeId(1120);
        } else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("CGRK")) {
            tPoMaster.setTypeId(1119);
        } else {
            tPoMaster.setTypeId(2453);
        }*/

        if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("DBRK") || erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("B2B") || erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("C2B")) {
            tPoMaster.setTypeId(1120);
        } else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("CGRK")) {
            tPoMaster.setTypeId(1119);
        } else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("YCDB")) {
            tPoMaster.setTypeId(2560);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("YZS")) {
            tPoMaster.setTypeId(2564);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("ZZ") || erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("ZZCX")) {
            tPoMaster.setTypeId(2420);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("STO")) {
            tPoMaster.setTypeId(2566);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("XSTHD")) {
            tPoMaster.setTypeId(2570);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("CGSHD")) {
            tPoMaster.setTypeId(2572);
        }else if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("XSTH")) {
            tPoMaster.setTypeId(1121);
        } else {
            tPoMaster.setTypeId(2453);
        }

        tPoMaster.setVendorCode("7510");//默认供应商编码
        tPoMaster.setCreateDate(DateUtil.getCurTimestamp());
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");
        //明细
        List<TPoDetail> tPoDetailList = Lists.newArrayList();
        if(erpNoticeAllocateRequest.getGrid().get(0).getDocumenttype() == 22)
        {
            erpNoticeAllocateRequest.getGrid().forEach(detail -> {
                TPoDetail tPoDetail = new TPoDetail();
                tPoDetail.setPoNumber(erpNoticeAllocateRequest.getBillid());
                tPoDetail.setLineNumber(String.valueOf((int) ((Math.random() * 9 + 1) * 10000)));
                //tPoDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
                tPoDetail.setSoLineNumber(detail.getBilldtlid());
                tPoDetail.setWhId(client.getWhId());
                tPoDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(), detail.getToskucode()));
                tPoDetail.setScheduleNumber(Integer.parseInt(detail.getBilldtlid()));//同solineNumber
                tPoDetail.setOrderUom("EA");//默认EA
                tPoDetail.setQty((double) detail.getBasequantity());
                tPoDetail.setSoNumber(detail.getToprescancust());
                tPoDetail.setLocationId(detail.getTozone());
                if (detail.getToprescanned().equals("Y")) {
                    tPoDetail.setSpecialProcessing("YES");
                }
                tPoDetail.setLotNumber(detail.getBatchcode());
                tPoDetailList.add(tPoDetail);
            });
        }
        else {
            if (erpNoticeAllocateRequest.getPackageskucode() != null && erpNoticeAllocateRequest.getPackagequantity() > 0) {
                TPoDetail tPoDetail = new TPoDetail();
                tPoDetail.setPoNumber(erpNoticeAllocateRequest.getBillid());
                tPoDetail.setLineNumber(String.valueOf((int) ((Math.random() * 9 + 1) * 10000)));
                //tPoDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
                tPoDetail.setSoLineNumber(erpNoticeAllocateRequest.getBillid());
                tPoDetail.setWhId(client.getWhId());
                tPoDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(), erpNoticeAllocateRequest.getPackageskucode()));
                tPoDetail.setScheduleNumber(Integer.parseInt(erpNoticeAllocateRequest.getBillid()));//同solineNumber
                tPoDetail.setOrderUom("EA");//默认EA
                tPoDetail.setQty((double) erpNoticeAllocateRequest.getPackagequantity());
                //tPoDetail.setSoNumber(detail.getToprescancust());
                //tPoDetail.setLocationId(detail.getTozone());
                //if (detail.getToprescanned().equals("Y")) {
                //    tPoDetail.setSpecialProcessing("YES");
                //}
                //tPoDetail.setLotNumber(detail.getBatchcode());
                tPoDetailList.add(tPoDetail);
            } else {
                erpNoticeAllocateRequest.getGrid().forEach(detail -> {
                    TPoDetail tPoDetail = new TPoDetail();
                    tPoDetail.setPoNumber(erpNoticeAllocateRequest.getBillid());
                    tPoDetail.setLineNumber(String.valueOf((int) ((Math.random() * 9 + 1) * 10000)));
                    //tPoDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
                    tPoDetail.setSoLineNumber(detail.getBilldtlid());
                    tPoDetail.setWhId(client.getWhId());
                    tPoDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(), detail.getToskucode()));
                    tPoDetail.setScheduleNumber(Integer.parseInt(detail.getBilldtlid()));//同solineNumber
                    tPoDetail.setOrderUom("EA");//默认EA
                    tPoDetail.setQty((double) detail.getBasequantity());
                    tPoDetail.setSoNumber(detail.getToprescancust());
                    tPoDetail.setLocationId(detail.getTozone());
                    if (detail.getToprescanned().equals("Y")) {
                        tPoDetail.setSpecialProcessing("YES");
                    }
                    tPoDetail.setLotNumber(detail.getBatchcode());
                    tPoDetailList.add(tPoDetail);
                });
            }
        }
        tPoMaster.setDetailList(tPoDetailList);
        log.info("日勒移仓调拨入库单报文："+ JSON.toJSON(tPoMaster));
        BaseResponse basicResult = inBoundService.createInBoundOrder(tPoMaster);
        if (basicResult.isFlag()) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(basicResult.getSubMessage());
        }
    }



}
