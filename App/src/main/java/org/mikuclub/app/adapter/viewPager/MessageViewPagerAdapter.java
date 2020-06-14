package org.mikuclub.app.adapter.viewPager;

import org.mikuclub.app.ui.fragments.HomeMessagePrivateFragment;
import org.mikuclub.app.ui.fragments.HomeMessageReplyCommentFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * 消息页面 viewpager 碎片适配器
 * message page, fragment adapter for viewpager
 */
public class MessageViewPagerAdapter extends FragmentStateAdapter
{
        private static final int FRAGMENT_NUMBER = 2;

        /**
         * 如果是在活动里使用
         *
         * @param fragmentActivity
         */
        public MessageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
        {
                super(fragmentActivity);
        }

        /**
         * 如果是在活动里使用
         *
         * @param fragment
         */
        public MessageViewPagerAdapter(@NonNull Fragment fragment)
        {
                super(fragment);
        }

        /**
         * 创建fragment
         * create fragment
         *
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
                //如果是第一个分页 就用私信页面
                if (position == 0)
                {
                        return new HomeMessagePrivateFragment();

                }
                //如果是第二个分页, 就用评论回复页面
                else
                {
                        return new HomeMessageReplyCommentFragment();
                }
        }

        /**
         * 返回需要创建的分页数量
         * Returns the number of fragment to create
         *
         * @return
         */
        @Override
        public int getItemCount()
        {
                return FRAGMENT_NUMBER;
        }


}
