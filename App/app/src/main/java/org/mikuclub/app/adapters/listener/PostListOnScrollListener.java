package org.mikuclub.app.adapters.listener;

import org.mikuclub.app.configs.GlobalConfig;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostListOnScrollListener extends RecyclerView.OnScrollListener
{
        private int lastVisibleItem;
        private GridLayoutManager gridLayoutManager;
        private RecyclerView.Adapter adapter;

        /**
         * 传递适配器 和 布局组件
         * @param adapter
         * @param gridLayoutManager
         */
        public PostListOnScrollListener(RecyclerView.Adapter adapter , GridLayoutManager gridLayoutManager)
        {
                this.adapter = adapter;
                this.gridLayoutManager = gridLayoutManager;

        }

        //满足条件时, 触发的方法
        // 需要被重写
        public void onExecute(){

        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
        {
                //滑动停止的时候
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                        //获取列表最后一个可见item的位置
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                        //LogUtils.e("lastitem: " + lastVisibleItem + " / total: " + homeListAdapter.getItemCount());

                        //如果可见item 到达了加载范围里, 触发加载
                        if (lastVisibleItem >= (adapter.getItemCount() - 1) - GlobalConfig.PRE_LOAD_ITEM_NUMBER)
                        {
                                //触发方法
                                onExecute();
                        }
                }


        }






}
