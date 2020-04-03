package org.shrek.hadata.service.jztey.config;

import com.google.common.collect.Maps;
import lombok.Data;
import org.shrek.hadata.service.jztey.model.Platform;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月12日 10:36
 */
@Data
@Configuration
@PropertySource("classpath:jztey.properties")
@ConfigurationProperties(prefix = "jztey")
public class JzteyConfig {
    Map<String, Platform> platforms = Maps.newHashMap();
}
