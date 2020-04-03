package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Maps;
import org.shrek.hadata.service.hwms.mapper.TCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月15日 10:40
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseCustomerServiceImpl implements CustomerService {

    @Autowired
    TCustomerMapper tCustomerMapper;


    @Override
    public String queryCustomerId(String sourceCode, String client, String store) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("sourceCode", sourceCode);
        params.put("client", client);
        params.put("store", store);
        return tCustomerMapper.queryCustomerId(params);
    }
}
