package org.shrek.hadata.service.reiley.service.scf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.reiley.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 10:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class SupplyChainFinanceServiceTest {

    @Autowired
    SupplyChainFinanceService supplyChainFinanceService;

    @Test
    public void getInventoryQuantity() throws Exception {
        supplyChainFinanceService.getInventoryQuantity("6001","601","9170038");
    }
}