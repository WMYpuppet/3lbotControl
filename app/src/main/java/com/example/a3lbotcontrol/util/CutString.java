package com.example.a3lbotcontrol.util;

/**
 * 作者：Created by Administrator on 2020/9/21.
 * 邮箱：
 */
public class CutString {
    private static CutString cutString;
    public static String strMaxLeft, strMinLeft, strMeanLeft, strFactorLeft, strAngleLeft;
    public static String strMaxRight, strMinRight, strMeanRight, strFactorRight, strAngleRight;
    public static String strMomentLeft, strMomentRight;
    public static int strYear, strMonth, strDay, strTime, strStepfrequency, strAngle;


    public static CutString getCutString() {
        if (cutString == null) {
            cutString = new CutString();
        }
        return cutString;
    }

    //获取命令
    public String cutCMD(String data) {
        String cMD = "";
        if (data.length() > 14) {
            cMD = data.substring(12, 14).trim();
        }
        return cMD;
    }

    //获取字符串后6位
    public String cutEnd(String data) {
        String strEnd = "";
        if (data.length() > 6) {
            strEnd = data.substring(data.length() - 6, data.length());
        }
        return strEnd;
    }

    public void cutTshow(String data) {
        if (data.length() > 94) {
            strMaxLeft = Transfrom.getTransfrom().cut16strTofloat(data.substring(14, 22).trim());
            strMinLeft = Transfrom.getTransfrom().cut16strTofloat(data.substring(22, 30).trim());
            strMeanLeft = Transfrom.getTransfrom().cut16strTofloat(data.substring(30, 38).trim());
            strFactorLeft = Transfrom.getTransfrom().cut16strTofloat(data.substring(38, 46).trim());
            strMaxRight = Transfrom.getTransfrom().cut16strTofloat(data.substring(46, 54).trim());
            strMinRight = Transfrom.getTransfrom().cut16strTofloat(data.substring(54, 62).trim());
            strMeanRight = Transfrom.getTransfrom().cut16strTofloat(data.substring(62, 70).trim());
            strFactorRight = Transfrom.getTransfrom().cut16strTofloat(data.substring(70, 78).trim());
//            strAngleLeft = Transfrom.getTransfrom().cut16strTofloat(data.substring(78, 86).trim());
//            strAngleRight = Transfrom.getTransfrom().cut16strTofloat(data.substring(86, 94).trim());
        }
    }

    public void cutMoment(String data) {
        if (data.length() > 30) {
            strMomentLeft = Transfrom.getTransfrom().cut16strTofloat(data.substring(14, 22).trim());
            strMomentRight = Transfrom.getTransfrom().cut16strTofloat(data.substring(22, 30).trim());
        }
    }

    public String cutStartAndPauseElectric(String data) {
        String strElectric = "";
        if (data.length() > 22) {
            strElectric = data.substring(16, 18);
        }
        return strElectric;
    }

    public String cutElectric(String data) {
        String strElectric = "";
        if (data.length() > 20) {
            strElectric = data.substring(20, 22).trim();
        }
        return strElectric;
    }

    public void cutHistoryData(String data) {
        strYear = Transfrom.getTransfrom().cut16strToInt(data.substring(14, 18).trim());
        strMonth = Transfrom.getTransfrom().cut16strToInt(data.substring(18, 20).trim());
        strDay = Transfrom.getTransfrom().cut16strToInt(data.substring(20, 22).trim());
        strTime = Transfrom.getTransfrom().cut16strToInt(data.substring(22, 26).trim()) / 60;
        strStepfrequency = Transfrom.getTransfrom().cut16strToInt(data.substring(26, 30).trim());
        strAngle = Transfrom.getTransfrom().cut16strToInt(data.substring(30, 34).trim());
    }

}
