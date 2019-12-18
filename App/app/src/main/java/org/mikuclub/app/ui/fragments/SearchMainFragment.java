package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.contexts.MyApplication;
import org.mikuclub.app.delegates.PostDelegate;

import org.mikuclub.app.ui.activity.HomeActivity;
import org.mikuclub.app.utils.RecyclerViewUtils;

import mikuclub.app.R;

public class SearchMainFragment extends Fragment
{
        private PostDelegate searchPresenter;

        //recycler view
        private EasyRecyclerView recyclerView;
        private RecyclerArrayAdapter<org.mikuclub.app.javaBeans.resources.Post> adapter;

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

                searchPresenter = new PostDelegate(HomeActivity.TAG);


                initRecyclerView();


        }



        private void initRecyclerView()
        {
                //create adapter
                adapter = new RecyclerArrayAdapter<org.mikuclub.app.javaBeans.resources.Post>(this.getActivity())
                {
                        @Override
                        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType)
                        {
                                //disccopiamento del adapter e viewHolder
                                //return new PostListHolder(parent);
                                return null;
                        }
                };
                //set default error and noMore view
                RecyclerViewUtils.setDefaultErrorAndNoMoreForAdapter(adapter);


                //in case of get more
                adapter.setMore(R.layout.list_item_more, new RecyclerArrayAdapter.OnMoreListener()
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
                                                callBackForRecyclerView();
                                        }
                                }, 1000);
                        }
                });

                //visualize the empty list
                //recyclerView.setVisibility(View.INVISIBLE);
                //adapter.clear();
        }


        public void callBackForRecyclerView()
        {


                WrapperCallBack wrapperCallBack = new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {

//                                Posts posts = (Posts) response;
//                                ArrayList<org.mikuclub.app.javaBeans.resources.Post> arrayList = posts.getBody();
//                                adapter.addAll(arrayList);


                        }

                        @Override
                        public void onError()
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

                //recyclerView.showProgress();

        }
}
