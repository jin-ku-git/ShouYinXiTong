package com.youwu.shouyinxitong.utils_tool;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入金额时的文本过虑器，可设置最大输入金额、小数点后位数
 *
 * @author Huangming  2018/8/15
 */
public class MoneyInputFilter implements InputFilter {

    /** 正则表达式：以0或正整数开头后跟0或1个(小数点后面跟0到2位数字) */
    private static final String FORMAT = "^(0|[1-9]\\d*)(\\.\\d{0,%s})?$";

    /** 正则表达式：0-9.之外的字符 */
    private static final Pattern SOURCE_PATTERN = Pattern.compile("[^0-9.]");

    /** 默认保留小数点后2位 */
    private Pattern mPattern = Pattern.compile(String.format(FORMAT, "2"));

    /** 允许输入的最大金额 */
    private double maxValue = Integer.MAX_VALUE;

    /**
     * 设置保留小数点后的位数，默认保留2位
     *
     * @param length
     */
    public void setDecimalLength(int length) {
        mPattern = Pattern.compile(String.format(FORMAT, length));
    }

    /**
     * 设置允许输入的最大金额
     *
     * @param maxValue
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * 当系统使用source的start到end的字串替换dest字符串中的dstart到dend位置的内容时，会调用本方法
     *
     * @param source 新输入的字符串
     * @param start 新输入的字符串起始下标，一般为0（删除时例外）
     * @param end 新输入的字符串终点下标，一般为source长度-1（删除时例外）
     * @param dest 输入之前文本框内容
     * @param dstart 原内容起始坐标，一般为dest长度（删除时例外）
     * @param dend 原内容终点坐标，一般为dest长度（删除时例外）
     * @return 你希望输入的内容，比如当新输入的字符串为“恨”时，你希望把“恨”变为“爱”，则return "爱"
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        // 删除时不用处理
        if (TextUtils.isEmpty(source)) {
            return null;
        }

        // 不接受数字、小数点之外的字符
        if (SOURCE_PATTERN.matcher(source).find()) {
            return "";
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder(dest);
        ssb.replace(dstart, dend, source, start, end);
        Matcher matcher = mPattern.matcher(ssb);
        if (matcher.find()) {
            String str = matcher.group();
            Log.d("匹配到的字符串=%s", str);

            //验证输入金额的大小
            double value = Double.parseDouble(str);
            if (value > maxValue) {
                return "";
            }
            return source;
        } else {
            Log.w("不匹配的字符串=%s", ssb.toString());
            return "";
        }
    }
}

