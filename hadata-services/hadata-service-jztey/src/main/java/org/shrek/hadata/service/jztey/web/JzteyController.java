package org.shrek.hadata.service.jztey.web;


import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.Constants;
import org.shrek.hadata.commons.exception.HadataException;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.MapUtil;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.*;
import org.shrek.hadata.service.jztey.config.JzteyConfig;
import org.shrek.hadata.service.jztey.model.Platform;
import org.shrek.hadata.service.jztey.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 14:05
 */
@Slf4j
@RestController
public class JzteyController {

    @Autowired
    JzteyConfig jzteyConfig;

    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    @Reference(version = "2.0.1",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Reference(version = "2.0.1",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    StoreService storeService;
    @Reference(version = "2.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    CustomerService customerService;


    /**
     * 接收物料信息
     *
     * @param content
     * @return
     */
    @RequestMapping("/receive_material")
    @ResponseBody
    public String receiveMaterial(@RequestParam String content) {
        try {
            Map<String, List<TClient>> clientMap = Maps.newHashMap();
            List<TItemMaster> masterList = Lists.newArrayList();
            RequestWrapper<RequestMaterial> wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, RequestWrapper.class, RequestMaterial.class);
            List<RequestMaterial> masters = wrapper.getList();
            masters.forEach(master -> {
                TItemMaster itemMaster = new TItemMaster();
                itemMaster.setItemNumber(master.getProdno());
                itemMaster.setDisplayItemNumber(master.getProdno());
                itemMaster.setAltItemNumber(master.getProdid());
                itemMaster.setDescription(master.getProdname());
                itemMaster.setShelfLife(0);
                itemMaster.setProductionDateControl("Y");
                itemMaster.setLotControl("F");
                itemMaster.setExpirationDateControl("Y");
                List<TClient> clients;
                if (clientMap.containsKey(master.getBranchid())) {
                    clients = clientMap.get(master.getBranchid());
                } else {
                    clients = clientService.queryClientByExtCode(master.getBranchid());
                    if (clients.isEmpty()) {
                        throw new HadataException(master.getProdid() + "：客户代码不存在。！");
                    }
                    clientMap.put(master.getBranchid(), clients);
                }
                itemMaster.setClients(clients);
                TItemUom itemUom = new TItemUom();
                itemUom.setItemMasterId(itemMaster.getItemMasterId());
                itemUom.setItemNumber(itemMaster.getItemNumber());
                itemUom.setConversionFactor(1D);
                itemUom.setUom("EA");
                itemUom.setUomPrompt("件");
                itemUom.setPickPutId("CASE");
                itemUom.setDefaultPickArea("EACH");
                itemMaster.getUoms().add(itemUom);
                masterList.add(itemMaster);
            });
            materielService.createMaterials(masterList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "false";
        }
        return "true";
    }

    /**
     * 接收入库订单
     *
     * @param content
     * @return
     */
    @RequestMapping("/receive_inbound")
    public String receiveInbound(@RequestParam String content) {
        try {
            List<TiReceiveOrderMaster> masterList = Lists.newArrayList();
            RequestWrapper<RequestInBound> wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, RequestWrapper.class, RequestInBound.class);
            Map<String, List<RequestInBound>> inBounds = wrapper.getList().stream().collect(Collectors.groupingBy(RequestInBound::getDjbh));
            inBounds.forEach((key, value) -> {
                TiReceiveOrderMaster<TiReceiveOrderJztey> master = new TiReceiveOrderMaster();
                master.setStoreCode(value.get(0).getStoreid());
                TClient tClient = clientService.queryClient(value.get(0).getBranchid(), value.get(0).getStoreid());
                master.setCustCode(tClient.getClientCode());
                master.setOrderCode(value.get(0).getDjbh());
                master.setOrderType(value.get(0).getYewtype());
                master.setOrderDate(value.get(0).getRq());
                master.setDealerCode(null);
                master.setActiveFlag("1");
                master.setProcFlag("0");
                Platform platform = jzteyConfig.getPlatforms().get(value.get(0).getBranchid() + value.get(0).getStoreid());
                TiReceiveOrderJztey jztey = new TiReceiveOrderJztey();
                jztey.setOrgCode(value.get(0).getDwbh());
                jztey.setContact(value.get(0).getLxr());
                jztey.setClerk(platform.getCgy());
                jztey.setOperator(platform.getShy());
                jztey.setDeptCode(value.get(0).getBmid());
                jztey.setPhoneNumber(value.get(0).getLxdh());
                jztey.setIsCnMedicine(value.get(0).getSfzy());
                jztey.setInboundType(value.get(0).getRktype());
                jztey.setUploader(value.get(0).getScf());
                jztey.setRejectType(value.get(0).getThlb());
                jztey.setLmisInvokingStatus(value.get(0).getZt());
                jztey.setBranch(value.get(0).getBranchid());
                jztey.setIsStressCheck(value.get(0).getIszdys());
                jztey.setChecker(platform.getZjy());
                if(value.get(0).getYewtype().equals("11")){
                    jztey.setIsConsign("Y");
                }
                master.setExtend(jztey);
                value.forEach(inBound -> {
                    TiReceiveOrderDetail detail = new TiReceiveOrderDetail();
                    detail.setId(inBound.getDjsort());
                    detail.setProductCode(inBound.getSpid());
                    detail.setProductBatch(inBound.getPihao());
                    detail.setProductMakeDate(inBound.getBaozhiqi());
                    detail.setProductExpirationDate(inBound.getSxrq());
                    detail.setProductNum(inBound.getShl());
                    detail.setProductBoxNum(inBound.getJiansh());
                    detail.setProductBulkNum(inBound.getLingsshl());
                    detail.setProductArrivalDate(inBound.getYdhrq());
                    detail.setProductUnitPrice(inBound.getDj());
                    detail.setProductTotalPrice(inBound.getJe());
                    detail.setProductQuality(inBound.getYspd());
                    detail.setProductBackReason(inBound.getYuany());
                    master.getDetails().add(detail);
                });
                masterList.add(master);
            });
            inBoundService.createInBounds(masterList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "false";

        }
        return "true";
    }

    /**
     * 查询入库订单执行结果-正常
     *
     * @param branch
     * @param store
     * @return
     */
    @RequestMapping("/query_inbound")
    public String queryInbound(@RequestParam(name = "branchid") String branch, @RequestParam(name = "storeid") String store) {
        return JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(queryInbound(branch, store, "N"));
    }

    /**
     * 查询入库订单执行结果-寄售
     *
     * @param branch
     * @param store
     * @return
     */
    @RequestMapping("/query_inbound_consign")
    public String queryInboundConsign(@RequestParam(name = "branchid") String branch, @RequestParam(name = "storeid") String store) {
        return JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(queryInbound(branch, store, "Y"));
    }

    /**
     * 查询入库订单执行结果
     *
     * @param branch
     * @param store
     * @param type
     * @return
     */
    private ResponseWrapper<ResponseInBound> queryInbound(String branch, String store, String type) {
        HashMap<String, List<String>> updateParams = Maps.newHashMap();
        HashMap<String, String> params = Maps.newHashMap();
        params.put("branch", branch);
        params.put("store", store);
        params.put("type", type);
        List<HashMap<String, String>> results = inBoundService.queryInBounds(params);
        ResponseWrapper<ResponseInBound> wrapper = new ResponseWrapper();
        results.forEach(result -> {
            try {
                ResponseInBound responseInBound = new ResponseInBound();
                MapUtil.toObject(responseInBound, result, true);
                Optional.ofNullable(updateParams.get(responseInBound.getWhId())).map(map -> map.add(responseInBound.getPoNumber())).orElseGet(() -> {
                    updateParams.put(responseInBound.getWhId(), Lists.newArrayList(responseInBound.getPoNumber()));
                    return true;
                });
                wrapper.getList().add(responseInBound);
            } catch (IllegalAccessException e) {
                return;
            } catch (InvocationTargetException e) {
                return;
            } catch (InstantiationException e) {
                return;
            }
        });
        inBoundService.updateInBoundsBack(updateParams);
        return wrapper;
    }

    /**
     * 接收出库订单
     *
     * @param content
     * @return
     */
    @RequestMapping("/receive_outbound")
    public String receiveOutbound(@RequestParam String content) {
        try {
            List<TiSendOrderMaster> masterList = Lists.newArrayList();
            RequestWrapper<RequestOutBound> wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, RequestWrapper.class, RequestOutBound.class);
            Map<String, List<RequestOutBound>> outBounds = wrapper.getList().stream().collect(Collectors.groupingBy(RequestOutBound::getDjbh));
            outBounds.forEach((key, value) -> {
                Platform platform = jzteyConfig.getPlatforms().get(value.get(0).getBranchid() + value.get(0).getStoreid());
                TiSendOrderMaster<TiSendOrderJztey> master = new TiSendOrderMaster();
                master.setStoreCode(value.get(0).getStoreid());
                TClient tc = clientService.queryClient(value.get(0).getBranchid(), value.get(0).getStoreid());
                master.setCustCode(tc.getClientCode());
                master.setOrderCode(value.get(0).getDjbh());
                master.setOrderType(value.get(0).getYewtype());
                Date orderDate = DateUtil.getDateFromString(value.get(0).getRq(), "yyyy-MM-dd HH-mm-ss");
                final String[] strOrderDate = {""};
                Optional.ofNullable(orderDate).ifPresent((v) -> {
                    strOrderDate[0] = DateUtil.formatDate(orderDate);
                });
                String dealerCode = customerService.queryCustomerId(value.get(0).getDwbh(), tc.getClientCode(), tc.getWhId());
                master.setDealerCode(Optional.ofNullable(dealerCode).orElse(value.get(0).getCustno()));
                master.setDealerName(value.get(0).getCustname());
                master.setDealerContact(value.get(0).getContactperson());
                master.setDealerContactTel(value.get(0).getContactphone());
                master.setDealerAddress(value.get(0).getConsigneeadd() !=null?value.get(0).getConsigneeadd():value.get(0).getConsigneeadd2());
                master.setActiveFlag("1");
                master.setProcFlag("0");
                TiSendOrderJztey sendOrderJztey = new TiSendOrderJztey();
                sendOrderJztey.setUnitInCode(value.get(0).getDwbh());
                sendOrderJztey.setSalesman(platform.getXsy());
                sendOrderJztey.setDepInCode(value.get(0).getBmid());
                sendOrderJztey.setOrderType(value.get(0).getDdlx());
                sendOrderJztey.setDocumentType(value.get(0).getDanjlx());
                sendOrderJztey.setSalesType(value.get(0).getXslx());
                sendOrderJztey.setDisType(value.get(0).getPslx());
                sendOrderJztey.setCostPrice(value.get(0).getDckhcbj());
                sendOrderJztey.setCallState(value.get(0).getZt());
                sendOrderJztey.setReturnCause(value.get(0).getYuany());
                sendOrderJztey.setReturnCategory(value.get(0).getThlb());
                sendOrderJztey.setMassState(value.get(0).getQualitystatename());
                sendOrderJztey.setSettlementPrice(value.get(0).getRealprice());
                sendOrderJztey.setMakeDate(value.get(0).getBaozhiqi());
                sendOrderJztey.setValidityDate(value.get(0).getSxrq());
                sendOrderJztey.setBoxNumber(value.get(0).getPxh());
                if(value.get(0).getYewtype().equals("10")){
                    sendOrderJztey.setIsConsign("Y");
                }
                master.setExtend(sendOrderJztey);

                value.forEach(outBound -> {
                    TiSendOrderDetail detail = new TiSendOrderDetail();
                    detail.setId(outBound.getDjsort());
                    detail.setProductCode(outBound.getSpid());
                    detail.setProductBatch(outBound.getPh());
                    detail.setProductNum(outBound.getShl());
                    detail.setProductBoxNum(outBound.getJiansh());
                    detail.setProductBulkNum(outBound.getLingsshl());
                    detail.setRemark(outBound.getKhbz());
                    detail.setProductUnitPrice(outBound.getDj());
                    detail.setProductTotalPrice(outBound.getHsje());
                    if (Optional.ofNullable(outBound.getSfrgsqph()).orElse("否").equals(Constants.CN_YES)) {
                        detail.setProductBatch(outBound.getHsje());
                        detail.setProductBatchAsk(outBound.getPhyq());
                    }
                    master.getDetails().add(detail);
                });
                masterList.add(master);
            });
            outBoundService.createOutBounds(masterList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "false";

        }
        return "true";
    }

    /**
     * 查询出库结果
     *
     * @param branch
     * @param store
     * @return
     */
    @RequestMapping("/query_outbound")
    public String queryOutBound(@RequestParam(name = "branchid") String branch, @RequestParam(name = "storeid") String store) {
        return JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(queryOutBoundConsign(branch, store, "N"));
    }

    /**
     * 查询出库结果
     *
     * @param branch
     * @param store
     * @return
     */
    @RequestMapping("/query_outbound_consign")
    public String queryOutBoundConsign(@RequestParam(name = "branchid") String branch, @RequestParam(name = "storeid") String store) {
        return JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(queryOutBoundConsign(branch, store, "Y"));
    }

    public ResponseWrapper queryOutBoundConsign(String branch, String store, String type) {
        HashMap<String, List<String>> updateParams = Maps.newHashMap();
        HashMap<String, String> params = Maps.newHashMap();
        params.put("branch", branch);
        params.put("store", store);
        params.put("type", type);

        List<HashMap<String, String>> results = outBoundService.queryOutBounds(params);
        ResponseWrapper<ResponseOutBound> wrapper = new ResponseWrapper();
        results.forEach(result -> {
            try {
                ResponseOutBound responseOutBound = new ResponseOutBound();
                MapUtil.toObject(responseOutBound, result, true);
                Optional.ofNullable(updateParams.get(store)).map(map -> map.add(responseOutBound.getOrderNumber())).orElseGet(() -> {
                    updateParams.put(store, Lists.newArrayList(responseOutBound.getOrderNumber()));
                    return true;
                });
                wrapper.getList().add(responseOutBound);
            } catch (IllegalAccessException e) {
                return;
            } catch (InvocationTargetException e) {
                return;
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
        outBoundService.updateOutBoundsBack(updateParams);
        return wrapper;
    }

    /**
     * 查询库存数据
     *
     * @param branch
     * @param store
     * @return
     */
    @RequestMapping("/query_store")
    public String queryStoreInfo(@RequestParam(name = "branchid") String branch, @RequestParam(name = "storeid") String store) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("branch", branch);
        params.put("store", store);
        List<HashMap<String, String>> results = storeService.queryStoreInfo(params);
        ResponseWrapper<ResponseStoreInfo> wrapper = new ResponseWrapper();
        results.forEach(result -> {
            try {
                ResponseStoreInfo responseStoreInfo = new ResponseStoreInfo();
                MapUtil.toObject(responseStoreInfo, result, true);
                wrapper.getList().add(responseStoreInfo);
            } catch (IllegalAccessException e) {
                return;
            } catch (InvocationTargetException e) {
                return;
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });

        return JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(wrapper);
    }

}
