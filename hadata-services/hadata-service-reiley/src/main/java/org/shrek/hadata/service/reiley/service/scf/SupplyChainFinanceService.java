package org.shrek.hadata.service.reiley.service.scf;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.*;
import org.shrek.hadata.service.reiley.service.scf.model.InventoryQuantityReponse;
import org.shrek.hadata.service.reiley.service.scf.model.InventoryQuantityBatchReponse;
import org.shrek.hadata.service.reiley.service.scf.model.MassiveStockOutRequest;
import org.shrek.hadata.service.reiley.service.scf.model.MassiveStockInRequest;
import org.shrek.hadata.service.reiley.service.scf.model.SkuDTO;
import org.shrek.hadata.service.reiley.service.scf.model.StockOutOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 供应链金融服务
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 09:29
 */
@Service
public class SupplyChainFinanceService {

    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    TblMassiveStockOutQuantityService tblMassiveStockOutQuantityService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 60000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;

    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    StoreService storeService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;


    /**
     * 查询货主SKU信息
     *
     * @return
     */
    public String getSkuInfo(String client, String whse,String whseCode) {
        SkuDTO sku = new SkuDTO();
        sku.setOwnerUserId(Joiner.on("-").join(whse,client));
        sku.setStoreCode(whseCode);
        List<TItemMaster> masters = materielService.getItemsByClientAndWhse(client, whse);
        List<SkuDTO.Item> items = Lists.newArrayList();
        masters.forEach(master -> {
            SkuDTO.Item item = new SkuDTO.Item();
            item.setItemCode(master.getDisplayItemNumber());
            item.setItemName(master.getDescription());
            item.setBeImport("FC".equals(master.getInventoryType())?"1":"0");
            item.setBePeriodGuarantee("Y".equals(master.getExpirationDateControl())?"1":"0");
            if (master.getCommodityCode() != null)
                item.setBarCode(master.getCommodityCode());
            item.setHeight(master.getHeight()==null?"0":master.getHeight().toString());
            item.setWidth(master.getWidth()==null?"0":master.getWidth().toString());
            item.setLength(master.getLength()==null?"0":master.getLength().toString());
            item.setWeight(master.getUnitWeight()==null?"0":master.getUnitWeight().toString());
            item.setVolume(master.getUnitVolume()==null?"0":master.getUnitVolume().toString());
            items.add(item);
        });
        sku.setItems(items);
        return JacksonUtil.nonEmpty().toJson(sku);
    }


    /**
     * 根据货主和仓库更新出库订单限制
     *
     * @param client
     * @param whse
     * @param quantity
     */
    public void updateMassiveStockOutQuantity(String client, String whse, int quantity) {
        tblMassiveStockOutQuantityService.updateMassiveStockOutQuantity(client, whse, quantity);

    }


    /**
     * 根据货主和仓储查询出库单信息
     *
     * @param client
     * @param whse
     * @return
     */
    public List<StockOutOrderDTO> getStockOutOrders(String client, String whse,String whseCode) {
        List<TOrder> orderList = outBoundService.queryBeforeDayConfirmOutBoundsByWhseAnd(whse, client);
        List<StockOutOrderDTO> stockOutOrders = Lists.newArrayList();
        orderList.forEach(tOrder -> {
            StockOutOrderDTO stockOutOrder = new StockOutOrderDTO();
            stockOutOrder.setOwnerUserId(Joiner.on("-").join(whse,client));
            //stockOutOrder.setOwnerUserName(tOrder.getClientName());
            stockOutOrder.setOwnerUserName(clientService.getClientByWhAndCode(whse,client).getName());
            stockOutOrder.setStockOutOrderCode(tOrder.getOrderNumber());
            stockOutOrder.setStoreCode(whseCode);
            stockOutOrder.setTradeOrder(tOrder.getOrderId().toString());
            List<StockOutOrderDTO.ItemsBean> itemsBeanList = Lists.newArrayList();
            List<TOrderDetail> details = tOrder.getOrderDetailList();
            details.forEach(detail -> {
                StockOutOrderDTO.ItemsBean itemsBean = new StockOutOrderDTO.ItemsBean();
                itemsBean.setItemCode(detail.getItemNumber().substring(StringUtils.indexOf(detail.getItemNumber(),"-")+1));
                //itemsBean.setItemName(detail.getItemDescription());
                itemsBean.setItemName(materielService.getItemByCode(detail.getItemNumber().substring(StringUtils.indexOf(detail.getItemNumber(),"-")+1),client).getDescription());
                itemsBean.setQuantity(Math.round(detail.getQty()));
                itemsBeanList.add(itemsBean);
            });
            stockOutOrder.setItems(itemsBeanList);
            stockOutOrders.add(stockOutOrder);
        });

        return stockOutOrders;
    }


    public InventoryQuantityReponse.Item getInventoryQuantity(String clientCode, String whse, String itemCode) {
        BaseResponse<HashMap<String, Double>> response = storeService.queryCsfStoreInfo(clientCode, whse, itemCode);
        HashMap<String, Double> SunmaryMap = response.getData();
        InventoryQuantityReponse.Item item = new InventoryQuantityReponse.Item();
        item.setItemCode(itemCode);
        item.setStoreCode(Joiner.on("-").join(whse,clientCode));
        item.setInventoryType(1);
        item.setQuantity(SunmaryMap.get("unlockSunmary").intValue() + SunmaryMap.get("lockSunmary").intValue());
        item.setLockQuantity(SunmaryMap.get("lockSunmary").intValue());
        return item;
    }

    public InventoryQuantityBatchReponse.Item getInventoryQuantityBatch(String clientCode, String whse, String itemCode) {
        BaseResponse<HashMap<String, ScfStoredBatchInfo>> response = storeService.queryCsfStoreBatchInfo(clientCode, whse, itemCode);
        HashMap<String, ScfStoredBatchInfo> SunmaryMap = response.getData();
        InventoryQuantityBatchReponse.Item item = new InventoryQuantityBatchReponse.Item();
        //item.setBatchCode(SunmaryMap);
       /* item.setItemCode(itemCode);
        //item.setStoreCode(Joiner.on("-").join(whse,clientCode));
        item.setInventoryType(1);
        item.setQuantity(SunmaryMap.get("unlockSunmary").intValue());
        item.setLockQuantity(SunmaryMap.get("lockSunmary").intValue());*/
        return item;
    }

    public BaseResponse<HashMap<String, ScfStoredBatchInfo>> getInventoryQuantityBatch2(String clientCode, String whse, String itemCode) {
        BaseResponse<HashMap<String, ScfStoredBatchInfo>> response = storeService.queryCsfStoreBatchInfo(clientCode, whse, itemCode);
        //HashMap<String, ScfStoredBatchInfo> SunmaryMap = response.getData();
        //InventoryQuantityBatchReponse.Item item = new InventoryQuantityBatchReponse.Item();
        //item.setBatchCode(SunmaryMap);
       /* item.setItemCode(itemCode);
        //item.setStoreCode(Joiner.on("-").join(whse,clientCode));
        item.setInventoryType(1);
        item.setQuantity(SunmaryMap.get("unlockSunmary").intValue());
        item.setLockQuantity(SunmaryMap.get("lockSunmary").intValue());*/
        return response;
    }

    public List<MassiveStockOutRequest> getMassiveStockOut(String client, String whse,String whseCode) {
        List<MassiveStockOutRequest> stockOutRequests = Lists.newArrayList();
        List<TOrder> orderList = outBoundService.queryMassiveOutbounds(whse,client);
        orderList.forEach(order -> {
            MassiveStockOutRequest massiveStockOutRequest = new MassiveStockOutRequest();
            massiveStockOutRequest.setOwnerUserId(Joiner.on("-").join(whse,  order.getClientCode()));
//            massiveStockOutRequest.setStoreCode(order.getWhId());
            massiveStockOutRequest.setStoreCode(whseCode);
            massiveStockOutRequest.setErpOrderCode(order.getOrderId().toString());
            massiveStockOutRequest.setOrderCode(order.getOrderNumber());
            massiveStockOutRequest.setOrderType("201");
            List<MassiveStockOutRequest.Item> items = Lists.newArrayList();
            order.getOrderDetailList().forEach(tOrderDetail -> {
                MassiveStockOutRequest.Item item = new MassiveStockOutRequest.Item();
                item.setOrderLineNo(tOrderDetail.getLineNumber());
                item.setItemCode(tOrderDetail.getItemNumber().substring(StringUtils.indexOf(tOrderDetail.getItemNumber(),"-")+1));
                item.setQuantity(tOrderDetail.getQty().intValue());
                items.add(item);
            });
            massiveStockOutRequest.setItems(items);

            stockOutRequests.add(massiveStockOutRequest);
        });
        return stockOutRequests;
    }

    public List<MassiveStockInRequest> getMassiveStockIn(String client, String whse,String whseCode) {
        List<MassiveStockInRequest> stockInRequests = Lists.newArrayList();
        List<TPoMaster> orderList = inBoundService.queryScfInbounds(whse,client);
        orderList.forEach(order -> {
            MassiveStockInRequest massiveStockInRequest = new MassiveStockInRequest();
            massiveStockInRequest.setOrderCode(order.getPoNumber());
            massiveStockInRequest.setCpStoreCode(whseCode);
            massiveStockInRequest.setOwnerUserId(Joiner.on("-").join(whse,  order.getClientCode()));
            if (order.getConfirmDateTime() == null)
                massiveStockInRequest.setOrderConfirmTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            else
                massiveStockInRequest.setOrderConfirmTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getConfirmDateTime()));
            massiveStockInRequest.setStatus("3");
            massiveStockInRequest.setOrderType(302);
            massiveStockInRequest.setOrderCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateDate()));
            massiveStockInRequest.setOutBizCode(Joiner.on("-").join(whse,  order.getPoNumber()));

            List<MassiveStockInRequest.StockInCheckItem> items = Lists.newArrayList();
            order.getDetailList().forEach(tOrderDetail -> {
                MassiveStockInRequest.StockInCheckItem item = new MassiveStockInRequest.StockInCheckItem();
                item.setOrderItemId(Joiner.on("-").join(tOrderDetail.getPoNumber(),  tOrderDetail.getLineNumber()));
                item.setItemCode(tOrderDetail.getItemNumber().substring(StringUtils.indexOf(tOrderDetail.getItemNumber(),"-")+1));
                item.setItemName(materielService.getItemByCode(tOrderDetail.getItemNumber().substring(StringUtils.indexOf(tOrderDetail.getItemNumber(),"-")+1),client).getDescription());
                item.setInventoryType("1");
                item.setQuantity(tOrderDetail.getActualQty()==null?0L:tOrderDetail.getActualQty().longValue());
                item.setPlanquantity(tOrderDetail.getQty().intValue());

                //item.setOrderLineNo(tOrderDetail.getLineNumber());
                //item.setItemCode(tOrderDetail.getItemNumber().substring(StringUtils.indexOf(tOrderDetail.getItemNumber(),"-")+1));
                //item.setQuantity(tOrderDetail.getQty().intValue());
                items.add(item);
            });
            massiveStockInRequest.setStockInItems(items);

            stockInRequests.add(massiveStockInRequest);
        });
        return stockInRequests;
    }

    public void updateMassiveStockOutStatus(String clientCode,String whse,String orderCode,String status) {
        outBoundService.createFinOrderAcceptNotice(clientCode,  whse,orderCode,status);
        outBoundService.updateMassiveOutboundsStatus(clientCode,  whse,orderCode,status);
    }

    public void updateScfStockInStatus(String clientCode,String whse,String orderCode,String status) {
        inBoundService.updateScfInboundsStatus(whse,orderCode);
    }

    public void updateClientSendControl(String whid, String client, String status){
        clientService.updateClientSendControl(whid,client,status);
    }
}
