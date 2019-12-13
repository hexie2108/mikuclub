package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.data.DateUtils;
import org.mikuclub.app.utils.data.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.data.MapUtils.putIfnotNull;

public class ParametersListMedia
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


                putIfnotNull(outputMap, "before", DateUtils.dateToString(before));
                putIfnotNull(outputMap, "after", DateUtils.dateToString(after));

                putIfnotNull(outputMap, "context", context);
                putIfnotNull(outputMap, "page", page);
                putIfnotNull(outputMap, "per_page", per_page);
                putIfnotNull(outputMap, "search", search);

                putIfnotNull(outputMap, "author", ArrayUtils.arrayListToString(author, "", ","));
                putIfnotNull(outputMap, "author_exclude", ArrayUtils.arrayListToString(author_exclude, "", ","));
                putIfnotNull(outputMap, "exclude", ArrayUtils.arrayListToString(exclude, "", ","));
                putIfnotNull(outputMap, "include", ArrayUtils.arrayListToString(include, "", ","));
                putIfnotNull(outputMap, "offset", offset);
                putIfnotNull(outputMap, "order", order);
                putIfnotNull(outputMap, "orderby", orderby);
                putIfnotNull(outputMap, "parent", ArrayUtils.arrayListToString(parent, "", ","));
                putIfnotNull(outputMap, "parent_exclude", ArrayUtils.arrayListToString(parent_exclude, "", ","));
                putIfnotNull(outputMap, "slug", ArrayUtils.arrayListToString(slug, "", ","));
                putIfnotNull(outputMap, "status", status);
                putIfnotNull(outputMap, "media_type", media_type);


                //parametri in più per ottenere il numero totale di pagina in risposta body, invece in header
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
