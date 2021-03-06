package com.ljkj.wellcover.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.utils.ActionUtils;
import com.ljkj.wellcover.utils.ConstantUtils;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.utils.Logger;
import com.ljkj.wellcover.utils.ToolUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：SetLockConfigActivity
 * 作者：Turbo
 * 时间： 12/16/20 11:17 AM
 * 蚁穴虽小，溃之千里。
 */
public class SetLockConfigActivity extends BaseActivity {

    private static final String TAG = "turbo";

    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.rl_getconfig)
    RelativeLayout rlGetconfig;
    @BindView(R.id.rl_setconfig)
    RelativeLayout rlSetconfig;
    @BindView(R.id.et_1)
    EditText et1;
    @BindView(R.id.et_2)
    EditText et2;
    @BindView(R.id.et_3)
    EditText et3;
    @BindView(R.id.et_4)
    EditText et4;
    @BindView(R.id.et_5)
    EditText et5;
    @BindView(R.id.et_6)
    EditText et6;
    @BindView(R.id.et_7)
    EditText et7;
    @BindView(R.id.et_8)
    EditText et8;
    @BindView(R.id.et_9)
    EditText et9;
    @BindView(R.id.et_10)
    EditText et10;
    @BindView(R.id.et_11)
    EditText et11;
    @BindView(R.id.et_12)
    EditText et12;
    @BindView(R.id.et_13)
    EditText et13;
    @BindView(R.id.et_14)
    EditText et14;
    @BindView(R.id.et_15)
    EditText et15;
    @BindView(R.id.et_16)
    EditText et16;
    @BindView(R.id.et_17)
    EditText et17;
    @BindView(R.id.et_18)
    EditText et18;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.rl_conn)
    RelativeLayout rlConn;
    @BindView(R.id.rl_disconn)
    RelativeLayout rlDisconn;

    private BleGattProfile mBleGattProfile;
    private BluetoothClient mBluetoothClient;
    private String mMac = "20:20:09:11:18:5E";
    private UUID mBleGattServiceNotify, mCharacterNotify;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_setlockconfig;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mBluetoothClient = new BluetoothClient(mContext);
        ImmersionBarUtils.initColorBar(SetLockConfigActivity.this);
        setBtnState(false);
        setActionBtnState(false);
    }

    @OnClick({R.id.ivBack, R.id.btn_getIDorMAC, R.id.rl_getconfig, R.id.rl_setconfig, R.id.rl_conn, R.id.rl_disconn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btn_getIDorMAC:
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, ConstantUtils.ADDEQUIPMENT2CAPTURE);
                break;
            case R.id.rl_getconfig:
                sendAction(2);
                break;
            case R.id.rl_setconfig:
                sendAction(1);
                break;
            case R.id.rl_conn:
//                mMac = tvMac.getText().toString().trim();
                connection();
                break;
            case R.id.rl_disconn:
                disConnection();
//                onProcessData(ceshi());
                ceshi();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantUtils.CAPTURE2ADDEQUIPMENT) {
            String result = data.getStringExtra(ConstantUtils.SCANQRCODESUCCESS);
            Logger.e(TAG, "onActivityResult: result = " + result);
            if (!TextUtils.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String lockId = jsonObject.getString("id");
                    String mac = jsonObject.getString("mac");
                    tvId.setText(lockId);
                    tvMac.setText(mac);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 连接
     */
    private void connection() {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(5)   // 连接如果失败重试3次
                .setConnectTimeout(10000)   // 连接超时10s
                .setServiceDiscoverRetry(5)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(10000)  // 发现服务超时20s
                .build();
        mBluetoothClient.connect(mMac, options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                Logger.e(TAG, "connect code = " + code);
                if (code == 0) {
                    Logger.e(TAG, "已经连接");
                    mBleGattProfile = data;
                    for (BleGattService service : data.getServices()) {
                        List<BleGattCharacter> characterList = service.getCharacters();
                        for (BleGattCharacter character : characterList) {
                            if (character.getProperty() == 30) {
                                mBleGattServiceNotify = service.getUUID();
                                mCharacterNotify = character.getUuid();
                            }
                        }
                    }
                    mBluetoothClient.notify(mMac, mBleGattServiceNotify,
                            mCharacterNotify, new BleNotifyResponse() {
                                @Override
                                public void onResponse(int code) {
                                    Logger.e(TAG, "onResponse: notify code = " + code);
                                    if (code == 0) {
                                        Logger.e(TAG, "订阅蓝牙通知成功");
                                        sendAction(3);
                                        setBtnState(true);
                                    } else {
                                        Logger.e(TAG, "订阅蓝牙通知失败");
                                        setBtnState(false);
                                    }
                                }

                                @Override
                                public void onNotify(UUID service, UUID character, byte[] value) {
                                    Logger.e(TAG, "接收数据 = " + ToolUtils.byteToHex(value));
                                    onProcessData(value);
                                }
                            });
                } else {
                    Logger.e(TAG, "没连通");
                    setBtnState(false);
                }
            }
        });
    }

    /**
     * 断开连接
     */
    private void disConnection() {
        if (!TextUtils.isEmpty(mMac)) {
            if (mBluetoothClient != null) {
                setActionBtnState(false);
                mBluetoothClient.disconnect(mMac);
            }
            setBtnState(false);
        }
    }

    /**
     * 发送指令
     *
     * @param type 1写入 2查询  3认证
     */
    private void sendAction(int type) {
        byte result[] = null;
        switch (type) {
            case 1:
                String str1 = et1.getText().toString().trim();
                String str2 = et2.getText().toString().trim();
                String str3 = et3.getText().toString().trim();
                String str4 = et4.getText().toString().trim();
                String str5 = et5.getText().toString().trim();
                String str6 = et6.getText().toString().trim();
                String str7 = et7.getText().toString().trim();
                String str8 = et8.getText().toString().trim();
                String str9 = et9.getText().toString().trim();
                String str10 = et10.getText().toString().trim();
                String str11 = et11.getText().toString().trim();
                String str12 = et12.getText().toString().trim();
                String str13 = et13.getText().toString().trim();
                String str14 = et14.getText().toString().trim();
                String str15 = et15.getText().toString().trim();
                String str16 = et16.getText().toString().trim();
                String str17 = et17.getText().toString().trim();
                String str18 = et18.getText().toString().trim();

                result = ActionUtils.setLockConfig(str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12,
                        str13, str14, str15, str16, str17, str18);
                break;
            case 2:
                result = ActionUtils.getLockConfig();
                break;
            case 3:
                result = ActionUtils.onCertification();
                break;
        }

        if (!TextUtils.isEmpty(mMac)) {
            mBluetoothClient.write(mMac, mBleGattServiceNotify,
                    mCharacterNotify, result,
                    new BleWriteResponse() {
                        @Override
                        public void onResponse(int code) {
                            Logger.e(TAG, "onResponse: write  code = " + code);
                            if (code == 0) {
                                Logger.e(TAG, "数据发送成功");
                            } else {
                                Logger.e(TAG, "数据发送失败");
                            }
                        }
                    });
        } else {
            Logger.e(TAG, "蓝牙mac地址为空");
        }
    }

    /**
     * 处理接收到的数据
     *
     * @param value
     */
    private void onProcessData(byte[] value) {
        Logger.e(TAG, "开始处理接收到的数据");
        String result = ToolUtils.byteToHex(value);
        String data[] = result.split(" ");
        byte yuan = value[value.length - 3];
        byte yan = ToolUtils.byteOrbyte(value, value.length - 4, 2);
        Logger.e(TAG, "onProcessData: yuan = " + yuan + "        yan = " + yan);
        if (yuan == yan) { //判断校验为
            Logger.e(TAG, "数据校验成功");
            switch (value[4]) {
                case 1:
                    Logger.e(TAG, "蓝牙认证信息");
                    if ("00".equals(data[8])) {
                        Logger.e(TAG, "蓝牙认证信息      成功");
                        setActionBtnState(true);
                    } else {
                        Logger.e(TAG, "蓝牙认证信息      失败");
                        setActionBtnState(false);
                    }
                    break;
                case 3:
                    Logger.e(TAG, "蓝牙配置指令");
                    if ("00".equals(data[8])) {
                        Logger.e(TAG, "蓝牙配置指令      密钥验证失败");
                    } else {
                        Logger.e(TAG, "蓝牙配置指令      成功");
                    }
                    break;
                case 4:
                    Logger.e(TAG, "蓝牙查询配置指令");
                    int index = 9;
                    Map<Byte, byte[]> hashMap = new HashMap<Byte, byte[]>();
                    for (int i = 0; i < value[8]; i++) {
                        if (i == 0) {
                            byte action = value[index + i];
                            byte[] bytes = subBytes(value, index + i + 2, value[index + i + 1]);
                            index += 1 + value[index + i + 1];
                            hashMap.put(action, bytes);
                        } else {
                            byte action = value[index + 1];
                            byte len = value[index + 1 + 1];
                            byte[] bytes = subBytes(value, index + 1 + 2, len);
                            index += 2 + len;
                            hashMap.put(action, bytes);
                        }
                    }

                    for (Map.Entry<Byte, byte[]> entry : hashMap.entrySet()) {
                        Logger.e(TAG, "key = " + entry.getKey() + ", value = " + ToolUtils.byteToHex(entry.getValue()));
                    }
            }
        } else {
            Logger.e(TAG, "数据校验失败");
        }
        Logger.e(TAG, "处理接收到的数据已经完成");
    }

    /**
     * 从一个byte[]数组中截取一部分
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    private byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) bs[i - begin] = src[i];
        return bs;
    }

    /**
     * 设置连接和断开按钮状态
     *
     * @param type
     */
    private void setBtnState(boolean type) {
        if (type) {
            rlConn.setBackgroundResource(R.drawable.btn_action_bg_g);
            rlConn.setEnabled(false);
            rlDisconn.setEnabled(true);
            rlDisconn.setBackgroundResource(R.drawable.btn_action_bg);
        } else {
            rlConn.setBackgroundResource(R.drawable.btn_action_bg);
            rlConn.setEnabled(true);
            rlDisconn.setEnabled(false);
            rlDisconn.setBackgroundResource(R.drawable.btn_action_bg_g);
        }
    }

    /**
     * 设置动作按钮的状态
     *
     * @param type
     */
    private void setActionBtnState(boolean type) {
        if (type) {
            rlGetconfig.setEnabled(true);
            rlGetconfig.setBackgroundResource(R.drawable.btn_action_bg);
            rlSetconfig.setEnabled(true);
            rlSetconfig.setBackgroundResource(R.drawable.btn_action_bg);
        } else {
            rlGetconfig.setEnabled(false);
            rlGetconfig.setBackgroundResource(R.drawable.btn_action_bg_g);
            rlSetconfig.setEnabled(false);
            rlSetconfig.setBackgroundResource(R.drawable.btn_action_bg_g);
        }
    }


//    private byte[] ceshi() {
//        byte[] b = new byte[38];
//        b[0] = (byte) 0xf3;
//        b[1] = (byte) 0x3f;
//
//        b[2] = (byte) 0xff;
//        b[3] = (byte) 0xff;
//
//        b[4] = (byte) 0x04;
//        b[5] = (byte) 0x00;
//
//        b[6] = (byte) 0x00;//长度
//        b[7] = (byte) 0x15;
//
//        b[8] = (byte) 0x05;
//
//        b[9] = (byte) 0x01;
//        b[10] = (byte) 0x03;
//        b[11] = (byte) 0x05;
//        b[12] = (byte) 0x06;
//        b[13] = (byte) 0x07;
//
//        b[14] = (byte) 0x02;
//        b[15] = (byte) 0x02;
//        b[16] = (byte) 0x09;
//        b[17] = (byte) 0x09;
//
//        b[18] = (byte) 0x03;
//        b[19] = (byte) 0x03;
//        b[20] = (byte) 0x05;
//        b[21] = (byte) 0x05;
//        b[22] = (byte) 0x05;
//
//        b[23] = (byte) 0x04;
//        b[24] = (byte) 0x04;
//        b[25] = (byte) 0x06;
//        b[26] = (byte) 0x06;
//        b[27] = (byte) 0x06;
//        b[28] = (byte) 0x06;
//
//        b[29] = (byte) 0x05;
//        b[30] = (byte) 0x04;
//        b[31] = (byte) 0x0a;
//        b[32] = (byte) 0x09;
//        b[33] = (byte) 0x09;
//        b[34] = (byte) 0x09;
//
//
//        b[35] = ToolUtils.byteOrbyte(b, 34, 2);
//        b[36] = (byte) 0xf4;
//        b[37] = (byte) 0x4f;
//
//        return b;
//    }


    private void ceshi() {
//        F3 3F FF FF 03 00 00 01 00 EE F4 4F

        byte[] b = new byte[14];
        b[0] = (byte) 0xF3;
        b[1] = (byte) 0x3F;

        b[2] = (byte) 0xFF;
        b[3] = (byte) 0xFF;

        b[4] = (byte) 0x03;
        b[5] = (byte) 0x00;

        b[6] = (byte) 0x00;
        b[7] = (byte) 0x01;

        b[8] = (byte) 0x00;

        b[9] = ToolUtils.byteOrbyte(b, 3, 2);

        b[10] = (byte) 0xF4;
        b[11] = (byte) 0x4F;


        Logger.e("turbo", "蓝牙查询配置指令    发送数据 = " + ToolUtils.byteToHex(b));

    }

    @Override
    protected void onDestroy() {
        disConnection();
        super.onDestroy();
    }
}
