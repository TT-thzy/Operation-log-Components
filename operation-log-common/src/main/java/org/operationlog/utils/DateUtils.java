package org.operationlog.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: TT-Berg
 * @date: 2024/5/27
 **/
public class DateUtils {

    /**
     * 获取指定日期所在周的周一和周日分别是几号
     *
     * @param date
     * @return
     */
    public static List<LocalDate> getMonDayAndSunday(LocalDate date) {
        List<LocalDate> result = new ArrayList<>(2);

        LocalDate monday = date.minusDays(date.getDayOfWeek().getValue() - 1);
        LocalDate sunDay = date.plusDays(7 - date.getDayOfWeek().getValue());

        result.add(monday);
        result.add(sunDay);

        return result;
    }

    /**
     * 获取指定日期所在月的第一天和最后一天分别是几号
     *
     * @param date
     * @return
     */
    public static List<LocalDate> getFirstDayAndLastDayByCurrentMonth(LocalDate date) {
        List<LocalDate> result = new ArrayList<>(2);

        YearMonth yearMonth = YearMonth.from(date);
        LocalDate firstDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        LocalDate lastDay = firstDay.plusMonths(1).minusDays(1);

        result.add(firstDay);
        result.add(lastDay);

        return result;
    }
}
