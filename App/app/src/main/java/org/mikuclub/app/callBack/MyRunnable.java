package org.mikuclub.app.callBack;


/**
 * 自定义Runnable
 * 允许传递变量
 */
public class MyRunnable implements Runnable
{
        private Object argument1;
        private Object argument2;

        public MyRunnable(Object argument1)
        {
                this.argument1 = argument1;
        }

        public MyRunnable(Object argument1, Object argument2)
        {
                this.argument1 = argument1;
                this.argument2 = argument2;
        }

        public Object getArgument1()
        {
                return argument1;
        }

        public void setArgument1(Object argument1)
        {
                this.argument1 = argument1;
        }

        public Object getArgument2()
        {
                return argument2;
        }

        public void setArgument2(Object argument2)
        {
                this.argument2 = argument2;
        }

        @Override
        public void run()
        {

        }
}
