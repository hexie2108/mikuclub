package org.mikuclub.app.javaBeans.parameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class CreatePrivateMessageParameters extends BaseParameters
{

        private String content;
        private Integer recipient_id;
        private String respond;


        public Map<String, String> toMap()
        {
                Map<String, String> outputMap = new HashMap<String, String>();

                putIfNotNull(outputMap, "content", content);
                putIfNotNull(outputMap, "recipient_id", recipient_id);
                putIfNotNull(outputMap, "respond", respond);

                return outputMap;

        }

        public String getContent()
        {
                return content;
        }

        public void setContent(String content)
        {
                this.content = content;
        }

        public Integer getRecipient_id()
        {
                return recipient_id;
        }

        public void setRecipient_id(Integer recipient_id)
        {
                this.recipient_id = recipient_id;
        }

        public String getRespond()
        {
                return respond;
        }

        public void setRespond(String respond)
        {
                this.respond = respond;
        }
}
