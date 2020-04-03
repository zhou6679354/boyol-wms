package org.shrek.hadata.service.hwms.service;


import org.shrek.hadata.service.hwms.model.TCarrier;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 11:32
 */
public interface CarrierService {
    public TCarrier getCarrierByCode(String code);
}
