package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 更新post文章或附件图片 元数据的参数
 */
public class UpdatePostMetaParameters extends BaseParameters
{
      private Integer post_id;
      private String meta_key;
      private Object meta_value;



        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();
                putIfNotNull(outputMap, "post_id", post_id);
                putIfNotNull(outputMap, "meta_key", meta_key);
                putIfNotNull(outputMap, "meta_value", meta_value);
                return outputMap;

        }


        public Integer getPost_id()
        {
                return post_id;
        }

        public void setPost_id(Integer post_id)
        {
                this.post_id = post_id;
        }

        public String getMeta_key()
        {
                return meta_key;
        }

        public void setMeta_key(String meta_key)
        {
                this.meta_key = meta_key;
        }

        public Object getMeta_value()
        {
                return meta_value;
        }

        public void setMeta_value(Object meta_value)
        {
                this.meta_value = meta_value;
        }
}
