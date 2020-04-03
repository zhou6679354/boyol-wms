package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TranLog;
import java.util.List;


public interface TTranLogMapper extends BasicMapper<TranLog> {
    public List<TranLog> selectTranLogInfo(TranLog tranLog);
}
