package org.shrek.hadata.service.hwms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月08日 13:23
 */
@tk.mybatis.spring.annotation.MapperScan(basePackages = "org.shrek.hadata.service.*.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>HWMS 服务启动执行 <<<<<<<<<<<<<");
    }
}
