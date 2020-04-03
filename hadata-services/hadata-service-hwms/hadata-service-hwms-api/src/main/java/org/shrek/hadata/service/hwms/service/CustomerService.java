package org.shrek.hadata.service.hwms.service;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月15日 10:39
 */
public interface CustomerService {

    /**
     * 插叙经销商ID
     *
     * @param sourceCode 来源编码
     * @param client 客户编码
     * @param store 仓库编码
     * @return 经销商ID
     */
    public String queryCustomerId(String sourceCode, String client, String store);
}
