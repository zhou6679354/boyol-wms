package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TWaveMaster;

import java.util.List;
import java.util.Map;

public interface TWaveMasterMapper extends BasicMapper<TWaveMaster> {
    public int updateWaIdStatusTrue(Map map);
    public int updateWaveCancelStatusDoing(Map map);
    List<String> queryWaIdByWhId(Map map);
}
