package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.RowBounds;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.hwms.mapper.*;
import org.shrek.hadata.service.hwms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 07:05
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseOutBoundServiceImpl implements OutBoundService<String> {

    @Autowired
    TOrderMapper tOrderMapper;

    @Autowired
    TOrderDetailMapper tOrderDetailMapper;

    @Autowired
    TItemUomMapper tItemUomMapper;

    @Autowired
    TCustomerMapper tCustomerMapper;

    @Autowired
    TblCmRequestMapper tblCmRequestMapper;
    @Autowired
    TExpressMapper tExpressMapper;
    @Autowired
    TPoDetailMapper tPoDetailMapper;

    @Override
    public List<TPoDetail> queryOutBoundsByWhse(String whse) {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("whse",whse);
        List<TPoDetail> tPoDetails = tPoDetailMapper.selectOutBoundsByOutB2C(param);
        return tPoDetails;
    }
    @Override
    public List<TPoDetail> queryOutBoundsByWhse2(String whse) {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("whse",whse);
        List<TPoDetail> tPoDetails = tPoDetailMapper.selectOutBoundsByOutB2CT(param);
        return tPoDetails;
    }
    @Override
    public List<TOrder> querySubscribeOutBounds(String whse,String client) {
        Example example = new Example(TOrder.class);
        example.createCriteria()
                .andEqualTo("whId",whse)
                .andEqualTo("clientCode",client)
                .andEqualTo("isSendBack","1")
                .andEqualTo("status","S")
                .andEqualTo("shipToResidentialFlag","N")
                .andIsNotNull("carrierNumber")
                .andLessThan("actualDeliveryDate","2019-01-01");
        List<TOrder> orders = tOrderMapper.selectByExample(example);
        return orders;
    }
    @Override
    public int updateOutBoundsBackByOrderNo(String whse,String OrderNumber) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setOrderNumber(OrderNumber);
        tOrder= tOrderMapper.selectOne(tOrder);
        tOrder.setIsSendBack("2");
        tOrder.setEarliestDeliveryDate(new Date());
        return tOrderMapper.updateByPrimaryKeySelective(tOrder);
    }
    @Override
    public int updateOutBoundsSubByOrderNo(String whse,String OrderNumber) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setOrderNumber(OrderNumber);
        tOrder= tOrderMapper.selectOne(tOrder);
        tOrder.setShipToResidentialFlag("Y");
        tOrder.setEarliestDeliveryDate(new Date());
        return tOrderMapper.updateByPrimaryKeySelective(tOrder);
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
            orderId = tOrder.getOrderId();
            TOrder tOrderUpdate = new TOrder();
            tOrderUpdate.setOrderId(orderId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                TExpress ex1 = express.get(0);
                String srcTime = ex1.getAcceptTime();
                tOrderUpdate.setActualDeliveryDate(sdf.parse(srcTime.substring(0,10) + ' ' + srcTime.substring(10,18)));
            }
            catch (Exception e){
                tOrderUpdate.setActualDeliveryDate(new Date());
            }
            tOrderMapper.updateByPrimaryKeySelective(tOrderUpdate);
            Example example = new Example(TExpress.class);
            example.createCriteria().andEqualTo("shipperCode",shipperCode).andEqualTo("logisticCode",logisticCode);
            tExpressMapper.deleteByExample(example);

            for(int i=0;i<express.size();i++){
                TExpress tExpress = express.get(i);
                tExpress.setSequenceId(i+1);
                tExpress.setOrderId(orderId);
                String srcTime = tExpress.getAcceptTime();
                tExpress.setAcceptTime(srcTime.substring(0,10) + ' ' + srcTime.substring(10,18));

                tExpressMapper.insertSelective(tExpress);
            }

        }
        return BaseResponse.success("");
    }
    @Override
    public List<TOrder> queryConfirmOutBoundsByWhse(String whse) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
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
            List<TOrderPackage> orderPackageList = tOrderMapper.selectOrderPackage(queryMap);
            orderPackageList.forEach(tOrderPackage -> {
                queryMap.put("carton_number", tOrderPackage.getCartonNumber());
                List<TOrderPackageItem> orderPackageItems = tOrderMapper.selectOrderPackageItem(queryMap);
                tOrderPackage.setItems(orderPackageItems);
            });
            order.setOrderPackageList(orderPackageList);
        });
        return orders;
    }

    @Override
    @Transactional
    public BaseResponse createOutBoundOrder(TOrder order) {
        TOrder example = new TOrder();
        example.setOrderNumber(order.getOrderNumber());
        example.setWhId(order.getWhId());
        int count = tOrderMapper.selectCount(example);
        if (count > 0) {
            return BaseResponse.fail("订单已经存在不允许重复推送!");
        } else {
            tOrderMapper.insertSelective(order);
            order.getOrderDetailList().forEach(detail -> {
                tOrderDetailMapper.insertSelective(detail);
            });
            return BaseResponse.success(order);
        }
    }

    @Override
    public BaseResponse cancelOutBoundOrder(String orderCode, String whid) {

        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(orderCode);
        tOrder.setWhId(whid);
        tOrder = tOrderMapper.selectOne(tOrder);
        if (Optional.ofNullable(tOrder).isPresent()) {
            Map<String, Object> paramterMap = Maps.newHashMap();
            paramterMap.put("order_id", tOrder.getOrderId());
            paramterMap.put("remag", "");
            tOrderMapper.cancelOutBoundOrder(paramterMap);
            String remag = (String) paramterMap.get("remag");
            if (remag.startsWith("S")) {
                return BaseResponse.success();
            } else {
                return BaseResponse.fail(String.valueOf(paramterMap.get(remag)));
            }
        } else {
            return BaseResponse.fail("查询不到对应订单!");
        }
    }
    @Override
    public String queryConfirmOutBoundsByCusAndWhse(Integer customerId,String whse){
        TCustomer tCustomer = new TCustomer();
        tCustomer.setCustomerId(customerId);
        tCustomer.setWhId(whse);
        return tCustomerMapper.selectOne(tCustomer).getCustomerCode();
    }

    @Override
    public boolean createOutBounds(List<TiSendOrderMaster<String>> tiSendOrderMasters) {
        return false;
    }

    @Override
    public List<HashMap<String, String>> queryOutBounds(HashMap<String, String> params) {
        return null;
    }

    @Override
    public List<String> queryTopOutBounds(HashMap<String, String> params) {
        return tOrderMapper.selectTopOrderByStatus(params);
    }
    @Override
    public List<TblCmRequest> queryConfirmTblcmByWhseAndClient() {
        List<TblCmRequest> orderConfirmList = tblCmRequestMapper.selectTblCmOrder();
        return orderConfirmList;
    }

    @Override
    public int updateOutBoundsBack(HashMap<String, List<String>> params) {
        TOrder tOrder = new TOrder();
        tOrder.setIsSendBack("1");
        params.keySet().forEach(key -> {
            Example example = new Example(TOrder.class);
            example.createCriteria().andIn("orderNumber", params.get(key));
            tOrderMapper.updateByExampleSelective(tOrder, example);

        });
        return 1;
    }

    @Override
    public int updateOutBoundsBack(Integer id) {
        TOrder tOrder = new TOrder();
        tOrder.setOrderId(id);
        tOrder.setIsSendBack("1");
        return tOrderMapper.updateByPrimaryKeySelective(tOrder);
    }

    @Override
    public TOrder queryOutBounds(String orderNumber, String whse) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setOrderNumber(orderNumber);
        tOrder= tOrderMapper.selectOne(tOrder);
        TOrderDetail tOrderDetail = new TOrderDetail();
        tOrderDetail.setWhId(whse);
        tOrderDetail.setOrderNumber(tOrder.getOrderNumber());
        List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
        tOrder.setOrderDetailList(details);

        HashMap<String, String> queryMap = Maps.newHashMap();
        queryMap.put("whse", "601");
        queryMap.put("order_number", tOrder.getOrderNumber());
        List<TOrderConfirm> orderConfirmList = tOrderMapper.selectOrderConfirm(queryMap);
        tOrder.setOrderConfirmList(orderConfirmList);
        List<TOrderPackage> orderPackageList = tOrderMapper.selectOrderPackage(queryMap);
        orderPackageList.forEach(tOrderPackage -> {
            queryMap.put("carton_number", tOrderPackage.getCartonNumber());
            List<TOrderPackageItem> orderPackageItems = tOrderMapper.selectOrderPackageItem(queryMap);
            tOrderPackage.setItems(orderPackageItems);
        });
        tOrder.setOrderPackageList(orderPackageList);
        return tOrder;
    }

    @Override
    public BaseResponse<String> cancelOutBoundOrder(String orderCode, String clientCode, String whId) {
        TOrder tOrder = new TOrder();
        tOrder.setOrderNumber(orderCode);
        tOrder.setClientCode(clientCode);
        tOrder.setWhId(whId);
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
    public TOrderDetail queryConfirmOutBoundsByWhseAndNoAndId(String lineNumber,String orderNumber,String whse){
        TOrderDetail tOrderDetail = new TOrderDetail();
        tOrderDetail.setOrderNumber(orderNumber);
        tOrderDetail.setLineNumber(lineNumber);
        tOrderDetail.setWhId(whse);
        return tOrderDetailMapper.selectOne(tOrderDetail);
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
    public String queryConfirmOutBoundsByItemAndWhse(String lineNumber,String whse){
        TItemUom tItemUom = new TItemUom();
        tItemUom.setItemNumber(lineNumber);
        tItemUom.setWhId(whse);
        tItemUom.setUom("EA");
        return tItemUomMapper.selectOne(tItemUom).getClassId();
    }

    @Override
    public List<TOrder> queryConfirmOutBoundsByWhseAndClientAndType(String whse, String client,int type) {
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        if(!"601".equals(whse)) {
            tOrder.setClientCode(client);
        }
        tOrder.setIsSendBack("0");
        tOrder.setStatus("S");
        RowBounds rowBounds = new RowBounds(0,50);
        //List<TOrder> orders = tOrderMapper.select(tOrder);
        List<TOrder> orders = tOrderMapper.selectByRowBounds(tOrder,rowBounds);
        TOrderDetail tOrderDetail = new TOrderDetail();
        HashMap<String, String> queryMap = Maps.newHashMap();
        queryMap.put("whse", whse);
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            tOrderDetail.setCancelFlag("N");
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
            queryMap.put("order_number", order.getOrderNumber());
            List<TOrderConfirm> orderConfirmList = tOrderMapper.selectOrderConfirm(queryMap);

            order.setOrderConfirmList(orderConfirmList);
            List<TOrderPackage> orderPackageList = tOrderMapper.selectOrderPackage(queryMap);
            orderPackageList.forEach(tOrderPackage -> {
                queryMap.put("carton_number", tOrderPackage.getCartonNumber());
                List<TOrderPackageItem> orderPackageItems = tOrderMapper.selectOrderPackageItem(queryMap);
                tOrderPackage.setItems(orderPackageItems);
            });
            order.setOrderPackageList(orderPackageList);
        });
        return orders;
    }
    @Override
    public int updateOutOrderCancel(TOrder tocOrder){
        return tOrderMapper.updateOutOrderCancel(tocOrder);
    }
    @Override
    public TOrder queryTOrderByWhIdAndOrderNumber(String whId, String orderNumber) {
        TOrder tOrder=new TOrder();
        tOrder.setWhId(whId);
        tOrder.setOrderNumber(orderNumber);
        return tOrderMapper.selectOne(tOrder);
    }

}
