package com.hwz.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/7.
 */

public class TabDetailBean {
    public TabDatailData data;
    public int retcode;

    public String toString()
    {
        return "TabDetailBean [data=" + this.data + "]";
    }

    public class News
    {
        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        public News()
        {
        }

        public String toString()
        {
            return "News [title=" + this.title + "]";
        }
    }

    public class TabDatailData
    {
        public String countcommenturl;
        public String more;
        public ArrayList<News> news;
        public String title;
        public ArrayList<TabDetailBean.Topic> topic;
        public ArrayList<TabDetailBean.TopNews> topnews;

        public TabDatailData()
        {
        }

        public String toString()
        {
            return "TabDatailData [news=" + this.news + ", title=" + this.title + ", topnews=" + this.topnews + "]";
        }
    }

    public class TopNews
    {
        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;

        public TopNews()
        {
        }

        public String toString()
        {
            return "TopNews [topimage=" + this.topimage + ", title=" + this.title + "]";
        }
    }

    public class Topic
    {
        public String description;
        public String id;
        public String listimage;
        public String sort;
        public String title;
        public String url;

        public Topic()
        {
        }
    }
}
