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
 * custom listener to manage the scrollBehavior of floating action button
 */
public class MyFloatingActionButtonScrollBehavior extends FloatingActionButton.Behavior
{
        private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
        private boolean mIsAnimatingOut = false;

        // 因为需要在布局xml中引用，所以必须实现该构造方法
        //default construct
        public MyFloatingActionButtonScrollBehavior(Context context, AttributeSet attrs)
        {
                super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
                                           final View directTargetChild, final View target, final int nestedScrollAxes, int type)
        {
                // 确保滚动方向为垂直方向
                //make sure the scroll direction is vertical
                return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed)
        {
                super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);

                // 如果屏幕是向下滑动 而且 按钮还是可见的状态
                //If the screen is swiped down  and the button is still visible
                if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE)
                {
                        animateOut(child);
                }
                // 如果屏幕是向向上滑动 而且 按钮是不可见的状态
                //If the screen is swiped up  and the button is still visible
                else if (dyConsumed < 0 && child.getVisibility() == View.INVISIBLE)
                {
                        animateIn(child);
                }
        }

        // FAB隐藏动画
        //hide FAB
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
        //show FAB
        private void animateIn(FloatingActionButton button)
        {
                button.show();
        }


}
