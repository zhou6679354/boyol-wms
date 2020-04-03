package org.shrek.hadata.commons.util;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.shrek.hadata.commons.Constants;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月08日 09:03
 */
public class SignUtil {

    public static String cainiaoSign(String content, String keys) {
        String sign = "";
        content = content + keys;
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(content.getBytes("utf-8"));
            sign = new String(Base64.encodeBase64(md.digest()), "utf-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sign;
    }
    /**
     * 给TOP请求签名。
     *
     * @param params
     *            所有字符型的TOP请求参数
     * @param body
     *            请求主体内容
     * @param secret
     *            签名密钥
     * @param signMethod
     *            签名方法，目前支持：空（老md5)、md5, hmac_md5三种
     * @return 签名
     */
    public static String qimenSign(Map<String, String> params, String body, String secret, String signMethod)
            throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (Constants.SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtil.areNotEmpty(key, value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：把请求主体拼接在参数后面
        if (body != null) {
            query.append(body);
        }

        // 第四步：使用MD5/HMAC加密
        byte[] bytes;
        if (Constants.SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else if (Constants.SIGN_METHOD_HMAC_SHA256.equals(signMethod)) {
            bytes = encryptHMACSHA256(query.toString(), secret);
        }  else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第五步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }

    /**
     * 给TOP请求签名。
     *
     * @param params
     *            所有字符型的TOP请求参数
     * @param secret
     *            签名密钥
     * @param signMethod
     *            签名方法，目前支持：空（老md5)、md5, hmac_md5三种
     * @return 签名
     */
    public static String hadataSign(Map<String, String> params,  String secret, String signMethod)
            throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (Constants.SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtil.areNotEmpty(key, value)) {
                query.append(key).append(value);
            }
        }

        // 第四步：使用MD5/HMAC加密
        byte[] bytes;
        if (Constants.SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else if (Constants.SIGN_METHOD_HMAC_SHA256.equals(signMethod)) {
            bytes = encryptHMACSHA256(query.toString(), secret);
        }  else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第五步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }
    private static byte[] encryptHMACSHA256(String data, String secret) throws IOException  {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), "HmacSHA256");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(data.getBytes(Constants.CHARSET_UTF8));
    }
    /**
     * 对字节流进行MD5摘要。
     */
    public static byte[] encryptMD5(byte[] data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data);
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }




    public static void main(String[] args) throws IOException {

        HashMap<String, String> params = Maps.newHashMap();
        params.put("method", "deliveryorder.create");
        params.put("format", "xml");
        params.put("app_key", "23036663");
        params.put("v", "2.0");
        params.put("timestamp", "2018-09-30 10:39:18");
        params.put("customerId", "reiley");
        params.put("sign_method", "md5");

        String body="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><deliveryOrder><deliveryOrderCode>R8093000061609</deliveryOrderCode><orderType>JYCK</orderType><warehouseCode>REILEY</warehouseCode><orderFlag></orderFlag><sourcePlatformCode>JD</sourcePlatformCode><sourcePlatformName>京东</sourcePlatformName><createTime>2018-09-30 10:34:15</createTime><placeOrderTime>2018-09-30 10:31:08</placeOrderTime><payTime>2018-09-30 10:31:37</payTime><payNo>80089047907</payNo><operateTime>2018-09-30 10:34:46</operateTime><shopNick>京东自营POP</shopNick><buyerNick>70772985-262591</buyerNick><totalAmount>129.00</totalAmount><itemAmount>129.00</itemAmount><discountAmount>0.00</discountAmount><freight>0.00</freight><arAmount>0</arAmount><gotAmount>129.00</gotAmount><logisticsCode>SF</logisticsCode><logisticsName>顺丰速运</logisticsName><buyerMessage></buyerMessage><expressCode></expressCode><sellerMessage></sellerMessage><remark></remark><senderInfo><name>张三</name><zipCode></zipCode><tel>13774387213</tel><mobile>13774387213</mobile><email></email><province>上海</province><city>上海市</city><area>浦东新区</area><detailAddress>上海上海市浦东新区瑞和路888号</detailAddress></senderInfo><receiverInfo><name>彭天宏</name><zipCode></zipCode><tel>18180789008</tel><mobile>18180789008</mobile><email></email><province>上海</province><city>上海市</city><area>普陀区</area><detailAddress>上海普陀区城区泸定路568号长风汇都3-801</detailAddress></receiverInfo><invoiceFlag>N</invoiceFlag><invoices><invoice><type></type><header></header><amount>129</amount><content></content></invoice></invoices><extendProps><totalPrice>129.00</totalPrice><sellerPrice></sellerPrice><payment>129</payment><shop_code>002</shop_code><promotions/><is_split>0</is_split></extendProps></deliveryOrder><orderLines><orderLine><orderLineNo>9973</orderLineNo><sourceOrderCode>80089047907</sourceOrderCode><subSourceOrderCode></subSourceOrderCode><ownerCode></ownerCode><itemCode>12240271</itemCode><itemId></itemId><itemName>S26 特配  爱儿复  400  罐装</itemName><planQty>1</planQty><actualPrice>129.00</actualPrice><extendProps><sku_id>28448167077</sku_id><jd_price>129.00</jd_price></extendProps></orderLine></orderLines><extendProps><SdId>5</SdId><ReceiverCountry>1</ReceiverCountry><ReceiverProvince>310000</ReceiverProvince><ReceiverCity>310100</ReceiverCity><ReceiverDistrict>310107</ReceiverDistrict><OrderMsg></OrderMsg><StorageMessage></StorageMessage><PayName>网银在线</PayName><PayCode>chinabank</PayCode><PayStatus>2</PayStatus><ShippingTimeTzph>1538274889</ShippingTimeTzph><ShippingTimeJh>0</ShippingTimeJh><IsCod>0</IsCod><CoopModel></CoopModel><shipping_best_time>0</shipping_best_time><cnService></cnService><pushTime></pushTime><esDate></esDate><esRange></esRange><osDate></osDate><osRange></osRange><store_code></store_code><tmallDelivery></tmallDelivery><threePlTiming></threePlTiming><cutoffMinutes></cutoffMinutes><esTime></esTime><deliveryTime></deliveryTime><collectTime></collectTime><sendTime></sendTime><signTime></signTime><deliveryCps></deliveryCps><gatherLastCenter></gatherLastCenter><gatherStation></gatherStation></extendProps></request>";
        String sign = SignUtil.qimenSign(params,body,"bcf66c17d34c0eecd03fe69bc055446a", "md5");
        System.out.println(sign);
    }

}
