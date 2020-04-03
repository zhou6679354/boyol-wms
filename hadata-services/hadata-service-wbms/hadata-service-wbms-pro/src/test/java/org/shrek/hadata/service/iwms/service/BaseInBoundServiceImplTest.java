package org.shrek.hadata.service.iwms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.iwms.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by shiqian.zhang on 2018/8/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class BaseInBoundServiceImplTest {
    @Autowired
    InBoundService inBoundService;

    @Test
    public void cancelInBoundOrder() {
//        BaseResponse result =  inBoundService.cancelInBoundOrder("0029125622","401");
//        System.out.println(result);
    }


}