package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TOrder;
import org.shrek.hadata.service.hwms.model.TOrderConfirm;
import org.shrek.hadata.service.hwms.model.TOrderPackage;
import org.shrek.hadata.service.hwms.model.TOrderPackageItem;

import java.util.List;
import java.util.Map;

public interface TOrderMapper extends BasicMapper<TOrder> {
    public void cancelOutBoundOrder(Map param);

    public List<TOrderConfirm> selectOrderConfirm(Map<String, String> param);
    public List<TOrderPackage> selectOrderPackage(Map<String, String> param);
    public List<TOrderPackageItem> selectOrderPackageItem(Map<String, String> param);
    public int updateOutOrderCancel(TOrder tocOrder);
    public List<String> selectTopOrderByStatus(Map<String, String> param);


}