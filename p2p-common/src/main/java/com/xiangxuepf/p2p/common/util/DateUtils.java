package com.xiangxuepf.p2p.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author mhw
 */
public class DateUtils {
    /**
     * 通过添加天数获取日期值；
     * @param date
     * @param days
     * @return
     */
    public static Date getDateByAddDays(Date date, Integer days) {

        //创建一个日期处理类对象
        Calendar instance = Calendar.getInstance();
        //设置当前日期处理类对象的时间值
        instance.setTime(date);
        //在当前日期处理类上添加天数
        instance.add(Calendar.DATE,days);

        return  instance.getTime(); //拿到当前时间；
    }

    /**
     * 通过添加月份获取日期值；
     * @param date
     * @param months
     * @return
     */
    public static Date getDateByAddMonths(Date date, Integer months) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH,months);
        return instance.getTime();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getDateByAddDays(new SimpleDateFormat("yyyy-MM-dd").parse("2008-08-08"),1));
    }

    /**
     * 获取时间戳
     * @return 格式：yyyyMMddHHmmssSSS
     */
    public static String getTimeStamp() {

        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
}
