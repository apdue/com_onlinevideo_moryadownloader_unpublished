package com.video.victusadownloaders.VideoSaver.utils;

public class NumberUtils {

    /* renamed from: c */
    public static char[] f47c = {'K', 'M', 'B', 'T'};

    public static String coolFormat(double d, int i) {
        String str;
        Object obj;
        if (d == 0.0d) {
            return "0";
        }
        if (d < 1000.0d) {
            return String.valueOf((int) d);
        }
        double d2 = ((double) (((long) d) / 100)) / 10.0d;
        boolean z = (d2 * 10.0d) % 10.0d == 0.0d;
        if (d2 < 1000.0d) {
            StringBuilder sb = new StringBuilder();
            if (d2 > 99.9d || z || (!z && d2 > 9.99d)) {
                obj = Integer.valueOf((((int) d2) * 10) / 10);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(d2);
                sb2.append("");
                obj = sb2.toString();
            }
            sb.append(obj);
            sb.append("");
            sb.append(f47c[i]);
            str = sb.toString();
        } else {
            str = coolFormat(d2, i + 1);
        }
        return str;
    }
}
