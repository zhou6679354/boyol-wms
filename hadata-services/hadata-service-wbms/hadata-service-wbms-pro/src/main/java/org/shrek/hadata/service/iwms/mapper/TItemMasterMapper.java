package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TItemMaster;

import java.util.List;

public interface TItemMasterMapper extends BasicMapper<TItemMaster> {
    public List<String> getItemNumberByClientAndWhse(TItemMaster itemMaster);
}