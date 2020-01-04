package org.mikuclub.app.controller;

import android.view.View;

import org.mikuclub.app.adapters.CommentsAdapter;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.CommentDelegate;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.javaBeans.resources.Comments;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CommentController
{

        //数据请求代理人
        private CommentDelegate delegate;
        //列表组件
        private RecyclerView recyclerView;
        //列表适配器
        private CommentsAdapter recyclerViewAdapter;
        //列表数据
        private List<Comment> recyclerDataList;
        //信号标 决定是否要加载新数据
        private boolean wantMore = true;
        //总页数
        private int totalPage = -1;
        //当前页数
        private int currentPage = 0;

        public CommentController(CommentDelegate delegate, RecyclerView recyclerView, CommentsAdapter recyclerViewAdapter, List<Comment> recyclerDataList)
        {
                this.delegate = delegate;
                this.recyclerView = recyclerView;
                this.recyclerViewAdapter = recyclerViewAdapter;
                this.recyclerDataList = recyclerDataList;
        }

        /*
       加载更多
        */
        public void getMore(int postId, int commentParentId)
        {
                //检查信号标
                if (wantMore)
                {
                        //关闭信号标
                        wantMore = false;
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                //成功的情况
                                @Override
                                public void onSuccess(String response)
                                {
                                        //解析数据
                                        Comments commentList = ParserUtils.comments(response);
                                        //加载数据
                                        recyclerDataList.addAll(commentList.getBody());
                                        //通知更新  (插入位置, 和新插入的数量)
                                        recyclerViewAdapter.notifyItemInserted(recyclerDataList.size());

                                        //当前页数+1
                                        currentPage++;
                                        //如果是还未获取过总页数
                                        if (totalPage == -1)
                                        {
                                                totalPage = commentList.getHeaders().getTotalPage();
                                        }

                                        //如果还未到最后一页
                                        if (currentPage < totalPage)
                                        {
                                                //重新开启信号标
                                                wantMore = true;
                                        }
                                        //如果已经到最后一页了
                                        else
                                        {
                                                //调用错误处理方法
                                                onError();
                                        }
                                }

                                //请求结果包含错误的情况
                                //结果主体为空, 无更多内容
                                @Override
                                public void onError()
                                {
                                        recyclerViewAdapter.setNotMoreError(true);
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        recyclerViewAdapter.setInternetError(true, v -> {
                                                //重置请求状态
                                                wantMore = true;
                                                //重置错误显示
                                                recyclerViewAdapter.setInternetError(false, null);
                                                getMore(postId, commentParentId);
                                        });
                                        //通知更新尾部
                                        recyclerViewAdapter.notifyItemChanged(recyclerDataList.size());
                                }

                                //取消请求的情况
                                @Override
                                public void onCancel()
                                {
                                        wantMore = true;
                                }
                        };
                        LogUtils.e(postId+"/"+commentParentId);
                        //委托代理人发送请求
                        delegate.getCommentsListByPostId(postId, commentParentId, currentPage + 1, httpCallBack);
                }
        }

}
