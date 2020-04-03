package org.shrek.hadata.service.jztey;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ImportResource;

/**
 * 九州通
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 14:02
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>JZTEY 服务启动执行 <<<<<<<<<<<<<");
    }
}
