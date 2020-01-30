package org.mikuclub.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import org.mikuclub.app.ui.activity.SettingsActivity;
import org.mikuclub.app.ui.activity.WelcomeActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import androidx.preference.PreferenceManager;
import mikuclub.app.R;

public class PostPushService extends Service
{
        /*静态变量*/
        //操作类型
        public static final int RESET_OPERATION = 1;
        public static final int EXECUTE_TASK_OPERATION = 2;

        private static final String INTENT_OPERATION_TYPE = "operation_type";
        //无法连接时的重试间隔
        private long retryTimeInMilliseconds = 1000 * 60 * 10;

        /*变量*/

        private SharedPreferences pref;
        private boolean postPushIsActivated;
        private String postPushCycle;
        private AlarmManager manager;
        private Intent launchIntent;
        private PendingIntent pendingIntent;
        private int operationType;

        @Override
        public void onCreate()
        {
                super.onCreate();

                //获取应用的配置偏好
                pref = PreferenceManager.getDefaultSharedPreferences(this);
                //确认是否有开启推送, 默认是开启
                postPushIsActivated = pref.getBoolean(ResourcesUtils.getString(R.string.new_post_push_key), true);
                //获取推送频率, 默认是 每天1次
                postPushCycle = pref.getString(ResourcesUtils.getString(R.string.new_post_push_cycle_key), "1");

                //获取定时器
                manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //创建intent 意图
                launchIntent = new Intent(this, WelcomeActivity.class);
                //添加操作类型
                launchIntent.putExtra(INTENT_OPERATION_TYPE, EXECUTE_TASK_OPERATION);
                //封装intent
                pendingIntent = PendingIntent.getService(this, 0, launchIntent, 0);

        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
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


                return super.onStartCommand(intent, flags, startId);
        }


        /**
         * 重置定时任务方法
         * <p>
         * 先取消旧任务
         * 然后根据 推送配置是否开启
         * 来配置新定时任务
         */
        private void resetScheduledTask()
        {
                //先取消旧任务
                manager.cancel(pendingIntent);

                //如果有开启投稿推送配置
                if (postPushIsActivated)
                {
                        //获取推送频率 的 总毫秒数 = 1000毫秒 * 60 秒 * 60 分钟 * 24小时 * 推送频率的都天数
                        //long postPushCycleInMilliseconds = 1000 * 60 * 60 * 24 * Integer.valueOf(postPushCycle);
                        //方便测试 设置 1分钟
                        long postPushCycleInMilliseconds = 1000 * 60;
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
                manager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
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
                                        super.onSuccess(response);
                                }

                                @Override
                                public void onError(String response)
                                {
                                        setScheduledTask(retryTimeInMilliseconds);
                                }

                                @Override
                                public void onHttpError()
                                {
                                        setScheduledTask(retryTimeInMilliseconds);
                                }

                                @Override
                                public void onFinally()
                                {
                                        super.onFinally();
                                }
                        };
                }
                //如果没网络链接
                else
                {
                        setScheduledTask(retryTimeInMilliseconds);
                }


        }


        private void sendRequest()
        {


        }

        private void createNotification()
        {

        }


        @Override
        public IBinder onBind(Intent intent)
        {
                return null;
        }


        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         * @param operationType 需要进行的操作类型
         */
        public static void startAction(Context context, int operationType)
        {
                Intent intent = new Intent(context, SettingsActivity.class);
                intent.putExtra(INTENT_OPERATION_TYPE, operationType);
                context.startService(intent);
        }

}
