package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TiReceiveOrderJztey;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 */
public interface TiReceiveOrderJzteyMapper extends BasicMapper<TiReceiveOrderJztey> {
    public List<HashMap<String, String>> selectInBoundsToJztey(HashMap<String,String> params);
}