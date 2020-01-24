package org.mikuclub.app.javaBeans.parameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 基本的请求参数
 * 只包含 _envelope 参数
 */
public class BaseParameters
{

        public Map<String, Object> toMap()
        {

                Map<String, Object> outputMap = new HashMap();
                //追加参数, 让wordpress 在 回复body中增加页数信息, 不然会被加到 回复header头部里
                putIfNotNull(outputMap, "_envelope", "1");
                return outputMap;

        }

}
