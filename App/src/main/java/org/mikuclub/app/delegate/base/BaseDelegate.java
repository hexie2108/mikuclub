package org.mikuclub.app.delegate.base;

import org.mikuclub.app.delegate.models.ResourceModel;

/**
 * 根据需要生成对应资源的请求
 * base request delegate
 */
public class BaseDelegate
{
        private ResourceModel model;
        private int tag;

        public BaseDelegate(int tag)
        {
                this.tag = tag;
        }

        public BaseDelegate(int tag, ResourceModel model)
        {
                this.tag = tag;
                this.model = model;
        }

        public ResourceModel getModel()
        {
                return model;
        }

        public int getTag()
        {
                return tag;
        }

        public void setModel(ResourceModel model)
        {
                this.model = model;
        }
}
