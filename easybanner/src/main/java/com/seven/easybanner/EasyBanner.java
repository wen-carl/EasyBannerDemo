package com.seven.easybanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seven.easybanner.adapter.ImageBannerAdapter;
import com.seven.easybanner.model.DataModel;
import com.seven.easybanner.transformer.AccordionTransformer;
import com.seven.easybanner.transformer.BackgroundToForegroundTransformer;
import com.seven.easybanner.transformer.CubeInTransformer;
import com.seven.easybanner.transformer.CubeOutTransformer;
import com.seven.easybanner.transformer.DefaultTransformer;
import com.seven.easybanner.transformer.DepthPageTransformer;
import com.seven.easybanner.transformer.FlipHorizontalTransformer;
import com.seven.easybanner.transformer.FlipVerticalTransformer;
import com.seven.easybanner.transformer.ForegroundToBackgroundTransformer;
import com.seven.easybanner.transformer.RotateDownTransformer;
import com.seven.easybanner.transformer.RotateUpTransformer;
import com.seven.easybanner.transformer.ScaleInOutTransformer;
import com.seven.easybanner.transformer.StackTransformer;
import com.seven.easybanner.transformer.TabletTransformer;
import com.seven.easybanner.transformer.ZoomInTransformer;
import com.seven.easybanner.transformer.ZoomOutSlideTransformer;
import com.seven.easybanner.transformer.ZoomOutTranformer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EasyBanner extends FrameLayout implements OnPageChangeListener {

    private static final String TAG = EasyBanner.class.getSimpleName();

    public static class DefaultConfig {
        public static final int AUTO_PLAY = R.bool.auto_play;
        public static final int TIME_INTERVAL = R.integer.time_interval;

        @LayoutRes
        public static final int INDICATOR_LAYOUT = R.layout.layout_default_indicator;

        @Direction
        public static final int DIRECTION = EasyBanner.DIRECTION_POSITIVE;

        @IndicatorStyle
        public static final int INDICATOR_STYLE = EasyBanner.STYLE_IMAGE_INDICATOR;

        @IndicatorMode
        public static final int INDICATOR_MODE = EasyBanner.INDICATOR_MODE_OUTSIDE;

        @IndicatorGravity
        public static final int INDICATOR_GRAVITY = EasyBanner.GRAVITY_CENTER;

        @IndicatorGravity
        public static final int TITLE_GRAVITY = EasyBanner.GRAVITY_START;

        public static final int INDICATOR_BACKGROUND = R.color.translucent;
        public static final int IMAGE_INDICATOR_STATE_BACKGROUND = R.drawable.image_indicator_background;
        public static final int IMAGE_INDICATOR_MARGIN_VERTICAL = R.dimen.dp4;
        public static final int IMAGE_INDICATOR_MARGIN_HORIZONTAL = R.dimen.dp4;
        public static final int NUM_INDICATOR_BACKGROUND = R.drawable.num_indicator_background;
        public static final int NUM_INDICATOR_TEXT_COLOR = R.color.white_ee;
        public static final int NUM_INDICATOR_TEXT_SIZE = R.dimen.sp18;
        public static final int TITLE_BACKGROUND = R.color.gray_30;
        public static final int TITLE_TEXT_SIZE = R.dimen.sp14;
        public static final int TITLE_TEXT_COLOR = R.color.black;
        public static final int TITLE_TEXT_LINE = R.integer.line_count;
    }

    /**
     * Banner mStatus
     */
    public static final int STATUS_NOT_START = 0;
    public static final int STATUS_AUTO_PLAYING = 1;
    public static final int STATUS_MANUAL_LAYING = 2;
    public static final int STATUS_PAUSED = 3;
    public static final int STATUS_STOPPED = 4;

    @IntDef({STATUS_NOT_START, STATUS_AUTO_PLAYING, STATUS_MANUAL_LAYING, STATUS_PAUSED, STATUS_STOPPED})
    @Retention(RetentionPolicy.SOURCE)
    private @interface BannerStatus {}

    /**
     * Scroll mDirection
     */
    public static final int DIRECTION_POSITIVE = 0;
    public static final int DIRECTION_NEGATIVE = 1;

    @IntDef({DIRECTION_POSITIVE, DIRECTION_NEGATIVE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Direction {}

    /**
     * Indicator Gravity
     */
    // Gravity.START | Gravity.CENTER_VERTICAL
    public static final int GRAVITY_START = 8388627;
    // Gravity.CENTER
    public static final int GRAVITY_CENTER = 17;
    // Gravity.END| Gravity.CENTER_VERTICAL
    public static final int GRAVITY_END = 8388629;

    @IntDef({GRAVITY_START, GRAVITY_CENTER, GRAVITY_END})
    @Retention(RetentionPolicy.SOURCE)
    private @interface IndicatorGravity {}

    /**
     * Indicator Mode
     */
    public static final int INDICATOR_MODE_INSIDE = 0;
    public static final int INDICATOR_MODE_OUTSIDE = 1;

    @IntDef({INDICATOR_MODE_INSIDE, INDICATOR_MODE_OUTSIDE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface IndicatorMode {}

    /**
     * Image banner style
     */
    public static final int STYLE_NONE = 0;
    public static final int STYLE_IMAGE_INDICATOR = 1;
    public static final int STYLE_NUM_INDICATOR = 2;
    public static final int STYLE_TITLE = 3;
    public static final int STYLE_TITLE_WITH_IMAGE_INDICATOR = 4;
    public static final int STYLE_TITLE_WITH_NUM_INDICATOR = 5;

    @IntDef({STYLE_NONE, STYLE_IMAGE_INDICATOR, STYLE_NUM_INDICATOR, STYLE_TITLE, STYLE_TITLE_WITH_IMAGE_INDICATOR, STYLE_TITLE_WITH_NUM_INDICATOR})
    @Retention(RetentionPolicy.SOURCE)
    private @interface IndicatorStyle {}

    private ViewPager mViewPager;
    private BaseAdapter mAdapter;
    private @BannerStatus int mStatus = STATUS_NOT_START;
    private boolean isAutoPlay;
    private int mTimeInterval;
    private @LayoutRes int mIndicatorLayoutId;
    private @Direction int mDirection;
    private @IndicatorStyle int mIndicatorStyle;
    private @IndicatorMode int mIndicatorMode;
    private @IndicatorGravity int mIndicatorGravity;
    private @IndicatorGravity int mTitleGravity;
    private @ColorRes int mIndicatorBackgroundId;
    private @DrawableRes int mImageIndicatorStateBackgroundId;
    private int mImageIndicatorWidth;
    private int mImageIndicatorHeight;
    private int mImageIndicatorMarginVertical;
    private int mImageIndicatorMarginHorizontal;
    private @DrawableRes int mNumIndicatorBackgroundId;
    private @ColorRes int mNumIndicatorTextColorId;
    private float mNumIndicatorTextSize;
    private @ColorRes int mTitleBackgroundId;
    private float mTitleTextSize;
    private @ColorRes int mTitleTextColorId;
    private int mTitleTextLine;
    private ImageView.ScaleType mScaleType;

    private static final int BASE_INDICATOR_ID = 1000;
    private View mIndicatorContainer;
    private LinearLayout mIndicatorView;
    private LinearLayout mImageIndicator;
    private TextView mNumIndicator;
    private TextView mTitleIndicator;
    private boolean isIndicatorDefault = true;
    private int mCurrentIndex = 1;

    private List<OnPageChangeListener> mOnPageChangeListeners;

    private static final Handler mHandler = new Handler();
    private final Runnable mLoopRunnable = new Runnable() {
        @Override
        public void run() {
            if (mAdapter.getData().size() > 1 && isAutoPlay) {
                if (DIRECTION_POSITIVE == mDirection) {
                    showNext();
                } else {
                    showPrevious();
                }

                mHandler.postDelayed(mLoopRunnable, mTimeInterval);
            }
        }
    };

    public EasyBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributeSet(attrs);

        ViewGroup mainContent = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_easy_banner, this, true);
        mViewPager = mainContent.findViewById(R.id.banner_view_pager);
        mViewPager.addOnPageChangeListener(this);

        ConstraintLayout superLayout = findViewById(R.id.mainContent);
        mIndicatorContainer = LayoutInflater.from(getContext()).inflate(mIndicatorLayoutId, superLayout, false);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(mIndicatorContainer.getLayoutParams());
        layoutParams.startToStart = R.id.mainContent;
        layoutParams.endToEnd = R.id.mainContent;
        layoutParams.bottomToBottom = R.id.mainContent;
        mIndicatorContainer.setLayoutParams(layoutParams);
        superLayout.addView(mIndicatorContainer);

        if (isIndicatorDefault) {
            mIndicatorView = mIndicatorContainer.findViewById(R.id.layout_indicator);
            mImageIndicator = mIndicatorContainer.findViewById(R.id.layout_image_indicator);
            mNumIndicator = mIndicatorContainer.findViewById(R.id.txt_num_indicator);
            mTitleIndicator = mIndicatorContainer.findViewById(R.id.txt_title);
        }
    }

    public EasyBanner(@NonNull Context context) {
        this(context, null);
    }

    public EasyBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void initView() {
        initIndicatorView();
        updateIndicatorByMode();
        updateIndicatorByStyle();
        if (mAdapter instanceof ImageBannerAdapter) {
            ((ImageBannerAdapter) mAdapter).setScaleTape(mScaleType);
        }
        show(mCurrentIndex);
    }

    private void initIndicatorView() {
        if (!isIndicatorDefault)
            return;

        mIndicatorView.setBackgroundColor(getResources().getColor(mIndicatorBackgroundId));
        mIndicatorView.setGravity(mIndicatorGravity);
        mNumIndicator.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumIndicatorTextSize);
        mNumIndicator.setTextColor(getResources().getColor(mNumIndicatorTextColorId));
        mNumIndicator.setBackgroundResource(mNumIndicatorBackgroundId);

        mTitleIndicator.setBackgroundColor(getResources().getColor(mTitleBackgroundId));
        mTitleIndicator.setTextColor(getResources().getColor(mTitleTextColorId));
        mTitleIndicator.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTitleIndicator.setLines(mTitleTextLine);
        mTitleIndicator.setGravity(mTitleGravity);

        createImageIndicator();
    }
    
    private void handleAttributeSet(AttributeSet attrs) {
        if (null == attrs)
            return;
        
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyBanner);
        isAutoPlay = typedArray.getBoolean(R.styleable.EasyBanner_auto_play, getResources().getBoolean(DefaultConfig.AUTO_PLAY));
        mTimeInterval = typedArray.getInt(R.styleable.EasyBanner_time_interval, getResources().getInteger(DefaultConfig.TIME_INTERVAL));
        mDirection = typedArray.getInt(R.styleable.EasyBanner_direction, DefaultConfig.DIRECTION);
        mIndicatorLayoutId = typedArray.getResourceId(R.styleable.EasyBanner_indicator_layout, DefaultConfig.INDICATOR_LAYOUT);
        mIndicatorStyle = typedArray.getInt(R.styleable.EasyBanner_indicator_style, DefaultConfig.INDICATOR_STYLE);
        mIndicatorMode = typedArray.getInt(R.styleable.EasyBanner_indicator_mode, DefaultConfig.INDICATOR_MODE);
        mIndicatorGravity = typedArray.getInt(R.styleable.EasyBanner_indicator_gravity, DefaultConfig.INDICATOR_GRAVITY);
        mTitleGravity = typedArray.getInt(R.styleable.EasyBanner_title_gravity, DefaultConfig.TITLE_GRAVITY);
        mIndicatorBackgroundId = typedArray.getResourceId(R.styleable.EasyBanner_indicator_background, DefaultConfig.INDICATOR_BACKGROUND);
        mImageIndicatorStateBackgroundId = typedArray.getResourceId(R.styleable.EasyBanner_indicator_image_state_background, DefaultConfig.IMAGE_INDICATOR_STATE_BACKGROUND);

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int indicatorSize = dm.widthPixels / 80;
        mImageIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicator_image_width, indicatorSize);
        mImageIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicator_image_height, indicatorSize);
        mImageIndicatorMarginHorizontal = typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicator_image_margin_horizontal, getResources().getDimensionPixelSize(DefaultConfig.IMAGE_INDICATOR_MARGIN_HORIZONTAL));
        mImageIndicatorMarginVertical = typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicator_image_margin_vertical, getResources().getDimensionPixelSize(DefaultConfig.IMAGE_INDICATOR_MARGIN_VERTICAL));
        mNumIndicatorTextColorId = typedArray.getResourceId(R.styleable.EasyBanner_indicator_num_text_color, DefaultConfig.NUM_INDICATOR_TEXT_COLOR);
        mNumIndicatorBackgroundId = typedArray.getResourceId(R.styleable.EasyBanner_indicator_num_background, DefaultConfig.NUM_INDICATOR_BACKGROUND);
        mNumIndicatorTextSize = typedArray.getDimensionPixelSize(R.styleable.EasyBanner_indicator_num_text_size, getResources().getDimensionPixelSize(DefaultConfig.NUM_INDICATOR_TEXT_SIZE));
        mTitleBackgroundId = typedArray.getResourceId(R.styleable.EasyBanner_title_background, DefaultConfig.TITLE_BACKGROUND);
        mTitleTextColorId = typedArray.getResourceId(R.styleable.EasyBanner_title_text_color, DefaultConfig.TITLE_TEXT_COLOR);
        mTitleTextSize = typedArray.getDimensionPixelSize(R.styleable.EasyBanner_title_text_size, getResources().getDimensionPixelSize(DefaultConfig.TITLE_TEXT_SIZE));
        mTitleTextLine = typedArray.getInt(R.styleable.EasyBanner_title_text_line, getResources().getInteger(DefaultConfig.TITLE_TEXT_LINE));
        int scaleType = typedArray.getInt(R.styleable.EasyBanner_image_scale_type, 6);
        switch (scaleType) {
            case 0:
                mScaleType = ImageView.ScaleType.MATRIX;
                break;
            case 1:
                mScaleType = ImageView.ScaleType.FIT_XY;
                break;
            case 2:
                mScaleType = ImageView.ScaleType.FIT_START;
                break;
            case 3:
                mScaleType = ImageView.ScaleType.FIT_CENTER;
                break;
            case 4:
                mScaleType = ImageView.ScaleType.FIT_END;
                break;
            case 5:
                mScaleType = ImageView.ScaleType.CENTER;
                break;
            case 6:
                mScaleType = ImageView.ScaleType.CENTER_CROP;
                break;
            case 7:
                mScaleType = ImageView.ScaleType.CENTER_INSIDE;
                break;
            default:
                mScaleType = ImageView.ScaleType.CENTER_CROP;
                break;
        }
        isIndicatorDefault = mIndicatorLayoutId == DefaultConfig.INDICATOR_LAYOUT;

        typedArray.recycle();
    }

    private EasyBanner addOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        if (null == mOnPageChangeListeners) {
            mOnPageChangeListeners = new ArrayList<>();
        }

        mOnPageChangeListeners.add(listener);

        return this;
    }

    private boolean removeOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        if (null != mOnPageChangeListeners && mOnPageChangeListeners.contains(listener)) {
            return mOnPageChangeListeners.remove(listener);
        }

        return false;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    public EasyBanner setAdapter(@NonNull BaseAdapter adapter) {
        mAdapter = adapter;
        mViewPager.setAdapter(adapter);

        return this;
    }

    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    @NonNull
    public final EasyBanner setAutoPlay(boolean enable) {
        switch (mStatus) {
            case STATUS_AUTO_PLAYING:
                if (enable != isAutoPlay) {
                    if (enable) {
                        mHandler.postDelayed(mLoopRunnable, mTimeInterval);
                    } else {
                        mHandler.removeCallbacks(mLoopRunnable);
                    }

                    mStatus = enable ? STATUS_AUTO_PLAYING : STATUS_MANUAL_LAYING;
                }
            case STATUS_NOT_START:
                isAutoPlay = enable;
                break;
            case STATUS_PAUSED:
            case STATUS_STOPPED:
            case STATUS_MANUAL_LAYING:
            default:
                break;
        }

        return this;
    }

    public long getTimeInterval() {
        return this.mTimeInterval;
    }

    @NonNull
    public final EasyBanner setTimeInterval(int interval) {
        mTimeInterval = interval;
        return this;
    }

    @Direction
    public int getDirection() {
        return mDirection;
    }

    public EasyBanner setDirection(@Direction int direction) {
        mDirection = direction;
        return this;
    }

    @IndicatorStyle
    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public EasyBanner setIndicatorStyle(@IndicatorStyle int style) {
        mIndicatorStyle = style;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            updateIndicatorByStyle();
        }

        return this;
    }

    @IndicatorMode
    public int getIndicatorMode() {
        return mIndicatorMode;
    }

    public EasyBanner setIndicatorMode(@IndicatorMode int indicatorMode) {
        mIndicatorMode = indicatorMode;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            updateIndicatorByMode();
        }

        return this;
    }

    public EasyBanner setIndicatorGravity(@IndicatorGravity int gravity) {
        mIndicatorGravity = gravity;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mIndicatorView.setGravity(mIndicatorGravity);
        }

        return this;
    }

    public EasyBanner setTitleGravity(@IndicatorGravity int gravity) {
        mTitleGravity = gravity;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mTitleIndicator.setGravity(mTitleGravity);
        }

        return this;
    }

    public EasyBanner setIndicatorBackgroundColorResource(@ColorRes int id) {
        mIndicatorBackgroundId = id;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mIndicatorView.setBackgroundColor(getResources().getColor(mIndicatorBackgroundId));
        }

        return this;
    }

    public EasyBanner setImageIndicatorStateBackgroundResource(@DrawableRes int id) {
        mImageIndicatorStateBackgroundId = id;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            for (int i = 0; i < mImageIndicator.getChildCount(); i ++) {
                ImageView iv = (ImageView) mImageIndicator.getChildAt(i);
                if (null != iv) {
                    iv.setImageResource(mImageIndicatorStateBackgroundId);
                }
            }
        }

        return this;
    }

    public EasyBanner setImageIndicatorSize(int width, int height) {
        mImageIndicatorWidth = width >= 0 ? width : mImageIndicatorWidth;
        mImageIndicatorHeight = height >= 0 ? height : mImageIndicatorHeight;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            View view = mImageIndicator.getChildAt(0);
            if (null != view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
                params.width = mImageIndicatorWidth;
                params.height = mImageIndicatorHeight;

                for (int i = 0; i < mImageIndicator.getChildCount(); i++) {
                    ImageView iv = (ImageView) mImageIndicator.getChildAt(i);
                    if (null != iv) {
                        iv.setLayoutParams(params);
                    }
                }
            }
        }

        return this;
    }

    public EasyBanner setImageIndicatorMargin(int marginHorizontal, int marginVertical) {
        mImageIndicatorMarginHorizontal = Math.max(0, marginHorizontal);
        mImageIndicatorMarginVertical = Math.max(0, marginVertical);
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            View view = mImageIndicator.getChildAt(0);
            if (null != view) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getLayoutParams());
                params.setMargins(mImageIndicatorMarginHorizontal, mImageIndicatorMarginVertical, mImageIndicatorMarginHorizontal, mImageIndicatorMarginVertical);

                for (int i = 0; i < mImageIndicator.getChildCount(); i++) {
                    ImageView iv = (ImageView) mImageIndicator.getChildAt(i);
                    if (null != iv) {
                        iv.setLayoutParams(params);
                    }
                }
            }
        }

        return this;
    }

    public EasyBanner setNumIndicatorBackgroundResource(@DrawableRes int id) {
        mNumIndicatorBackgroundId = id;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mNumIndicator.setBackgroundResource(mNumIndicatorBackgroundId);
        }

        return this;
    }

    public EasyBanner setNumIndicatorTextColorResource(@ColorRes int id) {
        mNumIndicatorTextColorId = id;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mNumIndicator.setTextColor(getResources().getColor(mNumIndicatorTextColorId));
        }

        return this;
    }

    public EasyBanner setNumIndicatorTextSize(int size) {
        mNumIndicatorTextSize = Math.max(0, size);
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mNumIndicator.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNumIndicatorTextSize);
        }

        return this;
    }

    public EasyBanner setTitleBackgroundResource(@DrawableRes int id) {
        mTitleBackgroundId = id;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mTitleIndicator.setBackgroundResource(mTitleBackgroundId);
        }

        return this;
    }

    public EasyBanner setTitleTextColorResource(@ColorRes int id) {
        mTitleTextColorId = id;
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mTitleIndicator.setTextColor(getResources().getColor(mTitleTextColorId));
        }

        return this;
    }

    public EasyBanner setTitleTextSize(int size) {
        mTitleTextSize = Math.max(0, size);
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mTitleIndicator.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        }

        return this;
    }

    public EasyBanner setTitleTextLineCount(int count) {
        mTitleTextLine = Math.max(1, count);
        if (isIndicatorDefault && STATUS_NOT_START != mStatus) {
            mTitleIndicator.setLines(mTitleTextLine);
        }

        return this;
    }

    public EasyBanner setImageScaleType(ImageView.ScaleType type) {
        if (isIndicatorDefault && STATUS_NOT_START != mStatus && mScaleType != type) {
            if (mAdapter instanceof ImageBannerAdapter) {
                ((ImageBannerAdapter) mAdapter).setScaleTape(type);
                mAdapter.notifyDataSetChanged();
            }
        }

        mScaleType = type;

        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public EasyBanner setPageTransformer(Class<? extends PageTransformer> transformer) {
        try {
            PageTransformer former = transformer.newInstance();
            setPageTransformer(former);
        } catch (InstantiationException | IllegalAccessException e) {
            Log.e(TAG, "setPageTransformer: " + e.getLocalizedMessage(), e);
        }

        return this;
    }

    public EasyBanner setPageTransformer(PageTransformer transformer) {
        mViewPager.setPageTransformer(true, transformer);
        return setPageTransformer(true, transformer);
    }

    public EasyBanner setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        mViewPager.setPageTransformer(reverseDrawingOrder, transformer);

        return this;
    }

    public EasyBanner setOnBannerItemClickListener(OnBannerItemClickListener listener) {
        if (null != mAdapter) {
            mAdapter.setOnBannerItemClickListener(listener);
        } else {
            throw new IllegalStateException("setOnBannerItemClickListener must called after setAdapter()");
        }

        return this;
    }

    @BannerStatus
    public int getStatus() {
        return mStatus;
    }

    public int getCurrentItem() {
        return mAdapter.getRealPosition(mCurrentIndex);
    }

    public EasyBanner setCurrentItem(int index) {
        int mockIndex = index + 1;
        if (index <= 0) {
            mockIndex = 1;
        } else if (index < mAdapter.getData().size()) {
            mockIndex = index + 1;
        } else {
            mockIndex = mAdapter.getData().size();
        }
        mViewPager.setCurrentItem(mockIndex);

        return this;
    }
    
    public EasyBanner setImageIndicatorGravity(@IndicatorGravity int gravity) {
        
        if (null != mImageIndicator) {
            mImageIndicator.setGravity(gravity);
        }
        
        return this;
    }

    public EasyBanner setTitleIndicatorGravity(@IndicatorGravity int gravity) {
        
        if (null != mTitleIndicator) {
            mTitleIndicator.setGravity(gravity);
        }
        
        return this;
    }

    public TextView getNumIndicator() {
        return mNumIndicator;
    }

    public TextView getTitleIndicator() {
        return mTitleIndicator;
    }

    private void createImageIndicator() {
        if (null == mImageIndicator) {
            return;
        }

        mImageIndicator.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mImageIndicatorWidth, mImageIndicatorHeight);
        params.setMargins(mImageIndicatorMarginHorizontal, mImageIndicatorMarginVertical, mImageIndicatorMarginHorizontal, mImageIndicatorMarginVertical);

        for (int i = 0; i < mAdapter.getData().size(); i ++) {
            ImageView iv = new ImageView(getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setLayoutParams(params);
            iv.setImageResource(mImageIndicatorStateBackgroundId);
            iv.setSelected(false);
            iv.setId(BASE_INDICATOR_ID + i);
            mImageIndicator.addView(iv);
        }
    }

    private void updateIndicatorByMode() {
        if (null == mIndicatorView && null != mTitleIndicator)
            return;

        switch (mIndicatorMode) {
            case INDICATOR_MODE_INSIDE: {
                mIndicatorView.setBackgroundColor(getResources().getColor(mTitleBackgroundId));

                int indicatorHeight = mIndicatorView.getHeight();
                int titleHeight = mTitleIndicator.getHeight();
                int height = Math.max(indicatorHeight, titleHeight);

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
                params.startToStart = -1;
                params.endToEnd = R.id.indicator_container;
                params.topToTop = R.id.indicator_container;
                params.bottomToBottom = R.id.indicator_container;
                mIndicatorView.setLayoutParams(params);

                ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(mTitleIndicator.getLayoutParams());
                titleParams.height = height;
                titleParams.startToStart = R.id.indicator_container;
                titleParams.endToStart = R.id.layout_indicator;
                titleParams.bottomToBottom = R.id.indicator_container;
                mTitleIndicator.setLayoutParams(titleParams);
            }
                break;
            case INDICATOR_MODE_OUTSIDE:
            default: {
                mIndicatorView.setBackgroundColor(getResources().getColor(mIndicatorBackgroundId));

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.startToStart = R.id.indicator_container;
                params.endToEnd = R.id.indicator_container;
                params.topToTop = R.id.indicator_container;
                params.bottomToTop = R.id.txt_title;
                mIndicatorView.setLayoutParams(params);

                ConstraintLayout.LayoutParams titleParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                titleParams.startToStart = R.id.indicator_container;
                titleParams.endToEnd = R.id.indicator_container;
                titleParams.bottomToBottom = R.id.indicator_container;
                mTitleIndicator.setLayoutParams(titleParams);
            }
                break;
        }
    }
    
    private void updateIndicatorByStyle() {
        if (null == mImageIndicator || null == mNumIndicator || null == mTitleIndicator) {
            return;
        }
        
        mImageIndicator.setVisibility(GONE);
        mNumIndicator.setVisibility(GONE);
        mTitleIndicator.setVisibility(GONE);
        updateIndicatorIndex(mCurrentIndex);

        switch (mIndicatorStyle) {
            case STYLE_IMAGE_INDICATOR:
                mImageIndicator.setVisibility(VISIBLE);
                break;
            case STYLE_NUM_INDICATOR:
                mNumIndicator.setVisibility(VISIBLE);
                break;
            case STYLE_TITLE:
                mTitleIndicator.setVisibility(VISIBLE);
                break;
            case STYLE_TITLE_WITH_IMAGE_INDICATOR:
                mImageIndicator.setVisibility(VISIBLE);
                mTitleIndicator.setVisibility(VISIBLE);
                break;
            case STYLE_TITLE_WITH_NUM_INDICATOR:
                mNumIndicator.setVisibility(VISIBLE);
                mTitleIndicator.setVisibility(VISIBLE);
                break;
            case STYLE_NONE:
            default:
                break;
        }
    }

    private void updateIndicatorIndex(int position) {
        int realPos = mAdapter.getRealPosition(position);
        if (isIndicatorDefault) {
            switch (mIndicatorStyle) {
                case STYLE_TITLE_WITH_IMAGE_INDICATOR:
                    updateTitleIndicator(realPos);
                case STYLE_IMAGE_INDICATOR:
                    updateCircleIndicator(realPos);
                    break;
                case STYLE_TITLE_WITH_NUM_INDICATOR:
                    updateTitleIndicator(realPos);
                case STYLE_NUM_INDICATOR:
                    updateNumIndicator(realPos);
                    break;
                case STYLE_TITLE:
                    updateTitleIndicator(realPos);
                    break;
                case STYLE_NONE:
                default:
                    break;
            }
        } else {
            mAdapter.bindIndicator(mIndicatorContainer, realPos);
        }
        
        mCurrentIndex = position;
    }
    
    private void updateCircleIndicator(int realPos) {
        if (null != mImageIndicator) {
            ImageView cancel = mImageIndicator.findViewById(BASE_INDICATOR_ID + mAdapter.getRealPosition(mCurrentIndex));
            if (null != cancel) {
                cancel.setSelected(false);
            }
        
            ImageView select = mImageIndicator.findViewById(BASE_INDICATOR_ID + realPos);
            if (null != select) {
                select.setSelected(true);
            }
        }
    }
    
    private void updateNumIndicator(int realPos) {
        if (null != mNumIndicator) {
            mNumIndicator.setText(String.format("%d/%d", realPos + 1, mAdapter.getData().size()));
        }
    }
    
    private void updateTitleIndicator(int realPos) {
        if (null != mTitleIndicator) {
            DataModel model = (DataModel) mAdapter.getData().get(realPos);
            mTitleIndicator.setText(model.getDescription());
        }
    }

    public final void start() {
        boolean shouldStart = isAutoPlay;
        switch(mStatus) {
            case STATUS_NOT_START:
                initView();
                mStatus = isAutoPlay ? STATUS_AUTO_PLAYING : STATUS_MANUAL_LAYING;
                break;
            case STATUS_PAUSED:
            case STATUS_STOPPED:
                if (isAutoPlay) {
                    mStatus = STATUS_AUTO_PLAYING;
                }
            case STATUS_AUTO_PLAYING:
                shouldStart &= true;
                break;
            case STATUS_MANUAL_LAYING:
            default:
                shouldStart = false;
        }

        if (shouldStart) {
            mHandler.removeCallbacks(mLoopRunnable);
            mHandler.postDelayed(mLoopRunnable, mTimeInterval);
        }
    }

    public final void stop() {
        this.mStatus = STATUS_STOPPED;
        mHandler.removeCallbacks(mLoopRunnable);
        
        if (mCurrentIndex != 1) {
            int position;
            switch (this.mDirection) {
                case DIRECTION_POSITIVE:
                    position = 0;
                    break;
                case DIRECTION_NEGATIVE:
                default:
                    position = 2;
            }

            mViewPager.setCurrentItem(position, false);
            show(1);
        }
    }

    public void pause() {
        this.mStatus = STATUS_PAUSED;
        mHandler.removeCallbacks(mLoopRunnable);
    }

    public void showPrevious() {
        int index;
        if (mCurrentIndex <= 0) {
            index = mAdapter.getData().size();
        } else {
            index = mCurrentIndex - 1;
        }
        show(index);
    }

    public void showNext() {
        int index;
        if (mCurrentIndex >= mAdapter.getData().size() + 1) {
            index = 1;
        } else {
            index = mCurrentIndex + 1;
        }
        show(index);
    }

    public void show(int index) {
        int temp;
        if (index <= 0) {
            temp = 0;
        } else if (index <= mAdapter.getData().size() + 1) {
            temp = index;
        } else {
            temp = mAdapter.getData().size() + 1;
        }
        mViewPager.setCurrentItem(temp);
    }

    public void onPageScrollStateChanged(int state) {
        switch(state) {
            case ViewPager.SCROLL_STATE_IDLE:
            case ViewPager.SCROLL_STATE_DRAGGING:
                if (mCurrentIndex == 0) {
                    mViewPager.setCurrentItem(mAdapter.getData().size(), false);
                } else if (mCurrentIndex == mAdapter.getData().size() + 1) {
                    mViewPager.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
            default:
                break;
        }

        if (mCurrentIndex != 0 && mCurrentIndex != mAdapter.getData().size() + 1) {
            if (null != mOnPageChangeListeners) {
                for (OnPageChangeListener listener : mOnPageChangeListeners) {
                    listener.onPageScrollStateChanged(state);
                }
            }
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mCurrentIndex != 0 && mCurrentIndex != mAdapter.getData().size() + 1) {
            if (null != mOnPageChangeListeners) {
                int realPosition = mAdapter.getRealPosition(position);
                for (OnPageChangeListener listener : mOnPageChangeListeners) {
                    listener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
                }
            }
        }
    }

    public void onPageSelected(int position) {
        updateIndicatorIndex(position);
        if (mCurrentIndex != 0 && mCurrentIndex != mAdapter.getData().size() + 1) {
            if (null != mOnPageChangeListeners) {
                int realPosition = mAdapter.getRealPosition(position);
                for (OnPageChangeListener listener : mOnPageChangeListeners) {
                    listener.onPageSelected(realPosition);
                }
            }
        }
    }

    /**
     * 事件分发，手指按在banner上时停止自动滚动
     * @param ev
     * @return
     */
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE
                ) {
            if (isAutoPlay && mStatus == STATUS_AUTO_PLAYING) {
                mHandler.postDelayed(mLoopRunnable, mTimeInterval);
            }
        } else if (action == MotionEvent.ACTION_DOWN) {
            mHandler.removeCallbacks(mLoopRunnable);
        }

        return super.dispatchTouchEvent(ev);
    }

    public static abstract class BaseAdapter<T extends DataModel> extends PagerAdapter {

        private static final int TAG_KEY = -100;
        private static final int DEFAULT_TYPE = -1;
        private static final int KEY_UNUSED = -1;

        private List<T> mData = new ArrayList<>();

        private List<T> mMockData;

        private HashMap<Number, HashMap<Number, View>> mCachedViewHolders = new HashMap<>();
        private HashMap<Number, View> mShowingViewHolders = new HashMap<>();

        private OnBannerItemClickListener mOnClickListener;
        private OnBannerItemLongClickListener mOnLongClickListener;

        public BaseAdapter(@NonNull List<T> mData) {
            bindData(mData);
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            int type = getViewType(position);
            HashMap<Number, View> tempMap = mCachedViewHolders.get(type);

            View view = null;
            if (null != tempMap) {
                view = tempMap.get(KEY_UNUSED);
                tempMap.remove(KEY_UNUSED);
            }

            if (null == view) {
                view = onCreateView(container, getRealPosition(position), type);
            }

            container.addView(view);
            onDisplay(view, getRealPosition(position), mMockData.get(position));

			mShowingViewHolders.put(position, view);
            view.setTag(TAG_KEY, getRealPosition(position));
            setClickListener(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
            mShowingViewHolders.remove(position);
            int type = getViewType(position);
            HashMap<Number, View> tempMap = mCachedViewHolders.get(type);
            if (null == tempMap) {
                tempMap = new HashMap<>();
                mCachedViewHolders.put(type, tempMap);
            }
            tempMap.put(KEY_UNUSED, view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mMockData.size();
        }

        public int getViewType(int position) {
            return DEFAULT_TYPE;
        }

        @NonNull
        abstract public View onCreateView(@NonNull ViewGroup parent, int position, int viewType);

        abstract public void onDisplay(@NonNull View view, int position, @NonNull T model);

        public final void bindIndicator(@NonNull View view, int position) {
            onBindIndicator(view, position, mData.get(position));
        }

        public void onBindIndicator(@NonNull View view, int position, T model) { }

        public void setOnBannerItemClickListener(OnBannerItemClickListener listener) {
            mOnClickListener = listener;
        }

        public void setOnBannerItemLongClickListener(OnBannerItemLongClickListener listener) {
            mOnLongClickListener = listener;
        }

        private void setClickListener(final View view) {
            if (!view.hasOnClickListeners()) {
                if (null != mOnClickListener) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int position = (int)view.getTag(TAG_KEY);
                            mOnClickListener.onBannerClicked(v, position, mData.get(position));
                        }
                    });
                }
            }

            // TODO: 2018/7/9  Need to fix
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != mOnLongClickListener) {
                        int position = (int)view.getTag();
                        mOnClickListener.onBannerClicked(v, position, mData.get(position));

                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

        public int getRealPosition(int position) {
            int real = 0;
            if (mData.size() <= 1) {
                real = 0;
            } else {
                real = (position - 1) % mData.size();
                if (real < 0) {
                    real += mData.size();
                }
            }

            return real;
        }

        private void updateMockedData() {
            mMockData = new ArrayList<>(mData);
            if (mData.size() > 1) {
                T first = mData.get(0);
                T last = mData.get(mData.size() - 1);

                mMockData.add(0, last);
                mMockData.add(first);
            }

            notifyDataSetChanged();
        }

        public void bindData(@NonNull List<T> mData) {
            this.mData = mData;
            updateMockedData();
        }

        public List<T> getData() {
            return mData;
        }
    }

    public interface OnBannerItemClickListener {
        void onBannerClicked(@NonNull View view, int  position, @NonNull DataModel model);
    }

    public interface OnBannerItemLongClickListener {
        void onBannerLongClicked(View view, int  position, DataModel model);
    }

    public static class Transformer {
        public static final Class<? extends PageTransformer> Default = DefaultTransformer.class;
        public static final Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
        public static final Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
        public static final Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
        public static final Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
        public static final Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
        public static final Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
        public static final Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
        public static final Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
        public static final Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
        public static final Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
        public static final Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
        public static final Class<? extends PageTransformer> Stack = StackTransformer.class;
        public static final Class<? extends PageTransformer> Tablet = TabletTransformer.class;
        public static final Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
        public static final Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
        public static final Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
    }
}