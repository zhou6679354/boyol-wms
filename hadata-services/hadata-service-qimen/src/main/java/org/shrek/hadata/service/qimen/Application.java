package org.shrek.hadata.service.qimen;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 奇门接口
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年11月28日 16:49
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>QIMEN 服务启动执行 <<<<<<<<<<<<<");
    }
}
