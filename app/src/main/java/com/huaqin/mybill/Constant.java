package com.huaqin.mybill;

import com.huaqin.mybill.util.NetworkUtils;

/**
 * Created by zhang on 2016.09.23.
 */
public class Constant {
    static boolean isWifi = NetworkUtils.isConnectWifi(MyApplication.getInstance());

    public static String APP_URL = "https://play.google.com/store/apps/details?id=com.eajy.materialdesign2";
    private static String DESIGNED_BY = "Designed by Eajy in China";
    public static String SHARE_CONTENT = "A beautiful app designed with Material Design 2:\n" + APP_URL + "\n- " + DESIGNED_BY;
    public static String EMAIL = "mailto:eajy.zhangxiao@gmail.com";
    public static String GIT_HUB = "https://github.com/Eajy/MaterialDesign2";
    public static String MY_WEBSITE = "https://sites.google.com/view/eajy";
    public static final String NETEASEBASE = isWifi ? "http://42.192.43.247:3000/" : "http://39.108.131.225:3000/";

}
