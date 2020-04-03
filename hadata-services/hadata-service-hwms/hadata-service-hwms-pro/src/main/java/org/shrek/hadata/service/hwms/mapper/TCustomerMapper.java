package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TCustomer;

import java.util.HashMap;

/**
 * @author chengjian
 */
public interface TCustomerMapper extends BasicMapper<TCustomer> {
    public String queryCustomerId(HashMap<String, String> params);
}