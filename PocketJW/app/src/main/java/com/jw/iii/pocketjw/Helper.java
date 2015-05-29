package com.jw.iii.pocketjw;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by End on 2015/5/29.
 */
public class Helper {

    // 获取字符串md5值
    public static String md5(String input) {
        if ( (input == null) || (input.length() == 0) ){
            return "D41D8CD98F00B204E9800998ECF8427E";
        }
        else{
            StringBuffer sb = new StringBuffer();
            try {
                MessageDigest algorithm = MessageDigest.getInstance("MD5");
                algorithm.reset();
                algorithm.update(input.getBytes());
                byte[] md5 = algorithm.digest();
                String singleByteHex = "";
                for (int i = 0; i < md5.length; i++) {
                    singleByteHex = Integer.toHexString(0xFF & md5[i]);
                    if (singleByteHex.length() == 1) {
                        sb.append("0");
                    }
                    sb.append(singleByteHex.toUpperCase());
                }
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            return sb.toString();
        }
    }
}
