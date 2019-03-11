package com.adachina.cfgcenter.utils.secure;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5摘要常用类,并兼容老版本的 MD5Utils
 * 常用方法为：
 * 	md5Hex("123456") --> e10adc3949ba59abbe56e057f20f883e
 * @brief  MD5摘要常用类
 * @author 戴德荣
 * @note 戴德荣 @ 2017-12-10 创建
 * @since verson / 2017-12-10
 */
public class Md5Utils {
	
	private Md5Utils() {
        throw new IllegalStateException("Md5Utils Utility class");
	}
	
	/**
	 * 16进制的字符0-9，A-F
	 */
    private static final String HEX_CHARS = "0123456789abcdef";
	
    /**
     *  返回一个md5加密的 MessageDigest 实例
     * @return 返回一个md5加密的 MessageDigest 实例
     * @throws RuntimeException 
     *   如果抛出一个 NoSuchAlgorithmException  {@link NoSuchAlgorithmException}
     *              时将转换成  RuntimeException
     */
    private static MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
	/**
     * 转换 byte数组成 一个16进制的String
     * eg:
     *  byte [] bs 
     *  bytesToHexString(bs);
     * @param b - 二进制数组
     * @author 转换成的16进制数组
     */
    public static  String bytesToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_CHARS.charAt(b[i] >>> 4 & 0x0F));
            sb.append(HEX_CHARS.charAt(b[i] & 0x0F));
        }
        return sb.toString();
    }

    /**
     * 将十六进制字符串转换为字节数组。
     * 注意如果包含非16进制字符，转换出来的结果将不正确
     * eg:  hexStringToByteArray("FABCDEF")    
     * @param s - 16进制字符串
     * @return byte[] 二进制数组
     */
    public static  byte[] hexStringToByteArray(String s) {
        byte[] buf = new byte[s.length() / 2];
        int j = 0;
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) ((Character.digit(s.charAt(j++), 16) << 4) | Character
                    .digit(s.charAt(j++), 16));
        }
        return buf;
    }

    /**
     * 计算MD5摘要
     * @param data 需要md5摘要的byte数组数据
     * @return byte[] MD5摘要
     */
    public static  byte[] md5(byte[] data) {
        return getDigest().digest(data);
    }

    /**
     * 计算MD5摘要 参考 {@code MD5Utils#md5(byte[]) }
     * @param data 需要md5摘要的String数据
     * @return byte[] MD5摘要
     */
    public static byte[] md5(String data) {
        return md5(data.getBytes());
    }

    /**
     * 转换成md5（32位）字串的 digest（摘要），
     * @param data 要转换的数据（byte数组)
     * @return md5后的（32位）字串的 digest（摘要）
     */
    public static String md5Hex(byte[] data) {
        return bytesToHexString(md5(data));
    }

    /**
     * 将数据转成32位Md5摘要
     * eg:
     * md5Hex("123456") --> e10adc3949ba59abbe56e057f20f883e
     * @param data String 
     * @return MD5 32位值
     */
    public static String md5Hex(String data) {
        return bytesToHexString(md5(data));
    }
    
    /**
     * 将字符串转成16位的md5 摘要 
      * eg:
     * md5Hex16("123456") --> 49ba59abbe56e057
     * @param data 
     * @return 16位的md5 摘要 
     */
    public static String md5Hex16(String data){
    		return  bytesToHexString(md5(data)).substring(8, 24);
    }

    /**
     * MD5 本地文件 摘要
     *
     */
    public static String getFileMd5String(File file) {
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
	        int numRead = 0;
	        MessageDigest messagedigest = getDigest();
	        while (true) {
                try {
                    if (!((numRead = fis.read(buffer)) > 0)) {break;};
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messagedigest.update(buffer, 0, numRead);
	        }
	        return md5Hex(messagedigest.digest());
    }    
    
    /**
     * URL 数据转 MD5(32位) 摘要
     * eg:
     * urlStreamToMD5("http://image.mgzf.com/imagefile/community/0/0/210/210_1396602455210.jpg") --> 6ce3f92d3655a50b39247c5fb1ee2307
     * @brief URL 数据转 MD5(32位) 摘要
     * @param urlStr
     * @return  MD5(32位) 摘要
     * @author 戴德荣
     * @note 戴德荣 @ 2017-12-10 创建
     */
    public static String urlStreamToMd5(String urlStr){          
		String md5Str32 = "";
		try (InputStream inputStream = new BufferedInputStream(new URL(urlStr).openStream())){
			byte[] bytes = new byte[1024];
			int len = 0;
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			while ((len = inputStream.read(bytes)) > 0) {
				messagedigest.update(bytes, 0, len);
			}
			md5Str32 = bytesToHexString(messagedigest.digest());
		} catch (Exception e) {
            e.printStackTrace();
		}
        return md5Str32;  
    }  
    
    /**
     * 
     * @param password
     * @return
     */
    public static String getMd5Str(String password){
        StringBuilder md5StrBuff = new StringBuilder();

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();

            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		return md5StrBuff.toString();
	}
}

