package com.ming.slideshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ming.com.slideshow_lib.Info;
import ming.com.slideshow_lib.SlideShow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SlideShow slideShow = findViewById(R.id.ss);
        final int[] p = {0};
        List<Info> mList = new ArrayList<>();
        Info info = new Info();
        info.setBannerTitle("标题0");
        info.setBannerUri("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2097124721,3074829049&fm=27&gp=0.jpg");
        Info info1 = new Info();
        info1.setBannerTitle("标题1");
        info1.setBannerUri("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3034670085,1677733933&fm=27&gp=0.jpg");
        Info info2 = new Info();
        info2.setBannerTitle("标题2");
        info2.setBannerUri("http://imgsrc.baidu.com/forum/pic/item/b64543a98226cffc8872e00cb9014a90f603ea30.jpg");
        Info info3 = new Info();
        info3.setBannerTitle("标题3");
        info3.setBannerUri("http://imgsrc.baidu.com/forum/pic/item/261bee0a19d8bc3e6db92913828ba61eaad345d4.jpg");
        mList.add(info);
        mList.add(info1);
        mList.add(info2);
        mList.add(info3);
        mList.add(info1);
        slideShow.setData(mList);
        slideShow.setSlideOnItemClickLisenter(new SlideShow.OnItemClickLisenter() {
            @Override
            public void onClickLisenter(int position) {
                Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
                if (p[0] == 0) {
                    slideShow.cancelSlide();
                    p[0] = 1;
                }else {
                    slideShow.startSlide();
                }

            }
        });
        slideShow.commit();
        slideShow.startSlide();

    }
}
