package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Joiner;
import org.shrek.hadata.service.iwms.mapper.*;
import org.shrek.hadata.service.iwms.model.TItemMaster;
import org.shrek.hadata.service.iwms.model.TItemUom;
import org.shrek.hadata.service.iwms.model.TblItemSynWisdom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月21日 10:33
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseMaterielServiceImpl implements MaterielService {

    @Autowired
    TItemMasterMapper tItemMasterMapper;
    @Autowired
    TItemUomMapper tItemUomMapper;
    @Autowired
    TClientMapper tClientMapper;
    @Autowired
    StorageStockMapper storageStockMapper;

    @Override
    @Transactional
    public boolean createMaterials(List<TItemMaster> masters) {
        boolean result = true;
        try {
            for (int i = 0; i < masters.size(); i++) {
                TItemMaster exampleCriteria = new TItemMaster();
                exampleCriteria.setItemNumber(masters.get(i).getItemNumber());
                exampleCriteria.setClientCode(masters.get(i).getClientCode());
                exampleCriteria.setWhId(masters.get(i).getWhId());
                int count = tItemMasterMapper.selectCount(exampleCriteria);
                if (count == 0) {
                    tItemMasterMapper.insertSelective(masters.get(i));
                    List<TItemUom> uoms = masters.get(i).getUoms();
                        for(int y = 0; y < uoms.size(); y++){
                            tItemUomMapper.insertSelective(uoms.get(y));
                    }
                }
            }
        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
        } finally {
            return result;
        }
    }
    @Override
    @Transactional
    public boolean createOrUpdateMaterials(TItemMaster master) {
        boolean result = true;
        try {
                TItemMaster exampleCriteria = new TItemMaster();
                exampleCriteria.setItemNumber(master.getItemNumber());
                exampleCriteria.setClientCode(master.getClientCode());
                exampleCriteria.setWhId(master.getWhId());
                TItemMaster masterCount = tItemMasterMapper.selectOne(exampleCriteria);
                if (masterCount != null) {
                    master.setItemMasterId(masterCount.getItemMasterId());
                    tItemMasterMapper.updateByPrimaryKeySelective(master);
                    List<TItemUom> uoms = master.getUoms();
                    for(int y = 0; y < uoms.size(); y++){
                        TItemUom exampleUom = new TItemUom();
                        exampleUom.setItemNumber(master.getUoms().get(y).getItemNumber());
                        exampleUom.setUom(master.getUoms().get(y).getUom());
                        exampleUom.setWhId(master.getUoms().get(y).getWhId());
                        TItemUom detailCount = tItemUomMapper.selectOne(exampleUom);
                            uoms.get(y).setItemUomId(detailCount.getItemUomId());
                            tItemUomMapper.updateByPrimaryKeySelective(uoms.get(y));

                    }
                }else{
                    tItemMasterMapper.insertSelective(master);
                    List<TItemUom> uoms = master.getUoms();
                    for(int y = 0; y < uoms.size(); y++){
                        tItemUomMapper.insertSelective(uoms.get(y));
                    }
                }
        } catch (Exception e) {
            result = false;
            System.out.println(e.toString());
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
    @Override
    public TItemMaster getItemMasterByWhIdandItemNumber(String whId, String itemNumber) {
        TItemMaster tItemMaster = new TItemMaster();
        tItemMaster.setItemNumber(itemNumber);
        tItemMaster.setWhId(whId);
        return tItemMasterMapper.selectOne(tItemMaster);
    }

    @Override
    public List<TblItemSynWisdom> getTblItemSynWisdom(String whId,Integer synFlag) {
        TblItemSynWisdom tblItemSynWisdom=new TblItemSynWisdom();
        tblItemSynWisdom.setWhId(whId);
        tblItemSynWisdom.setSynFlag(synFlag);
        return storageStockMapper.getTblItemSynWisdom(tblItemSynWisdom);
    }

    @Override
    public List<String> getItemNumberByClientAndWhse(String client, String whse) {
        TItemMaster tItemMaster = new TItemMaster();
        tItemMaster.setWhId(whse);
        tItemMaster.setClientCode(client);
        return tItemMasterMapper.getItemNumberByClientAndWhse(tItemMaster);
    }
}
