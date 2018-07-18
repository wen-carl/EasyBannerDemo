package com.seven.easybannerdemo;

import com.seven.easybanner.model.DataModel;

import java.util.ArrayList;
import java.util.List;

class DataHolder {

    public static final List<DataModel> models = new ArrayList<>();
    public static final List<String> styles = new ArrayList<>();
    public static final List<String> gravities = new ArrayList<>();
    public static final List<String> transformers = new ArrayList<>();
    public static final List<String> modes = new ArrayList<>();

    static {
        gravities.add("start");
        gravities.add("center");
        gravities.add("end");

        styles.add("STYLE_IMAGE_INDICATOR");
        styles.add("STYLE_NUM_INDICATOR");
        styles.add("STYLE_TITLE");
        styles.add("STYLE_TITLE_WITH_IMAGE_INDICATOR");
        styles.add("STYLE_TITLE_WITH_NUM_INDICATOR");
        styles.add("STYLE_NONE");

        modes.add("outside");
        modes.add("inside");

        transformers.add("Default");
        transformers.add("Accordion");
        transformers.add("BackgroundToForeground");
        transformers.add("ForegroundToBackground");
        transformers.add("CubeIn");
        transformers.add("CubeOut");
        transformers.add("DepthPage");
        transformers.add("FlipHorizontal");
        transformers.add("FlipVertical");
        transformers.add("RotateDown");
        transformers.add("RotateUp");
        transformers.add("ScaleInOut");
        transformers.add("Stack");
        transformers.add("Tablet");
        transformers.add("ZoomIn");
        transformers.add("ZoomOut");
        transformers.add("ZoomOutSlide");
    }
}
