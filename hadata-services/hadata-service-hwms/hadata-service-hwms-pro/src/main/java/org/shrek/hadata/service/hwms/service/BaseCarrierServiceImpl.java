package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.hwms.mapper.TCarrierMapper;
import org.shrek.hadata.service.hwms.model.TCarrier;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 11:33
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseCarrierServiceImpl implements CarrierService {

    @Autowired
    TCarrierMapper tCarrierMapper;

    @Override
    public TCarrier getCarrierByCode(String code) {
        TCarrier tCarrier = new TCarrier();
        tCarrier.setCarrierCode(code);

        return tCarrierMapper.selectOne(tCarrier);

    }
}
