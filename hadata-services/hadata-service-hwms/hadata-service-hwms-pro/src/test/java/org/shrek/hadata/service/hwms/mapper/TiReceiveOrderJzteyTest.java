package org.shrek.hadata.service.hwms.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.shrek.hadata.service.hwms.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月10日 18:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(Application.class)
public class TiReceiveOrderJzteyTest {
    @Autowired
    private TLocationMapper tLocationMapper;

    @Test
    public void test() {
//        String kq = "KF";
//        for (int i = 1; i <= 15; i++) {
//            String xd = String.format("%02d", i);
//            for (int j = 1; j <= 60; j++) {
//                String kw = String.format("%02d", j);
//                String id = kq + xd + "-" + kw;
//                String flow = Joiner.on("").join(11100,xd,0, kw);
////                String flow = "1" + String.format("%09s", s);
//                TLocation location = TLocation.newTLocation(kq, id, flow);
//                tLocationMapper.insertSelective(location);
//            }
//
//        }
    }

    @Test
    public void test02() {
//        String kq = "ZPL02";
//        for (int i = 1; i <= 8; i++) {
//            String xd = String.format("%02d", i);
//            for (int n = 1; n <= 8; n++) {
//                String ce = String.format("%02d", n);
//                for (int j = 1; j <= 10; j++) {
//                    String kw = String.format("%02d", j);
//                    String id = kq + "-"+ xd +ce + kw;
//                    String flow = Joiner.on("").join(32100, xd, n, kw);
////                String flow = "1" + String.format("%09s", s);
//                    TLocation location = TLocation.newTLocation(kq, id, flow);
//                    tLocationMapper.insertSelective(location);
//                }
//            }
//        }
    }
}