package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.iwms.mapper.TClientRuleMapper;
import org.shrek.hadata.service.iwms.model.TClientRule;
import org.springframework.beans.factory.annotation.Autowired;

@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseClientRuleServiceImpl implements ClientRuleService{
    @Autowired
    TClientRuleMapper tClientRuleMapper;
    @Override
    public int addClientRule(TClientRule clientRule) {
        return tClientRuleMapper.insert(clientRule);
    }
}
