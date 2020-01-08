package org.mikuclub.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.callBack.HttpCallBackForUtils;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.UtilsDelegate;
import org.mikuclub.app.javaBeans.resources.UserLogin;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.PreferencesUtlis;

import mikuclub.app.R;

public class LoginActivity extends AppCompatActivity
{

        /*静态变量*/
        public static final int TAG = 7;
        public static final int REQUEST_CODE = 1;

        /*变量*/
        private EditText inputUserName;
        private EditText inputUserPassword;
        private TextInputLayout inputUserNameLayout;
        private TextInputLayout inputUserPasswordLayout;
        private Button loginButton;
        private ProgressBar progressBar;

        private UtilsDelegate delegate;

        /*组件*/


        @Override
        public void onCreate(Bundle savedInstanceState)
        {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                inputUserName = findViewById(R.id.input_user_name);
                inputUserPassword = findViewById(R.id.input_user_password);
                inputUserNameLayout = findViewById(R.id.input_user_name_layout);
                inputUserPasswordLayout = findViewById(R.id.input_user_password_layout);
                loginButton = findViewById(R.id.login_button);
                progressBar = findViewById(R.id.progress_bar);

                Toolbar toolbar = findViewById(R.id.toolbar);
                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                }

                delegate = new UtilsDelegate(TAG);

                //初始化输入框
                initEditText();

        }

        /**
         * 初始化输入框
         */
        private void initEditText()
        {
                //获取焦点+弹出键盘
                KeyboardUtils.showKeyboard(inputUserName);
                //input内容监听器, 在都不为空的情况激活登陆按钮
                TextWatcher textWatcher = new TextWatcher()
                {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after)
                        {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count)
                        {
                        }

                        @Override
                        public void afterTextChanged(Editable s)
                        {
                                //重新输入后 就隐藏错误信息
                                inputUserNameLayout.setError(null);
                                inputUserPasswordLayout.setError(null);

                                //如果用户名和密码都不是空
                                if (!inputUserName.getText().toString().isEmpty() && !inputUserPassword.getText().toString().isEmpty())
                                {
                                        //激活按钮
                                        loginButton.setEnabled(true);
                                }
                                else
                                {
                                        //注销按钮
                                        loginButton.setEnabled(false);
                                }
                        }
                };
                //添加内容变化监听器
                inputUserName.addTextChangedListener(textWatcher);
                inputUserPassword.addTextChangedListener(textWatcher);

                //监听键盘动作
                inputUserPassword.setOnEditorActionListener((v, actionId, event) -> {
                        //如果是激活状态
                        if (loginButton.isEnabled())
                        {
                                //产生点击事件
                                loginButton.performClick();
                        }
                        else
                        {
                                Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                });

                loginButton.setOnClickListener(v -> {

                        sendLogin();
                });
        }


        /**
         * 发送登陆请求
         */
        private void sendLogin()
        {

                progressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);


                HttpCallBackForUtils httpCallBackForUtils = new HttpCallBackForUtils()
                {

                        /**
                         * 请求成功的情况
                         *
                         * @param response
                         */
                        @Override
                        public void onSuccess(String response)
                        {
                                try
                                {
                                        //先解析一遍返回数据
                                        JSONObject jsonObject = new JSONObject(response);
                                        response = jsonObject.getString("body");
                                        //设置登陆成功的信息
                                        setLoginResult(response);
                                }
                                catch (JSONException e)
                                {
                                        e.printStackTrace();
                                }
                        }

                        /**
                         * 返回错误信息的情况
                         *
                         * @param response
                         */
                        public void onError(String response)
                        {
                                progressBar.setVisibility(View.INVISIBLE);

                                String code = null;
                                try
                                {
                                        //先解析一遍返回数据
                                        JSONObject jsonObject = new JSONObject(response);
                                        code = jsonObject.getJSONObject("body").getString("code");
                                }
                                catch (JSONException e)
                                {
                                        e.printStackTrace();
                                }

                                if (code.indexOf("username") != -1)
                                {
                                        //显示错误信息
                                        inputUserNameLayout.setError("用户名错误");

                                }
                                else if (code.indexOf("email") != -1)
                                {
                                        //显示错误信息
                                        inputUserNameLayout.setError("邮箱地址错误");

                                }
                                else if (code.indexOf("password") != -1)
                                {
                                        //显示错误信息
                                        inputUserPasswordLayout.setError("密码错误");
                                }
                                else
                                {
                                        //显示错误信息
                                        inputUserPasswordLayout.setError("未知错误");
                                }
                        }

                        @Override
                        public void onHttpError()
                        {
                                progressBar.setVisibility(View.INVISIBLE);
                                loginButton.setEnabled(true);

                                //显示错误信息
                                inputUserPasswordLayout.setError("网络请求错误, 请重试");
                                //   inputUserPasswordLayout.setErrorEnabled(true);
                        }

                        @Override
                        public void onCancel()
                        {
                                progressBar.setVisibility(View.INVISIBLE);
                                loginButton.setEnabled(true);
                        }
                };
                delegate.login(httpCallBackForUtils, inputUserName.getText().toString(), inputUserPassword.getText().toString());

        }


        /**
         * 设置登陆成功后的信息
         */
        private void setLoginResult(String response)
        {

                UserLogin userLogin = ParserUtils.userLogin(response);
                //储存登陆信息
                PreferencesUtlis.getUserPreference(LoginActivity.this)
                        .edit()
                        .putString(GlobalConfig.Preferences.USER_TOKEN, userLogin.getToken())
                        .putString(GlobalConfig.Preferences.USER_LOGIN, response)
                        .putLong(GlobalConfig.Preferences.USER_TOKEN_EXPIRE, System.currentTimeMillis() + GlobalConfig.Preferences.USER_TOKEN_EXPIRE_TIME)
                        .apply();
                //设置请求结果 为 成功
                setResult(RESULT_OK);
                finish();

        }        //监听标题栏菜单动作

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item)
        {
                switch (item.getItemId())
                {
                        //如果点了返回键
                        case android.R.id.home:
                                //结束当前活动页
                                finish();
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }


        /**
         * 静态 启动本活动 并返回结果的方法
         *
         * @param context
         */
        public static void startActionForResult(Context context)
        {
                Intent intent = new Intent(context, LoginActivity.class);
                ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE);
        }


}
