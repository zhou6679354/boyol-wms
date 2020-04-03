package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TiSendOrderJztey;

import java.util.HashMap;
import java.util.List;

public interface TiSendOrderJzteyMapper extends BasicMapper<TiSendOrderJztey> {
    public List<HashMap<String, String>> selectOutBoundsToJztey(HashMap<String,String> params);
}