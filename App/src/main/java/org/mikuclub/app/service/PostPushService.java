package org.mikuclub.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.response.SingleResponse;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.ui.activity.WelcomeActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.NotificationUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import androidx.preference.PreferenceManager;
import mikuclub.app.R;

/**
 * 新投稿推送服务
 */
public class PostPushService extends Service
{
        /*静态变量*/
        //操作类型
        public static final int TAG = 14;
        public static final int RESET_OPERATION = 1;
        public static final int EXECUTE_TASK_OPERATION = 2;

        private static final String INTENT_OPERATION_TYPE = "operation_type";

        //无法连接时的重试间隔, 1小时后重试一次
        private static final long ALARM_RETRY_TIME = 1000 * 60 * 60;


        /*变量*/

        private SharedPreferences pref;
        private boolean postPushIsActivated;
        private String postPushCycle;
        private AlarmManager manager;
        private Intent intentToStartService;
        private PendingIntent pendintentToStartService;
        private int operationType;

        private PostDelegate delegate;

        @Override
        public void onCreate()
        {
                super.onCreate();
                //把代码写在 create里 会导致 无网络延迟定时失败
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
                LogUtils.v("推送服务 onStartCommand");

                //获取应用的配置偏好
                pref = PreferenceManager.getDefaultSharedPreferences(this);
                //获取定时器
                manager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //创建intent 意图
                intentToStartService = new Intent(this, PostPushService.class);
                intentToStartService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //添加操作类型
                intentToStartService.putExtra(INTENT_OPERATION_TYPE, EXECUTE_TASK_OPERATION);
                //封装intent
                pendintentToStartService = PendingIntent.getService(this, 0, intentToStartService, 0);

                delegate = new PostDelegate(TAG);

                //确认是否有开启推送, 默认是开启
                postPushIsActivated = pref.getBoolean(ResourcesUtils.getString(R.string.preference_new_post_push_key), true);
                //获取推送频率, 默认是 每天1次
                postPushCycle = pref.getString(ResourcesUtils.getString(R.string.preference_new_post_push_cycle_key), "1");

                //获取要执行的任务类型
                operationType = intent.getIntExtra(INTENT_OPERATION_TYPE, 0);
                //根据类型执行对应操作
                switch (operationType)
                {
                        case RESET_OPERATION:
                                resetScheduledTask();
                                break;
                        case EXECUTE_TASK_OPERATION:
                                executeScheduleTask();
                                break;
                }

                //走完流程就关闭服务
                stopSelf();

                return super.onStartCommand(intent, flags, startId);
        }

        /**
         * 重置定时任务方法
         * 先取消旧任务
         * 然后根据 推送配置是否开启
         * 来配置新定时任务
         */
        private void resetScheduledTask()
        {
                //先取消旧任务
                manager.cancel(pendintentToStartService);
                LogUtils.v("推送服务 已取消旧定时器");

                //如果有开启投稿推送配置
                if (postPushIsActivated)
                {
                        //配置新的定时器
                        //获取推送频率 的 总毫秒数 = 1天的毫秒数 * 推送频率的天数
                        long postPushCycleInMilliseconds = GlobalConfig.DAY_IN_MILLISECONDS * Integer.valueOf(postPushCycle);
                        //postPushCycleInMilliseconds = 1000*30*Integer.valueOf(postPushCycle);

                        setScheduledTask(postPushCycleInMilliseconds);
                }
        }

        /**
         * 设置定时任务
         *
         * @param time 距离当前时间的启动间隔
         */
        private void setScheduledTask(long time)
        {

                //定时器激活的时间
                long triggerTime = System.currentTimeMillis() + time;
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendintentToStartService);
                LogUtils.v("推送服务 已设置新定时器");
        }


        /**
         * 运行定时任务
         */
        private void executeScheduleTask()
        {

                //如果有网络连接
                if (HttpUtils.internetCheck(this))
                {
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        SingleResponse singleResponse = ParserUtils.fromJson(response, SingleResponse.class);
                                        //获取新文章计数
                                        int newPostCount = Integer.valueOf(singleResponse.getBody());
                                        //如果大于0, 说明有未读新投稿
                                        if(newPostCount>0){
                                                //生成通知
                                                createNotification(newPostCount);
                                        }
                                        //设置新的定时任务, 让服务在下次启动, 避免用户忽略通知
                                        resetScheduledTask();
                                }

                                @Override
                                public void onError(WpError wpError)
                                {
                                        setScheduledTask(ALARM_RETRY_TIME);
                                }

                                @Override
                                public void onHttpError()
                                {
                                        onError(null);
                                }
                        };
                        delegate.getNewPostCount(httpCallBack);
                }
                //如果没网络链接
                else
                {
                        LogUtils.v("推送服务 当前无网络, 已延迟运行定时器");
                        setScheduledTask(ALARM_RETRY_TIME);
                }

        }


        /**
         * 生成应用通知
         * @param newPostCount
         */
        private void createNotification(int newPostCount)
        {

                //创建启动应用的intent
                Intent intentToStartApp = new Intent(this, WelcomeActivity.class);
                intentToStartApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendintentToStartApp = PendingIntent.getActivity(this, 0, intentToStartApp, 0);
                //随意生成通知id
                int notificationId = (int) System.currentTimeMillis()/1000;
                //通知名称
                String title = ResourcesUtils.getString(R.string.notification_title);
                //通知内容
                String content = String.format(ResourcesUtils.getString(R.string.notification_content), newPostCount);
                //创建通知
                NotificationUtils.createNotification(this, notificationId, title, content, pendintentToStartApp);

        }



        @Override
        public IBinder onBind(Intent intent)
        {
                return null;
        }

        @Override
        public void onDestroy()
        {
                LogUtils.v("推送服务 onDestroy");
                super.onDestroy();
        }

        /**
         * 启动本服务的静态方法
         * static method to start current activity
         *通过改方法启动的service 默认为 重置操作
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, PostPushService.class);
                intent.putExtra(INTENT_OPERATION_TYPE, RESET_OPERATION);
                context.startService(intent);
        }


}
