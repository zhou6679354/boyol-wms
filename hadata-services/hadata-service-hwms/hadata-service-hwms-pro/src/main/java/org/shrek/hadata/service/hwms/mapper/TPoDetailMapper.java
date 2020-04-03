package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TOrderConfirm;
import org.shrek.hadata.service.hwms.model.TPoDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TPoDetailMapper extends BasicMapper<TPoDetail> {
    public List<TPoDetail> selectInBoundsByOutB2C(HashMap<String, Object> param);
    public List<TPoDetail> selectOutBoundsByOutB2CT(HashMap<String, Object> param);
    public List<TPoDetail> selectOutBoundsByOutB2C(HashMap<String, Object> param);
    public List<TPoDetail> selectInBoundsByOutB2CT(HashMap<String, Object> param);
}