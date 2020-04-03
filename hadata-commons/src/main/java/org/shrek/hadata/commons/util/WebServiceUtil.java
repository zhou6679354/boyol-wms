package org.shrek.hadata.commons.util;

import com.google.common.collect.Maps;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * SOAP方式调用Web service工具类
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年11月13日 09:06
 */
public class WebServiceUtil {
    String nameSpace = "";
    String wsdlUrl = "";
    String serviceName = "";
    String portName = "";
    String responseName = "";
    String elementName = "";
    int timeout = 20000;


    /**
     * @param nameSpace
     * @param wsdlUrl
     * @param serviceName
     * @param portName
     * @param element
     * @param responseName
     */
    public WebServiceUtil(String nameSpace, String wsdlUrl,
                          String serviceName, String portName, String element,
                          String responseName) {
        this.nameSpace = nameSpace;
        this.wsdlUrl = wsdlUrl;
        this.serviceName = serviceName;
        this.portName = portName;
        this.elementName = element;
        this.responseName = responseName;
    }


    /**
     * @param nameSpace
     * @param wsdlUrl
     * @param serviceName
     * @param portName
     * @param element
     * @param responseName
     * @param timeOut
     */
    public WebServiceUtil(String nameSpace, String wsdlUrl,
                          String serviceName, String portName, String element,
                          String responseName, int timeOut) {
        this.nameSpace = nameSpace;
        this.wsdlUrl = wsdlUrl;
        this.serviceName = serviceName;
        this.portName = portName;
        this.elementName = element;
        this.responseName = responseName;
        this.timeout = timeOut;
    }
    public String sendSoap11Message(HashMap<String, String> params) throws Exception {
        return sendSoapMessage(params,SOAPConstants.SOAP_1_1_PROTOCOL);
    }
    public String sendSoap12Message(HashMap<String, String> params) throws Exception {
        return sendSoapMessage(params,SOAPConstants.SOAP_1_2_PROTOCOL);
    }
    public String sendSoapMessage(HashMap<String, String> params,String soapProtocol) throws Exception {
        // 创建URL对象
        URL url = new URL(wsdlUrl);
        // 创建服务(Service)
        QName sname = new QName(nameSpace, serviceName);
        Service service = Service.create(url, sname);
        // 创建Dispatch对象
        Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName),
                SOAPMessage.class, Service.Mode.MESSAGE);
        // 创建SOAPMessage
        try {
            SOAPMessage msg = MessageFactory.newInstance(soapProtocol).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            // 创建SOAPHeader(不是必需)
//             SOAPHeader header = envelope.getHeader();
//             if (header == null)
//             header = envelope.addHeader();
//             QName hname = new QName(nameSpace, "username", "nn");
//             header.addHeaderElement(hname).setValue("huoyangege");

            // 创建SOAPBody
            SOAPBody body = envelope.getBody();
            QName ename = new QName(nameSpace, elementName, "ser");
            SOAPBodyElement ele = body.addBodyElement(ename);
            // 增加Body元素和值
            for (Map.Entry<String, String> entry : params.entrySet()) {
                ele.addChildElement(new QName(nameSpace, entry.getKey()))
                        .setValue(entry.getValue());
            }

            // 通过Dispatch传递消息,会返回响应消息
            SOAPMessage response = dispatch.invoke(msg);

            // 响应消息处理,将响应的消息转换为doc对象
            Document doc = response.getSOAPPart().getEnvelope().getBody()
                    .extractContentAsDocument();
            String resp = doc.getElementsByTagName(responseName).item(0)
                    .getTextContent();
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public static void main(String[] args) {
        WebServiceUtil ws = new WebServiceUtil("http://service.ws.boyol/",
                "http://127.0.0.1:8080/ws/gateway?WSDL",
                "GatewayService", "GatewayPort",
                "execute", "executeResponse");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("reqId", "2ac01f7c831d4b0eb1974f532fb76edc");
        params.put("reqMethod", "deliveryorder.create");
        params.put("reqFrom", "reliey_erp");
        params.put("reqDate", "2018-11-22 15:39:32");
        params.put("signType", "MD5");
//        params.put("sign", "66B8AFA73F9E0D3E8276D591670A2C1D");
        String content ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<request>\n" +
                "  <deliveryOrder>\n" +
                "    <deliveryOrderCode>XR9012203861459</deliveryOrderCode>\n" +
                "    <orderType>JYCK</orderType>\n" +
                "    <warehouseCode>REILEY_SZ_DS</warehouseCode>\n" +
                "    <orderFlag></orderFlag>\n" +
                "    <sourcePlatformCode>JD</sourcePlatformCode>\n" +
                "    <sourcePlatformName>京东</sourcePlatformName>\n" +
                "    <createTime>2019-01-22 00:14:01</createTime>\n" +
                "    <placeOrderTime>2019-01-22 00:09:02</placeOrderTime>\n" +
                "    <payTime>2019-01-22 00:09:20</payTime>\n" +
                "    <payNo>85731005421</payNo>\n" +
                "    <operateTime>2019-01-22 00:14:01</operateTime>\n" +
                "    <shopNick>京东旗舰店</shopNick>\n" +
                "    <buyerNick>jd_55c67aabbcac4</buyerNick>\n" +
                "    <totalAmount>1104.00</totalAmount>\n" +
                "    <itemAmount>1104.00</itemAmount>\n" +
                "    <discountAmount>0.00</discountAmount>\n" +
                "    <freight>0.00</freight>\n" +
                "    <arAmount>0</arAmount>\n" +
                "    <gotAmount>1104.00</gotAmount>\n" +
                "    <logisticsCode>YTO</logisticsCode>\n" +
                "    <logisticsName>圆通速递</logisticsName>\n" +
                "    <buyerMessage></buyerMessage>\n" +
                "    <expressCode></expressCode>\n" +
                "    <sellerMessage></sellerMessage>\n" +
                "    <remark></remark>\n" +
                "    <senderInfo>\n" +
                "      <name>惠氏京东旗舰店</name>\n" +
                "      <zipCode></zipCode>\n" +
                "      <tel>4007001826</tel>\n" +
                "      <mobile>4007001826</mobile>\n" +
                "      <email></email>\n" +
                "      <province>江苏省</province>\n" +
                "      <city>苏州市</city>\n" +
                "      <area>吴中区</area>\n" +
                "      <detailAddress>江苏省苏州市吴中区园区大新华物流园</detailAddress>\n" +
                "    </senderInfo>\n" +
                "    <receiverInfo>\n" +
                "      <name>叶秀丽</name>\n" +
                "      <zipCode></zipCode>\n" +
                "      <tel>13132288878</tel>\n" +
                "      <mobile>13132288878</mobile>\n" +
                "      <email></email>\n" +
                "      <province>海南省</province>\n" +
                "      <city>万宁市</city>\n" +
                "      <area></area>\n" +
                "      <detailAddress>兴隆华侨农场华侨农场石梅湾兴梅大道1号石梅山庄3期3区16栋2单元405</detailAddress>\n" +
                "    </receiverInfo>\n" +
                "    <invoiceFlag>N</invoiceFlag>\n" +
                "    <invoices>\n" +
                "      <invoice>\n" +
                "        <type></type>\n" +
                "        <header></header>\n" +
                "        <amount>1104</amount>\n" +
                "        <content></content>\n" +
                "      </invoice>\n" +
                "    </invoices>\n" +
                "    <extendProps>\n" +
                "      <totalPrice>1104.00</totalPrice>\n" +
                "      <sellerPrice></sellerPrice>\n" +
                "      <payment>1104</payment>\n" +
                "      <shop_code>008</shop_code>\n" +
                "      <promotions/>\n" +
                "      <is_split>0</is_split>\n" +
                "    </extendProps>\n" +
                "  </deliveryOrder>\n" +
                "  <orderLines>\n" +
                "    <orderLine>\n" +
                "      <orderLineNo>557973</orderLineNo>\n" +
                "      <sourceOrderCode>85731005421</sourceOrderCode>\n" +
                "      <subSourceOrderCode></subSourceOrderCode>\n" +
                "      <ownerCode></ownerCode>\n" +
                "      <itemCode>12281195</itemCode>\n" +
                "      <itemId></itemId>\n" +
                "      <itemName>惠氏启赋4阶段@6x900g （罐装）</itemName>\n" +
                "      <planQty>4</planQty>\n" +
                "      <retailPrice>276.00</retailPrice>\n" +
                "      <actualPrice>276.00</actualPrice>\n" +
                "      <extendProps>\n" +
                "        <sku_id>28441763082</sku_id>\n" +
                "        <jd_price>1104.00</jd_price>\n" +
                "      </extendProps>\n" +
                "    </orderLine>\n" +
                "  </orderLines>\n" +
                "  <extendProps>\n" +
                "    <SdId>11</SdId>\n" +
                "    <ReceiverCountry>1</ReceiverCountry>\n" +
                "    <ReceiverProvince>460000</ReceiverProvince>\n" +
                "    <ReceiverCity>469006</ReceiverCity>\n" +
                "    <ReceiverDistrict>0</ReceiverDistrict>\n" +
                "    <OrderMsg></OrderMsg>\n" +
                "    <StorageMessage></StorageMessage>\n" +
                "    <PayName>网银在线</PayName>\n" +
                "    <PayCode>chinabank</PayCode>\n" +
                "    <PayStatus>2</PayStatus>\n" +
                "    <ShippingTimeTzph>1548087241</ShippingTimeTzph>\n" +
                "    <ShippingTimeJh>0</ShippingTimeJh>\n" +
                "    <IsCod>0</IsCod>\n" +
                "    <CoopModel>SOP</CoopModel>\n" +
                "    <shipping_best_time>0</shipping_best_time>\n" +
                "    <cnService></cnService>\n" +
                "    <pushTime></pushTime>\n" +
                "    <esDate></esDate>\n" +
                "    <esRange></esRange>\n" +
                "    <osDate></osDate>\n" +
                "    <osRange></osRange>\n" +
                "    <store_code></store_code>\n" +
                "    <tmallDelivery></tmallDelivery>\n" +
                "    <threePlTiming></threePlTiming>\n" +
                "    <cutoffMinutes></cutoffMinutes>\n" +
                "    <esTime></esTime>\n" +
                "    <deliveryTime></deliveryTime>\n" +
                "    <collectTime></collectTime>\n" +
                "    <sendTime></sendTime>\n" +
                "    <signTime></signTime>\n" +
                "    <deliveryCps></deliveryCps>\n" +
                "    <gatherLastCenter></gatherLastCenter>\n" +
                "    <gatherStation></gatherStation>\n" +
                "  </extendProps>\n" +
                "</request>";
        params.put("content", content);
        try {
          String sign=  SignUtil.hadataSign(params,"5f8bcbc08c4246809a9198a2f73a28d1","MD5");
            params.put("sign", sign);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            String ret = ws.sendSoap11Message(params);
            System.out.println(ret.toString()); // 没有对结果做处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
