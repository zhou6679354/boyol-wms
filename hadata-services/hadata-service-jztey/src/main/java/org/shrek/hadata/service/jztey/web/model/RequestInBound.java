package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月03日 23:17
 */
@Data
public class RequestInBound implements Serializable{
    @JacksonXmlProperty(localName ="STOREID")
    String storeid;
    @JacksonXmlProperty(localName ="DJBH")
    String djbh;
    @JacksonXmlProperty(localName ="DWBH")
    String dwbh;
    @JacksonXmlProperty(localName ="RQ")
    String rq;
    @JacksonXmlProperty(localName ="LXR")
    String lxr;
    @JacksonXmlProperty(localName ="YDHRQ")
    String ydhrq;
    @JacksonXmlProperty(localName ="YWY")
    String ywy;
    @JacksonXmlProperty(localName ="USERNAME")
    String username;
    @JacksonXmlProperty(localName ="BMID")
    String bmid;
    @JacksonXmlProperty(localName ="LXDH")
    String lxdh;
    @JacksonXmlProperty(localName ="SF_ZY")
    String sfzy;
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
    @JacksonXmlProperty(localName ="JE")
    String je;
    @JacksonXmlProperty(localName ="RKTYPE")
    String rktype;
    @JacksonXmlProperty(localName ="SCF")
    String scf;
    @JacksonXmlProperty(localName ="THLB")
    String thlb;
    @JacksonXmlProperty(localName ="PIHAO")
    String pihao;
    @JacksonXmlProperty(localName ="BAOZHIQI")
    String baozhiqi;
    @JacksonXmlProperty(localName ="SXRQ")
    String sxrq;
    @JacksonXmlProperty(localName ="YSPD")
    String yspd;
    @JacksonXmlProperty(localName ="YUANY")
    String yuany;
    @JacksonXmlProperty(localName ="YEW_TYPE")
    String yewtype;
    @JacksonXmlProperty(localName ="ZT")
    String zt;
    @JacksonXmlProperty(localName ="BRANCHID")
    String branchid;
    @JacksonXmlProperty(localName ="ISZDYS")
    String iszdys;
    @JacksonXmlProperty(localName ="ZHIJREN")
    String zhijren;
}
