package org.shrek.hadata.service.reiley.model;

import org.junit.Test;
import org.shrek.hadata.commons.util.JacksonUtil;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月23日 16:14
 */
public class DeliveryOrderWrapperTest {

    static String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "\n" +
            "<request>\n" +
            "  <deliveryOrder>\n" +
            "    <deliveryOrderCode>18082000000133</deliveryOrderCode>\n" +
            "    <orderType>JYCK</orderType>\n" +
            "    <warehouseCode>REILEY</warehouseCode>\n" +
            "    <orderFlag/>\n" +
            "    <sourcePlatformCode>TB</sourcePlatformCode>\n" +
            "    <sourcePlatformName>淘宝</sourcePlatformName>\n" +
            "    <createTime>2018-08-20 11:38:03</createTime>\n" +
            "    <placeOrderTime>2018-08-20 11:38:03</placeOrderTime>\n" +
            "    <payTime>2018-08-20 11:38:09</payTime>\n" +
            "    <payNo>201212267021206988</payNo>\n" +
            "    <operateTime>2018-08-20 11:38:14</operateTime>\n" +
            "    <shopNick>启赋官方旗舰店</shopNick>\n" +
            "    <buyerNick>陈牧宇</buyerNick>\n" +
            "    <totalAmount>0.00</totalAmount>\n" +
            "    <itemAmount>0.00</itemAmount>\n" +
            "    <discountAmount>0.00</discountAmount>\n" +
            "    <freight>0.00</freight>\n" +
            "    <arAmount>0</arAmount>\n" +
            "    <gotAmount>0.00</gotAmount>\n" +
            "    <logisticsCode>EMS</logisticsCode>\n" +
            "    <logisticsName>EMS</logisticsName>\n" +
            "    <buyerMessage/>\n" +
            "    <expressCode/>\n" +
            "    <sellerMessage/>\n" +
            "    <remark/>\n" +
            "    <senderInfo>\n" +
            "      <name>张三</name>\n" +
            "      <zipCode/>\n" +
            "      <tel>13774387213</tel>\n" +
            "      <mobile>13774387213</mobile>\n" +
            "      <email/>\n" +
            "      <province>上海</province>\n" +
            "      <city>上海市</city>\n" +
            "      <area>浦东新区</area>\n" +
            "      <detailAddress>上海上海市浦东新区瑞和路888号</detailAddress>\n" +
            "    </senderInfo>\n" +
            "    <receiverInfo>\n" +
            "      <name>吕宇翔</name>\n" +
            "      <zipCode>537700</zipCode>\n" +
            "      <tel/>\n" +
            "      <mobile>13878075294</mobile>\n" +
            "      <email/>\n" +
            "      <province>广西壮族自治区</province>\n" +
            "      <city>玉林市</city>\n" +
            "      <area>陆川县</area>\n" +
            "      <detailAddress>温泉镇陆兴中路低街小区</detailAddress>\n" +
            "    </receiverInfo>\n" +
            "    <invoiceFlag>N</invoiceFlag>\n" +
            "    <invoices>\n" +
            "      <invoice>\n" +
            "        <type>222<type/>\n" +
            "        <header/>\n" +
            "        <amount>2944</amount>\n" +
            "        <content/>\n" +
            "      </invoice>\n" +
            "    </invoices>\n" +
            "  </deliveryOrder>\n" +
            "  <orderLines>\n" +
            "    <orderLine>\n" +
            "      <orderLineNo>15</orderLineNo>\n" +
            "      <sourceOrderCode>201212267021206988</sourceOrderCode>\n" +
            "      <subSourceOrderCode>201212267024206988</subSourceOrderCode>\n" +
            "      <ownerCode>REILEY</ownerCode>\n" +
            "      <itemCode>12320946</itemCode>\n" +
            "      <itemId/>\n" +
            "      <itemName>S26 爱儿乐妈妈 900 盒装（新）</itemName>\n" +
            "      <planQty>1</planQty>\n" +
            "      <actualPrice>0.00</actualPrice>\n" +
            "    </orderLine>\n" +
            "  </orderLines>\n" +
            "</request>\n";

    @Test
    public void test() {

        DeliveryOrderRequest wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(s, DeliveryOrderRequest.class);

        System.out.println(wrapper);
    }

}