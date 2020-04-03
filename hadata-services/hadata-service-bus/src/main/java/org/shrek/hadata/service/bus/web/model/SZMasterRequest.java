package org.shrek.hadata.service.bus.web.model;

import lombok.Data;

import java.util.List;

@Data
public class SZMasterRequest {
    //产品代码
    private String itemNumber;
    //产品名称
    private String description;
    //计量单位
    private String uom;
    //仓库编码
    private String whId;
    //货主ID
    private String clientCode;
    //单价
    private double price;
    //单件体积
    private double unitVolume;
    //外包装体积
    private double nestedVolume;
    //长
    private double length;
    //宽
    private double width;
    //高
    private double height;
    private Dtl dtl;
    @Data
    public class Dtl{
        //产品代码
        private String itemNumber;
        //单为
        private String uom;
        //箱规
        private Double conversionFactor;
        //仓库代码
        private String whId;
        //单位中文
        private String uomPrompt;
    }
}
