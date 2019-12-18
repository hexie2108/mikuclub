package org.mikuclub.app.ui.fragments;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.listener.ErrorFooterListener;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.utils.CustomGridLayoutSpanSizeLookup;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;
import org.mikuclub.app.utils.http.Request;

import java.util.List;

import mikuclub.app.R;

public class HomeMainFragment extends Fragment
{

        private PostDelegate postDelegate;

        //文章列表
        private RecyclerView recyclerView;
        private PostsAdapter postsAdapter;
        private List<Post> recyclerDataList;

        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;

        private NestedScrollView nestedScrollView;

        //首页幻灯片
        private BannerViewPager bannerViewPager;
        private ZoomIndicator zoomIndicator;

        //储存数据
        private Posts postList;
        //是否还有新的文章数据 (默认是有)
        private boolean areMorePosts = true;
        //是否要加载新数据 默认是
        private boolean wantMore = true;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {


                // 为fragment加载主布局
                View root = inflater.inflate(R.layout.fragment_home_main, container, false);

                postDelegate = new PostDelegate(HomeActivity.TAG);
                //获取组件
                bannerViewPager = root.findViewById(R.id.loop_viewpager);
                zoomIndicator = root.findViewById(R.id.bottom_scale_layout);
                recyclerView = root.findViewById(R.id.recycler_view);
                swipeRefresh = root.findViewById(R.id.swipe_refresh);

                nestedScrollView = root.findViewById(R.id.nested_scroll_view);

                //从intent里读取上个活动传送来的数据
                Posts stickyPostList = (Posts) getActivity().getIntent().getSerializableExtra("sticky_post_list");
                Posts postList = (Posts) getActivity().getIntent().getSerializableExtra("post_list");
                //加载幻灯片
                initSliders(stickyPostList);
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

        /**
         * 初始化幻灯片
         *
         * @param postList
         */
        private void initSliders(Posts postList)
        {

                PageBean bean = new PageBean.Builder<Post>()
                        .setDataObjects(postList.getBody())
                        .setIndicator(zoomIndicator)
                        .builder();

                // animation of slider
                // MzTransformer, DepthPageTransformer，ZoomOutPageTransformer
                //bannerViewPager.setPageTransformer(false, new DepthPageTransformer());

                bannerViewPager.setPageListener(bean, R.layout.slider_view_item, new PageHelperListener<Post>()
                {
                        @Override
                        public void getItemView(View view, Post data)
                        {

                                String imgUrl = data.getMetadata().getThumbnail_img_src().get(0);
                                NetworkImageView imageView = view.findViewById(R.id.item_image);
                                Request.getRemoteImg(imageView, imgUrl);

                                TextView textView = view.findViewById(R.id.item_text);
                                textView.setText(data.getTitle().getRendered());

                        }
                });
        }

        /**
         * 初始化文章列表
         *
         * @param postList
         */
        private void initRecyclerView(Posts postList)
        {
                recyclerDataList = postList.getBody();
                postsAdapter = new PostsAdapter(recyclerDataList);
                recyclerView.setAdapter(postsAdapter);

                //设置网格布局
                GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
                //让最后一个组件(进度条组件) 占据2个列
                manager.setSpanSizeLookup(new CustomGridLayoutSpanSizeLookup(recyclerDataList, 2));
                //加载布局
                recyclerView.setLayoutManager(manager);
                // recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);

                //设置滚动事件监听器
                nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
                {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
                        {
                                //检测距离列表底部的距离, 扣除 特地的距离 方便提前加载
                                if (scrollY >= ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) - v.getMeasuredHeight() * GlobalConfig.LIST_PRE_LOAD_HEIGHT_RATION))
                                {
                                        //获取最新文章
                                        getMore();
                                }
                        }
                });

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
                                //成功
                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.e(response);
                                        postList = Parser.posts(response);
                                        recyclerDataList.addAll(postList.getBody());
                                        postsAdapter.notifyItemInserted(recyclerDataList.size());
                                        //重新开启信号标
                                        wantMore = true;

                                }

                                //内容错误的情况
                                @Override
                                public void onError()
                                {
                                        refreshNotMoreErrorHandler();
                                }

                                //网络失败
                                @Override
                                public void onHttpError()
                                {
                                        refreshHttpErrorHandler();
                                }

                        };

                        int nextStart = recyclerDataList.size();
                        LogUtils.e("发送请求 " + nextStart);
                        postDelegate.getRecentlyPostList(nextStart, wrapperCallBack);
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
                                postList = Parser.posts(response);
                                recyclerDataList.clear();
                                recyclerDataList.addAll(postList.getBody());
                                postsAdapter.notifyDataSetChanged();

                        }

                        //请求结束后
                        @Override
                        public void onFinally()
                        {
                                //关闭进度条
                                swipeRefresh.setRefreshing(false);
                                wantMore = true;
                        }

                };
                int nextStart = 0;
                postDelegate.getRecentlyPostList(nextStart, wrapperCallBack);
        }

        /**
         * 自动加载发生错误的情况  就停止自动刷新, 改成手动触发
         */
        private void refreshHttpErrorHandler()
        {

                ErrorFooterListener.setupHttpErrorSchema(recyclerView.findViewHolderForAdapterPosition(recyclerDataList.size()), "加载失败, 请点击重试", new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                wantMore = true;
                                getMore();
                        }
                });
        }

        /**
         * 自动加载 没有更多数据的情况  停止自动刷新, 提示用户没有了
         */
        private void refreshNotMoreErrorHandler()
        {
                ErrorFooterListener.setupNotMoreErrorSchema(recyclerView.findViewHolderForAdapterPosition(recyclerDataList.size()), "已经到底了~");
        }


}
