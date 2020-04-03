package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月03日 23:18
 */
@Data
public class RequestOutBound implements Serializable {
    @JacksonXmlProperty(localName ="STOREID")
    String storeid;
    @JacksonXmlProperty(localName ="DJBH")
    String djbh;
    @JacksonXmlProperty(localName ="DWBH")
    String dwbh;
    @JacksonXmlProperty(localName ="RQ")
    String rq;
    @JacksonXmlProperty(localName ="YWY")
    String ywy;
    @JacksonXmlProperty(localName ="THFS")
    String thfs;
    @JacksonXmlProperty(localName ="BMID")
    String bmid;
    @JacksonXmlProperty(localName ="CKYXJ")
    String ckyxj;
    @JacksonXmlProperty(localName ="THSJ")
    String thsj;
    @JacksonXmlProperty(localName ="SCF")
    String scf;
    @JacksonXmlProperty(localName ="SF_RGSQPH")
    String sfrgsqph;
    @JacksonXmlProperty(localName ="JSLX")
    String jslx;
    @JacksonXmlProperty(localName ="DJBH_BD")
    String djbhbd;
    @JacksonXmlProperty(localName ="DWID_EJ")
    String dwidej;
    @JacksonXmlProperty(localName ="SF_FFCP")
    String sfffcp;
    @JacksonXmlProperty(localName ="KHBZ")
    String khbz;
    @JacksonXmlProperty(localName ="DJ_SORT")
    String djsort;
    @JacksonXmlProperty(localName ="SPID")
    String spid;
    @JacksonXmlProperty(localName ="SHL")
    String shl;
    @JacksonXmlProperty(localName ="JIANSH")
    String jiansh;
    @JacksonXmlProperty(localName ="LINGSSHL")
    String lingsshl;
    @JacksonXmlProperty(localName ="DJ")
    String dj;
    @JacksonXmlProperty(localName ="HSJE")
    String hsje;
    @JacksonXmlProperty(localName ="SHUILV")
    String shuilv;
    @JacksonXmlProperty(localName ="SHUIE")
    String shuie;
    @JacksonXmlProperty(localName ="KOULV")
    String koulv;
    @JacksonXmlProperty(localName ="PH")
    String ph;
    @JacksonXmlProperty(localName ="PHYQ")
    String phyq;
    @JacksonXmlProperty(localName ="SF_CXSP")
    String sfcxsp;
    @JacksonXmlProperty(localName ="CXMS")
    String cxms;
    @JacksonXmlProperty(localName ="DDLX")
    String ddlx;
    @JacksonXmlProperty(localName ="DANJLX")
    String danjlx;
    @JacksonXmlProperty(localName ="XSLX")
    String xslx;
    @JacksonXmlProperty(localName ="PSLX")
    String pslx;
    @JacksonXmlProperty(localName ="DCKHCBJ")
    String dckhcbj;
    @JacksonXmlProperty(localName ="ZT")
    String zt;
    @JacksonXmlProperty(localName ="YEW_TYPE")
    String yewtype;
    @JacksonXmlProperty(localName ="YUANY")
    String yuany;
    @JacksonXmlProperty(localName ="THLB")
    String thlb;
    @JacksonXmlProperty(localName ="QUALITYSTATENAME")
    String qualitystatename;
    @JacksonXmlProperty(localName ="REALPRICE")
    String realprice;
    @JacksonXmlProperty(localName ="BAOZHIQI")
    String baozhiqi;
    @JacksonXmlProperty(localName ="SXRQ")
    String sxrq;
    @JacksonXmlProperty(localName ="PXH")
    String pxh;
    @JacksonXmlProperty(localName ="SJXSKPDH")
    String sjxskpdh;
    @JacksonXmlProperty(localName ="BRANCHID")
    String branchid;
    @JacksonXmlProperty(localName ="JIES_TIME")
    String jiestime;
    @JacksonXmlProperty(localName ="consigneeadd")
    String consigneeadd;
    @JacksonXmlProperty(localName ="CONSIGNEEADD")
    String consigneeadd2;
    @JacksonXmlProperty(localName ="CONTACTPERSON")
    String contactperson;
    @JacksonXmlProperty(localName ="CONTACTPHONE")
    String contactphone;
    @JacksonXmlProperty(localName ="CUSTNO")
    String custno;
    @JacksonXmlProperty(localName ="CUSTNAME")
    String custname;

}
