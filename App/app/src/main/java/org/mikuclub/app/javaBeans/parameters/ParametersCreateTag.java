package org.mikuclub.app.javaBeans.parameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.data.MapUtils.putIfnotNull;

public class ParametersCreateTag
{

        private String description;
        private String name;
        private String slug;



        public Map<String, String> toMap()
        {
                Map<String, String> outputMap = new HashMap<String, String>();

                putIfnotNull(outputMap, "description", description);
                putIfnotNull(outputMap, "name", name);
                putIfnotNull(outputMap, "slug", slug);

                return outputMap;

        }


        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
        }

        public String getName()
        {
                return name;
        }

        public void setName(String name)
        {
                this.name = name;
        }

        public String getSlug()
        {
                return slug;
        }

        public void setSlug(String slug)
        {
                this.slug = slug;
        }
}
