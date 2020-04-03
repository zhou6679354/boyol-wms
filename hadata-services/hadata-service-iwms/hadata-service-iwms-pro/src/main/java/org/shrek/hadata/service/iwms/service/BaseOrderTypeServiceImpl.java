package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.iwms.mapper.TOrderTypeMapper;
import org.shrek.hadata.service.iwms.model.TblOrderTypeMapping;
import org.springframework.beans.factory.annotation.Autowired;

@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseOrderTypeServiceImpl implements OrderTypeService {
    @Autowired
    TOrderTypeMapper tOrderTypeMapper;
    @Override
    public int addOrderType(TblOrderTypeMapping tblOrderTypeMapping) {
        return tOrderTypeMapper.insert(tblOrderTypeMapping);
    }
}
