package com.ljkj.wellcover.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.ljkj.wellcover.R;
import com.ljkj.wellcover.adapter.ActionAdapter;
import com.ljkj.wellcover.bean.EquipmentInfoBean;
import com.ljkj.wellcover.utils.ActionUtils;
import com.ljkj.wellcover.utils.ConstantUtils;
import com.ljkj.wellcover.utils.ImmersionBarUtils;
import com.ljkj.wellcover.utils.Logger;
import com.ljkj.wellcover.utils.ToolUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名：ActionActivity
 * 作者：Turbo
 * 时间： 2020-10-05 19:09
 * 蚁穴虽小，溃之千里。
 */
public class ActionActivity extends BaseActivity {

    private static final String TAG = "ActionActivity";

    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.rl_content)
    RecyclerView rlContent;
    @BindView(R.id.rl_conn)
    RelativeLayout rlConn;
    @BindView(R.id.rl_disconn)
    RelativeLayout rlDisconn;

    private List<String> mList = new ArrayList<>();
    private ActionAdapter mActionAdapter;
    private EquipmentInfoBean mBean;
    private BleGattProfile mBleGattProfile;
    private BluetoothClient mBluetoothClient;
    private String mMac = "";
    private UUID mBleGattServiceNotify, mCharacterNotify;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_action;
    }

    @OnClick({R.id.ivBack, R.id.rl_open, R.id.rl_close, R.id.rl_clear, R.id.rl_conn, R.id.rl_disconn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rl_open:
                sendAction(1);
                mList.add(getTime() + ": 开启");
                mActionAdapter.addDatas(mList);
                break;
            case R.id.rl_close:
                sendAction(2);
                mList.add(getTime() + ": 关闭");
                mActionAdapter.addDatas(mList);
                break;
            case R.id.rl_clear:
                sendAction(3);
                mList.add(getTime() + ": 解除报警");
                mActionAdapter.addDatas(mList);
                break;
            case R.id.rl_conn:
                connection();
                mList.add(getTime() + ": 开始连接");
                mActionAdapter.addDatas(mList);
                break;
            case R.id.rl_disconn:
                disConnection();
                mList.add(getTime() + ": 断开连接");
                mActionAdapter.addDatas(mList);
                break;
        }
    }

    private String getTime() {
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(now);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mBluetoothClient = new BluetoothClient(mContext);
        mBean = (EquipmentInfoBean) getIntent().getSerializableExtra(ConstantUtils.DATA);
//        connection();
        if (null != mBean) {
            tvNumber.setText(mBean.getId());
            String lockStatus = mBean.getLockStatus();
            //设备状态：1：开启  2 ： 关闭 3： 解除报警
            switch (lockStatus) {
                case "1":
                    tvState.setText("开启");
                    break;
                case "2":
                    tvState.setText("关闭");
                    break;
                case "3":
                    tvState.setText("解除报警");
                    break;
            }
        }
        ImmersionBarUtils.initColorBar(ActionActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rlContent.setLayoutManager(mLayoutManager);
        rlContent.setItemAnimator(new DefaultItemAnimator());

        mActionAdapter = new ActionAdapter(mContext);
        rlContent.setAdapter(mActionAdapter);

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
                                        sendAction(4);
                                    } else {
                                        Logger.e(TAG, "订阅蓝牙通知失败");
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
                }
            }
        });
    }

    /**
     * 断开连接
     */
    private void disConnection() {
        mBluetoothClient.disconnect(mMac);
    }

    /**
     * 发送指令
     *
     * @param type 1解锁 2关锁 3解除报警 4认证
     */
    private void sendAction(int type) {
        byte result[] = null;
        switch (type) {
            case 1:
                result = ActionUtils.onLock(1);
                break;
            case 2:
                result = ActionUtils.onLock(2);
                break;
            case 3:
                result = ActionUtils.onLock(3);
                break;
            case 4:
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
        byte yan = ToolUtils.sumCheck(value, value.length - 4, 2);
        Logger.e(TAG, "onProcessData: yuan = " + yuan + "        yan = " + yan);
        if (yuan == yan) { //判断校验为
            Logger.e(TAG, "数据校验成功");
            switch (value[4]) {
                case 1:
                    Logger.e(TAG, "蓝牙认证信息");
                    if ("00".equals(data[8])) {
                        Logger.e(TAG, "蓝牙认证信息      成功");
                    } else {
                        Logger.e(TAG, "蓝牙认证信息      失败");
                    }
                    break;
                case 2:
                    Logger.e(TAG, "蓝牙解锁/关锁指令");
                    switch (data[8]) {
                        case "00":
                            Logger.e(TAG, "蓝牙解锁/关锁指令      密码错误");
                            break;
                        case "01":
                            Logger.e(TAG, "蓝牙解锁/关锁指令      操作成功");
                            break;
                        case "02":
                            Logger.e(TAG, "蓝牙解锁/关锁指令      因报警状态,解锁失败");
                            break;
                        case "03":
                            Logger.e(TAG, "蓝牙解锁/关锁指令      因电压过低,上锁失败(锁电低于3300mV,禁止上锁操作)");
                            break;
                        case "04":
                            Logger.e(TAG, "蓝牙解锁/关锁指令      响应超时，电机未到位");
                            break;
                        case "05":
                            Logger.e(TAG, "蓝牙解锁/关锁指令      锁杆不在位，不允许上锁");
                            break;
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
                    break;
            }
        } else {
            Logger.e(TAG, "数据校验失败");
        }
        Logger.e(TAG, "处理接收到的数据已经完成");
    }

}
