package org.shrek.hadata.service.reiley.config;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月12日 19:30
 */
@Data
@Configuration
@PropertySource("classpath:reiley-scf.properties")
@ConfigurationProperties(prefix = "reiley.scf")
public class ReileyScfConfig {

    Map<String, String> clients = Maps.newHashMap();
}
