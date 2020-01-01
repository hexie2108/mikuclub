package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.data.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.data.MapUtils.putIfnotNull;

public class ParametersListCategories
{
        private String context;
        private Integer page;
        private Integer per_page;
        private String search;
        private ArrayList<Integer> exclude;
        private ArrayList<Integer> include;
        private String order;
        private String orderby;
        private Boolean hide_empty;
        private Integer parent;
        private Integer post;
        private ArrayList<String> slug;




        public Map<String, String> toMap()
        {

                Map<String, String> outputMap = new HashMap<String, String>();


                putIfnotNull(outputMap, "context", context);
                putIfnotNull(outputMap, "page", page);
                putIfnotNull(outputMap, "per_page", per_page);
                putIfnotNull(outputMap, "search", search);
                putIfnotNull(outputMap, "exclude", ArrayUtils.arrayListToString(exclude, "", ","));
                putIfnotNull(outputMap, "include", ArrayUtils.arrayListToString(include, "", ","));
                putIfnotNull(outputMap, "order", order);
                putIfnotNull(outputMap, "orderby", orderby);
                putIfnotNull(outputMap, "hide_empty", hide_empty);
                putIfnotNull(outputMap, "parent", parent);
                putIfnotNull(outputMap, "post", post);
                putIfnotNull(outputMap, "slug", ArrayUtils.arrayListToString(slug, "", ","));

                //追加参数, 让wordpress 在 回复body中增加页数信息, 不然会被加到 回复header头部里
                putIfnotNull(outputMap, "_envelope", "1");

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

        public Integer getParent()
        {
                return parent;
        }

        public void setParent(Integer parent)
        {
                this.parent = parent;
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
}
