package com.example.chenlei.my_weather.myViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.example.chenlei.my_weather.R;
import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by chenlei on 2017/10/30.
 */

public class MyPullToRefreshScrollView extends PullToRefreshBase<MyScrollView> {


    public MyPullToRefreshScrollView(Context context) {
        super(context);
    }

    public MyPullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPullToRefreshScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public MyPullToRefreshScrollView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected MyScrollView createRefreshableView(Context context, AttributeSet attrs) {
        MyScrollView scrollView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            scrollView = new MyScrollView(context, attrs);
        } else {
            scrollView = (MyScrollView) new ScrollView(context, attrs);
        }

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    @TargetApi(9)
    final class InternalScrollViewSDK9 extends ScrollView {

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(MyPullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }

    public void firstRefreshing(boolean doScroll) {

        if (doScroll) {
            new AsyncTask<Integer, Integer, Integer>() {//该处是针对PullToRefreshScrollView控件的bug进行补充的
                @SuppressWarnings("ResourceType")
                @Override
                protected Integer doInBackground(Integer... params) {
                    while (true) {
                        if (mHeaderLayout.getHeight() > 0) {
                            return null;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


                @Override
                protected void onPostExecute(Integer result) {
                    setRefreshing(true);
                }
            }.execute();
        } else {
            setRefreshing(false);
        }
    }
}
