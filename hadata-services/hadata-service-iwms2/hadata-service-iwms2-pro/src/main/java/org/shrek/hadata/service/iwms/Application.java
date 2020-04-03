package org.shrek.hadata.service.iwms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @author chengjian
 * @IWM数据库操作
 * @date 2018年05月10日 14:25
 */
@MapperScan(basePackages = "org.shrek.hadata.service.*.mapper")
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
        System.out.println(">>>>>>>>>>>>>>>IWMS 服务启动执行 <<<<<<<<<<<<<");
    }
}
