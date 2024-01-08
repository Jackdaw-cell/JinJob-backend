package com.jackdaw.jinjobbackendcommon.utils;


import com.jackdaw.jinjobbackendmodel.enums.DateTimePatternEnum;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtil {

    public static String format(Date date, String pattern) {
        return fromLocateDateTime2String(date).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime fromLocateDateTime2String(Date date) {
        Instant instant = date.toInstant();
        // 默认时区
        ZoneId zone = ZoneId.systemDefault();
        // 获取LocalDate对象
        LocalDateTime localDate = instant.atZone(zone).toLocalDateTime();
        return localDate;
    }

    public static LocalDate fromLocateDate2String(Date date) {
        Instant instant = date.toInstant();
        // 默认时区
        ZoneId zone = ZoneId.systemDefault();
        // 获取LocalDate对象
        LocalDate localDate = instant.atZone(zone).toLocalDate();
        return localDate;
    }

    public static Date getDayAgo(Integer day) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(day);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static List<String> getBetweenDate(Date startDate, Date endDate) {
        LocalDate startLocalDate = fromLocateDate2String(startDate);
        LocalDate endLocalDate = fromLocateDate2String(endDate);
        long numOfDays = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1;
        List<LocalDate> localDateList = Stream.iterate(startLocalDate, date -> date.plusDays(1))
                .limit(numOfDays).collect(Collectors.toList());
        // 默认时区
        List<String> dateList = localDateList.stream().map(date -> date.format(DateTimeFormatter.ofPattern(DateTimePatternEnum.YYYY_MM_DD.getPattern()))).
                collect(Collectors.toList());
        return dateList;
    }
}
