package org.mikuclub.app.javaBeans.response.baseResource;

import java.io.Serializable;
import java.util.List;

public class UserLogin implements Serializable
{
        private int id;
        private String user_login;
        private String user_email;
        private String user_display_name;
        private String token;
        private String avatar_urls;
        private UserMeta user_meta;

        class UserMeta{

                private List<String> mm_capabilities;
                private List<Integer> mm_user_level;
                private List<Integer> mycred_default;
                private List<String> open_type;
                private List<Integer> my_post_count;
                private List<Integer> my_comment_count;
                private List<Integer> my_rating_count;
                private List<Integer> total_post_click;
                private List<Integer> total_post_comment;
                private List<Integer> total_post_rating;
                private List<Integer> total_post_count;

                public List<String> getMm_capabilities()
                {
                        return mm_capabilities;
                }

                public void setMm_capabilities(List<String> mm_capabilities)
                {
                        this.mm_capabilities = mm_capabilities;
                }

                public List<Integer> getMm_user_level()
                {
                        return mm_user_level;
                }

                public void setMm_user_level(List<Integer> mm_user_level)
                {
                        this.mm_user_level = mm_user_level;
                }

                public List<Integer> getMycred_default()
                {
                        return mycred_default;
                }

                public void setMycred_default(List<Integer> mycred_default)
                {
                        this.mycred_default = mycred_default;
                }

                public List<String> getOpen_type()
                {
                        return open_type;
                }

                public void setOpen_type(List<String> open_type)
                {
                        this.open_type = open_type;
                }

                public List<Integer> getMy_post_count()
                {
                        return my_post_count;
                }

                public void setMy_post_count(List<Integer> my_post_count)
                {
                        this.my_post_count = my_post_count;
                }

                public List<Integer> getMy_comment_count()
                {
                        return my_comment_count;
                }

                public void setMy_comment_count(List<Integer> my_comment_count)
                {
                        this.my_comment_count = my_comment_count;
                }

                public List<Integer> getMy_rating_count()
                {
                        return my_rating_count;
                }

                public void setMy_rating_count(List<Integer> my_rating_count)
                {
                        this.my_rating_count = my_rating_count;
                }

                public List<Integer> getTotal_post_click()
                {
                        return total_post_click;
                }

                public void setTotal_post_click(List<Integer> total_post_click)
                {
                        this.total_post_click = total_post_click;
                }

                public List<Integer> getTotal_post_comment()
                {
                        return total_post_comment;
                }

                public void setTotal_post_comment(List<Integer> total_post_comment)
                {
                        this.total_post_comment = total_post_comment;
                }

                public List<Integer> getTotal_post_rating()
                {
                        return total_post_rating;
                }

                public void setTotal_post_rating(List<Integer> total_post_rating)
                {
                        this.total_post_rating = total_post_rating;
                }

                public List<Integer> getTotal_post_count()
                {
                        return total_post_count;
                }

                public void setTotal_post_count(List<Integer> total_post_count)
                {
                        this.total_post_count = total_post_count;
                }
        }



        public String getAvatar_urls()
        {
                return avatar_urls;
        }

        public void setAvatar_urls(String avatar_urls)
        {
                this.avatar_urls = avatar_urls;
        }

        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public String getUser_login()
        {
                return user_login;
        }

        public void setUser_login(String user_login)
        {
                this.user_login = user_login;
        }

        public String getUser_email()
        {
                return user_email;
        }

        public void setUser_email(String user_email)
        {
                this.user_email = user_email;
        }

        public String getUser_display_name()
        {
                return user_display_name;
        }

        public void setUser_display_name(String user_display_name)
        {
                this.user_display_name = user_display_name;
        }

        public String getToken()
        {
                return token;
        }

        public void setToken(String token)
        {
                this.token = token;
        }


        public UserMeta getUser_meta()
        {
                return user_meta;
        }

        public void setUser_meta(UserMeta user_meta)
        {
                this.user_meta = user_meta;
        }
}