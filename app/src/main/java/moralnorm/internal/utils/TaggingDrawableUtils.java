package moralnorm.internal.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;


import com.yuk.miuiHomeR.R;

import moralnorm.internal.graphics.drawable.TaggingDrawable;

public class TaggingDrawableUtils {

    private static final int UNINITIAL = -1;
    private static int mPaddingLarge = -1;
    private static int mPaddingSingle = -1;
    private static int mPaddingSmall = -1;
    private static final int[] STATES_TAGS = {16842915, 16842916, 16842917, 16842918};
    private static final int[] STATE_SET_SINGLE = {16842915};
    private static final int[] STATE_SET_FIRST = {16842916};
    private static final int[] STATE_SET_MIDDLE = {16842917};
    private static final int[] STATE_SET_LAST = {16842918};

    public static void updateItemBackground(View view, int i, int i2) {
        updateBackgroundState(view, i, i2);
        updateItemPadding(view, i, i2);
    }

    public static void updateBackgroundState(View view, int i, int i2) {
        int[] iArr;
        if (view != null && i2 != 0) {
            Drawable background = view.getBackground();
            if ((background instanceof StateListDrawable) && TaggingDrawable.containsTagState((StateListDrawable) background, STATES_TAGS)) {
                TaggingDrawable taggingDrawable = new TaggingDrawable(background);
                view.setBackground(taggingDrawable);
                background = taggingDrawable;
            }
            if (background instanceof TaggingDrawable) {
                TaggingDrawable taggingDrawable2 = (TaggingDrawable) background;
                if (i2 == 1) {
                    iArr = STATE_SET_SINGLE;
                } else if (i == 0) {
                    iArr = STATE_SET_FIRST;
                } else if (i == i2 - 1) {
                    iArr = STATE_SET_LAST;
                } else {
                    iArr = STATE_SET_MIDDLE;
                }
                taggingDrawable2.setTaggingState(iArr);
            }
        }
    }

    public static void updateItemPadding(View view, int position, int count) {
        int paddingBottom;
        int paddingTop;
        if (view != null && count != 0) {
            Context context = view.getContext();
            int paddingStart = view.getPaddingStart();
            view.getPaddingTop();
            int paddingEnd = view.getPaddingEnd();
            view.getPaddingBottom();
            if (count == 1) {
                if (mPaddingSingle == UNINITIAL) {
                    mPaddingSingle = getDimen(context, R.dimen.appcompat_drop_down_menu_padding_single_item);
                }
                paddingTop = mPaddingSingle;
                paddingBottom = mPaddingSingle;
            } else {
                int paddingTop2 = mPaddingSmall;
                if (paddingTop2 == UNINITIAL) {
                    mPaddingSmall = getDimen(context, R.dimen.appcompat_drop_down_menu_padding_small);
                }
                if (mPaddingLarge == UNINITIAL) {
                    mPaddingLarge = getDimen(context, R.dimen.appcompat_drop_down_menu_padding_large);
                }
                if (position == 0) {
                    paddingTop = mPaddingLarge;
                    paddingBottom = mPaddingSmall;
                } else {
                    int paddingTop3 = count + UNINITIAL;
                    if (position == paddingTop3) {
                        paddingTop = mPaddingSmall;
                        paddingBottom = mPaddingLarge;
                    } else {
                        paddingTop = mPaddingSmall;
                        paddingBottom = mPaddingSmall;
                    }
                }
            }
            view.setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom);
        }
    }

    private static int getDimen(Context context, int resDimensId) {
        return context.getResources().getDimensionPixelSize(resDimensId);
    }
}