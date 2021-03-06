package com.seven.easybanner.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seven.easybanner.R;
import com.seven.easybanner.model.DataModel;
import com.seven.easybanner.EasyBanner.BaseAdapter;

import java.util.List;

public final class ImageBannerAdapter<T extends DataModel> extends BaseAdapter<T> {

    private IImageLoader mLoader;
    private ImageView.ScaleType mScaleTape = ImageView.ScaleType.CENTER_CROP;

    public ImageBannerAdapter(@NonNull List<T> mData, IImageLoader loader) {
        super(mData);
        mLoader = loader;
    }

    public void setScaleTape(ImageView.ScaleType scaleTape) {
        mScaleTape = scaleTape;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull ViewGroup parent, int position, int viewType) {
        ImageView iv = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        iv.setScaleType(mScaleTape);
        return iv;
    }

    @Override
    public void onDisplay(@NonNull View view, int position, @NonNull T model) {
        if (null != mLoader) {
            ImageView iv = (ImageView) view;
            mLoader.load(iv, position, model);
        }
    }

    public interface IImageLoader<D extends DataModel> {
        void load(@NonNull ImageView imageView, int position, @NonNull D model);
    }
}
