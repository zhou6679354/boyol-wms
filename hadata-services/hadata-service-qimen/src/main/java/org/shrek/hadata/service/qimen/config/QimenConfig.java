package org.shrek.hadata.service.qimen.config;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月30日 15:37
 */
@Component
@ConfigurationProperties(prefix = "qimen")
@PropertySource("classpath:qimen.properties")
public class QimenConfig {
    @Getter
    @Setter
    Map<String, Qimen> apps = Maps.newHashMap();

    @Data
    public static class Qimen {
        String customer;
        String warehouse;
        String client;
        String owner;
    }
}
