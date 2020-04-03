package org.shrek.hadata.service.iwms.mapper;

import org.apache.ibatis.annotations.Param;
import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TPoMaster;

import java.util.Date;
import java.util.List;

public interface TPoMasterMapper extends BasicMapper<TPoMaster> {
    int updateInOrderCancel(TPoMaster tPoMaster);
    public List<String> queryPoNumber(TPoMaster tPoMaster);
    public List<TPoMaster> selectByWhses(@Param("list") List<String> list);
}