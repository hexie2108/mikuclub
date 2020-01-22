package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.adapters.AuthorAdapter;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.delegates.BaseDelegate;
import org.mikuclub.app.delegates.UserDelegate;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.resources.base.User;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 文章列表控制器
 */
public class AuthorPostController extends PostController
{
        /*额外变量*/
        //下拉刷新后 需要跳转到的item位置
        private int scrollPositionAfterRefresh = 1;
        //给请求用户信息用的信号标
        private boolean  wantMoreAuthor = true;
        private UserDelegate userDelegate;


        public AuthorPostController(Context context)
        {
                super(context);
        }

        /**
         * 获取用户信息
         */
        public void getAuthor(int authorId){
                //如果信号标是开的
                if(wantMoreAuthor)
                {
                        //关闭信号标
                        wantMoreAuthor = false;
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        //获取用户信息
                                        User user = ParserUtils.userAuthor(response).getBody();
                                        //添加到适配器里
                                        ((AuthorAdapter)getRecyclerViewAdapter()).setUser(user);
                                        //显示头部布局
                                        ((AuthorAdapter) getRecyclerViewAdapter()).setHeaderVisible(true);
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
                        userDelegate.getUser(httpCallBack, authorId);
                }
        }


        public void setUserDelegate(UserDelegate userDelegate)
        {
                this.userDelegate = userDelegate;
        }
}
