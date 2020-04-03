package org.shrek.hadata.service.bus.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.util.ContentConvert;
import org.shrek.hadata.commons.util.SZSignUtil;
import org.shrek.hadata.service.iwms.model.QueryCity;
import org.shrek.hadata.service.bus.service.*;
import org.shrek.hadata.service.bus.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * @author zhouwenheng
 * @version 1.0
 * @date 2019年04月20日 11:05
 */
@Slf4j
@RestController
@RequestMapping
public class ServiceController {

    @Autowired
    OrderToTmsScheduled orderToTmsScheduled;
    @Autowired
    StorageService storageService;
    @Autowired
    StorageScheduled storageScheduled;
    @Autowired
    BinCardService binCardService;
    @Autowired
    BinCardScheduled binCardScheduled;
    @Autowired
    GHService ghService;
    @Autowired
    SRService srService;
    @Autowired
    OTMSOrderToWmsService oTMSOrderToWmsService;
    @Autowired
    SZService szService;
    @Autowired
    SZScheduled szScheduled;


    /**
     * 广汇订单信息接入
     */
    @Scheduled(fixedDelay=1800000,initialDelay=2000)
    public void ghOrderCreated(){
        try {
            log.info("广汇客户信息接入定时任务开始");
            ghService.ghCustomerInfo();
            log.info("广汇客户信息接入定时任务完成");
            log.info("广汇商品信息接入定时任务开始");
            ghService.ghProductInfo();
            log.info("广汇商品信息接入定时任务完成");
            log.info("广汇订单信息接入定时任务开始");
            ghService.ghOrderCreated();
            log.info("广汇订单信息接入定时任务完成");
        } catch (Exception e) {
            log.error("广汇订单信息接入定时任务失败:"+e.getMessage());
        }
    }


    /**
     * 惠凯订单信息接入
     */
    @Scheduled(fixedDelay=1800000,initialDelay=2000)
    public void hkOrderCreated(){
        try {
            log.info("汇楷客户信息接入定时任务开始");
            ghService.hkCustomerInfo();
            log.info("汇楷客户信息接入定时任务完成");
            log.info("汇楷商品信息接入定时任务开始");
            ghService.hkProductInfo();
            log.info("汇楷商品信息接入定时任务完成");
            log.info("汇楷订单信息接入定时任务开始");
            ghService.hkOrderCreated();
            log.info("汇楷订单信息接入定时任务完成");
        } catch (Exception e) {
            log.error("惠凯订单信息接入定时任务失败:"+e.getMessage());
        }
    }



    /**
     *BinCard出入库定时器
     */
    @Scheduled(fixedDelay=1800000,initialDelay=2000)
    public void creatOrder(){
        log.info("BinCard出入库定时任务开始");
        binCardService.createOrder();
        log.info("BinCard出入库定时任务完成");
        log.info("BinCard回传定时任务开始");
        binCardScheduled.returnOrder();
        log.info("BinCard回传定时任务完成");
    }
    @ResponseBody
    @RequestMapping(value = "/queryStock")
    public StorageResponse queryStock() {
        return storageService.queryStock();
    }
    @ResponseBody
    @RequestMapping(value = "/queryInfo")
    public StorageResponse queryInfo(@RequestParam String content) {
        QueryCity queryCity= (QueryCity) JSONObject.parseObject(content, QueryCity.class);
        return storageService.queryInfo(queryCity);
    }
    @ResponseBody
    @RequestMapping(value = "/queryThroughput")
    public StorageResponse queryThroughput(@RequestParam String content) {
        QueryCity queryCity= (QueryCity) JSONObject.parseObject(content, QueryCity.class);
        return storageService.queryThroughput(queryCity);
    }
    /**
     * 出库订单发送TMS
     */
    @Scheduled(fixedDelay=1800000,initialDelay=2000)
    public void orderSendToTms(){
        try {
            log.info("订单发送TMS开始");
            orderToTmsScheduled.feedbackPurchaseOutStorage();
            log.info("订单发送TMS完成");
        } catch (Exception e) {
            log.error("订单发送TMS失败:"+e.getMessage());
        }
    }



    /**
     * 下载OTMS订单到WMS
     */
    @Scheduled(fixedDelay=300000,initialDelay=2000)
    public void readOrderToWms(){
        try {
            log.info("OTMS订单下载定时任务开始");
            oTMSOrderToWmsService.readOrderToWms();
            log.info("OTMS订单下载定时任务完成");
        } catch (Exception e) {
            log.error("OTMS订单下载失败:"+e.getMessage());
        }
    }
    /**
     * 尚展入库单
     */
    @ResponseBody
    @RequestMapping(value = "/createSZInOrder")
    public String createSZInOrder(@RequestParam String content,@RequestParam String sign,@RequestParam String notifyid,@RequestParam String notifytime){
        SZResponse szResponse;
        String json;

        try {
            Map<String, String> params=new HashMap<String, String>();
            params.put("notifyid",notifyid);
            params.put("notifytime",notifytime);
            params.put("butype","json");
            params.put("source","sz");
            params.put("content",content );
            String signStr= SZSignUtil.signTopRequest(params,"819H85w17ds443Po7Zgc89738N8hKXq1","MD5");
            if(signStr.equals(sign)){
                SZInOrderRequest szInOrderRequest= (SZInOrderRequest) JSONObject.parseObject(ContentConvert.decryptString(content,"819H85w17ds443Po7Zgc89738N8hKXq1"), SZInOrderRequest.class);
                log.info("SZ尚展入库单报文："+ JSON.toJSON(szInOrderRequest));
                szResponse=szService.createSZInOrder(szInOrderRequest);
            }else{
                szResponse=SZResponse.fail("签名认证失败","101");
            }
        } catch (Exception e) {
            szResponse=SZResponse.fail(e.getMessage(),"401");
            log.error("SZ入库单URL编码失败:"+e.getMessage());
        }
        json= JSON.toJSONString(szResponse);
        return json;
    }
    /**
     * 尚展出库单
     */
    @ResponseBody
    @RequestMapping(value = "/createSZOutOrder")
    public String createSZOutOrder(@RequestParam String content,@RequestParam String sign,@RequestParam String notifyid,@RequestParam String notifytime){
            SZResponse szResponse;
        String json;
            try {
                Map<String, String> params=new HashMap<String, String>();
                params.put("notifyid",notifyid);
                params.put("notifytime",notifytime);
                params.put("butype","json");
                params.put("source","sz");
                params.put("content",content);
                String signStr= SZSignUtil.signTopRequest(params,"819H85w17ds443Po7Zgc89738N8hKXq1","MD5");
                if(signStr.equals(sign)){
                    SZOutOrderRequest szOutOrderRequest= (SZOutOrderRequest) JSONObject.parseObject(ContentConvert.decryptString(content,"819H85w17ds443Po7Zgc89738N8hKXq1"), SZOutOrderRequest.class);
                    log.info("SZ尚展出库单报文："+ JSON.toJSON(szOutOrderRequest));
                    szResponse=szService.createSZOutOrder(szOutOrderRequest);
                }else{
                    szResponse=SZResponse.fail("签名认证失败","101");
                }
            } catch (Exception e) {
                szResponse=SZResponse.fail(e.getMessage(),"401");
                log.error("SZ出库单URL编码失败:"+e.getMessage());
            }
        json= JSON.toJSONString(szResponse);
            return json;
    }
    /**
     * 尚展移仓调拨单
     */
    @ResponseBody
    @RequestMapping(value = "/shiftOrder")
    public String shiftOrder(@RequestParam String content,@RequestParam String sign,@RequestParam String notifyid,@RequestParam String notifytime){
        SZResponse szResponse;
        String json;
        try {
            Map<String, String> params=new HashMap<String, String>();
            params.put("notifyid",notifyid);
            params.put("notifytime",notifytime);
            params.put("butype","json");
            params.put("source","sz");
            params.put("content",content);
            String signStr= SZSignUtil.signTopRequest(params,"819H85w17ds443Po7Zgc89738N8hKXq1","MD5");
            if(signStr.equals(sign)){
                SZShiftOrderRequest shiftOrder= (SZShiftOrderRequest) JSONObject.parseObject(ContentConvert.decryptString(content,"819H85w17ds443Po7Zgc89738N8hKXq1"), SZShiftOrderRequest.class);
                log.info("SZ尚展移仓调拨单报文："+ JSON.toJSON(shiftOrder));
                szResponse=szService.shiftOrder(shiftOrder);
            }else{
                szResponse=SZResponse.fail("签名认证失败","101");
            }
        } catch (Exception e) {
            szResponse=SZResponse.fail(e.getMessage(),"401");
            log.error("SZ移仓调拨单URL编码失败:"+e.getMessage());
        }
        json= JSON.toJSONString(szResponse);
        return json;
    }
    /**
     * 尚展入库单取消
     */
    @ResponseBody
    @RequestMapping(value = "/closeInOrder")
    public String closeInOrder(@RequestParam String content,@RequestParam String sign,@RequestParam String notifyid,@RequestParam String notifytime){
        SZResponse szResponse;
        String json;
        try {
            Map<String, String> params=new HashMap<String, String>();
            params.put("notifyid",notifyid);
            params.put("notifytime",notifytime);
            params.put("butype","json");
            params.put("source","sz");
            params.put("content",content);
            String signStr= SZSignUtil.signTopRequest(params,"819H85w17ds443Po7Zgc89738N8hKXq1","MD5");
            if(signStr.equals(sign)){
                CloseOrderRequest closeOrderRequest= (CloseOrderRequest) JSONObject.parseObject(ContentConvert.decryptString(content,"819H85w17ds443Po7Zgc89738N8hKXq1"), CloseOrderRequest.class);
                log.info("SZ尚展入库单取消报文："+ JSON.toJSON(closeOrderRequest));
                szResponse=szService.closeInOrder(closeOrderRequest);
            }else{
                szResponse=SZResponse.fail("签名认证失败","101");
            }
        } catch (Exception e) {
            szResponse=SZResponse.fail(e.getMessage(),"401");
            log.error("SZ入库单URL编码失败:"+e.getMessage());
        }
        json= JSON.toJSONString(szResponse);
        return json;
    }
    /**
     * 尚展出库单取消
     */
    @ResponseBody
    @RequestMapping(value = "/closeOutOrder")
    public String closeOutorder(@RequestParam String content,@RequestParam String sign,@RequestParam String notifyid,@RequestParam String notifytime){
        SZResponse szResponse;
        String json;
        try {
            Map<String, String> params=new HashMap<String, String>();
            params.put("notifyid",notifyid);
            params.put("notifytime",notifytime);
            params.put("butype","json");
            params.put("source","sz");
            params.put("content",content);
            String signStr= SZSignUtil.signTopRequest(params,"819H85w17ds443Po7Zgc89738N8hKXq1","MD5");
            if(signStr.equals(sign)){
                CloseOrderRequest closeOrderRequest= (CloseOrderRequest) JSONObject.parseObject(ContentConvert.decryptString(content,"819H85w17ds443Po7Zgc89738N8hKXq1"), CloseOrderRequest.class);
                log.info("SZ尚展出库单取消报文："+ JSON.toJSON(closeOrderRequest));
                szResponse=szService.closeOutorder(closeOrderRequest);
            }else{
                szResponse=SZResponse.fail("签名认证失败","101");
            }
        } catch (Exception e) {
            szResponse=SZResponse.fail("数据URL编码失败","401");
            log.error("SZ出库单URL编码失败:"+e.getMessage());
        }
        json= JSON.toJSONString(szResponse);
        return json;
    }
    /**
     * 尚展商品信息同步
     */
    @ResponseBody
    @RequestMapping(value = "/singleitemSynchronize")
    public String singleitemSynchronize(@RequestParam String content,@RequestParam String sign,@RequestParam String notifyid,@RequestParam String notifytime){
        SZResponse szResponse;
        String json;
        try {
            Map<String, String> params=new HashMap<String, String>();
            params.put("notifyid",notifyid);
            params.put("notifytime",notifytime);
            params.put("butype","json");
            params.put("source","sz");
            params.put("content",content);
            String signStr= SZSignUtil.signTopRequest(params,"819H85w17ds443Po7Zgc89738N8hKXq1","MD5");
            if(signStr.equals(sign)){
                List<SZMasterRequest> masterRequest= (List<SZMasterRequest>) JSONArray.parseArray(ContentConvert.decryptString(content,"819H85w17ds443Po7Zgc89738N8hKXq1"), SZMasterRequest.class);
                log.info("SZ商品信息报文："+ JSON.toJSON(masterRequest));
                szResponse=szService.SingleitemSynchronize(masterRequest);
            }else{
                szResponse=SZResponse.fail("签名认证失败","101");
            }
        } catch (Exception e) {
            szResponse=SZResponse.fail("数据URL编码失败","401");
            log.error("SZ商品信息同步失败:"+e.getMessage());
        }
        json= JSON.toJSONString(szResponse);
        return json;
    }

    /**
     * SZ入库单反馈
     */
    @Scheduled(fixedDelay=300000,initialDelay=2000)
    public void inOrderReturn(){
        try {
            log.info("SZ入库单反馈定时任务开始");
            szScheduled.inOrderReturn();
            log.info("SZ入库单反馈定时任务完成");
        } catch (Exception e) {
            log.error("SZ入库单反馈失败:"+e.getMessage());
        }
    }
    /**
     * SZ出库单反馈
     */
    @Scheduled(fixedDelay=300000,initialDelay=2000)
    public void outOrderReturn(){
        try {
            log.info("SZ出库单反馈定时任务开始");
            szScheduled.outOrderReturn();
            log.info("SZ出库单反馈定时任务完成");
        } catch (Exception e) {
            log.error("SZ出库单反馈失败:"+e.getMessage());
        }
    }
    /**
     * SZ库存反馈
     */
    @Scheduled(fixedDelay=300000,initialDelay=2000)
    public void StockQuery(){
        try {
            log.info("SZ库存反馈定时任务开始");
            szScheduled.StockQuery();
            log.info("SZ库存反馈定时任务完成");
        } catch (Exception e) {
            log.error("SZ库存反馈失败:"+e.getMessage());
        }
    }

//    /**
//     * 出库拣货
//     */
//    @Scheduled(fixedDelay=1800000,initialDelay=2000)
//    public void stockPicking(){
//        try {
//            log.info("出库拣货回传定时任务开始");
//            storageScheduled.stockPicking();
//            log.info("出库拣货回传定时任务完成");
//        } catch (Exception e) {
//            log.error("出库拣货回传定时任务失败:"+e.getMessage());
//        }
//    }
//
//    /**
//     * 出库过账
//     */
//    @ResponseBody
//    @RequestMapping(value = "/reconciliation")
//    public void reconciliation(@RequestParam String content){
//        try {
//            log.info("出库拣货过账任务开始");
//            storageService.reconciliation(content);
//            log.info("出库拣货过账任务完成");
//        } catch (Exception e) {
//            log.error("出库拣货过账任务失败:"+e.getMessage());
//        }
//    }
//    /**
//     * 取消波次号
//     */
//    @Scheduled(fixedDelay=1800000,initialDelay=2000)
//    public void cancleWaId(){
//        try {
//            storageScheduled.cancleWaId();
//        } catch (Exception e) {
//            log.error("取消拣货计划失败,失败原因:"+e.getMessage());
//        }
//    }
}
