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
 * @date 2018年10月08日 14:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class SupplyChainFinanceScheduledTest {

    @Autowired
    SupplyChainFinanceScheduled supplyChainFinanceScheduled;
    @Test
    public void uploadSku2Cainao() throws Exception {
        supplyChainFinanceScheduled.uploadSku2Cainao();
    }
    @Test
    public void getMassiveStockOutQuantity() throws Exception {
        supplyChainFinanceScheduled.getMassiveStockOutQuantity();
    }

    @Test
    public void uploadStockOutOrder() throws Exception {
        supplyChainFinanceScheduled.uploadStockOutOrder();
    }
    @Test
    public void uploadMassiveStockOut() throws Exception {
        supplyChainFinanceScheduled.uploadMassiveStockOut();
    }
}