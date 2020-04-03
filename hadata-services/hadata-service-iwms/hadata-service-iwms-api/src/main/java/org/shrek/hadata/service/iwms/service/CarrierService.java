package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.service.iwms.model.TCarrier;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 11:32
 */
public interface CarrierService {
    public TCarrier getCarrierByCode(String code);
}
