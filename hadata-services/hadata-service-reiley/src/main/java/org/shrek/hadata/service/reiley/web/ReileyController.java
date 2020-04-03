package org.shrek.hadata.service.reiley.web;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.StringUtil;
import org.shrek.hadata.commons.web.BasicResult;
import org.shrek.hadata.service.hwms.model.TOrder;
import org.shrek.hadata.service.iwms.model.ScfStoredBatchInfo;
import org.shrek.hadata.service.iwms.model.TExpress;
import org.shrek.hadata.service.reiley.model.*;
import org.shrek.hadata.service.reiley.service.ReileyService;
import org.shrek.hadata.service.reiley.service.erp.ReileyErpService;
import org.shrek.hadata.service.reiley.service.scf.SupplyChainFinanceService;
import org.shrek.hadata.service.reiley.service.scf.model.*;
import org.shrek.hadata.service.reiley.service.waybill.WayBillServiceFactory;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillDto;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillPrintDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月20日 14:00
 */
@Slf4j
@RestController
@RequestMapping
public class ReileyController {

    @Autowired
    ReileyService reileyService;
    @Autowired
    ReileyErpService reileyErpService;
    @Autowired
    SupplyChainFinanceService supplyChainFinanceService;
    @Autowired
    WayBillServiceFactory wayBillServiceFactory;
    @Autowired
    private ApplicationEventPublisher publisher;

    @RequestMapping("/index")
    @ResponseBody
    public BasicResult index() {

        return BasicResult.success("Reiley服务后台");
    }

    /**
     * B2C发货单创建接口
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deliveryorder.create")
    public DeliveryOrderResponse createDeliveryOrder(@RequestBody String content) {
        log.warn(content);
        DeliveryOrderRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, DeliveryOrderRequest.class);
        return reileyService.createDeliveryOrder(wrapper);
    }


    /**
     * B2C退货入库单创建接口
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/returnorder.create")
    public ReturnOrderResponse createReturnOrder(@RequestBody String content) {
        log.warn(content);
        ReturnOrderRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, ReturnOrderRequest.class);
        return reileyService.createReturnOrder(wrapper);
    }


    /**
     * B2C单据取消接口
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order.cancel")
    public OrderCancelResponse cancelOrder(@RequestBody String content) {
        log.warn(content);
        OrderCancelRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, OrderCancelRequest.class);
        return reileyService.cancelOrder(wrapper);
    }

    /**
     * B2C快递单创建接口
     *
     * @param content
     * @return
     */
    @RequestMapping(value = "/waybill.create")
    public String createWayBill(@RequestBody String content) {
        HashMap<String, String> request = JacksonUtil.nonEmpty(JacksonUtil.Type.JSON).fromJson(content, HashMap.class);
        TOrder tOrder = reileyService.queryDeliveryOrder(request.get("order_code"), request.get("whse_code"));
        WaybillDto waybill = new WaybillDto();
        waybill.setSalePlatform(tOrder.getInterfacePlatform());
        waybill.setPlatformOrderNo(tOrder.getOrderSerialNumber());
        waybill.setVendorCode("667679");
        waybill.setVendorName("惠氏旗舰店");
        waybill.setProviderCode(tOrder.getCarrierScac());
        waybill.setVendorOrderCode(tOrder.getOrderNumber());
        waybill.setPlatformOrderNo(tOrder.getOrderNumber());
        waybill.setWeight(0);
        waybill.setVolume(0);
        waybill.setPrintTemplate(reileyService.getCarrierPrintTemplateByCode(tOrder.getCarrierScac()));
        WaybillDto.WaybillAddress fromAddress = new WaybillDto.WaybillAddress();
        fromAddress.setContact(tOrder.getDeliveryName());
        fromAddress.setMobile(tOrder.getDeliveryPhone());
        fromAddress.setPhone(tOrder.getDeliveryPhone());
        fromAddress.setProvinceName(tOrder.getDeliveryState());
        fromAddress.setCityName(tOrder.getDeliveryCity());
        fromAddress.setCountryName(tOrder.getDeliveryAddr2());
        fromAddress.setAddress(tOrder.getBillFrghtToAddr1());
        waybill.setFromAddress(fromAddress);
        WaybillDto.WaybillAddress toAddress = new WaybillDto.WaybillAddress();
        toAddress.setContact(tOrder.getShipToName());
        toAddress.setMobile(tOrder.getShipToPhone());
        toAddress.setPhone(tOrder.getShipToPhone());
        toAddress.setProvinceName(tOrder.getShipToState());
        toAddress.setCityName(tOrder.getShipToCity());
        toAddress.setCountryName(tOrder.getShipToAddr2());
        toAddress.setAddress(tOrder.getShipToAddr1());
        waybill.setToAddress(toAddress);
        BaseResponse<WaybillPrintDto> response = wayBillServiceFactory.build(tOrder.getInterfacePlatform()).newWayBill(waybill);
        if (response.isFlag()) {
            if (response.getData() != null) {
                reileyService.updateWayBillCode(tOrder.getOrderNumber(), tOrder.getWhId(), response.getData().getTask().getId());
                return JacksonUtil.nonEmpty().toJson(response.getData());
            } else {
                return JacksonUtil.nonEmpty().toJson(BaseResponse.fail(response.getSubMessage()));
            }
        } else {
            return JacksonUtil.nonEmpty().toJson(BaseResponse.fail(response.getSubMessage()));
        }
    }


    /**
     * 金融库存查询
     * SCF_INVENTORY_QUANTITY_QUERY
     *
     * @param inventoryQuantityRequest
     * @return
     */
    @RequestMapping(value = "/SCF_INVENTORY_QUANTITY_QUERY")
    public String queryInventoryQuantity(@RequestBody InventoryQuantityRequest inventoryQuantityRequest) {
        InventoryQuantityReponse reponse = new InventoryQuantityReponse();
        List<InventoryQuantityReponse.Item> items = Lists.newArrayList();
        inventoryQuantityRequest.getWmsInventoryQuantityQueryList().forEach(query -> {
            List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(query.getOwnerUserId());
            InventoryQuantityReponse.Item item = supplyChainFinanceService.getInventoryQuantity(list.get(1), list.get(0), query.getItemCode());
            item.setStoreCode(query.getStoreCode());
            items.add(item);
        });
        reponse.setSuccess(true);
        reponse.setItems(items);
        return JacksonUtil.nonEmpty().toJson(reponse);
    }

    /**
     * 菜鸟查询批次库存
     * SCF_INVENTORY_QUANTITY_QUERY_BATCH
     *
     * @param inventoryQuantitBbatchRequest
     * @return
     */
    @RequestMapping(value = "/SCF_INVENTORY_QUANTITY_QUERY_BATCH")
    public String queryInventoryQuantityBatch(@RequestBody InventoryQuantityBatchRequest inventoryQuantitBbatchRequest) {
        InventoryQuantityBatchReponse reponse = new InventoryQuantityBatchReponse();
        List<InventoryQuantityBatchReponse.Item> items = Lists.newArrayList();
        List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(inventoryQuantitBbatchRequest.getOwnerUserId());
        inventoryQuantitBbatchRequest.getItemCodes().forEach(query -> {
            //InventoryQuantityBatchReponse.Item item = supplyChainFinanceService.getInventoryQuantityBatch(list.get(1), list.get(0), query.getItemCode());
            //item.setStoreCode(query.getStoreCode());
            BaseResponse<HashMap<String, ScfStoredBatchInfo>> resp = supplyChainFinanceService.getInventoryQuantityBatch2(list.get(1), list.get(0), query);
            resp.getData().forEach((k,v)->{
                InventoryQuantityBatchReponse.Item item = new InventoryQuantityBatchReponse.Item();
                item.setItemCode(v.getItemCode());
                item.setBatchCode(v.getBatchCode());
                item.setQuantity(v.getQuantity() + v.getLockQuantity());
                item.setLockQuantity(v.getLockQuantity());
                item.setInventoryType(v.getInventoryType());
                item.setProduceDate(v.getProduceDate());
                item.setOverdueDate(v.getOverdueDate());
                item.setGuaranteePeriod(v.getGuaranteePeriod());
                items.add(item);
            });

        });
        reponse.setSuccess(true);
        reponse.setOwnerUserId(inventoryQuantitBbatchRequest.getOwnerUserId());
        reponse.setCpStoreCode(inventoryQuantitBbatchRequest.getCpStoreCode());
        reponse.setItems(items);
        reponse.setErrorCode("");
        reponse.setErrorMsg(" ");
        return JacksonUtil.nonEmpty().toJson(reponse);
    }

    /**
     * 金融库存查询
     * SCF_MESSAGE_NOTIFY
     *
     * @param messageNotifyRequest
     * @return
     */
    @RequestMapping(value = "/SCF_MESSAGE_NOTIFY")
    public String messageNotify(@RequestBody MessageNotifyRequest messageNotifyRequest) {
        MessageNotifyReponse reponse = new MessageNotifyReponse();
        MessageNotifyRequest.Content content = JacksonUtil.nonEmpty().fromJson(messageNotifyRequest.getMsgContent(), MessageNotifyRequest.Content.class);
        try {
            List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(content.getOwnerUserId());
            if (StringUtil.isEmpty(content.getOrderCode())) {
                supplyChainFinanceService.updateClientSendControl(list.get(0), list.get(1), messageNotifyRequest.getMsgCode().equals("STOCK_OUT_USER_ACCEPT") ? "1" : "0");
            } else {
                supplyChainFinanceService.updateMassiveStockOutStatus(list.get(1), list.get(0), content.getOrderCode(),
                        messageNotifyRequest.getMsgCode().equals("STOCK_OUT_ORDER_ACCEPT") ? "A" : "N");
            }
            reponse.setSuccess(true);
        } catch (Exception e) {
            reponse.setSuccess(false);
            reponse.setErrorCode("500100");
            reponse.setErrorMsg("消息处理失败");
        }
        return JacksonUtil.nonEmpty().toJson(reponse);
    }

    /**
     * 快递鸟推送节点信息
     * kdniaoDist
     *
     * @param kdniaoDistRequest
     * @return
     */
    @RequestMapping(value = "/kdniaoDist")
    public String createOrderDist(@RequestBody KdniaoDistRequest kdniaoDistRequest) {
        log.warn("快递鸟快递节点推送报文接收逻辑开始");
        //KdniaoDistRequest request = JacksonUtil.nonEmpty().fromJson(kdniaoDistRequest,KdniaoDistRequest.class);
        KdniaoDistResponse reponse = new KdniaoDistResponse();
        //MessageNotifyRequest.Content content = JacksonUtil.nonEmpty().fromJson(messageNotifyRequest.getMsgContent(), MessageNotifyRequest.Content.class);
        try {
            reileyService.updateExpressInfo(kdniaoDistRequest);

            /*List<KdniaoDistRequest.KdniaoDistRequestData> expressData = kdniaoDistRequest.getData();
            if(expressData != null && expressData.size()>0){
                List<TExpress> expressList = Lists.newArrayList();
                expressData.forEach(exContent -> {
                    exContent.getTraces().forEach(trace ->{
                        TExpress express = new TExpress();
                        express.setShipperCode(exContent.getShipperCode());
                        express.setAcceptStation(trace.getAcceptStation());
                        express.setAcceptTime(trace.getAcceptTime());
                        express.setLogisticCode(exContent.getLogisticCode());
                        expressList.add(express);
                    });

                    //BaseResponse responseSingle = reileyErpService.updateExpressInfo()
                });
            }*/
           /* List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(content.getOwnerUserId());
            if (StringUtil.isEmpty(content.getOrderCode())) {
                supplyChainFinanceService.updateClientSendControl(list.get(0), list.get(1), messageNotifyRequest.getMsgCode().equals("STOCK_OUT_USER_ACCEPT") ? "1" : "0");
            } else {
                supplyChainFinanceService.updateMassiveStockOutStatus(list.get(1), list.get(0), content.getOrderCode(),
                        messageNotifyRequest.getMsgCode().equals("STOCK_OUT_ORDER_ACCEPT") ? "A" : "N");
            }*/
            reponse.setSuccess(true);
        } catch (Exception e) {
            reponse.setSuccess(false);
            reponse.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            reponse.setReason("消息处理失败," + e.getMessage());
        }
        reponse.setSuccess(true);
        log.warn("快递鸟快递节点推送报文接收：" + JacksonUtil.nonEmpty().toJson(kdniaoDistRequest));
        return JacksonUtil.nonEmpty().toJson(reponse);
    }
}
