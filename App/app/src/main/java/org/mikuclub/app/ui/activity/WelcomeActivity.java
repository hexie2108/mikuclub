package org.mikuclub.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import mikuclub.app.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * 启动页面
 * launch activity
 */
public class WelcomeActivity extends AppCompatActivity
{

        private static final int TAG = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_welcome);

                permissionCheck();

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
}
