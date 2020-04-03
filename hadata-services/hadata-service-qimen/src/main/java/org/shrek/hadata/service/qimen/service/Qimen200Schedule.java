package org.shrek.hadata.service.qimen.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.qimen.api.DefaultQimenClient;
import com.qimen.api.QimenClient;
import com.qimen.api.QimenRequest;
import com.qimen.api.QimenResponse;
import com.qimen.api.request.DeliveryorderConfirmRequest;
import com.qimen.api.request.EntryorderConfirmRequest;
import com.qimen.api.request.ReturnorderConfirmRequest;
import com.qimen.api.request.StockoutConfirmRequest;
import com.qimen.api.response.DeliveryorderConfirmResponse;
import com.qimen.api.response.EntryorderConfirmResponse;
import com.qimen.api.response.ReturnorderConfirmResponse;
import com.qimen.api.response.StockoutConfirmResponse;
import com.taobao.api.ApiException;
//import jdk.internal.instrumentation.Logger;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.InBoundService;
import org.shrek.hadata.service.hwms.service.OutBoundService;
import org.shrek.hadata.service.qimen.config.QimenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月30日 09:15
 */
@Service
public class Qimen200Schedule {
    private static String url = "http://qimen.api.taobao.com/router/qimen/service";
    //private static String appkey = "25024325";
    //private static String secret = "bcf66c17d34c0eecd03fe69bc055446a";
    private static String appkey = "25580624";
    private static String secret = "85353126ddfa24b3e01b0e7bf898c7aa";
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 60000)
    OutBoundService outBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 30000)
    InBoundService inBoundService;

    @Autowired
    QimenConfig qimenConfig;

    @Value("${scheduled.flag}")
    private boolean scheduled;

    private <T extends QimenResponse> T _execute(QimenRequest<T> request) throws ApiException {
        QimenClient client = new DefaultQimenClient(url, appkey, secret);
        return client.execute(request);
    }

    /**
     * 回传入库单确认信息   ok
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackEntryOrderConfirm() {
        if (scheduled) {
            qimenConfig.getApps().forEach((k, v) -> {
                List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByWhse(v.getClient(), v.getWarehouse(), 1119);
                for (int i = 0; i < tPoMasters.size(); i++) {
                    TPoMaster tPoMaster = tPoMasters.get(i);
                    EntryorderConfirmRequest request = new EntryorderConfirmRequest();
                    request.setCustomerId(v.getCustomer());
                    EntryorderConfirmRequest.EntryOrder entryOrder = new EntryorderConfirmRequest.EntryOrder();
                    entryOrder.setOutBizCode(UUID.randomUUID().toString());
                    entryOrder.setEntryOrderCode(tPoMaster.getPoNumber());
                    entryOrder.setWarehouseCode(v.getWarehouse());
                    entryOrder.setOwnerCode(v.getOwner());
                    entryOrder.setEntryOrderType("CGRK");
                    entryOrder.setStatus("FULFILLED");
                    request.setEntryOrder(entryOrder);
                    List<EntryorderConfirmRequest.OrderLine> orderLines = Lists.newArrayList();
                    List<TPoDetail> details = tPoMaster.getDetailList();
                    for (int j = 0; j < details.size(); j++) {
                        TPoDetail detail = details.get(j);
                        EntryorderConfirmRequest.OrderLine orderLine = new EntryorderConfirmRequest.OrderLine();
                        orderLine.setOrderLineNo(detail.getLineNumber());
                        orderLine.setOwnerCode(v.getOwner());
                        List<String> list = Splitter.on('-')
                                .trimResults()
                                .omitEmptyStrings()
                                .splitToList(detail.getItemNumber());
                        orderLine.setItemCode(list.get(1));
                        //应收商品数量
                        orderLine.setPlanQty(Math.round(detail.getQty()));
                        orderLine.setActualQty(Math.round(detail.getQty()));
                        orderLines.add(orderLine);
                    }
                    request.setOrderLines(orderLines);

                    try {
                        EntryorderConfirmResponse response = _execute(request);
                        if (response.getFlag().equals("success")) {
                            inBoundService.updateInBoundsBack(tPoMaster.getWhId(), tPoMaster.getPoNumber());
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 回传其他入库确认信息
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackOthOrderConfirm() {
        if (scheduled) {
            qimenConfig.getApps().forEach((k, v) -> {
                List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByWhse(v.getClient(), v.getWarehouse(), 2453);
                for (int i = 0; i < tPoMasters.size(); i++) {
                    TPoMaster tPoMaster = tPoMasters.get(i);
                    EntryorderConfirmRequest request = new EntryorderConfirmRequest();
                    request.setCustomerId(v.getCustomer());
                    EntryorderConfirmRequest.EntryOrder entryOrder = new EntryorderConfirmRequest.EntryOrder();
                    entryOrder.setOutBizCode(UUID.randomUUID().toString());
                    entryOrder.setEntryOrderCode(tPoMaster.getPoNumber());
                    entryOrder.setWarehouseCode(v.getWarehouse());
                    entryOrder.setOwnerCode(v.getOwner());
                    entryOrder.setEntryOrderType("QTRK");
                    entryOrder.setStatus("FULFILLED");
                    request.setEntryOrder(entryOrder);
                    List<EntryorderConfirmRequest.OrderLine> orderLines = Lists.newArrayList();
                    List<TPoDetail> details = tPoMaster.getDetailList();
                    for (int j = 0; j < details.size(); j++) {
                        TPoDetail detail = details.get(j);
                        EntryorderConfirmRequest.OrderLine orderLine = new EntryorderConfirmRequest.OrderLine();
                        orderLine.setOrderLineNo(detail.getLineNumber());
                        orderLine.setOwnerCode(v.getOwner());
                        List<String> list = Splitter.on('-')
                                .trimResults()
                                .omitEmptyStrings()
                                .splitToList(detail.getItemNumber());
                        orderLine.setItemCode(list.get(1));
                        //应收商品数量
                        orderLine.setPlanQty(Math.round(detail.getQty()));
                        orderLine.setActualQty(Math.round(detail.getQty()));
                        orderLines.add(orderLine);
                    }
                    request.setOrderLines(orderLines);

                    try {
                        EntryorderConfirmResponse response = _execute(request);
                        if (response.getFlag().equals("success")) {
                            inBoundService.updateInBoundsBack(tPoMaster.getWhId(), tPoMaster.getPoNumber());
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 回传退货入库单确认信息  ok
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackReturnOrderConfirm() {
        if (scheduled) {
            qimenConfig.getApps().forEach((k, v) -> {
                List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByWhse(v.getClient(), v.getWarehouse(), 1121);
                for (int i = 0; i < tPoMasters.size(); i++) {
                    TPoMaster tPoMaster = tPoMasters.get(i);
                    ReturnorderConfirmRequest request = new ReturnorderConfirmRequest();
                    request.setCustomerId(v.getCustomer());
                    ReturnorderConfirmRequest.ReturnOrder returnOrder = new ReturnorderConfirmRequest.ReturnOrder();
                    returnOrder.setOutBizCode(UUID.randomUUID().toString());
                    returnOrder.setReturnOrderCode(tPoMaster.getPoNumber());
                    returnOrder.setWarehouseCode(v.getWarehouse());
                    returnOrder.setOwnerCode(v.getOwner());
                    returnOrder.setOrderType("THRK");
                    returnOrder.setReturnOrderStatus("FULFILLED");
                    request.setReturnOrder(returnOrder);
                    List<ReturnorderConfirmRequest.OrderLine> orderLines = Lists.newArrayList();
                    List<TPoDetail> details = tPoMaster.getDetailList();
                    for (int j = 0; j < details.size(); j++) {
                        TPoDetail detail = details.get(j);
                        ReturnorderConfirmRequest.OrderLine orderLine = new ReturnorderConfirmRequest.OrderLine();
                        orderLine.setOrderLineNo(detail.getLineNumber());
                        orderLine.setOwnerCode(v.getOwner());
                        List<String> list = Splitter.on('-')
                                .trimResults()
                                .omitEmptyStrings()
                                .splitToList(detail.getItemNumber());
                        orderLine.setItemCode(list.get(1));
                        //应收商品数量
                        orderLine.setPlanQty(Math.round(detail.getQty()));
                        orderLine.setActualQty(detail.getQty().toString());
                        orderLines.add(orderLine);
                    }
                    request.setOrderLines(orderLines);
                    try {
                        ReturnorderConfirmResponse response = _execute(request);
                        if (response.getFlag().equals("success")) {
                            inBoundService.updateInBoundsBack(tPoMaster.getWhId(), tPoMaster.getPoNumber());
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 回传发货单确认信息
     */
    //20191203@Scheduled(cron = "0 0/1 * * * ?")
    public void feedbackDeliveryOrderConfirm() {
        if (scheduled) {
            qimenConfig.getApps().forEach((k, v) -> {
                List<TOrder> orders = outBoundService.queryConfirmOutBoundsByWhseAndClientAndType(v.getWarehouse(), v.getClient(), 1154);
                for (int i = 0; i < orders.size(); i++) {
                    TOrder tOrder = orders.get(i);
                    DeliveryorderConfirmRequest request = new DeliveryorderConfirmRequest();
                    request.setCustomerId(v.getCustomer());
                    DeliveryorderConfirmRequest.DeliveryOrder deliveryOrder = new DeliveryorderConfirmRequest.DeliveryOrder();
                    deliveryOrder.setDeliveryOrderCode(tOrder.getOrderNumber());
                    deliveryOrder.setWarehouseCode(tOrder.getWhId());
                    deliveryOrder.setOrderType("JYCK");
                    deliveryOrder.setOutBizCode(UUID.randomUUID().toString().trim().replaceAll("-", ""));
                    deliveryOrder.setTotalOrderLines(String.valueOf(tOrder.getOrderDetailList().size()));
                    deliveryOrder.setExpressCode(tOrder.getCarrierNumber());
                    deliveryOrder.setLogisticsCode(tOrder.getCarrierScac());
                    request.setDeliveryOrder(deliveryOrder);
                    List<DeliveryorderConfirmRequest.OrderLine> orderLines = Lists.newArrayList();
                    //单据列表
                    List<TOrderConfirm> confirms = tOrder.getOrderConfirmList();
                    for (int n = 0; n < confirms.size(); n++) {
                        TOrderConfirm confirm = confirms.get(n);
                        DeliveryorderConfirmRequest.OrderLine orderLine = new DeliveryorderConfirmRequest.OrderLine();
                        //实发商品数量
                        orderLine.setActualQty(confirm.getTranQty());
                        //库存类型(ZP=正品;CC=残次;JS=机损;XS=箱损;ZT=在途库存;默认为查所有类型的库存)
                        orderLine.setInventoryType("ZP");
                        //商品编码
                        orderLine.setItemCode(confirm.getDisplayItemNumber());
                        //商品名称
                        orderLine.setItemName(confirm.getDescription());
                        //单据行号
                        orderLine.setOrderLineNo(confirm.getLineNumber());
                        //生产批号
                        orderLine.setProduceCode(confirm.getLotNumber());
                        //生产日期(YYYY-MM-DD)
                        orderLine.setProductDate(confirm.getProdDate());
                        //批次编号
                        orderLine.setBatchCode(confirm.getLotNumber());
                        //过期日期(YYYY-MM-DD)
                        orderLine.setExpireDate(confirm.getEndTranDate());

                        orderLines.add(orderLine);
                    }
                    request.setOrderLines(orderLines);
                    List<DeliveryorderConfirmRequest.Package> confirmPackages = Lists.newArrayList();
                    List<TOrderPackage> packages = tOrder.getOrderPackageList();

                    for (int m = 0; m < packages.size(); m++) {
                        TOrderPackage tOrderPackage = packages.get(m);
                        DeliveryorderConfirmRequest.Package pack = new DeliveryorderConfirmRequest.Package();

                        pack.setExpressCode(tOrderPackage.getShipLabelBarcode());
                        List<DeliveryorderConfirmRequest.Item> packageItems = Lists.newArrayList();
                        List<TOrderPackageItem> items = tOrderPackage.getItems();
                        for (int j = 0; j < items.size(); j++) {
                            TOrderPackageItem packageItem = items.get(j);
                            DeliveryorderConfirmRequest.Item item = new DeliveryorderConfirmRequest.Item();
                            item.setItemCode(packageItem.getDisplayItemNumber());
                            item.setItemName(packageItem.getDescription());
                            item.setQuantity(packageItem.getQty());
                            packageItems.add(item);
                        }
                        pack.setLogisticsCode(tOrderPackage.getCarrierCode());
                        pack.setLogisticsName(tOrderPackage.getCarrierName());
                        pack.setItems(packageItems);
                        pack.setWeight(tOrderPackage.getWeight());
                        confirmPackages.add(pack);
                    }
                    request.setPackages(confirmPackages);
                    try {
                        String json = JacksonUtil.nonAlways().toJson(request);
                        System.out.println("奇门出库回传报文：" + json);
                        DeliveryorderConfirmResponse response = _execute(request);
                        System.out.println(request.getDeliveryOrder().getDeliveryOrderCode());

                        System.out.println("奇门出库回传返回报文：" + JacksonUtil.nonAlways().toJson(response));
                        System.out.println(response.getBody());
                        System.out.println(response.getMessage());
                        if (response.getFlag().equals("success")) {
                            outBoundService.updateOutBoundsBack(tOrder.getOrderId());
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 退供出库单确认信息
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackStockoutOrderConfirm() {
        if (scheduled) {
            qimenConfig.getApps().forEach((k, v) -> {
                List<TOrder> orders = outBoundService.queryConfirmOutBoundsByWhseAndClientAndType(v.getWarehouse(), v.getClient(), 2322);
                for (int i = 0; i < orders.size(); i++) {
                    TOrder tOrder = orders.get(i);
                    StockoutConfirmRequest request = new StockoutConfirmRequest();
                    request.setCustomerId(v.getCustomer());
                    StockoutConfirmRequest.DeliveryOrder deliveryOrder = new StockoutConfirmRequest.DeliveryOrder();
                    deliveryOrder.setDeliveryOrderCode(tOrder.getOrderNumber());
                    deliveryOrder.setWarehouseCode(tOrder.getWhId());
                    deliveryOrder.setOrderType("QTCK");
                    deliveryOrder.setOutBizCode(UUID.randomUUID().toString().trim().replaceAll("-", ""));
                    deliveryOrder.setTotalOrderLines(Long.valueOf(tOrder.getOrderDetailList().size()));
                    request.setDeliveryOrder(deliveryOrder);
                    List<StockoutConfirmRequest.OrderLine> orderLines = Lists.newArrayList();
                    //单据列表
                    List<TOrderConfirm> confirms = tOrder.getOrderConfirmList();
                    for (int n = 0; n < confirms.size(); n++) {
                        TOrderConfirm confirm = confirms.get(n);
                        StockoutConfirmRequest.OrderLine orderLine = new StockoutConfirmRequest.OrderLine();
                        //实发商品数量
                        orderLine.setActualQty(confirm.getTranQty());
                        //库存类型(ZP=正品;CC=残次;JS=机损;XS=箱损;ZT=在途库存;默认为查所有类型的库存)
                        orderLine.setInventoryType("ZP");
                        //商品编码
                        orderLine.setItemCode(confirm.getDisplayItemNumber());
                        //商品名称
                        orderLine.setItemName(confirm.getDescription());
                        //单据行号
                        orderLine.setOrderLineNo(confirm.getLineNumber());
                        //生产批号
                        orderLine.setProduceCode(confirm.getLotNumber());
                        //生产日期(YYYY-MM-DD)
                        orderLine.setProductDate(confirm.getProdDate());
                        //批次编号
                        orderLine.setBatchCode(confirm.getLotNumber());
                        //过期日期(YYYY-MM-DD)
                        orderLine.setExpireDate(confirm.getEndTranDate());

                        orderLines.add(orderLine);
                    }
                    request.setOrderLines(orderLines);
                    List<StockoutConfirmRequest.Package> confirmPackages = Lists.newArrayList();
                    StockoutConfirmRequest.Package pack = new StockoutConfirmRequest.Package();
                    pack.setExpressCode(tOrder.getCarrierNumber());
                    pack.setLogisticsCode(tOrder.getCarrierScac());
                    List<StockoutConfirmRequest.Item> packageItems = Lists.newArrayList();
                    List<TOrderDetail> detailList = tOrder.getOrderDetailList();
                    for (int m = 0; m < detailList.size(); m++) {
                        TOrderDetail tOrderDetail = detailList.get(m);
                        StockoutConfirmRequest.Item item = new StockoutConfirmRequest.Item();
                        List<String> list = Splitter.on("-").omitEmptyStrings().splitToList(tOrderDetail.getItemNumber());
                        item.setItemCode(list.get(1));
                        item.setQuantity(tOrderDetail.getQtyShipped().toString());
                        packageItems.add(item);
                    }
                    pack.setItems(packageItems);
                    confirmPackages.add(pack);
                    request.setPackages(confirmPackages);
                    try {
                        StockoutConfirmResponse response = _execute(request);
                        if (response.getFlag().equals("success")) {
                            outBoundService.updateOutBoundsBack(tOrder.getOrderId());
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
