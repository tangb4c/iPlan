package me.plan.comm.Widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/2/1.
 */
public class WrapContentHeightViewPager extends ViewPager {
    int mLastHeight = 0;
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        final int currentIndex = getCurrentItem();
        final int start = currentIndex > 0? currentIndex - 1:0;
        final int end = currentIndex >= getChildCount()? getChildCount():currentIndex + 1;
        //只需要测量中间要显示的view, 以及两边的view
        for(int i = start; i < end; i++) {
            TLog.i("index:%d total:%d", i, getChildCount());
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
            int h = child.getMeasuredHeight();
            TLog.i("child[%d].getMeasuredHeight:%d", i, h);
            if (h > height) height = h;
        }
        if(height < 1)
            height = mLastHeight;
        else
            mLastHeight = height;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
