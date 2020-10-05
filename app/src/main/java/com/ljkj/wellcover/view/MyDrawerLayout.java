package com.ljkj.wellcover.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.drawerlayout.widget.DrawerLayout;

/**
 * 文件名：MyDrawerLayout
 * 作者：Turbo
 * 时间： 2020-10-05 17:51
 * 蚁穴虽小，溃之千里。
 */
public class MyDrawerLayout extends DrawerLayout {
    public MyDrawerLayout(Context context) {
        super(context);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}