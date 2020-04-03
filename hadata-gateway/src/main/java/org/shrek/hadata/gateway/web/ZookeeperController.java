package org.shrek.hadata.gateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 11:47
 */
@RestController
@RequestMapping("/zookeeper")
public class ZookeeperController {

    @Autowired
    DiscoveryClient discoveryClient;

    @RequestMapping(value = "/services", method = {RequestMethod.GET})
    @ResponseBody
    public List<String> services() {
        return  discoveryClient.getServices();
    }

    @RequestMapping(value = "/services/{instance}", method = {RequestMethod.GET})
    @ResponseBody
    public List<String> serviceUrl(@PathVariable String instance) {
        List<ServiceInstance> list = discoveryClient.getInstances(instance);
        List<String> services = new ArrayList<>();
        if (list != null && list.size() > 0 ) {
            list.forEach(serviceInstance -> {
                services.add(serviceInstance.getUri().toString());
            });
        }
        return services;
    }
}
