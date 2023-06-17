package com.light.noteai.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 获取下一天日期
     * @param d
     * @return
     */
    public static Date getNextDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }
}
