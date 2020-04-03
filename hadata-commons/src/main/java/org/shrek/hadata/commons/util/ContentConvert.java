package org.shrek.hadata.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.opensaml.xml.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;

@Slf4j
public class ContentConvert {

	/**
	 * 加密，其中sKey为双方一致的秘钥
	 * @param content 需要加密的文本
	 * @param sKey 双方一致的秘钥
	 * @return 
	 * @throws Exception
	 */
	public static String encryptString(String content, String sKey) throws Exception{
		String resultEncode;
		try{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(sKey.getBytes());
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] encryptResult = cipher.doFinal(byteContent);	//加密后接口
			String result = Base64.encodeBytes(encryptResult);
			resultEncode=URLEncoder.encode(URLEncoder.encode(result,"UTF-8"),"UTF-8");//进行url编码防止http传输过程出现异常
		}catch (NoSuchAlgorithmException e){
			log.info("加密初始化AES加密器失败:",e.getMessage());
			throw new NoSuchAlgorithmException(e.toString());
		}catch (NoSuchPaddingException e){
			log.info("加密初始化补码机制失败:",e.getMessage());
			throw new NoSuchPaddingException(e.toString());
		}catch (UnsupportedEncodingException e){
			log.info("加密密钥获取字节码失败:",e.getMessage());
			throw new UnsupportedEncodingException(e.toString());
		}catch (InvalidKeyException e){
			log.info("加密加载密钥失败:",e.getMessage());
			throw new InvalidKeyException(e.toString());
		}catch (BadPaddingException e){
			log.info("加密算法补码无效:",e.getMessage());
			throw new BadPaddingException(e.toString());
		}catch (IllegalBlockSizeException e){
			log.info("加密正文失败:",e.getMessage());
			throw new IllegalBlockSizeException(e.toString());
		}
		return resultEncode;
	}
	
	
	
	/**
	 * 解密，其中sKey为双方一致的秘钥
	 * @param encryptContent
	 * @param sKey
	 * @return 解密后的信息 String
	 * @throws Exception
	 */
	public static String decryptString(String encryptContent, String sKey) throws Exception{
		String resultStr ;
		try{
			String content = URLDecoder.decode(URLDecoder.decode(encryptContent, "UTF-8"),"UTF-8");//进行url解码
			byte[] contentByte = Base64.decode(content);// 先用base64解密
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random=SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(sKey.getBytes());
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(contentByte);
			resultStr=new String(result,"utf-8"); // 解密
		} catch (NoSuchAlgorithmException e) {
			log.info("解密初始化AES加密器失败:",e.getMessage());
			throw new NoSuchAlgorithmException(e.toString());
		} catch (InvalidKeyException e) {
			log.info("解密加载密钥失败:",e.getMessage());
			throw new InvalidKeyException(e.toString());
		} catch (NoSuchPaddingException e) {
			log.info("解密初始化补码机制失败:",e.getMessage());
			throw new NoSuchPaddingException(e.toString());
		} catch (BadPaddingException e) {
			log.info("解密算法补码无效:",e.getMessage());
			throw new BadPaddingException(e.toString());
		} catch (UnsupportedEncodingException e) {
			log.info("URL解码失败:",e.getMessage());
			throw new UnsupportedEncodingException(e.toString());
		} catch (IllegalBlockSizeException e) {
			log.info("加载正文失败:",e.getMessage());
			throw new IllegalBlockSizeException(e.toString());
		}
		return resultStr;
	 }


	
}
