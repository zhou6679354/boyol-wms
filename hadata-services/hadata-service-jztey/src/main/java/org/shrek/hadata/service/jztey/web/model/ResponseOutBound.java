package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 23:51
 */
@Data
public class ResponseOutBound implements Serializable {

    @JacksonXmlProperty(localName = "DJBH")
    String orderNumber;
    @JacksonXmlProperty(localName = "DWBH")
    String unitInCode;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @JacksonXmlProperty(localName = "RQ")
    String orderDate;
    @JacksonXmlProperty(localName = "BMID")
    String depInCode;
    @JacksonXmlProperty(localName = "YWY")
    String saleName;
    @JacksonXmlProperty(localName = "SCF")
    String uploader;
    @JacksonXmlProperty(localName = "SPID")
    String altItemNumber;
    @JacksonXmlProperty(localName = "SHL")
    String tranQty;
    //TODO
    @JacksonXmlProperty(localName = "JIANSH")
    String JIANSH;
    @JacksonXmlProperty(localName = "PH")
    String lotNumber;
    @JacksonXmlProperty(localName = "DJ")
    String unitPrice;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @JacksonXmlProperty(localName = "BAOZHIQI")
    String prodDate;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @JacksonXmlProperty(localName = "SXRQ")
    String expDate;
    @JacksonXmlProperty(localName = "YEW_TYPE")
    String orderType;
    @JacksonXmlProperty(localName = "SF_ZP")
    String ifGift;
    @JacksonXmlProperty(localName = "SF_CH")
    String ifRedFlush;
    @JacksonXmlProperty(localName = "PHX")
    String boxNumber;
    @JacksonXmlProperty(localName = "BRANCHID")
    String extCode;
    @JacksonXmlProperty(localName = "STOREID")
    String longClientCode;
    @JacksonXmlProperty(localName = "DDLX")
    String orderTypeDesc;
    @JacksonXmlProperty(localName = "MINAREACODE")
    String minAreaCode;
    @JacksonXmlProperty(localName = "MAXAREACODE")
    String maxAreaCode;
    @JacksonXmlProperty(localName = "RY_FUHY")
    String ryFuhy;
    @JacksonXmlProperty(localName = "ANTILOTNO")
    String antilotno;
    @JacksonXmlProperty(localName = "DJ_SORT")
    String lineNumber;
    @JacksonXmlProperty(localName = "ZT")
    String zt;

    public String getIfGift(){
        return Optional.ofNullable(this.ifGift).orElse("否") ;
    }
    public String getIfRedFlush(){
        return Optional.ofNullable(this.ifRedFlush).orElse("否") ;
    }
    public String getBoxNumber(){
        return Optional.ofNullable(this.boxNumber).orElse("") ;
    }
    public String getMinAreaCode(){
        return Optional.ofNullable(this.minAreaCode).orElse("") ;
    }
    public String getMaxAreaCode(){
        return Optional.ofNullable(this.maxAreaCode).orElse("") ;
    }
    public String getRyFuhy(){
        return Optional.ofNullable(this.ryFuhy).orElse("") ;
    }
    public String getAntilotno(){
        return Optional.ofNullable(this.antilotno).orElse("") ;
    }
    public String getUploader(){
        return "1";
    }
    public String getZt(){
        return Optional.ofNullable(this.zt).orElse("N");
    }
}
