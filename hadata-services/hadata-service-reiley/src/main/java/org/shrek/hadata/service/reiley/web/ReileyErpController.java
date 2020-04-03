package org.shrek.hadata.service.reiley.web;

import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.reiley.service.erp.ReileyErpB2CService;
import org.shrek.hadata.service.reiley.service.erp.ReileyErpService;
import org.shrek.hadata.service.reiley.service.erp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年11月26日 15:28
 */
@Slf4j
@RestController
@RequestMapping
public class ReileyErpController {

    @Autowired
    ReileyErpB2CService reileyErpB2CService;
    @Autowired
    ReileyErpService reileyErpService;
    /**
     * B2C退货入库单创建接口
     *
     * @param content
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/AddMaterial",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse createMaterial(@RequestBody String content) {
        List<ErpMaterialRequest> materials = JacksonUtil.nonEmpty().fromJson(content, List.class, ErpMaterialRequest.class);
       BaseResponse response= reileyErpService.createMaterial(materials);
        if(response.isFlag()) {
            return ErpMaterialResponse.success();
        }else{
            return ErpMaterialResponse.fail(1,response.getSubMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/AddNoticeIn",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse createInBound(@RequestBody String content) {
        ErpInBoundRequest erpInBoundRequest = JacksonUtil.nonEmpty().fromJson(content, ErpInBoundRequest.class);
        BaseResponse response= reileyErpService.createErpInBound(erpInBoundRequest);
        if(response.isFlag()) {
            return ErpMaterialResponse.success();
        }else{
            return ErpMaterialResponse.fail(1,response.getSubMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/AddNoticeOut",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse createOutBound(@RequestBody String content) {
        ErpOutBoundRequest erpOutBoundRequest = JacksonUtil.nonEmpty().fromJson(content, ErpOutBoundRequest.class);
        BaseResponse response= reileyErpService.createErpOutBound(erpOutBoundRequest);
        if(response.isFlag()) {
            return ErpMaterialResponse.success();
        }else{
            return ErpMaterialResponse.fail(1,response.getSubMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/StockCancel",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse cancelOrder(@RequestBody String content) {
        ErpCancelInBoundRequest erpCancelInBoundRequest = JacksonUtil.nonEmpty().fromJson(content, ErpCancelInBoundRequest.class);
        BaseResponse response= reileyErpService.cancelOrder(erpCancelInBoundRequest);
        if(response.isFlag()) {
            return ErpMaterialResponse.success();
        }else{
            return ErpMaterialResponse.fail(1,response.getSubMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/AddNoticeAllocate",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse noticeAllocate(@RequestBody String content) {
        ErpNoticeAllocateRequest erpNoticeAllocateRequest = JacksonUtil.nonEmpty().fromJson(content, ErpNoticeAllocateRequest.class);
        BaseResponse response = new BaseResponse();
        if("B2B".equals(erpNoticeAllocateRequest.getBilltypeid())){
            response=reileyErpService.noticeAllocateOut(erpNoticeAllocateRequest);
            if (response.isFlag()) {
                response=reileyErpService.noticeAllocateIn(erpNoticeAllocateRequest);
                if (response.isFlag()) {
                    return ErpMaterialResponse.success();
                }else{
                    return ErpMaterialResponse.fail(1,response.getSubMessage());
                }
            }else{
                return ErpMaterialResponse.fail(1,response.getSubMessage());
            }
        }else if("B2C".equals(erpNoticeAllocateRequest.getBilltypeid())){
            response=reileyErpService.noticeAllocateOut(erpNoticeAllocateRequest);
            if (response.isFlag()) {
                response=reileyErpB2CService.noticeAllocateIn(erpNoticeAllocateRequest);
                if (response.isFlag()) {
                    return ErpMaterialResponse.success();
                }else{
                    return ErpMaterialResponse.fail(1,response.getSubMessage());
                }
            }else{
                return ErpMaterialResponse.fail(1,response.getSubMessage());
            }
        }else if("C2C".equals(erpNoticeAllocateRequest.getBilltypeid())){
            response=reileyErpB2CService.noticeAllocateOut(erpNoticeAllocateRequest);
            if (response.isFlag()) {
                response=reileyErpB2CService.noticeAllocateIn(erpNoticeAllocateRequest);
                if (response.isFlag()) {
                    return ErpMaterialResponse.success();
                }else{
                    return ErpMaterialResponse.fail(1,response.getSubMessage());
                }
            }else{
                return ErpMaterialResponse.fail(1,response.getSubMessage());
            }
        }else if("C2B".equals(erpNoticeAllocateRequest.getBilltypeid())){
            response=reileyErpB2CService.noticeAllocateOut(erpNoticeAllocateRequest);
            if (response.isFlag()) {
                response=reileyErpService.noticeAllocateIn(erpNoticeAllocateRequest);
                if (response.isFlag()) {
                    return ErpMaterialResponse.success();
                }else{
                    return ErpMaterialResponse.fail(1,response.getSubMessage());
                }
            }else{
                return ErpMaterialResponse.fail(1,response.getSubMessage());
            }
        }else{
            //return ErpMaterialResponse.fail(1,"billtypeid无法识别");
            response=reileyErpService.noticeAllocateOut(erpNoticeAllocateRequest);
            if (response.isFlag()) {
                response=reileyErpService.noticeAllocateIn(erpNoticeAllocateRequest);
                if (response.isFlag()) {
                    return ErpMaterialResponse.success();
                }else{
                    return ErpMaterialResponse.fail(1,response.getSubMessage());
                }
            }else{
                return ErpMaterialResponse.fail(1,response.getSubMessage());
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/AddNoticeInReturn",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse deliveryInReturn(@RequestBody String content) {
        ErpOutBoundRequest erpOutBoundRequest = JacksonUtil.nonEmpty().fromJson(content, ErpOutBoundRequest.class);
        erpOutBoundRequest.setBilltypeid("CGTH");
        BaseResponse response= reileyErpService.createErpOutBound(erpOutBoundRequest);
        if(response.isFlag()) {
            return ErpMaterialResponse.success();
        }else{
            return ErpMaterialResponse.fail(1,response.getSubMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/AddNoticeOutReturn",produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ErpMaterialResponse deliveryOutReturn(@RequestBody String content) {
        ErpInBoundRequest erpInBoundRequest = JacksonUtil.nonEmpty().fromJson(content, ErpInBoundRequest.class);
        erpInBoundRequest.setBilltypeid("XSTH");
        BaseResponse response= reileyErpService.createErpInBound(erpInBoundRequest);
        if(response.isFlag()) {
            return ErpMaterialResponse.success();
        }else{
            return ErpMaterialResponse.fail(1,response.getSubMessage());
        }
    }


}
