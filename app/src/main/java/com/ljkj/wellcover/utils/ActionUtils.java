package com.ljkj.wellcover.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件名：ActionUtils
 * 作者：Turbo
 * 时间： 12/15/20 3:59 PM
 * 蚁穴虽小，溃之千里。
 */
public class ActionUtils {

    /**
     * 认证
     *
     * @return
     */
    public static byte[] onCertification() {
        byte[] b = new byte[39];
        b[0] = (byte) 0xf1;
        b[1] = (byte) 0x1f;

        b[2] = (byte) 0xff;
        b[3] = (byte) 0xff;

        b[4] = (byte) 0x01;
        b[5] = (byte) 0x00;

        b[6] = (byte) 0x00;//长度
        b[7] = (byte) 0x1c;

        byte[] actionID = getActionID();
        for (int i = 0; i < actionID.length; i++) {
            b[8 + i] = actionID[i];
        }

        for (int i = 0; i < 17; i++) {
            b[14 + i] = (byte) 0x00;
        }

        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyMMddHHmmss");
        byte[] result = ToolUtils.str2Bcd(f.format(now));

        for (int i = 0; i < result.length; i++) {
            b[30 + i] = result[i];
        }

        b[36] = ToolUtils.sumCheck(b, 35, 2);

        b[37] = (byte) 0xf2;
        b[38] = (byte) 0x2f;

        Logger.e("turbo", "认证  发送数据 = " + ToolUtils.byteToHex(b));
        return b;
    }

    /**
     * 对锁的操作
     *
     * @param type 1解锁 2关锁 3解除报警
     * @return
     */
    public static byte[] onLock(int type) {
        byte[] b = new byte[26];
        b[0] = (byte) 0xf1;
        b[1] = (byte) 0x1f;

        b[2] = (byte) 0xff;
        b[3] = (byte) 0xff;

        b[4] = (byte) 0x02;
        b[5] = (byte) 0x00;

        b[6] = (byte) 0x00;//长度
        b[7] = (byte) 0x15;

        byte[] actionID = getActionID();
        for (int i = 0; i < actionID.length; i++) {
            b[8 + i] = actionID[i];
        }

        for (int i = 0; i < 9; i++) {
            b[14 + i] = (byte) 0x00;
        }

        switch (type) {
            case 1:
                b[22] = (byte) 0x30;//解锁
                break;
            case 2:
                b[22] = (byte) 0x31;//关锁
                break;
            case 3:
                b[22] = (byte) 0x32; //解除报警
                break;
        }

        b[23] = ToolUtils.sumCheck(b, 22, 2);

        b[24] = (byte) 0xf2;
        b[25] = (byte) 0x2f;

        Logger.e("turbo", "对锁的操作   发送数据 = " + ToolUtils.byteToHex(b));
        return b;
    }

    private static byte[] getActionID() {
        byte[] b = new byte[6];
        b[0] = (byte) 0x01;
        b[1] = (byte) 0x00;

        b[2] = (byte) 0x02;
        b[3] = (byte) 0x00;

        b[4] = (byte) 0x03;
        b[5] = (byte) 0x00;
        return b;
    }

    /**
     * 蓝牙配置指令
     *
     * @param str1
     * @param str2
     * @param str3
     * @param str4
     * @param str5
     * @param str6
     * @param str7
     * @param str8
     * @param str9
     * @param str10
     * @param str11
     * @param str12
     * @param str13
     * @param str14
     * @param str15
     * @param str16
     * @param str17
     * @param str18
     * @return
     */
    public static byte[] setLockConfig(String str1, String str2, String str3, String str4, String str5, String str6, String str7, String str8,
                                       String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16,
                                       String str17, String str18) {
        int length1 = 0, length2 = 0, length3 = 0, length4 = 0, length5 = 0, length6 = 0, length7 = 0, length8 = 0, length9 = 0,
                length10 = 0, length11 = 0, length12 = 0, length13 = 0, length14 = 0, length15 = 0, length16 = 0, length17 = 0, length18 = 0;
        int index = 0;
        if (!TextUtils.isEmpty(str1)) {
            index++;
            length1 = str1.getBytes().length;
        }
        if (!TextUtils.isEmpty(str2)) {
            index++;
            length2 = str2.getBytes().length;
        }
        if (!TextUtils.isEmpty(str3)) {
            index++;
            length3 = str3.getBytes().length;
        }
        if (!TextUtils.isEmpty(str4)) {
            index++;
            length4 = str4.getBytes().length;
        }
        if (!TextUtils.isEmpty(str5)) {
            index++;
            length5 = str5.getBytes().length;
        }
        if (!TextUtils.isEmpty(str6)) {
            index++;
            length6 = str6.getBytes().length;
        }
        if (!TextUtils.isEmpty(str7)) {
            index++;
            length7 = str7.getBytes().length;
        }
        if (!TextUtils.isEmpty(str8)) {
            index++;
            length8 = str8.getBytes().length;
        }
        if (!TextUtils.isEmpty(str9)) {
            index++;
            length9 = str9.getBytes().length;
        }
        if (!TextUtils.isEmpty(str10)) {
            index++;
            length10 = str10.getBytes().length;
        }
        if (!TextUtils.isEmpty(str11)) {
            index++;
            length11 = str11.getBytes().length;
        }
        if (!TextUtils.isEmpty(str12)) {
            index++;
            length12 = str12.getBytes().length;
        }
        if (!TextUtils.isEmpty(str13)) {
            index++;
            length13 = str13.getBytes().length;
        }
        if (!TextUtils.isEmpty(str14)) {
            index++;
            length14 = str14.getBytes().length;
        }
        if (!TextUtils.isEmpty(str15)) {
            index++;
            length15 = str15.getBytes().length;
        }
        if (!TextUtils.isEmpty(str16)) {
            index++;
            length16 = str16.getBytes().length;
        }
        if (!TextUtils.isEmpty(str17)) {
            index++;
            length17 = str17.getBytes().length;
        }
        if (!TextUtils.isEmpty(str18)) {
            index++;
            length18 = str18.getBytes().length;
        }

        int result = length1 + length2 + length3 + length4 + length5 + length6 + length7 + length8 +
                length9 + length10 + length11 + length12 + length13 + length14 + length15 + length16 + length17 + length18;
        byte[] b = new byte[20 + result + index * 2];

        b[0] = (byte) 0xf1;
        b[1] = (byte) 0x1f;

        b[2] = (byte) 0xff;
        b[3] = (byte) 0xff;

        b[4] = (byte) 0x03;
        b[5] = (byte) 0x00;


        b[6] = (byte) 0x00; //长度
        b[7] = (byte) Integer.parseInt(b.length - 11 + "", 16);

        byte[] actionCode = onActionCode();
        for (int i = 0; i < actionCode.length; i++) {
            b[8 + i] = actionCode[i];
        }

        b[16] = (byte) Integer.parseInt(index + "", 16);//总参数条数

        String strs[] = {str1, str2, str3, str4, str5, str6, str7, str8,
                str9, str10, str11, str12, str13, str14, str15, str16,
                str17, str18};
        int in = 16;
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            if (i == 0) {
                if (!TextUtils.isEmpty(str)) {
                    b[in + i + 1] = (byte) Integer.parseInt((i + 1) + "", 16);
                    b[in + i + 2] = (byte) Integer.parseInt(str.getBytes().length + "", 16);
                    byte str1b[] = str.getBytes();
                    for (int j = 0; j < str1b.length; j++) {
                        b[(in + i + 3) + j] = str1b[j];
                    }
                    in += str.getBytes().length + 2;
                } else {
                    in += str.getBytes().length;
                }
            } else {
                if (TextUtils.isEmpty(str)) {
                    in += str.getBytes().length - 1;
                    if (in < 16) in = 17;
                } else {
                    b[in + i] = (byte) Integer.parseInt((i + 1) + "", 16);
                    b[in + i + 1] = (byte) Integer.parseInt(str.getBytes().length + "", 16);
                    byte str1b[] = str.getBytes();
                    for (int j = 0; j < str1b.length; j++) {
                        b[(in + i + 2) + j] = str1b[j];
                    }
                    in += str.getBytes().length + 1;
                }

            }
        }

        b[b.length - 3] = ToolUtils.sumCheck(b, b.length - 4, 2);
        b[b.length - 2] = (byte) 0xf2;
        b[b.length - 1] = (byte) 0x2f;

        Logger.e("turbo", "蓝牙配置指令    发送数据 = " + ToolUtils.byteToHex(b));
        return b;
    }

    /**
     * 蓝牙查询配置指令
     *
     * @return
     */
    public static byte[] getLockConfig() {
        byte[] b = new byte[37];
        b[0] = (byte) 0xf1;
        b[1] = (byte) 0x1f;

        b[2] = (byte) 0xff;
        b[3] = (byte) 0xff;

        b[4] = (byte) 0x04;
        b[5] = (byte) 0x00;

        b[6] = (byte) 0x00; //长度
        b[7] = (byte) 0x1a;

        byte[] actionCode = onActionCode();
        for (int i = 0; i < actionCode.length; i++) {
            b[8 + i] = actionCode[i];
        }

        b[16] = (byte) 0x11;//总参数条数

        b[17] = (byte) 0x01;
        b[18] = (byte) 0x02;
        b[19] = (byte) 0x03;
        b[20] = (byte) 0x04;
        b[21] = (byte) 0x05;
        b[22] = (byte) 0x06;
        b[23] = (byte) 0x07;
        b[24] = (byte) 0x08;
        b[25] = (byte) 0x09;
        b[26] = (byte) 0x0A;
        b[27] = (byte) 0x0B;
        b[28] = (byte) 0x0C;
        b[29] = (byte) 0x0D;
        b[30] = (byte) 0x0E;
        b[31] = (byte) 0x0F;
        b[32] = (byte) 0x11;
        b[33] = (byte) 0x12;

        b[34] = ToolUtils.sumCheck(b, 33, 2);

        b[35] = (byte) 0xf2;
        b[36] = (byte) 0x2f;

        Logger.e("turbo", "蓝牙查询配置指令    发送数据 = " + ToolUtils.byteToHex(b));
        return b;
    }

    /**
     * 操作码
     *
     * @return
     */
    private static byte[] onActionCode() {
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) {
            result[i] = (byte) 0x01;
        }
        return result;
    }
}
