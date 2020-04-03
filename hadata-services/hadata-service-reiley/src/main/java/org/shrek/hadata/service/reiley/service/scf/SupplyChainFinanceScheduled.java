package org.shrek.hadata.service.reiley.service.scf;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.OkHttpUtil;
import org.shrek.hadata.commons.util.SignUtil;
import org.shrek.hadata.commons.util.StringUtil;
import org.shrek.hadata.service.iwms.model.TClient;
import org.shrek.hadata.service.iwms.service.ClientService;
import org.shrek.hadata.service.iwms.service.WarehouseService;
import org.shrek.hadata.service.reiley.config.ReileyScfConfig;
import org.shrek.hadata.service.reiley.service.scf.model.MassiveStockOutQuantityReponse;
import org.shrek.hadata.service.reiley.service.scf.model.MassiveStockOutReponse;
import org.shrek.hadata.service.reiley.service.scf.model.MassiveStockOutRequest;
import org.shrek.hadata.service.reiley.service.scf.model.MassiveStockInRequest;
import org.shrek.hadata.service.reiley.service.scf.model.StockOutOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 09:37
 */
@Slf4j
@Component
public class SupplyChainFinanceScheduled {

    private static String URL = "http://link.cainiao.com/gateway/link.do";
    private static String URL_TEST = "https://link.tbsandbox.com/gateway/link.do";
   // private static String URL_TEST = "https://linkdaily.tbsandbox.com/gateway/link.do";
    private static String CP_CODE = "SCF0100";
    private static String CP_CODE_GZ = "SCF0101";
    //private static String CP_CODE_TEST = "831511_RL_test";
    //private static String CP_CODE_TEST_1 = "REILEY_SZ";
    private static String CP_KEY = "0S2s9025241OMvmZqm4z2LfX99O0ZblU";//正式环境参数
    //private static String CP_KEY = "ms5o905zt1Gzjlo87z70061HT7I0f856";//彩虹桥环境
    @Autowired
    ReileyScfConfig reileyScfConfig;
    @Autowired
    OkHttpClient okHttpClient;
    @Autowired
    SupplyChainFinanceService supplyChainFinanceService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    //WarehouseService warehouseService;
    @Value("${scheduled.flag}")
    private boolean scheduled;

    private String httpPost(String method, String context) {
        log.warn(context);
        HashMap<String, String> params = Maps.newHashMap();
        params.put("msg_type", method);
        //params.put("logistic_provider_id", context.contains("601-6001")?CP_CODE:CP_CODE_GZ);
        params.put("logistic_provider_id", getCPCode(context));
        params.put("data_digest", SignUtil.cainiaoSign(context, CP_KEY));
        params.put("logistics_interface", context);
        log.warn(params.get("logistic_provider_id"));
        log.warn(SignUtil.cainiaoSign(context, CP_KEY));
        return OkHttpUtil.post(okHttpClient, URL, params);
    }

    private String  getCPCode(String context)
    {
        String cpCode = "";
        for(String key:reileyScfConfig.getClients().keySet())
        {
            if(context.contains(reileyScfConfig.getClients().get(key)))
                cpCode = reileyScfConfig.getClients().get(key);
        }
        //log.warn(cpCode);
        if(cpCode != null)
        {
            List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(cpCode);
            //log.warn(list.get(0));
            //log.warn(list.get(1));
            TClient client = new TClient();
            client = clientService.getClientByWhAndCode(list.get(0),list.get(1));
            //log.warn(list.get(0));
            //log.warn(list.get(1));
            //cpCode = (clientService.getClientByWhAndCode(list.get(0),list.get(1))==null || clientService.getClientByWhAndCode(list.get(0),list.get(1)).getContact() == null)?"":clientService.getClientByWhAndCode(list.get(0),list.get(1)).getContact();
            if (client != null)
                cpCode = client.getContact();
        }
        return cpCode;
    }

    /**
     * 货品信息上传给菜鸟
     * SCF_SKU_INFO_UPLOAD
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void uploadSku2Cainao() {
        if (scheduled) {
            reileyScfConfig.getClients().forEach((k, v) -> {
                log.warn("货品同步给菜鸟:" + v);
                List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(v);
                String context = supplyChainFinanceService.getSkuInfo(list.get(1), list.get(0), k);
                String response = httpPost("SCF_SKU_INFO_UPLOAD", context);
                log.warn("货品同步给菜鸟反馈:" + response);
                System.out.println(response);
            });
        }
    }

    /**
     * 大货出库数目查询，cp查询具体货主每天大货出库的数目限制，超过此限制的出库单需要向金融申请
     * SCF_MASSIVE_STOCK_OUT_QUANTITY
     */
    @Scheduled(cron = "0 30 1 * * ?")
    public void getMassiveStockOutQuantity() {
        if (scheduled) {
            reileyScfConfig.getClients().forEach((k, v) -> {
                HashMap<String, String> params = Maps.newHashMap();
                params.put("ownerUserId", v);
                params.put("storeCode", k);
                String response = httpPost("SCF_MASSIVE_STOCK_OUT_QUANTITY", JacksonUtil.nonEmpty().toJson(params));
                MassiveStockOutQuantityReponse mReponse = JacksonUtil.nonEmpty().fromJson(response, MassiveStockOutQuantityReponse.class);
                if (mReponse.isSuccess()) {
                    List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(v);
                    supplyChainFinanceService.updateMassiveStockOutQuantity(list.get(1), list.get(0), mReponse.getQuantity());
                }
            });
        }
    }

    /**
     * 出库单同步给金融系统
     * SCF_STOCK_OUT_ORDER_UPLOAD
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void uploadStockOutOrder() {
        if (scheduled) {
            reileyScfConfig.getClients().forEach((k, v) -> {
                List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(v);
                List<StockOutOrderDTO> stockOutOrders = supplyChainFinanceService.getStockOutOrders(list.get(1), list.get(0), k);
                stockOutOrders.forEach(stockOutOrder -> {
                    System.out.println(JacksonUtil.nonAlways().toJson(stockOutOrder));
                    log.warn("出库单同步给金融系统:" + JacksonUtil.nonAlways().toJson(stockOutOrder));
                    String response = httpPost("SCF_STOCK_OUT_ORDER_UPLOAD", JacksonUtil.nonAlways().toJson(stockOutOrder));
                    log.warn("出库单同步给金融系统:" + response);
                });
            });
        }

    }


    /**
     * 大货出库申请（单次）
     * SCF_MASSIVE_STOCK_OUT_UPLOAD
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void uploadMassiveStockOut() {
        if (scheduled) {
            reileyScfConfig.getClients().forEach((k, v) -> {
                List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(v);
                List<MassiveStockOutRequest> massiveStockOutRequestList = supplyChainFinanceService.getMassiveStockOut(list.get(1), list.get(0), k);
                massiveStockOutRequestList.forEach(massiveStockOutRequest -> {
                    String context = JacksonUtil.nonEmpty().toJson(massiveStockOutRequest);
                    log.warn("大货出库申请:" + context);
                    String response = httpPost("SCF_MASSIVE_STOCK_OUT_UPLOAD", context);
                    log.warn("大货出库申请:" + response);
                    if (!StringUtil.isEmpty(response)) {
                        MassiveStockOutReponse massiveStockOutReponse = JacksonUtil.nonEmpty().fromJson(response, MassiveStockOutReponse.class);
                        if (massiveStockOutReponse.isSuccess()) {
                            supplyChainFinanceService.updateMassiveStockOutStatus(list.get(1), list.get(0), massiveStockOutRequest.getOrderCode(), "I");
                        }
                    }
                });
            });
        }
    }

    /**
     * 入库消息同步菜鸟接口
     * SCF_STOCK_IN_ORDER_UPLOAD
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void uploadStockInOrder() {
        if (scheduled) {
            reileyScfConfig.getClients().forEach((k, v) -> {
                List<String> list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(v);
                List<MassiveStockInRequest> massiveStockInRequestList = supplyChainFinanceService.getMassiveStockIn(list.get(1), list.get(0), k);
                log.warn("入库消息同步:" + massiveStockInRequestList.size());
                if (massiveStockInRequestList.size() > 0) {
                    massiveStockInRequestList.forEach(massiveStockInRequest -> {
                        String context = JacksonUtil.nonEmpty().toJson(massiveStockInRequest);
                        log.warn("入库消息同步:" + context);
                        String response = httpPost("SCF_STOCK_IN_ORDER_UPLOAD", context);
                        log.warn("入库消息同步:" + response);
                        if (!StringUtil.isEmpty(response)) {
                            MassiveStockOutReponse massiveStockOutReponse = JacksonUtil.nonEmpty().fromJson(response, MassiveStockOutReponse.class);
                            if (massiveStockOutReponse.isSuccess() || ("入库单已经上传过".equals(massiveStockOutReponse.getErrorMsg()))) {
                                supplyChainFinanceService.updateScfStockInStatus(list.get(1), list.get(0), massiveStockInRequest.getOrderCode(), "Y");
                            }
                        }
                    });
                }
            });
        }
    }

}
