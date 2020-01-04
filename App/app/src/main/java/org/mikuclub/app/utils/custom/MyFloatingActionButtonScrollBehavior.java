package org.mikuclub.app.utils.custom;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

/**
 * 自定义浮动按钮的动作管理器
 */
public class MyFloatingActionButtonScrollBehavior extends FloatingActionButton.Behavior
{
        private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
        private boolean mIsAnimatingOut = false;

        // 因为需要在布局xml中引用，所以必须实现该构造方法
        public MyFloatingActionButtonScrollBehavior(Context context, AttributeSet attrs)
        {
                super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                           final View directTargetChild, final View target, final int nestedScrollAxes, int type)
        {
                // 确保滚动方向为垂直方向
                return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed)
        {
                super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
                // 向下滑动 而且 按钮还是可见的状态
                if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE)
                {
                        animateOut(child);
                }
                // 向上滑动
                else if (dyConsumed < 0 && child.getVisibility() == View.INVISIBLE)
                {
                        animateIn(child);
                }
        }

        // FAB隐藏动画
        private void animateOut(FloatingActionButton button)
        {
                button.hide(new FloatingActionButton.OnVisibilityChangedListener()
                            {
                                    @Override
                                    public void onHidden(FloatingActionButton fab)
                                    {
                                            super.onHidden(fab);
                                            fab.setVisibility(View.INVISIBLE);
                                    }
                            }
                );
        }

        // FAB显示动画
        private void animateIn(FloatingActionButton button)
        {
                button.show();
        }


}
