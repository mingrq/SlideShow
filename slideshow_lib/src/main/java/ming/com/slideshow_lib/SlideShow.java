package ming.com.slideshow_lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private LinearLayout indicator;
    private ViewPager banners;


    /**
     * 指示器区域高度
     */
    private int mIndicatorHeight;


    /**
     * 指示器背景色
     */
    private int indicatorBgColor;

    /**
     * 指示器大小
     */
    private int mIndicatorSize;

    /**
     * 指示器选择器
     */
    int mIndicatorSelecter;

    /**
     * 轮播图数据
     */
    List<String> infoList;


    /**
     * 指示器间距
     */
    private int mIndicatorMargin;

    /**
     * 滚动时间
     */
    private int slideTime;
    /**
     * item滑动监听
     */
    private OnItemTouchLisenter onItemTouchLisenter;
    private int indicatorEn;
    private TypedArray array;
    private List<ImageView> bannerlist;
    private boolean isAutoPlay = true;
    private Runnable task;
    private OnItemClickLisenter onItemClickLisenter;
    private Handler handler;
    private Handler handler1;

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
        indicator = findViewById(R.id.indicator);
        banners = findViewById(R.id.vp_banner);
        array = context.obtainStyledAttributes(attrs, R.styleable.SlideShow);
        init();
    }

    private void init() {
        setIndicatorSelecter(array.getResourceId(R.styleable.SlideShow_indicatorSelecter, R.drawable.selecter_slideindicator));
        setIndicatorBgColor(array.getColor(R.styleable.SlideShow_indicatorBgColor, 0x2A000000));
        setIndicatorSize(array.getDimension(R.styleable.SlideShow_indicatorSize, dp2px(6)));
        setIndicatorHeight(array.getDimension(R.styleable.SlideShow_indicatorHeight, dp2px(30)));
        setSlideTime(array.getInteger(R.styleable.SlideShow_slideTime, 4000));
        setIndicatorMargin(array.getDimension(R.styleable.SlideShow_indicatorMargin, dp2px(6)));
        array.recycle();
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
                imageUri = infoList.get(infoList.size() - 1);
            } else if (i == count - 1) {
                imageUri = infoList.get(0);
            } else {
                imageUri = infoList.get(i - 1);
            }
            ImageView imageView = new ImageView(context);
            imageView.setId(i);
            Picasso.with(context).load(imageUri).into(imageView);
            bannerlist.add(imageView);
        }
        BannerViewPager bannerViewPager = new BannerViewPager();
        banners.setAdapter(bannerViewPager);
        banners.setCurrentItem(1);
    }

    /**
     * 设置指示器
     */
    private void setIndicator() {
        for (int k = 0; k < infoList.size(); k++) {
            View vIndicator = new View(context);
            vIndicator.setId(k);
            int size = (int) (mIndicatorSize + 0.5f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.leftMargin = (int) (mIndicatorMargin / 2 + 0.5f);
            params.rightMargin = (int) (mIndicatorMargin / 2 + 0.5f);
            vIndicator.setLayoutParams(params);
            vIndicator.setBackgroundResource(mIndicatorSelecter);
            vIndicator.setEnabled(false);
            indicator.addView(vIndicator);
        }
        indicatorEn = 0;
        indicator.getChildAt(indicatorEn).setEnabled(true);
    }

    /**
     * viewpager绑定指示器
     */
    private void bindingViewPager() {
        banners.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int currentItem;
                if (i == 0) {
                    currentItem = infoList.size() - 1;
                } else if (i == infoList.size() + 1) {
                    currentItem = 0;
                } else {
                    currentItem = i - 1;
                }
                indicator.getChildAt(indicatorEn).setEnabled(false);
                indicator.getChildAt(currentItem).setEnabled(true);
                indicatorEn = currentItem;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE) {//当前item完全显示
                    isAutoPlay = true;
                    int currentItem = banners.getCurrentItem();
                    if (currentItem == 0) {
                        banners.setCurrentItem(infoList.size(), false);
                    } else if (currentItem == infoList.size() + 1) {
                        banners.setCurrentItem(1, false);
                    }
                    if (onItemTouchLisenter != null) {
                        onItemTouchLisenter.onTouchLisenter(currentItem - 1);
                    }
                } else if (i == ViewPager.SCROLL_STATE_DRAGGING) {//正在滑动
                    isAutoPlay = false;
                } else if (i == ViewPager.SCROLL_STATE_SETTLING) {//滑动到最后一个
                    isAutoPlay = true;
                }

            }
        });
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
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            ImageView view = bannerlist.get(position);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentItem;
                    if (position == 0) {
                        currentItem = bannerlist.size() - 2;
                    } else if (position == bannerlist.size() - 1) {
                        currentItem = 0;
                    } else {
                        currentItem = position - 1;
                    }
                    if (onItemClickLisenter != null)
                        onItemClickLisenter.onClickLisenter(currentItem);
                }
            });
            return view;
        }
    }

    /**
     * 点击监听
     */
    public interface OnItemClickLisenter {
        void onClickLisenter(int position);
    }

    /**
     * 滑动监听
     */
    public interface OnItemTouchLisenter {
        void onTouchLisenter(int position);
    }

    /*-----------------------------------------------对外方法----------------------------------------------------*/
    //----------------------------初始化方法------------------------------------

    /**
     * 设置指示器间距
     */
    public SlideShow setIndicatorMargin(float indicatorMargin) {
        this.mIndicatorMargin = (int) (indicatorMargin + 0.5f);
        return this;
    }

    /**
     * 设置指示器大小
     *
     * @param indicatorSize 单位 px
     */
    public SlideShow setIndicatorSize(float indicatorSize) {
        this.mIndicatorSize = (int) (indicatorSize + 0.5f);
        return this;
    }

    /**
     * 设置指示器背景色
     */
    public SlideShow setIndicatorBgColor(int indicatorBgColor) {
        this.indicatorBgColor = indicatorBgColor;
        indicator.setBackgroundColor(indicatorBgColor);
        return this;
    }

    /**
     * 设置指示器区域高度
     *
     * @param indicatorHeight 单位 px
     * @return
     */
    public SlideShow setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = (int) (indicatorHeight + 0.5f);
        ViewGroup.LayoutParams params = indicator.getLayoutParams();
        params.height = mIndicatorHeight;
        indicator.setLayoutParams(params);
        return this;
    }

    /**
     * 设置滚动时间
     */
    public SlideShow setSlideTime(int slideTime) {
        this.slideTime = slideTime;
        return this;
    }

    /**
     * 设置指示器
     *
     * @param indicatorSelecter 状态选择器
     */
    public SlideShow setIndicatorSelecter(int indicatorSelecter) {
        mIndicatorSelecter = indicatorSelecter;
        return this;
    }

    /**
     * 设置轮播图数据
     *
     * @param infoList
     */
    public SlideShow setData(List<String> infoList) {
        this.infoList = infoList;
        return this;
    }

    /**
     * 设置点击监听
     */
    public SlideShow setSlideOnItemClickLisenter(OnItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
        return this;
    }

    /**
     * 设置滑动监听
     */
    public SlideShow setSlideOnItemTouchLisenter(OnItemTouchLisenter onItemTouchLisenter) {
        this.onItemTouchLisenter = onItemTouchLisenter;
        return this;
    }

    /**
     * 设置指示器
     *
     * @param indicator   当前显示状态
     * @param unIndicator 未显示状态
     */
    public SlideShow setIndicatorSelecter(int indicator, int unIndicator) {
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = context.getResources().getDrawable(unIndicator);
        Drawable checked = context.getResources().getDrawable(indicator);
        drawable.addState(new int[]{android.R.attr.checkable}, checked);
        drawable.addState(new int[]{-android.R.attr.checkable}, normal);
        return this;
    }

    /**
     * dp转px
     */
    public int dp2px(float dpValues) {
        dpValues = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValues, context.getResources().getDisplayMetrics());
        return (int) (dpValues + 0.5f);
    }


    public void commit() {
        initBannerImage();
        setIndicator();
        bindingViewPager();
    }
//------------------------------------操作方法--------------------------------------------------

    /**
     * 开始轮播
     */
    public void startSlide() {
        task = new Runnable() {
            @Override
            public void run() {
                if (isAutoPlay) {
                    banners.setCurrentItem(banners.getCurrentItem() + 1);
                }
                if (handler == null)
                    handler = new Handler();
                handler.postDelayed(task, slideTime);
            }
        };
        if (handler1 == null)
            handler1 = new Handler();
        handler1.postDelayed(task, slideTime);
    }

    /**
     * 停止轮播
     */
    public void cancelSlide() {
        handler.removeCallbacks(task);
        handler1.removeCallbacks(task);
        if (task != null)
            task = null;
    }
}
