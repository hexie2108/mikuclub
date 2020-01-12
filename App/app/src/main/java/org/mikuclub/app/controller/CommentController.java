package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.delegates.BaseDelegate;
import org.mikuclub.app.delegates.CommentDelegate;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.utils.ParserUtils;

import androidx.recyclerview.widget.RecyclerView;

public class CommentController extends BaseController
{


        public CommentController(Context context , BaseDelegate delegate, RecyclerView recyclerView, BaseParameters parameters)
        {
                super(context, delegate, recyclerView, parameters);
        }

        /*
       加载更多
        */
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
                                        Comments newComments = ParserUtils.comments(response);
                                        //加载数据
                                        getRecyclerDataList().addAll(newComments.getBody());
                                        //通知列表更新, 获取正确的插入位置, 排除可能的头部造成的偏移
                                        int position = getRecyclerDataList().size()+getRecyclerViewAdapter().getHeaderRow();
                                        getRecyclerViewAdapter().notifyItemInserted(position);

                                        //当前页数+1
                                        setCurrentPage(getCurrentPage()+1);
                                        //如果是还未获取过总页数
                                        if (getTotalPage() == -1)
                                        {
                                                setTotalPage(newComments.getHeaders().getTotalPage());
                                        }

                                        //如果还未到最后一页
                                        if (getCurrentPage() < getTotalPage())
                                        {
                                                //重新开启信号标
                                                setWantMore(true);
                                        }
                                        //如果已经到最后一页了
                                        else
                                        {
                                                //调用错误处理方法
                                                onError(null);
                                        }
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
                                }
                        };

                        startDelegate(httpCallBack);
                }
        }

        /**
         * 启动代理人发送请求
         * @param httpCallBack
         */
        private void startDelegate(HttpCallBack httpCallBack)
        {
                ( (CommentDelegate)getDelegate()).getCommentList(httpCallBack, getCurrentPage()+1, (CommentParameters) getParameters());
        }



}
