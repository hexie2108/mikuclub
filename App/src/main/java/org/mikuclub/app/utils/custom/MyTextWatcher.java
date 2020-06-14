package org.mikuclub.app.utils.custom;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * 自定义 editText 变化监视器
 * 只在文字内容变化完成后 才会调用回调
 * custom text watcher
 * the callback only works on onTextChanged
 */
public class MyTextWatcher implements TextWatcher
{
        private MyCallback callback;

        public MyTextWatcher(MyCallback callback)
        {
                this.callback = callback;
        }

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

                //调用回调程序
                callback.onExecute();
        }


        /**
         * 创建 变化监视器 监听空内容错误
         *
         * @param input
         * @param inputLayout
         * @param errorMessage
         * @return
         */
        public static MyTextWatcher createMyTextWatcherForEmpty(TextInputEditText input, TextInputLayout inputLayout, String errorMessage)
        {

                MyTextWatcher myTextWatcher = new MyTextWatcher(() -> {
                        //获取内容
                        if (input.getText() != null)
                        {
                                String text = input.getText().toString().trim();
                                //如果内容为空
                                if (text.isEmpty())
                                {
                                        //显示错误
                                        inputLayout.setError(errorMessage);
                                }
                                else
                                {
                                        //取消错误
                                        inputLayout.setError(null);
                                }
                        }
                });
                return myTextWatcher;

        }

}
