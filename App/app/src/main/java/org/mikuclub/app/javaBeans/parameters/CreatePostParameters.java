package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class CreatePostParameters extends BaseParameters
{

        private String title;
        private String content;
        private ArrayList<Integer> categories;
        private Meta meta;

        //暂时未用到
        private ArrayList<Integer> tags;


        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();

                putIfNotNull(outputMap, "title", title);
                putIfNotNull(outputMap, "content", content);
                putIfNotNull(outputMap, "categories", categories);
                putIfNotNull(outputMap, "meta", meta);

                putIfNotNull(outputMap, "tags", tags);

                return outputMap;

        }


        public static  class Meta
        {
                private String content;
                private String source_name;
                private ArrayList<String> previews;
                private String down;
                private String down2;
                private String password;
                private String password2;
                private String unzip_password;
                private String unzip_password2;
                private String bilibili;

                //暂时未用到
                private String source;
                private String video;

                public String getContent()
                {
                        return content;
                }

                public void setContent(String content)
                {
                        this.content = content;
                }

                public String getSource_name()
                {
                        return source_name;
                }

                public void setSource_name(String source_name)
                {
                        this.source_name = source_name;
                }

                public ArrayList<String> getPreviews()
                {
                        return previews;
                }

                public void setPreviews(ArrayList<String> previews)
                {
                        this.previews = previews;
                }

                public String getDown()
                {
                        return down;
                }

                public void setDown(String down)
                {
                        this.down = down;
                }

                public String getDown2()
                {
                        return down2;
                }

                public void setDown2(String down2)
                {
                        this.down2 = down2;
                }

                public String getPassword()
                {
                        return password;
                }

                public void setPassword(String password)
                {
                        this.password = password;
                }

                public String getPassword2()
                {
                        return password2;
                }

                public void setPassword2(String password2)
                {
                        this.password2 = password2;
                }

                public String getUnzip_password()
                {
                        return unzip_password;
                }

                public void setUnzip_password(String unzip_password)
                {
                        this.unzip_password = unzip_password;
                }

                public String getUnzip_password2()
                {
                        return unzip_password2;
                }

                public void setUnzip_password2(String unzip_password2)
                {
                        this.unzip_password2 = unzip_password2;
                }

                public String getBilibili()
                {
                        return bilibili;
                }

                public void setBilibili(String bilibili)
                {
                        this.bilibili = bilibili;
                }

                public String getSource()
                {
                        return source;
                }

                public void setSource(String source)
                {
                        this.source = source;
                }

                public String getVideo()
                {
                        return video;
                }

                public void setVideo(String video)
                {
                        this.video = video;
                }
        }

        public String getTitle()
        {
                return title;
        }

        public void setTitle(String title)
        {
                this.title = title;
        }

        public String getContent()
        {
                return content;
        }

        public void setContent(String content)
        {
                this.content = content;
        }

        public ArrayList<Integer> getCategories()
        {
                return categories;
        }

        public void setCategories(ArrayList<Integer> categories)
        {
                this.categories = categories;
        }

        public ArrayList<Integer> getTags()
        {
                return tags;
        }

        public void setTags(ArrayList<Integer> tags)
        {
                this.tags = tags;
        }

        public Meta getMeta()
        {
                return meta;
        }

        public void setMeta(Meta meta)
        {
                this.meta = meta;
        }
}




