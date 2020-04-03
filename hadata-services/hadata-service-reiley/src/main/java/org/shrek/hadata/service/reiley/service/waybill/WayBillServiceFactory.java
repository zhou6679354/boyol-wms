package org.shrek.hadata.service.reiley.service.waybill;

import org.shrek.hadata.service.reiley.service.waybill.impl.CainiaoWayBillServiceImpl;
import org.shrek.hadata.service.reiley.service.waybill.impl.JingDongWayBillServiceImpl;
import org.springframework.stereotype.Component;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月29日 14:17
 */
@Component
public class WayBillServiceFactory {

    public WayBillService build(String type) {
        if ("TB".equals(type)) {
            return new CainiaoWayBillServiceImpl();
        } else if ("JD".equals(type)) {
            return new JingDongWayBillServiceImpl();
        } else {
            return new JingDongWayBillServiceImpl();
        }
    }
}
