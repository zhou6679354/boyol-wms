package org.shrek.hadata.service.reiley.service.erp.model;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月27日 16:17
 */
public class ErpMaterialResponse extends ErpBaseResponse {

    public static ErpMaterialResponse success(){
        ErpMaterialResponse response = new ErpMaterialResponse();
        response.setStatus(0);
        response.setMessage("");
        return response;
    }

    public static ErpMaterialResponse fail(int status,String message){
        ErpMaterialResponse response = new ErpMaterialResponse();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }
}
