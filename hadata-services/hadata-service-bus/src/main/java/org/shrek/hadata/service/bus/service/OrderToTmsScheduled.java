package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.MaterielService;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.shrek.hadata.service.bus.web.model.TMSOrder;
import org.shrek.hadata.service.bus.web.model.TMSOrderDetail;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class OrderToTmsScheduled {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    MaterielService materielService;
    /**
     * 出库单推送TMS
     */
    public void feedbackPurchaseOutStorage() {
        List<TOrder> orders = outBoundService.queryOrderForTMS("811");
        for(int i=0;i<orders.size();i++){
            TMSOrder tmsOrder=new TMSOrder();
            tmsOrder.setOrderId(orders.get(i).getOrderId());
            tmsOrder.setWhId(orders.get(i).getWhId());
            tmsOrder.setOrderNumber(orders.get(i).getOrderNumber());
            tmsOrder.setStoreOrderNumber(orders.get(i).getStoreOrderNumber());
            tmsOrder.setTypeId(orders.get(i).getTypeId());
            tmsOrder.setCustomerId(orders.get(i).getCustomerId());
            tmsOrder.setCustPoNumber(orders.get(i).getCustPoNumber());
            tmsOrder.setCustomerName(orders.get(i).getCustomerName());
            tmsOrder.setCustomerPhone(orders.get(i).getCustomerPhone());
            tmsOrder.setCustomerFax(orders.get(i).getCustomerFax());
            tmsOrder.setCustomerEmail(orders.get(i).getCustomerEmail());
            tmsOrder.setDepartment(orders.get(i).getDepartment());
            tmsOrder.setLoadId(orders.get(i).getLoadId());
            tmsOrder.setLoadSeq(orders.get(i).getLoadSeq());
            tmsOrder.setBolNumber(orders.get(i).getBolNumber());
            tmsOrder.setProNumber(orders.get(i).getProNumber());
            tmsOrder.setMasterBolNumber(orders.get(i).getMasterBolNumber());
            tmsOrder.setCarrier(orders.get(i).getCarrier());
            tmsOrder.setCarrierScac(orders.get(i).getCarrierScac());
            tmsOrder.setFreightTerms(orders.get(i).getFreightTerms());
            tmsOrder.setRush(orders.get(i).getRush());
            tmsOrder.setPriority(orders.get(i).getPriority());
            tmsOrder.setOrderDate(orders.get(i).getOrderDate());
            tmsOrder.setArriveDate(orders.get(i).getArriveDate());
            tmsOrder.setActualArrivalDate(orders.get(i).getActualArrivalDate());
            tmsOrder.setDatePicked(orders.get(i).getDatePicked());
            tmsOrder.setDateExpected(orders.get(i).getDateExpected());
            tmsOrder.setPromisedDate(orders.get(i).getPromisedDate());
            tmsOrder.setWeight(orders.get(i).getWeight());
            tmsOrder.setCubicVolume(orders.get(i).getCubicVolume());
            tmsOrder.setContainers(orders.get(i).getContainers());
            tmsOrder.setBackorder(orders.get(i).getBackorder());
            tmsOrder.setPrePaid(orders.get(i).getPrePaid());
            tmsOrder.setCodAmount(orders.get(i).getCodAmount());
            tmsOrder.setInsuranceAmount(orders.get(i).getInsuranceAmount());
            tmsOrder.setPipAmount(orders.get(i).getPipAmount());
            tmsOrder.setFreightCost(orders.get(i).getFreightCost());
            tmsOrder.setRegion(orders.get(i).getRegion());
            tmsOrder.setBillToCode(orders.get(i).getBillToCode());
            tmsOrder.setBillToName(orders.get(i).getBillToName());
            tmsOrder.setBillToAddr1(orders.get(i).getBillToAddr1());
            tmsOrder.setBillToAddr2(orders.get(i).getBillToAddr2());
            tmsOrder.setBillToAddr3(orders.get(i).getBillToAddr3());
            tmsOrder.setBillToCity(orders.get(i).getBillToCity());
            tmsOrder.setBillToState(orders.get(i).getBillToState());
            tmsOrder.setBillToZip(orders.get(i).getBillToZip());
            tmsOrder.setBillToCountryCode(orders.get(i).getBillToCountryCode());
            tmsOrder.setBillToCountryName(orders.get(i).getBillToCountryName());
            tmsOrder.setBillToPhone(orders.get(i).getBillToPhone());
            tmsOrder.setShipToCode(orders.get(i).getShipToCode());
            tmsOrder.setShipToName(orders.get(i).getShipToName());
            tmsOrder.setShipToAddr1(orders.get(i).getShipToAddr1());
            tmsOrder.setShipToAddr2(orders.get(i).getShipToAddr2());
            tmsOrder.setShipToAddr3(orders.get(i).getShipToAddr3());
            tmsOrder.setShipToState(orders.get(i).getShipToState());
            tmsOrder.setShipToZip(orders.get(i).getShipToZip());
            tmsOrder.setShipToCountryCode(orders.get(i).getShipToCountryCode());
            tmsOrder.setShipToCountryName(orders.get(i).getShipToCountryName());
            tmsOrder.setShipToPhone(orders.get(i).getShipToPhone());
            tmsOrder.setDeliveryName(orders.get(i).getDeliveryName());
            tmsOrder.setDeliveryAddr1(orders.get(i).getDeliveryAddr1());
            tmsOrder.setDeliveryAddr2(orders.get(i).getDeliveryAddr2());
            tmsOrder.setDeliveryAddr3(orders.get(i).getDeliveryAddr3());
            tmsOrder.setDeliveryCity(orders.get(i).getDeliveryCity());
            tmsOrder.setDeliveryCountryCode(orders.get(i).getDeliveryCountryCode());
            tmsOrder.setDeliveryCountryName(orders.get(i).getDeliveryCountryName());
            tmsOrder.setDeliveryPhone(orders.get(i).getDeliveryPhone());
            tmsOrder.setBillFrghtToCode(orders.get(i).getBillFrghtToCode());
            tmsOrder.setBillFrghtToName(orders.get(i).getBillFrghtToName());
            tmsOrder.setBillFrghtToAddr1(orders.get(i).getBillFrghtToAddr1());
            tmsOrder.setBillFrghtToAddr2(orders.get(i).getBillFrghtToAddr2());
            tmsOrder.setBillFrghtToAddr3(orders.get(i).getBillFrghtToAddr3());
            tmsOrder.setBillFrghtToCity(orders.get(i).getBillFrghtToCity());
            tmsOrder.setBillFrghtToState(orders.get(i).getBillFrghtToState());
            tmsOrder.setBillFrghtToZip(orders.get(i).getBillFrghtToZip());
            tmsOrder.setBillFrghtToCountryCode(orders.get(i).getBillFrghtToCountryCode());
            tmsOrder.setBillFrghtToCountryName(orders.get(i).getBillFrghtToCountryName());
            tmsOrder.setBillFrghtToPhone(orders.get(i).getBillFrghtToPhone());
            tmsOrder.setReturnToCode(orders.get(i).getReturnToCode());
            tmsOrder.setReturnToName(orders.get(i).getReturnToName());
            tmsOrder.setReturnToAddr1(orders.get(i).getReturnToAddr1());
            tmsOrder.setReturnToAddr2(orders.get(i).getReturnToAddr2());
            tmsOrder.setReturnToAddr3(orders.get(i).getReturnToAddr3());
            tmsOrder.setReturnToCity(orders.get(i).getReturnToCity());
            tmsOrder.setReturnToState(orders.get(i).getReturnToState());
            tmsOrder.setReturnToZip(orders.get(i).getReturnToZip());
            tmsOrder.setReturnToCountryCode(orders.get(i).getReturnToCountryCode());
            tmsOrder.setReturnToCountryName(orders.get(i).getReturnToCountryName());
            tmsOrder.setReturnToPhone(orders.get(i).getReturnToPhone());
            tmsOrder.setRmaNumber(orders.get(i).getRmaNumber());
            tmsOrder.setRmaExpirationDate(orders.get(i).getRmaExpirationDate());
            tmsOrder.setCartonLabel(orders.get(i).getCartonLabel());
            tmsOrder.setVerFlag(orders.get(i).getVerFlag());
            tmsOrder.setFullPallets(orders.get(i).getFullPallets());
            tmsOrder.setHazFlag(orders.get(i).getHazFlag());
            tmsOrder.setOrderWgt(orders.get(i).getOrderWgt());
            tmsOrder.setStatus(orders.get(i).getStatus());
            tmsOrder.setZone(orders.get(i).getZone());
            tmsOrder.setDropShip(orders.get(i).getDropShip());
            tmsOrder.setLockFlag(orders.get(i).getLockFlag());
            tmsOrder.setPartialOrderFlag(orders.get(i).getPartialOrderFlag());
            tmsOrder.setEarliestShipDate(orders.get(i).getEarliestShipDate());
            tmsOrder.setLatestShipDate(orders.get(i).getLatestShipDate());
            tmsOrder.setActualShipDate(orders.get(i).getActualShipDate());
            tmsOrder.setEarliestDeliveryDate(orders.get(i).getEarliestDeliveryDate());
            tmsOrder.setLatestDeliveryDate(orders.get(i).getLatestDeliveryDate());
            tmsOrder.setActualDeliveryDate(orders.get(i).getActualDeliveryDate());
            tmsOrder.setRoute(orders.get(i).getRoute());
            tmsOrder.setBaselineRate(orders.get(i).getBaselineRate());
            tmsOrder.setPlanningRate(orders.get(i).getPlanningRate());
            tmsOrder.setCarrierId(orders.get(i).getCarrierId());
            tmsOrder.setManifestCarrierId(orders.get(i).getManifestCarrierId());
            tmsOrder.setShipViaId(orders.get(i).getShipViaId());
            tmsOrder.setDisplayOrderNumber(orders.get(i).getDisplayOrderNumber());
            tmsOrder.setClientCode(orders.get(i).getClientCode());
            tmsOrder.setClientName(orders.get(i).getClientName());
            tmsOrder.setShipToResidentialFlag(orders.get(i).getShipToResidentialFlag());
            tmsOrder.setCarrierMode(orders.get(i).getCarrierMode());
            tmsOrder.setServiceLevel(orders.get(i).getServiceLevel());
            tmsOrder.setShipToAttention(orders.get(i).getShipToAttention());
            tmsOrder.setEarliestApptTime(orders.get(i).getEarliestApptTime());
            tmsOrder.setLatestApptTime(orders.get(i).getLatestApptTime());
            tmsOrder.setPaymentTerms(orders.get(i).getPaymentTerms());
            tmsOrder.setOrderSerialNumber(orders.get(i).getOrderSerialNumber());
            tmsOrder.setCreateDate(orders.get(i).getCreateDate());
            tmsOrder.setCarrierNumber(orders.get(i).getCarrierNumber());
            tmsOrder.setPlateNumber(orders.get(i).getPlateNumber());
            tmsOrder.setInterfaceId(orders.get(i).getInterfaceId());
            tmsOrder.setIsSendBack(orders.get(i).getIsSendBack());
            tmsOrder.setMassiveControl(orders.get(i).getMassiveControl());
            tmsOrder.setTmsSynRemark(orders.get(i).getTmsSynRemark());
            tmsOrder.setTmsSynStatus(orders.get(i).getTmsSynStatus());
            tmsOrder.setTmsSynTimes(orders.get(i).getTmsSynTimes());
            tmsOrder.setTmsSynTime(orders.get(i).getTmsSynTime());
            tmsOrder.setCustomerCode(orders.get(i).getCustomerCode());
            List<TMSOrderDetail> tmsOrderDetails=new ArrayList<TMSOrderDetail>();
            for(int y=0;y<orders.get(i).getOrderDetailList().size();y++){
                TMSOrderDetail tmsOrderDetail=new TMSOrderDetail();
                TOrderDetail tOrderDetail=orders.get(i).getOrderDetailList().get(y);
                TItemMaster itemMaster=materielService.getItemMasterByWhIdandItemNumber("811",tOrderDetail.getItemNumber());
                tmsOrderDetail.setOrderDetailId(tOrderDetail.getOrderDetailId());
                tmsOrderDetail.setOrderId(tOrderDetail.getOrderId());
                tmsOrderDetail.setItemMasterId(tOrderDetail.getItemMasterId());
                tmsOrderDetail.setWhId(tOrderDetail.getWhId());
                tmsOrderDetail.setOrderNumber(tOrderDetail.getOrderNumber());
                tmsOrderDetail.setLineNumber(tOrderDetail.getLineNumber());
                tmsOrderDetail.setItemNumber(tOrderDetail.getItemNumber());
                tmsOrderDetail.setItemName(itemMaster.getDescription());
                tmsOrderDetail.setBoQty(tOrderDetail.getBoQty());
                tmsOrderDetail.setBoDescription(tOrderDetail.getBoDescription());
                tmsOrderDetail.setBoWeight(tOrderDetail.getBoWeight());
                tmsOrderDetail.setQty(tOrderDetail.getQty());
                tmsOrderDetail.setAfoPlanQty(tOrderDetail.getAfoPlanQty());
                tmsOrderDetail.setUnitPack(tOrderDetail.getUnitPack());
                tmsOrderDetail.setItemWeight(tOrderDetail.getItemWeight());
                tmsOrderDetail.setItemTareWeight(tOrderDetail.getItemTareWeight());
                tmsOrderDetail.setHazMaterial(tOrderDetail.getHazMaterial());
                tmsOrderDetail.setBOLClass(tOrderDetail.getBOLClass());
                tmsOrderDetail.setBOLLine1(tOrderDetail.getBOLLine1());
                tmsOrderDetail.setBOLLine2(tOrderDetail.getBOLLine2());
                tmsOrderDetail.setBOLLine3(tOrderDetail.getBOLLine3());
                tmsOrderDetail.setBOLPlacCode(tOrderDetail.getBOLPlacCode());
                tmsOrderDetail.setBOLPlacDesc(tOrderDetail.getBOLPlacDesc());
                tmsOrderDetail.setBOLCode(tOrderDetail.getBOLCode());
                tmsOrderDetail.setQtyShipped(tOrderDetail.getQtyShipped());
                tmsOrderDetail.setLineType(tOrderDetail.getLineType());
                tmsOrderDetail.setItemDescription(tOrderDetail.getItemDescription());
                tmsOrderDetail.setStackingSeq(tOrderDetail.getStackingSeq());
                tmsOrderDetail.setCustPart(tOrderDetail.getCustPart());
                tmsOrderDetail.setLotNumber(tOrderDetail.getLotNumber());
                tmsOrderDetail.setPickingFlow(tOrderDetail.getPickingFlow());
                tmsOrderDetail.setUnitWeight(itemMaster.getUnitWeight());
                tmsOrderDetail.setUnitVolume(itemMaster.getUnitVolume());
                tmsOrderDetail.setExtendedWeight(tOrderDetail.getExtendedWeight());
                tmsOrderDetail.setExtendedVolume(tOrderDetail.getExtendedVolume());
                tmsOrderDetail.setOverAllocQty(tOrderDetail.getOverAllocQty());
                tmsOrderDetail.setDateExpected(tOrderDetail.getDateExpected());
                tmsOrderDetail.setOrderUom(tOrderDetail.getOrderUom());
                tmsOrderDetail.setHostWaveId(tOrderDetail.getHostWaveId());
                tmsOrderDetail.setTranPlanQty(tOrderDetail.getTranPlanQty());
                tmsOrderDetail.setUseShippableUom(tOrderDetail.getUseShippableUom());
                tmsOrderDetail.setUnitInsuranceAmount(tOrderDetail.getUnitInsuranceAmount());
                tmsOrderDetail.setStoredAttributeId(tOrderDetail.getStoredAttributeId());
                tmsOrderDetail.setHoldReasonId(tOrderDetail.getHoldReasonId());
                tmsOrderDetail.setPoNumber(tOrderDetail.getPoNumber());
                tmsOrderDetail.setPoLineNumber(tOrderDetail.getPoLineNumber());
                tmsOrderDetail.setPoItemNumber(tOrderDetail.getPoItemNumber());
                tmsOrderDetail.setCancelFlag(tOrderDetail.getCancelFlag());
                tmsOrderDetail.setOriLineNumber(tOrderDetail.getOriLineNumber());
                tmsOrderDetail.setZone(tOrderDetail.getZone());
                tmsOrderDetail.setPickLocation(tOrderDetail.getPickLocation());
                tmsOrderDetail.setHuId(tOrderDetail.getHuId());
                tmsOrderDetail.setStorageType(tOrderDetail.getStorageType());
                tmsOrderDetail.setProductionDate(tOrderDetail.getProductionDate());
                tmsOrderDetail.setExpirationDate(tOrderDetail.getExpirationDate());
                tmsOrderDetail.setMeasuringNumber(tOrderDetail.getMeasuringNumber());
                tmsOrderDetail.setColorNumber(tOrderDetail.getColorNumber());
                tmsOrderDetail.setStCode(tOrderDetail.getStCode());
                tmsOrderDetail.setStName(tOrderDetail.getStName());
                tmsOrderDetails.add(tmsOrderDetail);
            }
            tmsOrder.setOrderDetailList(tmsOrderDetails);
            String json = JacksonUtil.nonAlways().toJson(tmsOrder);
            log.info("出库单同步TMS发送的JSON串为"+json);
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                Request request = new Request.Builder().url("http://116.236.121.205:8086/wms/order").post(body).build();
                Response response = client.newCall(request).execute();
                String responses = response.body().string();
                JacksonUtil js = new JacksonUtil(JsonInclude.Include.ALWAYS, JacksonUtil.Type.JSON);
                HashMap results = js.fromJson(responses, HashMap.class);
                if (Integer.parseInt(String.valueOf(results.get("errorCode")))==0) {
                    outBoundService.updateOutBoundsToTmsStatus(orders.get(i).getOrderId());
                    log.info("出库单同步TMS"+results.get("remark")+orders.get(i).getOrderNumber());
                } else {
                    log.error("出库单同步TMS"+results.get("remark")+orders.get(i).getOrderNumber());
                }
            } catch (Exception e) {
                log.error("出库单同步TMS失败,原因"+e.toString());
            }
        }
    }
}
