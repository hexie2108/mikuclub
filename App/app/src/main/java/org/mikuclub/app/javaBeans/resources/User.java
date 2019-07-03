package org.mikuclub.app.javaBeans.resources;

import org.mikuclub.app.javaBeans.resources.modules.Avatar_urls;

import java.util.Date;
import java.util.List;

public class User
{
        private int id;
        private String username;
        private String name;
        private String first_name;
        private String last_name;
        private String email;
        private String url;
        private String description;
        private String link;
        private String locale;
        private String nickname;
        private String slug;
        private Date registered_date;
        private List<String> roles;
        private String password;
        private Object capabilities;
        private Object extra_capabilities;
        private Avatar_urls avatar_urls;
        private Object meta;

        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public String getUsername()
        {
                return username;
        }

        public void setUsername(String username)
        {
                this.username = username;
        }

        public String getName()
        {
                return name;
        }

        public void setName(String name)
        {
                this.name = name;
        }

        public String getFirst_name()
        {
                return first_name;
        }

        public void setFirst_name(String first_name)
        {
                this.first_name = first_name;
        }

        public String getLast_name()
        {
                return last_name;
        }

        public void setLast_name(String last_name)
        {
                this.last_name = last_name;
        }

        public String getEmail()
        {
                return email;
        }

        public void setEmail(String email)
        {
                this.email = email;
        }

        public String getUrl()
        {
                return url;
        }

        public void setUrl(String url)
        {
                this.url = url;
        }

        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
        }

        public String getLink()
        {
                return link;
        }

        public void setLink(String link)
        {
                this.link = link;
        }

        public String getLocale()
        {
                return locale;
        }

        public void setLocale(String locale)
        {
                this.locale = locale;
        }

        public String getNickname()
        {
                return nickname;
        }

        public void setNickname(String nickname)
        {
                this.nickname = nickname;
        }

        public String getSlug()
        {
                return slug;
        }

        public void setSlug(String slug)
        {
                this.slug = slug;
        }

        public Date getRegistered_date()
        {
                return registered_date;
        }

        public void setRegistered_date(Date registered_date)
        {
                this.registered_date = registered_date;
        }

        public List<String> getRoles()
        {
                return roles;
        }

        public void setRoles(List<String> roles)
        {
                this.roles = roles;
        }

        public String getPassword()
        {
                return password;
        }

        public void setPassword(String password)
        {
                this.password = password;
        }

        public Object getCapabilities()
        {
                return capabilities;
        }

        public void setCapabilities(Object capabilities)
        {
                this.capabilities = capabilities;
        }

        public Object getExtra_capabilities()
        {
                return extra_capabilities;
        }

        public void setExtra_capabilities(Object extra_capabilities)
        {
                this.extra_capabilities = extra_capabilities;
        }

        public Avatar_urls getAvatar_urls()
        {
                return avatar_urls;
        }

        public void setAvatar_urls(Avatar_urls avatar_urls)
        {
                this.avatar_urls = avatar_urls;
        }

        public Object getMeta()
        {
                return meta;
        }

        public void setMeta(Object meta)
        {
                this.meta = meta;
        }


}