package org.shrek.hadata.service.iwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.iwms.model.TVendor;


import java.util.List;

public interface TVendorMapper extends BasicMapper<TVendor> {
    public List<String> getVendorCode();
}