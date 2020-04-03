package org.shrek.hadata.service.jztey.web;

import org.junit.Test;
import org.shrek.hadata.commons.util.JacksonUtil;
import org.shrek.hadata.service.jztey.web.model.RequestInBound;
import org.shrek.hadata.service.jztey.web.model.RequestWrapper;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月15日 12:26
 */

public class JzteyControllerTEST {


    @Test
    public void receiveOutbound() throws Exception {
        String content="<wrapper>\n" +
                "  <root>\n" +
                "    <root>\n" +
                "      <DJBH>TJS00000004</DJBH>\n" +
                "      <DWBH>DWI35969083</DWBH>\n" +
                "      <RQ>2018-05-26</RQ>\n" +
                "      <ZDR>系统管理员</ZDR>\n" +
                "      <BMID>ORG18399726</BMID>\n" +
                "      <DANJLX>食品</DANJLX> ---\n" +
                "      <DJ_SORT>1</DJ_SORT>\n" +
                "      <SPID>SPH93307606</SPID>\n" +
                "      <SHL>49</SHL>\n" +
                "      <JIANSH>1.02</JIANSH>\n" +
                "      <LINGSSHL>1</LINGSSHL>\n" +
                "      <DJ>50</DJ>\n" +
                "      <JE>2450</JE>\n" +
                "      <RKTYPE>正常</RKTYPE>\n" +
                "      <SCF>0</SCF>\n" +
                "      <ZT>N</ZT>\n" +
                "      <YEW_TYPE>11</YEW_TYPE>\n" +
                "      <BRANCHID>FS8</BRANCHID>\n" +
                "      <PIHAO>20180301</PIHAO>\n" +
                "      <BAOZHIQI>2018-03-01</BAOZHIQI>\n" +
                "      <SXRQ>2019-03-31</SXRQ>\n" +
                "      <YSPD />\n" +
                "      <DWID_EJ />   ----\n" +
                "      <JSCKID>CKI00006284</JSCKID>  --\n" +
                "      <STOREID>CKI00005688</STOREID>\n" +
                "    </root>\n" +
                "    <root>\n" +
                "      <DJBH>TJS00000004</DJBH>\n" +
                "      <DWBH>DWI35969083</DWBH>\n" +
                "      <RQ>2018-05-26</RQ>\n" +
                "      <ZDR>系统管理员</ZDR>\n" +
                "      <BMID>ORG18399726</BMID>\n" +
                "      <DANJLX>食品</DANJLX>\n" +
                "      <DJ_SORT>2</DJ_SORT>\n" +
                "      <SPID>SPH92046121</SPID>\n" +
                "      <SHL>1</SHL>\n" +
                "      <JIANSH>0.01</JIANSH>\n" +
                "      <LINGSSHL>1</LINGSSHL>\n" +
                "      <DJ>300</DJ>\n" +
                "      <JE>300</JE>\n" +
                "      <RKTYPE>正常</RKTYPE>\n" +
                "      <SCF>0</SCF>\n" +
                "      <ZT>N</ZT>\n" +
                "      <YEW_TYPE>11</YEW_TYPE>\n" +
                "      <BRANCHID>FS8</BRANCHID>\n" +
                "      <PIHAO>54P802</PIHAO>\n" +
                "      <BAOZHIQI>2018-05-22</BAOZHIQI>\n" +
                "      <SXRQ>2021-05-06</SXRQ>\n" +
                "      <YSPD />\n" +
                "      <DWID_EJ />\n" +
                "      <JSCKID>CKI00006284</JSCKID>\n" +
                "      <STOREID>CKI00005688</STOREID>\n" +
                "    </root>\n" +
                "    <root>\n" +
                "      <DJBH>TJS00000004</DJBH>\n" +
                "      <DWBH>DWI35969083</DWBH>\n" +
                "      <RQ>2018-05-26</RQ>\n" +
                "      <ZDR>系统管理员</ZDR>\n" +
                "      <BMID>ORG18399726</BMID>\n" +
                "      <DANJLX>食品</DANJLX>\n" +
                "      <DJ_SORT>3</DJ_SORT>\n" +
                "      <SPID>SPH93852114</SPID>\n" +
                "      <SHL>3</SHL>\n" +
                "      <JIANSH>0</JIANSH>\n" +
                "      <LINGSSHL>3</LINGSSHL>\n" +
                "      <DJ>2</DJ>\n" +
                "      <JE>6</JE>\n" +
                "      <RKTYPE>正常</RKTYPE>\n" +
                "      <SCF>0</SCF>\n" +
                "      <ZT>N</ZT>\n" +
                "      <YEW_TYPE>11</YEW_TYPE>\n" +
                "      <BRANCHID>FS8</BRANCHID>\n" +
                "      <PIHAO>20200501</PIHAO>\n" +
                "      <BAOZHIQI>2018-05-01</BAOZHIQI>\n" +
                "      <SXRQ>2021-04-15</SXRQ>\n" +
                "      <YSPD />\n" +
                "      <DWID_EJ />\n" +
                "      <JSCKID>CKI00006284</JSCKID>\n" +
                "      <STOREID>CKI00005688</STOREID>\n" +
                "    </root>\n" +
                "  </root>\n" +
                "</wrapper>";
        RequestWrapper<RequestInBound> wrapper = JacksonUtil.nonEmpty(JacksonUtil.Type.XML).fromJson(content, RequestWrapper.class, RequestInBound.class);

        System.out.println(wrapper);
    }

}