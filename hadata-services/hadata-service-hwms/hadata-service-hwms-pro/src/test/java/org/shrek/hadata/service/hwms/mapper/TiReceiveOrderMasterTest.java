package org.shrek.hadata.service.hwms.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.hwms.Application;
import org.shrek.hadata.service.hwms.model.TiReceiveOrderMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月10日 21:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class TiReceiveOrderMasterTest {

    @Autowired
    TiReceiveOrderMasterMapper tiReceiveOrderMasterMapper;

    @Test
    public void test(){
        TiReceiveOrderMaster master =new TiReceiveOrderMaster();
        master.setCustCode("11111111");
        tiReceiveOrderMasterMapper.insertSelective(master);
    }



}