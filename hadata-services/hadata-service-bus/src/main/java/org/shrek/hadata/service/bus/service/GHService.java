package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.xml.XMLSerializer;
import org.shrek.hadata.service.bus.web.model.*;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.CustomerService;
import org.shrek.hadata.service.iwms.service.InBoundService;
import org.shrek.hadata.service.iwms.service.MaterielService;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Dispatch;
import java.net.URL;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author zhouwenheng
 * @version 1.0
 * @date 2019年04月15日 11:05
 */
@Slf4j
@Component
public class GHService {
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
    CustomerService customerService;

    /**
     * 出入库单创建
     */
    public void ghOrderCreated() {
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetPendingBillsResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("bygh");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            Calendar calendarBefor = Calendar.getInstance();
            calendarBefor.add(Calendar.DATE, -1); //得到前一天
            Date beforDay = calendarBefor.getTime();
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.add(Calendar.DATE, -0); //得到前一天
            Date nowDay = calendarNow.getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetPendingBills", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "sDate", "tem")).setValue(df.format(beforDay));
            clientInfo.addChildElement(new QName(nameSpace, "eDate", "tem")).setValue(df.format(nowDay));
            clientInfo.addChildElement(new QName(nameSpace, "billType", "tem")).setValue("0");
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if(resutStr.contains("Originator")){
                List<GHOrder> ghOrders = (List<GHOrder>) JSONArray.parseArray(resutStr, GHOrder.class);
                for (int i = 0; i < ghOrders.size(); i++) {
                    if (ghOrders.get(i).getBillType()==3||ghOrders.get(i).getBillType()==4||ghOrders.get(i).getBillType()==5||ghOrders.get(i).getBillType()==22||ghOrders.get(i).getBillType()==23) {
                        String vendorCode;
                        if(ghOrders.get(i).getTraderNo()==null){
                            vendorCode="GHGYS";
                        }else {
                            vendorCode = "SZGH" + ghOrders.get(i).getTraderNo();
                        }
                            TVendor vendor=customerService.getVendorByWhseAndCode(vendorCode);
                            if(vendor!=null){
                                TPoMaster poMaster = new TPoMaster();
                                List<TPoDetail> orderDetails=new ArrayList<TPoDetail>();
                                GHOrderDetail ghOrderDetail = ghPoOrderDeatilCreated(ghOrders.get(i).getBillNo());
                                GHOrderDetail.Bill ghBill=ghOrderDetail.getBill();
                                List<GHOrderDetail.BillDetail> ghBillDetails=ghOrderDetail.getBillDetail();
                                poMaster.setCreateDate(new Date());
                                poMaster.setPoNumber("gh"+ghOrders.get(i).getBillNo());
                                poMaster.setWhId("217");
                                poMaster.setStatus("O");
                                poMaster.setCarrierMode(ghBill.getDeliveryMethod());
                                if (ghOrders.get(i).getBillType() == 23) {
                                    poMaster.setTypeId(1121);
                                } else if (ghOrders.get(i).getBillType() == 5) {
                                    poMaster.setTypeId(1120);
                                } else {
                                    poMaster.setTypeId(1119);
                                }
                                poMaster.setClientCode("SZGH");
                                poMaster.setVendorCode(vendor.getVendorCode());
                                poMaster.setVendorName(vendor.getVendorName());
                                for(int y=0;y<ghBillDetails.size();y++){
                                    TPoDetail orderDetail=new TPoDetail();
                                    orderDetail.setPoNumber("gh"+ghBill.getBillNo());
                                    orderDetail.setWhId("217");
                                    orderDetail.setItemNumber("SZGH-"+ghBillDetails.get(y).getProductNo());
                                    orderDetail.setRemark(ghBillDetails.get(y).getProductName());
                                    orderDetail.setLineNumber(String.valueOf(y)+1);
                                    orderDetail.setQty(ghBillDetails.get(y).getQty());
                                    orderDetail.setLotNumber(ghBillDetails.get(y).getBatchNo());
                                    if(ghBillDetails.get(y).getProductionDate()!=null){
                                        orderDetail.setLatestShipDate(df.parse(ghBillDetails.get(y).getProductionDate()));
                                    }
                                    orderDetails.add(orderDetail);
                                }
                                poMaster.setDetailList(orderDetails);
                                try{
                                    inBoundService.createOrUpdateInBoundOrder(poMaster);
                                    log.info("广汇入库单入库成功，单号"+ghOrders.get(i).getBillNo());
                                }catch (Exception e){
                                    log.error("广汇入库单入库失败，单号"+ghOrders.get(i).getBillNo()+"异常原因：",e.getMessage());
                                }
                            }else{
                                log.error("广汇入库单入库失败，单号"+ghOrders.get(i).getBillNo()+"供应商不存在");
                            }

                    } else {
                            String customerCode;
                            if(ghOrders.get(i).getTraderNo()==null){
                                customerCode="GHGYS";
                            }else {
                                customerCode = "SZGH" + ghOrders.get(i).getTraderNo();
                            }
                            TCustomer customer=customerService.getCustomerByWhseAndCode("217",customerCode);
                            String address=ghOrders.get(i).getDeliveryAddress();
                            if(customer!=null){
                                TOrder order=new TOrder();
                                List<TOrderDetail> orderDetails=new ArrayList<TOrderDetail>();
                                GHOrderDetail ghOrderDetail = ghSoOrderDeatilCreated(ghOrders.get(i).getBillNo());
                                GHOrderDetail.Bill ghBill=ghOrderDetail.getBill();
                                List<GHOrderDetail.BillDetail> ghBillDetails=ghOrderDetail.getBillDetail();
                                String orderSerialNumber=outBoundService.getOrderSerialNumber("217");
                                order.setOrderSerialNumber(orderSerialNumber);
                                order.setOrderDate(ghOrders.get(i).getBillDate());
                                order.setCreateDate(new Date());
                                order.setOrderNumber("gh"+ghOrders.get(i).getBillNo());
                                order.setCustomerId(customer.getCustomerId());
                                order.setCustomerCode(customer.getCustomerCode());
                                order.setCustomerName(customer.getCustomerName());
                                if(address!=null&& !address.isEmpty()){
                                    String[] newAddress=address.split("\\s+");
                                    if(newAddress.length==1){
                                        order.setShipToAddr1(newAddress[0]);
                                        order.setShipToAddr2(newAddress[0]);
                                    }else if(newAddress.length==2){
                                        order.setShipToAddr1(newAddress[0]);
                                        order.setShipToAddr2(newAddress[0]);
                                        order.setShipToPhone(newAddress[1]);
                                    }else if(newAddress.length==3){
                                        order.setShipToAddr1(newAddress[0]);
                                        order.setShipToAddr2(newAddress[0]);
                                        order.setShipToPhone(newAddress[1]);
                                        order.setShipToName(newAddress[2]);
                                    }
                                }else{
                                    order.setShipToAddr1(customer.getCustomerAddr1());
                                    order.setShipToAddr2(customer.getCustomerAddr1());
                                    order.setShipToPhone(customer.getCustomerPhone());
                                    order.setShipToName(customer.getLinkman());
                                }
                                order.setWhId("217");
                                order.setStatus("N");
                                order.setClientCode("SZGH");
                                order.setCarrierMode(ghBill.getDeliveryMethod());
                                if (ghOrders.get(i).getBillType() == 230) {
                                    order.setTypeId(1155);
                                } else if (ghOrders.get(i).getBillType() == 7) {
                                    order.setTypeId(1154);
                                } else {
                                    order.setTypeId(1153);
                                }
                                for(int y=0;y<ghBillDetails.size();y++){
                                    TOrderDetail orderDetail=new TOrderDetail();
                                    orderDetail.setOrderNumber("gh"+ghBill.getBillNo());
                                    orderDetail.setWhId("217");
                                    orderDetail.setItemNumber("SZGH-"+ghBillDetails.get(y).getProductNo());
                                    orderDetail.setItemDescription(ghBillDetails.get(y).getProductName());
                                    orderDetail.setLineNumber(String.valueOf(y)+1);
                                    orderDetail.setQty(ghBillDetails.get(y).getQty());
                                    orderDetail.setAfoPlanQty(ghBillDetails.get(y).getQty());
                                    orderDetail.setLotNumber(ghBillDetails.get(y).getBatchNo());
                                    if(ghBillDetails.get(y).getProductionDate()!=null) {
                                        orderDetail.setProductionDate(df.parse(ghBillDetails.get(y).getProductionDate()));
                                    }
                                    orderDetails.add(orderDetail);
                                }
                                order.setOrderDetailList(orderDetails);
                                try{
                                    outBoundService.createOrUpdateOutBoundOrder(order);
                                }catch (Exception e){
                                    log.error("广汇出库单入库失败，单号"+ghOrders.get(i).getBillNo()+"异常原因：",e.getMessage());
                                }
                            }else{
                                log.error("广汇出库单入库失败，单号"+ghOrders.get(i).getBillNo()+"客户不存在");
                            }
                    }
                }
            }else{
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        } catch (Exception e) {
            log.error("广汇出入库单接收异常：",e.getMessage());
        }
    }

    /**
     * 入库单明细创建
     */
    public GHOrderDetail ghPoOrderDeatilCreated(String billNo) {
        GHOrderDetail ghOrderDetail=null;
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetBillResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            javax.xml.soap.SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("bygh");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetBill", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "billNo", "tem")).setValue(billNo);
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if (resutStr.contains("Originator")) {
                ghOrderDetail = (GHOrderDetail) JSONArray.parseObject(resutStr, GHOrderDetail.class);
            } else {
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        } catch (Exception e) {
            log.error("广汇入库单明细创建异常：",e.getMessage());
        }
        return ghOrderDetail;
    }
    /**
     * 出库单明细创建
     */
    public GHOrderDetail ghSoOrderDeatilCreated(String billNo) {
        GHOrderDetail ghOrderDetail=null;
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetBillResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            javax.xml.soap.SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("bygh");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetBill", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "billNo", "tem")).setValue(billNo);
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String resultS = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutString = xmlSerializer.read(resultS).toString();
            if (resutString.contains("Originator")) {
                ghOrderDetail = (GHOrderDetail) JSONArray.parseObject(resutString, GHOrderDetail.class);
            } else {
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resultS, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        } catch (Exception e) {
            log.error("广汇出库单明细创建异常：",e.getMessage());
        }
        return ghOrderDetail;
    }
    /**
     * 出入库单创建
     */
    public void hkOrderCreated() {
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetPendingBillsResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("byhk");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            Calendar calendarBefor = Calendar.getInstance();
            calendarBefor.add(Calendar.DATE, -1); //得到前一天
            Date beforDay = calendarBefor.getTime();
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.add(Calendar.DATE, -0); //得到前一天
            Date nowDay = calendarNow.getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetPendingBills", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "sDate", "tem")).setValue(df.format(beforDay));
            clientInfo.addChildElement(new QName(nameSpace, "eDate", "tem")).setValue(df.format(nowDay));
            clientInfo.addChildElement(new QName(nameSpace, "billType", "tem")).setValue("0");
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if(resutStr.contains("Originator")){
                List<GHOrder> hkOrders = (List<GHOrder>) JSONArray.parseArray(resutStr, GHOrder.class);
                for (int i = 0; i < hkOrders.size(); i++) {
                    if (hkOrders.get(i).getBillType()==3||hkOrders.get(i).getBillType()==4||hkOrders.get(i).getBillType()==5||hkOrders.get(i).getBillType()==22||hkOrders.get(i).getBillType()==23) {
                        String vendorCode;
                        if(hkOrders.get(i).getTraderNo()==null){
                            vendorCode="HKGYS";
                        }else {
                            vendorCode = "SZHK" + hkOrders.get(i).getTraderNo();
                        }
                            TVendor vendor=customerService.getVendorByWhseAndCode(vendorCode);
                            if(vendor!=null){
                                TPoMaster poMaster = new TPoMaster();
                                List<TPoDetail> orderDetails=new ArrayList<TPoDetail>();
                                GHOrderDetail hkOrderDetail = hkPoOrderDeatilCreated(hkOrders.get(i).getBillNo());
                                GHOrderDetail.Bill hkBill=hkOrderDetail.getBill();
                                List<GHOrderDetail.BillDetail> hkBillDetails=hkOrderDetail.getBillDetail();
                                poMaster.setCreateDate(new Date());
                                poMaster.setPoNumber("hk"+hkOrders.get(i).getBillNo());
                                poMaster.setWhId("217");
                                poMaster.setStatus("O");
                                poMaster.setCarrierMode(hkBill.getDeliveryMethod());
                                if (hkOrders.get(i).getBillType() == 23) {
                                    poMaster.setTypeId(1121);
                                } else if (hkOrders.get(i).getBillType() == 5) {
                                    poMaster.setTypeId(1120);
                                } else {
                                    poMaster.setTypeId(1119);
                                }
                                poMaster.setClientCode("SZHK");
                                poMaster.setVendorCode(vendor.getVendorCode());
                                poMaster.setVendorName(vendor.getVendorName());
                                for(int y=0;y<hkBillDetails.size();y++){
                                    TPoDetail orderDetail=new TPoDetail();
                                    orderDetail.setPoNumber("hk"+hkBill.getBillNo());
                                    orderDetail.setWhId("217");
                                    orderDetail.setItemNumber("SZHK-"+hkBillDetails.get(y).getProductNo());
                                    orderDetail.setRemark(hkBillDetails.get(y).getProductName());
                                    orderDetail.setLineNumber(String.valueOf(y)+1);
                                    orderDetail.setQty(hkBillDetails.get(y).getQty());
                                    orderDetail.setLotNumber(hkBillDetails.get(y).getBatchNo());
                                    if(hkBillDetails.get(y).getProductionDate()!=null){
                                        orderDetail.setLatestShipDate(df.parse(hkBillDetails.get(y).getProductionDate()));
                                    }
                                    orderDetails.add(orderDetail);
                                }
                                poMaster.setDetailList(orderDetails);
                                try{
                                    inBoundService.createOrUpdateInBoundOrder(poMaster);
                                    log.info("汇楷入库单入库成功，单号"+hkOrders.get(i).getBillNo());
                                }catch (Exception e){
                                    log.error("汇楷入库单入库失败，单号"+hkOrders.get(i).getBillNo()+"异常原因：",e.getMessage());
                                }
                            }else{
                                log.error("汇楷入库单入库失败，单号"+hkOrders.get(i).getBillNo()+"供应商不存在");
                            }

                    } else {
                        String customerCode;
                        if(hkOrders.get(i).getTraderNo()==null){
                            customerCode="HKGYS";
                        }else {
                            customerCode = "SZHK" + hkOrders.get(i).getTraderNo();
                        }
                            TCustomer customer=customerService.getCustomerByWhseAndCode("217",customerCode);
                            String address=hkOrders.get(i).getDeliveryAddress();
                            if(customer!=null){
                                TOrder order=new TOrder();
                                List<TOrderDetail> orderDetails=new ArrayList<TOrderDetail>();
                                GHOrderDetail hkOrderDetail = hkSoOrderDeatilCreated(hkOrders.get(i).getBillNo());
                                GHOrderDetail.Bill hkBill=hkOrderDetail.getBill();
                                List<GHOrderDetail.BillDetail> hkBillDetails=hkOrderDetail.getBillDetail();
                                String orderSerialNumber=outBoundService.getOrderSerialNumber("217");
                                order.setOrderSerialNumber(orderSerialNumber);
                                order.setOrderDate(hkOrders.get(i).getBillDate());
                                order.setCreateDate(new Date());
                                order.setOrderNumber("hk"+hkOrders.get(i).getBillNo());
                                order.setCustomerId(customer.getCustomerId());
                                order.setCustomerCode(customer.getCustomerCode());
                                order.setCustomerName(customer.getCustomerName());
                                if(address!=null&& !address.isEmpty()){
                                    String[] newAddress=address.split("\\s+");
                                    if(newAddress.length==1){
                                        order.setShipToAddr1(newAddress[0]);
                                        order.setShipToAddr2(newAddress[0]);
                                    }else if(newAddress.length==2){
                                        order.setShipToAddr1(newAddress[0]);
                                        order.setShipToAddr2(newAddress[0]);
                                        order.setShipToPhone(newAddress[1]);
                                    }else if(newAddress.length==3){
                                        order.setShipToAddr1(newAddress[0]);
                                        order.setShipToAddr2(newAddress[0]);
                                        order.setShipToPhone(newAddress[1]);
                                        order.setShipToName(newAddress[2]);
                                    }
                                }else{
                                    order.setShipToAddr1(customer.getCustomerAddr1());
                                    order.setShipToAddr2(customer.getCustomerAddr1());
                                    order.setShipToPhone(customer.getCustomerPhone());
                                    order.setShipToName(customer.getLinkman());
                                }
                                order.setWhId("217");
                                order.setStatus("N");
                                order.setClientCode("SZHK");
                                order.setCarrierMode(hkBill.getDeliveryMethod());
                                if (hkOrders.get(i).getBillType() == 230) {
                                    order.setTypeId(1155);
                                } else if (hkOrders.get(i).getBillType() == 7) {
                                    order.setTypeId(1154);
                                } else {
                                    order.setTypeId(1153);
                                }
                                for(int y=0;y<hkBillDetails.size();y++){
                                    TOrderDetail orderDetail=new TOrderDetail();
                                    orderDetail.setOrderNumber("hk"+hkBill.getBillNo());
                                    orderDetail.setWhId("217");
                                    orderDetail.setItemNumber("SZHK-"+hkBillDetails.get(y).getProductNo());
                                    orderDetail.setItemDescription(hkBillDetails.get(y).getProductName());
                                    orderDetail.setLineNumber(String.valueOf(y)+1);
                                    orderDetail.setQty(hkBillDetails.get(y).getQty());
                                    orderDetail.setAfoPlanQty(hkBillDetails.get(y).getQty());
                                    orderDetail.setLotNumber(hkBillDetails.get(y).getBatchNo());
                                    if(hkBillDetails.get(y).getProductionDate()!=null) {
                                        orderDetail.setProductionDate(df.parse(hkBillDetails.get(y).getProductionDate()));
                                    }
                                    orderDetails.add(orderDetail);
                                }
                                order.setOrderDetailList(orderDetails);
                                try{
                                    outBoundService.createOrUpdateOutBoundOrder(order);
                                    log.info("汇楷入库单出库成功，单号"+hkOrders.get(i).getBillNo());
                                }catch (Exception e){
                                    log.error("汇楷入库单出库失败，单号"+hkOrders.get(i).getBillNo()+"异常原因：",e.getMessage());
                                }
                            }else{
                                log.error("汇楷出库单入库失败，单号"+hkOrders.get(i).getBillNo()+"客户不存在");
                            }
                    }
                }
            }else{
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        } catch (Exception e) {
            log.error("汇楷出入库单接收异常：",e.getMessage());
        }
    }

    /**
     * 入库单明细创建
     */
    public GHOrderDetail hkPoOrderDeatilCreated(String billNo) {
        GHOrderDetail hkOrderDetail=null;
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetBillResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            javax.xml.soap.SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("byhk");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetBill", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "billNo", "tem")).setValue(billNo);
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if (resutStr.contains("Originator")) {
                hkOrderDetail = (GHOrderDetail) JSONArray.parseObject(resutStr, GHOrderDetail.class);
            } else {
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        } catch (Exception e) {
            log.error("汇楷出入库单明细创建异常：",e.getMessage());
        }
        return hkOrderDetail;
    }
    /**
     * 出库单明细创建
     */
    public GHOrderDetail hkSoOrderDeatilCreated(String billNo) {
        GHOrderDetail hkOrderDetail=null;
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetBillResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            javax.xml.soap.SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("byhk");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetBill", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "billNo", "tem")).setValue(billNo);
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String resultS = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutString = xmlSerializer.read(resultS).toString();
            if (resutString.contains("Originator")) {
                 hkOrderDetail = (GHOrderDetail) JSONArray.parseObject(resutString, GHOrderDetail.class);
            } else {
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resultS, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        } catch (Exception e) {
            log.error("汇楷出入库单创建异常：",e.getMessage());
        }
        return hkOrderDetail;
    }

    /**
     * 产品信息创建
     */
    public void ghProductInfo() {
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetProductsResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("bygh");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetProducts", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "productNo", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "productName", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "barcode", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "brand", "tem")).setValue("");
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if (resutStr.contains("TradePrice")) {
                List<GHProduct> ghProducts = (List<GHProduct>) JSONArray.parseArray(resutStr, GHProduct.class);
                for(int i = 0; i < ghProducts.size(); i++){
                        TItemMaster itemMaster=new TItemMaster();
                        List<TItemUom> itemUoms=new ArrayList<TItemUom>();
                        TItemUom itemUomEA=new TItemUom();
                        itemMaster.setWhId("217");
                        itemMaster.setClientCode("SZGH");
                        itemMaster.setCommodityCode(ghProducts.get(i).getBarcode());
                        itemMaster.setClassId("SZGH01");
                        itemMaster.setItemNumber("SZGH-"+ghProducts.get(i).getProductNo());
                        itemMaster.setDisplayItemNumber(ghProducts.get(i).getProductNo());
                        itemMaster.setDescription(ghProducts.get(i).getProductName());
                        itemMaster.setShelfLife(ghProducts.get(i).getSl_days());
                        itemMaster.setUom("EA");
                        itemMaster.setStdQtyUom(ghProducts.get(i).getModels());
                        itemMaster.setMsdsUrl(ghProducts.get(i).getBrand());
                        itemUomEA.setItemNumber("SZGH-"+ghProducts.get(i).getProductNo());
                        itemUomEA.setWhId("217");
                        itemUomEA.setUomPrompt(ghProducts.get(i).getUnit());
                        itemUomEA.setConversionFactor(1.00);
                        itemUomEA.setUom("EA");
                        itemUoms.add(itemUomEA);
                        itemMaster.setUoms(itemUoms);
                    try{
                        materielService.createOrUpdateMaterials(itemMaster);
                    }catch (Exception e){
                        log.error("广汇商品信息入库异常！异常原因：{}",e.getMessage());
                    }
                }
            }else{
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        }
        catch (Exception e) {
            log.error("广汇商品信息入库异常：",e.getMessage());
        }
    }
    /**
     * 产品信息创建
     */
    public void hkProductInfo() {
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetProductsResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("byhk");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetProducts", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "productNo", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "productName", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "barcode", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "brand", "tem")).setValue("");
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if (resutStr.contains("TradePrice")) {
                List<GHProduct> ghProducts = (List<GHProduct>) JSONArray.parseArray(resutStr, GHProduct.class);
                for(int i = 0; i < ghProducts.size(); i++){
                        TItemMaster itemMaster=new TItemMaster();
                        List<TItemUom> itemUoms=new ArrayList<TItemUom>();
                        TItemUom itemUomEA=new TItemUom();
                        itemMaster.setWhId("217");
                        itemMaster.setClientCode("SZHK");
                        itemMaster.setCommodityCode(ghProducts.get(i).getBarcode());
                        itemMaster.setClassId("SZGH01");
                        itemMaster.setItemNumber("SZHK-"+ghProducts.get(i).getProductNo());
                        itemMaster.setDisplayItemNumber(ghProducts.get(i).getProductNo());
                        itemMaster.setDescription(ghProducts.get(i).getProductName());
                        itemMaster.setShelfLife(ghProducts.get(i).getSl_days());
                        itemMaster.setUom("EA");
                        itemMaster.setStdQtyUom(ghProducts.get(i).getModels());
                        itemMaster.setMsdsUrl(ghProducts.get(i).getBrand());
                    itemUomEA.setItemNumber("SZHK-"+ghProducts.get(i).getProductNo());
                    itemUomEA.setWhId("217");
                    itemUomEA.setUomPrompt(ghProducts.get(i).getUnit());
                    itemUomEA.setConversionFactor(1.00);
                    itemUomEA.setUom("EA");
                    itemUoms.add(itemUomEA);
                    itemMaster.setUoms(itemUoms);
                    try{
                        materielService.createOrUpdateMaterials(itemMaster);
                    }catch (Exception e){
                        log.error("汇楷商品信息入库异常！异常原因：{}",e.getMessage());
                    }
                }
            }else{
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        }
        catch (Exception e) {
            log.error("汇楷商品信息入库异常：",e.getMessage());
        }
    }
    /**
     * 客户信息创建
     */
    public void ghCustomerInfo() {
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetCustomersResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("bygh");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetCustomers", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "customerNo", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "customerName", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "category", "tem")).setValue("");
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if (resutStr.contains("Employee")) {
                List<GHCustomer> ghCustomers = (List<GHCustomer>) JSONArray.parseArray(resutStr, GHCustomer.class);
                for(int i = 0; i < ghCustomers.size(); i++) {
                    if (ghCustomers.get(i).getCategory() != null) {
                        if (ghCustomers.get(i).getCategory().equals("供应商")) {
                                TVendor vendor=new TVendor();
                                vendor.setVendorCode("SZGH" + ghCustomers.get(i).getCustomerNo());
                                vendor.setVendorName(ghCustomers.get(i).getCustomerName());
                            try{
                                customerService.createOrUpdateVendor(vendor);
                            }catch (Exception e){
                                log.error("广汇供应商信息入库异常！异常原因：{}",e.getMessage());
                            }
                        }else{
                                TCustomer customer = new TCustomer();
                                customer.setCustomerCode("SZGH" + ghCustomers.get(i).getCustomerNo());
                                customer.setCustomerName(ghCustomers.get(i).getCustomerName());
                                customer.setCustomerPhone(ghCustomers.get(i).getTel());
                                customer.setLinkman(ghCustomers.get(i).getEmployee());
                                customer.setCustomerAddr1(ghCustomers.get(i).getAddress());
                                customer.setCustomerCountryCode("SZGH");
                                customer.setWhId("217");
                            try{
                                customerService.createOrUpdateCustomer(customer);
                            }catch (Exception e){
                                log.error("广汇客户信息入库异常！异常原因：{}",e.getMessage());
                            }
                        }
                    }
                }

            }else{
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        }
        catch (Exception e) {
            log.error("广汇客户信息入库异常！异常原因：{}",e.getMessage());
        }
    }
    /**
     * 客户信息创建
     */
    public void hkCustomerInfo() {
        try {
            String nameSpace = "http://tempuri.org/";
            String wsdlUrl = "http://119.3.53.234/Skyyx/WebService/SSThirdInterface_WMS.asmx?wsdl";
            String serviceName = "SSThirdInterface_WMS";
            String portName = "SSThirdInterface_WMSSoap12";
            String responseName = "GetCustomersResponse";
            URL url = new URL(wsdlUrl);
            QName sname = new QName(nameSpace, serviceName);
            Service service = Service.create(url, sname);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(nameSpace, portName), SOAPMessage.class, Service.Mode.MESSAGE);
            SOAPMessage msg = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
            msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("tem", nameSpace);
            envelope.removeAttribute("xmlns:env");
            SOAPHeader header = envelope.getHeader();
            SOAPHeaderElement userInfo=header.addHeaderElement(new QName(nameSpace, "UserSoapHeader", "tem"));
            userInfo.addChildElement(new QName(nameSpace, "UserName", "tem")).setValue("byhk");
            userInfo.addChildElement(new QName(nameSpace, "Password", "tem")).setValue("q5wuP9EmcRUEekek77Fk");
            header.setPrefix("soapenv");
            SOAPBody body = envelope.getBody();
            body.setPrefix("soapenv");
            SOAPBodyElement clientInfo = body.addBodyElement(new QName(nameSpace, "GetCustomers", "tem"));
            clientInfo.addChildElement(new QName(nameSpace, "customerNo", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "customerName", "tem")).setValue("");
            clientInfo.addChildElement(new QName(nameSpace, "category", "tem")).setValue("");
            msg.writeTo(System.out);
            dispatch.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, 2000);
            dispatch.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 2000);
            SOAPMessage response = dispatch.invoke(msg);
            Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
            String result = doc.getElementsByTagName(responseName).item(0).getTextContent();
            XMLSerializer xmlSerializer = new XMLSerializer();
            String resutStr = xmlSerializer.read(result).toString();
            if (resutStr.contains("Employee")) {
                List<GHCustomer> hkCustomers = (List<GHCustomer>) JSONArray.parseArray(resutStr, GHCustomer.class);
                for(int i = 0; i < hkCustomers.size(); i++) {
                    if (hkCustomers.get(i).getCategory() != null) {
                        if (hkCustomers.get(i).getCategory().equals("供应商")) {
                                TVendor vendor = new TVendor();
                                vendor.setVendorCode("SZHK" + hkCustomers.get(i).getCustomerNo());
                                vendor.setVendorName(hkCustomers.get(i).getCustomerName());
                            try{
                                customerService.createOrUpdateVendor(vendor);
                            }catch (Exception e){
                                log.error("汇楷供应商信息入库异常！异常原因：{}",e.getMessage());
                            }
                        }else{
                                TCustomer customer = new TCustomer();
                                customer.setCustomerCode("SZHK" + hkCustomers.get(i).getCustomerNo());
                                customer.setCustomerName(hkCustomers.get(i).getCustomerName());
                                customer.setCustomerPhone(hkCustomers.get(i).getTel());
                                customer.setLinkman(hkCustomers.get(i).getEmployee());
                                customer.setCustomerAddr1(hkCustomers.get(i).getAddress());
                                customer.setCustomerCountryCode("SZHK");
                                customer.setWhId("217");
                            try{
                                customerService.createOrUpdateCustomer(customer);
                            }catch (Exception e){
                                log.error("汇楷客户信息入库异常！异常原因：{}",e.getMessage());
                            }
                        }
                    }
                }
            }else{
                GHResponse responseObhect = (GHResponse) JSONObject.parseObject(resutStr, GHResponse.class);
                log.error("调用错误："+responseObhect.getDescription());
            }
        }
        catch (Exception e) {
            log.error("汇楷客户信息入库异常！异常原因：{}",e.getMessage());
        }
    }
}