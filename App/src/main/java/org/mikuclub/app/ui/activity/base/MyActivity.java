package org.mikuclub.app.ui.activity.base;

import android.os.Bundle;

import org.mikuclub.app.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 自定义activity
 * 增加了生命周期的日志输出功能, 方便debug
 * custom activity class
 * Added the log function on the life cycle to facilitate debugging
 */
public class MyActivity extends AppCompatActivity
{
        public boolean lifeCycleDebug = true;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState)
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onCreate");
                }
                super.onCreate(savedInstanceState);
        }

        @Override
        protected void onStart()
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onStart");
                }
                super.onStart();
        }

        @Override
        protected void onResume()
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onResume");
                }
                super.onResume();
        }


        @Override
        protected void onPause()
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onPause");
                }
                super.onPause();
        }

        @Override
        protected void onStop()
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onStop");
                }
                super.onStop();
        }

        @Override
        protected void onRestart()
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onRestart");
                }
                super.onRestart();
        }

        @Override
        protected void onDestroy()
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onDestroy");
                }
                super.onDestroy();
        }
}
