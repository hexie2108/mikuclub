package org.mikuclub.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import mikuclub.app.BuildConfig;
import mikuclub.app.R;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.callBack.MyRunnable;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.UtilsDelegate;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.AppUpdate;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.PreferencesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 启动页面
 * launch activity
 */
public class WelcomeActivity extends AppCompatActivity
{
        /*静态变量*/
        public static final int TAG = 1;
        //需要等待的请求数量
        private static final int REQUEST_TOTAL_NUMBER = 3;

        /*变量*/
        private PostDelegate postDelegate;
        private UtilsDelegate utilsDelegate;

        private Posts stickyPostList = null;
        private Posts postList = null;
        private String categoriesCache;
        //已完成的请求数量 (成功和失败都算)
        private int requestCount = 0;

        /*组件*/
        private TextView welecomeInfoText;
        private ProgressBar welecomeProgressBar;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);

                welecomeInfoText = findViewById(R.id.welcome_info_text);
                welecomeProgressBar = findViewById(R.id.welcome_progress_bar);

                //创建代理人
                postDelegate = new PostDelegate(TAG);
                utilsDelegate = new UtilsDelegate(TAG);


                //检测权限
                permissionCheck();
        }

        @Override
        protected void onStart()
        {
                super.onStart();
                initApplication();
        }

        /**
         * 初始化应用配置 和检测
         */
        private void initApplication()
        {
                //检测网络状态
                boolean isInternetAvailable = internetCheck();
                if (isInternetAvailable)
                {
                        //检测登陆令牌是否失效
                        checkTokenValidity();
                }
                else
                {
                        //提示+延时结束应用
                        finishActivityDueNoInternet();
                }
        }


        /**
         * 检测登陆令牌是否失效
         */
        private void checkTokenValidity()
        {

                //如果用户有登陆
                if (GeneralUtils.userIsLogin())
                {
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                //token正常
                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.v("登陆信息还有效");
                                        //检查更新, 之后会回调方法 获取文章和分类的数据
                                        checkUpdate();
                                }

                                //令牌过期了
                                @Override
                                public void onError(String response)
                                {
                                        LogUtils.v("登陆信息已过期");
                                        //注销用户相关信息
                                        GeneralUtils.userLogout();
                                        ToastUtils.shortToast("登陆信息已过期, 请重新登陆");
                                        //然后继续
                                        onSuccess(null);
                                }

                                //网络错误
                                @Override
                                public void onHttpError()
                                {
                                        displayErrorInfo();
                                }
                        };
                        //检测令牌是否还有效
                        utilsDelegate.tokenValidate(httpCallBack);

                }
                //如果无登陆, 就不需要检查token有效性
                else
                {
                        //检查更新, 之后会回调方法 获取文章和分类的数据
                        checkUpdate();
                }

        }

        /**
         * 检查软件更新
         */
        private void checkUpdate()
        {
                long appUpdateExpire = PreferencesUtils.getApplicationPreference().getLong(GlobalConfig.Preferences.APP_UPDATE_EXPIRE, 0);
                //如果检查更新已过期
                if (System.currentTimeMillis() > appUpdateExpire)
                {
                        LogUtils.v("检查应用更新");

                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        //修正response乱码问题
                                        response = GeneralUtils.fixStringEncoding(response);
                                        //获取更新信息
                                        AppUpdate appUpdate = ParserUtils.appUpdate(response);
                                        //如果更新信息不是空的 和 当前版本号低于新版本
                                        if (appUpdate != null && BuildConfig.VERSION_CODE < appUpdate.getVersionCode())
                                        {
                                                LogUtils.v("发现新版本");
                                                //弹出弹窗
                                                openAlertDialog(appUpdate);
                                        }
                                        //已经是新版 或者 请求发生了错误
                                        else
                                        {
                                                //如果已经是新版了, 就写入这次的检查时间, 避免后续重复检查
                                                if (BuildConfig.VERSION_CODE == appUpdate.getVersionCode())
                                                {
                                                        LogUtils.v("已经是最新版了");
                                                        long expire = System.currentTimeMillis() + GlobalConfig.Preferences.APP_UPDATE_EXPIRE_TIME;
                                                        //保存 这次检查更新的过期时间
                                                        PreferencesUtils.getApplicationPreference().edit().putLong(GlobalConfig.Preferences.APP_UPDATE_EXPIRE, expire).apply();
                                                }
                                                //请求文章数据
                                                getDataForHome();
                                        }
                                }

                                //因为格式不一样 所以需要跳过默认格式检查
                                @Override
                                public void onSuccessHandler(String response)
                                {
                                        onSuccess(response);
                                }

                                //网络错误的情况, 忽视, 下次再检查更新
                                @Override
                                public void onHttpError()
                                {
                                        //请求文章数据
                                        getDataForHome();
                                }
                        };
                        utilsDelegate.checkUpdate(httpCallBack);

                }
                //如果上次请求的时间 未过期 则不需要检查更新
                else
                {
                        LogUtils.v("无需检查");
                        //请求文章数据
                        getDataForHome();
                }


        }

        /**
         * 请求数据并跳转主页
         */
        private void getDataForHome()
        {
                int page = 1;
                //请求置顶文章 回调函数
                HttpCallBack callBackToGetStickyPost = new HttpCallBack()
                {
                        //请求成功
                        @Override
                        public void onSuccess(String response)
                        {
                                //解析数据 +保存数据
                                stickyPostList = ParserUtils.posts(response);
                                //尝试启动主页
                                startHomeSafety();
                        }

                        @Override
                        public void onError(String response)
                        {
                                displayErrorInfo();
                        }

                        //请求失败
                        @Override
                        public void onHttpError()
                        {
                                displayErrorInfo();
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
                                postList = ParserUtils.posts(response);
                                startHomeSafety();
                        }

                        @Override
                        public void onError(String response)
                        {
                                displayErrorInfo();
                        }

                        @Override
                        public void onHttpError()
                        {
                                displayErrorInfo();
                        }

                        @Override
                        public void onCancel()
                        {
                                //重置请求计数器
                                requestCount = 0;
                        }
                };

                //获取置顶文章
                postDelegate.getStickyPostList(callBackToGetStickyPost, page);

                //获取最新文章
                //设置请求参数
                PostParameters parameters = new PostParameters();
                parameters.setCategories_exclude(new ArrayList<>(Arrays.asList(GlobalConfig.CATEGORY_ID_MOFA)));
                postDelegate.getPostList(callBackToGetPost, page, parameters);

                //获取分类信息
                checkCategories();
        }

        /**
         * 检查分类的缓存
         * 过期或者没有缓存的话 就获取新的
         */
        private void checkCategories()
        {

                final long categoriesCacheExpire = PreferencesUtils.getCategoryPreference().getLong(GlobalConfig.Preferences.CATEGORIES_CACHE_EXPIRE, 0);
                categoriesCache = PreferencesUtils.getCategoryPreference().getString(GlobalConfig.Preferences.CATEGORIES_CACHE, null);


                //如果分类缓存 已过期 或者 缓存为null
                if (System.currentTimeMillis() > categoriesCacheExpire || categoriesCache == null)
                {
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {

                                @Override
                                public void onSuccess(String response)
                                {
                                        //获取分类信息
                                        categoriesCache = response;
                                        //计算缓存过期时间
                                        long expire = System.currentTimeMillis() + GlobalConfig.Preferences.CATEGORIES_CACHE_EXPIRE_TIME;
                                        //更新分类缓存 和 缓存过期时间
                                        PreferencesUtils.getCategoryPreference()
                                                .edit()
                                                .putString(GlobalConfig.Preferences.CATEGORIES_CACHE, categoriesCache)
                                                .putLong(GlobalConfig.Preferences.CATEGORIES_CACHE_EXPIRE, expire)
                                                .apply();

                                        LogUtils.v("已重新请求分类信息");

                                        startHomeSafety();

                                }

                                @Override
                                public void onError(String response)
                                {
                                        //只有在无缓存的情况, 才会报错
                                        if (categoriesCache.isEmpty())
                                        {
                                                displayErrorInfo();
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
         * 检测请求是否都已经完成, 并且数据都已获取
         * 都成功的情况 才会 启动主页
         * 否则 报错
         */
        private void startHomeSafety()
        {
                //增加请求计数器
                addRequestCount();

                //所有请求都完成, 并且数据都成功获取的情况
                if (requestCount == REQUEST_TOTAL_NUMBER && stickyPostList != null && postList != null && !categoriesCache.isEmpty())
                {

                        //启动主页
                        HomeActivity.startAction(WelcomeActivity.this, stickyPostList, postList);
                        //结束欢迎页
                        finish();
                }
        }

        /**
         * 错误的情况 , 给用户显示信息, 并允许用户手动重试
         */
        private void displayErrorInfo()
        {
                //取消所有连接
                Request.cancelRequest(TAG);
                //清零计数器
                requestCount = 0;

                //切换组件显示
                welecomeProgressBar.setVisibility(View.INVISIBLE);
                welecomeInfoText.setText("当前无法连接上服务器, 请点我尝试");
                welecomeInfoText.setVisibility(View.VISIBLE);

                //绑定点击事件 允许用户手动重试
                welecomeInfoText.setOnClickListener(v -> {

                        welecomeInfoText.setVisibility(View.INVISIBLE);
                        welecomeProgressBar.setVisibility(View.VISIBLE);
                        getDataForHome();
                });


        }

        /**
         * 创建显示更新提示的弹窗
         *
         * @param appUpdate 更新信息
         */
        private void openAlertDialog(final AppUpdate appUpdate)
        {
                AlertDialog.Builder dialog = new AlertDialog.Builder(WelcomeActivity.this);
                dialog.setTitle("发现应用的新版本");
                String message = "版本名: " + appUpdate.getVersionName() + "\n" + appUpdate.getDescription();
                dialog.setMessage(message);
                //如果是强制更新, 就无法取消
                dialog.setCancelable(!appUpdate.isForceUpdate());
                //设置确认按钮名和动作
                dialog.setPositiveButton("前往下载", (dialog1, which) -> GeneralUtils.startWebViewIntent(WelcomeActivity.this, appUpdate.getDownUrl(), ""));
                //设置取消按钮名和动作
                dialog.setNegativeButton("取消", (dialog12, which) -> {
                        //如果是强制更新, 取消等于关闭应用
                        if (appUpdate.isForceUpdate())
                        {
                                ToastUtils.shortToast("本次更新非常重要, 请下载安装新版本");
                                finish();
                        }
                        //如果不是强制
                        else
                        {
                                //正常请求文章数据
                                getDataForHome();
                        }
                });
                //显示消息框
                dialog.show();
        }

        /**
         * 检查应用是否已获取敏感权限授权
         * 还没有的话, 则请求权限
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
                //如果申请列表不是空的
                if (!permissionList.isEmpty())
                {
                        //把权限列表转换成 字符串数组
                        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                        //发起权限请求
                        ActivityCompat.requestPermissions(WelcomeActivity.this, permissions, 1);
                }
        }

        /**
         * 检测网络状态
         *
         * @return
         */
        private boolean internetCheck()
        {

                boolean isInternetAvailable = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null)
                {
                        //如果设备SDK版本等于大于29
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        {
                                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                                if (capabilities != null)
                                {
                                        //如果有手机网络, wifi网络或以太网
                                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                                        {
                                                isInternetAvailable = true;
                                        }

                                }
                        }
                        //低于 sdk 29的版本
                        else
                        {
                                //获取网络状态
                                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                                if (activeNetworkInfo != null && activeNetworkInfo.isConnected())
                                {
                                        isInternetAvailable = true;
                                }
                        }
                }


                return isInternetAvailable;
        }

        /**
         * 定时退出活动 (无网络连接的情况)
         */
        private void finishActivityDueNoInternet()
        {
                //创建子线程
                new Thread(new Runnable()
                {
                        @Override
                        public void run()
                        {
                                try
                                {
                                        //循环5次, 每次暂停1秒钟
                                        int cycle = 5;
                                        for (int i = 0; i < cycle; i++)
                                        {
                                                //切换到主线程
                                                //更新UI显示
                                                runOnUiThread(new MyRunnable(cycle - i)
                                                {
                                                        //通过get方法获取到外部传递的第一个变量
                                                        int seconds = (int) this.getArgument1();

                                                        @Override
                                                        public void run()
                                                        {

                                                                //更新活动页 UI
                                                                welecomeInfoText.setText("未发现可用的网络连接, 本应用将在" + seconds + "秒后自动退出");
                                                                welecomeInfoText.setVisibility(View.VISIBLE);
                                                                welecomeProgressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                });

                                                //暂停子线程1秒种
                                                Thread.sleep(1000);
                                        }
                                        //退出程序
                                        WelcomeActivity.this.finish();
                                }
                                catch (InterruptedException e)
                                {
                                        LogUtils.w(WelcomeActivity.class.getName() + "子进程延时错误");
                                        e.printStackTrace();
                                }

                        }
                }).start();
        }

        /**
         * 处理请求权限后的结果
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
                                                //如果未同意
                                                if (result != PackageManager.PERMISSION_GRANTED)
                                                {
                                                        //弹出提示 + 结束应用
                                                        ToastUtils.longToast("必须授权本应用权限才能正常使用");
                                                        finish();
                                                        return;
                                                }
                                        }
                                }
                                else
                                {
                                        ToastUtils.shortToast("发生未知错误");
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