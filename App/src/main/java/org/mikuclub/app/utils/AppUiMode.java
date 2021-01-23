package org.mikuclub.app.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.aliya.uimode.UiModeManager;

import org.mikuclub.app.storage.ApplicationPreferencesUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatDelegate;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

/**
 * UiMode 日夜模式切换系统
 *
 * @author a_liYa
 * @see "https://github.com/a-liYa/AndroidUiMode"
 * @since 2018/1/26 16:47.
 */
public final class AppUiMode
{

        private static Context sContext;
        private static volatile AppUiMode sInstance;
        private int uiMode;


        /**
         * 私人 构造函数 (通过类自带的公开静态函数调用)
         */
        private AppUiMode()
        {

                //如果有共享参数
                uiMode = ApplicationPreferencesUtils.getAppUiMode();
                //如果参数未0 说明还不存在偏好设置
                if (uiMode == 0)
                {
                        //使用安卓系统配置
                        uiMode = AppCompatDelegate.getDefaultNightMode();
                }
        }


        /**
         * 私人 方法 更新当前模式ID  并储存到 共享参数中
         *
         * @param uiMode
         */
        private void setUiMode(@AppCompatDelegate.NightMode int uiMode)
        {
                if (this.uiMode != uiMode)
                {
                        this.uiMode = uiMode;
                        ApplicationPreferencesUtils.setAppUiMode(uiMode);
                }
        }

        /**
         * 获取当前类实例 (单例模式 )
         *
         * @return 当前类实例
         */
        private static AppUiMode _get()
        {
                if (sInstance == null)
                {
                        synchronized (AppUiMode.class)
                        {
                                if (sInstance == null)
                                {
                                        sInstance = new AppUiMode();
                                }
                        }
                }
                return sInstance;
        }

        /**
         * 初始化日夜模式实例
         *
         * @param context
         */
        public static void init(Context context)
        {
                sContext = context.getApplicationContext();
                UiModeManager.init(sContext, null, new LayoutInflater.Factory2() {
                        @Override
                        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                                // 此处可自定义拦截创建View
                                return null;
                        }

                        @Override
                        public View onCreateView(String name, Context context, AttributeSet attrs) {
                                return onCreateView(null, name, context, attrs);
                        }
                });
                UiModeManager.setDefaultUiMode(_get().uiMode);
        }

        /**
         * 切换模式
         *
         * @param uiMode
         */
        public static void applyUiMode(@ApplyableNightMode int uiMode)
        {
                _get().setUiMode(uiMode);
                UiModeManager.setUiMode(_get().uiMode);
        }


        /**
         * 获取当前所处模式 的ID编号
         */
        @ApplyableNightMode
        public static int getUiMode()
        {
                return _get().uiMode;
        }

        @IntDef({MODE_NIGHT_NO, MODE_NIGHT_YES, MODE_NIGHT_FOLLOW_SYSTEM})
        @Retention(RetentionPolicy.SOURCE)
        @interface ApplyableNightMode
        {
        }

}
