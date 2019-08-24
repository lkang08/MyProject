package com.lk.myproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.lk.myproject.R;

/**
 * 水波纹进度  https://github.com/gelitenight/WaveView
 * create by lzx
 * time:2018/9/11
 */
public class WaveProgressView extends View {

    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public enum ShapeType {
        CIRCLE,
        SQUARE,
        HEART
    }

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;
    // paint to draw border
    private Paint mBorderPaint;

    private Paint mTextPaint;

    private int textSize = 8; //sp
    private String textColor = "#0b7091";

    private String currProgressText = "0%";

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;

    public WaveProgressView(Context context) {
        super(context);
        init();
    }

    public WaveProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WaveProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        // mViewPaint.setColor(Color.WHITE);
        mViewPaint.setStyle(Paint.Style.FILL);//设置填充样式
        mViewPaint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mViewPaint.setFilterBitmap(true);//加快显示速度，本设置项依赖于dither和xfermode的设置

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor(textColor));
        mTextPaint.setTextSize(sp2px(getContext(), textSize));
    }

    private void init(AttributeSet attrs) {
        init();

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.WaveView, 0, 0);

        mAmplitudeRatio = typedArray.getFloat(R.styleable.WaveView_amplitudeRatio, DEFAULT_AMPLITUDE_RATIO);
        mWaterLevelRatio = typedArray.getFloat(R.styleable.WaveView_waveWaterLevel, DEFAULT_WATER_LEVEL_RATIO);
        mWaveLengthRatio = typedArray.getFloat(R.styleable.WaveView_waveLengthRatio, DEFAULT_WAVE_LENGTH_RATIO);
        mWaveShiftRatio = typedArray.getFloat(R.styleable.WaveView_waveShiftRatio, DEFAULT_WAVE_SHIFT_RATIO);
        mFrontWaveColor = typedArray.getColor(R.styleable.WaveView_frontWaveColor, DEFAULT_FRONT_WAVE_COLOR);
        mBehindWaveColor = typedArray.getColor(R.styleable.WaveView_behindWaveColor, DEFAULT_BEHIND_WAVE_COLOR);
        mShapeType = typedArray.getInt(R.styleable.WaveView_waveShape, 0) == 0 ? ShapeType.CIRCLE : ShapeType.SQUARE;
        mShowWave = typedArray.getBoolean(R.styleable.WaveView_showWave, true);

        typedArray.recycle();

    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     *   根据  waveShiftRatio 水平移动波形。
     *     
     *  @param waveShiftRatio应为0~1。默认为0。
     *   waveShiftRatio的结果WaveView的宽度是移位的长度。
     *      
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    public void updateCurrProgressText(String currProgressText) {
        this.currProgressText = currProgressText;
    }

    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Paint.Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        if (getWidth() > 0 && getHeight() > 0) {
            // need to recreate shader when color changed
            mWaveShader = null;
            createShader();
            invalidate();
        }
    }

    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        totalW=w;
        totalH=h;
        if (getWidth() > 0 && getHeight() > 0) {
            try {
                createShader();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    Bitmap photo;
    private void initialize() {
        mScreenWidth = getWidth();
        mScreenHeight = getHeight();

        Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_heart)).getBitmap();
        photo = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
    }

        private int totalW,totalH,mScreenWidth,mScreenHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) {
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                                (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    float radius = getWidth() / 2f - borderWidth;
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);
                    break;
                case SQUARE:
                    if(photo == null){
                        initialize();
                    }
                    //canvas.drawBitmap(photo, 0, 0, null);
                    int sc=canvas.saveLayer(0,0,totalW,totalH,mViewPaint,Canvas.ALL_SAVE_FLAG);
                    canvas.drawBitmap(photo, 0, 0, null);

                    mViewPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
                    mViewPaint.setShader(mWaveShader);
                    if (borderWidth > 0) {
                        canvas.drawRect(
                                borderWidth / 2f,
                                borderWidth / 2f,
                                getWidth() - borderWidth / 2f - 0.5f,
                                getHeight() - borderWidth / 2f - 0.5f,
                                mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                            getHeight() - borderWidth, mViewPaint);
                    mViewPaint.setXfermode(null);
                    canvas.restoreToCount(sc);

                    break;
                case HEART:
                    int width = getWidth(); //获取屏幕宽
                    int height = getHeight(); //获取屏幕高

                    //左半面
                    Path path = new Path();
                    path.moveTo(width / 2, height / 4);
                    path.cubicTo((width * 6) / 7, height / 9, (width * 12) / 13, (height * 2) / 5,
                            width / 2, (height * 7) / 12);
                    canvas.drawPath(path, mViewPaint);
                    //右半面
                    Path path2 = new Path();
                    path2.moveTo(width / 2, height / 4);
                    path2.cubicTo(width / 7, height / 9, width / 13, (height * 2) / 5, width / 2,
                            (height * 7) / 12);
                    canvas.drawPath(path2, mViewPaint);
                    break;
                default:
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }

        /*float textWidth = mTextPaint.measureText(currProgressText);
        float textHeight = mTextPaint.ascent() + mTextPaint.descent();
        float textX = getWidth() / 2;
        float textY = getHeight() / 2;
        textX = textX - textWidth / 2;
        textY = textY - textHeight / 2;
        canvas.drawText(currProgressText, textX, textY, mTextPaint);*/
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}