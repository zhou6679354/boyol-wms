package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.commons.util.StringUtil;
import org.shrek.hadata.service.hwms.mapper.TClientMapper;
import org.shrek.hadata.service.hwms.model.TClient;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月13日 21:38
 */
@Service(
        version = "2.0.0",
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
    public TClient queryClient(String extCode, String longClientCode) {
        TClient tClient = new TClient();
        tClient.setExtCode(extCode);
        tClient.setLongClientCode(longClientCode);
        return tClientMapper.selectOne(tClient);
    }

    @Override
    public TClient getClientByWhAndCode(String whid, String code) {
        TClient client = new TClient();
        if(whid != null){
            client.setWhId(whid);
        }
        if(StringUtil.isEmpty(code)){
            code = "";
        }
        client.setClientCode(code);
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
    public TClient getClientByWhAndLongCode(String whId, String code) {
        if(StringUtil.isEmpty(whId)){
            whId = "";
        }
        if(StringUtil.isEmpty(code)){
            code = "";
        }
        TClient client = new TClient();
        client.setWhId(whId);
        client.setLongClientCode(code);
        return tClientMapper.selectOne(client);
    }

}
