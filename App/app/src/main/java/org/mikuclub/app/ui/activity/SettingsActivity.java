package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import mikuclub.app.R;

public class SettingsActivity extends AppCompatActivity
{
        //偏好管理器
        private SharedPreferences pref;
        //偏好变更监听器
       private SharedPreferences.OnSharedPreferenceChangeListener listener;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_settings);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsFragment())
                        .commit();
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                }


                pref = PreferenceManager.getDefaultSharedPreferences(this);
                listener = new SharedPreferences.OnSharedPreferenceChangeListener()
                {
                        @Override
                        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
                        {

                        }
                };
                pref.registerOnSharedPreferenceChangeListener(listener);


        }
        @Override
        public void onStop()
        {
                //注销变更监听器
                pref.unregisterOnSharedPreferenceChangeListener(listener);
                super.onStop();

        }


        public static class SettingsFragment extends PreferenceFragmentCompat
        {
                @Override
                public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
                {
                        setPreferencesFromResource(R.xml.settings_preferences, rootKey);

                }


        }

        /**
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
        }
}