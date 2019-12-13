package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.data.DateUtils;
import org.mikuclub.app.utils.data.ArrayUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.data.MapUtils.putIfnotNull;

public class ParametersListComments
{
        private String context;
        private Integer page;
        private Integer per_page;
        private String search;
        private Date after;
        private ArrayList<Integer> author;
        private ArrayList<Integer> author_exclude;
        private String author_email;
        private Date before;
        private ArrayList<Integer> exclude;
        private ArrayList<Integer> include;
        private Integer offset;
        private String order;
        private String orderby;
        private Integer parent;
        private Integer post;
        private String status;

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
                putIfnotNull(outputMap, "author_email", author_email);
                putIfnotNull(outputMap, "exclude", ArrayUtils.arrayListToString(exclude, "", ","));
                putIfnotNull(outputMap, "include", ArrayUtils.arrayListToString(include, "", ","));
                putIfnotNull(outputMap, "offset", offset);
                putIfnotNull(outputMap, "order", order);
                putIfnotNull(outputMap, "orderby", orderby);
                putIfnotNull(outputMap, "parent", parent);
                putIfnotNull(outputMap, "post", post);
                putIfnotNull(outputMap, "status", status);

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

        public String getAuthor_email()
        {
                return author_email;
        }

        public void setAuthor_email(String author_email)
        {
                this.author_email = author_email;
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

        public String getStatus()
        {
                return status;
        }

        public void setStatus(String status)
        {
                this.status = status;
        }
}
