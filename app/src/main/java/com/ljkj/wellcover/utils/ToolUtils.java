package com.ljkj.wellcover.utils;


/**
 * 文件名：ToolUtils
 * 作者：Turbo
 * 时间： 12/15/20 4:01 PM
 * 蚁穴虽小，溃之千里。
 */
public class ToolUtils {

    /**
     * 异或运算和
     *
     * @param b     需要求校验的字节数组
     * @param len   结束的下标
     * @param start 开始的下标
     * @return 校验结果
     */
    public static byte byteOrbyte(byte[] b, int len, int start) {
        byte value = b[start];
        for (int i = start; i <= len; i++) {
            value = (byte) (value^b[i]);
        }
        return value;
    }

    /**
     * str转BCD
     *
     * @param asc
     * @return
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /**
     * byte数组转hex
     *
     * @param bytes
     * @return
     */
    public static String byteToHex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append(((strHex.length() == 1) ? "0" + strHex : strHex).toUpperCase() + " "); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }

}
