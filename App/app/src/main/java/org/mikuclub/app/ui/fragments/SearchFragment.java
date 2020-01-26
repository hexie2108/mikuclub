package org.mikuclub.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.controller.SearchPostController;
import org.mikuclub.app.delegates.PostDelegate;

import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.SearchActivity;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.List;

import mikuclub.app.R;

public class SearchFragment extends Fragment
{
        /*变量*/
        //数据请求代理人
        private PostDelegate delegate;
        private SearchPostController controller;
        //列表适配器
        private PostsAdapter recyclerViewAdapter;
        //列表数据
        private List<Post> recyclerDataList;

        /*组件*/
        private RecyclerView recyclerView;
        //搜索栏组件
        private EditText searchInput;
        //搜索图标
        private ImageView searchInputIcon;

        //搜索内容
        private String queryString;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_search, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);


                //绑定组件
                recyclerView = view.findViewById(R.id.recycler_view);

                //创建数据请求 代理人
                delegate = new PostDelegate(((SearchActivity) getActivity()).TAG);
                recyclerDataList = new ArrayList<>();

                //初始化列表
                initRecyclerView();

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
                super.onActivityCreated(savedInstanceState);

                //初始化 活动上的组件
                initSearchInput();
                //初始化 活动上的浮动按钮
                initFloatingActionButton();
                //初始化控制器
                initController();
        }



        /**
         * 初始化 空的文章列表
         */
        private void initRecyclerView()
        {

                recyclerViewAdapter = new PostsAdapter(recyclerDataList, getActivity());

                //创建网格布局
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberColumn);
                //让最后一个组件(尾部) 占据2个列
                layoutManager.setSpanSizeLookup(new MyGridLayoutSpanSizeLookup(recyclerDataList, numberColumn, false));

                //创建列表滑动监听器
                MyListOnScrollListener listener = new MyListOnScrollListener(recyclerViewAdapter, layoutManager)
                {
                        @Override
                        public void onExecute()
                        {
                                //加载更多
                                controller.getMore();
                        }
                };

                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE * 2, true, true, listener);

        }

        /**
         * 初始化控制器
         */
        private void initController()
        {
                //设置查询参数
                PostParameters parameters = new PostParameters();

                //创建数据控制器
                controller = new SearchPostController(getActivity());
                controller.setDelegate(delegate);
                controller.setRecyclerView(recyclerView);
                controller.setRecyclerViewAdapter(recyclerViewAdapter);
                controller.setRecyclerDataList(recyclerDataList);
                controller.setParameters(parameters);
                controller.setWantMore(false);
        }


        /**
         * 初始化搜索框
         */
        private void initSearchInput()
        {

                //绑定activity里的组件
                searchInput = ((SearchActivity) getActivity()).getSearchInput();
                searchInputIcon = ((SearchActivity) getActivity()).getSearchInputIcon();
                //弹出键盘+获取焦点
                KeyboardUtils.showKeyboard(searchInput);

                //监听键盘动作
                searchInput.setOnEditorActionListener((v, actionId, event) -> {
                        //从键盘点了搜索键
                        if (actionId == EditorInfo.IME_ACTION_SEARCH)
                        {
                                //触发搜索图标的点击事件
                                searchInputIcon.performClick();
                        }
                        return true;
                });

                //监听是否有焦点
                searchInput.setOnFocusChangeListener((v, hasFocus) -> {
                        //如果有焦点
                        if (hasFocus)
                        {
                                //显示图标
                                searchInputIcon.setVisibility(View.VISIBLE);
                                //把光标移动到末尾
                                searchInput.setSelection(searchInput.getText().length());
                        }
                        //如果无焦点
                        else
                        {
                                //隐藏图标
                                searchInputIcon.setVisibility(View.INVISIBLE);

                        }
                });

                //搜索图标点击监听器
                searchInputIcon.setOnClickListener(v -> {
                        //发送搜索请求
                        sendSearch();
                });

        }

        /**
         * 发送搜索请求
         */
        private void sendSearch()
        {
                //获取搜索内容, 去除左右空格
                queryString = searchInput.getText().toString().trim();
                //只有搜索内容不是空的情况 并且不在搜索中的状态
                if (!queryString.isEmpty())
                {
                        //取消当前的请求
                        Request.cancelRequest(SearchActivity.TAG);
                        //隐藏键盘+取消焦点
                        KeyboardUtils.hideKeyboard(searchInput);

                        controller.setQuery(queryString);
                        controller.refreshPosts(1);
                }
        }



        /**
         * 为父活动上的浮动按钮点击事件绑定动作
         */
        private void initFloatingActionButton()
        {
                ((SearchActivity) getActivity()).getFloatingActionButton().setOnClickListener(v -> {
                        controller.openJumPageAlertDialog();
                });
        }

}
