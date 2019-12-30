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
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.mikuclub.app.callBack.MyRunnable;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.delegates.AppUpdateDelegate;
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
        private AppUpdateDelegate appUpdateDelegate;

        //存储通过网络获取的文章数据, 需要传递给主页
        private Posts stickyPostList = null;
        private Posts postList = null;

        //需要等待的请求数量
        private int requestNumber = 2;
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
                appUpdateDelegate = new AppUpdateDelegate(TAG);

                //检测权限
                permissionCheck();


        }

        @Override
        protected void onStart()
        {
                super.onStart();

                //检测网络状态
                boolean isInternetAvailable = internetCheck();
                if (isInternetAvailable)
                {
                        //检查更新
                        checkUpdate();
                }
                else
                {
                        //提示+延时结束应用
                        finishActivityDueNoInternet();
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
         * 检查软件更新
         */
        private void checkUpdate()
        {

                appUpdateDelegate.checkUpdate(new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //修正response乱码问题
                                response = GeneralUtils.fixStringEncoding(response);
                                final AppUpdate appUpdate = Parser.appUpdate(response);
                                //如果更新信息不是空的, 和 当前版本号低于新版本
                                if (appUpdate != null && BuildConfig.VERSION_CODE < appUpdate.getVersionCode())
                                {

                                        AlertDialog.Builder dialog = new AlertDialog.Builder(WelcomeActivity.this);
                                        dialog.setTitle("发现应用的新版本");
                                        String message = "版本名: "+appUpdate.getVersionName() + "\n" + appUpdate.getDescription();
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
                                //已经是新版或者更新信息错误
                                else
                                {
                                        //请求文章数据
                                        getDataForHome();
                                }
                        }

                        @Override
                        public void onSuccessHandler(String response)
                        {
                                onSuccess(response);
                        }

                        //回复错误的情况, 忽视, 下次再检查更新
                        @Override
                        public void onError()
                        {
                                //请求文章数据
                                getDataForHome();
                        }

                        //网络错误的情况, 忽视, 下次再检查更新
                        @Override
                        public void onHttpError()
                        {
                                //请求文章数据
                                getDataForHome();
                        }
                });


        }

        /**
         * 请求数据并跳转主页
         */
        private void getDataForHome()
        {

                //获取置顶文章
                postDelegate.getStickyPostsList(new WrapperCallBack()
                {
                        //请求成功
                        @Override
                        public void onSuccess(String response)
                        {
                                //解析数据
                                Posts posts = Parser.posts(response);
                                //保存数据
                                stickyPostList = posts;

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
                });

                //获取最新文章
                postDelegate.getRecentlyPostsList(0, new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                Posts posts = Parser.posts(response);
                                postList = posts;

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
                });

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
                        if (stickyPostList != null && postList != null)
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


        private void setStickyPostList(Posts stickyPostList)
        {
                this.stickyPostList = stickyPostList;
        }

        private void setPostList(Posts postList)
        {
                this.postList = postList;
        }

        /**
         * 同步增加计数器
         */
        private synchronized void addRequestCount()
        {
                this.requestCount++;
        }
}
