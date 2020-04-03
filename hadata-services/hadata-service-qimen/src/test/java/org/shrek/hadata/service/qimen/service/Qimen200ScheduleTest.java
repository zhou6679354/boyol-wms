package org.shrek.hadata.service.qimen.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.qimen.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月30日 15:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class Qimen200ScheduleTest {

    @Autowired
    Qimen200Schedule qimen200Schedule;

//    @Test
//    public void feedbackEntryOrderConfirm() throws Exception {
//        qimen200Schedule.feedbackEntryOrderConfirm();
//    }
//
//    @Test
//    public void feedbackOthOrderConfirm() throws Exception {
//        qimen200Schedule.feedbackOthOrderConfirm();
//
//    }
//
//    @Test
//    public void feedbackReturnOrderConfirm() throws Exception {
//        qimen200Schedule.feedbackReturnOrderConfirm();
//
//    }

    @Test
    public void feedbackDeliveryOrderConfirm() throws Exception {
        qimen200Schedule.feedbackDeliveryOrderConfirm();
    }
//
//    @Test
//    public void feedbackStockoutOrderConfirm() throws Exception {
//        qimen200Schedule.feedbackStockoutOrderConfirm();
//    }

}