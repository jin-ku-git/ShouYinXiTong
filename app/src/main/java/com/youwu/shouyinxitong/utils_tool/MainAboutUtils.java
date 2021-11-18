package com.youwu.shouyinxitong.utils_tool;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainAboutUtils {


    //判断是不是微信付款码
    public static boolean isWXPayCode(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches() && str.length() == 18) {
            String num = str.substring(0, 2);
            if ("10".equals(num) || "11".equals(num) || "12".equals(num) || "13".equals(num) || "14".equals(num) || "15".equals(num)) {
                return true;
            }
        }
        return false;
    }

    //判断是不是支付宝付款码
    public static boolean isAlipayCode(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            if (str.length() <= 26 || str.length() >= 16) {
                if (str.length()>=2){
                    String num = str.substring(0, 2);
                    if ("25".equals(num) || "26".equals(num) || "27".equals(num) || "28".equals(num) || "29".equals(num) || "30".equals(num)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //取整的时候的计算 抹零
    public static float YUround(float mustpay) {
        double floor = Math.floor((double) mustpay + 0.5D);
        double realfloor = floor > mustpay ? floor - 0.5D : floor;
        return (float) realfloor;
    }

    //    字符串转毫秒
    public static long getmilles(String startTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis();
    }


    /**
     * 是否为横屏
     *
     * @return
     */
    public static boolean getWetherHorizontalScreen(Context context) {

        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
            //横屏
            Log.e("asafafa", "横屏");
            return true;

        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
            //竖屏
            Log.e("asafafa", "竖屏");
            return false;

        }
        return true;
    }

}
