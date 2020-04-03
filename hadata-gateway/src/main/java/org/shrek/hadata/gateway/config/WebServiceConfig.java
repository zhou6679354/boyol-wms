package org.shrek.hadata.gateway.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.shrek.hadata.gateway.webservice.GatewayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月16日 14:09
 */
@Configuration
public class WebServiceConfig {
    @Autowired
    private Bus bus;

    @Autowired
    GatewayServiceImpl gatewayService;

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, gatewayService);
        endpoint.publish("/gateway");
        return endpoint;
    }
}