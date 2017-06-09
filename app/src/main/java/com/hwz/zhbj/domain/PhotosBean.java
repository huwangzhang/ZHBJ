package com.hwz.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/9.
 */

public class PhotosBean {
    public PhotosData data;
    public int retcode;

    public String toString()
    {
        return "PhotosBean [data=" + this.data + "]";
    }

    public class PhotosData
    {
        public String countcommenturl;
        public String more;
        public ArrayList<PhotosNews> news;

        public PhotosData()
        {
        }

        public String toString()
        {
            return "PhotosData [news=" + this.news + "]";
        }
    }

    public class PhotosNews
    {
        public boolean comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String largeimage;
        public String listimage;
        public String pubdate;
        public String smallimage;
        public String title;
        public String type;
        public String url;

        public PhotosNews()
        {
        }

        public String toString()
        {
            return "PhotosNews [listimage=" + this.listimage + ", title=" + this.title + "]";
        }
    }
}
