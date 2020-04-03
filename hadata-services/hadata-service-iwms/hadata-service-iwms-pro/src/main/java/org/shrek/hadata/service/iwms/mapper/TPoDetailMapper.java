package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TPoDetail;

import java.util.List;

public interface TPoDetailMapper extends BasicMapper<TPoDetail> {
    public List<TPoDetail> selectReceivePoDetail(String whse, String poNumber);
}