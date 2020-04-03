package org.shrek.hadata.gateway.web;

import org.apache.commons.lang.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月15日 16:04
 */
public class GateWayControllerTest {

    public static void main(String[] args) {
        String s = "{\"msgContent\":\"{\\\"ownerUserId\\\":\\\"679439293\\\", \\\"orderCode\\\":\\\"SO60118101510417\\\",\\\"orderType\\\",\\\"12\\\"}\",\"msgType\":\"1\",\"remark\":\"1\",\"msgCode\":\"STOCK_OUT_ORDER_ACCEPT\"}";

        String content = StringEscapeUtils.unescapeJava(s);

        try{
            SimpleDateFormat sdf=new SimpleDateFormat("MMM dd yyyy hh:mm aa");
            Date date=sdf.parse("Oct 12 2018 12:00 AM".toString());
            System.out.println("日期为:"+date);
        }catch (Exception e){
            System.out.println("生产日期赋值异常，原因:"+e.toString());
        }

        System.out.println(content);
    }
}