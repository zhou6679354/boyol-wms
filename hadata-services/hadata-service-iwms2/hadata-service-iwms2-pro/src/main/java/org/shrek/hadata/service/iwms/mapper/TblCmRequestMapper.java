package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TblCmRequest;

import java.util.List;
import java.util.Map;

public interface TblCmRequestMapper extends BasicMapper<TblCmRequest> {
    public List<TblCmRequest> selectTblCmOrder();
}