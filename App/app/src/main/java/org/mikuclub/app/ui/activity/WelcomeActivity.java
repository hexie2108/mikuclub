package org.mikuclub.app.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.MessageDelegate;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.delegate.UtilsDelegate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.response.AppUpdate;
import org.mikuclub.app.javaBeans.response.Posts;
import org.mikuclub.app.javaBeans.response.SingleResponse;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.service.PostPushService;
import org.mikuclub.app.storage.ApplicationPreferencesUtils;
import org.mikuclub.app.storage.CategoryPreferencesUtils;
import org.mikuclub.app.storage.MessagePreferencesUtils;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import mikuclub.app.BuildConfig;
import mikuclub.app.R;


/**
 * 启动页面
 * launch activity
 */
public class WelcomeActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 1;

        /* 变量 local variable */
        private PostDelegate postDelegate;
        private UtilsDelegate utilsDelegate;
        private MessageDelegate messageDelegate;

        private Posts stickyPostList = null;
        private Posts postList = null;
        private String categoryCache;

        //需要完成的请求的总数
        private final int REQUEST_TOTAL_NUMBER = 7;
        //已完成的请求数量 (成功和失败都算)
        private int requestCount = 0;

        /* 组件 views */
        private TextView welecomeInfoText;
        private ProgressBar welecomeProgressBar;
        private ConstraintLayout layout;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);

                welecomeInfoText = findViewById(R.id.welcome_info_text);
                welecomeProgressBar = findViewById(R.id.welcome_progress_bar);
                layout = findViewById(R.id.layout);

                //创建请求代理人
                postDelegate = new PostDelegate(TAG);
                utilsDelegate = new UtilsDelegate(TAG);
                messageDelegate = new MessageDelegate(TAG);


        }

        @Override
        protected void onStart()
        {
                super.onStart();
                //检测权限 permission check
                permissionCheck();

        }

        /**
         * 初始化页面
         * init the page
         */
        private void initPage()
        {
                //如果有网络连接
                if (HttpUtils.internetCheck(this))
                {
                        //检测登陆令牌是否失效
                        checkTokenValidity();
                        //检查更新
                        checkUpdate();
                        //获取分类信息
                        checkCategories();
                        //获取未读消息数量
                        getUnreadMessageCount();
                        //获取主页文章数据
                        getPostDataForHome();

                        //设置最新的访问时间
                        ApplicationPreferencesUtils.setLatestAccessTime();
                        //根据偏好配置 判断是否启动推送服务
                        initPostPushService();

                }
                else
               {
                        //没网络的话 就报错
                        String errorMessage = ResourcesUtils.getString(R.string.welcome_internet_not_available_error_message);
                        setErrorInfo(errorMessage);
                }
        }


        /**
         * 检测登陆令牌是否失效
         * check validity of login token for the current user
         */
        private void checkTokenValidity()
        {
                //如果用户有登陆
                if (UserPreferencesUtils.isLogin())
                {
                        LogUtils.v("开始验证登陆信息有效性");
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                //token正常
                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.v("登陆信息还有效");
                                        //增加计数器
                                        startHomeSafety();
                                }

                                //令牌错误
                                @Override
                                public void onTokenError()
                                {
                                        LogUtils.v("登陆信息已失效");
                                        //增加计数器
                                        startHomeSafety();
                                }

                                //内容错误
                                @Override
                                public void onError(WpError wpError)
                                {
                                        //增加计数器
                                        startHomeSafety();
                                }

                                //网络错误
                                @Override
                                public void onHttpError()
                                {
                                        setErrorInfo(null);
                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置请求计数器
                                        requestCount = 0;
                                }
                        };
                        //检测令牌是否还有效
                        utilsDelegate.tokenValidate(httpCallBack);

                }
                //如果未登陆
                else
                {
                        //增加计数器
                        startHomeSafety();
                }

        }

        /**
         * 检查软件更新
         * 先获取上次检查的时间, 如果超时了 就重新检查一遍
         * check if there is a new version of app
         * check the time of the last check first, and check again if it times out
         */
        private void checkUpdate()
        {

                //如果上次检查更新的有效期已过期
                if (System.currentTimeMillis() > ApplicationPreferencesUtils.getUpdateCheckExpire())
                {
                        LogUtils.v("检测时间已过期, 开始重新检测更新");
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.v("检测更新成功");
                                        //获取更新信息
                                        AppUpdate appUpdate = ParserUtils.fromJson(response, AppUpdate.class);
                                        //如果当前版本号低于新版本
                                        if (BuildConfig.VERSION_CODE < appUpdate.getBody().getVersionCode())
                                        {
                                                LogUtils.v("发现新版本");
                                                //弹出弹窗
                                                openAlertDialogToUpdate(appUpdate);
                                        }
                                        //已经是新版 或者 请求发生了错误
                                        else
                                        {
                                                //如果已经是新版了, 就写入这次的检查时间, 避免后续重复检查
                                                if (BuildConfig.VERSION_CODE == appUpdate.getBody().getVersionCode())
                                                {
                                                        LogUtils.v("已经是最新版了");
                                                        // 设置检查更新的有效期
                                                       ApplicationPreferencesUtils.setUpdateCheckExpire();
                                                }
                                                //增加计数器
                                                startHomeSafety();
                                        }
                                }

                                //内容错误的情况, 忽视, 下次再检查更新
                                @Override
                                public void onError(WpError wpError)
                                {
                                        LogUtils.v("更新信息内容有错误, 下次再检测");
                                        //增加计数器
                                        startHomeSafety();
                                }

                                //网络错误的情况, 忽视, 下次再检查更新
                                @Override
                                public void onHttpError()
                                {
                                        LogUtils.v("更新请求失败, 下次再检测");
                                        //增加计数器
                                        startHomeSafety();
                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置请求计数器
                                        requestCount = 0;
                                }
                        };
                        utilsDelegate.checkUpdate(httpCallBack);

                }
                //如果上次请求的时间 未过期 则不需要检查更新
                else
                {
                        LogUtils.v("更新时间未过期, 无需检查");
                        //增加计数器
                        startHomeSafety();
                }
        }


        /**
         * 检查分类的缓存
         * 过期或者没有缓存的话 就获取新的
         */
        private void checkCategories()
        {

                categoryCache = CategoryPreferencesUtils.getCategoryCache() ;

                //如果分类信息检测有效期已过期 或者 找不到分类缓存
                if (System.currentTimeMillis() > CategoryPreferencesUtils.getCategoryCheckExpire() || categoryCache == null)
                {
                        LogUtils.v("开始重新请求分类信息");
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {

                                @Override
                                public void onSuccess(String response)
                                {
                                        //获取分类信息
                                        categoryCache = response;
                                        //更新分类缓存 和 缓存过期时间
                                        CategoryPreferencesUtils.setCategoryCacheAndExpire(categoryCache);
                                        LogUtils.v("重新请求分类信息 成功");
                                        startHomeSafety();
                                }

                                @Override
                                public void onError(WpError wpError)
                                {
                                        //只有在无缓存的情况, 才会报错
                                        if (categoryCache.isEmpty())
                                        {
                                                setErrorInfo(null);
                                        }
                                        //有缓存的话 无视
                                        else
                                        {
                                                startHomeSafety();
                                        }
                                }

                                @Override
                                public void onHttpError()
                                {
                                        onError(null);
                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置请求计数器
                                        requestCount = 0;
                                }
                        };
                        //发送请求
                        utilsDelegate.getCategory(httpCallBack);
                }
                //直接使用缓存
                else
                {
                        LogUtils.v("已使用旧分类缓存");
                        startHomeSafety();
                }
        }

        /**
         * 获取用户未读私信和未读评论回复的数量
         *Get the counts of unread private messages and unread comment of current user
         */
        private void getUnreadMessageCount()
        {

                //如果用户有登陆
                if (UserPreferencesUtils.isLogin())
                {
                        LogUtils.v("开始获取未读消息数量");
                        //获取未读私信计数的回调
                        HttpCallBack countPrivateMessageCallBack = new HttpCallBack()
                        {

                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.v("获取未读私信数量成功");

                                        //解析回复
                                        SingleResponse singleResponse = ParserUtils.fromJson(response, SingleResponse.class);
                                        //从回复类里提取 计数, 转换成 数字, 储存到应用参数里
                                        MessagePreferencesUtils.setPrivateMessageCount(Integer.valueOf(singleResponse.getBody()));

                                }

                                @Override
                                public void onFinally()
                                {
                                        //不管是成功 , 内容错误, 登陆令牌错误, 还是网络错误, 通通无视
                                        //增加计数器
                                        startHomeSafety();
                                }
                                @Override
                                public void onCancel()
                                {
                                        //重置请求计数器
                                        requestCount = 0;
                                }
                        };
                        //获取未读评论计数的回调
                        HttpCallBack countReplyCommentCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.v("获取未读评论数量成功");
                                        //解析回复
                                        SingleResponse singleResponse = ParserUtils.fromJson(response, SingleResponse.class);
                                        //从回复类里提取 计数, 转换成 数字, 储存到应用参数里
                                        MessagePreferencesUtils.setReplyCommentCount(Integer.valueOf(singleResponse.getBody()));
                                }

                                @Override
                                public void onFinally()
                                {
                                        //不管是成功 , 内容错误, 登陆令牌错误, 还是网络错误, 通通无视
                                        //增加计数器
                                        startHomeSafety();
                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置请求计数器
                                        requestCount = 0;
                                }
                        };

                        //发送请求
                        messageDelegate.countPrivateMessage(countPrivateMessageCallBack, true, false);
                        messageDelegate.countReplyComment(countReplyCommentCallBack, true);

                }
                //如果未登陆直接增加2次计数器
                else
                {
                        //增加2次计数器
                        startHomeSafety();
                        startHomeSafety();
                }
        }

        /**
         * 获取主页需要的文章数据
         * get the post data for home
         */
        private void getPostDataForHome()
        {


                //请求置顶文章 回调函数
                HttpCallBack callBackToGetStickyPost = new HttpCallBack()
                {
                        //请求成功
                        @Override
                        public void onSuccess(String response)
                        {
                                //解析数据 +保存数据
                                stickyPostList = ParserUtils.fromJson(response, Posts.class);
                                //尝试启动主页
                                startHomeSafety();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                setErrorInfo(null);
                        }

                        //请求失败
                        @Override
                        public void onHttpError()
                        {
                                onError(null);
                        }

                        @Override
                        public void onCancel()
                        {
                                //重置请求计数器
                                requestCount = 0;
                        }
                };
                //请求普通文章 回调函数
                HttpCallBack callBackToGetPost = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                postList = ParserUtils.fromJson(response, Posts.class);
                                startHomeSafety();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                setErrorInfo(null);
                        }

                        @Override
                        public void onHttpError()
                        {
                                onError(null);
                        }

                        @Override
                        public void onCancel()
                        {
                                //重置请求计数器
                                requestCount = 0;
                        }
                };
                //只需要第一页
                int page = 1;
                //获取置顶文章
                postDelegate.getStickyPostList(callBackToGetStickyPost, page);

                //获取最新文章
                //设置请求参数
                PostParameters parameters = new PostParameters();
                parameters.setCategories_exclude(new ArrayList<>(Collections.singletonList(GlobalConfig.CATEGORY_ID_MOFA)));
                postDelegate.getPostList(callBackToGetPost, page, parameters);

        }

        /**
         * 检测请求是否都已经完成, 并且数据都已获取
         * 只有在都请求都成功的情况 才会 启动主页
         * 否则 报错
         * Check whether the requests have been completed and the data has been obtained
         * The homepage will only be launched if all requests are successful
         *Otherwise display error message
         */
        private synchronized void startHomeSafety()
        {
                //增加请求计数器
                addRequestCount();

                //所有请求都完成, 并且数据都成功获取的情况
                if (requestCount == REQUEST_TOTAL_NUMBER && stickyPostList != null && postList != null && !categoryCache.isEmpty())
                {
                        //启动主页
                        HomeActivity.startAction(WelcomeActivity.this, stickyPostList, postList);
                        //结束欢迎页
                        finish();
                }
        }

        /**
         * 根据偏好配置 判断是否启动服务
         */
        private void initPostPushService(){

                //确认是否有开启推送, 默认是开启
                boolean postPushIsActivated = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(ResourcesUtils.getString(R.string.preference_new_post_push_key), true);
                //只有在开启的情况
                if(postPushIsActivated){
                        //启动文章推送服务
                        PostPushService.startAction(this);
                }

        }




        /**
         * 显示错误信息
         * 并允许用户手动重试
         * Show error message
         * And allow users to manually retry
         *
         * @param message 自定义错误信息
         */
        private void setErrorInfo(String message)
        {
                String errorMessage = ResourcesUtils.getString(R.string.welcome_server_error);
                if(message!=null){
                        errorMessage = message;
                }

                //取消所有连接
                Request.cancelRequest(TAG);
                //清零计数器
                requestCount = 0;

                //切换组件显示
                welecomeProgressBar.setVisibility(View.INVISIBLE);
                welecomeInfoText.setText(errorMessage);
                welecomeInfoText.setVisibility(View.VISIBLE);

                //绑定点击事件 允许用户手动重试
                layout.setOnClickListener(v -> {
                        // 卸载点击监听
                        layout.setOnClickListener(null);
                        //切换组件显示
                        welecomeInfoText.setVisibility(View.INVISIBLE);
                        welecomeProgressBar.setVisibility(View.VISIBLE);
                        //清零计数器
                        requestCount = 0;
                        //重试
                        initPage();
                });
        }


        /**
         * 创建和显示 更新提示弹窗
         * Create alert dialog to showing update info
         * @param appUpdate 更新信息
         */
        private void openAlertDialogToUpdate(final AppUpdate appUpdate)
        {
                AlertDialog.Builder dialog = new MaterialAlertDialogBuilder(WelcomeActivity.this);
                dialog.setTitle(ResourcesUtils.getString(R.string.welcome_update_windows_title));
                //获取描述
                String description = appUpdate.getBody().getDescription();
                //把被转义\\n恢复成普通换行符
                description = description.replace("\\n", "\n");
                String message = ResourcesUtils.getString(R.string.welcome_update_version_name)+" "+ appUpdate.getBody().getVersionName() + "\n" + description;
                dialog.setMessage(message);
                //如果是强制更新, 就无法取消
                dialog.setCancelable(!appUpdate.getBody().isForceUpdate());
                //设置确认按钮名和动作
                dialog.setPositiveButton(ResourcesUtils.getString(R.string.welcome_update_positive_button), (dialog1, which) -> {
                        HttpUtils.startWebViewIntent(WelcomeActivity.this, appUpdate.getBody().getDownUrl(), null);
                        //关闭应用
                        finish();
                });
                //设置取消按钮名和动作
                dialog.setNegativeButton(ResourcesUtils.getString(R.string.cancel), (dialog12, which) -> {
                        //如果是强制更新, 取消等于关闭应用
                        if (appUpdate.getBody().isForceUpdate())
                        {
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.welcome_force_update_notice));
                                finish();
                        }
                        //如果不是强制
                        else
                        {
                                //正常请求文章数据
                                startHomeSafety();
                        }
                });
                //显示消息框
                dialog.show();
        }

        /**
         * 检查应用是否已获取敏感权限授权
         * 还没有的话, 则请求权限
         *Check if the application has been authorized for sensitive permissions
         * If not, request permission
         */
        private void permissionCheck()
        {

                List<String> permissionList = new ArrayList<>();
                //检查是否拥有权限
                if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                        //添加权限名到 待申请列表
                        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                //检查是否拥有权限
                if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                {
                        //添加权限名到 待申请列表
                        permissionList.add(Manifest.permission.READ_PHONE_STATE);
                }
                //如果待申请列表不是空的
                if (!permissionList.isEmpty())
                {
                        //把权限列表转换成 字符串数组
                        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                        //发起权限请求
                        ActivityCompat.requestPermissions(WelcomeActivity.this, permissions, 1);
                }
                //如果已经拥有全部权限
                else{
                        //正常初始化页面 init the page
                        initPage();
                }
        }





        /**
         * 请求权限后的结果回调
         * callback after requesting permission
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
        {
                switch (requestCode)
                {
                        //限请求代号为1的情况
                        case 1:
                                if (grantResults.length > 0)
                                {
                                        //遍历每个权限的请求结果
                                        for (int result : grantResults)
                                        {
                                                //如果有未同意的权限
                                                if (result != PackageManager.PERMISSION_GRANTED)
                                                {
                                                        //弹出提示 + 结束应用
                                                        ToastUtils.longToast("必须授权本应用所需的权限才能正常运行");
                                                        finish();
                                                        return;
                                                }
                                        }

                                        //正常初始化应用
                                        initPage();
                                }
                                break;
                }
        }

        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }

        /**
         * 同步增加计数器
         */
        private synchronized void addRequestCount()
        {
                this.requestCount++;
        }

}