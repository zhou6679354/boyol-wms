package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.iwms.mapper.TClientMapper;
import org.shrek.hadata.service.iwms.model.TClient;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月24日 14:46
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseClientServiceImpl implements ClientService {


    @Autowired
    TClientMapper tClientMapper;

    @Override
    public List<TClient> queryClientByExtCode(String extCode) {
        Example example = new Example(TClient.class);
        example.createCriteria().andEqualTo("extCode", extCode);
        List<TClient> clients = tClientMapper.selectByExample(example);
        return clients;
    }
    @Override
    public List<TClient> queryClientByWh(String whId) {
        TClient client = new TClient();
        client.setWhId(whId);
        return tClientMapper.selectByExample(client);
    }
    @Override
    public TClient getClientByWhAndCode(String whid, String code) {
        TClient client = new TClient();
        if(whid != null){
            client.setWhId(whid);
        }
        client.setClientCode(code);
        TClient nul = tClientMapper.selectOne(client);
        System.out.println(nul);
        return tClientMapper.selectOne(client);
    }

    @Override
    public TClient getClientByWhAndLongCode(String whid, String code) {
        TClient client = new TClient();
        if(whid != null){
            client.setWhId(whid);
        }
        client.setLongClientCode(code);
        return tClientMapper.selectOne(client);
    }

    @Override
    public int updateClientSendControl(String whid, String client, String status) {
        Example example = new Example(TClient.class);
        example.createCriteria()
                .andEqualTo("whId", whid)
                .andEqualTo("clientCode", client);
        TClient tClient = new TClient();
        tClient.setSendControl(status);
        return  tClientMapper.updateByExampleSelective(tClient, example);

    }
    @Override
    public int addClient(TClient client) {
        return tClientMapper.insert(client);
    }
}
