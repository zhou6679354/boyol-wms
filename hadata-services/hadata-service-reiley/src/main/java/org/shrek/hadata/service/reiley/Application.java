package org.shrek.hadata.service.reiley;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 日砾
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月20日 13:57
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>REILEY 服务启动执行 <<<<<<<<<<<<<");
    }
}

