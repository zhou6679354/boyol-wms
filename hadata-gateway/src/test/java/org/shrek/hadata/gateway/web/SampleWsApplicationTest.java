package org.shrek.hadata.gateway.web;

import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.shrek.hadata.gateway.webservice.model.WebServiceRequest;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月14日 13:05
 */
public class SampleWsApplicationTest {


    public static void main(String[] args) throws Exception {
        JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client = factory.createClient("http://127.0.0.1:8080/ws/gateway?wsdl");

        WebServiceRequest request = new WebServiceRequest();
        request.setReqId("11111111111");
        Object[] result = client.invoke("execute", request);
        System.out.println(result);
    }
}
