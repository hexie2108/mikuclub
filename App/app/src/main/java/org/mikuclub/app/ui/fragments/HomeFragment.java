package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.holders.RecentlyPostsHolder;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.presenter.HomePresenter;
import org.mikuclub.app.utils.RecyclerViewUtils;
import org.mikuclub.app.utils.httpUtils.Connection;

import java.util.ArrayList;

import mikuclub.app.R;

public class HomeFragment extends Fragment
{
        private HomePresenter homePresenter;

        //recycler view
        private EasyRecyclerView recyclerView;
        private RecyclerArrayAdapter<Post> adapter;

        //slidershow banner
        private BannerViewPager bannerViewPager;
        //indecator of slidershow
        private ZoomIndicator zoomIndicator;

        private View header;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.home_fragment, container, false);


        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                homePresenter = new HomePresenter();

                header = getActivity().getLayoutInflater().inflate(R.layout.home_fragment_header, null);

                initSliderShowView();
                initRecyclerView();

                adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
                        @Override
                        public View onCreateView(ViewGroup parent) {
                                return header;
                        }

                        @Override
                        public void onBindView(View headerView) {
                        }
                });



        }

        private void initSliderShowView()
        {


                bannerViewPager = (BannerViewPager) header.findViewById(R.id.loop_viewpager);
                zoomIndicator = (ZoomIndicator) header.findViewById(R.id.bottom_scale_layout);
                callBackForSliderShow();

        }

        private void callBackForSliderShow()
        {
                WrapperCallBack wrapperCallBack = new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(Object response)
                        {
                                addDataToSliderShow(response);
                        }

                };
                homePresenter.getStickyPostList(0, wrapperCallBack);
        }

        private void addDataToSliderShow(Object response)
        {
                Posts posts = (Posts) response;

                PageBean bean = new PageBean.Builder<Post>()
                        .setDataObjects(posts.getBody())
                        .setIndicator(zoomIndicator)
                        .builder();

                // animation of slider
                // MzTransformer, DepthPageTransformer，ZoomOutPageTransformer
                //mBannerCountViewPager.setPageTransformer(false, new DepthPageTransformer());

                bannerViewPager.setPageListener(bean, R.layout.slider_view_item, new PageHelperListener<Post>()
                {
                        @Override
                        public void getItemView(View view, Post data)
                        {

                                NetworkImageView imageView = view.findViewById(R.id.loop_img);
                                String imgUrl = data.getMetadata().getThumbnail_img_src().get(0);
                                Connection.getImg(imgUrl, imageView);
                                TextView textView = view.findViewById(R.id.loop_text);
                                textView.setText(data.getTitle().getRendered());

                                //for eventual listener
                        }
                });
        }


        private void initRecyclerView()
        {
                //create adapter
                adapter = new RecyclerArrayAdapter<Post>(this.getActivity())
                {
                        @Override
                        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
                        {
                                //disccopiamento del adapter e viewHolder
                                return new RecentlyPostsHolder(parent);
                        }
                };
                //set default error and noMore view
                RecyclerViewUtils.setDefaultErrorAndNoMoreForAdapter(adapter);


                //in case of get more
                adapter.setMore(R.layout.recycler_view_more, new RecyclerArrayAdapter.OnMoreListener()
                {
                        @Override
                        public void onMoreShow()
                        {
                                callBackForRecyclerView();
                        }

                        @Override
                        public void onMoreClick()
                        {
                        }
                });


                //click event
                adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener()
                {
                        @Override
                        public void onItemClick(int position)
                        {
                                //position不包含Header
                                Toast.makeText(MyApplication.getContext(),
                                        position + "", Toast.LENGTH_LONG).show();
                        }
                });

                //long click event
                adapter.setOnItemLongClickListener(new RecyclerArrayAdapter.OnItemLongClickListener()
                {
                        @Override
                        public boolean onItemLongClick(int position)
                        {
                                Toast.makeText(MyApplication.getContext(),
                                        position + "", Toast.LENGTH_LONG).show();
                                return true;
                        }
                });

                //create recyclerView
                recyclerView = getView().findViewById(R.id.homeRecentlyPosts);
                //initial recyclerView
                RecyclerViewUtils.initRecyclerView(recyclerView, this.getActivity());
                //mount adapter
                recyclerView.setAdapterWithProgress(adapter);
                //refresh the recycleView
                recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
                {
                        @Override
                        public void onRefresh()
                        {
                                recyclerView.postDelayed(new Runnable()
                                {
                                        @Override
                                        public void run()
                                        {
                                                adapter.clear();
                                                callBackForRecyclerView();
                                        }
                                }, 1000);
                        }
                });

                callBackForRecyclerView();
        }


        private void callBackForRecyclerView()
        {
                WrapperCallBack wrapperCallBack = new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(Object response)
                        {
                                Posts posts = (Posts) response;
                                ArrayList<Post> arrayList = posts.getBody();
                                adapter.addAll(arrayList);
                        }

                        @Override
                        public void onErrorHappened()
                        {

                                recyclerView.showError();
                        }

                };
                int nextStart = adapter.getCount() + 1;
                homePresenter.getRecentlyPostList(nextStart, wrapperCallBack);
        }


}
