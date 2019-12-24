package org.mikuclub.app.ui.fragments;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.HomeListAdapter;
import org.mikuclub.app.adapters.listener.ErrorFooterListener;
import org.mikuclub.app.adapters.listener.PostListOnScrollListener;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.view.CustomGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;

import java.util.List;

import mikuclub.app.R;

public class HomeMainFragment extends Fragment
{

        private PostDelegate postDelegate;

        //文章列表
        private RecyclerView recyclerView;
        private HomeListAdapter homeListAdapter;
        private List<Post> recyclerDataList;

        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;
        //滚动布局
        private NestedScrollView nestedScrollView;

        private Posts stickyPosts;
        private List<Post> stickyPostList;

        //是否要加载新数据 默认是
        private boolean wantMore = true;

        private int start = 0;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {


                // 为fragment加载主布局
                View root = inflater.inflate(R.layout.fragment_home_main, container, false);

                //创建数据请求 代理人
                postDelegate = new PostDelegate(HomeActivity.TAG);


                recyclerView = root.findViewById(R.id.recycler_view);
                swipeRefresh = root.findViewById(R.id.swipe_refresh);


                //从intent里读取上个活动传送来的数据
                stickyPosts = (Posts) getActivity().getIntent().getSerializableExtra("sticky_post_list");
                stickyPostList = stickyPosts.getBody();
                Posts postList = (Posts) getActivity().getIntent().getSerializableExtra("post_list");
                recyclerDataList = postList.getBody();

                //加载文章列表
                initRecyclerView(postList);

                //配置下拉刷新
                swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
                swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
                {
                        @Override
                        public void onRefresh()
                        {
                                refreshPosts();
                        }
                });

                return root;
        }

        @Override
        public void onStart()
        {
                super.onStart();
                //每次开始的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                getMore();
        }


        /**
         * 初始化文章列表
         *
         * @param postList
         */
        private void initRecyclerView(Posts postList)
        {
                //创建适配器
                homeListAdapter = new HomeListAdapter(recyclerDataList, stickyPostList);
                recyclerView.setAdapter(homeListAdapter);

                //设置网格布局
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                //让最后一个组件(进度条组件) 占据2个列
                gridLayoutManager.setSpanSizeLookup(new CustomGridLayoutSpanSizeLookup(recyclerDataList, 2, true));
                //加载布局
                recyclerView.setLayoutManager(gridLayoutManager);
                //recyclerView.setHasFixedSize(true);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(GlobalConfig.NUMBER_FOR_PAGE*2);
                //绑定滑动事件
                recyclerView.addOnScrollListener(new PostListOnScrollListener(homeListAdapter, gridLayoutManager)
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
         * 加载更多
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
                                //成功
                                @Override
                                public void onSuccess(String response)
                                {

                                        //解析新数据
                                        Posts newPostList = Parser.posts(response);
                                        //插入新数据
                                        recyclerDataList.addAll(newPostList.getBody());
                                        //通知增加了对应位置的数据
                                        homeListAdapter.notifyItemRangeInserted(recyclerDataList.size(), GlobalConfig.NUMBER_FOR_PAGE);

                                        //重新开启信号标
                                        wantMore = true;

                                }

                                //内容错误的情况
                                @Override
                                public void onError()
                                {
                                        homeListAdapter.setNotMoreError(true);
                                        //通知更新尾部
                                        homeListAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //网络失败
                                @Override
                                public void onHttpError()
                                {

                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        homeListAdapter.setInternetError(true, new View.OnClickListener()
                                        {
                                                @Override
                                                public void onClick(View v)
                                                {
                                                        //重置请求状态
                                                        wantMore = true;
                                                        //重置错误显示
                                                        homeListAdapter.setInternetError(false, null);
                                                        getMore();
                                                }
                                        });
                                        //通知更新尾部
                                        homeListAdapter.notifyItemChanged(recyclerDataList.size());

                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置信号标
                                        wantMore = true;

                                }
                        };

                        int start = recyclerDataList.size();
                        LogUtils.e(start + "");
                        postDelegate.getRecentlyPostList(start, wrapperCallBack);
                }
        }

        /**
         * 下拉刷新最新文章
         */

        private void refreshPosts()
        {
                wantMore = false;
                WrapperCallBack wrapperCallBack = new WrapperCallBack()
                {
                        //成功
                        @Override
                        public void onSuccess(String response)
                        {
                                Posts postList = Parser.posts(response);
                                recyclerDataList.clear();
                                recyclerDataList.addAll(postList.getBody());
                                homeListAdapter.notifyDataSetChanged();

                        }

                        //请求结束后
                        @Override
                        public void onFinally()
                        {
                                //关闭进度条
                                swipeRefresh.setRefreshing(false);
                                wantMore = true;
                        }

                        //如果请求被取消
                        @Override
                        public void onCancel()
                        {
                                //关闭加载进度条
                                swipeRefresh.setRefreshing(false);
                                //重置信号标
                                wantMore = true;
                                LogUtils.e("我被取消了");
                        }
                };
                postDelegate.getRecentlyPostList(0, wrapperCallBack);
        }


}