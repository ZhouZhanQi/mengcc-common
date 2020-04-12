package com.mengcc.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zhouzq
 * @date 2019/10/21
 * @desc 日期时间工具类
 */
public class DateTimeUtils {

    public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public final static String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    public final static String DATE_FORMAT = "yyyyMMdd";


    private DateTimeUtils() {
    }

    public static String convertLocalDateTimeToDefaultStr(LocalDateTime dateTime){
        return convertLocalDateTimeToStr(dateTime, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String convertLocalDateTimeToStr(LocalDateTime dateTime, String formatterStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterStr);
        return dateTime.format(formatter);
    }

    public static LocalDate convertStrToLocalDate(String dateStr, String formatterStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterStr);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalDateTime convertStrToLocalDateTime(String dateStr, String formatterStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterStr);
        return LocalDateTime.parse(dateStr, formatter);
    }

}
