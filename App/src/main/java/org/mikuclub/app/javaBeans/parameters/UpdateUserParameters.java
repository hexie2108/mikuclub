package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 更新post文章或附件图片 元数据的参数
 */
public class UpdateUserParameters extends BaseParameters
{
        private String nickname;
        private String email;
        private String description;
        private String password;
        private Meta meta;


        public static class Meta
        {
                private int mm_user_avatar;

                public int getMm_user_avatar()
                {
                        return mm_user_avatar;
                }

                public void setMm_user_avatar(int mm_user_avatar)
                {
                        this.mm_user_avatar = mm_user_avatar;
                }
        }


        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();
                //昵称
                putIfNotNull(outputMap, "nickname", nickname);
                //display name 显示名
                putIfNotNull(outputMap, "name", nickname);
                putIfNotNull(outputMap, "email", email);
                putIfNotNull(outputMap, "description", description);
                putIfNotNull(outputMap, "password", password);
                putIfNotNull(outputMap, "meta", meta);
                return outputMap;

        }

        public String getNickname()
        {
                return nickname;
        }

        public void setNickname(String nickname)
        {
                this.nickname = nickname;
        }

        public String getEmail()
        {
                return email;
        }

        public void setEmail(String email)
        {
                this.email = email;
        }

        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
        }

        public String getPassword()
        {
                return password;
        }

        public void setPassword(String password)
        {
                this.password = password;
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
