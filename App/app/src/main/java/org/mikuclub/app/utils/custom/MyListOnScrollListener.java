package org.mikuclub.app.utils.custom;

import org.mikuclub.app.config.GlobalConfig;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义recyclerView 滚动监听器
 * Custom recyclerView scroll listener
 */
public abstract  class MyListOnScrollListener extends RecyclerView.OnScrollListener
{
        private int lastVisibleItem;
        private LinearLayoutManager manager;
        private RecyclerView.Adapter adapter;

        /**
         * 默认构建函数
         * @param adapter 适配器
         * @param manager 布局管理器
         */
        public MyListOnScrollListener(RecyclerView.Adapter adapter , LinearLayoutManager manager)
        {
                this.adapter = adapter;
                this.manager = manager;
        }

        /**
         * 满足条件后 被调用的方法
         */
        public abstract void onExecute();

        /**
         * 列表滚动时触发
         * @param recyclerView
         * @param newState
         */
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
        {

                //滑动停止的时候
                if (newState == RecyclerView.SCROLL_STATE_IDLE  )
                {

                        //获取列表最后一个可见item的位置
                        lastVisibleItem = manager.findLastVisibleItemPosition();
                        //如果可见item 到达了加载范围里, 触发加载
                        if (lastVisibleItem >= (adapter.getItemCount() - 1) - GlobalConfig.PRE_LOAD_ITEM_NUMBER)
                        {
                                //触发方法
                                onExecute();
                        }
                }


        }






}
