package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TCustomer;

import java.util.List;

public interface TCustomerMapper extends BasicMapper<TCustomer> {
    public List<String> getCustomerCodeByWhse(TCustomer customer);
}