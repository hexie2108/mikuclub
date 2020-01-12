package org.mikuclub.app.controller;

import android.content.Context;
import android.widget.EditText;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.delegates.BaseDelegate;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.ParserUtils;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 文章列表控制器
 */
public class SearchPostController extends PostController
{
        /*额外变量*/
        //下拉刷新后 需要跳转到的item位置
        private int scrollPositionAfterRefresh = 0;
        //查询的内容
        private String query;


        /*额外组件*/
        //下拉刷新布局
        private EditText searchInput;


        public SearchPostController(Context context, BaseDelegate delegate, RecyclerView recyclerView, EditText searchInput, BaseParameters parameters)
        {
                super(context, delegate, recyclerView, null,parameters);

        }


        /**
         * 搜索页专用
         * 搜索文章+跳转功能
         *
         * @param page 请求文章的页数
         */
        @Override
        public void refreshPosts(final int page)
        {
                //关闭信号标
                setWantMore(false);
                //清空旧数据
                getRecyclerDataList().clear();
                //开启加载进度条
                getRecyclerViewAdapter().updateFooterStatus(true, false, false);
                //更新数据
                getRecyclerViewAdapter().notifyDataSetChanged();
                //返回顶部
                getRecyclerView().scrollToPosition(scrollPositionAfterRefresh);

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        //成功
                        @Override
                        public void onSuccess(String response)
                        {
                                //解析新数据
                                Posts newPosts = ParserUtils.posts(response);
                                //添加数据到列表
                                getRecyclerDataList().addAll(newPosts.getBody());
                                //重置列表尾部状态
                                getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                //更新数据
                                getRecyclerViewAdapter().notifyDataSetChanged();
                                //更新当前页数
                                setCurrentPage(page);
                                //更新总页数
                                setTotalPage(newPosts.getHeaders().getTotalPage());
                                //重新开启信号标
                                setWantMore(true);

                        }

                        @Override
                        public void onError(String response)
                        {
                                getRecyclerViewAdapter().setNotMoreErrorMessage("抱歉, 没有找到相关内容");
                                getRecyclerViewAdapter().updateFooterStatus(false, true, false);
                        }

                        @Override
                        public void onHttpError()
                        {
                                getRecyclerViewAdapter().setInternetErrorListener(v->{
                                        refreshPosts(page);
                                });
                                getRecyclerViewAdapter().updateFooterStatus(false, false, true);
                        }


                        //如果请求被取消
                        @Override
                        public void onCancel()
                        {
                                getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                        }
                };

                startDelegate(httpCallBack, page);
        }

        /**
         * 启动代理人发送请求
         *
         * @param httpCallBack
         * @param page
         */
        private void startDelegate(HttpCallBack httpCallBack, int page)
        {
                PostParameters parameters = (PostParameters) getParameters();
                parameters.setSearch(query);
                ((PostDelegate) getDelegate()).getPostList(httpCallBack, page, parameters);
        }


        public String getQuery()
        {
                return query;
        }

        public void setQuery(String query)
        {
                this.query = query;
        }
}
