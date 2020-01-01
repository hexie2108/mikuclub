package org.mikuclub.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import mikuclub.app.BuildConfig;
import mikuclub.app.R;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import org.mikuclub.app.callBack.MyRunnable;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.BaseDelegate;
import org.mikuclub.app.delegates.PostsDelegate;
import org.mikuclub.app.javaBeans.AppUpdate;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.Parser;
import org.mikuclub.app.utils.http.Request;

import java.util.ArrayList;
import java.util.List;


/**
 * 启动页面
 * launch activity
 */
public class WelcomeActivity extends AppCompatActivity
{

        public static final int TAG = 1;


        private TextView welecomeInfoText;
        private ProgressBar welecomeProgressBar;

        private PostsDelegate postDelegate;
        private BaseDelegate baseDelegate;
        private SharedPreferences preferences;

        //存储通过网络获取的文章数据, 需要传递给主页
        private Posts stickyPostList = null;
        private Posts postList = null;
        private String categoriesCache;

        //需要等待的请求数量
        private int requestNumber = 3;
        //已完成的请求数量 (成功和失败都算)
        private int requestCount = 0;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);

                welecomeInfoText = findViewById(R.id.welcome_info_text);
                welecomeProgressBar = findViewById(R.id.welcome_progress_bar);

                postDelegate = new PostsDelegate(TAG);
                baseDelegate = new BaseDelegate(TAG);
                //获取软件设置参数文件
                preferences = PreferenceManager.getDefaultSharedPreferences(this);

                //检测权限
                permissionCheck();


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
                        //检查更新, 之后会回调方法 获取文章和分类的数据
                        checkUpdate();
                }
                else
                {
                        //提示+延时结束应用
                        finishActivityDueNoInternet();
                }
        }

        /**
         * 检查软件更新
         */
        private void checkUpdate()
        {
                long updateLastCheckTime = preferences.getLong(GlobalConfig.APP_UPDATE_CACHE_TIME, 0);
                //如果当前时间已经超过了 上次检查时间+更新周期的时间
                if (System.currentTimeMillis() > updateLastCheckTime + GlobalConfig.APP_UPDATE_CHECK_CYCLE)
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
                                        AppUpdate appUpdate = Parser.appUpdate(response);
                                        //如果更新信息不是空的 和 当前版本号低于新版本
                                        if (appUpdate != null && BuildConfig.VERSION_CODE < appUpdate.getVersionCode())
                                        {
                                                //弹出弹窗
                                                openAlertDialog(appUpdate);
                                        }
                                        //已经是新版 或者 请求发生了错误
                                        else
                                        {
                                                //如果已经是新版了, 就写入这次的检查时间, 避免后续重复检查
                                                if (BuildConfig.VERSION_CODE == appUpdate.getVersionCode())
                                                {
                                                        //保存检查时间
                                                        preferences.edit().putLong(GlobalConfig.APP_UPDATE_CACHE_TIME, System.currentTimeMillis()).apply();
                                                }
                                                //请求文章数据
                                                getDataForHome();
                                        }
                                }

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
                        baseDelegate.checkUpdate(httpCallBack);

                }
                //如果上次请求的时间 未过期 则不需要检查更新
                else
                {
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
                                stickyPostList = Parser.posts(response);
                                //尝试启动主页
                                startHomeSafety();
                        }

                        //请求失败
                        @Override
                        public void onHttpError()
                        {
                                //增加请求计数器
                                setError();
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
                                postList = Parser.posts(response);
                                startHomeSafety();
                        }

                        @Override
                        public void onHttpError()
                        {
                                setError();
                        }

                        @Override
                        public void onCancel()
                        {
                                //重置请求计数器
                                requestCount = 0;
                        }
                };

                //获取置顶文章
                postDelegate.getStickyPostList(page, callBackToGetStickyPost);
                //获取最新文章
                postDelegate.getPostList(page, callBackToGetPost);

                //获取分类信息
                checkCategories();
        }


        /**
         * 检查分类的缓存
         * 过期或者没有缓存的话 就获取新的
         */
        private void checkCategories()
        {

                final long categoriesLastCheckTime = preferences.getLong(GlobalConfig.CATEGORIES_CACHE_TIME, 0);
                categoriesCache = preferences.getString(GlobalConfig.CATEGORIES_CACHE, "");
                LogUtils.e(categoriesLastCheckTime + " " + categoriesCache);

                //如果当前时间已经超过了 上次检查时间+检查周期的时长 或者 分类字符串缓存为空

                if (System.currentTimeMillis() > categoriesLastCheckTime + GlobalConfig.CATEGORIES_CHECK_CYCLE  || categoriesCache.isEmpty())
                {
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {

                                @Override
                                public void onSuccess(String response)
                                {
                                        //获取分类信息
                                        categoriesCache = response;
                                        //更新分类缓存 和 缓存时间
                                        preferences
                                                .edit()
                                                .putString(GlobalConfig.CATEGORIES_CACHE, categoriesCache)
                                                .putLong(GlobalConfig.CATEGORIES_CACHE_TIME, System.currentTimeMillis())
                                                .apply();


                                        LogUtils.v("重新请求分类信息");

                                        startHomeSafety();

                                }

                                @Override
                                public void onError()
                                {
                                        //只有在无缓存的情况, 才会报错
                                        if (categoriesCache.isEmpty())
                                        {
                                                setError();
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
                                        onError();
                                }

                                @Override
                                public void onCancel()
                                {
                                        //重置请求计数器
                                        requestCount = 0;
                                }

                        };
                        //发送请求
                        baseDelegate.getCategory(httpCallBack);

                }
                //直接使用缓存
                else
                {

                        LogUtils.v("使用旧分类缓存");
                        startHomeSafety();
                }
        }


        /**
         * 安全的启动首页活动
         * 检测请求是否都已经成功
         * 都成功的情况 才会 启动主页
         * 否则 报错
         */
        private void startHomeSafety()
        {
                //增加请求计数器
                addRequestCount();

                //所有请求都成功的情况
                if (requestCount == requestNumber)
                {
                        //数据们都成功获取
                        if (stickyPostList != null && postList != null && !categoriesCache.isEmpty())
                        {
                                //启动主页
                                HomeActivity.startAction(WelcomeActivity.this, stickyPostList, postList);
                                //结束欢迎页
                                finish();
                                //Toast.makeText(this, "获取成功 Yeah!   " + stickyPostList.getStatus() + " " + postList.getStatus(), Toast.LENGTH_SHORT).show();

                        }
                }
        }


        /**
         * 错误的情况 , 给用户显示信息, 并允许用户手动重试
         */
        private void setError()
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
                welecomeInfoText.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                welecomeInfoText.setVisibility(View.INVISIBLE);
                                welecomeProgressBar.setVisibility(View.VISIBLE);

                                getDataForHome();
                        }
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
                dialog.setPositiveButton("前往下载", new DialogInterface.OnClickListener()
                {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                                GeneralUtils.startWebViewIntent(WelcomeActivity.this, appUpdate.getDownUrl(), "");

                        }
                });
                //设置取消按钮名和动作
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                                //如果是强制更新, 取消等于关闭应用
                                if (appUpdate.isForceUpdate())
                                {
                                        Toast.makeText(WelcomeActivity.this, "本次更新非常重要, 请下载安装新版本", Toast.LENGTH_LONG).show();
                                        finish();

                                }
                                //如果不是强制
                                else
                                {
                                        //正常请求文章数据
                                        getDataForHome();
                                }
                        }
                });
                //显示消息框
                dialog.show();
        }


        @Override
        protected void onStart()
        {
                super.onStart();
                initApplication();
        }

        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
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
                                                        Toast.makeText(this, "必须授权本应用权限才能正常使用", Toast.LENGTH_LONG).show();
                                                        finish();
                                                        return;
                                                }
                                        }
                                }
                                else
                                {
                                        Toast.makeText(this, "发生未知错误", Toast.LENGTH_LONG).show();
                                }
                                break;
                }
        }


        /**
         * 同步增加计数器
         */
        private synchronized void addRequestCount()
        {
                this.requestCount++;
        }
}
