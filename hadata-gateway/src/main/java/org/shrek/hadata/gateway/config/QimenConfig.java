package org.shrek.hadata.gateway.config;


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
 * @date 2018年11月20日 08:53
 */
@Component
@ConfigurationProperties(prefix = "qimen")
@PropertySource("classpath:qimen.properties")
//@PropertySource("file:qimen.properties")
public class QimenConfig {

    @Getter
    @Setter
    Map<String, Qimen> apps = Maps.newHashMap();

    @Data
    public static class Qimen {
        String appKey;
        String secret;
        String version;
    }
}
