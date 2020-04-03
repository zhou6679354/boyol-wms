package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TblCmRequest;

import java.util.List;

public interface TblCmRequestMapper extends BasicMapper<TblCmRequest> {
    public List<TblCmRequest> selectTblCmOrder();
}