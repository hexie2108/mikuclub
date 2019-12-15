package org.mikuclub.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import org.mikuclub.app.callBack.MyRunnable;
import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.resources.Posts;
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

        private PostDelegate postDelegate;

        //存储通过网络获取的文章数据, 需要传递给主页
        private Posts stickyPostList = null;
        private Posts postList = null;

        //需要等待的请求数量
        private int requestNumber=2;
        //已完成的请求数量 (成功和失败都算)
        private int requestCount = 0;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);

                welecomeInfoText = findViewById(R.id.welcome_info_text);
                welecomeProgressBar = findViewById(R.id.welcome_progress_bar);

                postDelegate = new PostDelegate(TAG);

                //检测权限
                permissionCheck();
                //检测网络状态
                boolean isInternetAvailable = internetCheck();
                if (isInternetAvailable)
                {
                        //请求数据 + 跳转主页
                        getDataForHome();
                }
                else{
                        //提示+延时结束应用
                        finishActivityDueNoInternet();
                }

        }

        @Override
        protected void onStop()
        {
                super.onStop();
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
        }

        /**
         * 请求数据并跳转主页
         */
        private void getDataForHome()
        {

                //获取置顶文章
                postDelegate.getStickyPostList(new WrapperCallBack()
                {
                        //请求成功
                        @Override
                        public void onSuccess(String response)
                        {
                                //解析数据
                                Posts posts = Parser.posts(response);
                                //保存数据
                                setStickyPostList(posts);
                                //增加请求计数器
                                addRequestCount();
                                //尝试启动主页
                                startHomeSafety();
                        }

                        //请求失败
                        @Override
                        public void onError()
                        {
                                //增加请求计数器
                                addRequestCount();
                        }
                });

                //获取最新文章
                postDelegate.getRecentlyPostList(0, new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                Posts posts = Parser.posts(response);
                                setPostList(posts);
                                addRequestCount();
                                startHomeSafety();
                        }
                        @Override
                        public void onError()
                        {
                                addRequestCount();
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
                //所有请求已经结束
                if (requestCount == requestNumber)
                {
                        //数据们都成功获取
                        if (stickyPostList != null && postList != null)
                        {
                                //启动主页
                                HomeActivity.startAction(WelcomeActivity.this, stickyPostList, postList);
                                //Toast.makeText(this, "获取成功 Yeah!   " + stickyPostList.getStatus() + " " + postList.getStatus(), Toast.LENGTH_SHORT).show();

                        }
                        //有数据获取失败
                        else
                        {
                                welecomeInfoText.setText("当前无法连接上服务器, 请您稍后再重新尝试");
                                welecomeInfoText.setVisibility(View.VISIBLE);
                                welecomeProgressBar.setVisibility(View.INVISIBLE);
                        }
                }
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
        private void finishActivityDueNoInternet(){
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
                                                        Toast.makeText(this, "必须授权本应用权限才能正常使用", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        return;
                                                }
                                        }
                                }
                                else
                                {
                                        Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
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
        private void addRequestCount()
        {
                this.requestCount++;
        }
}
