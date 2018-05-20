package com.example.administrator.widgetdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: huangxiaoming
 * Date: 2018/5/2
 * Desc:
 * Version: 1.0
 */
public class ZhihuNewsEntity {

    /**
     * date : 20180502
     * stories : [{"images":["https://pic1.zhimg.com/v2-f1f57ba3bf69f4af017bd9f9503c4890.jpg"],"type":0,"id":9680781,"ga_prefix":"050213","title":"男子公交上暴打小孩的事如果发生在欧美，会有怎样的法律后果？"},{"images":["https://pic2.zhimg.com/v2-ec4f30f3035d921d491679f3f0b01e0d.jpg"],"type":0,"id":9680697,"ga_prefix":"050212","title":"大误 · 吃鸡中哪个城区的房子最值得投资？"},{"images":["https://pic3.zhimg.com/v2-860cde4f35ce43acea354c5688d39c16.jpg"],"type":0,"id":9680855,"ga_prefix":"050211","title":"「一分钟内不回话就开除」，像这么骄横跋扈的领导，劝你别忍了"},{"images":["https://pic3.zhimg.com/v2-7f9657c137b52dc5cd102988cb020f3e.jpg"],"type":0,"id":9680323,"ga_prefix":"050210","title":"古代卖私盐犯法，那要是卖咸鱼呢？"},{"images":["https://pic4.zhimg.com/v2-d88943c656b9d089ec5355f1963a8f63.jpg"],"type":0,"id":9680109,"ga_prefix":"050209","title":"对比金庸和古龙的招式，谁写的武功更夸张？"},{"images":["https://pic2.zhimg.com/v2-1138b18bf0b9c9f78f01832a6aa42e5d.jpg"],"type":0,"id":9679573,"ga_prefix":"050208","title":"- 妈，我不想一毕业就回老家工作，结婚生子\r\n- 你可真自私"},{"images":["https://pic4.zhimg.com/v2-abc63eed295877be0ba47514334123cf.jpg"],"type":0,"id":9680197,"ga_prefix":"050207","title":"毕业后进入投行工作是种怎样的体验？"},{"images":["https://pic2.zhimg.com/v2-d39531cf65b80fd886ff63f80499669d.jpg"],"type":0,"id":9679987,"ga_prefix":"050207","title":"当了领导后我才明白的事"},{"images":["https://pic2.zhimg.com/v2-cc900f8de17093369adfe2f47de0ffd1.jpg"],"type":0,"id":9680766,"ga_prefix":"050206","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic1.zhimg.com/v2-99e72c55cbccb948bb7b39de8c4ad1dc.jpg","type":0,"id":9680855,"ga_prefix":"050211","title":"「一分钟内不回话就开除」，像这么骄横跋扈的领导，劝你别忍了"},{"image":"https://pic2.zhimg.com/v2-f583b6f641ecd8e3642041caad93f931.jpg","type":0,"id":9680781,"ga_prefix":"050213","title":"男子公交上暴打小孩的事如果发生在欧美，会有怎样的法律后果？"},{"image":"https://pic2.zhimg.com/v2-141374d1cb00870ef0f4d7a69e7bd239.jpg","type":0,"id":9680756,"ga_prefix":"042907","title":"本周热门精选 · 人在职场"},{"image":"https://pic3.zhimg.com/v2-5b7380a302a25a96ea8c3d92bc4d7266.jpg","type":0,"id":9680713,"ga_prefix":"042908","title":"亲爱的刘看山 · 看山和朋友们"},{"image":"https://pic1.zhimg.com/v2-5f9166d45e9195447481ed46f05018c0.jpg","type":0,"id":9680709,"ga_prefix":"042818","title":"刘爸刘妈的日常 · 甜蜜的烦恼"}]
     */

    private String date;
    private ArrayList<StoriesBean> stories;
    private ArrayList<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(ArrayList<StoriesBean> stories) {
        this.stories = stories;
    }

    public ArrayList<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic1.zhimg.com/v2-f1f57ba3bf69f4af017bd9f9503c4890.jpg"]
         * type : 0
         * id : 9680781
         * ga_prefix : 050213
         * title : 男子公交上暴打小孩的事如果发生在欧美，会有怎样的法律后果？
         */

        private int type;
        private String id;
        private String ga_prefix;
        private String title;
        private ArrayList<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "StoriesBean{" +
                    "type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    ", images=" + images +
                    '}';
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic1.zhimg.com/v2-99e72c55cbccb948bb7b39de8c4ad1dc.jpg
         * type : 0
         * id : 9680855
         * ga_prefix : 050211
         * title : 「一分钟内不回话就开除」，像这么骄横跋扈的领导，劝你别忍了
         */

        private String image;
        private int type;
        private String id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "TopStoriesBean{" +
                    "image='" + image + '\'' +
                    ", type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ZhihuNewsEntity{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }
}
