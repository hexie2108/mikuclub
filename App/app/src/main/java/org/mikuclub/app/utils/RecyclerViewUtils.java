package org.mikuclub.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;

import mikuclub.app.R;

public class RecyclerViewUtils
{

        public static void initRecyclerView(EasyRecyclerView recyclerView, Context activity){

                recyclerView.setLayoutManager(new LinearLayoutManager(activity));

                //设置分割线
                DividerDecoration decoration = new DividerDecoration(Color.RED, 2, 20, 20);
                decoration.setDrawLastItem(false);
                recyclerView.addItemDecoration(decoration);

                //添加边框
                SpaceDecoration itemDecoration = new SpaceDecoration((int) GeneralUtils.convertDpToPixel(8,activity));
                itemDecoration.setPaddingEdgeSide(true);
                itemDecoration.setPaddingStart(true);
                itemDecoration.setPaddingHeaderFooter(false);
                recyclerView.addItemDecoration(itemDecoration);



        }

        public static <T> void setDefaultErrorAndNoMoreForAdapter(RecyclerArrayAdapter<T> adapter){

                //set default view per no more case
                adapter.setNoMore(R.layout.recycler_view_nomore);
                //set default view per error case
                adapter.setError(R.layout.recycler_view_error, new RecyclerArrayAdapter.OnErrorListener()
                {
                        @Override
                        public void onErrorShow()
                        {
                                Log.d("TAG", getClass() + ":\n" + "onErrorShow:");
                                //                adapter.resumeMore();
                        }

                        @Override
                        public void onErrorClick()
                        {
                                Log.d("TAG", getClass() + ":\n" + "onErrorClick:");
                                //                adapter.resumeMore();
                        }
                });
        }




}
