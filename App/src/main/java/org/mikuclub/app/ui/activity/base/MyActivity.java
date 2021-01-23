package org.mikuclub.app.ui.activity.base;

import android.content.res.Configuration;
import android.os.Bundle;

import com.aliya.uimode.UiModeManager;
import com.aliya.uimode.intef.UiModeChangeListener;

import org.mikuclub.app.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 自定义activity
 * 增加了生命周期的日志输出功能, 方便debug
 * 增加了日/夜间模式 支持
 */
public class MyActivity extends AppCompatActivity implements UiModeChangeListener
{

        //debug信息开关
        public boolean lifeCycleDebug = false;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState)
        {
                if (lifeCycleDebug)
                {
                        LogUtils.e(this.getClass().getName() + "/" + this.toString() + " onCreate");
                }
                UiModeManager.setInflaterFactor(getLayoutInflater());
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


        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig)
        {
                super.onConfigurationChanged(newConfig);

                //UiModeManager.setUiMode();
                //Log.e("TAG", "onConfigurationChanged: " + getResources().getConfiguration().uiMode);
        }

        @Override
        public void onUiModeChange()
        {
                //Log.e("TAG", "onUiModeChange " + getResources().getConfiguration().uiMode);
        }
}
