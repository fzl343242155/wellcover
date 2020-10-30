package com.ljkj.wellcover.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.ljkj.wellcover.R;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件名：TestActivity
 * 作者：Turbo
 * 时间： 2020-10-05 16:13
 * 蚁穴虽小，溃之千里。
 */
public class TestActivity extends BaseActivity {

    private static final String TAG = "TestActivity";

    @BindView(R.id.et_mac)
    EditText etMac;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.btn_disconnect)
    Button btnDisconnect;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.et_output)
    EditText etOutput;

    BleGattProfile mBleGattProfile;
    BluetoothClient mBluetoothClient;

    String mMac;

    UUID mBleGattServiceNotify, mCharacterNotify;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mBluetoothClient = new BluetoothClient(mContext);

        mMac = etMac.getText().toString();
    }

    @OnClick({R.id.btn_connect, R.id.btn_disconnect, R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                BleConnectOptions options = new BleConnectOptions.Builder()
                        .setConnectRetry(5)   // 连接如果失败重试3次
                        .setConnectTimeout(10000)   // 连接超时10s
                        .setServiceDiscoverRetry(5)  // 发现服务如果失败重试3次
                        .setServiceDiscoverTimeout(10000)  // 发现服务超时20s
                        .build();
                mBluetoothClient.connect(mMac, options, new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile data) {
                        Log.e(TAG, "onResponse: connect code = " + code);
                        if (code == 0) {
                            btnConnect.setText("已经连接");

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
                                            Log.e(TAG, "onResponse: notify code = " + code);
                                            if (code == 0) {
                                                Log.e(TAG, "订阅蓝牙通知成功");
                                            } else {
                                                Log.e(TAG, "订阅蓝牙通知失败");
                                            }
                                        }

                                        @Override
                                        public void onNotify(UUID service, UUID character, byte[] value) {

                                            Log.e(TAG, "接收数据:" + new String(value));

                                            etOutput.setText(new String(value));
                                        }
                                    });

                        } else {
                            btnConnect.setText("没连通");
                        }
                    }
                });
                break;
            case R.id.btn_disconnect:
                mBluetoothClient.disconnect(mMac);
                btnConnect.setText("连接");
                break;
            case R.id.btn_send:
                String content = etInput.getText().toString();


                mBluetoothClient.write(mMac, mBleGattServiceNotify,
                        mCharacterNotify, content.getBytes(),
                        new BleWriteResponse() {
                            @Override
                            public void onResponse(int code) {
                                Log.e(TAG, "onResponse: write  code = " + code);
                            }
                        });

                break;
        }
    }
}
