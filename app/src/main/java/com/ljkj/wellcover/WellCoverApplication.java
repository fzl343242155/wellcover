package com.ljkj.wellcover;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import androidx.multidex.MultiDex;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;
import okhttp3.OkHttpClient;

/**
 * 文件名：WellCoverApp
 * 作者：Turbo
 * 时间： 2020-07-29 17:27
 * 蚁穴虽小，溃之千里。
 */
public class WellCoverApplication extends Application {

    private static WellCoverApplication mInstance = null;
    protected static Context context;
    protected static Handler handler;
    protected static int mainThreadId;

    //全局一次性设置默认属性和默认Header
    static {//使用static代码段可以防止内存泄漏
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
            layout.setHeaderHeight(60);//Header标准高度（显示下拉高度>=标准高度 触发刷新）
            layout.setFooterHeight(60);//Footer标准高度（显示上拉高度>=标准高度 触发加载）
            layout.setEnableOverScrollDrag(true);
            layout.setHeaderMaxDragRate(2);//最大显示下拉高度/Header标准高度
            layout.setFooterMaxDragRate(2);//最大显示下拉高度/Footer标准高度
            layout.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
            layout.setEnableFooterFollowWhenNoMoreData(true);
            layout.setEnableFooterFollowWhenLoadFinished(true);
            layout.setEnableNestedScroll(false);
            layout.setEnablePureScrollMode(false);
            layout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
            layout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        });
        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
            layout.setEnableHeaderTranslationContent(true);
            return new ClassicsHeader(context).setAccentColor(context.getResources().getColor(R.color.black));
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            layout.setEnableFooterTranslationContent(true);
            return new ClassicsFooter(context).setAccentColor(context.getResources().getColor(R.color._98A3AF));
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        initAutoSize();
        initOkGo(this);
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();
    }

    public static WellCoverApplication getInstance() {
        return mInstance;
    }

    private void initAutoSize() {
        AutoSizeConfig.getInstance().setExcludeFontScale(false).getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
    }

    private void initOkGo(Application context) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //header不支持中文
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间 60s
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间 60s
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间 60s
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(context)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3)
                .addCommonHeaders(headers);
    }

    /**
     * 获取上下文对象
     *
     * @return context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return handler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return mainThreadId;
    }
}
