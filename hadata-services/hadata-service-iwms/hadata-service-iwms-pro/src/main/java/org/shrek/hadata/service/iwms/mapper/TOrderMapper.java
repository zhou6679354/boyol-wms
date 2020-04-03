package org.shrek.hadata.service.iwms.mapper;

import org.apache.ibatis.annotations.Param;
import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TOrder;
import org.shrek.hadata.service.iwms.model.TOrderConfirm;

import java.util.List;
import java.util.Map;

/**
 * @author chengjian
 */
public interface TOrderMapper extends BasicMapper<TOrder> {

    public void cancelOutBoundOrder(Map param);
    public List<TOrderConfirm> selectOrderConfirm(Map<String, String> param);
    public int updateOutOrderCancel(TOrder order);
    public void getOrderSerialNumber(Map param);
    public List<TOrder> selectOrderForTMS(TOrder tOrder);
    public List<String> queryOrderNumber(TOrder tOrder);
    public List<TOrder> selectByWhses(@Param("list")List<String> list);
}