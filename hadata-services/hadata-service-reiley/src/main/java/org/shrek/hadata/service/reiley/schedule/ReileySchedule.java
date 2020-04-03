package org.shrek.hadata.service.reiley.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.qimen.api.DefaultQimenClient;
import com.qimen.api.QimenClient;
import com.qimen.api.QimenRequest;
import com.qimen.api.QimenResponse;
import com.qimen.api.request.DeliveryorderConfirmRequest;
import com.qimen.api.request.ReturnorderConfirmRequest;
import com.qimen.api.response.DeliveryorderConfirmResponse;
import com.qimen.api.response.ReturnorderConfirmResponse;
import com.qimencloud.api.DefaultQimenCloudClient;
import com.qimencloud.api.QimenCloudClient;
import com.qimencloud.api.QimenCloudRequest;
import com.qimencloud.api.QimenCloudResponse;
import com.taobao.api.ApiException;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.InBoundService;
import org.shrek.hadata.service.hwms.service.OutBoundService;
import org.shrek.hadata.service.hwms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * 定时任务控制
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月22日 14:32
 */
@Component
public class ReileySchedule {
    private static String url = "http://qimen.api.taobao.com/router/qimen/service";
//    private static String appkey = "25024325";
//    private static String secret = "bcf66c17d34c0eecd03fe69bc055446a";
    private static String appkey = "25580624";
    private static String secret = "85353126ddfa24b3e01b0e7bf898c7aa";
    private static String targetAppkey = "23036663";

    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    WarehouseService warehouseService;

    @Value("${scheduled.flag}")
    private boolean scheduled;

    private QimenCloudResponse _execute(String apiName, String body) throws ApiException {
        QimenCloudClient client = new DefaultQimenCloudClient(url, appkey, secret, "xml");
        QimenCloudRequest request = new QimenCloudRequest();
        request.setApiMethodName(apiName);
        request.setTargetAppKey(targetAppkey);
        request.addQueryParam("customerId", "reiley");
        request.setBody(body);
        return client.execute(request);
    }

    private <T extends QimenResponse> T _execute(QimenRequest<T> request) throws ApiException {
        QimenClient client = new DefaultQimenClient(url, appkey, secret);
        return client.execute(request);
    }

    /**
     * 回传发货单确认信息
     * <p>
     * 执行周期 2分钟
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackDeliveryOrderConfirm() {
//        if (scheduled) {
            //获取实际数据
            List<TOrder> orders = outBoundService.queryConfirmOutBoundsByWhse("601");
            for (int i = 0; i < orders.size(); i++) {
                TOrder tOrder = orders.get(i);
                DeliveryorderConfirmRequest request = new DeliveryorderConfirmRequest();
                request.setCustomerId("reiley");

                //发货单信息
                DeliveryorderConfirmRequest.DeliveryOrder deliveryOrder = new DeliveryorderConfirmRequest.DeliveryOrder();
                deliveryOrder.setDeliveryOrderCode(tOrder.getOrderNumber());
                deliveryOrder.setDeliveryOrderId(String.valueOf(tOrder.getOrderId()));
                //TODO
                deliveryOrder.setOrderConfirmTime(DateUtil.getTimeStampStr(tOrder.getOrderDate()));
                deliveryOrder.setOrderType("JYCK");
                deliveryOrder.setOutBizCode(UUID.randomUUID().toString().trim().replaceAll("-", ""));
                deliveryOrder.setStatus("DELIVERED");
                deliveryOrder.setTotalOrderLines(String.valueOf(tOrder.getOrderDetailList().size()));
                String whCode = warehouseService.getWarehouseCodeById(tOrder.getWhId());
                deliveryOrder.setWarehouseCode(whCode);
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
                    DeliveryorderConfirmResponse response = _execute(request);
                    if (response.getFlag().equals("success")) {
                        outBoundService.updateOutBoundsBack(tOrder.getOrderId());
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
//        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void feedbackReturnOrderConfirm() {
        if (scheduled) {
            List<TPoMaster> tPoMasters = inBoundService.queryConfirmInBoundsByWhse("601");
            for (int i = 0; i < tPoMasters.size(); i++) {
                TPoMaster tPoMaster = tPoMasters.get(i);
                ReturnorderConfirmRequest request = new ReturnorderConfirmRequest();
                request.setCustomerId("reiley");
                ReturnorderConfirmRequest.ReturnOrder returnOrder = new ReturnorderConfirmRequest.ReturnOrder();
                returnOrder.setReturnOrderCode(tPoMaster.getPoNumber());
                returnOrder.setWarehouseCode("REILEY");
                returnOrder.setOrderType("THRK");
                if (tPoMaster.getConfirmDateTime() != null) {
                    returnOrder.setOrderConfirmTime(String.valueOf(tPoMaster.getConfirmDateTime().getTime()));
                } else {
                    returnOrder.setOrderConfirmTime(DateUtil.currentLongTime());
                }
                returnOrder.setLogisticsCode(tPoMaster.getCarrierScac());
                returnOrder.setExpressCode(tPoMaster.getCarrierMode());
                ReturnorderConfirmRequest.SenderInfo senderInfo = new ReturnorderConfirmRequest.SenderInfo();
                senderInfo.setName(tPoMaster.getShipFromName());
                senderInfo.setMobile(tPoMaster.getShipFromPhone());
                senderInfo.setTel("");
                senderInfo.setProvince(tPoMaster.getShipFromState());
                senderInfo.setCity(tPoMaster.getShipFromCity());
                senderInfo.setArea(tPoMaster.getShipFromArea());
                senderInfo.setDetailAddress(tPoMaster.getShipFromAddr1());
                returnOrder.setSenderInfo(senderInfo);
                request.setReturnOrder(returnOrder);
                List<ReturnorderConfirmRequest.OrderLine> orderLines = Lists.newArrayList();
                List<TPoDetail> details = tPoMaster.getDetailList();
                for (int j = 0; j < details.size(); j++) {
                    TPoDetail detail = details.get(j);
                    ReturnorderConfirmRequest.OrderLine orderLine = new ReturnorderConfirmRequest.OrderLine();
                    orderLine.setOrderLineNo(detail.getLineNumber());
                    List<String> list = Splitter.on('-')
                            .trimResults()
                            .omitEmptyStrings()
                            .splitToList(detail.getItemNumber());
                    orderLine.setItemCode(list.get(1));
                    //应收商品数量
                    orderLine.setPlanQty(Math.round(detail.getQty()));
                    orderLine.setActualQty(String.valueOf(detail.getQty()));
                    orderLines.add(orderLine);
                }
                request.setOrderLines(orderLines);

                try {
                    String json = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(request);

                    ReturnorderConfirmResponse response = _execute(request);
                    if (response.getFlag().equals("success")) {
                        inBoundService.updateInBoundsBack(tPoMaster.getWhId(), tPoMaster.getPoNumber());
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
