package org.shrek.hadata.service.reiley.service.waybill.impl;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.reiley.service.waybill.WayBillService;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillDto;
import org.shrek.hadata.service.reiley.service.waybill.model.WaybillPrintDto;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月29日 14:16
 */
public class CainiaoWayBillServiceImpl implements WayBillService {
    @Override
    public BaseResponse<WaybillPrintDto> newWayBill(WaybillDto waybill) {


        return BaseResponse.success();
    }
}
