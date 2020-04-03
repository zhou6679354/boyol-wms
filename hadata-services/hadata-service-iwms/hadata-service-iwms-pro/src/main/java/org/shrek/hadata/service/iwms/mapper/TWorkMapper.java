package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TWork;

import java.util.List;

public interface TWorkMapper extends BasicMapper<TWork> {
    public List<TWork> queryTWorkByWhId(TWork work);
}
