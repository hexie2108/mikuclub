package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.listener.ErrorFooterListener;
import org.mikuclub.app.adapters.listener.ManageInfoUtilView;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostDelegate;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.ui.activity.SearchActivity;
import org.mikuclub.app.utils.CustomGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;

import java.util.ArrayList;
import java.util.List;

import mikuclub.app.R;

public class SearchFragment extends Fragment
{
        //数据请求代理人
        private PostDelegate postDelegate;

        //文章列表
        private RecyclerView recyclerView;
        private PostsAdapter postsAdapter;
        private List<Post> recyclerDataList;

        //滚动布局
        private NestedScrollView nestedScrollView;

        //搜索栏组件
        private EditText searchInput;
        //搜索图标
        private ImageView searchInputIcon;
        //键盘
        private InputMethodManager imm;

        private ManageInfoUtilView manageInfoUtilView;


        //信号标 是否要加载新数据 默认不开启
        private boolean wantMore = false;
        //信号标 是否正在搜索
        private boolean isSearching = false;
        //搜索内容
        private String queryString;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

                // 为fragment加载主布局
                View root = inflater.inflate(R.layout.fragment_search, container, false);
                //创建数据请求 代理人
                postDelegate = new PostDelegate(SearchActivity.TAG);

                recyclerView = root.findViewById(R.id.recycler_view);
                nestedScrollView = root.findViewById(R.id.nested_scroll_view);
                //绑定信息提示组件
                manageInfoUtilView = new ManageInfoUtilView(root);

                //获取键盘
                imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //初始化列表
                initRecyclerView();

                // Inflate the layout for this fragment
                return root;


        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
                super.onActivityCreated(savedInstanceState);

                //初始化 活动上的组件
                initSearchInput();

        }

        @Override
        public void onStart()
        {
                super.onStart();

                //如果数据列表不是空的
                if (!recyclerDataList.isEmpty())
                {
                        //每次恢复页面的时候追加请求一次数据 (解决中途切换活动导致的不加载问题)
                        getMore();
                }
        }


        /**
         * 初始化 空的文章列表
         */
        private void initRecyclerView()
        {
                //隐藏信息提示框(加载进度条)
                manageInfoUtilView.setVisibility(false);

                recyclerDataList = new ArrayList<Post>();
                postsAdapter = new PostsAdapter(recyclerDataList);
                recyclerView.setAdapter(postsAdapter);

                //设置网格布局
                GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
                //让最后一个组件(进度条组件) 占据2个列
                manager.setSpanSizeLookup(new CustomGridLayoutSpanSizeLookup(recyclerDataList, 2));
                //加载布局
                recyclerView.setLayoutManager(manager);
                //兼容nestedScroll布局
                recyclerView.setNestedScrollingEnabled(false);

                //设置滚动事件监听器
                nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
                {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
                        {
                                //  LogUtils.e(scrollY+" / "+(v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()));
                                //检测距离列表底部的距离, 扣除 特地的距离 方便提前加载
                                if (scrollY >= ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) - v.getMeasuredHeight() * GlobalConfig.LIST_PRE_LOAD_HEIGHT_RATION))
                                {
                                        //加载新数据
                                        getMore();
                                }
                        }
                });

        }

        /**
         * 初始化搜索框
         */
        private void initSearchInput()
        {


                //绑定activity里的组件
                searchInput = getActivity().findViewById(R.id.search_input);
                searchInputIcon = getActivity().findViewById(R.id.search_input_icon);
                //获取焦点
                searchInput.requestFocus();
                //弹出键盘
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //监听键盘动作
                searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                        {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                                {
                                        //发送搜索请求
                                        sendSearch();
                                }
                                return true;
                        }
                });
                //监听是否有焦点
                searchInput.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus)
                        {
                                //如果有焦点
                                if(hasFocus){
                                        //显示搜索图标
                                        searchInputIcon.setVisibility(View.VISIBLE);
                                }
                                //如果无焦点
                                else{
                                        //隐藏搜索图标
                                        searchInputIcon.setVisibility(View.INVISIBLE);
                                }

                        }
                });
                //监听搜索图标
                searchInputIcon.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                //发送搜索请求
                                sendSearch();
                        }
                });


        }

        /**
         * 发送搜索请求
         */
        public void sendSearch()
        {
                //获取搜索内容, 去除左右空格
                queryString = searchInput.getText().toString().trim();
                //只有搜索内容不是空的情况 并且不在搜索中的状态
                if (!queryString.isEmpty() && !isSearching)
                {
                        //开启信号标 正在搜索
                        isSearching = true;
                        //关闭信号标 不要自动加载
                        wantMore = false;
                        //移除搜索栏的focus状态
                        searchInput.clearFocus();
                        //隐藏键盘
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        //显示加载进度条
                       manageInfoUtilView.setLoadingInfo();
                       //隐藏 列表
                        recyclerView.setVisibility(View.GONE);

                        WrapperCallBack wrapperCallBack = new WrapperCallBack()
                        {
                                //成功的情况
                                @Override
                                public void onSuccess(String response)
                                {

                                        //解析新数据
                                        Posts postList = Parser.posts(response);
                                        //如果搜索结果不是空的
                                        if (!postList.getBody().isEmpty())
                                        {

                                                //只有在不是空列表的情况
                                               if(!recyclerDataList.isEmpty())
                                               {
                                                       //重新生成尾部功能组件
                                                       recyclerView.removeViewAt(recyclerDataList.size());
                                               }
                                                //清空旧数据
                                                recyclerDataList.clear();
                                                //添加数据到列表
                                                recyclerDataList.addAll(postList.getBody());
                                                //通知列表更新
                                                postsAdapter.notifyDataSetChanged();
                                                //显示列表
                                                recyclerView.setVisibility(View.VISIBLE);
                                                //隐藏信息提示加载进度条
                                                manageInfoUtilView.setVisibility(false);

                                                //开启信号标
                                                wantMore = true;
                                        }
                                        //如果搜索结果是空的
                                        else
                                        {
                                                //提示用户
                                                manageInfoUtilView.setErrorInfo("抱歉, 没有找到相关内容", null);
                                        }

                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //提示用户, 绑定动作允许用户手动重试
                                        manageInfoUtilView.setErrorInfo("搜索失败, 请点击重试", new View.OnClickListener()
                                        {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                        sendSearch();
                                                }
                                        });

                                }

                                //请求结束后
                                @Override
                                public void onFinally()
                                {
                                        //关闭 信号标
                                        isSearching = false;
                                }

                                //请求被取消的情况
                                @Override
                                public void onCancel()
                                {
                                        //关闭信号标
                                        isSearching = false;
                                        //隐藏信息提示组件
                                       manageInfoUtilView.setVisibility(false);
                                }
                        };
                        //获取当前 数据长度
                        int nextStart = recyclerDataList.size();
                        //委托代理人发送请求
                        postDelegate.getPostListBySearch(queryString, nextStart, wrapperCallBack);


                }
        }

        private void getMore()
        {
                //检查信号标
                if (wantMore)
                {
                        //关闭信号标
                        wantMore = false;

                        WrapperCallBack wrapperCallBack = new WrapperCallBack()
                        {
                                //成功的情况
                                @Override
                                public void onSuccess(String response)
                                {
                                        //解析数据
                                        Posts postList = Parser.posts(response);
                                        //加载数据
                                        recyclerDataList.addAll(postList.getBody());
                                        //通知更新
                                        postsAdapter.notifyItemInserted(recyclerDataList.size());
                                        //重新开启信号标
                                        wantMore = true;
                                }

                                //内容错误的情况
                                //400 无更多内容
                                @Override
                                public void onError()
                                {
                                        ErrorFooterListener.refreshNotMoreErrorHandler(recyclerView, recyclerDataList);
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        ErrorFooterListener.refreshHttpErrorHandler(recyclerView, recyclerDataList, new View.OnClickListener()
                                        {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                        wantMore = true;
                                                        getMore();
                                                }
                                        });
                                }

                                //取消请求的情况
                                @Override
                                public void onCancel()
                                {
                                        wantMore = true;
                                }
                        };
                        //获取当前 数据长度
                        int nextStart = recyclerDataList.size();
                        //委托代理人发送请求
                        postDelegate.getPostListBySearch(queryString, nextStart, wrapperCallBack);
                }
        }


}
