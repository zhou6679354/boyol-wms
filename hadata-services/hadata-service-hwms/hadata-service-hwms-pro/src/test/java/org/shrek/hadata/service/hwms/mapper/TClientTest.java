package org.shrek.hadata.service.hwms.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.hwms.Application;
import org.shrek.hadata.service.hwms.model.TClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月10日 18:33
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class TClientTest {
    @Autowired
    private TClientMapper tClientMapper;

    @Test
    public void test() {

    }
}