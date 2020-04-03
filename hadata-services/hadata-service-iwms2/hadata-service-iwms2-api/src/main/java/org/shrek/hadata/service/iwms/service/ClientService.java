package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.service.iwms.model.TClient;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 14:44
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


    public TClient getClientByWhAndCode(String whid, String code);
    public int addClient(TClient client);
    public TClient getClientByWhAndLongCode(String whid, String code);
    public List<TClient> queryClientByWh(String whId);
    public int updateClientSendControl(String whid, String client, String status);
}
