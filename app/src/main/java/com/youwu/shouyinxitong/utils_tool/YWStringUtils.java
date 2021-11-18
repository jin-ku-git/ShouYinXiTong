package com.youwu.shouyinxitong.utils_tool;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YWStringUtils {

    public static String getStanMoney (float f){
        DecimalFormat df = new DecimalFormat("#.00");
        String retureStr = df.format(f);
        if (retureStr.contains("1") ||retureStr.contains("2") ||retureStr.contains("3")
                ||retureStr.contains("4") ||retureStr.contains("5")
                ||retureStr.contains("6") ||retureStr.contains("7") ||retureStr.contains("8")
                ||retureStr.contains("9")){
            if (retureStr.indexOf(".") == 0){
                return "0"+retureStr;
            }
            return retureStr;
        }
        return "0";
    }

    public static String getStanWeight(float f){
        DecimalFormat df = new DecimalFormat("#.000");
        String retureStr = df.format(f);
        if (retureStr.contains("1") ||retureStr.contains("2") ||retureStr.contains("3")
                ||retureStr.contains("4") ||retureStr.contains("5")
                ||retureStr.contains("6") ||retureStr.contains("7") ||retureStr.contains("8")
                ||retureStr.contains("9")){
            if (retureStr.indexOf(".") == 0){
                return "0"+retureStr;
            }
            return retureStr;
        }
        return "0";
    }

    public static String SixteenToTeen(String sixteenStr){
        int d = Integer.valueOf(sixteenStr,16);
        return String.valueOf(d);
    }

    /**
     * 把十六进制表示的字节数组字符串，转换成十六进制字节数组
     *
     * @param
     * @return byte[]
     */
    public static byte[] hexStr2bytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toUpperCase().toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (hexChar2byte(achar[pos]) << 4 | hexChar2byte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 十六进制字节数组转字符串
     *
     * @param src 目标数组
     * @param dec 起始位置
     * @param length 长度
     * @return
     */
    public static String bytes2HexStr(byte[] src, int dec, int length) {
        byte[] temp = new byte[length];
        System.arraycopy(src, dec, temp, 0, length);
        return bytes2HexStr(temp);
    }

    /**
     * 字节数组转换成对应的16进制表示的字符串
     *
     * @param src
     * @return
     */
    public static String bytes2HexStr(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            builder.append(buffer);
        }
        return builder.toString().toUpperCase();
    }

    /**
     * 把16进制字符[0123456789abcde]（含大小写）转成字节
     *
     * @param c
     * @return
     */
    private static int hexChar2byte(char c) {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
            case 'A':
                return 10;
            case 'b':
            case 'B':
                return 11;
            case 'c':
            case 'C':
                return 12;
            case 'd':
            case 'D':
                return 13;
            case 'e':
            case 'E':
                return 14;
            case 'f':
            case 'F':
                return 15;
            default:
                return -1;
        }
    }

    /*
     * 字符串转16进制字符串
     */
    public static String string2HexString(String s){
        String r = bytes2HexString(string2Bytes(s));
        return r;
    }

    /*
     * 字符串转字节数组
     */
    public static byte[] string2Bytes(String s){
        byte[] r = s.getBytes();
        return r;
    }

    /*
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    /*
     * 10进制转字节
     */
    public static byte int2Byte(int i){
        byte r = (byte) i;
        return r;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String dataToYMD(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }
}
