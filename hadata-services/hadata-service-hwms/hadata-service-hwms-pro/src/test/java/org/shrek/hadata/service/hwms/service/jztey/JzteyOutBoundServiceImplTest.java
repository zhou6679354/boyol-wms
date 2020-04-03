package org.shrek.hadata.service.hwms.service.jztey;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.hwms.Application;
import org.shrek.hadata.service.hwms.mapper.TOrderMapper;
import org.shrek.hadata.service.hwms.model.TOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年07月04日 11:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class JzteyOutBoundServiceImplTest {
    @Autowired
    TOrderMapper tOrderMapper;
    @Test
    public void test(){
        TOrder tOrder = new TOrder();
        tOrder.setIsSendBack("1");
        Example example = new Example(TOrder.class);
        example.createCriteria().andEqualTo("orderNumber", "XSG00035239");
        tOrderMapper.updateByExampleSelective(tOrder, example);
    }



}