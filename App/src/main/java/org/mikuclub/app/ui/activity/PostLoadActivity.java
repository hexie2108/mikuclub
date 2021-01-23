package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.response.SinglePost;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.base.MyActivity;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import androidx.appcompat.app.AlertDialog;
import mikuclub.app.R;

/**
 * 加载文章跳转页
 */
public class PostLoadActivity extends MyActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 20;

        public static final String INTENT_POST_ID = "pos_id";

        /* 变量 local variable */

        private int postId;
        private PostDelegate delegate;

        /* 组件 views */

        //创建进度条弹窗
        private AlertDialog progressDialog;
        private AlertDialog confirmDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_post_load);

                postId = getIntent().getIntExtra(INTENT_POST_ID, 0);

                //如果等于0
                if (postId == 0)
                {
                        //结束窗口并 显示错误显示弹窗
                        ToastUtils.longToast(ResourcesUtils.getString(R.string.error_message_missing_post_id));
                        finish();
                }

                //准备通过id请求文章
                prepareGetPost();

        }

        @Override
        protected void onStart()
        {
                super.onStart();

                //发送请求
                getPostData();

        }

        /**
         * 准备请求文章
         * prepare request to get post
         */
        private void prepareGetPost()
        {
                //创建请求代理扔
                delegate = new PostDelegate(TAG);
                //创建进度条弹窗
                progressDialog = AlertDialogUtils.createProgressDialog(this, true, true);
                //弹窗确认按钮点击事件监听
                DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
                        //重试请求
                        getPostData();
                };
                //弹窗取消按钮点击事件监听
                DialogInterface.OnClickListener negativeClickListener = (dialog, which) -> {
                        //关闭当前页面
                        finish();
                };
                //创建重试弹窗
                confirmDialog = AlertDialogUtils.createConfirmDialog(this, ResourcesUtils.getString(R.string.post_get_by_id_error_message), null, true, true, ResourcesUtils.getString(R.string.retry), positiveClickListener, ResourcesUtils.getString(R.string.cancel), negativeClickListener);

        }


        /**
         * 发送请求
         * send request to get post
         */
        private void getPostData()
        {

                //显示进度条
                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //获取文章数据
                                Post post = ParserUtils.fromJson(response, SinglePost.class).getBody();
                                //启动文章页
                                PostActivity.startAction(PostLoadActivity.this, post);
                                //销毁当前加载页
                                finish();
                                overridePendingTransition(0,0);
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                //隐藏进度条弹窗
                                progressDialog.dismiss();
                                //弹出确认窗口 允许用户重试
                                confirmDialog.show();
                        }

                        @Override
                        public void onHttpError()
                        {
                                //隐藏进度条弹窗
                                progressDialog.dismiss();
                                onError(null);
                        }

                        @Override
                        public void onFinally()
                        {
                        }

                        @Override
                        public void onCancel()
                        {
                                //隐藏进度条弹窗
                                progressDialog.dismiss();
                        }
                };
                delegate.getPost(httpCallBack, postId);

        }


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();

        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity2
         * 只提供post id
         *
         * @param context
         * @param postId
         */
        public static void startAction(Context context, int postId)
        {
                Intent intent = new Intent(context, PostLoadActivity.class);
                intent.putExtra(INTENT_POST_ID, postId);

                context.startActivity(intent);
        }


}
