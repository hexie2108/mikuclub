package org.mikuclub.app.utils.custom;

import android.text.Editable;
import android.text.TextWatcher;

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

}
