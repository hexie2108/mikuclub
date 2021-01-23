package org.mikuclub.app.utils;

import org.mikuclub.app.utils.custom.MyListOnScrollListener;

import java.lang.reflect.Field;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * recyclerView通用初始化配置
 * recyclerView general initialization configuration
 */
public class RecyclerViewUtils
{

        /**
         * recycler通用初始化配置
         *
         * @param recyclerView              列表组件
         * @param adapter                   数据适配器
         * @param layout                    列表容器布局
         * @param cacheSize                 缓存item的数量
         * @param hasFixedSize              是否每个item都一样大
         * @param hasNestedScrollingEnabled 列表被内嵌的时候的滑动支持
         * @param myListOnScrollListener    滑动监听器
         */
        public static void setup(RecyclerView recyclerView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layout, int cacheSize, boolean hasFixedSize, boolean hasNestedScrollingEnabled, MyListOnScrollListener myListOnScrollListener)
        {

                //配置recyclerView
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layout);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(cacheSize);
                //item是否都一样大
                recyclerView.setHasFixedSize(hasFixedSize);
                //是否支持内嵌滑动
                recyclerView.setNestedScrollingEnabled(hasNestedScrollingEnabled);

                if (myListOnScrollListener != null)
                {
                        //绑定滑动事件
                        recyclerView.addOnScrollListener(myListOnScrollListener);
                }


        }

        /**
         * 降低文章页 分页的横向滑动敏感度 , ViewPager2新版本可能会失效
         * @param viewPager
         */
        public static void reduceViewPagerHorizontalScrollSensibility(ViewPager2 viewPager)
        {
                //越大越不敏感, 滑动需要的动作越大
                int sensitivity = 8;

                try
                {
                        final Field recyclerViewField = ViewPager2.class.getDeclaredField("mRecyclerView");
                        recyclerViewField.setAccessible(true);

                        final RecyclerView recyclerView = (RecyclerView) recyclerViewField.get(viewPager);

                        final Field touchSlopField = RecyclerView.class.getDeclaredField("mTouchSlop");
                        touchSlopField.setAccessible(true);

                        final int touchSlop = (int) touchSlopField.get(recyclerView);
                        touchSlopField.set(recyclerView, touchSlop * sensitivity);
                }
                catch (Exception ignore)
                {
                }
        }


}
