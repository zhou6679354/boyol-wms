package org.shrek.hadata.service.jztey.web.model;

import com.google.common.collect.Maps;
import org.shrek.hadata.commons.util.BeanUtil;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.commons.util.MapUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 08:05
 */
public class InBoundReturnTest {

    public static void main(String[] args) {
        ResponseInBound boundReturn=new ResponseInBound();
        HashMap<String, String> map = Maps.newHashMap();
        map.put("total_price", "808.08");
        map.put("box_num", "0.5");
        map.put("unit_price", "134.68");
        map.put("display_item_number", "SPH92268757");
        map.put("lot_status", "合格");
        map.put("lot_number", "70370017A1");
        map.put("prod_date", "01/01/1900");
        map.put("bulk_num", "6.0");
        map.put("po_number", "XTK00000575");
        map.put("line_number", "1");
        map.put("qty_received", "6.0");
        map.put("expira_date", "2019-02-06");
        map.put("status", "N");

        try {
            BeanUtil.populate(boundReturn, map);
            MapUtil.toObject(boundReturn, map, true);
            ResponseWrapper<ResponseInBound> wrapper = new ResponseWrapper();
            wrapper.getList().add(boundReturn);
            System.out.println(JacksonUtil.nonEmpty(JacksonUtil.Type.XML).toJson(wrapper));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}