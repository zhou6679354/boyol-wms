package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.service.bus.web.model.*;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Slf4j
@Service
public class SZService {

    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;



    /**
     *入库单创建
     * @param orderRequests
     * @return
     */
    public SZResponse createSZInOrder(SZInOrderRequest orderRequests){
        String message = "";
        SZResponse szResponse;
        TPoMaster poMasterOrder;
        try {
            if(orderRequests.getWhId()==null){
                message="入库单创建失败,单号："+orderRequests.getErp_no()+"仓库货主信息有误!";
                log.error(message);
                return SZResponse.fail(message,"301");
            }else{
                poMasterOrder=inBoundService.queryTPoMasterByWhIdAndPoNumber(orderRequests.getWhId(),orderRequests.getErp_no());
            }
                if(poMasterOrder!=null){
                    message="入库单创建失败,单号："+orderRequests.getErp_no()+"已存在!";
                    log.error(message);
                    szResponse=SZResponse.fail(message,"301");
                }else{
                    TPoMaster poMaster = new TPoMaster();
                    List<TPoDetail> poDetails = new ArrayList<TPoDetail>();
                    poMaster.setPoNumber(orderRequests.getErp_no());
                    poMaster.setCreateDate(orderRequests.getBilllDate());
                    poMaster.setTypeId(orderRequests.getBillTypeId());
                    poMaster.setClientCode(orderRequests.getStorageLocation());
                    poMaster.setWhId(orderRequests.getWhId());
                    for (int y = 0; y < orderRequests.getGrid().size(); y++) {
                        TItemMaster itemMaster=materielService.getItemMasterByWhIdandItemNumber(orderRequests.getWhId(),orderRequests.getStorageLocation()+"-"+orderRequests.getGrid().get(y).getSkuCode());
                        if(itemMaster==null){
                            message="入库单创建失败,单号："+orderRequests.getErp_no()+"单据信息有误!商品信息不存在"+orderRequests.getGrid().get(y).getSkuCode();
                            log.error(message);
                            return SZResponse.fail(message,"301");
                        }else{
                            TPoDetail poDetail = new TPoDetail();
                            poDetail.setPoNumber(orderRequests.getErp_no());
                            poDetail.setWhId(orderRequests.getWhId());
                            poDetail.setQty(orderRequests.getGrid().get(y).getBaseQuantity());
                            poDetail.setActualQty(orderRequests.getGrid().get(y).getBaseQuantity());
                            poDetail.setItemNumber(orderRequests.getStorageLocation() +"-"+orderRequests.getGrid().get(y).getSkuCode());
                            poDetail.setStCode(String.valueOf(orderRequests.getGrid().get(y).getQuantity()));
                            poDetail.setStName(orderRequests.getGrid().get(y).getMaterialType());
                            poDetail.setLineNumber(String.valueOf(y+1));
                            poDetail.setSoNumber(orderRequests.getGrid().get(y).getPreScanCust());
                            poDetail.setRemark(orderRequests.getGrid().get(y).getNoteDtl());
                            poDetails.add(poDetail);
                        }
                    }
                    poMaster.setDetailList(poDetails);
                    inBoundService.createInBoundOrder(poMaster);
                    message="入库单创建成功,单号："+orderRequests.getErp_no();
                    log.info(message);
                    szResponse=SZResponse.success(message,"200");
                }
        }catch (Exception e){
            message="入库单创建失败,单号："+orderRequests.getErp_no()+"字段信息有误请确认!"+e.getStackTrace()[0];
            log.error("入库单创建失败:",e.getMessage());
            szResponse=SZResponse.fail(message,"301");
        }
        return szResponse;
    }
    /**
     * 出库单创建
     * @param orderRequest
     * @return
     */
    public SZResponse createSZOutOrder(SZOutOrderRequest orderRequest) {
        String message = "";
        SZResponse szResponse;
        TOrder orders;
        try {
            if(orderRequest.getWhId()==null){
                message="出库单创建失败,单号："+orderRequest.getErp_no()+"仓库货主信息有误!";
                log.error(message);
                return SZResponse.fail(message,"301");
            }else{
                orders=outBoundService.queryTOrderByWhIdAndOrderNumber(orderRequest.getWhId(),orderRequest.getErp_no());
            }
            if(orders!=null){
                message="出库单创建失败,单号："+orderRequest.getErp_no()+"已存在!";
                log.error(message);
                szResponse=SZResponse.fail(message,"301");
            }else{
                TOrder order = new TOrder();
                List<TOrderDetail> orderDetails = new ArrayList<TOrderDetail>();
                order.setTypeId(orderRequest.getBillTypeId());
                order.setOrderNumber(orderRequest.getErp_no());
                order.setCreateDate(orderRequest.getBilllDate());
                order.setServiceLevel(orderRequest.getNote());
                order.setShipToName(orderRequest.getDeliverycon());
                order.setShipToAddr1(orderRequest.getDeliveryAddress());
                order.setShipToAddr2(orderRequest.getDeliveryAddress());
                order.setShipToAddr3(orderRequest.getDeliveryAddress());
                order.setShipToPhone(orderRequest.getDeliverytel());
                order.setClientCode(orderRequest.getStorageLocation());
                order.setWhId(orderRequest.getWhId());
                for (int y = 0; y < orderRequest.getGrid().size(); y++) {
                    TItemMaster itemMaster=materielService.getItemMasterByWhIdandItemNumber(orderRequest.getWhId(),orderRequest.getStorageLocation()+"-"+orderRequest.getGrid().get(y).getSkuCode());
                    if(itemMaster==null){
                        message="出库单创建失败,单号："+orderRequest.getErp_no()+"单据信息有误!商品信息不存在"+orderRequest.getGrid().get(y).getSkuCode();
                        log.error(message);
                        return SZResponse.fail(message,"301");
                    }else{
                        TOrderDetail orderDetail = new TOrderDetail();
                        orderDetail.setOrderNumber(orderRequest.getErp_no());
                        orderDetail.setWhId(orderRequest.getWhId());
                        orderDetail.setQty(orderRequest.getGrid().get(y).getBaseQuantity());
                        orderDetail.setAfoPlanQty(orderRequest.getGrid().get(y).getBaseQuantity());
                        orderDetail.setItemNumber(orderRequest.getStorageLocation() +"-"+ orderRequest.getGrid().get(y).getSkuCode());
                        orderDetail.setStCode(String.valueOf(orderRequest.getGrid().get(y).getQuantity()));
                        orderDetail.setStName(orderRequest.getGrid().get(y).getMaterialType());
                        orderDetail.setLineNumber(String.valueOf(y+1));
                        orderDetail.setCustPart(orderRequest.getGrid().get(y).getPreScanCust());
                        orderDetails.add(orderDetail);
                    }
                }
                order.setOrderDetailList(orderDetails);
                outBoundService.createOutBoundOrder(order);
                message = "出库单创建成功，单号："+orderRequest.getErp_no();
                log.info(message);
                szResponse=SZResponse.success(message,"200");
            }
        }catch (Exception e){
            message="出库单创建失败,单号："+orderRequest.getErp_no()+"字段信息有误请确认!"+e.getStackTrace()[0];
            log.error("出库单创建失败:",e.getMessage());
            szResponse=SZResponse.fail(message,"301");
        }
        return szResponse;
    }
    /**
     * 基础信息
     * @param masterRequest
     * @return
     */
    public SZResponse SingleitemSynchronize(List<SZMasterRequest> masterRequest){
        String message="";
        List<TItemMaster> itemMasters=new ArrayList<TItemMaster>();
        for(int i=0;masterRequest.size()>i;i++){
            TItemMaster itemMaster=new TItemMaster();
            List<TItemUom> itemUoms=new ArrayList<TItemUom>();
            TItemUom itemUomEA=new TItemUom();
            TItemUom itemUomCS=new TItemUom();
            itemMaster.setItemNumber(masterRequest.get(i).getClientCode()+"-"+masterRequest.get(i).getItemNumber());
            itemMaster.setDisplayItemNumber(masterRequest.get(i).getItemNumber());
            itemMaster.setWhId(masterRequest.get(i).getWhId());
            itemMaster.setDescription(masterRequest.get(i).getDescription());
            itemMaster.setPrice(masterRequest.get(i).getPrice());
            itemMaster.setUnitVolume(masterRequest.get(i).getUnitVolume());
            itemMaster.setNestedVolume(masterRequest.get(i).getNestedVolume());
            itemMaster.setUnitVolume(masterRequest.get(i).getUnitVolume());
            itemMaster.setLength(masterRequest.get(i).getLength());
            itemMaster.setWidth(masterRequest.get(i).getWidth());
            itemMaster.setHeight(masterRequest.get(i).getHeight());
            itemMaster.setClientCode(masterRequest.get(i).getClientCode());
            itemMaster.setUom("EA");
            itemUomEA.setItemNumber(masterRequest.get(i).getClientCode()+"-"+masterRequest.get(i).getItemNumber());
            itemUomEA.setPickPutId("UniqueFELotFI");
            itemUomEA.setWhId(masterRequest.get(i).getWhId());
            itemUomEA.setConversionFactor(1.00);
            itemUomEA.setUomPrompt(masterRequest.get(i).getDtl().getUom());
            itemUomEA.setUom("EA");
            itemUoms.add(itemUomEA);
            itemUomCS.setItemNumber(masterRequest.get(i).getClientCode()+"-"+masterRequest.get(i).getItemNumber());
            itemUomCS.setPickPutId("UniqueFELotFI");
            itemUomCS.setWhId(masterRequest.get(i).getWhId());
            itemUomCS.setConversionFactor(masterRequest.get(i).getDtl().getConversionFactor());
            itemUomCS.setUomPrompt(masterRequest.get(i).getDtl().getUom());
            itemUomCS.setUom("CS");
            itemUoms.add(itemUomCS);
            itemMaster.setUoms(itemUoms);
            itemMasters.add(itemMaster);
        }
        try{
            materielService.createMaterials(itemMasters);
            message = "商品信息同步成功";
            log.info(message);
            return SZResponse.success(message,"200");
        }catch (Exception e){
            message="商品信息同步失败原因："+e.getMessage();
            log.error(message);
            return SZResponse.fail(message,"301");
        }
    }
    /**
     *入库单取消
     * @param orderRequest
     * @return
     */
    public SZResponse closeInOrder(CloseOrderRequest orderRequest){
        String message;
        SZResponse szResponse;
        try {
            TPoMaster poMaster=inBoundService.queryTPoMasterByWhIdAndPoNumber(orderRequest.getWhId(),orderRequest.getErp_no());
            if(poMaster==null){
                message="入库单取消失败,单号："+orderRequest.getErp_no()+"不存在!";
                log.info(message);
                szResponse=SZResponse.fail(message,"301");
            }else if(poMaster.getStatus().equals("C")){
                message="入库单取消失败该单已入库上架,单号："+orderRequest.getErp_no();
                log.info(message);
                szResponse=SZResponse.fail(message,"302");
            }else{
                if(poMaster.getStatus().equals("D")){
                    message = "入库单已取消请勿重复操作，单号："+orderRequest.getErp_no();
                    szResponse=SZResponse.success(message,"200");
                }else{
                    TPoMaster poMasterOrder=new TPoMaster();
                    poMasterOrder.setTypeId(orderRequest.getBillTypeId());
                    poMasterOrder.setWhId(orderRequest.getWhId());
                    poMasterOrder.setPoNumber(orderRequest.getErp_no());
                    poMasterOrder.setServiceLevel(orderRequest.getNote());
                    poMasterOrder.setStatus("D");
                    inBoundService.updateInOrderCancel(poMaster);
                    message="入库单取消成功,单号："+orderRequest.getErp_no();
                    log.info(message);
                    szResponse=SZResponse.success(message,"200");
                }
            }
        }catch (Exception e){
            message="入库单取消失败,单号："+orderRequest.getErp_no()+"参数信息有误!";
            log.error("入库单取消失败,单号:",e.getMessage());
            szResponse=SZResponse.fail(message,"401");
        }
        return szResponse;
    }
    /**
     * 出库单取消
     * @param orderRequest
     * @return
     */
    public SZResponse closeOutorder(CloseOrderRequest orderRequest) {
        String message;
        SZResponse szResponse;
        try {
            TOrder order=outBoundService.queryTOrderByWhIdAndOrderNumber(orderRequest.getWhId(),orderRequest.getErp_no());
            if(order==null){
                message="出库单取消失败,单号："+orderRequest.getErp_no()+"不存在!";
                log.info(message);
                szResponse=SZResponse.fail(message,"301");
            }else if(order.getStatus().equals("S")){
                message = "出库单取消失败该单号已发货，单号："+orderRequest.getErp_no();
                log.info(message);
                szResponse=SZResponse.fail(message,"302");
            }else{
                if(order.getStatus().equals("U")){
                    message = "出库单已取消请勿重复操作，单号："+orderRequest.getErp_no();
                    szResponse=SZResponse.success(message,"200");
                }else{
                    TOrder newOrder=new TOrder();
                    newOrder.setTypeId(orderRequest.getBillTypeId());
                    newOrder.setWhId(orderRequest.getWhId());
                    newOrder.setOrderNumber(orderRequest.getErp_no());
                    newOrder.setServiceLevel(orderRequest.getNote());
                    newOrder.setStatus("U");
                    outBoundService.updateOutOrderCancel(newOrder);
                    message = "出库单取消成功，单号："+orderRequest.getErp_no();
                    log.info(message);
                    szResponse=SZResponse.success(message,"200");
                }
            }
        }catch (Exception e){
            message="出库单取消失败,单号："+orderRequest.getErp_no()+"参数信息有误!";
            log.error("出库单取消失败,单号:",e.getMessage());
            szResponse=SZResponse.fail(message,"401");
        }
        return szResponse;
    }
    /**
     * 移仓调拨通知（货主变更）
     * @param orderRequest
     * @return
     */
    public SZResponse shiftOrder(SZShiftOrderRequest orderRequest) {
        String message;
        SZResponse szResponse;
        try {
                        if(orderRequest.getWhId().isEmpty()){
                            message="移仓调拨命令失败,单号："+orderRequest.getErp_no()+"仓库货主信息有误!";
                            log.error(message);
                            return SZResponse.fail(message,"301");
                        }else{
                            TOrder order = new TOrder();
                            List<TOrderDetail> orderDetails = new ArrayList<TOrderDetail>();
                            order.setOrderNumber(orderRequest.getErp_no());
                            order.setTypeId(1155);
                            order.setCreateDate(orderRequest.getBilllDate());
                            order.setServiceLevel(orderRequest.getNote());
                            order.setClientCode(orderRequest.getFromClient());
                            order.setWhId(orderRequest.getWhId());
                            for (int y = 0; y < orderRequest.getGrid().size(); y++) {
                                TItemMaster itemMaster=materielService.getItemMasterByWhIdandItemNumber(orderRequest.getWhId(),orderRequest.getFromClient()+"-"+orderRequest.getGrid().get(y).getFromSkuCode());
                                if(itemMaster==null){
                                    message="移仓调拨命令失败,单号："+orderRequest.getErp_no()+"生成调拨出库单据信息有误!商品信息不存在"+orderRequest.getGrid().get(y).getFromSkuCode();
                                    log.error(message);
                                    return SZResponse.fail(message,"301");
                                }else{
                                    TOrderDetail orderDetail = new TOrderDetail();
                                    orderDetail.setOrderNumber(orderRequest.getErp_no());
                                    orderDetail.setWhId(orderRequest.getWhId());
                                    orderDetail.setQty(orderRequest.getGrid().get(y).getBaseQuantity());
                                    orderDetail.setAfoPlanQty(orderRequest.getGrid().get(y).getBaseQuantity());
                                    orderDetail.setItemNumber(orderRequest.getFromClient() +"-"+ orderRequest.getGrid().get(y).getFromSkuCode());
                                    orderDetail.setStCode(String.valueOf(orderRequest.getGrid().get(y).getQuantity()));
                                    orderDetail.setLineNumber(String.valueOf(y+1));
                                    orderDetail.setCustPart(orderRequest.getGrid().get(y).getFromPreScanCust());
                                    orderDetails.add(orderDetail);
                                }
                            }
                            order.setOrderDetailList(orderDetails);
                            outBoundService.createOutBoundOrder(order);
                            log.info("调拨出库单创建成功成功，单号："+orderRequest.getErp_no());
                            TPoMaster poOrder = new TPoMaster();
                            List<TPoDetail> poDetails = new ArrayList<TPoDetail>();
                            poOrder.setPoNumber(orderRequest.getErp_no());
                            poOrder.setTypeId(1120);
                            poOrder.setCreateDate(orderRequest.getBilllDate());
                            poOrder.setServiceLevel(orderRequest.getNote());
                            poOrder.setClientCode(orderRequest.getToClient());
                            poOrder.setWhId(orderRequest.getWhId());
                            for (int i = 0; i < orderRequest.getGrid().size(); i++) {
                                TItemMaster itemMaster=materielService.getItemMasterByWhIdandItemNumber(orderRequest.getWhId(),orderRequest.getToClient()+"-"+orderRequest.getGrid().get(i).getToSkuCode());
                                if(itemMaster==null){
                                    message="移仓调拨命令失败,单号："+orderRequest.getErp_no()+"生成调拨入库单据信息有误!商品信息不存在"+orderRequest.getGrid().get(i).getToSkuCode();
                                    log.error(message);
                                    return SZResponse.fail(message,"301");
                                }else{
                                    TPoDetail poDetail = new TPoDetail();
                                    poDetail.setPoNumber(orderRequest.getErp_no());
                                    poDetail.setWhId(orderRequest.getWhId());
                                    poDetail.setQty(orderRequest.getGrid().get(i).getBaseQuantity());
                                    poDetail.setActualQty(orderRequest.getGrid().get(i).getBaseQuantity());
                                    poDetail.setItemNumber(orderRequest.getToClient() +"-"+ orderRequest.getGrid().get(i).getToSkuCode());
                                    poDetail.setStCode(String.valueOf(orderRequest.getGrid().get(i).getQuantity()));
                                    poDetail.setLineNumber(String.valueOf(i+1));
                                    poDetail.setSoNumber(orderRequest.getGrid().get(i).getToPreScanCust());
                                    poDetails.add(poDetail);
                                }
                            }
                            poOrder.setDetailList(poDetails);
                            inBoundService.createInBoundOrder(poOrder);
                            log.info("调拨入库单创建成功成功，单号："+orderRequest.getErp_no());
                            message = "移仓调拨成功，单号："+orderRequest.getErp_no();
                            log.info(message);
                            szResponse=SZResponse.success(message,"200");
            }
        }catch (Exception e){
            message="移仓调拨命令失败,单号："+orderRequest.getErp_no()+"字段信息有误请确认!"+e.getStackTrace()[0];
            log.error("移仓调拨命令失败:",e.getMessage());
            szResponse=SZResponse.fail(message,"301");
        }
        return szResponse;
    }
}
