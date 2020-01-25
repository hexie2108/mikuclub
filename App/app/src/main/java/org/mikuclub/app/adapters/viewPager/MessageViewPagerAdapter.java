package org.mikuclub.app.adapters.viewPager;

import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.ui.fragments.CategoryFragment;
import org.mikuclub.app.ui.fragments.EmptyFragment;
import org.mikuclub.app.ui.fragments.HomeMessageFragment;
import org.mikuclub.app.ui.fragments.HomeMessagePrivateFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * 消息页面活动
 * viewpager 碎片适配器
 */
public class MessageViewPagerAdapter extends FragmentStateAdapter
{
        //两个碎片页面
        private int pageNumber = 2;


        /*如果是在活动里使用*/
        public MessageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
        {
                super(fragmentActivity);
        }

        /*如果是在碎片页里使用*/
        public MessageViewPagerAdapter(@NonNull Fragment fragment)
        {
                super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
                //如果是第一个分页 就用私信页面
                if (position == 0)
                {
                        return new HomeMessagePrivateFragment();

                }
                //如果是后续分页, 就用评论页面
                else
                {
                        return new EmptyFragment();
                }



        }

        /**
         * 返回分页数
         *
         * @return
         */
        @Override
        public int getItemCount()
        {
                return pageNumber;
        }


}
