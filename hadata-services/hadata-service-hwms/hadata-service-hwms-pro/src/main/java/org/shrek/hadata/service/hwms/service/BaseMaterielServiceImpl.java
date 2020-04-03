package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.service.hwms.mapper.TClientMapper;
import org.shrek.hadata.service.hwms.mapper.TItemMasterMapper;
import org.shrek.hadata.service.hwms.mapper.TItemUomMapper;
import org.shrek.hadata.service.hwms.model.TClient;
import org.shrek.hadata.service.hwms.model.TItemMaster;
import org.shrek.hadata.service.hwms.model.TItemUom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 06:47
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
@Slf4j
public class BaseMaterielServiceImpl implements MaterielService {

    @Autowired
    TItemMasterMapper tItemMasterMapper;
    @Autowired
    TItemUomMapper tItemUomMapper;
    @Autowired
    TClientMapper tClientMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public boolean createMaterials(List<TItemMaster> masters) {
        boolean result = true;
        try {
            masters.forEach(master -> {

                String itemNumber = master.getItemNumber();
                List<TClient> clients = master.getClients();
                clients.forEach(client -> {
                    try {
                        String itemCode = Joiner.on('-').join(client.getClientCode(), itemNumber);
                        TItemMaster exampleCriteria = new TItemMaster();
                        exampleCriteria.setItemNumber(itemCode);
                        exampleCriteria.setClientCode(client.getClientCode());
                        exampleCriteria.setWhId(client.getWhId());
                        int count = tItemMasterMapper.selectCount(exampleCriteria);
                        if (count == 0) {
                            master.setWhId(client.getWhId());
                            master.setClientCode(client.getClientCode());
                            master.setItemNumber(itemCode);
                            tItemMasterMapper.insertSelective(master);
                            List<TItemUom> uoms = master.getUoms();
                            if (Optional.ofNullable(uoms).isPresent() == true) {
                                uoms.forEach(uom -> {
                                    uom.setWhId(client.getWhId());
                                    uom.setItemNumber(itemCode);
                                    uom.setItemMasterId(master.getItemMasterId());
                                    tItemUomMapper.insertSelective(uom);
                                });
                            }
                        }
                    } catch (Exception e) {
                        log.warn(e.getMessage());
                    }
                });
            });
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            return result;
        }

    }

    @Override
    public List<TItemMaster> getItemsByClientAndWhse(String client, String whse) {
        TItemMaster tItemMaster = new TItemMaster();
        tItemMaster.setWhId(whse);
        tItemMaster.setClientCode(client);
        return tItemMasterMapper.select(tItemMaster);
    }

    @Override
    public TItemMaster getItemByCode(String code, String client) {
        TItemMaster tItemMaster = new TItemMaster();
        tItemMaster.setItemNumber(Joiner.on("-").join(client, code));
        tItemMaster.setClientCode(client);
        return tItemMasterMapper.selectOne(tItemMaster);
    }
}
