package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.iwms.mapper.TWhseControlMapper;
import org.shrek.hadata.service.iwms.mapper.TWhseMapper;
import org.shrek.hadata.service.iwms.model.TWhse;
import org.shrek.hadata.service.iwms.model.TWhseControl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 10:51
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseWarehouseServiceImpl implements WarehouseService {

    @Autowired
    TWhseMapper tWhseMapper;
    @Autowired
    TWhseControlMapper tWhseControlMapper;

    @Override
    public TWhse getWarehouseByCode(String code) {
        TWhse tWhse=new TWhse();
        tWhse.setCode(code);
        tWhse=  tWhseMapper.selectOne(tWhse);
        return tWhse;
    }
    @Override
    public TWhse getWarehouseByWhId(String whId) {
        TWhse tWhse=new TWhse();
        tWhse.setWhId(whId);
        tWhse=  tWhseMapper.selectOne(tWhse);
        return tWhse;
    }
    @Override
    public TWhse getWarehouseByCity(String city) {
        TWhse tWhse=new TWhse();
        tWhse.setCity(city);
        tWhse=  tWhseMapper.selectOne(tWhse);
        return tWhse;
    }

    @Override
    public int addWarehouse(TWhse whse) {
        return tWhseMapper.insert(whse);
    }

    @Override
    public int addWarehouseControl(TWhseControl whseControl) {
        return tWhseControlMapper.insert(whseControl);
    }
}
