package com.light.noteai.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    public static String removePrefixAndDot(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 匹配： ### 123.
        Pattern pattern = Pattern.compile("#+|\\s+|\\d+\\.");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.replaceAll("");
        }

        return input;
    }

    public static void main(String[] args) {
        String s = "### 123.性能测试：Spring Boot2项目中的性能测试方法";
        String result = removePrefixAndDot(s);
        System.out.println(result);
    }

}
