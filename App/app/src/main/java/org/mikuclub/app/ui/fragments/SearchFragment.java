package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.listener.ErrorFooterListener;
import org.mikuclub.app.adapters.listener.ManageInfoUtilView;
import org.mikuclub.app.adapters.listener.PostListOnScrollListener;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostDelegate;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.SearchActivity;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.view.CustomGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.Parser;

import java.security.Key;
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


        //搜索栏组件
        private EditText searchInput;
        //搜索图标
        private ImageView searchInputIcon;
        //错误信息提示组件
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

                //绑定组件
                recyclerView = root.findViewById(R.id.recycler_view);
                //绑定信息组件
                manageInfoUtilView = new ManageInfoUtilView(root);

                //初始化列表
                initRecyclerView();

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
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                //让最后一个组件(进度条组件) 占据2个列
                gridLayoutManager.setSpanSizeLookup(new CustomGridLayoutSpanSizeLookup(recyclerDataList, 2, false));
                //加载布局
                recyclerView.setLayoutManager(gridLayoutManager);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(GlobalConfig.NUMBER_FOR_PAGE * 2);
                //所有item大小一样
                recyclerView.setHasFixedSize(true);

                //绑定滑动事件
                recyclerView.addOnScrollListener(new PostListOnScrollListener(postsAdapter, gridLayoutManager)
                {
                        //只有满足位置条件才会触发方法
                        @Override
                        public void onExecute()
                        {
                                //加载更多
                                getMore();
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
                //弹出键盘+获取焦点
                KeyboardUtils.showKeyboard(searchInput);

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
                                if (hasFocus)
                                {
                                        //显示搜索图标
                                        searchInputIcon.setVisibility(View.VISIBLE);
                                }
                                //如果无焦点
                                else
                                {
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

                        //隐藏键盘+取消焦点
                        KeyboardUtils.hideKeyboard(searchInput);

                        //显示加载进度条
                        manageInfoUtilView.setLoadingInfo();
                        //返回顶部
                        recyclerView.scrollToPosition(0);
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
                                                //清空旧数据
                                                recyclerDataList.clear();
                                                //添加数据到列表
                                                recyclerDataList.addAll(postList.getBody());
                                                //重置网络错误
                                                postsAdapter.setInternetError(false, null);
                                                //如果返回的文章等于规定数量, 正常
                                                if(postList.getBody().size() == GlobalConfig.NUMBER_FOR_PAGE){
                                                        //开启信号灯
                                                        wantMore = true;
                                                        //重置没有更多错误
                                                        postsAdapter.setNotMoreError(false);
                                                }
                                                else{
                                                        //开启没有更多错误
                                                        postsAdapter.setNotMoreError(true);
                                                }
                                                //通知列表更新
                                                postsAdapter.notifyDataSetChanged();

                                                //隐藏信息提示加载进度条
                                                manageInfoUtilView.setVisibility(false);
                                                //显示列表
                                                recyclerView.setVisibility(View.VISIBLE);

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

        /*
        加载更多
         */
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
                                        postsAdapter.setNotMoreError(true);
                                        //通知更新尾部
                                        postsAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        postsAdapter.setInternetError(true, new View.OnClickListener()
                                                {
                                                        @Override
                                                        public void onClick(View v)
                                                        {
                                                                //重置请求状态
                                                                wantMore = true;
                                                                //重置错误显示
                                                                postsAdapter.setInternetError(false, null);
                                                                getMore();
                                                        }
                                                });
                                        //通知更新尾部
                                        postsAdapter.notifyItemChanged(recyclerDataList.size());
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
