package com.example.a3lbotcontrol.util;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * 作者：Created by Administrator on 2020/9/2.
 * 邮箱：
 */
public class Transfrom {
    private static Transfrom transfrom;

    public static Transfrom getTransfrom() {
        if (transfrom == null) {
            transfrom = new Transfrom();
        }
        return transfrom;
    }

    //将16进制字节数组转换为字符串
    public String bytesStrings(byte[] data) {
        String getString = "";
        for (int i = 0; i < data.length; i++) {
            getString += String.format("%02X", data[i]);
        }
        return getString;
    }

    //16进制字符串转浮点
    public String cut16strTofloat(String hexString) {
        Integer l = Integer.parseInt(hexString, 16);
        Float f = Float.intBitsToFloat(l.intValue());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String VALUE = decimalFormat.format(f);//format 返回的是字符串
        System.out.println(VALUE);
        return VALUE;
    }
    //16进制字符串转整型
    public  int cut16strToInt(String HexString) {
        int num = Integer.valueOf(HexString, 16);
        return num;
    }


    //字符串转浮点
    public Float cutstrTofloat(String sf) {
        float f = Float.parseFloat(sf);
        return f;
    }

    //浮点型转16进制字符串
    public String cutfloatTo16str(float f1) {
        return Integer.toHexString(Float.floatToIntBits(f1));
    }

    //字符转换为字节
    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //16进制字符串转字节数组
    public byte[] hexString2Bytes(String hex) {
        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }
    }

    public String writeRandomL(String angleL, String momentL, String factorL,String angleR) {
        float getAngleL = Transfrom.getTransfrom().cutstrTofloat(angleL);
        float getMomentL = Transfrom.getTransfrom().cutstrTofloat(momentL);
        float getFactorL = Transfrom.getTransfrom().cutstrTofloat(factorL);
        float getAngleR = Transfrom.getTransfrom().cutstrTofloat(angleR);
        String getAnglestrL = Transfrom.getTransfrom().cutfloatTo16str(getAngleL);
        String getMomentstrL = Transfrom.getTransfrom().cutfloatTo16str(getMomentL);
        String getFactorstrL = Transfrom.getTransfrom().cutfloatTo16str(getFactorL);
        String getAnglestrR = Transfrom.getTransfrom().cutfloatTo16str(getAngleR);
        if (getAnglestrL.equals("0")){
            getAnglestrL="00000000";
        }
        if (getMomentstrL.equals("0")){
            getMomentstrL="00000000";
        }
        if (getFactorstrL.equals("0")){
            getFactorstrL="00000000";
        }
        if (getAnglestrR.equals("0")){
            getAnglestrR="00000000";
        }

        String strAll = "e91c0063" + getAnglestrL + getMomentstrL + getFactorstrL + getAnglestrR;
        return strAll;
    }

    public String writeRandomR( String momentR,  String factorR,String strCut) {

        float getMomentR = Transfrom.getTransfrom().cutstrTofloat(momentR);
        float getFactorR = Transfrom.getTransfrom().cutstrTofloat(factorR);
        String getMomentstrR = Transfrom.getTransfrom().cutfloatTo16str(getMomentR);
        String getFactorstrR = Transfrom.getTransfrom().cutfloatTo16str(getFactorR);
        if (getMomentstrR.equals("0")){
            getMomentstrR="00000000";
        }
        if (getFactorstrR.equals("0")){
            getFactorstrR="00000000";
        }
        String strAll = getMomentstrR  + getFactorstrR + strCut + "000000ef";
        return strAll;
    }

    public String writeFixed(String strL, String strR) {
        float getEtL = Transfrom.getTransfrom().cutstrTofloat(strL)/4;
        float getEtR = Transfrom.getTransfrom().cutstrTofloat(strR)/4;
        String getEtstrL = Transfrom.getTransfrom().cutfloatTo16str(getEtL);
        String getEtstrR = Transfrom.getTransfrom().cutfloatTo16str(getEtR);
        if(getEtstrL.equals("0")){
            getEtstrL="00000000";
        }
        if (getEtstrR.equals("0")){
            getEtstrR="00000000";
        }
        Log.e("0000000",getEtstrL+""+getEtstrR);
        String strAll = "e90800f1" + getEtstrL + getEtstrR + "ef";
        return strAll;
    }

}
