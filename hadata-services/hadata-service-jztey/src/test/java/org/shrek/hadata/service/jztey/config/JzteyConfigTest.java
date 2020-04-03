package org.shrek.hadata.service.jztey.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.jztey.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月12日 10:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class JzteyConfigTest {

    @Autowired
    JzteyConfig jzteyConfig;

    @Test
    public void test(){

    }
}