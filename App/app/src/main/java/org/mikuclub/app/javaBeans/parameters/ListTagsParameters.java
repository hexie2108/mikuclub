package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class ListTagsParameters extends BaseParameters
{
        private String context;
        private Integer page;
        private Integer per_page;
        private String search;
        private ArrayList<Integer> exclude;
        private ArrayList<Integer> include;
        private Integer offset;
        private String order;
        private String orderby;
        private Boolean hide_empty;
        private Integer post;
        private ArrayList<String> slug;



        public Map<String, String> toMap()
        {

                Map<String, String> outputMap = new HashMap<String, String>();


                putIfNotNull(outputMap, "context", context);
                putIfNotNull(outputMap, "page", page);
                putIfNotNull(outputMap, "per_page", per_page);
                putIfNotNull(outputMap, "search", search);
                putIfNotNull(outputMap, "exclude", DataUtils.arrayListToString(exclude, "", ","));
                putIfNotNull(outputMap, "include", DataUtils.arrayListToString(include, "", ","));
                putIfNotNull(outputMap, "offset", offset);
                putIfNotNull(outputMap, "order", order);
                putIfNotNull(outputMap, "orderby", orderby);
                putIfNotNull(outputMap, "hide_empty", hide_empty);

                putIfNotNull(outputMap, "post", post);
                putIfNotNull(outputMap, "slug", DataUtils.arrayListToString(slug, "", ","));

                //追加参数, 让wordpress 在 回复body中增加页数信息, 不然会被加到 回复header头部里
                putIfNotNull(outputMap, "_envelope", "1");

                return outputMap;

        }

        public String getContext()
        {
                return context;
        }

        public void setContext(String context)
        {
                this.context = context;
        }

        public Integer getPage()
        {
                return page;
        }

        public void setPage(Integer page)
        {
                this.page = page;
        }

        public Integer getPer_page()
        {
                return per_page;
        }

        public void setPer_page(Integer per_page)
        {
                this.per_page = per_page;
        }

        public String getSearch()
        {
                return search;
        }

        public void setSearch(String search)
        {
                this.search = search;
        }

        public ArrayList<Integer> getExclude()
        {
                return exclude;
        }

        public void setExclude(ArrayList<Integer> exclude)
        {
                this.exclude = exclude;
        }

        public ArrayList<Integer> getInclude()
        {
                return include;
        }

        public void setInclude(ArrayList<Integer> include)
        {
                this.include = include;
        }

        public String getOrder()
        {
                return order;
        }

        public void setOrder(String order)
        {
                this.order = order;
        }

        public String getOrderby()
        {
                return orderby;
        }

        public void setOrderby(String orderby)
        {
                this.orderby = orderby;
        }

        public Boolean getHide_empty()
        {
                return hide_empty;
        }

        public void setHide_empty(Boolean hide_empty)
        {
                this.hide_empty = hide_empty;
        }


        public Integer getPost()
        {
                return post;
        }

        public void setPost(Integer post)
        {
                this.post = post;
        }

        public ArrayList<String> getSlug()
        {
                return slug;
        }

        public void setSlug(ArrayList<String> slug)
        {
                this.slug = slug;
        }

        public Integer getOffset()
        {
                return offset;
        }

        public void setOffset(Integer offset)
        {
                this.offset = offset;
        }
}
