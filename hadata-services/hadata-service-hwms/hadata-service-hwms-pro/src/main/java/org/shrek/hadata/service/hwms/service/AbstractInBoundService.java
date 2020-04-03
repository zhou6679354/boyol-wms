package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.service.hwms.mapper.TPoMasterMapper;
import org.shrek.hadata.service.hwms.mapper.TiReceiveOrderDetailMapper;
import org.shrek.hadata.service.hwms.mapper.TiReceiveOrderMasterMapper;
import org.shrek.hadata.service.hwms.model.TPoMaster;
import org.shrek.hadata.service.hwms.model.TiReceiveOrderDetail;
import org.shrek.hadata.service.hwms.model.TiReceiveOrderMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月10日 18:57
 */
public abstract class AbstractInBoundService<T> implements InBoundService<T> {

    @Autowired
    TiReceiveOrderMasterMapper tiReceiveOrderMasterMapper;

    @Autowired
    TiReceiveOrderDetailMapper tiReceiveOrderDetailMapper;

    @Autowired
    TPoMasterMapper tPoMasterMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public boolean createInBounds(List<TiReceiveOrderMaster<T>> masters) {
        masters.forEach(m -> {
            tiReceiveOrderMasterMapper.insertSelective(m);
            Optional.ofNullable(m.getExtend()).ifPresent(value -> {
                insertExtend(m);
            });
            List<TiReceiveOrderDetail> details = m.getDetails();
            details.forEach(detail -> {
                detail.setMasterId(m.getId());
                tiReceiveOrderDetailMapper.insertSelective(detail);
            });
        });
        return true;
    }

    protected abstract void insertExtend(TiReceiveOrderMaster<T> master);

    /**
     * @param params
     * @return
     */
    @Override
    public int updateInBoundsBack(HashMap<String, List<String>> params) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setIsSendBack("1");
        params.keySet().forEach(key -> {
            Example example = new Example(TPoMaster.class);
            example.createCriteria().andIn("poNumber", params.get(key));
            tPoMasterMapper.updateByExampleSelective(tPoMaster, example);

        });
        return 1;
    }

}