package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.zhengsr.viewpagerlib.indicator.ZoomIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.holders.PostListHolder;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.presenter.SearchPresenter;
import org.mikuclub.app.utils.RecyclerViewUtils;

import java.util.ArrayList;

import mikuclub.app.R;

public class SearchMainFragment extends Fragment
{
        private SearchPresenter searchPresenter;

        //recycler view
        private EasyRecyclerView recyclerView;
        private RecyclerArrayAdapter<Post> adapter;

        //query string
        private String query;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.recycler_fragment, container, false);


        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                searchPresenter = new SearchPresenter();

                initRecyclerView();


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
                                return new PostListHolder(parent);
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
                recyclerView = getView().findViewById(R.id.recycler_view);
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
                                                recyclerView.showProgress();

                                                callBackForRecyclerView();
                                        }
                                }, 1000);
                        }
                });

                //visualize the empty list
                //recyclerView.showEmpty();

                recyclerView.showEmpty();
        }


        public void callBackForRecyclerView()
        {
                WrapperCallBack wrapperCallBack = new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(Object response)
                        {
                                Posts posts = (Posts) response;
                                ArrayList<Post> arrayList = posts.getBody();
                                adapter.addAll(arrayList);
                                recyclerView.showRecycler();
                        }

                        @Override
                        public void onErrorHappened()
                        {
                                recyclerView.showError();

                        }

                };
                int nextStart = adapter.getCount() + 1;
                searchPresenter.getPostListBySearch( query, nextStart, wrapperCallBack);
        }

        public void setQuery(String query)
        {
                this.query = query;
        }
}
