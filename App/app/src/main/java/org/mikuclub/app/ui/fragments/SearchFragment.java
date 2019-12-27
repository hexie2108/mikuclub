package org.mikuclub.app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.listener.ManageInfoUtilView;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostsDelegate;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.SearchActivity;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.view.CustomGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.Parser;

import java.util.ArrayList;
import java.util.List;

import mikuclub.app.R;

public class SearchFragment extends Fragment
{

        //文章列表
        private RecyclerView recyclerView;
        private PostsAdapter recyclerViewAdapter;
        private List<Post> recyclerDataList;

        //数据请求代理人
        private PostsDelegate delegate;

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

                return root;


        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);


                //绑定组件
                recyclerView = view.findViewById(R.id.recycler_view);
                //绑定信息组件
                manageInfoUtilView = new ManageInfoUtilView(view);

                //创建数据请求 代理人
                delegate = new PostsDelegate(((SearchActivity) getActivity()).TAG);
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

                recyclerViewAdapter = new PostsAdapter(recyclerDataList, getActivity());
                recyclerView.setAdapter(recyclerViewAdapter);

                //设置网格布局
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                //让最后一个组件(进度条组件) 占据2个列
                gridLayoutManager.setSpanSizeLookup(new CustomGridLayoutSpanSizeLookup(recyclerDataList, 2, false));
                //加载布局
                recyclerView.setLayoutManager(gridLayoutManager);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(GlobalConfig.NUMBER_PER_PAGE * 2);
                //所有item大小一样
                recyclerView.setHasFixedSize(true);

                //绑定滑动事件
                recyclerView.addOnScrollListener(new MyListOnScrollListener(recyclerViewAdapter, gridLayoutManager)
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


                //创建2个搜索图标的点击监听器
                final View.OnClickListener sendSearchListener = new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                //发送搜索请求
                                sendSearch();
                        }
                };
                final View.OnClickListener cancelInputListener = new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                //清空输入框
                                searchInput.setText("");
                                //隐藏图标
                                searchInputIcon.setVisibility(View.INVISIBLE);
                        }
                };


                //监听是否有焦点
                searchInput.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus)
                        {
                                //如果有焦点
                                if (hasFocus)
                                {
                                        //显示图标
                                        searchInputIcon.setVisibility(View.VISIBLE);
                                        //改变图标
                                        searchInputIcon.setImageResource(R.drawable.search);
                                        //绑定对应动作监听器
                                        searchInputIcon.setOnClickListener(sendSearchListener);
                                        //把光标移动到末尾
                                        searchInput.setSelection(searchInput.getText().length());
                                }
                                //如果无焦点
                                else
                                {
                                        //如果搜索内容不是空的
                                        if (searchInput.getText().toString().length() > 0)
                                        {
                                                //显示图标
                                                searchInputIcon.setVisibility(View.VISIBLE);
                                                //改变图标
                                                searchInputIcon.setImageResource(R.drawable.baseline_clear);
                                                //绑定对应动作监听器
                                                searchInputIcon.setOnClickListener(cancelInputListener);
                                        }
                                        else
                                        {
                                                //如果是空的 就隐藏图标
                                                searchInputIcon.setVisibility(View.INVISIBLE);
                                        }

                                }
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
                                        //清空旧数据
                                        recyclerDataList.clear();
                                        //添加数据到列表
                                        recyclerDataList.addAll(postList.getBody());
                                        //重置网络错误
                                        recyclerViewAdapter.setInternetError(false, null);
                                        //如果返回的文章等于规定数量, 正常
                                        if (postList.getBody().size() == GlobalConfig.NUMBER_PER_PAGE)
                                        {
                                                //开启信号灯
                                                wantMore = true;
                                                //重置没有更多错误
                                                recyclerViewAdapter.setNotMoreError(false);
                                        }
                                        else
                                        {
                                                //开启没有更多错误
                                                recyclerViewAdapter.setNotMoreError(true);
                                        }
                                        //通知列表更新
                                        recyclerViewAdapter.notifyDataSetChanged();

                                        //隐藏信息提示加载进度条
                                        manageInfoUtilView.setVisibility(false);
                                        //显示列表
                                        recyclerView.setVisibility(View.VISIBLE);


                                }

                                //网络正常, 请求结果有问题的情况
                                @Override
                                public void onError()
                                {
                                        //请求结果为空
                                        //提示用户
                                        manageInfoUtilView.setErrorInfo("抱歉, 没有找到相关内容", null);

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
                        int offset = recyclerDataList.size();
                        //委托代理人发送请求
                        delegate.getPostsListBySearch(queryString, offset, wrapperCallBack);
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
                                        recyclerViewAdapter.notifyItemInserted(recyclerDataList.size());
                                        //重新开启信号标
                                        wantMore = true;
                                }

                                //请求结果包含错误的情况
                                //结果主体为空, 无更多内容
                                @Override
                                public void onError()
                                {
                                        recyclerViewAdapter.setNotMoreError(true);
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        recyclerViewAdapter.setInternetError(true, new View.OnClickListener()
                                        {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                        //重置请求状态
                                                        wantMore = true;
                                                        //重置错误显示
                                                        recyclerViewAdapter.setInternetError(false, null);
                                                        getMore();
                                                }
                                        });
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
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
                        delegate.getPostsListBySearch(queryString, nextStart, wrapperCallBack);
                }
        }

}
