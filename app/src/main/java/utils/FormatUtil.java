package utils;

import java.text.DecimalFormat;

// Created by Administrator on 2016/10/18.

public class FormatUtil {

    // --------------------------------------------------------------
    public static String format(int data, String pattern) {
        return new DecimalFormat(pattern).format(data);
    }

    public static String format(int data) {
        return format(data, "#,###");
    }

    // --------------------------------------------------------------
    public static String format(long data, String pattern) {
        return new DecimalFormat(pattern).format(data);
    }

    public static String format(long data) {
        return format(data, "#,###");
    }

    // --------------------------------------------------------------
    public static String format(float data, String pattern) {
        return new DecimalFormat(pattern).format(data);
    }

    public static String format(float data) {
        return format(data, "#,###.00");
    }

    // --------------------------------------------------------------
    public static String format(double data, String pattern) {
        return new DecimalFormat(pattern).format(data);
    }

    public static String format(double data) {
        return format(data, "#,###.00");
    }

    // ---------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println(FormatUtil.format(100000000));

        System.out.println(FormatUtil.format(10000000000L));

        System.out.println(FormatUtil.format(1000000.00f));

        System.out.println(FormatUtil.format(1000000.00));
    }

}
