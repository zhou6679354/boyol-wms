package org.shrek.hadata.service.reiley.service.waybill.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.wujiemiandan.SignSuccessQueryApi.SignSuccessQueryDTO;
import com.jd.open.api.sdk.domain.wujiemiandan.StockQueryApi.WaybillStockDTO;
import com.jd.open.api.sdk.domain.wujiemiandan.http.WaybillResponseDTO;
import com.jd.open.api.sdk.request.wujiemiandan.LdopAlphaProviderSignSuccessRequest;
import com.jd.open.api.sdk.request.wujiemiandan.LdopAlphaVendorBigshotQueryRequest;
import com.jd.open.api.sdk.request.wujiemiandan.LdopAlphaVendorStockQueryRequest;
import com.jd.open.api.sdk.request.wujiemiandan.LdopAlphaWaybillReceiveRequest;
import com.jd.open.api.sdk.response.wujiemiandan.LdopAlphaProviderSignSuccessResponse;
import com.jd.open.api.sdk.response.wujiemiandan.LdopAlphaVendorBigshotQueryResponse;
import com.jd.open.api.sdk.response.wujiemiandan.LdopAlphaVendorStockQueryResponse;
import com.jd.open.api.sdk.response.wujiemiandan.LdopAlphaWaybillReceiveResponse;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.IdUtil;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.StringUtil;
import org.shrek.hadata.service.reiley.service.waybill.WayBillService;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillDto;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillPrintContentDto;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillPrintDataDto;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillPrintDto;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月29日 14:15
 */
@Slf4j
public class JingDongWayBillServiceImpl implements WayBillService {

    public static IdUtil idUtil = new IdUtil(0, 0);

    private static String serverUrl = "https://api.jd.com/routerjson";
    private static String accessToken = "70110ff5-f3bf-45b9-9632-fa09b0d0c48f";
    private static String appKey = "02C00E7BABB474AD83C640F032C7315C";
    private static String appSecret = "7c176d6a4bff48aa92b44b9dcffd9e99";
    private static String vendorCode = "667679";

    private static JdClient client = new DefaultJdClient(serverUrl, accessToken, appKey, appSecret);

    @Override
    public BaseResponse<WaybillPrintDto> newWayBill(WaybillDto waybill) {
        if (waybill.getSalePlatform().equals("JD")) {
            waybill.setSalePlatform("0010001");
        } else {
            waybill.setSalePlatform("0030001");
        }

        //根据商家编码查询商家所有审核成功的签约信息
        BaseResponse<List<SignSuccessQueryDTO>> signResponse = querySignProviders(client, vendorCode);
        List<SignSuccessQueryDTO> signProviders = signResponse.getData().stream().filter(provider -> provider.getProviderCode().equals(waybill.getProviderCode())).collect(Collectors.toList());
        //调用“商家库存查询接口”查询是否有库存信息
        if (signProviders.size() > 0) {
            SignSuccessQueryDTO signSuccessQueryDTO = signProviders.get(0);
            if (signSuccessQueryDTO.getOperationType().toString().equals("1")) {
                waybill.setSettlementCode(signSuccessQueryDTO.getSettlementCode());
                if (signSuccessQueryDTO.getProviderCode().equals("SF")) {
                    waybill.setExpressPayMethod("1");
                    waybill.setExpressType("1");
                }
            } else {
                waybill.setBranchCode(signSuccessQueryDTO.getBranchCode());
            }
//            BaseResponse<List<WaybillStockDTO>> stockResult = queryVendorStock(client, vendorCode, signSuccessQueryDTO.getProviderId());
            //调用京东接单接口,获取快递公司运单号.
//            if (stockResult.isFlag()) {
            BaseResponse<String> ldopWaybill = receiveWaybill(client, waybill);
            if (ldopWaybill.isFlag()) {
                WaybillPrintDto waybillPrint = new WaybillPrintDto();
                waybillPrint.setId(String.valueOf(idUtil.nextId()));
                waybillPrint.setCmd("print");
                waybillPrint.setVersion("1.0");
                WaybillPrintDataDto printData = new WaybillPrintDataDto();
                WaybillResponseDTO dto = JacksonUtil.nonEmpty().fromJson(ldopWaybill.getData(), WaybillResponseDTO.class);
                printData.setWaybillCode(dto.getData().getWaybillCodeList().get(0));
                printData.setGoodsDesc("营养品");
                WaybillPrintDataDto.AddressBean sendAddress = new WaybillPrintDataDto.AddressBean();
                sendAddress.setProvince(waybill.getFromAddress().getProvinceName());
                sendAddress.setCity(waybill.getFromAddress().getCityName());
                sendAddress.setDistrict(waybill.getFromAddress().getCountryName());
                sendAddress.setDetail(waybill.getFromAddress().getAddress());
                WaybillPrintDataDto.Contacts sendContacts = new WaybillPrintDataDto.Contacts();
                sendContacts.setName(waybill.getFromAddress().getContact());
                sendContacts.setPhone(waybill.getFromAddress().getPhone());
                sendContacts.setMobile(waybill.getFromAddress().getMobile());
                sendContacts.setAddress(sendAddress);
                printData.setSender(sendContacts);
                WaybillPrintDataDto.AddressBean receiveAddress = new WaybillPrintDataDto.AddressBean();
                receiveAddress.setProvince(waybill.getToAddress().getProvinceName());
                receiveAddress.setCity(waybill.getToAddress().getCityName());
                receiveAddress.setDistrict(waybill.getToAddress().getCountryName());
                receiveAddress.setDetail(waybill.getToAddress().getAddress());
                WaybillPrintDataDto.Contacts receiveContacts = new WaybillPrintDataDto.Contacts();
                receiveContacts.setName(waybill.getToAddress().getContact());
                receiveContacts.setPhone(waybill.getToAddress().getPhone());
                receiveContacts.setMobile(waybill.getToAddress().getMobile());
                receiveContacts.setAddress(receiveAddress);
                printData.setRecipient(receiveContacts);
                LdopAlphaVendorBigshotQueryResponse bigshotResponse = queryBigshot(client, dto.getData().getWaybillCodeList().get(0), waybill.getProviderCode());
                WaybillPrintDataDto.RoutingInfoBean routingInfoBean = new WaybillPrintDataDto.RoutingInfoBean();
                routingInfoBean.setRouteCode(Joiner.on('-').join(bigshotResponse.getResultInfo().getData().get(0).getSecondSectionCode()
                        , bigshotResponse.getResultInfo().getData().get(0).getThirdSectionCode()));
                WaybillPrintDataDto.RoutingInfoBean.ConsolidationBean consolidation = new WaybillPrintDataDto.RoutingInfoBean.ConsolidationBean();
                consolidation.setCode(bigshotResponse.getResultInfo().getData().get(0).getGatherCenterCode());
                consolidation.setName(bigshotResponse.getResultInfo().getData().get(0).getGatherCenterName());
                routingInfoBean.setConsolidation(consolidation);
                WaybillPrintDataDto.RoutingInfoBean.OriginBean origin = new WaybillPrintDataDto.RoutingInfoBean.OriginBean();
                origin.setCode("");
                origin.setName("");
                routingInfoBean.setOrigin(origin);
                WaybillPrintDataDto.RoutingInfoBean.SortationBean sortation = new WaybillPrintDataDto.RoutingInfoBean.SortationBean();
                sortation.setName(bigshotResponse.getResultInfo().getData().get(0).getBigShotName());
                routingInfoBean.setSortation(sortation);
                printData.setRoutingInfo(routingInfoBean);
                WaybillPrintDataDto.ShippingOption shippingOption = new WaybillPrintDataDto.ShippingOption();
                shippingOption.setCode("COD");
                HashMap<String, HashMap<String, String>> services = Maps.newHashMap();
                HashMap<String, String> nextDay = Maps.newHashMap();
                nextDay.put("value", "NEXT-DAY");
                services.put("TIMED-DELIVERY", nextDay);
                HashMap<String, String> svcInsure = Maps.newHashMap();
                svcInsure.put("value", "MONTHLY");
                services.put("PAYMENT-TYPE", svcInsure);
                shippingOption.setServices(services);
                shippingOption.setTitle("电商");
                printData.setShippingOption(shippingOption);
                WaybillPrintContentDto content = new WaybillPrintContentDto();
                content.setData(printData);
                content.setTemplate(waybill.getPrintTemplate());
                WaybillPrintDto.WaybillPrintTask printTask = new WaybillPrintDto.WaybillPrintTask();
                printTask.setId(dto.getData().getWaybillCodeList().get(0));
                printTask.setPreview(false);
                printTask.setPreviewType("pdf");
                printTask.setPrinter("");
                printTask.setFirstDocumentNumber(10);
                printTask.setTotalDocumentCount(100);
                WaybillPrintDto.WaybillPrintTaskDocument printTaskDocument = new WaybillPrintDto.WaybillPrintTaskDocument();
                printTaskDocument.setId(dto.getData().getPlatformOrderNo());
                printTaskDocument.setContents(Lists.newArrayList(content));
                printTask.setDocuments(Lists.newArrayList(printTaskDocument));
                waybillPrint.setTask(printTask);
                return BaseResponse.success(waybillPrint);

            } else {
                return BaseResponse.fail(ldopWaybill.getSubMessage());
            }
//            } else {
//                return BaseResponse.fail(stockResult.getSubCode(), stockResult.getSubMessage());
//            }
        } else {
            return BaseResponse.fail("未查询商家所有审核成功的签约信息");
        }
    }


    /**
     * 根据商家编码查询商家所有审核成功的签约信息
     * jingdong.ldop.alpha.provider.sign.success
     *
     * @param client
     * @param vendorCode
     * @return
     */
    private BaseResponse<List<SignSuccessQueryDTO>> querySignProviders(JdClient client, String vendorCode) {
        LdopAlphaProviderSignSuccessRequest request = new LdopAlphaProviderSignSuccessRequest();
        request.setVendorCode(vendorCode);
        try {

            LdopAlphaProviderSignSuccessResponse response = client.execute(request);
            if (response.getResultInfo().getStatusCode() == 0) {
                return BaseResponse.success(response.getResultInfo().getData());
            } else {
                return BaseResponse.fail(vendorCode + "查询商家所有审核成功的签约信息失败" + response.getResultInfo().getStatusMessage());
            }
        } catch (JdException e) {
            return BaseResponse.fail(vendorCode + "查询商家所有审核成功的签约信息异常" + e.getErrMsg());

        }
    }

    /**
     * 调用“商家库存查询接口”查询是否有库存信息
     * jingdong.ldop.alpha.vendor.stock.query
     */
    private BaseResponse<List<WaybillStockDTO>> queryVendorStock(JdClient client, String vendorCode, int providerId) {
        LdopAlphaVendorStockQueryRequest request = new LdopAlphaVendorStockQueryRequest();
        request.setVendorCode(vendorCode);
        request.setProviderId(providerId);
        try {
            LdopAlphaVendorStockQueryResponse response = client.execute(request);
            if (response.getResultInfo().getStatusCode() == 0) {
                return BaseResponse.success(response.getResultInfo().getData());
            } else {
                return BaseResponse.fail("该承运商面单库存不足!");
            }
        } catch (JdException e) {
            return BaseResponse.fail(e.getErrMsg());
        }
    }

    /**
     * 调用京东接单接口,获取快递公司运单号
     * jingdong.ldop.alpha.waybill.receive
     */
    private BaseResponse<String> receiveWaybill(JdClient client, WaybillDto waybill) {
        LdopAlphaWaybillReceiveRequest ldopAlphaWaybillReceiveRequest = new LdopAlphaWaybillReceiveRequest();
        String content = JacksonUtil.nonEmpty().toJson(waybill);
        ldopAlphaWaybillReceiveRequest.setContent(content);
        try {
            log.warn("调用京东电子面单接口:" + content);
            LdopAlphaWaybillReceiveResponse response = client.execute(ldopAlphaWaybillReceiveRequest);
            if(StringUtil.isEmpty(response.getCode())) {
                return BaseResponse.success(response.getMsg());
            }else{
                return BaseResponse.fail(response.getMsg());
            }
        } catch (JdException e) {
            return BaseResponse.fail(e.getErrCode(), e.getErrMsg());
        }
    }

    /**
     * 调用大头笔查询接口,获取快递公司大头笔、二段码、三段码、集包地等信息.
     * jingdong.ldop.alpha.vendor.bigshot.query
     */
    private LdopAlphaVendorBigshotQueryResponse queryBigshot(JdClient client, String waybillCode, String providerCode) {
        LdopAlphaVendorBigshotQueryRequest request = new LdopAlphaVendorBigshotQueryRequest();
        request.setWaybillCode(waybillCode);
        request.setProviderCode(providerCode);
        try {
            LdopAlphaVendorBigshotQueryResponse response = client.execute(request);
            return response;
        } catch (JdException e) {
            log.error(e.getErrCode(), e.getErrMsg());
            return null;
        }


    }
}
