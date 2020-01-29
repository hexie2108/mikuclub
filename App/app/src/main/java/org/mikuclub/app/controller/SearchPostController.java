package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.Posts;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import mikuclub.app.R;

/**
 * 根据搜索内容获取文章列表的请求控制器
 * request controller to get  post list by keyword
 */
public class SearchPostController extends PostController
{
        /* 额外变量 Additional variables */
        //下拉刷新后 需要跳转到的item位置
        private int scrollPositionAfterRefresh = 0;
        //查询的内容
        private String query;


        public SearchPostController(Context context)
        {
                super(context);
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
                                Posts newPosts = ParserUtils.fromJson(response, Posts.class);
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
                        public void onTokenError()
                        {
                                getRecyclerViewAdapter().setNotMoreErrorMessage(ResourcesUtils.getString(R.string.login_token_error));
                                getRecyclerViewAdapter().updateFooterStatus(false, true, false);
                        }

                        @Override
                        public void onError(String response)
                        {
                                getRecyclerViewAdapter().setNotMoreErrorMessage(ResourcesUtils.getString(R.string.search_empty_error_message));
                                getRecyclerViewAdapter().updateFooterStatus(false, true, false);
                        }

                        @Override
                        public void onHttpError()
                        {
                                getRecyclerViewAdapter().setInternetErrorListener(v -> {
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


        public void setQuery(String query)
        {
                this.query = query;
        }
}
