package com.ljkj.wellcover.utils;

import android.util.Log;

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

        Log.e("turbo", "发送数据 = " + ToolUtils.byteToHex(b));
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
}
