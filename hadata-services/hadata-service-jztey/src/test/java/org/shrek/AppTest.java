package org.shrek;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.jztey.web.model.RequestInBound;
import org.shrek.hadata.service.jztey.web.model.RequestWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * Unit test for simple App.
 */
public class AppTest {
    public static void filter(List<String> names, Predicate condition) {
        for (String name : names) {
            if (condition.test(name)) {
                System.out.println(name + " ");
            }
        }
    }

    /**
     * Rigorous Test :-)
     */
    public void shouldAnswerWithTrue() {
        String content = "";
        RequestWrapper<RequestInBound> wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, RequestWrapper.class, RequestInBound.class);

        System.out.println(wrapper);
    }

    @Test
    public void test() throws ParseException {
        String string = "2018-05-13 18-14-10";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        SimpleDateFormat tosdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(tosdf.format(sdf.parse(string)));
    }

    @Test
    public void testOptional() {
        HashMap<String, String> param = Maps.newHashMap();
        param.put("order_code", "1809300006160");
        param.put("whse_code", "601");
        String json=JacksonUtil.nonEmpty().toJson(param);
        System.out.println(json);
    }



}
