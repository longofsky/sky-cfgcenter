package com.sky.utils.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES加密解密
 * @author Charlotte
 * @time 2015年6月5日
 */
public class MogoAesUtil {
	private static final Logger logger = LoggerFactory.getLogger(MogoAesUtil.class);
	
	
	private final static String KEY = "8542dges";

	private final static String CHARSET_UTF8 = "UTF-8";

	private MogoAesUtil() {
		throw new IllegalStateException("MogoAesUtil Utility class");
	}
	/**
	 * 加密
	 * @author Charlotte
	 * @time 2015年6月5日
	 * @param content
	 * @return
	 */
	public static String encrypt(String content){ 
	        try {            
	                KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
	                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
	                secureRandom.setSeed(MogoAesUtil.KEY.getBytes());  
	                kgen.init(128, secureRandom); 
	                SecretKey secretKey = kgen.generateKey(); 
	                byte[] enCodeFormat = secretKey.getEncoded(); 
	                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES"); 
	                //创建密码器 
	                Cipher cipher = Cipher.getInstance("AES");
	                // 初始化 
	                cipher.init(Cipher.ENCRYPT_MODE, key);
	                byte[] result = cipher.doFinal(content.getBytes("utf-8")); 
	                return parseByte2HexStr(result); 
	        } catch (Exception e) { 
	        	logger.error(e.toString());
	        }
	        return null; 
	} 

	/**
	 * 解密
	 * @author Charlotte
	 * @time 2015年6月5日
	 * @param content
	 * @return
	 */
	 public static String decrypt(String content) { 
	         try { 
	                  KeyGenerator kgen = KeyGenerator.getInstance("AES"); 
	                  SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
	                  secureRandom.setSeed(MogoAesUtil.KEY.getBytes());  
	                  kgen.init(128, secureRandom);
	                  SecretKey secretKey = kgen.generateKey(); 
	                  byte[] enCodeFormat = secretKey.getEncoded(); 
	                  SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
	               // 创建密码器
	                  Cipher cipher = Cipher.getInstance("AES");
	               // 初始化 
	                 cipher.init(Cipher.DECRYPT_MODE, key);
	                 byte[] result = cipher.doFinal(parseHexStr2Byte(content)); 
	              // 加密 
	                 return new String(result,"utf-8"); 
	         } catch (Exception e) { 
	        	 logger.error(e.toString());
	         }
	         return null; 
	 }
	 /**
	  * 将二进制转换成16进制 
	  * @author Charlotte
	  * @time 2015年6月5日
	  * @param buf
	  * @return
	  */
	 public static String parseByte2HexStr(byte[] buf) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	} 
	 
	 /**
	  * 将16进制转换为二进制 
	  * @author Charlotte
	  * @time 2015年6月5日
	  * @param hexStr
	  * @return
	  */
	 public static byte[] parseHexStr2Byte(String hexStr) {  
	         if (hexStr.length() < 1)  {
	                 return new byte[0];
	         }
	         int halfDivider = 2;
	         byte[] result = new byte[hexStr.length()/halfDivider];  
	         for (int i = 0;i< hexStr.length()/halfDivider; i++) {  
	                 int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                 int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                 result[i] = (byte) (high * 16 + low);  
	         }  
	         return result;  
	 }

	/**
	 * @brief    AES加密
	 * @details  ES加密
	 * @param    key 加密密钥
	 * @param    ivParamter 偏移量
	 * @param    value 需要加密的字符串
	 * @author   huliangjun
	 * @date     2017-07-17 14:43:54
	 * @note     huliangjun@2017-07-17 14:43:59 创建
	 */
	public static String encrypt(String key, String ivParamter, String value) {
//		try {
//			IvParameterSpec iv = new IvParameterSpec(ivParamter.getBytes(CHARSET_UTF8));
//			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET_UTF8), "AES");
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//			byte[] encrypted = cipher.doFinal(value.getBytes());
//			return Base64.encodeBase64String(encrypted);
//		} catch (Exception ex) {
//			logger.error(ex);
//		}
		return null;
	}

	/**
	 * @brief    AES解密
	 * @details  AES解密
	 * @param    key 加密密钥
	 * @param    ivParamter 偏移量
	 * @param    encrypted 需要加密的字符串
	 * @author   huliangjun
	 * @date     2017-07-17 14:43:54
	 * @note     huliangjun@2017-07-17 14:43:59 创建
	 */
	public static String decrypt(String key, String ivParamter, String encrypted) {
//		try {
//			IvParameterSpec iv = new IvParameterSpec(ivParamter.getBytes(CHARSET_UTF8));
//			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET_UTF8), "AES");
//
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
//			return new String(original);
//		} catch (Exception ex) {
//			logger.error(ex);
//		}
		return null;
	}

}
