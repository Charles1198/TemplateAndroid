package com.charles.common.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.charles.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 * @date 2018/11/28
 * @description
 */
public class BannerView extends FrameLayout {
    private Handler handler = new Handler();
    private Context context;
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private int currentIndex = 0;

    private final Runnable replaceImage = new Runnable() {
        @Override
        public void run() {
            currentIndex++;
            viewPager.setCurrentItem(currentIndex);
            handler.postDelayed(replaceImage, 4000);
        }
    };
    private int imageCount = 0;
    private List<ImageView> points = new ArrayList<>();

    private Drawable pointWhite;
    private Drawable pointWhiteTransparent;

    public BannerView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_banner, this);
        viewPager = view.findViewById(R.id.banner_viewPager);
        linearLayout = view.findViewById(R.id.banner_point_layout);

        pointWhite = AppCompatResources.getDrawable(context, R.drawable.ic_point_white_6dp);
        pointWhiteTransparent = AppCompatResources.getDrawable(context, R.drawable.ic_point_white_transparent_6dp);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setImageViews(List<ImageView> list) {
        imageCount = list.size();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 10);
        lp.gravity = Gravity.CENTER_VERTICAL;
        for (int i = 0; i < imageCount; i++) {
            ImageView pointImage = new ImageView(context);
            pointImage.setImageDrawable(pointWhiteTransparent);
            linearLayout.addView(pointImage, lp);
            points.add(pointImage);
        }

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                // 设置count无限大，实现无限轮播
                return Integer.MAX_VALUE;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView imageView = list.get(position % imageCount);
                container.addView(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < imageCount; j++) {
                    points.get(j).setImageDrawable(i % imageCount == j ? pointWhite : pointWhiteTransparent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        handler.post(replaceImage);
    }
}
