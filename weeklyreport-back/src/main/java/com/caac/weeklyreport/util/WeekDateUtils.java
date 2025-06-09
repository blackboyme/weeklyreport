package com.caac.weeklyreport.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class WeekDateUtils {

    /**
     * 根据周数获取该周的开始日期（周一）
     * @param weekNumber 今年的第几周
     * @return 该周周一的LocalDateTime对象（时间部分为00:00:00）
     * @throws IllegalArgumentException 如果周数不在有效范围内
     */
    public static LocalDateTime getStartDateOfWeek(int weekNumber) {
        return getWeekDate(weekNumber, 1).withHour(0).withMinute(0).withSecond(0);
    }

    /**
     * 根据周数获取该周的结束日期（周日）
     * @param weekNumber 今年的第几周
     * @return 该周周日的LocalDateTime对象（时间部分为23:59:59.999999999）
     * @throws IllegalArgumentException 如果周数不在有效范围内
     */
    public static LocalDateTime getEndDateOfWeek(int weekNumber) {
        return getWeekDate(weekNumber, 7).withHour(23).withMinute(59).withSecond(59);
    }

    private static LocalDateTime getWeekDate(int weekNumber, long dayOfWeek) {
        // 获取当前年份
        int year = LocalDate.now().getYear();

        // 验证周数是否有效
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        long maxWeeks = firstDayOfYear.range(WeekFields.of(Locale.getDefault()).weekOfYear()).getMaximum();

        if (weekNumber < 1 || weekNumber > maxWeeks) {
            throw new IllegalArgumentException("周数必须在1到" + maxWeeks + "之间");
        }

        // 计算该周的指定日期
        LocalDate date = LocalDate.of(year, 1, 1)
                .with(WeekFields.of(Locale.getDefault()).weekOfYear(), weekNumber)
                .with(WeekFields.of(Locale.getDefault()).dayOfWeek(), dayOfWeek);

        return date.atStartOfDay();
    }

//    public static void main(String[] args) {
//        // 测试示例
//        int testWeek = 12;
//
//        LocalDateTime startDate = getStartDateOfWeek(testWeek);
//        System.out.println("第" + testWeek + "周的开始日期是: " + startDate);
//
//        LocalDateTime endDate = getEndDateOfWeek(testWeek);
//        System.out.println("第" + testWeek + "周的结束日期是: " + endDate);
//    }
}