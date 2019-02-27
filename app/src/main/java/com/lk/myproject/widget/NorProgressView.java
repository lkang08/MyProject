package com.lk.myproject.widget;


import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lk.myproject.R;
/**
 * 进度条视图，只能在代码中使用
 * 支持两种模式：不确定进度（三点动画）和 可设置具体进度
 */
public class NorProgressView {
	private Activity mContext;
	private boolean mIndeterminate;
	private FrameLayout mContentLayout;
	private TextView mMessageTv;
	private ProgressBar mProgressBar;
	private boolean mFocus;

	/**
	 * 创建一个不确定进度视图
	 * @param context 
	 */
	public NorProgressView(Activity context) {
		this(context, true);
	}

	/**
	 * 创建一个进度视图
	 * @param context
	 * @param indeterminate 是否为不确定类型
	 */
	public NorProgressView(Activity context, boolean indeterminate) {
		mContext = context;
		mIndeterminate = indeterminate;
	}
	
	/**
	 * 显示进度视图(显示努力加载中...)
	 * 采用赖加载方式，第一次调用或dismiss之后会执行UI填充和附加操作，再使其可见和设置提示内容
	 * 再次调用时 只是使其可见和设置提示内容
	 */
	public void show(){
		show("努力加载中");
	}

	/**
	 * 显示进度视图
	 * 采用赖加载方式，第一次调用或dismiss之后会执行UI填充和附加操作，再使其可见和设置提示内容
	 * 再次调用时 只是使其可见和设置提示内容
	 * @param message 提示内容
	 */
	public void show(CharSequence message) {
		if (mContentLayout == null) {// 这里默认约定在UI线程调用，故不考虑多线程问题
			FrameLayout rootView = (FrameLayout) mContext.getWindow().getDecorView().findViewById(android.R.id.content);
			LayoutParams lp;

			LayoutInflater inflater = LayoutInflater.from(mContext);
			View progressLayout;
			if (mIndeterminate) {
				progressLayout = inflater.inflate(R.layout.dw_progress_view_indeterminate_layout, null);
			} else {
				progressLayout = inflater.inflate(R.layout.dw_progress_view_determinate_layout, null);
				mProgressBar = (ProgressBar) progressLayout.findViewById(R.id.progress_pb);
			}
			mMessageTv = (TextView) progressLayout.findViewById(R.id.message_tv);

			mContentLayout = new FrameLayout(mContext);
			mContentLayout.addView(progressLayout, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

            if (mFocus) {// 屏蔽主界面的点击事件
                mContentLayout.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }

			lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
			rootView.addView(mContentLayout, lp);
		}
		mMessageTv.setText(message);
		if (mContentLayout.getVisibility() != View.VISIBLE) {
			mContentLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏进度条视图
	 * dismiss 和 hide 之后都可再次调用show显示，差别在于dismiss会从根view中移除，再show时需要重新附加到根view
	 * hide 只是使其不可见
	 */
	public void hide() {
		if (mContentLayout != null) {
			mContentLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 判断当前视图是否显示中
	 * @return
	 */
	public boolean isShowing() {
		if (mContentLayout != null && mContentLayout.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 销毁进度视图,如果确定不再使用可调用该方法，否则调用hide
	 * dismiss 和 hide 之后都可再次调用show显示，差别在于dismiss会从根view中移除，再show时需要重新附加到根view
	 * hide 只是使其不可见
	 */
	public void dismiss() {
		if (mContentLayout != null) {
			FrameLayout rootView = (FrameLayout) mContext.getWindow().getDecorView().findViewById(android.R.id.content);
			mContentLayout.setVisibility(View.GONE);
			rootView.removeView(mContentLayout);
			mContentLayout = null;
			mMessageTv = null;
			mProgressBar = null;
		}
	}

	public void setFocus(boolean focus) {
		mFocus = focus;
	}

	/**
	 * 设置进度（针对确定进度视图的有效）
	 * @param progress 有效范围为 0—100
	 */
	public void setProgress(int progress) {
		setProgress(progress, null);
	}

	/**
	 * 设置进度并修改提示内容（针对确定进度视图的有效）
	 * @param progress 有效范围为 0—100
	 */
	public void setProgress(int progress, CharSequence message) {
		if (progress < 0 || progress > 100) {
			return;
		}
		if (mProgressBar != null) {
			mProgressBar.setProgress(progress);
		}
		if (mMessageTv != null && message != null) {
			mMessageTv.setText(message);
		}
	}

	/**
	 * 获取当前进度（针对确定进度视图的有效）
	 */
	public int getProgress() {
		return mProgressBar != null ? mProgressBar.getProgress() : 0;
	}

	/**
	 * 当前进度增加diff（针对确定进度视图的有效）
	 * @param diff 递增值
	 */
	public void incrementProgressBy(int diff) {
		if (mProgressBar != null) {
			mProgressBar.incrementProgressBy(diff);
		}
	}
}
