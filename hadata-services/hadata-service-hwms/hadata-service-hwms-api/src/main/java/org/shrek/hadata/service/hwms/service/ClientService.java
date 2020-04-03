package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.service.hwms.model.TClient;

import java.util.List;

/**
 * 货主Service
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月13日 21:37
 */
public interface ClientService {

    /**
     * 根据平台编码查询货主清单
     * <p>
     * 平台:仓库:货主 = 1:N:N,
     *
     * @param extCode
     * @return
     */
    public List<TClient> queryClientByExtCode(String extCode);


    /**
     * 根据来源客户编码，平台编码查询客户
     *
     * @return
     */
    public TClient queryClient(String extCode, String longClientCode);


    public TClient getClientByWhAndCode(String whid, String code);

    public int updateClientSendControl(String whid, String client, String status);

    public TClient getClientByWhAndLongCode(String whId, String code);
}
