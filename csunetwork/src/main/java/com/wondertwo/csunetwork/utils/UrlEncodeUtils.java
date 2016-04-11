package com.wondertwo.csunetwork.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * UrlEncodeUtils
 * Created by wondertwo on 2016/4/10.
 */
public class UrlEncodeUtils {

    public static String chineseToUTF8(String chineseword) {
        String resultUTF8 = null;
        try {
            resultUTF8 = URLEncoder.encode(chineseword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultUTF8;
    }
}
