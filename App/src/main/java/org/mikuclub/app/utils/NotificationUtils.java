package org.mikuclub.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import mikuclub.app.R;

public class NotificationUtils
{
        private static final String CHANNEL_ID = "1";

        /**
         *
         * 生成应用通知
         * @param context
         * @param notificationId
         * @param title
         * @param content
         */
        public static void createNotification(Context context, int notificationId, String title, String content, PendingIntent pendingIntent)
        {


                //创建通知
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.mail)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                //为安卓8.0 以上 创建通知通道
                createNotificationChannel(context);

                //显示通知
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(notificationId, builder.build());
        }

        /**
         * 安卓8.0  API 26+ 以上要创建额外消息通道
         * @param context
         */
        private static void createNotificationChannel(Context context){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = ResourcesUtils.getString(R.string.channel_name);
                        String description = ResourcesUtils.getString(R.string.channel_description);
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                        if(notificationManager!=null){
                                notificationManager.createNotificationChannel(channel);
                        }

                }

        }



}
