package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.service.hwms.mapper.TOrderDetailMapper;
import org.shrek.hadata.service.hwms.mapper.TOrderMapper;
import org.shrek.hadata.service.hwms.mapper.TiSendOrderDetailMapper;
import org.shrek.hadata.service.hwms.mapper.TiSendOrderMasterMapper;
import org.shrek.hadata.service.hwms.model.TOrder;
import org.shrek.hadata.service.hwms.model.TiSendOrderDetail;
import org.shrek.hadata.service.hwms.model.TiSendOrderMaster;
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
 * @date 2018年05月11日 06:23
 */
public abstract class AbstractOutBoundService<T> implements OutBoundService<T> {

    @Autowired
    TiSendOrderMasterMapper tiSendOrderMasterMapper;

    @Autowired
    TiSendOrderDetailMapper tiSendOrderDetailMapper;

    @Autowired
    TOrderMapper tOrderMapper;

    @Autowired
    TOrderDetailMapper tOrderDetailMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public boolean createOutBounds(List<TiSendOrderMaster<T>> masters) {

        masters.forEach(master -> {
            tiSendOrderMasterMapper.insertSelective(master);
            if (Optional.ofNullable(master.getExtend()).isPresent() == true) {
                insertExtend(master);
            }
            List<TiSendOrderDetail> details = master.getDetails();
            details.forEach(detail -> {
                detail.setMasterId(master.getId());
                tiSendOrderDetailMapper.insertSelective(detail);
            });
        });

        return true;
    }

    protected abstract void insertExtend(TiSendOrderMaster<T> master);


    @Override
    public int updateOutBoundsBack(HashMap<String, List<String>> params) {
        TOrder tOrder = new TOrder();
        tOrder.setIsSendBack("1");
        params.keySet().forEach(key -> {
            Example example = new Example(TOrder.class);
            example.createCriteria().andIn("orderNumber", params.get(key));
            tOrderMapper.updateByExampleSelective(tOrder, example);

        });
        return 1;
    }
}
