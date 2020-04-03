package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.annotation.JsonInclude;
import okhttp3.*;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.bus.web.model.SRInOrderDetail;
import org.shrek.hadata.service.bus.web.model.SRInOrderRequest;
import org.shrek.hadata.service.bus.web.model.SRResponse;


import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class SRService {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    WarehouseService warehouseService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    StoreService storeService;

//    /**
//     *入库单创建
//     * @param
//     * @return
//     */
//    public void pushInOrderToSR(){
//        String inOrderUrl="service/wh/push-product-action";
//        List<TPoMaster>  poMasters=inBoundService.queryConfirmInBoundsByWhse("111");
//        List<String> poNumbers=new ArrayList<String>();
//        SRInOrderRequest srInOrderRequest=new SRInOrderRequest();
//        List<SRInOrderDetail> inOrderDetails=new ArrayList<SRInOrderDetail>();
//        for(int i = 0; i < poMasters.size(); i++){
//            SRInOrderDetail inOrderDetail=new SRInOrderDetail();
//                List<TReceipt> receipts=inBoundService.queryTReceiptByPoNumberAndWhIdAndLoadId(poMasters.get(i).getPoNumber(),"");
//                for(int y = 0; y < receipts.size(); y++){
//                    TItemUom itemUom=materielService.getItemUomByWhIdandItemNumber("111",receipts.get(i).getItemNumber(),"CS");
//                    TItemMaster itemMaster=materielService.getItemMasterByWhIdandItemNumber("111",receipts.get(i).getItemNumber());
//                    inOrderDetail.setWhNo(receipts.get(i).getWhId());
//                    inOrderDetail.setExWhDate(receipts.get(i).getReceiptDate());
//                    inOrderDetail.setOrderNo(receipts.get(i).getPoNumber());
//                    inOrderDetail.setPerNum(itemUom.getConversionFactor().intValue());
//                    inOrderDetail.setProductBatchNo(receipts.get(i).getLotNumber());
//                    inOrderDetail.setProductDate(receipts.get(i).getExpirationDate());
//                    inOrderDetail.setProductNum((int) Math.floor(receipts.get(i).getQtyReceived().intValue()/itemUom.getConversionFactor().intValue()));
//                    inOrderDetail.setProductNo(receipts.get(i).getItemNumber());
//                    inOrderDetail.setProductPrice(itemMaster.getPrice());
//                    inOrderDetail.setProductSkuNo(receipts.get(i).getItemNumber());
//                    inOrderDetail.setTotalAmount(itemMaster.getPrice()*receipts.get(i).getQtyReceived().intValue());
//                    inOrderDetail.setTotalNum(receipts.get(i).getQtyReceived().intValue());
//                    inOrderDetail.setExWhDate(receipts.get(i).getReceiptDate());
//                    inOrderDetail.setWhPostionNo(receipts.get(i).getLocationId());
//                    inOrderDetails.add(inOrderDetail);
//                }
//            poNumbers.add(poMasters.get(i).getPoNumber());
//        }
//        srInOrderRequest.setProjectNo("111");
//        srInOrderRequest.setTotalRecordNum(inOrderDetails.size());
//        String json = JacksonUtil.nonAlways().toJson(srInOrderRequest);
//        System.out.println("入库回传发送的JSON串为"+json);
//        try {
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//            Request request = new Request.Builder().url(inOrderUrl).post(body).build();
//            Response response = client.newCall(request).execute();
//            String responses = response.body().string();
//            JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
//            HashMap results = js.fromJson(responses, HashMap.class);
//            if ("200".equals(results.get("code"))) {
//                for(int i = 0; i < poNumbers.size(); i++){
//                    inBoundService.updateInBoundsBack("", poNumbers.get(i));
//                    System.out.println(results.get("message")+poNumbers.get(i));
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("入库数据回传失败,原因"+e.toString());
//        }
//    }
//    public void pullOutOrderFromSR(){
//
//    }
}
