package ming.com.slideshow_lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Author MingRuQi
 * E-mail mingruqi@sina.cn
 * DateTime 2018/12/14 8:18
 */
public class SlideShow extends FrameLayout {

    private Context context;
    private RadioGroup radioGroup;
    private TextView title;
    private ViewPager banners;


    /**
     * 指示器区域高度
     */
    private float indicatorHeight;


    /**
     * 指示器背景色
     */
    private int indicatorBgColor;

    /**
     * 指示器大小
     */
    private float indicatorSize;


    /**
     * 标题文字颜色
     */
    private int titleColor;

    /**
     * 标题文字大小
     */
    private float titleSize;
    /**
     * 指示器选择器
     */
    int mIndicatorSelecter;

    /**
     * 轮播图数据
     */
    List<Info> infoList;
    /**
     * 是否需要标题
     */
    private boolean isTitle = true;

    private TypedArray array;
    private List<ImageView> bannerlist;

    public SlideShow(@NonNull Context context) {
        this(context, null);
    }

    public SlideShow(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.slideshow, this);
        radioGroup = findViewById(R.id.indicator);
        title = findViewById(R.id.banner_title);
        banners = findViewById(R.id.vp_banner);
        array = context.obtainStyledAttributes(attrs, R.styleable.SlideShow);
        init();
    }

    private void init() {
        setIndicatorSelecter(array.getResourceId(R.styleable.SlideShow_indicatorSelecter, R.drawable.selecter_slideindicator));
        setIndicatorBgColor(array.getColor(R.styleable.SlideShow_indicatorBgColor, 0x2A000000));
        setIndicatorSize(array.getDimension(R.styleable.SlideShow_indicatorSize, 10));
        setIndicatorHeight(array.getDimension(R.styleable.SlideShow_indicatorHeight, 40));
        setTitleColor(array.getColor(R.styleable.SlideShow_titleColor, 0xffffffff));
        setTitleSize(array.getDimension(R.styleable.SlideShow_titleSize, 10));
        array.recycle();
    }

    /**
     * 获取指示器大小
     *
     * @return
     */
    public float getIndicatorSize() {
        return indicatorSize;
    }

    /**
     * 设置指示器大小
     *
     * @param indicatorSize
     */
    public void setIndicatorSize(float indicatorSize) {
        this.indicatorSize = indicatorSize;
    }

    /**
     * 获取指示器背景色
     */
    public int getIndicatorBgColor() {
        return indicatorBgColor;
    }

    /**
     * 设置指示器背景色
     */
    public void setIndicatorBgColor(int indicatorBgColor) {
        this.indicatorBgColor = indicatorBgColor;
        radioGroup.setBackgroundColor(indicatorBgColor);
    }

    /**
     * 获取指示器区域高度
     */
    public float getIndicatorHeight() {
        return indicatorHeight;
    }

    /**
     * 设置指示器区域高度
     */
    public void setIndicatorHeight(float indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        LinearLayout.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) indicatorHeight);
        radioGroup.setLayoutParams(params);
    }

    /**
     * 获取标题文字颜色
     */
    public int getTitleColor() {
        return titleColor;
    }

    /**
     * 设置标题文字颜色
     */
    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * 获取标题文字大小
     */
    public float getTitleSize() {
        return titleSize;
    }

    /**
     * 设置标题文字大小
     */
    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
    }

    /**
     * 不需要标题
     */
    public void needlessTitle() {
        isTitle = false;
    }

    /**
     * =================================================================================================================
     */


    /**
     * 设置指示器
     *
     * @param indicator   当前显示状态
     * @param unIndicator 未显示状态
     */
    public void setIndicatorSelecter(int indicator, int unIndicator) {
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = context.getResources().getDrawable(unIndicator);
        Drawable checked = context.getResources().getDrawable(indicator);
        drawable.addState(new int[]{android.R.attr.checkable}, checked);
        drawable.addState(new int[]{-android.R.attr.checkable}, normal);
    }

    /**
     * 设置指示器
     *
     * @param indicatorSelecter 状态选择器
     */
    public void setIndicatorSelecter(int indicatorSelecter) {
        mIndicatorSelecter = indicatorSelecter;
        Drawable drawable = context.getResources().getDrawable(mIndicatorSelecter);
    }

    /**
     * 设置轮播图数据
     *
     * @param infoList
     */
    public void setData(List<Info> infoList) {
        this.infoList = infoList;
    }

    /**
     * 设置点击监听
     */
    public void setSlideOnItemClickLisenter() {

    }

    public void commit() {
        initBannerImage();
    }

    /**
     * 初始化轮播图片
     */
    private void initBannerImage() {
        bannerlist = new ArrayList<>();
        int count = infoList.size();
        if (count > 0 && count < 2) {
            count = 1;
        } else if (count >= 2) {
            count += 2;
        } else {
            return;
        }
        for (int i = 0; i < count; i++) {
            String imageUri;
            if (i == 0) {
                imageUri = infoList.get(infoList.size() - 1).getBannerUri();
            } else if (i == count - 1) {
                imageUri = infoList.get(0).getBannerUri();
            } else {
                imageUri = infoList.get(i - 1).getBannerUri();
            }
            ImageView imageView = new ImageView(context);
            imageView.setId(i);
            Picasso.with(context).load(imageUri).into(imageView);
            bannerlist.add(imageView);
        }
        BannerViewPager bannerViewPager = new BannerViewPager();
        banners.setAdapter(bannerViewPager);
    }

    /**
     * 设置指示器
     */
    private void setIndicator() {

    }


    /**
     * ==================================================================================================
     * 轮播图适配器
     * ==================================================================================================
     */

    private class BannerViewPager extends PagerAdapter {

        @Override
        public int getCount() {
            return bannerlist == null ? 0 : bannerlist.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if (bannerlist.size() > 0) {
                container.removeView(bannerlist.get(position));
            } else {
                super.destroyItem(container, position, object);
            }
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = bannerlist.get(position);
            container.addView(view);
            return view;
        }
    }
}
