package com.lk.myproject.giftanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lk.myproject.R;
import com.lk.myproject.utils.FP;

import java.util.ArrayList;
import java.util.Iterator;

public class SendGiftAnimationView extends FrameLayout
        implements ValueAnimator.AnimatorUpdateListener {

    private static final String TAG = SendGiftAnimationView.class.getSimpleName();
    private static final int MAX_CACHE_SIZE = 3;
    int size = 0;
    private boolean animating = false;
    private boolean isAnimationStart = false;
    private OnAnimationListener mOnAnimationListener = null;
    private ArrayList<AnimationHolder> mItems = new ArrayList<AnimationHolder>();
    private ArrayList<View> mScrapViews = new ArrayList<View>();
    private ArrayList<Animator> mAnimators = new ArrayList<Animator>();

    public SendGiftAnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        size = getResources().getDimensionPixelSize(R.dimen.room_animator_gift_size);
    }

    public SendGiftAnimationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendGiftAnimationView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.i(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();

        if (mScrapViews != null) {
            mScrapViews.clear();
        }

        if (mItems != null) {
            mItems.clear();
        }

    }

    private AnimationHolder getAnimationHolder(long id, String url) {
        View view;
        if (!mScrapViews.isEmpty() && this.indexOfChild(mScrapViews.get(0)) == -1) {
            Log.i(TAG, "startGiftAnimation1  getView from cache ");
            view = mScrapViews.get(0);
            mScrapViews.remove(0);
        } else {
            Log.i(TAG, "startGiftAnimation1  getView from new View ");
            view = inflate(getContext(), R.layout.layout_gift_anim_send, null);
        }
        ImageView giftView = view.findViewById(R.id.iv_gift);
        TextView tv = view.findViewById(R.id.tv_weekstar_tile);
        if (id != 0) {
            String name = "";
            if (!FP.empty(name)) {
                tv.setText(name);
                Log.i(TAG, "startGiftAnimation name visable");
                tv.setVisibility(View.VISIBLE);
            } else {
                Log.i(TAG, "startGiftAnimation name gone");
                tv.setVisibility(View.GONE);
            }
        }
        if (url.contains("drawable://")) {
            int resId = Integer.parseInt(url.replace("drawable://", ""));
            Glide.with(this).load(resId).into(giftView);
        } else {
            Glide.with(this).load(url).into(giftView);
        }

        addView(view, new LayoutParams(size, size));

        final AnimationHolder holder = new AnimationHolder();
        holder.view = view;
        holder.index = mItems.size();
        mItems.add(holder);
        return holder;
    }

    public void startGiftAnimation(long id, String url, int startX, int startY, int middleX,
                                   int middleY, int endX,
                                   int endY, float minScale, float maxScale,
                                   long inDuration, long outDuration, long middleDuration) {

        final AnimationHolder holder = getAnimationHolder(id, url);

        PropertyValuesHolder initScaleX1 = PropertyValuesHolder.ofFloat("scaleX", 0.0f, minScale);
        PropertyValuesHolder initScaleY1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, minScale);
        PropertyValuesHolder initPvhX = PropertyValuesHolder.ofInt("x", startX, startX);
        PropertyValuesHolder initPvhY = PropertyValuesHolder.ofInt("y", startY, startY);
        ObjectAnimator initScaleAnimator1 =
                ObjectAnimator.ofPropertyValuesHolder(holder, initScaleX1, initScaleY1,
                        initPvhX, initPvhY);
        long duration1 = (long) (inDuration * 0.2f);
        initScaleAnimator1.setDuration(duration1);
        initScaleAnimator1.addUpdateListener(this);

        PropertyValuesHolder initScaleX2 = PropertyValuesHolder.ofFloat("scaleX", minScale,
                minScale * 0.6f);
        PropertyValuesHolder initScaleY2 = PropertyValuesHolder.ofFloat("scaleY", minScale,
                minScale * 0.6f);
        ObjectAnimator initScaleAnimator2 =
                ObjectAnimator.ofPropertyValuesHolder(holder, initScaleX2, initScaleY2);
        initScaleAnimator2.setDuration((int) (duration1 * 0.8f));
        initScaleAnimator2.addUpdateListener(this);

        PropertyValuesHolder startPvhX = PropertyValuesHolder.ofInt("x", startX, middleX);
        PropertyValuesHolder startPvhY = PropertyValuesHolder.ofInt("y", startY, middleY);
        PropertyValuesHolder startPvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
                minScale * 0.6f, maxScale);
        PropertyValuesHolder startPvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
                minScale * 0.6f, maxScale);
        ObjectAnimator startObjectAnimator =
                ObjectAnimator.ofPropertyValuesHolder(holder, startPvhX, startPvhY,
                        startPvhScaleX, startPvhScaleY);
        startObjectAnimator.setDuration(inDuration);
        startObjectAnimator.setStartDelay((int) (duration1 * 0.8f));
        startObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        startObjectAnimator.addUpdateListener(this);

        PropertyValuesHolder endPvhX = PropertyValuesHolder.ofInt("x", middleX, endX);
        PropertyValuesHolder endPvhY = PropertyValuesHolder.ofInt("y", middleY, endY);
        PropertyValuesHolder endPvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
                maxScale, minScale * 0.8f);
        PropertyValuesHolder endPvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
                maxScale, minScale * 0.8f);
        ObjectAnimator endObjectAnimator =
                ObjectAnimator.ofPropertyValuesHolder(holder, endPvhX, endPvhY,
                        endPvhScaleX, endPvhScaleY);
        endObjectAnimator.setDuration(outDuration);
        endObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        endObjectAnimator.addUpdateListener(this);

        PropertyValuesHolder finishScaleX1 = PropertyValuesHolder.ofFloat("scaleX",
                minScale * 0.8f, minScale);
        PropertyValuesHolder finishScaleY1 = PropertyValuesHolder.ofFloat("scaleY",
                minScale * 0.8f, minScale);
        ObjectAnimator finishScaleAnimator1 =
                ObjectAnimator.ofPropertyValuesHolder(holder, finishScaleX1,
                        finishScaleY1);
        long duration2 = (long) (outDuration * 0.2f);
        finishScaleAnimator1.setDuration((int) (duration2 * 0.8f));
        finishScaleAnimator1.addUpdateListener(this);

        PropertyValuesHolder finishScaleX2 = PropertyValuesHolder.ofFloat("scaleX",
                minScale, 0.0f);
        PropertyValuesHolder finishScaleY2 = PropertyValuesHolder.ofFloat("scaleY",
                minScale, 0.0f);
        ObjectAnimator finishScaleAnimator2 = ObjectAnimator.ofPropertyValuesHolder(holder,
                finishScaleX2, finishScaleY2);
        finishScaleAnimator2.setDuration(duration2);
        finishScaleAnimator2.setStartDelay((int) (duration1 * 0.8f));
        finishScaleAnimator2.addUpdateListener(this);

        AnimatorSet set = new AnimatorSet();
        mAnimators.add(set);
        set.addListener(new MyAnimatorListenerAdapter(holder));
        set.play(initScaleAnimator1).before(initScaleAnimator2);
        set.play(initScaleAnimator2).before(startObjectAnimator);
        set.play(startObjectAnimator).before(endObjectAnimator);
        set.play(endObjectAnimator).before(finishScaleAnimator1);
        set.play(finishScaleAnimator1).before(finishScaleAnimator2);
        set.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        for (AnimationHolder holder : mItems) {
            /*Log.debug(TAG, "draw x = " + holder.x + " y = " + holder.y + " sx=" + holder.scaleX + "
            sy=" + holder.scaleY + " items size=" + mItems.size()
                    + " index=" + holder.index);*/
            canvas.save();
            canvas.translate(holder.x, holder.y);
//  canvas.scale(holder.scaleX, holder.scaleY);
            canvas.scale(holder.scaleX, holder.scaleY, holder.view.getWidth() / 2f,
                    holder.view.getHeight() / 2f);
            holder.view.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator arg0) {
        this.invalidate();
    }

    public OnAnimationListener getOnAnimationListener() {
        return mOnAnimationListener;
    }

    public void setOnAnimationListener(OnAnimationListener mOnAnimationListener) {
        this.mOnAnimationListener = mOnAnimationListener;
    }

    public void stopAnimation() {
        Iterator<Animator> iterator = mAnimators.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel();
        }
    }

    /*@Override
    public void attachLifecycle(FragmentActivity lifecycleOwner) {
        if (lifecycleOwner == null) {
            recycle();
            return;
        }

        lifecycleOwner.getLifecycle().addObserver(new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                Log.i(TAG, event.name());
                if (event == Lifecycle.Event.ON_DESTROY) {
                    Log.i(TAG, "Lifecycle.Event.ON_DESTROY");
                    recycle();
                } else if (event == Lifecycle.Event.ON_STOP) {
                    Log.i(TAG, "Lifecycle.Event.ON_STOP");
                    if (mOnAnimationListener != null) {
                        mOnAnimationListener.onAnimationStop();
                    }
                    recycle();
                }
            }
        });
    }

    @Override*/

    public void recycle() {
        mItems.clear();
        stopAnimation();
        mAnimators.clear();
        animating = false;
        isAnimationStart = false;
        mOnAnimationListener = null;
    }

    public interface OnAnimationListener {

        void onAnimationStart();

        void onAnimationEnd();

        void onAnimationCancel();

        void onAnimationStop();
    }

    private static class AnimationHolder {

        public int x;
        public int y;

        public View view;

        public float scaleX;
        public float scaleY;

        public int index;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public float getScaleX() {
            return scaleX;
        }

        public void setScaleX(float scaleX) {
            this.scaleX = scaleX;
        }

        public float getScaleY() {
            return scaleY;
        }

        public void setScaleY(float scaleY) {
            this.scaleY = scaleY;
        }

    }

    class MyAnimatorListenerAdapter extends AnimatorListenerAdapter {

        AnimationHolder holder;

        MyAnimatorListenerAdapter(AnimationHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            if (holder.view != null) {
                holder.view.setVisibility(View.GONE);
            }
            mAnimators.remove(animation);
            mItems.remove(holder);

            if (mScrapViews.size() <= MAX_CACHE_SIZE) {
                mScrapViews.add(holder.view);
                holder.view = null;
            }

            Log.i(TAG, "onAnimationEnd ");
            if (mItems.isEmpty()) {
                animating = false;
                isAnimationStart = false;
                if (mOnAnimationListener != null) {
                    mOnAnimationListener.onAnimationEnd();
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            Log.i(TAG, "onAnimationCancel");
            if (holder.view != null) {
                holder.view.setVisibility(View.GONE);
            }
            super.onAnimationCancel(animation);
            mAnimators.remove(animation);
            mItems.remove(holder);

            if (mScrapViews.size() <= MAX_CACHE_SIZE) {
                mScrapViews.add(holder.view);
                holder.view = null;
            }

            if (mItems.isEmpty()) {
                animating = false;
                isAnimationStart = false;
                if (mOnAnimationListener != null) {
                    mOnAnimationListener.onAnimationCancel();
                }
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            animating = true;
            if (holder.view != null) {
                holder.view.setVisibility(View.VISIBLE);
            }

            if (mItems.size() > 0 && !isAnimationStart) {
                Log.i(TAG, "onAnimationStart ");
                isAnimationStart = true;

                if (mOnAnimationListener != null) {
                    mOnAnimationListener.onAnimationStart();
                }
            }
        }
    }

}
