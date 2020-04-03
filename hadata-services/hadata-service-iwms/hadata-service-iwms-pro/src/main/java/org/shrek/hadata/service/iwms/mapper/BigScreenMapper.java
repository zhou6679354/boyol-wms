package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.BigScreen;
import org.shrek.hadata.service.iwms.model.BigScreenStorck;
import org.shrek.hadata.service.iwms.model.QueryCity;

import java.util.List;


public interface BigScreenMapper extends BasicMapper<BigScreen> {
    public List<BigScreen> queryBigScreen(QueryCity queryCity);
    public List<BigScreenStorck>  queryBigScreenStorck(QueryCity queryCity);
}
