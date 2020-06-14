package org.mikuclub.app.utils.custom;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *限制数字范围的ediText 输入的范围的过滤器
 * The filter to limit the range of input value of editText
 *
 * author: Dharin Rajgor
 * EditText et = (EditText) findViewById(R.id.myEditText);
 * et.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
 * * https://www.techcompose.com/how-to-set-minimum-and-maximum-value-in-edittext-in-android-app-development/
 */
public class MyEditTextNumberFilterMinMax implements InputFilter
{
        private int min, max;

        public MyEditTextNumberFilterMinMax(int min, int max)
        {
                this.min = min;
                this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
        {
                try
                {
                        // Remove the string out of destination that is to be replaced
                        String newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend);
                        // Add the new string in
                        newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(dstart);
                        int input = Integer.parseInt(newVal);
                        if (isInRange(min, max, input))
                        {
                                return null;
                        }
                }
                catch (NumberFormatException nfe)
                {
                        nfe.printStackTrace();
                }
                return "";
        }

        private boolean isInRange(int a, int b, int c)
        {
                return b > a ? c >= a && c <= b : c >= b && c <= a;
        }


}
