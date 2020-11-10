package com.ljkj.wellcover.activity;

import android.os.Environment;
import android.view.KeyEvent;

import com.ljkj.wellcover.R;
import com.ljkj.wellcover.fragment.HomeFragment;
import com.ljkj.wellcover.fragment.MessageFragment;
import com.ljkj.wellcover.fragment.MyFragment;
import com.ljkj.wellcover.utils.AppUpdateUtils;
import com.ljkj.wellcover.view.BottomBar;

import java.io.File;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;

    private long touchTime = 0;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= 2000) {
                toast(getString(R.string.hint_click_exit));
                touchTime = currentTime;
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    protected void initViews() {
        mBottomBar.setContainer(R.id.fl_container)
                .setTitleSize(13)
                .setTitleBeforeAndAfterColor(getResources().getColor(R.color._cdcdcd),
                        getResources().getColor(R.color._515151))
                .addItem(HomeFragment.class,
                        getString(R.string.main_home),
                        R.mipmap.home,
                        R.mipmap.homed)
                .addItem(MessageFragment.class,
                        getString(R.string.main_messge),
                        R.mipmap.message,
                        R.mipmap.messaged)
                .addItem(MyFragment.class,
                        getString(R.string.main_my),
                        R.mipmap.my,
                        R.mipmap.myed)
                .setFirstChecked(0)
                .build();
        makeDir();//创建文件夹
    }

    private void makeDir() {
        File dirFirstFolder = new File(Folder_NAME);//方法二：通过变量文件来获取需要创建的文件夹名字
        if (!dirFirstFolder.exists()) { //如果该文件夹不存在，则进行创建
            dirFirstFolder.mkdirs();//创建文件夹
        }
    }

    public String Folder_NAME = Environment.getExternalStorageDirectory() + File.separator + "wellcover";
}
