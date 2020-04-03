package org.shrek.hadata.service.qimen.web;

import com.qimen.api.request.*;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.web.BasicResult;
import org.shrek.hadata.service.qimen.service.Qimen200Service;
import org.shrek.hadata.service.qimen.service.model.*;
import org.shrek.hadata.service.qimen.service.model.ReturnorderCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月28日 16:56
 */
@Controller
@RequestMapping("/2.0.0")
public class Qimen200Controller {

    @Autowired
    Qimen200Service qimen200Service;

    @ResponseBody
    @RequestMapping("/index")
    public BasicResult index() {
        return BasicResult.success("Qimen服务后台");
    }

    /**
     * 入库单创建接口
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/entryorder.create", produces = MediaType.APPLICATION_XML_VALUE)
    public EntryorderCreateResponse createEntryorder(@RequestBody String content) {
        EntryorderCreateRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, EntryorderCreateRequest.class);
        return qimen200Service.createEntryorder(wrapper);
    }


    /**
     * 退货入库创建
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/returnorder.create", produces = MediaType.APPLICATION_XML_VALUE)
    public ReturnorderCreateResponse createReturnOrder(@RequestBody String content) {
        ReturnorderCreateRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, ReturnorderCreateRequest.class);
        return qimen200Service.createReturnOrder(wrapper);
    }

    /**
     * 商品同步
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/singleitem.synchronize", produces = MediaType.APPLICATION_XML_VALUE)
    public SingleitemSynchronizeResponse singleitemSynchronize(@RequestBody String content) {
        SingleitemSynchronizeRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, SingleitemSynchronizeRequest.class);
        return qimen200Service.singleitemSynchronize(wrapper);
    }

    /**
     * 出库单创建
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/stockout.create", produces = MediaType.APPLICATION_XML_VALUE)
    public StockoutCreateResponse createStockout(@RequestBody String content) {
        StockoutCreateRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, StockoutCreateRequest.class);
        return qimen200Service.createStockout(wrapper);
    }

    /**
     * 发货单创建
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deliveryorder.create", produces = MediaType.APPLICATION_XML_VALUE)
    public DeliveryorderCreateResponse createDeliveryOrder(@RequestBody String content) {
        DeliveryorderCreateRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content.replace("\\n",""), DeliveryorderCreateRequest.class);
        return qimen200Service.createDeliveryOrder(wrapper);
    }

    /**
     * 单据取消
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/order.cancel", produces = MediaType.APPLICATION_XML_VALUE)
    public OrderCancelResponse cancelOrder(@RequestBody String content) {
        OrderCancelRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, OrderCancelRequest.class);
        return qimen200Service.cancelOrder(wrapper);
    }


    /**
     * 单据取消
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/inventory.query", produces = MediaType.APPLICATION_XML_VALUE)
    public com.qimen.api.response.InventoryQueryResponse inventoryQuery(@RequestBody String content) {
        InventoryQueryRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content.replace("\\n",""), InventoryQueryRequest.class);
        return qimen200Service.inventoryQuery(wrapper);
    }
}
