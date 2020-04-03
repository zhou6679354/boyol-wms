package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TPickDetail;

import java.util.List;

public interface TPickDetailMapper extends BasicMapper<TPickDetail> {
    public int updateTPickDetailByOrderNumberAndWhId(TPickDetail pickDetail);
}
