package moralnorm.preference.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.widget.NestedScrollView;

import moralnorm.internal.utils.DisplayUtils;
import moralnorm.preference.R;
import moralnorm.preference.drawable.AlphaPatternDrawable;

public class ColorPickerView extends NestedScrollView {
    private static final int ALPHA_PANEL_HEIGH_DP = 20;
    private static final int BORDER_WIDTH_PX = 1;
    private static final int CIRCLE_TRACKER_RADIUS_DP = 5;
    private static final int HUE_PANEL_WDITH_DP = 30;
    private static final int PANEL_SPACING_DP = 10;
    private static final int SLIDER_TRACKER_OFFSET_DP = 2;
    private static final int SLIDER_TRACKER_SIZE_DP = 4;
    private static int mDefaultBorderColor;
    private static int mDefaultSliderColor;
    private int alpha;
    private Paint alphaPaint;
    private int alphaPanelHeightPx;
    private AlphaPatternDrawable alphaPatternDrawable;
    private Rect alphaRect;
    private Shader alphaShader;
    private String alphaSliderText;
    private Paint alphaTextPaint;
    private int borderColor;
    private Paint borderPaint;
    private int circleTrackerRadiusPx;
    private Rect drawingRect;
    private float hue;
    private Paint hueAlphaTrackerPaint;
    private BitmapCache hueBackgroundCache;
    private int huePanelWidthPx;
    private Rect hueRect;
    private int mRequiredPadding;
    private OnColorChangedListener onColorChangedListener;
    private int panelSpacingPx;
    private float sat;
    private Shader satShader;
    private BitmapCache satValBackgroundCache;
    private Paint satValPaint;
    private Rect satValRect;
    private Paint satValTrackerPaint;
    private boolean showAlphaPanel;
    private int sliderTrackerColor;
    private int sliderTrackerOffsetPx;
    private int sliderTrackerSizePx;
    private Point startTouchPoint;
    private float val;
    private Shader valShader;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.alpha = 255;
        this.hue = 360.0f;
        this.sat = 0.0f;
        this.val = 0.0f;
        this.showAlphaPanel = false;
        this.alphaSliderText = null;
        this.sliderTrackerColor = mDefaultSliderColor;
        this.borderColor = mDefaultBorderColor;
        this.startTouchPoint = null;
        init(context, attrs);
    }

    public Parcelable onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putParcelable("instanceState", super.onSaveInstanceState());
        state.putInt("alpha", this.alpha);
        state.putFloat("hue", this.hue);
        state.putFloat("sat", this.sat);
        state.putFloat("val", this.val);
        state.putBoolean("show_alpha", this.showAlphaPanel);
        state.putString("alpha_text", this.alphaSliderText);
        return state;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.alpha = bundle.getInt("alpha");
            this.hue = bundle.getFloat("hue");
            this.sat = bundle.getFloat("sat");
            this.val = bundle.getFloat("val");
            this.showAlphaPanel = bundle.getBoolean("show_alpha");
            this.alphaSliderText = bundle.getString("alpha_text");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    private void init(Context context, AttributeSet attrs) {
        mDefaultBorderColor = getResources().getColor(R.color.preference_color_default_border_color);
        mDefaultSliderColor = getResources().getColor(R.color.default_slider_color);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPickerView);
        this.showAlphaPanel = a.getBoolean(R.styleable.ColorPickerView_cpv_alphaChannelVisible, false);
        this.alphaSliderText = a.getString(R.styleable.ColorPickerView_cpv_alphaChannelText);
        this.sliderTrackerColor = a.getColor(R.styleable.ColorPickerView_cpv_sliderColor, -4342339);
        this.borderColor = a.getColor(R.styleable.ColorPickerView_borderColor, -9539986);
        a.recycle();
        applyThemeColors(context);
        this.huePanelWidthPx = DisplayUtils.dp2px(getContext(), 30.0f);
        this.alphaPanelHeightPx = DisplayUtils.dp2px(getContext(), 20.0f);
        this.panelSpacingPx = DisplayUtils.dp2px(getContext(), 10.0f);
        this.circleTrackerRadiusPx = DisplayUtils.dp2px(getContext(), 5.0f);
        this.sliderTrackerSizePx = DisplayUtils.dp2px(getContext(), 4.0f);
        this.sliderTrackerOffsetPx = DisplayUtils.dp2px(getContext(), 2.0f);
        this.mRequiredPadding = getResources().getDimensionPixelSize(R.dimen.cpv_required_padding);
        initPaintTools();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void applyThemeColors(Context c) {
        TypedValue value = new TypedValue();
        TypedArray a = c.obtainStyledAttributes(value.data, new int[]{16842808});
        if (this.borderColor == mDefaultBorderColor) {
            this.borderColor = a.getColor(0, mDefaultBorderColor);
        }
        if (this.sliderTrackerColor == mDefaultSliderColor) {
            this.sliderTrackerColor = a.getColor(0, mDefaultSliderColor);
        }
        a.recycle();
    }

    private void initPaintTools() {
        this.satValPaint = new Paint();
        this.satValTrackerPaint = new Paint();
        this.hueAlphaTrackerPaint = new Paint();
        this.alphaPaint = new Paint();
        this.alphaTextPaint = new Paint();
        this.borderPaint = new Paint();
        this.satValTrackerPaint.setStyle(Paint.Style.STROKE);
        this.satValTrackerPaint.setStrokeWidth(5.0f);
        this.satValTrackerPaint.setAntiAlias(true);
        this.hueAlphaTrackerPaint.setStyle(Paint.Style.STROKE);
        this.hueAlphaTrackerPaint.setColor(-1);
        this.hueAlphaTrackerPaint.setStrokeWidth(this.alphaPanelHeightPx / 5);
        this.hueAlphaTrackerPaint.setAntiAlias(true);
        this.alphaTextPaint.setColor(-14935012);
        this.alphaTextPaint.setTextSize(DisplayUtils.dp2px(getContext(), 14.0f));
        this.alphaTextPaint.setAntiAlias(true);
        this.alphaTextPaint.setTextAlign(Paint.Align.CENTER);
        this.alphaTextPaint.setFakeBoldText(true);
    }

    protected void onDraw(Canvas canvas) {
        if (this.drawingRect.width() > 0 && this.drawingRect.height() > 0) {
            drawSatValPanel(canvas);
            drawHuePanel(canvas);
            drawAlphaPanel(canvas);
        }
    }

    private void drawSatValPanel(Canvas canvas) {
        Rect rect = this.satValRect;
        this.borderPaint.setColor(this.borderColor);
        canvas.drawRect(this.drawingRect.left, this.drawingRect.top, rect.right + 1, rect.bottom + 1, this.borderPaint);
        if (this.valShader == null) {
            this.valShader = new LinearGradient(rect.left, rect.top, rect.left, rect.bottom, -1, -16777216, Shader.TileMode.CLAMP);
        }
        if (this.satValBackgroundCache == null || this.satValBackgroundCache.value != this.hue) {
            if (this.satValBackgroundCache == null) {
                this.satValBackgroundCache = new BitmapCache();
            }
            if (this.satValBackgroundCache.bitmap == null) {
                this.satValBackgroundCache.bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
            }
            if (this.satValBackgroundCache.canvas == null) {
                this.satValBackgroundCache.canvas = new Canvas(this.satValBackgroundCache.bitmap);
            }
            int rgb = Color.HSVToColor(new float[]{this.hue, 1.0f, 1.0f});
            this.satShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top, -1, rgb, Shader.TileMode.CLAMP);
            ComposeShader mShader = new ComposeShader(this.valShader, this.satShader, PorterDuff.Mode.MULTIPLY);
            this.satValPaint.setShader(mShader);
            this.satValBackgroundCache.canvas.drawRoundRect(0.0f, 0.0f, this.satValBackgroundCache.bitmap.getWidth(), this.satValBackgroundCache.bitmap.getHeight(), 10.0f, 10.0f, this.satValPaint);
            this.satValBackgroundCache.value = this.hue;
        }
        canvas.drawBitmap(this.satValBackgroundCache.bitmap, (Rect) null, rect, (Paint) null);
        Point p = satValToPoint(this.sat, this.val);
        this.satValTrackerPaint.setColor(-1);
        canvas.drawCircle(p.x, p.y, this.circleTrackerRadiusPx, this.satValTrackerPaint);
    }

    private void drawHuePanel(Canvas canvas) {
        Rect rect = this.hueRect;
        this.borderPaint.setColor(-16711936);
        canvas.drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, 60.0f, 60.0f, this.alphaPaint);
        if (this.hueBackgroundCache == null) {
            this.hueBackgroundCache = new BitmapCache();
            this.hueBackgroundCache.bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
            this.hueBackgroundCache.canvas = new Canvas(this.hueBackgroundCache.bitmap);
            int[] hueColors = new int[(int) (rect.height() + 0.5f)];
            float h = 360.0f;
            for (int i = 0; i < hueColors.length; i++) {
                hueColors[i] = Color.HSVToColor(new float[]{h, 1.0f, 1.0f});
                h -= 360.0f / hueColors.length;
            }
            Paint linePaint = new Paint();
            linePaint.setStrokeWidth(0.0f);
            for (int i2 = 0; i2 < hueColors.length; i2++) {
                linePaint.setColor(hueColors[i2]);
                this.hueBackgroundCache.canvas.drawLine(0.0f, i2, this.hueBackgroundCache.bitmap.getWidth(), i2, linePaint);
            }
        }
        canvas.drawBitmap(this.hueBackgroundCache.bitmap, (Rect) null, rect, (Paint) null);
        Point p = hueToPoint(this.hue);
        RectF r = new RectF();
        r.left = rect.left - this.sliderTrackerOffsetPx;
        r.right = rect.right + this.sliderTrackerOffsetPx;
        r.top = p.y - (this.sliderTrackerSizePx / 2);
        r.bottom = p.y + (this.sliderTrackerSizePx / 2);
        canvas.drawCircle((r.left + r.right) / 2.0f, p.y, this.circleTrackerRadiusPx, this.hueAlphaTrackerPaint);
    }

    private void drawAlphaPanel(Canvas canvas) {
        if (this.showAlphaPanel && this.alphaRect != null && this.alphaPatternDrawable != null) {
            Rect rect = this.alphaRect;
            this.borderPaint.setColor(0);
            canvas.drawRect(rect.left - 1, rect.top - 1, rect.right + 1, rect.bottom + 1, this.borderPaint);
            this.alphaPatternDrawable.draw(canvas);
            float[] hsv = {this.hue, this.sat, this.val};
            int color = Color.HSVToColor(hsv);
            int acolor = Color.HSVToColor(0, hsv);
            this.alphaShader = new LinearGradient(rect.left, rect.top, rect.right, rect.top, color, acolor, Shader.TileMode.CLAMP);
            this.alphaPaint.setShader(this.alphaShader);
            canvas.drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, 60.0f, 60.0f, this.alphaPaint);
            if (this.alphaSliderText != null && !this.alphaSliderText.equals("")) {
                canvas.drawText(this.alphaSliderText, rect.centerX(), rect.centerY() + DisplayUtils.dp2px(getContext(), 4.0f), this.alphaTextPaint);
            }
            Point p = alphaToPoint(this.alpha);
            RectF r = new RectF();
            r.left = p.x - (this.sliderTrackerSizePx / 2);
            r.right = p.x + (this.sliderTrackerSizePx / 2);
            r.top = rect.top - this.sliderTrackerOffsetPx;
            r.bottom = rect.bottom + this.sliderTrackerOffsetPx;
            canvas.drawCircle(p.x, (r.top + r.bottom) / 2.0f, this.circleTrackerRadiusPx, this.hueAlphaTrackerPaint);
        }
    }

    private Point hueToPoint(float hue) {
        Rect rect = this.hueRect;
        float height = rect.height();
        Point p = new Point();
        p.y = (int) ((height - ((hue * height) / 360.0f)) + rect.top);
        p.x = rect.left;
        return p;
    }

    private Point satValToPoint(float sat, float val) {
        Rect rect = this.satValRect;
        float height = rect.height();
        float width = rect.width();
        Point p = new Point();
        p.x = (int) ((sat * width) + rect.left);
        p.y = (int) (((1.0f - val) * height) + rect.top);
        return p;
    }

    private Point alphaToPoint(int alpha) {
        Rect rect = this.alphaRect;
        float width = rect.width();
        Point p = new Point();
        p.x = (int) ((width - ((alpha * width) / 255.0f)) + rect.left);
        p.y = rect.top;
        return p;
    }

    private float[] pointToSatVal(float x, float y) {
        float x2;
        float y2;
        Rect rect = this.satValRect;
        float[] result = new float[2];
        float width = rect.width();
        float height = rect.height();
        if (x < rect.left) {
            x2 = 0.0f;
        } else if (x > rect.right) {
            x2 = width;
        } else {
            x2 = x - rect.left;
        }
        if (y < rect.top) {
            y2 = 0.0f;
        } else if (y > rect.bottom) {
            y2 = height;
        } else {
            y2 = y - rect.top;
        }
        result[0] = (1.0f / width) * x2;
        result[1] = 1.0f - ((1.0f / height) * y2);
        return result;
    }

    private float pointToHue(float y) {
        float y2;
        Rect rect = this.hueRect;
        float height = rect.height();
        if (y < rect.top) {
            y2 = 0.0f;
        } else if (y > rect.bottom) {
            y2 = height;
        } else {
            y2 = y - rect.top;
        }
        float hue = 360.0f - ((y2 * 360.0f) / height);
        return hue;
    }

    private int pointToAlpha(int x) {
        int x2;
        Rect rect = this.alphaRect;
        int width = rect.width();
        if (x < rect.left) {
            x2 = 0;
        } else if (x > rect.right) {
            x2 = width;
        } else {
            x2 = x - rect.left;
        }
        return 255 - ((x2 * 255) / width);
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean update = false;
        switch (event.getAction()) {
            case 0:
                this.startTouchPoint = new Point((int) event.getX(), (int) event.getY());
                update = moveTrackersIfNeeded(event);
                break;
            case 1:
                this.startTouchPoint = null;
                update = moveTrackersIfNeeded(event);
                break;
            case 2:
                update = moveTrackersIfNeeded(event);
                break;
        }
        if (update) {
            if (this.onColorChangedListener != null) {
                this.onColorChangedListener.onColorChanged(Color.HSVToColor(this.alpha, new float[]{this.hue, this.sat, this.val}));
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean moveTrackersIfNeeded(MotionEvent event) {
        if (this.startTouchPoint == null) {
            return false;
        }
        int startX = this.startTouchPoint.x;
        int startY = this.startTouchPoint.y;
        if (this.hueRect.contains(startX, startY)) {
            this.hue = pointToHue(event.getY());
            return true;
        } else if (this.satValRect.contains(startX, startY)) {
            float[] result = pointToSatVal(event.getX(), event.getY());
            this.sat = result[0];
            this.val = result[1];
            return true;
        } else if (this.alphaRect == null || !this.alphaRect.contains(startX, startY)) {
            return false;
        } else {
            this.alpha = pointToAlpha((int) event.getX());
            return true;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth;
        int finalHeight;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthAllowed = (View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()) - getPaddingRight();
        int heightAllowed = (View.MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom()) - getPaddingTop();
        if (widthMode == 1073741824 || heightMode == 1073741824) {
            if (widthMode == 1073741824 && heightMode != 1073741824) {
                int h = (widthAllowed - this.panelSpacingPx) - this.huePanelWidthPx;
                if (this.showAlphaPanel) {
                    h += this.panelSpacingPx + this.alphaPanelHeightPx;
                }
                if (h > heightAllowed) {
                    finalHeight = heightAllowed;
                } else {
                    finalHeight = h;
                }
                finalWidth = widthAllowed;
            } else if (heightMode == 1073741824 && widthMode != 1073741824) {
                int w = this.panelSpacingPx + heightAllowed + this.huePanelWidthPx;
                if (this.showAlphaPanel) {
                    w -= this.panelSpacingPx + this.alphaPanelHeightPx;
                }
                if (w > widthAllowed) {
                    finalWidth = widthAllowed;
                } else {
                    finalWidth = w;
                }
                finalHeight = heightAllowed;
            } else {
                finalWidth = widthAllowed;
                finalHeight = heightAllowed;
            }
        } else {
            int widthNeeded = this.panelSpacingPx + heightAllowed + this.huePanelWidthPx;
            int heightNeeded = (widthAllowed - this.panelSpacingPx) - this.huePanelWidthPx;
            if (this.showAlphaPanel) {
                widthNeeded -= this.panelSpacingPx + this.alphaPanelHeightPx;
                heightNeeded += this.panelSpacingPx + this.alphaPanelHeightPx;
            }
            boolean widthOk = false;
            boolean heightOk = false;
            if (widthNeeded <= widthAllowed) {
                widthOk = true;
            }
            if (heightNeeded <= heightAllowed) {
                heightOk = true;
            }
            if (widthOk && heightOk) {
                finalWidth = widthAllowed;
                finalHeight = heightNeeded;
            } else if (!heightOk && widthOk) {
                finalHeight = heightAllowed;
                finalWidth = widthNeeded;
            } else if (!widthOk && heightOk) {
                finalHeight = heightNeeded;
                finalWidth = widthAllowed;
            } else {
                finalHeight = heightAllowed;
                finalWidth = widthAllowed;
            }
        }
        setMeasuredDimension(getPaddingLeft() + finalWidth + getPaddingRight(), getPaddingTop() + finalHeight + getPaddingBottom());
    }

    private int getPreferredWidth() {
        int width = DisplayUtils.dp2px(getContext(), 200.0f);
        return this.huePanelWidthPx + width + this.panelSpacingPx;
    }

    private int getPreferredHeight() {
        int height = DisplayUtils.dp2px(getContext(), 200.0f);
        if (this.showAlphaPanel) {
            return height + this.panelSpacingPx + this.alphaPanelHeightPx;
        }
        return height;
    }

    public int getPaddingTop() {
        return Math.max(super.getPaddingTop(), this.mRequiredPadding);
    }

    public int getPaddingBottom() {
        return Math.max(super.getPaddingBottom(), this.mRequiredPadding);
    }

    public int getPaddingLeft() {
        return Math.max(super.getPaddingLeft(), this.mRequiredPadding);
    }

    public int getPaddingRight() {
        return Math.max(super.getPaddingRight(), this.mRequiredPadding);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.drawingRect = new Rect();
        this.drawingRect.left = getPaddingLeft();
        this.drawingRect.right = w - getPaddingRight();
        this.drawingRect.top = getPaddingTop();
        this.drawingRect.bottom = h - getPaddingBottom();
        this.valShader = null;
        this.satShader = null;
        this.alphaShader = null;
        this.satValBackgroundCache = null;
        this.hueBackgroundCache = null;
        setUpSatValRect();
        setUpHueRect();
        setUpAlphaRect();
    }

    private void setUpSatValRect() {
        Rect dRect = this.drawingRect;
        int left = dRect.left + 1;
        int top = dRect.top + 1;
        int bottom = dRect.bottom - 1;
        int right = ((dRect.right - 1) - this.panelSpacingPx) - this.huePanelWidthPx;
        if (this.showAlphaPanel) {
            bottom -= this.alphaPanelHeightPx + this.panelSpacingPx;
        }
        this.satValRect = new Rect(left, top, right, bottom);
    }

    private void setUpHueRect() {
        Rect dRect = this.drawingRect;
        int left = (dRect.right - this.huePanelWidthPx) + 1;
        int top = dRect.top + 1;
        int bottom = (dRect.bottom - 1) - (this.showAlphaPanel ? this.panelSpacingPx + this.alphaPanelHeightPx : 0);
        int right = dRect.right - 1;
        this.hueRect = new Rect(left, top, right, bottom);
    }

    private void setUpAlphaRect() {
        if (this.showAlphaPanel) {
            Rect dRect = this.drawingRect;
            int left = dRect.left + 1;
            int top = (dRect.bottom - this.alphaPanelHeightPx) + 1;
            int bottom = dRect.bottom - 1;
            int right = dRect.right - 1;
            this.alphaRect = new Rect(left, top, right, bottom);
            this.alphaPatternDrawable = new AlphaPatternDrawable(DisplayUtils.dp2px(getContext(), 5.0f));
            this.alphaPatternDrawable.setBounds(Math.round(this.alphaRect.left), Math.round(this.alphaRect.top), Math.round(this.alphaRect.right), Math.round(this.alphaRect.bottom));
        }
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.onColorChangedListener = listener;
    }

    public int getColor() {
        return Color.HSVToColor(this.alpha, new float[]{this.hue, this.sat, this.val});
    }

    public void setColor(int color) {
        setColor(color, false);
    }

    public void setColor(int color, boolean callback) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);
        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        this.alpha = alpha;
        this.hue = hsv[0];
        this.sat = hsv[1];
        this.val = hsv[2];
        if (callback && this.onColorChangedListener != null) {
            this.onColorChangedListener.onColorChanged(Color.HSVToColor(this.alpha, new float[]{this.hue, this.sat, this.val}));
        }
        invalidate();
    }

    public void setAlphaSliderVisible(boolean visible) {
        if (this.showAlphaPanel != visible) {
            this.showAlphaPanel = visible;
            this.valShader = null;
            this.satShader = null;
            this.alphaShader = null;
            this.hueBackgroundCache = null;
            this.satValBackgroundCache = null;
            requestLayout();
        }
    }

    public int getSliderTrackerColor() {
        return this.sliderTrackerColor;
    }

    public void setSliderTrackerColor(int color) {
        this.sliderTrackerColor = color;
        this.hueAlphaTrackerPaint.setColor(this.sliderTrackerColor);
        invalidate();
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
        invalidate();
    }

    public String getAlphaSliderText() {
        return this.alphaSliderText;
    }

    public void setAlphaSliderText(int res) {
        String text = getContext().getString(res);
        setAlphaSliderText(text);
    }

    public void setAlphaSliderText(String text) {
        this.alphaSliderText = text;
        invalidate();
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    private class BitmapCache {
        public Bitmap bitmap;
        public Canvas canvas;
        public float value;

        private BitmapCache() {
        }
    }
}