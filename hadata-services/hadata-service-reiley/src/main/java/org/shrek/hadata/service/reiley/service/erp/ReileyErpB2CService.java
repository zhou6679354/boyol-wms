package org.shrek.hadata.service.reiley.service.erp;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.*;
import org.shrek.hadata.service.reiley.service.erp.model.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月28日 10:12
 */
@Service
public class ReileyErpB2CService {

    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;

    ReileyErpService reileyErpService;

    /**
     * 移仓调拨通知C2C
     * @return
     */
    public BaseResponse noticeAllocateC2C(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
        //出库
        BaseResponse basicResult1 = noticeAllocateOut(erpNoticeAllocateRequest);
        if (basicResult1.isFlag()) {
            //入库
            BaseResponse basicResult2 = noticeAllocateIn(erpNoticeAllocateRequest);
            if (basicResult2.isFlag()) {
                return BaseResponse.success();
            } else {
                return BaseResponse.fail(basicResult2.getSubMessage());
            }
        } else {
            return BaseResponse.fail(basicResult1.getSubMessage());
        }
    }

//    /**
//     * 移仓调拨通知C2B
//     * @return
//     */
//    public BaseResponse noticeAllocateC2B(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
//        //出库
//        BaseResponse basicResult1 = noticeAllocateOut(erpNoticeAllocateRequest);
//        if (basicResult1.isFlag()) {
//            //入库
//            BaseResponse basicResult2 = reileyErpService.noticeAllocateIn(erpNoticeAllocateRequest);
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
     * 移仓调拨通知 出库
     * @param erpNoticeAllocateRequest
     * @return
     */
    public BaseResponse noticeAllocateOut(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(erpNoticeAllocateRequest.getBillid());//ID
        tOrder.setClientCode(erpNoticeAllocateRequest.getFromclient());
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpNoticeAllocateRequest.getFromclient());
            tOrder.setWhId(client.getWhId());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }

        //XSCK出库单
        if (erpNoticeAllocateRequest.getBilltypeid().toUpperCase().equals("XSCK")) {
            tOrder.setTypeId(1155);
        } else {
            tOrder.setTypeId(2456);
        }
        tOrder.setStatus("N");
        tOrder.setOrderDate(erpNoticeAllocateRequest.getBillldate());
        tOrder.setStoreOrderNumber(erpNoticeAllocateRequest.getErp_no());
        tOrder.setCreateDate(DateUtil.getCurTimestamp());
        tOrder.setShipToName(erpNoticeAllocateRequest.getContact());
        tOrder.setShipToPhone(erpNoticeAllocateRequest.getTel());
        tOrder.setShipToAddr1(erpNoticeAllocateRequest.getAddress());
        //明细
        List<TOrderDetail> tOrderDetailList = Lists.newArrayList();
        erpNoticeAllocateRequest.getGrid().forEach(detail -> {
            TOrderDetail tOrderDetail = new TOrderDetail();
            tOrderDetail.setWhId(client.getWhId());
            tOrderDetail.setOrderNumber(erpNoticeAllocateRequest.getBillid());
            tOrderDetail.setLineNumber(String.valueOf((int)((Math.random()*9+1)*10000)));
            //tOrderDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
            tOrderDetail.setPoLineNumber(detail.getBilldtlid());
            tOrderDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(),detail.getFromskucode()));
            tOrderDetail.setOrderUom("EA");//默认EA
            tOrderDetail.setQty((double)detail.getBasequantity());
            tOrderDetail.setAfoPlanQty((double)detail.getBasequantity());
            tOrderDetail.setCustPart(detail.getFromprescancust());
            tOrderDetail.setZone(detail.getFromzone());
            if(detail.getFromprescanned().equals("Y")){
                tOrderDetail.setStorageType(9L);
            }
            tOrderDetail.setLotNumber(detail.getBatchcode());
            tOrderDetailList.add(tOrderDetail);
        });
        tOrder.setOrderDetailList(tOrderDetailList);
        BaseResponse basicResult = outBoundService.createOutBoundOrder(tOrder);
        if (basicResult.isFlag()) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(basicResult.getSubMessage());
        }
    }

    /**
     * 移仓调拨通知 入库
     * @param erpNoticeAllocateRequest
     * @return
     */
    public BaseResponse noticeAllocateIn(ErpNoticeAllocateRequest erpNoticeAllocateRequest){
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setPoNumber(erpNoticeAllocateRequest.getBillid());//ERP单据 ID  getErp_no
        tPoMaster.setClientCode(erpNoticeAllocateRequest.getToclient());
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(null, erpNoticeAllocateRequest.getToclient());
            tPoMaster.setWhId(client.getWhId());
        } catch (Exception e) {
            return BaseResponse.fail("获取客户信息失败!");
        }
        //1120调拨入库
        tPoMaster.setTypeId(1120);
        tPoMaster.setVendorCode("7510");//默认供应商编码
        tPoMaster.setCreateDate(DateUtil.getCurTimestamp());
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");
        //明细
        List<TPoDetail> tPoDetailList = Lists.newArrayList();
        erpNoticeAllocateRequest.getGrid().forEach(detail -> {
            TPoDetail tPoDetail = new TPoDetail();
            tPoDetail.setPoNumber(erpNoticeAllocateRequest.getBillid());
            //tPoDetail.setLineNumber(String.valueOf((int)(Math.random()*100)));
            tPoDetail.setLineNumber(String.valueOf((int)((Math.random()*9+1)*10000)));
            //tPoDetail.setLineNumber(String.valueOf((int)((int)(Math.random()*100) + Integer.parseInt(detail.getBilldtlid().substring(1,3)) + (int)(Math.random()*100) +(int)(Math.random()*100))/3));
            tPoDetail.setSoLineNumber(detail.getBilldtlid());
            tPoDetail.setWhId(client.getWhId());
            tPoDetail.setItemNumber(Joiner.on('-').join(client.getClientCode(),detail.getToskucode()));
            tPoDetail.setScheduleNumber(Integer.parseInt(detail.getBilldtlid()));//同solineNumber
            tPoDetail.setOrderUom("EA");//默认EA
            tPoDetail.setQty((double)detail.getBasequantity());
            tPoDetail.setSoNumber(detail.getToprescancust());
            tPoDetail.setLocationId(detail.getTozone());
            if(detail.getToprescanned().equals("Y")){
                tPoDetail.setSpecialProcessing("YES");
            }
            tPoDetail.setLotNumber(detail.getBatchcode());
            tPoDetailList.add(tPoDetail);
        });
        tPoMaster.setDetailList(tPoDetailList);
        BaseResponse basicResult = inBoundService.createInBoundOrder(tPoMaster);
        if (basicResult.isFlag()) {
            return BaseResponse.success();
        } else {
            return BaseResponse.fail(basicResult.getSubMessage());
        }
    }

}
