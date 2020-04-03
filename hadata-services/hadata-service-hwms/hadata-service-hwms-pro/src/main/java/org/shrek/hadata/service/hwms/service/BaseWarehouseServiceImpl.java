package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.hwms.mapper.TWhseMapper;
import org.shrek.hadata.service.hwms.model.TWhse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 10:51
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseWarehouseServiceImpl implements WarehouseService {

    @Autowired
    TWhseMapper tWhseMapper;

    @Override
    public String getWarehouseIdByCode(String code) {
        TWhse tWhse=new TWhse();
        tWhse.setCode(code);
        tWhse=  tWhseMapper.selectOne(tWhse);
        if(tWhse !=null) {
            return tWhse.getWhId();
        }else{
            return "";
        }
    }

    @Override
    public String getWarehouseCodeById(String whId) {
        TWhse tWhse=new TWhse();
        tWhse.setWhId(whId);
        tWhse=  tWhseMapper.selectOne(tWhse);
        return tWhse.getCode();
    }

    @Override
    public TWhse getWarehouseByCode(String code) {
        TWhse tWhse=new TWhse();
        tWhse.setCode(code);
        tWhse=  tWhseMapper.selectOne(tWhse);
        return tWhse;
    }
}
