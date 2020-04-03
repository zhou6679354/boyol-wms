package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.iwms.mapper.TblMassiveStockOutQuantityMapper;
import org.shrek.hadata.service.iwms.model.TblMassiveStockOutQuantity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 16:07
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class TblMassiveStockOutQuantityServiceImpl implements TblMassiveStockOutQuantityService {

    @Autowired
    TblMassiveStockOutQuantityMapper tblMassiveStockOutQuantityMapper;

    @Override
    public int updateMassiveStockOutQuantity(String client, String whse, int quantity) {

        TblMassiveStockOutQuantity tblMassiveStockOutQuantity = new TblMassiveStockOutQuantity();
        tblMassiveStockOutQuantity.setClientCode(client);
        tblMassiveStockOutQuantity.setWhId(whse);
        TblMassiveStockOutQuantity stockOutQuantity = tblMassiveStockOutQuantityMapper.selectOne(tblMassiveStockOutQuantity);
        if (stockOutQuantity == null) {
            tblMassiveStockOutQuantity.setQuantity(quantity);
            return tblMassiveStockOutQuantityMapper.insert(tblMassiveStockOutQuantity);

        } else {
            tblMassiveStockOutQuantity.setQuantity(quantity);
            return tblMassiveStockOutQuantityMapper.updateByPrimaryKey(tblMassiveStockOutQuantity);
        }
    }
}
