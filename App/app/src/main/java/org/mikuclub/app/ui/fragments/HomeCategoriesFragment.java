package org.mikuclub.app.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.CategoriesAdapter;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.RecyclerViewUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 主页活动-分类碎片
 */
public class HomeCategoriesFragment extends Fragment
{


        //列表适配器
        private CategoriesAdapter recyclerViewAdapter;
        //列表数据
        private List<Category> recyclerDataList;
        //应用参数
        private SharedPreferences preferences;

        /*组件*/
        //列表
        private RecyclerView recyclerView;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_home_categories, container, false);

        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {

                super.onViewCreated(view, savedInstanceState);

                recyclerView = view.findViewById(R.id.recycler_view);

                //获取软件应用参数文件
                preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                //从参数里获取分类信息
                String categoriesCache = preferences.getString(GlobalConfig.Preferences.CATEGORIES_CACHE, "");
                //反序列化
                recyclerDataList = ParserUtils.categories(categoriesCache).getBody();

                //初始化数据列表
                initRecyclerView();

        }


        /**
         * 初始化文章列表
         */
        private void initRecyclerView()
        {
                //创建适配器
                recyclerViewAdapter = new CategoriesAdapter(recyclerDataList, getActivity());

                //创建列表网格布局
                //设置行数
                int numberColumn = 2;
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberColumn);

                //配置列表
                RecyclerViewUtils.setup(recyclerView, recyclerViewAdapter, layoutManager, recyclerDataList.size() , true, true, null);

        }


}
