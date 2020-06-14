package org.mikuclub.app.adapter.viewPager;

import org.mikuclub.app.javaBeans.response.baseResource.Category;
import org.mikuclub.app.ui.fragments.CategoryFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**
 * 分类页面 viewpager 碎片适配器
 * category page, fragment adapter for viewpager
 */
public class CategoryViewPagerAdapter extends FragmentStateAdapter
{
        private Category category;

        /**
         * @param fragmentActivity
         * @param category 相关的分类数据
         */
        public CategoryViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Category category)
        {
                super(fragmentActivity);
                this.category = category;
        }

        /**
         * 创建fragment
         * create fragment
         * @param position
         * @return
         */
        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
                Category categoryData;
                //如果是第一个分页, 碎片的启动数据 就用主分类自己
                if (position == 0)
                {
                        categoryData = category;
                }
                //如果是后续分页, 则使用主分类的下辖子分类
                else
                {
                        //因为position 0 被主分类自己用掉了, 所以要-1 来修正 数组的位置偏差
                        categoryData = category.getChildren().get(position - 1);
                }
                return CategoryFragment.startAction(categoryData);


        }

        /**
         * 返回需要创建的分页数量
         * Returns the number of fragment to create
         * @return
         */
        @Override
        public int getItemCount()
        {
                //默认只创建一个
                int count = 1;
                //如果有子分类
                if (category.getChildren() != null)
                {
                        //加上子分类的数量
                        count = count + category.getChildren().size();
                }
                return count;
        }


}
