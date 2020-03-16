package com.test.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lijian
 * @date 2019/7/30 15:26
 * @desc
 */
public class ToolUtil {
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(lineToHump("test_te1_tet_1_test"));
    }
}
