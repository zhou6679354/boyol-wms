package org.shrek.hadata.service.reiley.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.hwms.model.TOrder;
import org.shrek.hadata.service.reiley.Application;
import org.shrek.hadata.service.reiley.service.ReileyService;
import org.shrek.hadata.service.reiley.service.scf.SupplyChainFinanceService;
import org.shrek.hadata.service.reiley.service.waybill.WayBillServiceFactory;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillDto;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillPrintDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月26日 14:25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class ReileyControllerTest {
    @Autowired
    ReileyService reileyService;
    @Autowired
    SupplyChainFinanceService supplyChainFinanceService;
    @Autowired
    WayBillServiceFactory wayBillServiceFactory;
    @Test
    public void createWayBill() throws Exception {
        TOrder tOrder = reileyService.queryDeliveryOrder("R8102500753836", "601");
        WaybillDto waybill = new WaybillDto();
        waybill.setSalePlatform(tOrder.getInterfacePlatform());
        waybill.setPlatformOrderNo(tOrder.getOrderSerialNumber());
        waybill.setVendorCode("667679");
        waybill.setVendorName("惠氏旗舰店");
        waybill.setProviderCode(tOrder.getCarrierScac());
        waybill.setVendorOrderCode(tOrder.getOrderNumber());
        waybill.setPlatformOrderNo(tOrder.getOrderNumber());
        waybill.setWeight(0);
        waybill.setVolume(0);
        waybill.setPrintTemplate(reileyService.getCarrierPrintTemplateByCode(tOrder.getCarrierScac()));
        WaybillDto.WaybillAddress fromAddress = new WaybillDto.WaybillAddress();
        fromAddress.setContact(tOrder.getDeliveryName());
        fromAddress.setMobile(tOrder.getDeliveryPhone());
        fromAddress.setPhone(tOrder.getDeliveryPhone());
        fromAddress.setProvinceName(tOrder.getDeliveryState());
        fromAddress.setCityName(tOrder.getDeliveryCity());
        fromAddress.setCountryName(tOrder.getDeliveryAddr2());
        fromAddress.setAddress(tOrder.getBillFrghtToAddr1());
        waybill.setFromAddress(fromAddress);
        WaybillDto.WaybillAddress toAddress = new WaybillDto.WaybillAddress();
        toAddress.setContact(tOrder.getShipToName());
        toAddress.setMobile(tOrder.getShipToPhone());
        toAddress.setPhone(tOrder.getShipToPhone());
        toAddress.setProvinceName(tOrder.getShipToState());
        toAddress.setCityName(tOrder.getShipToCity());
        toAddress.setCountryName(tOrder.getShipToAddr2());
        toAddress.setAddress(tOrder.getShipToAddr1());
        waybill.setToAddress(toAddress);
        BaseResponse<WaybillPrintDto> response = wayBillServiceFactory.build(tOrder.getInterfacePlatform()).newWayBill(waybill);
        if (response.isFlag()) {
            if (response.getData() != null) {
                reileyService.updateWayBillCode(tOrder.getOrderNumber(), tOrder.getWhId(), response.getData().getTask().getId());
                System.out.println(JacksonUtil.nonEmpty().toJson(response.getData()));
            } else {
                System.out.println(JacksonUtil.nonEmpty().toJson(BaseResponse.fail(response.getSubMessage())));
            }
        } else {
            System.out.println(JacksonUtil.nonEmpty().toJson(BaseResponse.fail(response.getSubMessage())));
        }
    }

}