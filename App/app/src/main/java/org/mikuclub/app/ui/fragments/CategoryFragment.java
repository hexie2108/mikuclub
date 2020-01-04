package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.listener.MyListOnScrollListener;
import org.mikuclub.app.callBack.CallBack;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.CategoryActivity;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.controller.PostController;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.custom.MyGridLayoutSpanSizeLookup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

public class CategoryFragment extends Fragment
{

        //文章列表
        private RecyclerView recyclerView;
        private PostsAdapter recyclerViewAdapter;
        private List<Post> recyclerDataList;

        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;

        //数据请求代理人
        private PostDelegate delegate;
        private Category category;

        //信号标 是否要加载新数据 默认开启
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
                View root = inflater.inflate(R.layout.fragment_category, container, false);

                return root;


        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                //获取传递的数据
                category = (Category) getArguments().getSerializable("category");

                //绑定组件
                recyclerView = view.findViewById(R.id.recycler_view);
                swipeRefresh = view.findViewById(R.id.swipe_refresh);

                //创建数据请求 代理人
                delegate = new PostDelegate(((CategoryActivity) getActivity()).TAG);

                recyclerDataList = new ArrayList<>();
                currentPage = 0;
                totalPage = -1;

                //初始化列表
                initRecyclerView();

                //配置下拉刷新
                initSwipeRefresh();

        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState)
        {
                super.onActivityCreated(savedInstanceState);

                //初始化 活动上的浮动按钮
                initFloatingActionButton();
        }

        @Override
        public void onStart()
        {
                super.onStart();
                //每次开始的时候请求一次数据 (解决中途切换活动导致的不加载问题)
                getMore();
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
         * 初始化 空的文章列表
         */
        private void initRecyclerView()
        {

                //创建数据适配器
                recyclerViewAdapter = new PostsAdapter(recyclerDataList, getActivity());

                //创建网格布局
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberColumn);
                //让最后一个组件(尾部) 占据2个列
                layoutManager.setSpanSizeLookup(new MyGridLayoutSpanSizeLookup(recyclerDataList, numberColumn, false));

                //创建列表滑动监听器
                MyListOnScrollListener listener = new MyListOnScrollListener(recyclerViewAdapter, layoutManager){
                        @Override
                        public void onExecute()
                        {
                                //加载更多
                                getMore();
                        }
                };

                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, GlobalConfig.NUMBER_PER_PAGE * 2, true, true, listener);

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
                                        Posts newPostList = ParserUtils.posts(response);
                                        //插入新数据
                                        recyclerDataList.addAll(newPostList.getBody());
                                        //通知增加了对应位置的数据
                                        recyclerViewAdapter.notifyItemRangeInserted(recyclerDataList.size(), newPostList.getBody().size());

                                        //重新开启信号标
                                        wantMore = true;
                                        //当前页数+1
                                        currentPage++;
                                        //如果是第一次获取总页数
                                        if(totalPage == -1)
                                        {
                                                totalPage = newPostList.getHeaders().getTotalPage();
                                        }
                                }

                                //内容错误的情况
                                @Override
                                public void onError()
                                {
                                        recyclerViewAdapter.setNotMoreError(true);
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //网络失败
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

                                @Override
                                public void onCancel()
                                {
                                        //重置信号标
                                        wantMore = true;

                                }
                        };

                        delegate.getCategoryPostList(category.getId(), currentPage + 1, httpCallBack);
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



                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        //成功
                        @Override
                        public void onSuccess(String response)
                        {

                                Posts postList = ParserUtils.posts(response);
                                recyclerDataList.clear();
                                recyclerDataList.addAll(postList.getBody());

                                //重置网络错误
                                recyclerViewAdapter.setInternetError(false, null);
                                //重置内容错误
                                recyclerViewAdapter.setNotMoreError(false);
                                //更新数据
                                recyclerViewAdapter.notifyDataSetChanged();

                                //返回顶部
                                recyclerView.scrollToPosition(0);

                                //更新当前页数
                                currentPage = page;

                        }

                        @Override
                        public void onError()
                        {
                                Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onHttpError()
                        {
                                onError();
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
                delegate.getCategoryPostList(category.getId(), page, httpCallBack);
        }




        /**
         * 为父活动上的浮动按钮点击事件绑定动作
         */
        private void initFloatingActionButton()
        {
                ((CategoryActivity) getActivity()).getFloatingActionButton().setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                PostController.openAlertDialog((AppCompatActivity) getActivity(), currentPage, totalPage, new CallBack()
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


        /**
         * 本碎片的静态启动方法
         *
         * @param category
         * @return
         */
        public static CategoryFragment startAction(Category category)
        {
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", category);
                CategoryFragment fragment = new CategoryFragment();
                fragment.setArguments(bundle);
                return fragment;
        }

}
