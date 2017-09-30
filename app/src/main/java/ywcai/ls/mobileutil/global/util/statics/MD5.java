package ywcai.ls.mobileutil.global.util.statics;

import java.security.MessageDigest;


public class MD5 {


    public static String md5(String src) {
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] mdByte = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            mdByte = md5.digest(src.getBytes("utf-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int j = mdByte.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = mdByte[i];
            str[k++] = md5String[byte0 >>> 4 & 0xf];
            str[k++] = md5String[byte0 & 0xf];
        }
        //返回经过加密后的字符串
        return new String(str);
    }

}
