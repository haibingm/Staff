package com.tujia.staff.common.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by haibingm 2018/01/11.
 */
public class DateUtils {


    private static Calendar calendar = Calendar.getInstance();
    /**
     *@Author haibingm 2018/01/11.
     *@Date 018/01/11.
     *@Description 得到今天是周几
     */

    public static int getTodayWeek(){

       calendar.setTime(new Date());
       int week = calendar.get(Calendar.DAY_OF_WEEK)-1;
       if(week<0) week=7;
       return week;
   }

    /**
     *@Author haibingm 2018/01/11.
     *@Date 018/01/11.
     *@Description 计算时间差 分钟数
     */

    public static  int getMunite(Date startDate,Date endDate){

          long start = startDate.getTime();
          long end = endDate.getTime();
          int munite =  (int)(end-start)/(1000*60);
          return munite;
      }


    /**
     *@Author haibingm 2018/01/11.
     *@Date 018/01/11.
     *@Description 获取当天某个时间
     */

    public static Date getDate(int hour,int minute){

          calendar.set(Calendar.HOUR_OF_DAY,hour);
          calendar.set(Calendar.MINUTE,minute);
          return  calendar.getTime();
      }


}
