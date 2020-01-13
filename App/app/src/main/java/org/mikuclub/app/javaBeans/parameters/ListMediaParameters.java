package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class ListMediaParameters extends BaseParameters
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
        private ArrayList<Integer> parent;
        private ArrayList<Integer> parent_exclude;
        private ArrayList<String> slug;
        private String status;
        private String media_type;



        public Map<String, String> toMap()
        {

                Map<String, String> outputMap = new HashMap<String, String>();


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
                putIfNotNull(outputMap, "parent", DataUtils.arrayListToString(parent, "", ","));
                putIfNotNull(outputMap, "parent_exclude", DataUtils.arrayListToString(parent_exclude, "", ","));
                putIfNotNull(outputMap, "slug", DataUtils.arrayListToString(slug, "", ","));
                putIfNotNull(outputMap, "status", status);
                putIfNotNull(outputMap, "media_type", media_type);


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

        public ArrayList<Integer> getParent()
        {
                return parent;
        }

        public void setParent(ArrayList<Integer> parent)
        {
                this.parent = parent;
        }

        public ArrayList<Integer> getParent_exclude()
        {
                return parent_exclude;
        }

        public void setParent_exclude(ArrayList<Integer> parent_exclude)
        {
                this.parent_exclude = parent_exclude;
        }

        public ArrayList<String> getSlug()
        {
                return slug;
        }

        public void setSlug(ArrayList<String> slug)
        {
                this.slug = slug;
        }

        public String getStatus()
        {
                return status;
        }

        public void setStatus(String status)
        {
                this.status = status;
        }

        public String getMedia_type()
        {
                return media_type;
        }

        public void setMedia_type(String media_type)
        {
                this.media_type = media_type;
        }
}
