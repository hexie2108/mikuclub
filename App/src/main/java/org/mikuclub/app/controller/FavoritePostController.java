package org.mikuclub.app.controller;

import android.content.Context;

import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.utils.http.HttpCallBack;

/**
 * 获取收藏文章列表的请求控制器
 */
public class FavoritePostController extends PostController
{

        public FavoritePostController(Context context)
        {
                super(context);
        }

        @Override
        protected void startDelegate(HttpCallBack httpCallBack, int page)
        {
                ((PostDelegate) getDelegate()).getFavoritePostList(httpCallBack, page, (PostParameters) getParameters());
        }
}
