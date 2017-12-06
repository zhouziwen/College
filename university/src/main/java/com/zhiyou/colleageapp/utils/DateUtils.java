package com.zhiyou.colleageapp.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author ： LongWeiHu on 2016/5/16.
 */
public class DateUtils {

    private static String FORMAT = "yyyy-MM-dd HH:mm:ss";



    private final static long SECONDS_PER_DAY = 24 * 60 * 60;


    /**
     * 得到两个日期间的小时的差值
     *
     * @param date1
     * @param date2
     * @return
     */
    public static double getHoursBetween2Date(Date date1, Date date2) {
        double a = 3600 * 1000;
        return (date1.getTime() - date2.getTime()) / a;
    }

    /**
     * 得到两个日期间的毫秒差值
     * @param date1
     * @param date2
     * @return
     */
    public static long getMilliSecondsBetween2Date(Date date1, Date date2){
        return (date1.getTime() - date2.getTime());
    }

    /**
     * 得到某一指定日期最近的周五的日期，（如果该指定日期是周六或周日，那么得到的日期将是下一周的周五的日期）
     *
     * @param date
     * @return
     */
    public static Date getFridayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);// 指定日期是一周中的第几天
        if (dayOfWeek == 6) {// 如果是周五
            calendar.add(Calendar.DATE, 7);
        } else if (dayOfWeek == 7) {// 如果是周六
            calendar.add(Calendar.DATE, 6);
        } else { // 其他
            calendar.add(Calendar.DATE, 6 - dayOfWeek);
        }
        return calendar.getTime();
    }

    /**
     * 根据日期获取某一天对应的周几
     *
     * @param date
     * @return
     */
    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dayOfWeek = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = " 周日  ";
                break;
            case 2:
                dayOfWeek = " 周一  ";
                break;
            case 3:
                dayOfWeek = " 周二  ";
                break;
            case 4:
                dayOfWeek = " 周三  ";
                break;
            case 5:
                dayOfWeek = " 周四  ";
                break;
            case 6:
                dayOfWeek = " 周五  ";
                break;
            case 7:
                dayOfWeek = " 周六  ";
                break;

            default:
                break;
        }

        return dayOfWeek;
    }

    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 取得指定日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    /**
     * 取得指定日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        return c.getTime();
    }

    /**
     * 取得指定日期所在月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH,1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }




    /**
     * 取得指定日期所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        c.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        c.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        return c.getTime();
    }

    /**
     * 取得指定日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstTimeOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    /**
     * 取得指定日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getLastTimeOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        return c.getTime();
    }

    public static String getHourAndMinuteOfDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minute = calendar.get(Calendar.MINUTE);
        String minuteString = ""+minute;
        if (minute <10) {
            minuteString = "0"+minute;
        }
        return calendar.get(Calendar.HOUR_OF_DAY) + ":"+ minuteString;
    }

    /**获得第二天的日期
     * @return
     */
    public static Date getNextDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        return calendar.getTime();
    }

    /**获得第二天的上午的日期
     * @return
     */
    public static Date getAmNextDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**获得第二天的下午日期
     * @return
     */
    public static Date getPmNextDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**获得当天的下午日期
     * @return
     */
    public static Date getPmTodayDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**获得当天的上午日期
     * @return
     */
    public static Date getAmTodayDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**获取当前月份之前的月份数组
     * @return
     */
    public static List<Integer> getMonthBeforeCurrDate() {
        Calendar calendar = Calendar.getInstance();
        List<Integer> list = new ArrayList<Integer>();
        int start = calendar.get(Calendar.MONTH);
        for (int index = start; index >= 0; index--) {
            list.add(index + 1);
        }
        return list;
    }

    /**获取以当前月份为基准的某个月份的第一天
     * @param offsetOfCurrentMonth：为正数，则表示未来的某个月份; 为负数,则表示过去的某个月；
     * @return
     */
    public static Date getFirstDayOfCertainMonth(int offsetOfCurrentMonth){
        Calendar c = Calendar.getInstance();
        Calendar currentCalender = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, currentCalender.get(Calendar.YEAR));
        c.set(Calendar.MONTH,currentCalender.get(Calendar.MONTH));
        c.add(Calendar.MONTH, offsetOfCurrentMonth);
        c.set(Calendar.DAY_OF_MONTH,1);
        return c.getTime();
    }

    /**获取以当前月份为基准的某个月份的第最后一天
     * @param offsetOfCurrentMonth：为正数，则表示未来的某个月份; 为负数,则表示过去的某个月；
     * @return
     */
    public static Date getLastDayOfCertainMonth(int offsetOfCurrentMonth){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);// 设为当前月的1号
        c.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        c.add(Calendar.MONTH, offsetOfCurrentMonth);// 加offsetOfCurrentMonth个月，变为加offsetOfCurrentMonth月的1号
        c.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**获取以当前月份为基准的某个月份的第一天
     * @param yearOffset：为正数，则表示未来的某年; 为负数,则表示过去的某年；
     * @return
     */
    public static Date getFirstDayOfCertainYear(int yearOffset){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.add(Calendar.YEAR,yearOffset);
        return calendar.getTime();
    }

    /**获取以当前月份为基准的某个月份的最后一天
     * @param yearOffset：为正数，则表示未来的某年; 为负数,则表示过去的某年；
     * @return
     */
    public static Date getLastDayOfCertainYear(int yearOffset){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.add(Calendar.YEAR,yearOffset);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);

        return calendar.getTime();
    }


}
