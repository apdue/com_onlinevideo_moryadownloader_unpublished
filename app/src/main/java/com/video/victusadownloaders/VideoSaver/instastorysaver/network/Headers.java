package com.video.victusadownloaders.VideoSaver.instastorysaver.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Headers {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";

    public static class Client {
        private static final String[] CLIENT = new String[]{"X-IG-Connection-Type", "WIFI", "X-IG-Capabilities", "3Q4=", "Accept-Language", "en-US", "User-Agent", "Instagram 9.3.0 Android (22/5.1; 480dpi; 1080x1776; LG; Google Nexus 5 - 5.1.0 - API 22 - 1080x1920; armani; qcom; en_US)", "Accept-Encoding", "deflate, sdch"};

        public static String[] add(boolean z, String... strArr) {
            List arrayList;
            int i = 0;
            int length;
            if (z) {
                arrayList = new ArrayList(Arrays.asList(strArr));
                strArr = CLIENT;
                length = strArr.length;
                while (i < length) {
                    arrayList.add(strArr[i]);
                    i++;
                }
            } else {
                arrayList = new ArrayList(Arrays.asList(CLIENT));
                length = strArr.length;
                while (i < length) {
                    arrayList.add(strArr[i]);
                    i++;
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        }

        public static String[] get() {
            return CLIENT;
        }
    }
}
