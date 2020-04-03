package org.shrek.hadata.service.qimen.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qimen.api.request.*;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.commons.util.StringUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.*;
import org.shrek.hadata.service.qimen.common.QimenResponseUtil;
import org.shrek.hadata.service.qimen.service.model.*;
import org.shrek.hadata.service.qimen.service.model.ReturnorderCreateRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月28日 17:09
 */
@Service
public class Qimen200Service {
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    WarehouseService warehouseService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 60000)
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
    StoreService storeService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;


    /**
     * 入库单创建
     *
     * @param entryorder
     * @return
     */
    public EntryorderCreateResponse createEntryorder(EntryorderCreateRequest entryorder) {

        TPoMaster tPoMaster = new TPoMaster();
        TWhse tWhse = warehouseService.getWarehouseByCode(entryorder.getEntryOrder().getWarehouseCode());
        try {
            tPoMaster.setWhId(tWhse.getWhId());
        } catch (Exception e) {
            return QimenResponseUtil.fail("获取仓库信息失败!", EntryorderCreateResponse.class);
        }
        TClient client =clientService.getClientByWhAndCode(tWhse.getWhId(), entryorder.getEntryOrder().getOwnerCode());
        if (client == null) {
            client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), entryorder.getEntryOrder().getOwnerCode());
            if (client == null) {
                client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), (String) entryorder.getExtendProps().get("shop_name"));
                if (client == null) {
                    return QimenResponseUtil.fail("获取客户信息失败!", EntryorderCreateResponse.class);
                }
            }
        }
        tPoMaster.setClientCode(client.getClientCode());
        //入库单 发货人信息
        tPoMaster.setPoNumber(entryorder.getEntryOrder().getEntryOrderCode());
        //CGRK-采购,DBRK-调拨,QTRK-其他
        if (entryorder.getEntryOrder().getOrderType().toUpperCase().equals("DBRK")) {
            tPoMaster.setTypeId(1120);
        } else if (entryorder.getEntryOrder().getOrderType().toUpperCase().equals("CGRK")) {
            tPoMaster.setTypeId(1119);
        } else {
            tPoMaster.setTypeId(2453);
        }
        tPoMaster.setVendorCode("7510");
        tPoMaster.setCreateDate(DateUtil.getCurTimestamp());
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");
        //姓名
        tPoMaster.setShipFromName(entryorder.getEntryOrder().getSenderInfo().getName());
        //市
        tPoMaster.setShipFromCity(entryorder.getEntryOrder().getSenderInfo().getCity());
        //省
        tPoMaster.setShipFromState(entryorder.getEntryOrder().getSenderInfo().getProvince());
        //区
        tPoMaster.setShipFromAddr2(entryorder.getEntryOrder().getSenderInfo().getArea());
        //移动电话
        tPoMaster.setShipFromPhone(entryorder.getEntryOrder().getSenderInfo().getMobile());
        //详细地址
        tPoMaster.setShipFromAddr1(entryorder.getEntryOrder().getSenderInfo().getDetailAddress());
        //物流公司
        tPoMaster.setCarrierScac(entryorder.getEntryOrder().getLogisticsCode());
        //入库明细
        List<String> noFindIteams = Lists.newArrayList();
        List<TPoDetail> details = Lists.newArrayList();
        final int[] lineNumber = {1000};
        entryorder.getOrderLines().forEach(orderLinesBean -> {
            lineNumber[0]++;
            TPoDetail detail = new TPoDetail();
            detail.setPoNumber(entryorder.getEntryOrder().getEntryOrderCode());
            detail.setLineNumber(StringUtil.isEmpty(orderLinesBean.getOrderLineNo()) ? String.valueOf(lineNumber[0]) : orderLinesBean.getOrderLineNo());
            detail.setWhId(tWhse.getWhId());

            TItemMaster tItemMaster = materielService.getItemByCode(orderLinesBean.getItemCode(), tPoMaster.getClientCode());
            if (tItemMaster != null) {
                detail.setItemNumber(tItemMaster.getItemNumber());
                detail.setScheduleNumber(Integer.parseInt(detail.getLineNumber()));
                detail.setOrderUom("EA");
                detail.setQty(orderLinesBean.getPlanQty().doubleValue());
            } else {
                noFindIteams.add(orderLinesBean.getItemCode());
            }
            details.add(detail);
        });

        if (noFindIteams.size() != 0) {

            String noFindIteam = Joiner.on(',').join(noFindIteams);
            return QimenResponseUtil.fail("物料代码：" + noFindIteam + "信息获取失败!", EntryorderCreateResponse.class);
        }
        tPoMaster.setDetailList(details);
        BaseResponse<TPoMaster> basicResult = inBoundService.createInBoundOrder(tPoMaster);
        if (basicResult.isFlag()) {
            return QimenResponseUtil.success(EntryorderCreateResponse.class);
        } else {
            return QimenResponseUtil.fail(basicResult.getSubMessage(), EntryorderCreateResponse.class);
        }

    }


    /**
     * 退货入库创建
     *
     * @param returnOrder
     * @return
     */
    public ReturnorderCreateResponse createReturnOrder(ReturnorderCreateRequest returnOrder) {
        TPoMaster tPoMaster = new TPoMaster();
        TWhse tWhse = warehouseService.getWarehouseByCode(returnOrder.getReturnOrder().getWarehouseCode());
        try {
            tPoMaster.setWhId(tWhse.getWhId());
        } catch (Exception e) {
            return QimenResponseUtil.fail("获取仓库信息失败!", ReturnorderCreateResponse.class);
        }

        TClient client = clientService.getClientByWhAndCode(tWhse.getWhId(), returnOrder.getReturnOrder().getOwnerCode());
        if (client == null) {
            client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), returnOrder.getReturnOrder().getOwnerCode());
            if (client == null) {
                client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), (String) returnOrder.getReturnOrder().getExtendProps().get("shop_name"));
                if (client == null) {
                    return QimenResponseUtil.fail("获取客户信息失败!", ReturnorderCreateResponse.class);                }
            }
        }

        tPoMaster.setClientCode(client.getClientCode());

        tPoMaster.setPoNumber(returnOrder.getReturnOrder().getReturnOrderCode());
        tPoMaster.setTypeId(1121);
        tPoMaster.setVendorCode("7510");
        tPoMaster.setCreateDate(DateUtil.getCurTimestamp());
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");

        //发货人信息
        //姓名
        tPoMaster.setShipFromName(returnOrder.getReturnOrder().getSenderInfo().getName());
        //市
        tPoMaster.setShipFromCity(returnOrder.getReturnOrder().getSenderInfo().getCity());
        //省
        tPoMaster.setShipFromState(returnOrder.getReturnOrder().getSenderInfo().getProvince());
        //区
        tPoMaster.setShipFromAddr2(returnOrder.getReturnOrder().getSenderInfo().getArea());
        //移动电话
        tPoMaster.setShipFromPhone(returnOrder.getReturnOrder().getSenderInfo().getMobile());
        //详细地址
        tPoMaster.setShipFromAddr1(returnOrder.getReturnOrder().getSenderInfo().getDetailAddress());
        tPoMaster.setCarrierScac(returnOrder.getReturnOrder().getLogisticsCode());
        tPoMaster.setServiceLevel(returnOrder.getReturnOrder().getExpressCode());
        //退货单所属原出库单号
        tPoMaster.setInterfaceId(returnOrder.getReturnOrder().getPreDeliveryOrderCode());
        List<String> noFindIteams = Lists.newArrayList();
        List<TPoDetail> details = Lists.newArrayList();
        final int[] lineNumber = {1000};
        returnOrder.getOrderLines().forEach(orderLinesBean -> {
            lineNumber[0]++;
            TItemMaster tItemMaster = materielService.getItemByCode(orderLinesBean.getItemCode(),tPoMaster.getClientCode());
            if (tItemMaster != null) {
                TPoDetail detail = new TPoDetail();
                detail.setWhId(tWhse.getWhId());
                detail.setOrderUom("EA");
                detail.setPoNumber(returnOrder.getReturnOrder().getReturnOrderCode());
                detail.setLineNumber(StringUtil.isEmpty(orderLinesBean.getOrderLineNo()) ? String.valueOf(lineNumber[0]) : orderLinesBean.getOrderLineNo());
                detail.setItemNumber(tItemMaster.getItemNumber());
                detail.setScheduleNumber(Integer.parseInt(detail.getLineNumber()));
                detail.setQty(orderLinesBean.getPlanQty().doubleValue());
                details.add(detail);
            } else {
                noFindIteams.add(orderLinesBean.getItemCode());
            }
        });
        if (noFindIteams.size() != 0) {

            String noFindIteam = Joiner.on(',').join(noFindIteams);
            return QimenResponseUtil.fail("物料代码：" + noFindIteam + "信息获取失败!", ReturnorderCreateResponse.class);
        }
        tPoMaster.setDetailList(details);
        BaseResponse<String> basicResult = inBoundService.createInBoundOrder(tPoMaster);
        if (returnOrder.getReturnOrder().getOrderType().endsWith("THRK")) {
            return QimenResponseUtil.success(ReturnorderCreateResponse.class);
        } else {
            return QimenResponseUtil.fail(basicResult.getMessage(), ReturnorderCreateResponse.class);
        }
    }


    /**
     * 商品同步
     *
     * @param synchronize
     * @return
     */
    public SingleitemSynchronizeResponse singleitemSynchronize(SingleitemSynchronizeRequest synchronize) {
        TItemMaster item = new TItemMaster();
        TWhse tWhse = warehouseService.getWarehouseByCode(synchronize.getWarehouseCode());
        try {
            item.setWhId(tWhse.getWhId());
        } catch (Exception e) {
            return QimenResponseUtil.fail("获取仓库信息失败!", SingleitemSynchronizeResponse.class);
        }
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(tWhse.getWhId(), synchronize.getOwnerCode());
            if (client == null) {
                client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), synchronize.getOwnerCode());
            }
            item.setClientCode(client.getClientCode());
        } catch (Exception e) {
            return QimenResponseUtil.fail("获取客户信息失败!", SingleitemSynchronizeResponse.class);
        }

        //物料信息表
        List<TItemMaster> itemMasters = Lists.newArrayList();
        TItemMaster itemMaster = new TItemMaster();
        itemMaster.setClients(Lists.newArrayList(client));
        itemMaster.setItemNumber(synchronize.getItem().getItemCode());
        itemMaster.setDisplayItemNumber(synchronize.getItem().getItemCode());
        itemMaster.setAltItemNumber(synchronize.getItem().getBarCode());
        itemMaster.setDescription(synchronize.getItem().getItemName());
        itemMaster.setInventoryType(synchronize.getItem().getItemType());
        itemMaster.setCommodityCode(synchronize.getItem().getBarCode());

        itemMaster.setPrice(Double.valueOf(Optional.ofNullable(synchronize.getItem().getRetailPrice()).orElse("0")));
        itemMaster.setUnitWeight(Double.valueOf(Optional.ofNullable(synchronize.getItem().getNetWeight()).orElse("0")));
        itemMaster.setTareWeight(Double.valueOf(Optional.ofNullable(synchronize.getItem().getGrossWeight()).orElse("0")));
        itemMasters.add(itemMaster);
        TItemUom itemUom = new TItemUom();
        itemUom.setItemMasterId(itemMaster.getItemMasterId());
        itemUom.setItemNumber(itemMaster.getItemNumber());
        itemUom.setConversionFactor(1D);
        itemUom.setUom("EA");
        itemUom.setUomPrompt("件");
        itemMaster.getUoms().add(itemUom);
        boolean rest = materielService.createMaterials(itemMasters);
        if (rest) {
            SingleitemSynchronizeResponse response = QimenResponseUtil.success(SingleitemSynchronizeResponse.class);
            response.setItemId(Joiner.on("-").join(client.getClientCode(), synchronize.getItem().getItemCode()));
            return response;
        } else {
            return QimenResponseUtil.fail(Maps.newHashMap(), SingleitemSynchronizeResponse.class);
        }

    }


    /**
     * 出库单创建
     *
     * @param createStockout
     * @return
     */
    public StockoutCreateResponse createStockout(StockoutCreateRequest createStockout) {
        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(createStockout.getDeliveryOrder().getDeliveryOrderCode());
        TWhse tWhse = warehouseService.getWarehouseByCode(createStockout.getDeliveryOrder().getWarehouseCode());
        try {
            tOrder.setWhId(tWhse.getWhId());
        } catch (Exception e) {
            return QimenResponseUtil.fail("获取仓库信息失败!", StockoutCreateResponse.class);
        }
        TClient client;
        try {
            client = clientService.getClientByWhAndCode(tWhse.getWhId(), createStockout.getDeliveryOrder().getOwnerCode());
            if (client == null) {
                client = clientService.getClientByWhAndCode(tWhse.getWhId(), createStockout.getOrderLines().get(0).getOwnerCode());
                if (client == null) {
                    client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), createStockout.getOrderLines().get(0).getOwnerCode());
                }
            }
            tOrder.setClientCode(client.getClientCode());
        } catch (Exception e) {
            return QimenResponseUtil.fail("获取客户信息失败!", StockoutCreateResponse.class);
        }

        tOrder.setTypeId(2456);
        tOrder.setStatus("N");
        tOrder.setShipToName(createStockout.getDeliveryOrder().getReceiverInfo().getName());
        if(createStockout.getDeliveryOrder().getReceiverInfo().getMobile() == null || "".equals(createStockout.getDeliveryOrder().getReceiverInfo().getMobile()))
        {
            tOrder.setShipToPhone(createStockout.getDeliveryOrder().getReceiverInfo().getTel());
        }
        else {
            tOrder.setShipToPhone(createStockout.getDeliveryOrder().getReceiverInfo().getMobile());
        }
        tOrder.setShipToState(createStockout.getDeliveryOrder().getReceiverInfo().getProvince());
        tOrder.setShipToCity(createStockout.getDeliveryOrder().getReceiverInfo().getCity());
        tOrder.setShipToAddr2(createStockout.getDeliveryOrder().getReceiverInfo().getArea());
        tOrder.setShipToZip(createStockout.getDeliveryOrder().getReceiverInfo().getZipCode());
        tOrder.setShipToAddr1(createStockout.getDeliveryOrder().getReceiverInfo().getDetailAddress());


        List<TOrderDetail> details = Lists.newArrayList();
        List<StockoutCreateRequest.OrderLine> orderLines = createStockout.getOrderLines();
        List<String> noFindIteams = Lists.newArrayList();
        final int[] lineNumber = {1000};
        orderLines.forEach(line -> {
            lineNumber[0]++;
            TOrderDetail detail = new TOrderDetail();
            detail.setWhId(tWhse.getWhId());
            detail.setOrderNumber(createStockout.getDeliveryOrder().getDeliveryOrderCode());
            detail.setLineNumber(StringUtil.isEmpty(line.getOrderLineNo()) ? String.valueOf(lineNumber[0]) : line.getOrderLineNo());
            TItemMaster tItemMaster = materielService.getItemByCode(line.getItemCode(), tOrder.getClientCode());
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
            return QimenResponseUtil.fail("物料代码：" + noFindIteam + "信息获取失败!", StockoutCreateResponse.class);

        }
        tOrder.setOrderDetailList(details);
        BaseResponse<String> basicResult = outBoundService.createOutBoundOrder(tOrder);
        if (basicResult.isFlag()) {
            return QimenResponseUtil.success(StockoutCreateResponse.class);
        } else {
            return QimenResponseUtil.fail(basicResult.getMessage(), StockoutCreateResponse.class);
        }
    }


    /**
     * 发货单创建
     *
     * @param createDelivery
     * @return
     */
    public DeliveryorderCreateResponse createDeliveryOrder(DeliveryorderCreateRequest createDelivery) {

        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(createDelivery.getDeliveryOrder().getDeliveryOrderCode());
        TWhse tWhse = warehouseService.getWarehouseByCode(createDelivery.getDeliveryOrder().getWarehouseCode());
        if (tWhse != null) {
            tOrder.setWhId(tWhse.getWhId());
        } else {
            return QimenResponseUtil.fail("获取仓库信息失败", DeliveryorderCreateResponse.class);
        }

        TClient client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), createDelivery.getDeliveryOrder().getShopNick());
        if (client == null) {
            client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), createDelivery.getDeliveryOrder().getOwnerCode());
            if (client == null) {
                client = clientService.getClientByWhAndCode(tWhse.getWhId(), createDelivery.getDeliveryOrder().getOwnerCode());
                if (client == null) {
                    return QimenResponseUtil.fail("获取客户信息失败", DeliveryorderCreateResponse.class);
                }
            }
        }
        tOrder.setClientCode(client.getClientCode());

//        JYCK=一般交易出库单;HHCK=换货出库单;BFCK=补发出库单;QTCK=其他出 库 单
        if (createDelivery.getDeliveryOrder().getOrderType().equals("JYCK")) {
            tOrder.setTypeId(1154);
        } else {
            tOrder.setTypeId(2456);
        }
        tOrder.setOrderDate(DateUtil.parseDate(createDelivery.getDeliveryOrder().getPayTime(), DateUtil.DATE_FORMAT_DATETIME));
        tOrder.setStatus("N");

        TCarrier carrier = carrierService.getCarrierByCode(createDelivery.getDeliveryOrder().getLogisticsCode());
        if (carrier != null) {
            tOrder.setCarrierId(carrier.getCarrierId());
            tOrder.setCarrierScac(createDelivery.getDeliveryOrder().getLogisticsCode());
        } else {
            return QimenResponseUtil.fail("获取快递信息失败", DeliveryorderCreateResponse.class);
        }
        tOrder.setShipToName(createDelivery.getDeliveryOrder().getReceiverInfo().getName());
        tOrder.setShipToPhone(createDelivery.getDeliveryOrder().getReceiverInfo().getMobile());
        tOrder.setShipToState(createDelivery.getDeliveryOrder().getReceiverInfo().getProvince());
        tOrder.setShipToCity(createDelivery.getDeliveryOrder().getReceiverInfo().getCity());
        tOrder.setShipToAddr2(createDelivery.getDeliveryOrder().getReceiverInfo().getArea());
        tOrder.setShipToZip(createDelivery.getDeliveryOrder().getReceiverInfo().getZipCode());
        tOrder.setShipToAddr1(createDelivery.getDeliveryOrder().getReceiverInfo().getDetailAddress());
        tOrder.setDeliveryName(createDelivery.getDeliveryOrder().getSenderInfo().getName());
        tOrder.setDeliveryPhone(createDelivery.getDeliveryOrder().getSenderInfo().getMobile());
        tOrder.setDeliveryState(createDelivery.getDeliveryOrder().getSenderInfo().getProvince());
        tOrder.setDeliveryCity(createDelivery.getDeliveryOrder().getSenderInfo().getCity());
        tOrder.setDeliveryAddr2(createDelivery.getDeliveryOrder().getSenderInfo().getArea());
        tOrder.setDeliveryZip(createDelivery.getDeliveryOrder().getSenderInfo().getZipCode());
        tOrder.setDeliveryAddr1(createDelivery.getDeliveryOrder().getSenderInfo().getDetailAddress());
        tOrder.setInterfacePlatform(createDelivery.getDeliveryOrder().getSourcePlatformCode());
        List<TOrderDetail> details = Lists.newArrayList();
        List<DeliveryorderCreateRequest.OrderLine> orderLines = createDelivery.getOrderLines();
        List<String> noFindIteams = Lists.newArrayList();
        final int[] lineNumber = {1};
        orderLines.forEach(line -> {
            lineNumber[0]++;
            TOrderDetail detail = new TOrderDetail();
            detail.setWhId(tWhse.getWhId());
            detail.setOrderNumber(createDelivery.getDeliveryOrder().getDeliveryOrderCode());
            detail.setLineNumber(StringUtil.isEmpty(line.getOrderLineNo()) ? String.valueOf(lineNumber[0]) : line.getOrderLineNo());
//            detail.setUnitPrice();
//            detail.setUnitPriceReal();
            TItemMaster tItemMaster = materielService.getItemByCode(line.getItemCode(), tOrder.getClientCode());
            if (tItemMaster != null) {
                detail.setItemNumber(tItemMaster.getItemNumber());
                detail.setQty(Double.parseDouble(line.getPlanQty()));
                detail.setAfoPlanQty(Double.parseDouble(line.getPlanQty()));
                detail.setOrderUom("EA");
                detail.setUnitPrice(new BigDecimal(line.getRetailPrice()));
                detail.setUnitPriceReal(new BigDecimal(line.getActualPrice()));
                details.add(detail);
            } else {
                noFindIteams.add(line.getItemCode());
            }
        });

        if (noFindIteams.size() != 0) {
            String noFindIteam = Joiner.on(',').join(noFindIteams);
            return QimenResponseUtil.fail("物料代码：" + noFindIteam + "信息获取失败!", DeliveryorderCreateResponse.class);

        }

        tOrder.setOrderDetailList(details);
        BaseResponse<TOrder> basicResult = outBoundService.createOutBoundOrder(tOrder);
        if (basicResult.isFlag()) {
            return QimenResponseUtil.success(DeliveryorderCreateResponse.class);
        } else {
            return QimenResponseUtil.fail(basicResult.getSubMessage(), DeliveryorderCreateResponse.class);
        }
    }


    /**
     * 单据取消
     *
     * @param orderCancel
     * @return
     */
    public OrderCancelResponse cancelOrder(OrderCancelRequest orderCancel) {

        TWhse tWhse = warehouseService.getWarehouseByCode(orderCancel.getWarehouseCode());
        if (tWhse == null) {
            return QimenResponseUtil.fail("获取仓库信息失败", OrderCancelResponse.class);
        }

        if (orderCancel.getWarehouseCode().indexOf("REILEY") >= 0) {
            if (orderCancel.getOrderType().endsWith("CK")) {
                //出库订单取消
                BaseResponse<String> basicResult = outBoundService.cancelOutBoundOrder(orderCancel.getOrderCode(), tWhse.getWhId());
                if (basicResult.isFlag()) {
                    return QimenResponseUtil.success(OrderCancelResponse.class);
                } else {
                    return QimenResponseUtil.fail(basicResult.getSubMessage(), OrderCancelResponse.class);
                }
            } else {
                //入库订单取消
                BaseResponse<String> basicResult = inBoundService.cancelInBoundOrder(orderCancel.getOrderCode(), tWhse.getWhId());
                if (basicResult.isFlag()) {
                    return QimenResponseUtil.success(OrderCancelResponse.class);
                } else {
                    return QimenResponseUtil.fail(basicResult.getSubMessage(), OrderCancelResponse.class);
                }
            }
        } else {
            TClient client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), orderCancel.getOwnerCode());
            if (client == null) {
                client = clientService.getClientByWhAndLongCode(tWhse.getWhId(), orderCancel.getOwnerCode());
                if (client == null) {
                    client = clientService.getClientByWhAndCode(tWhse.getWhId(), orderCancel.getOwnerCode());
                    if (client == null) {
                        return QimenResponseUtil.fail("获取客户信息失败", OrderCancelResponse.class);
                    }
                }
            }
            if (orderCancel.getOrderType().endsWith("CK")) {
                //出库订单取消
                BaseResponse<String> basicResult = outBoundService.cancelOutBoundOrder(orderCancel.getOrderCode(), client.getClientCode(), tWhse.getWhId());
                if (basicResult.isFlag()) {
                    return QimenResponseUtil.success(OrderCancelResponse.class);
                } else {
                    return QimenResponseUtil.fail(basicResult.getSubMessage(), OrderCancelResponse.class);
                }
            } else {
                //入库订单取消
                BaseResponse<String> basicResult = inBoundService.cancelInBoundOrder(orderCancel.getOrderCode(), orderCancel.getOwnerCode(), tWhse.getWhId());
                if (basicResult.isFlag()) {
                    return QimenResponseUtil.success(OrderCancelResponse.class);
                } else {
                    return QimenResponseUtil.fail(basicResult.getSubMessage(), OrderCancelResponse.class);
                }
            }
        }
    }


    public com.qimen.api.response.InventoryQueryResponse inventoryQuery(InventoryQueryRequest request) {
        com.qimen.api.response.InventoryQueryResponse response = new com.qimen.api.response.InventoryQueryResponse();
        response.setFlag("success");
        response.setCode("0");
        response.setMessage("");
        try {
            List<com.qimen.api.response.InventoryQueryResponse.Item> items = Lists.newArrayList();
            request.getCriteriaList().forEach(criteria -> {
                TClient client = clientService.getClientByWhAndLongCode(criteria.getWarehouseCode(), criteria.getOwnerCode());
                if (client == null) {
                    client = clientService.getClientByWhAndCode(criteria.getWarehouseCode(), criteria.getOwnerCode());
                    if (client == null) {
                        response.setFlag("failure");
                        response.setCode("0");
                        response.setMessage("客户信息不存在!");
                        return;
                    }
                }
                BaseResponse<HashMap<String, QimenStoredInfo>> resp = storeService.queryQimenStoreInfo(client.getClientCode(), criteria.getWarehouseCode(), Joiner.on('-').join(client.getClientCode(), criteria.getItemCode()));
                resp.getData().forEach((k, v) -> {
                    com.qimen.api.response.InventoryQueryResponse.Item item = new com.qimen.api.response.InventoryQueryResponse.Item();
                    item.setWarehouseCode(v.getWarehouseCode());
                    item.setItemId(v.getItemCode());
                    item.setItemCode(criteria.getItemCode());
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
        } catch (RuntimeException e) {
            response.setFlag("failure");
            response.setCode("500");
            response.setMessage("信息查询失败,请稍后重试!");
        }
        return response;
    }
}
