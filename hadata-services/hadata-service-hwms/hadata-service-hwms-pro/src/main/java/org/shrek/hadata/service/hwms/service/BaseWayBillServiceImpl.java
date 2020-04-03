package org.shrek.hadata.service.hwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import org.shrek.hadata.service.hwms.mapper.TblShippingLabelMapper;
import org.shrek.hadata.service.hwms.model.TblShippingLabel;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月11日 12:05
 */
@Service(
        version = "2.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseWayBillServiceImpl implements WayBillService {

    @Autowired
    TblShippingLabelMapper tblShippingLabelMapper;

    @Override
    public void updateWayBillCode(String orderNumber, String whse, String billcode) {
        Example example = new Example(TblShippingLabel.class);
        example.createCriteria()
                .andEqualTo("orderNumber", orderNumber)
                .andEqualTo("whId", whse);
        TblShippingLabel tblShippingLabel = new TblShippingLabel();
        tblShippingLabel.setShipLabelBarcode(billcode);
        tblShippingLabelMapper.updateByExampleSelective(tblShippingLabel, example);
    }
}
