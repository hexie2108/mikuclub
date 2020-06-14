package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class PostParameters extends BaseParameters
{
        private String context;
        private Integer page;
        private Integer per_page;
        private String search;
        private Date after;
        private ArrayList<Integer> author;
        private ArrayList<Integer> author_exclude;
        private Date before;
        private ArrayList<Integer> exclude;
        private ArrayList<Integer> include;
        private Integer offset;
        private String order;
        private String orderby;
        private ArrayList<String> slug;
        private ArrayList<String> status;
        private ArrayList<Integer> categories;
        private ArrayList<Integer> categories_exclude;
        private ArrayList<Integer> tags;
        private ArrayList<Integer> tags_exclude;
        private Boolean sticky;


        public Map<String, Object> toMap()
        {

                Map<String, Object> outputMap = new HashMap();


                putIfNotNull(outputMap, "before", DataUtils.dateToString(before));
                putIfNotNull(outputMap, "after", DataUtils.dateToString(after));

                putIfNotNull(outputMap, "context", context);
                putIfNotNull(outputMap, "page", page);
                putIfNotNull(outputMap, "per_page", per_page);
                putIfNotNull(outputMap, "search", search);

                putIfNotNull(outputMap, "author", DataUtils.arrayListToString(author, "", ","));
                putIfNotNull(outputMap, "author_exclude", DataUtils.arrayListToString(author_exclude, "", ","));
                putIfNotNull(outputMap, "exclude", DataUtils.arrayListToString(exclude, "", ","));
                putIfNotNull(outputMap, "include", DataUtils.arrayListToString(include, "", ","));
                putIfNotNull(outputMap, "offset", offset);
                putIfNotNull(outputMap, "order", order);
                putIfNotNull(outputMap, "orderby", orderby);
                putIfNotNull(outputMap, "slug", DataUtils.arrayListToString(slug, "", ","));
                putIfNotNull(outputMap, "status", status);
                putIfNotNull(outputMap, "categories", DataUtils.arrayListToString(categories, "", ","));
                putIfNotNull(outputMap, "categories_exclude", DataUtils.arrayListToString(categories_exclude, "", ","));
                putIfNotNull(outputMap, "tags", DataUtils.arrayListToString(tags, "", ","));
                putIfNotNull(outputMap, "tags_exclude", DataUtils.arrayListToString(tags_exclude, "", ","));
                putIfNotNull(outputMap, "sticky", sticky);

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

        public Date getAfter()
        {
                return after;
        }

        public void setAfter(Date after)
        {
                this.after = after;
        }

        public ArrayList<Integer> getAuthor()
        {
                return author;
        }

        public void setAuthor(ArrayList<Integer> author)
        {
                this.author = author;
        }

        public ArrayList<Integer> getAuthor_exclude()
        {
                return author_exclude;
        }

        public void setAuthor_exclude(ArrayList<Integer> author_exclude)
        {
                this.author_exclude = author_exclude;
        }

        public Date getBefore()
        {
                return before;
        }

        public void setBefore(Date before)
        {
                this.before = before;
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

        public Integer getOffset()
        {
                return offset;
        }

        public void setOffset(Integer offset)
        {
                this.offset = offset;
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

        public ArrayList<String> getSlug()
        {
                return slug;
        }

        public void setSlug(ArrayList<String> slug)
        {
                this.slug = slug;
        }

        public ArrayList<String> getStatus()
        {
                return status;
        }

        public void setStatus(ArrayList<String> status)
        {
                this.status = status;
        }

        public ArrayList<Integer> getCategories()
        {
                return categories;
        }

        public void setCategories(ArrayList<Integer> categories)
        {
                this.categories = categories;
        }

        public ArrayList<Integer> getCategories_exclude()
        {
                return categories_exclude;
        }

        public void setCategories_exclude(ArrayList<Integer> categories_exclude)
        {
                this.categories_exclude = categories_exclude;
        }

        public ArrayList<Integer> getTags()
        {
                return tags;
        }

        public void setTags(ArrayList<Integer> tags)
        {
                this.tags = tags;
        }

        public ArrayList<Integer> getTags_exclude()
        {
                return tags_exclude;
        }

        public void setTags_exclude(ArrayList<Integer> tags_exclude)
        {
                this.tags_exclude = tags_exclude;
        }

        public Boolean getSticky()
        {
                return sticky;
        }

        public void setSticky(Boolean sticky)
        {
                this.sticky = sticky;
        }

}
