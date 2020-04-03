package org.shrek.hadata.service.reiley.schedule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.reiley.Application;
import org.shrek.hadata.service.reiley.service.erp.WmsToErpB2CScheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年12月21日 16:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class ReileyScheduleTest {

    @Autowired
    ReileySchedule reileySchedule;
    @Autowired
    WmsToErpB2CScheduled wmsToErpB2CScheduled;

    @Test
    public void feedbackDeliveryOrderConfirm() {
        wmsToErpB2CScheduled.feedbackInBoundResponse();

    }
}