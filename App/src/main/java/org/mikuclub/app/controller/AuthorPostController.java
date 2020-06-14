package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.adapter.AuthorAdapter;
import org.mikuclub.app.delegate.UserDelegate;
import org.mikuclub.app.javaBeans.response.SingleAuthor;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

/**
 * 作者页面
 * 获取作者信息 和 相关文章列表的请求控制器
 * Author page
 * request controller to get author info and list of posts relative the author
 */
public class AuthorPostController extends PostController
{
        /* 额外变量 Additional variables */
        //下拉刷新后 需要跳转到的item位置
        private int scrollPositionAfterRefresh = 1;
        //请求作者信息用的请求信号标
        private boolean  wantMoreAuthor = true;
        //请求代理人
        private UserDelegate userDelegate;


        public AuthorPostController(Context context)
        {
                super(context);
        }

        /**
         * 获取用户信息的请求
         */
        public void getAuthor(int authorId){
                //如果信号标是开的
                if(wantMoreAuthor)
                {
                        //关闭信号标
                        wantMoreAuthor = false;
                        //创建请求回调方法
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        //获取用户信息
                                        Author author = ParserUtils.fromJson(response, SingleAuthor.class).getBody();
                                        //添加到适配器里
                                        ((AuthorAdapter)getRecyclerViewAdapter()).setAuthor(author);
                                        //显示头部布局
                                        ((AuthorAdapter) getRecyclerViewAdapter()).setMoreAuthorInfo(true);
                                        //通知头部更新
                                        getRecyclerViewAdapter().notifyItemChanged(0);

                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置信号标
                                        wantMoreAuthor = true;
                                }
                        };

                        //发送请求
                        startUserDelegate(httpCallBack, authorId);
                }
        }

        /**
         * 启动代理人发送请求
         *
         * @param httpCallBack
         * @param authorId
         */
        private void startUserDelegate(HttpCallBack httpCallBack, int authorId)
        {
                userDelegate.getAuthor(httpCallBack, authorId);
        }


        public void setUserDelegate(UserDelegate userDelegate)
        {
                this.userDelegate = userDelegate;
        }
}
