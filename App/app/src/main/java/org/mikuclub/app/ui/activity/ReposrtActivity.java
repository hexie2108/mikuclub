package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.UtilsDelegate;
import org.mikuclub.app.javaBeans.parameters.CreatePrivateMessageParameters;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.ViewUtils;
import org.mikuclub.app.utils.http.Request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import mikuclub.app.R;

/**
 * 搜索页面
 */
public class ReposrtActivity extends AppCompatActivity
{
        /*静态变量*/
        public static final int TAG = 10;

        /*变量*/

        //数据请求代理人
        private UtilsDelegate delegate;


        /*组件*/
        private TextInputLayout inputContactLayout;
        private TextInputEditText inputContact;
        private TextInputLayout inputReportLayout;
        private TextInputEditText inputReport;
        private Button buttonSend;
        private AlertDialog progressDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_report);
                //绑定组件
                Toolbar toolbar = findViewById(R.id.toolbar);

                inputContactLayout = findViewById(R.id.input_contact_layout);
                inputContact = findViewById(R.id.input_contact);
                inputReportLayout = findViewById(R.id.input_report_layout);
                inputReport = findViewById(R.id.input_report);
                buttonSend = findViewById(R.id.button_send);
                //创建进度条弹窗
                progressDialog = ViewUtils.initProgressDialog(this);

                //创建数据请求 代理人
                delegate = new UtilsDelegate(TAG);


                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setTitle("问题反馈");
                }
                //初始化表单
                initInputForm();

        }

        /**
         * 初始化表单
         */
        private void initInputForm()
        {
                //input内容监听器, 在report内容不为空的情况激活发送按钮
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
                                String content = inputReport.getText().toString().trim();
                                //如果内容不是空, 并大于10个字
                                if (!content.isEmpty() && content.length()>10)
                                {
                                        //激活按钮
                                        buttonSend.setEnabled(true);
                                }
                                else
                                {
                                        //注销按钮
                                        buttonSend.setEnabled(false);
                                }
                        }
                };
                //添加内容变化监听器
                inputReport.addTextChangedListener(textWatcher);
                //绑定发送按钮点击事件
                buttonSend.setOnClickListener(v -> {
                        //发送私信
                        sendReport();
                });
        }

        /**
         * 发送错误报告信息
         */
        private void sendReport(){

                //显示加载进度条
                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack(){
                        @Override
                        public void onSuccess(String response)
                        {
                                //清空内容
                                inputContact.setText("");
                                inputReport.setText("");
                                ToastUtils.longToast("发送成功, 感谢反馈");
                        }

                        @Override
                        public void onError(String response)
                        {
                                LogUtils.e(response);
                                ToastUtils.longToast("发送失败, 请重新尝试");
                        }

                        @Override
                        public void onFinally()
                        {
                                //隐藏进度条
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                //隐藏进度条
                                progressDialog.dismiss();
                        }
                };


                String content = "APP问题反馈: ";
                content += inputReport.getText().toString();
                //获取联系方式
                String contactQQ = inputContact.getText().toString().trim();
                //如果联系方式不是空 并大于5个字
                if(!contactQQ.isEmpty() && contactQQ.length()>5){
                        //在消息尾上加上联系方式
                        content += " 联系QQ: "+inputContact.getText().toString();
                }
                //创建请求参数类
                CreatePrivateMessageParameters bodyParameters = new CreatePrivateMessageParameters();
                bodyParameters.setContent(content);
                //设置接收人为管理员
                bodyParameters.setRecipient_id(GlobalConfig.ADMIN_USER_ID);

                delegate.sendPrivateMessage(httpCallBack, bodyParameters);

        }



        //监听标题栏菜单动作
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


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }


        /**
         * 静态 启动本活动的方法
         *
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, ReposrtActivity.class);
                context.startActivity(intent);
        }


}
