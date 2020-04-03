package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.RowBounds;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.iwms.mapper.*;
import org.shrek.hadata.service.iwms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出库订单
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 10:56
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseOutBoundServiceImpl implements OutBoundService {

    @Autowired
    TOrderMapper tOrderMapper;

    @Autowired
    TOrderDetailMapper tOrderDetailMapper;
    @Autowired
    TblFinOrderAcceptNoticeMapper tblFinOrderAcceptNoticeMapper;
    @Autowired
    TblCmRequestMapper tblCmRequestMapper;
    @Autowired
    TCustomerMapper tCustomerMapper;
    @Autowired
    TItemUomMapper tItemUomMapper;
    @Autowired
    TRcptShipMapper tRcptShipMapper;
    @Autowired
    TRcptShipPoMapper tRcptShipPoMapper;
    @Autowired
    TPickDetailMapper tPickDetailMapper;
    @Autowired
    TStoredItemMapper tStoredItemMapper;
    @Autowired
    StorageStockMapper storageStockMapper;
    @Override
    public List<TOrder> queryConfirmOutBoundsByWhse(String whse) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setIsSendBack("0");
        tOrder.setStatus("S");
        RowBounds rowBounds=new  RowBounds(0,5);
        List<TOrder> orders = tOrderMapper.selectByRowBounds(tOrder,rowBounds);
        TOrderDetail tOrderDetail = new TOrderDetail();
        HashMap<String, String> queryMap = Maps.newHashMap();
        queryMap.put("whse", whse);
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
            queryMap.put("order_number", order.getOrderNumber());
            List<TOrderConfirm> orderConfirmList = tOrderMapper.selectOrderConfirm(queryMap);
            order.setOrderConfirmList(orderConfirmList);
        });
        return orders;
    }

    @Override
    public List<TOrder> queryConfirmOutBoundsByWhseAndClient(String whse,String client) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setClientCode(client);
        tOrder.setIsSendBack("0");
        tOrder.setStatus("S");
        List<TOrder> orders = tOrderMapper.select(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
        });
        return orders;
    }
    @Override
    public List<TOrder> queryOrderForTMS(String whse) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setStatus("S");
        List<TOrder> orders = tOrderMapper.selectOrderForTMS(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
        });
        return orders;
    }
    @Override
    public List<TOrder> queryConfirmOutBoundsByWhseAndClientAndTypeId(String whse, String client, Integer typeId) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setClientCode(client);
        tOrder.setIsSendBack("0");
        tOrder.setStatus("S");
        tOrder.setTypeId(typeId);
        List<TOrder> orders = tOrderMapper.select(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
        });
        return orders;
    }

    @Override
    public TOrderDetail queryConfirmOutBoundsByWhseAndNoAndId(String lineNumber,String orderNumber,String whse){
        TOrderDetail tOrderDetail = new TOrderDetail();
        tOrderDetail.setOrderNumber(orderNumber);
        tOrderDetail.setPoLineNumber(lineNumber);
        tOrderDetail.setWhId(whse);
        return tOrderDetailMapper.select(tOrderDetail).get(0);
    }

    @Override
    public TOrderDetail queryConfirmOutBoundsByWhseAndLineNoAndId(String lineNumber,String orderNumber,String whse){
        TOrderDetail tOrderDetail = new TOrderDetail();
        tOrderDetail.setOrderNumber(orderNumber);
        tOrderDetail.setLineNumber(lineNumber);
        tOrderDetail.setWhId(whse);
        return tOrderDetailMapper.selectOne(tOrderDetail);
    }


    @Override
    public String queryConfirmOutBoundsByItemAndWhse(String itemNumber,String whse){
        TItemUom tItemUom = new TItemUom();
        tItemUom.setItemNumber(itemNumber);
        tItemUom.setWhId(whse);
        tItemUom.setUom("EA");
        return tItemUomMapper.selectOne(tItemUom).getClassId();
    }

    @Override
    public String queryConfirmOutBoundsByCusAndWhse(Integer customerId,String whse){
        TCustomer tCustomer = new TCustomer();
        tCustomer.setCustomerId(customerId);
        tCustomer.setWhId(whse);
        return tCustomerMapper.selectOne(tCustomer).getCustomerCode();
    }

    @Override
    public List<TOrder> queryConfirmGoodsReceiveByWhseAndClient(String whse, String client) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setLoadSeq(1);
        tOrder.setClientCode(client);
        List<TOrder> orders = tOrderMapper.select(tOrder);
        return orders;
    }

    @Override
    public List<TblCmRequest> queryConfirmTblcmByWhseAndClient() {
        List<TblCmRequest> orderConfirmList = tblCmRequestMapper.selectTblCmOrder();
        return orderConfirmList;
    }

    @Override
    public List<TOrder> queryConfirmOutBoundsByWhseAndClientAndType(String whse, String client, int type) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setClientCode(client);
        tOrder.setIsSendBack("0");
        tOrder.setStatus("S");
        List<TOrder> orders = tOrderMapper.select(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        HashMap<String, String> queryMap = Maps.newHashMap();
        queryMap.put("whse", whse);
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
            queryMap.put("order_number", order.getOrderNumber());
            List<TOrderConfirm> orderConfirmList = tOrderMapper.selectOrderConfirm(queryMap);

            order.setOrderConfirmList(orderConfirmList);
        });
        return orders;
    }

    @Override
    public List<TOrder> queryMassiveOutbounds(String whse, String clientCode) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setClientCode(clientCode);
        tOrder.setStatus("N");
        tOrder.setMassiveControl("W");
        List<TOrder> orders = tOrderMapper.select(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
        });
        return orders;
    }

    @Override
    public int updateMassiveOutboundsStatus(String clientCode,String whse,String orderCode,String status) {
        Example example = new Example(TOrder.class);
        if (status=="I") {
            example.createCriteria()
                    .andEqualTo("whId", whse)
                    .andEqualTo("clientCode", clientCode)
                    .andEqualTo("orderNumber", orderCode)
                    .andEqualTo("massiveControl", "W");
        }
        else {
            example.createCriteria()
                    .andEqualTo("whId", whse)
                    .andEqualTo("clientCode", clientCode)
                    .andEqualTo("orderNumber", orderCode);
        }

        TOrder tOrder = new TOrder();
        tOrder.setMassiveControl(status);
        return tOrderMapper.updateByExampleSelective(tOrder,example);
    }

    @Override
    public void createFinOrderAcceptNotice(String clientCode,String whse,String orderCode,String status) {
        /*Example example = new Example(TOrder.class);
        if (status=="I") {
            example.createCriteria()
                    .andEqualTo("whId", whse)
                    .andEqualTo("clientCode", clientCode)
                    .andEqualTo("orderNumber", orderCode)
                    .andEqualTo("massiveControl", "W");
        }
        else {
            example.createCriteria()
                    .andEqualTo("whId", whse)
                    .andEqualTo("clientCode", clientCode)
                    .andEqualTo("orderNumber", orderCode);
        }

        TOrder tOrder = new TOrder();
        tOrder.setMassiveControl(status);
        return tOrderMapper.updateByExampleSelective(tOrder,example);*/
        TblFinOrderAcceptNotice tblFinOrderAcceptNotice = new TblFinOrderAcceptNotice();
        tblFinOrderAcceptNotice.setAcceptStatus(status);
        tblFinOrderAcceptNotice.setClientCode(clientCode);
        tblFinOrderAcceptNotice.setOrderNumber(orderCode);
        tblFinOrderAcceptNotice.setWhse(whse);

        tblFinOrderAcceptNoticeMapper.insertSelective(tblFinOrderAcceptNotice);

    }


    @Override
    public int updateOutBoundsBack(Integer id) {
        TOrder tOrder = new TOrder();
        tOrder.setOrderId(id);
        tOrder.setIsSendBack("1");
        return tOrderMapper.updateByPrimaryKeySelective(tOrder);
    }
    @Override
    public int updateOutBoundsToTmsStatus(Integer id) {
        TOrder tOrder = new TOrder();
        tOrder.setOrderId(id);
        tOrder.setTmsSynStatus(1);
        return tOrderMapper.updateByPrimaryKeySelective(tOrder);
    }
    @Override
    public int updateOutBoundsGoodsBack(Integer id) {
        TOrder tOrder = new TOrder();
        tOrder.setOrderId(id);
        tOrder.setLoadSeq(2);
        return tOrderMapper.updateByPrimaryKeySelective(tOrder);
    }

    @Override
    public List<TOrder> queryBeforeDayConfirmOutBoundsByWhseAnd(String whse, String client) {
        Example example = new Example(TOrder.class);
        example.createCriteria()
                .andEqualTo("whId", whse)
                .andEqualTo("clientCode", client)
                .andBetween("actualShipDate", DateUtil.subDays(1), DateUtil.subDays(0));
        List<TOrder> orders = tOrderMapper.selectByExample(example);
        TOrderDetail tOrderDetail = new TOrderDetail();
        HashMap<String, String> queryMap = Maps.newHashMap();
        queryMap.put("whse", whse);
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
            queryMap.put("order_number", order.getOrderNumber());
            List<TOrderConfirm> orderConfirmList = tOrderMapper.selectOrderConfirm(queryMap);
            order.setOrderConfirmList(orderConfirmList);
        });
        return orders;
    }

    @Override
    public TRcptShipPo queryInfoByWhseAndPoNumber(String whse, String poNumber) {
        TRcptShipPo tRcptShipPo=new TRcptShipPo();
        tRcptShipPo.setPoNumber(poNumber);
        tRcptShipPo.setWhId(whse);
        return tRcptShipPoMapper.selectOne(tRcptShipPo);
    }
    @Override
    public TRcptShip queryInfoByWhseAndShipmentNumber(String whse, String shipmentNumber) {
        TRcptShip tRcptShip=new TRcptShip();
        tRcptShip.setShipmentNumber(shipmentNumber);
        tRcptShip.setWhId(whse);
        return tRcptShipMapper.selectOne(tRcptShip);
    }

    @Override
    @Transactional
    public BaseResponse createOutBoundOrder(TOrder order) {
        TOrder example = new TOrder();
        example.setOrderNumber(order.getOrderNumber());
        example.setClientCode(order.getClientCode());
        example.setWhId(order.getWhId());
        int count = tOrderMapper.selectCount(example);
        if (count > 0) {
            return BaseResponse.success("订单已经存在不允许重复推!");
        } else {
            tOrderMapper.insertSelective(order);
            order.getOrderDetailList().forEach(detail -> {
                tOrderDetailMapper.insertSelective(detail);
            });
            return BaseResponse.success();
        }
    }
    @Override
    @Transactional
    public BaseResponse createOrUpdateOutBoundOrder(TOrder order) {
        TOrder example = new TOrder();
        example.setOrderNumber(order.getOrderNumber());
        example.setClientCode(order.getClientCode());
        example.setWhId(order.getWhId());
        TOrder count = tOrderMapper.selectOne(example);
        if (count !=null) {
            order.setOrderId(count.getOrderId());
            order.setStatus(count.getStatus());
            tOrderMapper.updateByPrimaryKeySelective(order);
            for(int i=0;i<order.getOrderDetailList().size();i++){
                TOrderDetail exampleDetail = new TOrderDetail();
                exampleDetail.setOrderNumber(order.getOrderDetailList().get(i).getOrderNumber());
                exampleDetail.setLineNumber(order.getOrderDetailList().get(i).getLineNumber());
                exampleDetail.setWhId(order.getOrderDetailList().get(i).getWhId());
                TOrderDetail countDetail = tOrderDetailMapper.selectOne(exampleDetail);
                order.getOrderDetailList().get(i).setOrderDetailId(countDetail.getOrderDetailId());
                tOrderDetailMapper.updateByPrimaryKeySelective(order.getOrderDetailList().get(i));
            }
            return BaseResponse.success("订单更新成功!");
        } else {
            tOrderMapper.insertSelective(order);
            order.getOrderDetailList().forEach(detail -> {
                tOrderDetailMapper.insertSelective(detail);
            });
            return BaseResponse.success();
        }
    }
    @Override
    public BaseResponse cancelOutBoundOrder(String orderCode,String client, String whid) {

        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(orderCode);
        tOrder.setClientCode(client);
        tOrder.setWhId(whid);
        tOrder = tOrderMapper.selectOne(tOrder);
        if (Optional.ofNullable(tOrder).isPresent()) {
            Map<String, Object> paramterMap = Maps.newHashMap();
            paramterMap.put("order_id", tOrder.getOrderId());
            paramterMap.put("employee_id", "hadata");
            paramterMap.put("remag", "");
            tOrderMapper.cancelOutBoundOrder(paramterMap);
            String remag = (String) paramterMap.get("remag");
            if (remag.startsWith("S")) {
                return BaseResponse.success();
            } else {
                return BaseResponse.fail(remag);
            }
        } else {
            return BaseResponse.success("查询不到对应订单!");
        }
    }
    @Override
    public int updateOutOrderCancel(TOrder order){
        return tOrderMapper.updateOutOrderCancel(order);
    }
    @Override
    public int updateOutOrderSendBack(String whId, String orderNumber){
        TOrder order=new TOrder();
        order.setOrderNumber(orderNumber);
        order.setWhId(whId);
        order.setIsSendBack("1");
        return tOrderMapper.updateOutOrderCancel(order);
    }

    @Override
    public List<StorageStock> queryStorageStockByWhId(String whId) {
        TOrder order=new TOrder();
        order.setWhId(whId);
        return storageStockMapper.queryStorageStockByWhId(order);
    }

    @Override
    public List<TblOrderSynWisdom> queryTblOrderSynWisdomByWhId(String whId) {
        TblOrderSynWisdom tblOrderSynWisdom=new TblOrderSynWisdom();
        tblOrderSynWisdom.setSynFlag(0);
        tblOrderSynWisdom.setWhId(whId);
        return storageStockMapper.queryTblOrderSynWisdomByWhId(tblOrderSynWisdom);
    }

    @Override
    @Transactional
    public BaseResponse updateExpressInfo (String jsonList ,String shipperCode,String logisticCode) {
        List<TExpress> express = JacksonUtil.nonEmpty().fromJson(jsonList,List.class,TExpress.class);
        Integer orderId = 0;
        TOrder tOrder = new TOrder();
        tOrder.setCarrierScac(shipperCode);
        tOrder.setCarrierNumber(logisticCode);
        tOrder = tOrderMapper.selectOne(tOrder);
        if(tOrder != null && tOrder.getActualDeliveryDate().before(new Date(2019,1,1))){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                tOrder.setActualDeliveryDate(sdf.parse(express.get(0).getAcceptTime()));
            }
            catch (Exception e){
                tOrder.setActualDeliveryDate(new Date());
            }
            tOrderMapper.updateByPrimaryKey(tOrder);
            orderId = tOrder.getOrderId();


        }
        return BaseResponse.success("");
    }

    @Override
    public TOrder queryTOrderByWhIdAndOrderNumber(String whId, String orderNumber) {
        TOrder tOrder=new TOrder();
        tOrder.setWhId(whId);
        tOrder.setOrderNumber(orderNumber);
        return tOrderMapper.selectOne(tOrder);
    }

    @Override
    public List<TPickDetail> queryTPickDetailByWhId(String whId) {
        TPickDetail pickDetail=new TPickDetail();
        pickDetail.setWhId(whId);
        return tPickDetailMapper.select(pickDetail);
    }
    @Override
    public int updateTPickDetailByOrderNumberAndWhId(String orderNumber,String whId) {
        TPickDetail pickDetail=new TPickDetail();
        pickDetail.setWhId(whId);
        pickDetail.setOrderNumber(orderNumber);
        pickDetail.setLoadId("1");
        return tPickDetailMapper.updateTPickDetailByOrderNumberAndWhId(pickDetail);
    }

    @Override
    public int updateTblOrderSynWisdom(String orderNumber,String whId) {
        TblOrderSynWisdom tblOrderSynWisdom=new TblOrderSynWisdom();
        tblOrderSynWisdom.setSynFlag(1);
        tblOrderSynWisdom.setOrderNumber(orderNumber);
        tblOrderSynWisdom.setWhId(whId);
        return storageStockMapper.updateTblOrderSynWisdom(tblOrderSynWisdom);
    }

    @Override
    public int updateTblItemSynWisdom(String itemNumber, String whId) {
        TblItemSynWisdom tblItemSynWisdom=new TblItemSynWisdom();
        tblItemSynWisdom.setSynFlag(1);
        tblItemSynWisdom.setItemNumber(itemNumber);
        tblItemSynWisdom.setWhId(whId);
        return storageStockMapper.updateTblItemSynWisdom(tblItemSynWisdom);
    }

    @Override
    public List<TOrderDetail> queryTOrderDetailByWhIdAndOrderNumber(String whId, String orderNumber) {
        TOrderDetail orderDetail=new TOrderDetail();
        orderDetail.setOrderNumber(orderNumber);
        orderDetail.setWhId(whId);
        return tOrderDetailMapper.select(orderDetail);
    }

    @Override
    public String getOrderSerialNumber(String Whse) {
        String serialNumber = "";
        Map<String, Object> paramterMap = Maps.newHashMap();
        paramterMap.put("in_vchType", "ORDER SERIAL NUMBER");
        paramterMap.put("in_description", "Order Serial Number");
        paramterMap.put("out_nUID", "");
        paramterMap.put("out_nErrorNumber", "");
        paramterMap.put("out_vchLogMsg", "");
        tOrderMapper.getOrderSerialNumber(paramterMap);
        serialNumber =(String) paramterMap.get("out_nUID");
        java.util.Date d=new java.util.Date();
        java.text.SimpleDateFormat simpleDateFormat=new java.text.SimpleDateFormat("yyyyMMdd");
        String dateStr = simpleDateFormat.format(d).substring(2,7);
        return "SO" + Whse + dateStr + serialNumber;
    }

    @Override
    public List<TOrder> queryOutboundsToIWMS(String whse, String clientCode) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setClientCode(clientCode);
        tOrder.setStatus("N");
        tOrder.setLoadSeq(0);
        List<TOrder> orders = tOrderMapper.select(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
        });
        return orders;
    }

    @Override
    public List<String> queryOrderNumber(String whse,String clientCode) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setClientCode(clientCode);
        return tOrderMapper.queryOrderNumber(tOrder);
    }

    @Override
    public void confirmOutbound(String whse,String poNumber,String lineNumber,Long qty) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setOrderNumber(poNumber);

        tOrder = tOrderMapper.selectOne(tOrder);
        if(tOrder != null) {
            tOrder.setStatus("S");
            tOrder.setActualShipDate(new Date());
            tOrderMapper.updateByPrimaryKeySelective(tOrder);
        }

        TOrderDetail tOrderDetail = new TOrderDetail();
        tOrderDetail.setWhId(whse);
        tOrderDetail.setOrderNumber(poNumber);
        tOrderDetail.setLineNumber(lineNumber);

        tOrderDetail = tOrderDetailMapper.selectOne(tOrderDetail);
        if(tOrderDetail != null) {
            if (tOrderDetail.getQtyShipped() == 0) {
                tOrderDetail.setQtyShipped(qty.doubleValue());
                tOrderDetailMapper.updateByPrimaryKeySelective(tOrderDetail);
            }
        }
    }
}
