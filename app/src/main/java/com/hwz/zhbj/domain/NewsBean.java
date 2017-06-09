package com.hwz.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/6.
 */

public class NewsBean {
    public ArrayList<NewsMenuBean> data;
    public ArrayList<String> extend;
    public int retcode;

    public String toString()
    {
        return "NewsBean [data=" + this.data + ", retcode=" + this.retcode + "]";
    }

    public class NewsMenuBean
    {
        public ArrayList<NewsBean.NewsMenuTab> children;
        public String id;
        public String title;
        public int type;
        public String url;
        public String url1;

        public NewsMenuBean()
        {
        }

        public String toString()
        {
            return "NewsMenuBean [children=" + this.children + ", title=" + this.title + "]";
        }
    }

    public class NewsMenuTab
    {
        public String id;
        public String title;
        public int type;
        public String url;

        public NewsMenuTab()
        {
        }

        public String toString()
        {
            return "NewsMenuTab [id=" + this.id + ", title=" + this.title + "]";
        }
    }
}
