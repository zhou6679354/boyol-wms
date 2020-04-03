package org.shrek;

import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.reiley.service.erp.model.ErpMaterialRequest;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月11日 15:28
 */
public class CainiaoTest {


    public static void main(String[] args) {
//        ErpStoreResponse.StoreItem storeItem = new ErpStoreResponse.StoreItem();
//        storeItem.setItemCode("0020081");
//        storeItem.setBatchCode("111111");
//        storeItem.setQty(6);
//        storeItem.setBoxQty(1);
//        storeItem.setStore("666");
//
//        ErpStoreResponse.StoreItem storeItem1 = new ErpStoreResponse.StoreItem();
//        storeItem1.setItemCode("0020082");
//        storeItem1.setBatchCode("惠氏S-26金装膳儿加3阶段@4x1.2kg（盒装）");
//        storeItem1.setQty(6);
//        storeItem1.setBoxQty(1);
//        storeItem1.setStore("666");
//        List<ErpStoreResponse.StoreItem> items = Lists.newArrayList(storeItem, storeItem1);
//        ErpStoreResponse response = new ErpStoreResponse();
//        response.setStatus(0);
//        response.setMessage("");
//        response.setItems(items);
//        String json = JacksonUtil.nonAlways().toJson(response);
//        System.out.println(json);

        String content="[\n" +
                "    {\n" +
                "        \"code\": \"11490251\", \n" +
                "        \"exp_date\": 720, \n" +
                "        \"ie_type\": 1, \n" +
                "        \"name\": \"惠氏S-26金装膳儿加3阶段@12x400g（罐装）\", \n" +
                "        \"pcs\": 12, \n" +
                "        \"sn\": \"8886472100071\", \n" +
                "        \"spec\": \"1*12\", \n" +
                "        \"trail1\": 1, \n" +
                "        \"trail2\": 1, \n" +
                "        \"trail3\": 1, \n" +
                "        \"type\": 1, \n" +
                "        \"util\": \"EA\", \n" +
                "        \"volume\": 0, \n" +
                "        \"weight\": 0\n" +
                "    }, \n" +
                "    {\n" +
                "        \"code\": \"11490252\", \n" +
                "        \"exp_date\": 180, \n" +
                "        \"ie_type\": 1, \n" +
                "        \"name\": \"惠氏S-26金装膳儿加3阶段@4x1.2kg（盒装）\", \n" +
                "        \"pcs\": 12, \n" +
                "        \"sn\": \"8886472100088\", \n" +
                "        \"spec\": \"1*12\", \n" +
                "        \"trail1\": 1, \n" +
                "        \"trail2\": 1, \n" +
                "        \"trail3\": 1, \n" +
                "        \"type\": 1, \n" +
                "        \"util\": \"EA\", \n" +
                "        \"volume\": 0, \n" +
                "        \"weight\": 0\n" +
                "    }\n" +
                "]";
        List<ErpMaterialRequest> materials = JacksonUtil.nonEmpty().fromJson(content, List.class, ErpMaterialRequest.class);
        System.out.println(materials);
    }
}
