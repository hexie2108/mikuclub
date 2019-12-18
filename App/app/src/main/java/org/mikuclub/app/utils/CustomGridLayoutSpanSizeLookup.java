package org.mikuclub.app.utils;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 自定义每个组件使用的  gridlayout 列数
 * 让最后一个组件占据自定义列数
 */
public class CustomGridLayoutSpanSizeLookup extends  GridLayoutManager.SpanSizeLookup
{

        private List dataList;
        private int columnsNumberToSpan;

        /**
         *
         * @param dataList 数据列表
         * @param columnsNumberToSpan 最后一个item需要占据的列数
         */
        public CustomGridLayoutSpanSizeLookup(List dataList, int columnsNumberToSpan)
        {
                this.dataList = dataList;
                this.columnsNumberToSpan = columnsNumberToSpan;
        }

        @Override
        public int getSpanSize(int position)
        {
                //默认每个视图占1列
                int columns = 1;
                //position从0开始, 所以只有碰到 视图末尾(非item) 的加载组件的时候, 才会进入
                if (position == dataList.size())
                {
                        columns = columnsNumberToSpan;
                }

                return columns;
        }
}
