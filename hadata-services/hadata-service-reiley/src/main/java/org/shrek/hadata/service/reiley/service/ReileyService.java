package org.shrek.hadata.service.reiley.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jd.open.api.sdk.internal.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.StringUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.*;
import org.shrek.hadata.service.iwms.model.TExpress;
import org.shrek.hadata.service.reiley.model.*;
import org.shrek.hadata.service.reiley.service.scf.model.KdniaoDistRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月22日 14:33
 */
@Service
@Slf4j
public class ReileyService {

    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    WarehouseService warehouseService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    CarrierService carrierService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    WayBillService wayBillService;

    public DeliveryOrderResponse createDeliveryOrder(DeliveryOrderRequest deliveryOrder) {

        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(deliveryOrder.getDeliveryOrder().getDeliveryOrderCode());
        tOrder.setOrderSerialNumber("");
        String whId = warehouseService.getWarehouseIdByCode(deliveryOrder.getDeliveryOrder().getWarehouseCode());
        if (!StringUtil.isEmpty(whId)) {
            tOrder.setWhId(whId);
        } else {
            return DeliveryOrderResponse.fail("获取仓库信息失败!");
        }

        TClient client = clientService.getClientByWhAndLongCode(whId, deliveryOrder.getDeliveryOrder().getShopNick());
        if (client != null) {
            tOrder.setClientCode(client.getClientCode());
        } else {
            return DeliveryOrderResponse.fail("获取客户信息失败!" + deliveryOrder.getDeliveryOrder().getShopNick());
        }
        tOrder.setTypeId(1154);
        tOrder.setOrderDate(DateUtil.parseDate(deliveryOrder.getDeliveryOrder().getPayTime(), DateUtil.DATE_FORMAT_DATETIME));
        tOrder.setStatus("N");
        TCarrier carrier = carrierService.getCarrierByCode(deliveryOrder.getDeliveryOrder().getLogisticsCode());
        if (carrier != null) {
            tOrder.setCarrierId(carrier.getCarrierId());
        } else {
            return DeliveryOrderResponse.fail("获取快递信息失败!");
        }
        tOrder.setCarrierScac(deliveryOrder.getDeliveryOrder().getLogisticsCode());
        tOrder.setShipToName(deliveryOrder.getDeliveryOrder().getReceiverInfo().getName());
        tOrder.setShipToPhone(deliveryOrder.getDeliveryOrder().getReceiverInfo().getMobile());
        tOrder.setShipToState(deliveryOrder.getDeliveryOrder().getReceiverInfo().getProvince());
        tOrder.setShipToCity(deliveryOrder.getDeliveryOrder().getReceiverInfo().getCity());
        tOrder.setShipToAddr2(deliveryOrder.getDeliveryOrder().getReceiverInfo().getArea());
        tOrder.setShipToZip(deliveryOrder.getDeliveryOrder().getReceiverInfo().getZipCode());
        tOrder.setShipToAddr1(deliveryOrder.getDeliveryOrder().getReceiverInfo().getDetailAddress());
        tOrder.setDeliveryName(deliveryOrder.getDeliveryOrder().getSenderInfo().getName());
        tOrder.setDeliveryPhone(deliveryOrder.getDeliveryOrder().getSenderInfo().getMobile());
        tOrder.setDeliveryState(deliveryOrder.getDeliveryOrder().getSenderInfo().getProvince());
        tOrder.setDeliveryCity(deliveryOrder.getDeliveryOrder().getSenderInfo().getCity());
        tOrder.setDeliveryAddr2(deliveryOrder.getDeliveryOrder().getSenderInfo().getArea());
        tOrder.setDeliveryZip(deliveryOrder.getDeliveryOrder().getSenderInfo().getZipCode());
        tOrder.setDeliveryAddr1(deliveryOrder.getDeliveryOrder().getSenderInfo().getDetailAddress());
        tOrder.setInterfacePlatform(deliveryOrder.getDeliveryOrder().getSourcePlatformCode());
        List<TOrderDetail> details = Lists.newArrayList();
        List<DeliveryOrderRequest.OrderLinesBean> orderLines = deliveryOrder.getOrderLines();
        List<String> noFindIteams = Lists.newArrayList();
        orderLines.forEach(line -> {
            TOrderDetail detail = new TOrderDetail();
            detail.setWhId(whId);
            detail.setOrderNumber(deliveryOrder.getDeliveryOrder().getDeliveryOrderCode());
            detail.setLineNumber(line.getOrderLineNo());
            log.warn("日砾新增3:"+new BigDecimal(line.getRetailPrice())+",日砾新增4:"+new BigDecimal(line.getActualPrice()));
            detail.setUnitPrice(new BigDecimal(line.getRetailPrice()));
            detail.setUnitPriceReal(new BigDecimal(line.getActualPrice()));
            TItemMaster tItemMaster = materielService.getItemByCode(line.getItemCode(), client.getClientCode());
            if (tItemMaster != null) {
                detail.setItemNumber(tItemMaster.getItemNumber());
                detail.setQty(Double.parseDouble(line.getPlanQty()));
                detail.setAfoPlanQty(Double.parseDouble(line.getPlanQty()));
                detail.setOrderUom("EA");
                details.add(detail);
            } else {
                noFindIteams.add(line.getItemCode());
            }
        });

        if (noFindIteams.size() != 0) {

            String noFindIteam = Joiner.on(',').join(noFindIteams);
            return DeliveryOrderResponse.fail("物料代码：" + noFindIteam + "信息获取失败!");
        }

        tOrder.setOrderDetailList(details);
        log.warn(JacksonUtil.nonEmpty().toJson(tOrder));
        BaseResponse<TOrder> basicResult = outBoundService.createOutBoundOrder(tOrder);
        if (basicResult.isFlag()) {
            DeliveryOrderResponse response = new DeliveryOrderResponse();
            response.setFlag("success");
            response.setCode("0");
            response.setDeliveryOrderId(Joiner.on('-').join(basicResult.getData().getOrderNumber(),basicResult.getData().getWhId()));
            response.setMessage("创建成功!");
            response.setCreateTime(DateUtil.getShortDateStr());
            return response;
        } else {
            return DeliveryOrderResponse.fail(basicResult.getSubMessage());
        }
    }


    public ReturnOrderResponse createReturnOrder(ReturnOrderRequest returnOrderRequest) {

        TPoMaster tPoMaster = new TPoMaster();
        String whId = warehouseService.getWarehouseIdByCode(returnOrderRequest.getReturnOrder().getWarehouseCode());
        if (!StringUtil.isEmpty(whId)) {
            tPoMaster.setWhId(whId);
        } else {
            return ReturnOrderResponse.fail(returnOrderRequest.getReturnOrder().getPreDeliveryOrderId(), "获取仓库信息失败!");
        }
        TClient client = clientService.getClientByWhAndLongCode(whId, returnOrderRequest.getReturnOrder().getExtendProps().getShop_name());
        if (client != null) {
            tPoMaster.setClientCode(client.getClientCode());
        } else {
            return ReturnOrderResponse.fail(returnOrderRequest.getReturnOrder().getPreDeliveryOrderId(), "获取客户信息失败!");
        }
        tPoMaster.setPoNumber(returnOrderRequest.getReturnOrder().getReturnOrderCode());
        tPoMaster.setTypeId(1121);
        tPoMaster.setVendorCode("7510");
        tPoMaster.setCreateDate(DateUtil.getCurTimestamp());
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");
        tPoMaster.setServiceLevel(returnOrderRequest.getReturnOrder().getExpressCode());
        tPoMaster.setShipFromName(returnOrderRequest.getReturnOrder().getSenderInfo().getName());
        tPoMaster.setShipFromPostalCode(returnOrderRequest.getReturnOrder().getSenderInfo().getZipCode());
        tPoMaster.setShipFromPhone(returnOrderRequest.getReturnOrder().getSenderInfo().getMobile());
        tPoMaster.setShipFromState(returnOrderRequest.getReturnOrder().getSenderInfo().getProvince());
        tPoMaster.setShipFromCity(returnOrderRequest.getReturnOrder().getSenderInfo().getCity());
        tPoMaster.setShipFromArea(returnOrderRequest.getReturnOrder().getSenderInfo().getArea());
        tPoMaster.setShipFromAddr1(returnOrderRequest.getReturnOrder().getSenderInfo().getDetailAddress());
        tPoMaster.setCarrierScac(returnOrderRequest.getReturnOrder().getLogisticsCode());
        log.warn("日砾新增1:"+returnOrderRequest.getReturnOrder().getLogisticsCode());
        tPoMaster.setInterfaceId(returnOrderRequest.getReturnOrder().getPreDeliveryOrderCode());
        List<String> noFindIteams = Lists.newArrayList();
        List<TPoDetail> details = Lists.newArrayList();
        returnOrderRequest.getOrderLines().forEach(orderLinesBean -> {
            TPoDetail detail = new TPoDetail();
            detail.setPoNumber(returnOrderRequest.getReturnOrder().getReturnOrderCode());
            detail.setLineNumber(orderLinesBean.getOrderLineNo());
            TItemMaster tItemMaster = materielService.getItemByCode(orderLinesBean.getItemCode(), client.getClientCode());

            if (tItemMaster != null) {
                detail.setItemNumber(tItemMaster.getItemNumber());
                detail.setScheduleNumber(Integer.parseInt(orderLinesBean.getOrderLineNo()));
                detail.setWhId(whId);
                detail.setOrderUom("EA");
                detail.setQty(Double.parseDouble(orderLinesBean.getPlanQty()));
            } else {
                noFindIteams.add(orderLinesBean.getItemCode());
            }
            details.add(detail);
        });
        if (noFindIteams.size() != 0) {

            String noFindIteam = Joiner.on(',').join(noFindIteams);
            return ReturnOrderResponse.fail(returnOrderRequest.getReturnOrder().getReturnOrderCode(), "物料代码：" + noFindIteam + "信息获取失败!");
        }
        tPoMaster.setDetailList(details);
        log.warn("日砾新增2InterfaceId:"+tPoMaster.getInterfaceId());
        log.warn("日砾新增3expresscode:"+tPoMaster.getServiceLevel());
        BaseResponse<TPoMaster> basicResult = inBoundService.createInBoundOrder(tPoMaster);
        if (basicResult.isFlag()) {
            return ReturnOrderResponse.success(returnOrderRequest.getReturnOrder().getReturnOrderCode());
        } else {
            return ReturnOrderResponse.fail(returnOrderRequest.getReturnOrder().getReturnOrderCode(), basicResult.getMessage());
        }
    }


    public OrderCancelResponse cancelOrder(OrderCancelRequest orderCancelRequest) {
        String whId = warehouseService.getWarehouseIdByCode(orderCancelRequest.getWarehouseCode());

        if (StringUtil.isEmpty(whId)) {
            return OrderCancelResponse.fail("获取仓库信息失败!");
        }

        if (orderCancelRequest.getOrderType().endsWith("CK")) {
            //出库订单取消
            BaseResponse<String> basicResult = outBoundService.cancelOutBoundOrder(orderCancelRequest.getOrderCode(), whId);
            if (basicResult.isFlag()) {
                return OrderCancelResponse.success();
            } else {
                return OrderCancelResponse.fail(basicResult.getSubMessage());
            }
        } else {
            //入库订单取消
            BaseResponse<String> basicResult = inBoundService.cancelInBoundOrder(orderCancelRequest.getOrderCode(), whId);
            if (basicResult.isFlag()) {
                return OrderCancelResponse.success();
            } else {
                return OrderCancelResponse.fail(basicResult.getSubMessage());
            }
        }
    }

    public TOrder queryDeliveryOrder(String orderNumber, String whse) {
        return outBoundService.queryOutBounds(orderNumber, whse);
    }

    public String getCarrierPrintTemplateByCode(String carrierCode) {
        return carrierService.getCarrierByCode(carrierCode).getNotes();
    }

    public void updateWayBillCode(String orderNumber, String whse, String billcode) {
        wayBillService.updateWayBillCode(orderNumber, whse, billcode);
    }

    /**
     * 更新快递节点
     * @param kdniaoDistRequest
     * @return
     */
    public BaseResponse updateExpressInfo (KdniaoDistRequest kdniaoDistRequest){
        try {
            List<KdniaoDistRequest.KdniaoDistRequestData> expressData = kdniaoDistRequest.getData();
            if (expressData != null && expressData.size() > 0) {
                List<TExpress> expressList = new ArrayList<TExpress>();
                expressData.forEach(exContent -> {
                    exContent.getTraces().forEach(trace -> {
                        TExpress express = new TExpress();
                        express.setShipperCode(exContent.getShipperCode());
                        express.setAcceptStation(trace.getAcceptStation());
                        express.setAcceptTime(trace.getAcceptTime());
                        express.setLogisticCode(exContent.getLogisticCode());
                        expressList.add(express);
                    });
                    String jsonList = JacksonUtil.nonEmpty().toJson(expressList);
                    BaseResponse responseSingle = outBoundService.updateExpressInfo(jsonList, exContent.getShipperCode(), exContent.getLogisticCode());
                });
            }

            return BaseResponse.success();
        }
        catch(Exception e){
            return BaseResponse.fail(e.getMessage());
        }
    }
}
