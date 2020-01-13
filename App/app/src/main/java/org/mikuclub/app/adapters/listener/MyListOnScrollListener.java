package org.mikuclub.app.adapters.listener;

import org.mikuclub.app.configs.GlobalConfig;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract  class MyListOnScrollListener extends RecyclerView.OnScrollListener
{
        private int lastVisibleItem;
        private LinearLayoutManager manager;
        private RecyclerView.Adapter adapter;

        /**
         * 传递适配器 和 布局组件
         * @param adapter
         * @param manager
         */
        public MyListOnScrollListener(RecyclerView.Adapter adapter , LinearLayoutManager manager)
        {
                this.adapter = adapter;
                this.manager = manager;
        }

        //满足条件时, 触发的方法
        // 需要被重写
        public abstract void onExecute();

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
        {
                //滑动停止的时候
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
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
