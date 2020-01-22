package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.adapters.BaseAdapter;
import org.mikuclub.app.delegates.BaseDelegate;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 基础控制器原型
 */
public abstract class BaseController
{
        /*变量*/
        //数据请求代理人
        private BaseDelegate delegate;
        //列表适配器
        private BaseAdapter recyclerViewAdapter;
        //列表数据
        private List recyclerDataList;
        //请求参数
        private BaseParameters parameters;
        //信号标 决定是否要加载新数据
        private boolean wantMore = true;
        //总页数
        private int totalPage = -1;
        //当前页数
        private int currentPage = 0;
        private Context context;

        /*组件*/
        //列表组件
        private RecyclerView recyclerView;


        public BaseController(Context context)
        {
                this.context = context;
        }

        /**
         * 加载更多
         */
        public abstract void getMore();





        public BaseDelegate getDelegate()
        {
                return delegate;
        }

        public void setDelegate(BaseDelegate delegate)
        {
                this.delegate = delegate;
        }

        public BaseAdapter getRecyclerViewAdapter()
        {
                return recyclerViewAdapter;
        }

        public void setRecyclerViewAdapter(BaseAdapter recyclerViewAdapter)
        {
                this.recyclerViewAdapter = recyclerViewAdapter;
        }

        public List getRecyclerDataList()
        {
                return recyclerDataList;
        }

        public void setRecyclerDataList(List recyclerDataList)
        {
                this.recyclerDataList = recyclerDataList;
        }

        public BaseParameters getParameters()
        {
                return parameters;
        }

        public void setParameters(BaseParameters parameters)
        {
                this.parameters = parameters;
        }

        public boolean isWantMore()
        {
                return wantMore;
        }

        public int getTotalPage()
        {
                return totalPage;
        }

        public void setTotalPage(int totalPage)
        {
                this.totalPage = totalPage;
        }

        public int getCurrentPage()
        {
                return currentPage;
        }

        public void setCurrentPage(int currentPage)
        {
                this.currentPage = currentPage;
        }

        public Context getContext()
        {
                return context;
        }

        public void setContext(Context context)
        {
                this.context = context;
        }

        public RecyclerView getRecyclerView()
        {
                return recyclerView;
        }

        public void setRecyclerView(RecyclerView recyclerView)
        {
                this.recyclerView = recyclerView;
        }

        public void setWantMore(boolean wantMore)
        {
                this.wantMore = wantMore;
        }


}
