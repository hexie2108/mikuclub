package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.controller.base.BaseController;
import org.mikuclub.app.delegate.MessageDelegate;
import org.mikuclub.app.javaBeans.response.HomeReplyComments;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

/**
 * 获取主页评论回复列表的请求控制器
 * request controller to get  home comment reply list
 */
public class HomeReplyCommentController extends BaseController
{
        /* 额外变量 Additional variables */
        //下拉刷新后 需要跳转到的item位置
        private int scrollPositionAfterRefresh = 0;

        /*额外组件*/
        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;

        public HomeReplyCommentController(Context context)
        {
                super(context);
        }



        @Override
        public void getMore()
        {
                //检查信号标
                if (isWantMore())
                {
                        //关闭信号标
                        setWantMore(false);
                        //显示尾部加载
                        getRecyclerViewAdapter().updateFooterStatus(true, false, false);

                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                //成功的情况
                                @Override
                                public void onSuccess(String response)
                                {
                                        //解析数据
                                        HomeReplyComments homeReplyComments = ParserUtils.fromJson(response, HomeReplyComments.class);
                                        //加载数据
                                        getRecyclerDataList().addAll(homeReplyComments.getBody());
                                        //通知列表更新, 获取正确的插入位置, 排除可能的头部造成的偏移
                                        int position = getRecyclerDataList().size() + getRecyclerViewAdapter().getHeaderRow();
                                        getRecyclerViewAdapter().notifyItemInserted(position);

                                        //当前页数+1
                                        setCurrentPage(getCurrentPage() + 1);

                                        //重新开启信号标
                                        setWantMore(true);
                                        //隐藏尾部加载进度条
                                        getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                }

                                //请求结果包含错误的情况
                                //结果主体为空, 无更多内容
                                @Override
                                public void onError(String response)
                                {
                                        getRecyclerViewAdapter().updateFooterStatus(false, true, false);
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        getRecyclerViewAdapter().setInternetErrorListener(v -> {
                                                //重置请求状态
                                                setWantMore(true);
                                                getMore();
                                        });
                                        getRecyclerViewAdapter().updateFooterStatus(false, false, true);
                                }

                                //取消请求的情况
                                @Override
                                public void onCancel()
                                {
                                        setWantMore(true);
                                        //隐藏尾部加载进度条
                                        getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                }
                        };

                        startDelegate(httpCallBack, getCurrentPage() + 1);
                }
        }



        /**
         * 下拉刷新文章+跳转功能
         *
         * @param page 请求文章的页数
         */
        public void refreshPosts(int page)
        {
                setWantMore(false);

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
                                //解析新数据
                                HomeReplyComments homeReplyComments = ParserUtils.fromJson(response, HomeReplyComments.class);
                                //清空旧数据
                                getRecyclerDataList().clear();
                                //添加数据到列表
                                getRecyclerDataList().addAll(homeReplyComments.getBody());

                                //重置列表尾部状态
                                getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                //更新数据
                                getRecyclerViewAdapter().notifyDataSetChanged();

                                //更新当前页数
                                setCurrentPage(page);
                                //返回顶部
                                getRecyclerView().scrollToPosition(scrollPositionAfterRefresh);
                        }

                        @Override
                        public void onError(String response)
                        {
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.general_toast_message_on_error));
                        }


                        //请求结束后
                        @Override
                        public void onFinally()
                        {
                                //关闭加载进度条
                                swipeRefresh.setRefreshing(false);
                                setWantMore(true);
                        }

                        //如果请求被取消
                        @Override
                        public void onCancel()
                        {
                                onFinally();
                        }
                };

                ((MessageDelegate) getDelegate()).getReplyComment(httpCallBack, 1);
        }


        /**
         * 启动代理人发送请求
         *
         * @param httpCallBack
         */
        private void startDelegate(HttpCallBack httpCallBack, int page)
        {
                ((MessageDelegate) getDelegate()).getReplyComment(httpCallBack,  page);
        }


        public void setSwipeRefresh(SwipeRefreshLayout swipeRefresh)
        {
                this.swipeRefresh = swipeRefresh;
        }
}
