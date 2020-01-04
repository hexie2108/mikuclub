package org.mikuclub.app.javaBeans.resources.modules;

import java.io.Serializable;

/* questo è un classe di supporto per gli altri classe beans*/
public class Rendered implements Serializable
{
        private String rendered;

        public String getRendered()
        {
                return rendered;
        }

        public void setRendered(String rendered)
        {
                this.rendered = rendered;
        }
}
