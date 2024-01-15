package com.light.noteai.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    public static String removePrefixAndDot(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        Pattern pattern = Pattern.compile("^\\d+\\.");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.replaceFirst("");
        }

        return input;
    }

    public static void main(String[] args) {
        String s = "123.性能测试：Spring Boot项目中的性能测试方法";
        String result = removePrefixAndDot(s);
        System.out.println(result);
    }

}
