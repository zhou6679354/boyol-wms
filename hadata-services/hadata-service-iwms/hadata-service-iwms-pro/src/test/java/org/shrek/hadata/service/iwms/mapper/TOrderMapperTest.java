package org.shrek.hadata.service.iwms.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.iwms.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月25日 16:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class TOrderMapperTest {

    @Autowired
    TOrderMapper tOrderMapper;

    @Test
    public void test() {
        Map<String, Object> paramterMap = new HashMap<>();
        paramterMap.put("para_in", "123456");
        paramterMap.put("para_out", "");
         tOrderMapper.cancelOutBoundOrder(paramterMap);
        System.out.println(paramterMap);
    }

}