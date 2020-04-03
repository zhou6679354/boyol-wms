package org.shrek.hadata.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.opensaml.xml.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
@Slf4j
public class SZSignUtil {
    public static String signTopRequest(Map<String, String> params, String secret, String signMethod) throws Exception {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (signMethod.equals("MD5")) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils
                    .isNotBlank(key) /* && StringUtils.isNotBlank(value) */ ) { //改为允许value为空
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (signMethod.equals("HMAC")) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }
    public static String signSZRequest(String data, String sid,String appid,String timestamp, String nonce,String key) throws Exception {
        String base64Data= Base64.encodeBytes(data.getBytes(),8);
        String md5Str=base64Data+sid+appid+timestamp+nonce+key;
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byte2hex(md.digest(md5Str.getBytes()));
    }
    public static byte[] encryptHMAC(String data, String secret) throws Exception{
        byte[] bytes;
        try{
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes= mac.doFinal(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            log.info("初始化密码器失败:",e.getMessage());
            throw new NoSuchAlgorithmException(e.toString());
        } catch (UnsupportedEncodingException e) {
            log.info("密钥获取字节码失败:",e.getMessage());
            throw new UnsupportedEncodingException(e.toString());
        } catch (InvalidKeyException e) {
            log.info("初始化加密密钥失败:",e.getMessage());
            throw new InvalidKeyException(e.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws  Exception{
        byte[] bytes;
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
             bytes = md.digest(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            log.info("初始化密码器失败:",e.getMessage());
            throw new NoSuchAlgorithmException(e.toString());
        } catch (UnsupportedEncodingException e) {
            log.info("密钥获取字节码失败:",e.getMessage());
            throw new UnsupportedEncodingException(e.toString());
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
}
