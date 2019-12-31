package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.adapters.HomeListAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.callBack.CallBack;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostsDelegate;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;
import org.mikuclub.app.utils.PostListUtils;
import org.mikuclub.app.view.CustomGridLayoutSpanSizeLookup;
import org.mikuclub.app.view.EditTextNumberFilterMinMax;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

public class HomeMainFragment extends Fragment
{

        private PostsDelegate delegate;

        //文章列表
        private RecyclerView recyclerView;
        private HomeListAdapter recyclerViewAdapter;
        private List<Post> recyclerDataList;

        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;


        private Posts stickyPosts;
        private List<Post> stickyPostList;


        //是否要加载新数据 默认是
        private boolean wantMore = true;
        //当前页数
        private int currentPage;
        //总页数
        private int totalPage;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

                // 为fragment加载主布局
                View root = inflater.inflate(R.layout.fragment_home_main, container, false);

                return root;
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                recyclerView = view.findViewById(R.id.recycler_view);
                swipeRefresh = view.findViewById(R.id.swipe_refresh);

                //创建数据请求 代理人
                delegate = new PostsDelegate(HomeActivity.TAG);
                //从intent里读取上个活动传送来的数据
                stickyPosts = (Posts) getActivity().getIntent().getSerializableExtra("sticky_post_list");
                stickyPostList = stickyPosts.getBody();
                Posts postList = (Posts) getActivity().getIntent().getSerializableExtra("post_list");
                recyclerDataList = postList.getBody();

                //设置总页数
                totalPage = postList.getHeaders().getTotalPage();
                //设置当前页数
                currentPage = 1;
                //加载文章列表
                initRecyclerView(postList);

                //配置下拉刷新
                initSwipeRefresh();
        }

        @Override
        public void onStart()
        {
                super.onStart();
                //每次开始的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                getMore();
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
                super.onActivityCreated(savedInstanceState);
                initFloatingActionButton();
        }

        /**
         * 初始化文章列表
         *
         * @param postList
         */
        private void initRecyclerView(Posts postList)
        {
                //创建适配器
                recyclerViewAdapter = new HomeListAdapter(recyclerDataList, stickyPostList, getActivity());
                recyclerView.setAdapter(recyclerViewAdapter);

                //设置网格布局
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                //让最后一个组件(进度条组件) 占据2个列
                gridLayoutManager.setSpanSizeLookup(new CustomGridLayoutSpanSizeLookup(recyclerDataList, 2, true));
                //加载布局
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setHasFixedSize(false);
                //缓存item的数量
                recyclerView.setItemViewCacheSize(GlobalConfig.NUMBER_PER_PAGE * 2);
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
         * 配置上拉刷新动作
         */
        private void initSwipeRefresh()
        {
                //设置进度条颜色
                swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
                //绑定动作
                swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
                {
                        @Override
                        public void onRefresh()
                        {
                                //获取最新文章
                                refreshPosts(1);
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

                        HttpCallBack httpCallBack = new HttpCallBack()
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
                                        recyclerViewAdapter.notifyItemRangeInserted(recyclerDataList.size() + 1, newPostList.getBody().size());

                                        //重新开启信号标
                                        wantMore = true;
                                        //当前页数+1
                                        currentPage++;

                                }

                                //内容错误的情况
                                @Override
                                public void onError()
                                {
                                        recyclerViewAdapter.setNotMoreError(true);
                                        //通知更新尾部, 因为有头部存在,额外+1
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size() + 1);
                                }

                                //网络失败
                                @Override
                                public void onHttpError()
                                {
                                        LogUtils.e("onHttpError");
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
                                        //通知更新尾部 , 因为有头部存在,额外+1
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size() + 1);

                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置信号标
                                        wantMore = true;

                                }
                        };

                        delegate.getPostList(currentPage + 1, httpCallBack);
                }
        }

        /**
         * 拉刷新最新文章
         *
         * @param page 请求文章的页数
         */
        private void refreshPosts(final int page)
        {
                wantMore = false;

                //如果加载进度条没有出现 (跳转页面情况)
                if (!swipeRefresh.isRefreshing())
                {
                        //让加载进度条显示
                        swipeRefresh.setRefreshing(true);
                }

                //返回顶部
                recyclerView.scrollToPosition(1);

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        //成功
                        @Override
                        public void onSuccess(String response)
                        {
                                Posts postList = Parser.posts(response);
                                recyclerDataList.clear();
                                recyclerDataList.addAll(postList.getBody());

                                //重置网络错误
                                recyclerViewAdapter.setInternetError(false, null);
                                //重置内容错误
                                recyclerViewAdapter.setNotMoreError(false);
                                //更新数据
                                recyclerViewAdapter.notifyDataSetChanged();

                                //更新当前页数
                                currentPage = page;

                        }

                        @Override
                        public void onHttpError()
                        {
                                Toast.makeText(getActivity(), "请求失败, 请重试", Toast.LENGTH_SHORT).show();
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
                        }
                };
                delegate.getPostList(page, httpCallBack);
        }

        /**
         * 为父活动上的浮动按钮点击事件绑定动作
         */
        private void initFloatingActionButton()
        {
                ((HomeActivity) getActivity()).getListFloatingActionButton().setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                PostListUtils.openAlertDialog((AppCompatActivity) getActivity(), currentPage, totalPage, new CallBack()
                                {
                                        @Override
                                        public void execute(String... args)
                                        {
                                                //确保有参数被传送回来
                                                if (args.length > 0)
                                                {
                                                        int page = Integer.valueOf(args[0]);
                                                        refreshPosts(page);
                                                }
                                        }
                                });
                        }
                });
        }


}
