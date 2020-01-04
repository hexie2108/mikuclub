package org.mikuclub.app.controller;

import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.callBack.CallBack;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.custom.MyEditTextNumberFilterMinMax;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import mikuclub.app.R;

/**
 * 文章列表控制器
 */
public class PostController
{


        /**
         * 创建并展示 输入弹窗
         */
        public static void openAlertDialog(final AppCompatActivity activity, int currentPage, int totalPage, final CallBack callBack)
        {

                //创建布局组件
                View view = activity.getLayoutInflater().inflate(R.layout.alert_dialog_input, (ViewGroup) activity.findViewById(android.R.id.content).getRootView(), false);
                final EditText input = view.findViewById(R.id.text_input);
                TextInputLayout textInputLayout = view.findViewById(R.id.text_input_layout);
                //加载占位符
                textInputLayout.setHint("第" + currentPage + "页 / 总共" + totalPage + "页");
                //限制input输入范围
                input.setFilters(new InputFilter[]{new MyEditTextNumberFilterMinMax(1, totalPage)});
                //获取焦点+弹出键盘
                KeyboardUtils.showKeyboard(input);

                //创建 弹出构造器
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                //设置标题
                builder.setTitle("跳转");
                //加载布局
                builder.setView(view);
                //设置确认按钮 和 动作
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
                {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                                //必须不是空
                                if (!input.getText().toString().isEmpty())
                                {
                                        //传递要跳转的页码
                                        callBack.execute(input.getText().toString());
                                }
                                else
                                {
                                        Toast.makeText(activity, "未输入页数", Toast.LENGTH_SHORT).show();
                                }

                        }
                });
                //设置取消按钮 和 无动作
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                });
                //显示弹出
                final AlertDialog alertDialog = builder.show();

                //监听键盘动作
                input.setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                        {
                                //如果从键盘点了确认键
                                if (actionId == EditorInfo.IME_ACTION_DONE)
                                {
                                        //触发弹窗确认键的点击
                                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                                }
                                return true;
                        }
                });

        }

}
