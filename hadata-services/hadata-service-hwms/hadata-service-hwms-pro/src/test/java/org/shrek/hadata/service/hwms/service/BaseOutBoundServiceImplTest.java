package org.shrek.hadata.service.hwms.service;

import com.google.common.collect.Maps;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.hwms.Application;
import org.shrek.hadata.service.hwms.mapper.TOrderDetailMapper;
import org.shrek.hadata.service.hwms.mapper.TOrderMapper;
import org.shrek.hadata.service.hwms.model.TOrder;
import org.shrek.hadata.service.hwms.model.TOrderConfirm;
import org.shrek.hadata.service.hwms.model.TOrderDetail;
import org.shrek.hadata.service.hwms.model.TOrderPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月20日 17:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class BaseOutBoundServiceImplTest {

    @Autowired
    TOrderMapper tOrderMapper;

    @Autowired
    TOrderDetailMapper tOrderDetailMapper;

    @Test
    public void queryConfirmOutBoundsByWhse() {
        String whse = "601";
        TOrder tOrder = new TOrder();
        tOrder.setWhId(whse);
        tOrder.setIsSendBack("1");
        RowBounds rowBounds=new  RowBounds(0,50);
        List<TOrder> orders = tOrderMapper.selectByRowBounds(tOrder,rowBounds);
        TOrderDetail tOrderDetail = new TOrderDetail();
        HashMap<String, String> queryMap = Maps.newHashMap();
        queryMap.put("whse", "601");
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setOrderNumber(order.getOrderNumber());
            List<TOrderDetail> details = tOrderDetailMapper.select(tOrderDetail);
            order.setOrderDetailList(details);
            queryMap.put("order_number",order.getOrderNumber());
            List<TOrderConfirm> orderConfirmList=  tOrderMapper.selectOrderConfirm(queryMap);
            order.setOrderConfirmList (orderConfirmList);
            List<TOrderPackage> orderPackageList=  tOrderMapper.selectOrderPackage(queryMap);
            order.setOrderPackageList(orderPackageList);
        });

        System.out.println(orders);
    }
}