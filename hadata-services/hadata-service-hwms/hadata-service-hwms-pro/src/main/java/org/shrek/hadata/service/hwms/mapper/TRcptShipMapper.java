package org.shrek.hadata.service.hwms.mapper;

import org.shrek.hadata.commons.mybatis.BasicMapper;
import org.shrek.hadata.service.hwms.model.TRcptShip;

import java.util.Map;

public interface TRcptShipMapper extends BasicMapper<TRcptShip> {
    public void callUspGetNextControlValue(Map param);
}