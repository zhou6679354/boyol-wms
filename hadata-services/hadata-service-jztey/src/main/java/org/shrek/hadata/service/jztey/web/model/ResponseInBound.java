package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 07:54
 */
@Data
public class ResponseInBound implements Serializable {
    @JacksonXmlProperty(localName = "CGDD_SORT")
    String lineNumber;
    @JacksonXmlProperty(localName = "Djbh")
    String poNumber;
    @JacksonXmlProperty(localName = "Dwbh")
    String orgCode;
    @JacksonXmlProperty(localName = "Rq")
    String orderDate;
    @JacksonXmlProperty(localName = "SHY")
    String clerk;
    @JacksonXmlProperty(localName = "ZJY")
    String operator;
    @JacksonXmlProperty(localName = "CGY")
    String checker;
    @JacksonXmlProperty(localName = "BMID")
    String deptCode;
    @JacksonXmlProperty(localName = "SCF")
    String uploader;
    @JacksonXmlProperty(localName = "SPID")
    String displayItemNumber;
    @JacksonXmlProperty(localName = "JIANSH")
    String boxNum;
    @JacksonXmlProperty(localName = "LINGSSHL")
    String bulkNum;
    @JacksonXmlProperty(localName = "SHL")
    String qtyReceived;
    @JacksonXmlProperty(localName = "DJ")
    String unitPrice;
    @JacksonXmlProperty(localName = "JE")
    String totalPrice;
    @JacksonXmlProperty(localName = "PH")
    String lotNumber;
    @JacksonXmlProperty(localName = "BAOZHIQI")
    String prodDate;
    @JacksonXmlProperty(localName = "SXRQ")
    String expiraDate;
    @JacksonXmlProperty(localName = "RKTYPE")
    String inboundType;
    @JacksonXmlProperty(localName = "CGDDH")
    String busCode;
    @JacksonXmlProperty(localName = "YEW_TYPE")
    String orderType;
    @JacksonXmlProperty(localName = "PHZT")
    String lotStatus;
    @JacksonXmlProperty(localName = "ZT")
    String status;
    @JacksonXmlProperty(localName = "BRANCHID")
    String branch;
    @JacksonXmlProperty(localName = "STOREID")
    String storeCode;
    @JacksonXmlProperty(localName = "DJ_SORT")
    String receiptIdentity;

    @JsonIgnore
    String whId;

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
        this.busCode = poNumber;
    }


    public String getUploader(){
        return "1";
    }

}
