package org.shrek.hadata.service.iwms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.iwms.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月07日 09:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class BaseStoreServiceImplTest {

    @Autowired
    StoreService storeService;

    @Test
    public void queryCsfStoreInfo() throws Exception {
        BaseResponse<HashMap<String, Double>> response = storeService.queryCsfStoreInfo("6001", "601", "12281199");
        System.out.println(response);
    }

}