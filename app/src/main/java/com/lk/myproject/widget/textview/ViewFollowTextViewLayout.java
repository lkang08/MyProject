package com.lk.myproject.widget.textview;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chen.yingjie on 2019/7/23
 * description 第一个子控件是TextView，第二个子控件紧跟这TextView后面显示。
 * 使用小技巧：给TextView设置lineSpacingExtra来增加行间距，以免第二个子控件显示时遮住TextView。
 */
public class ViewFollowTextViewLayout extends ViewGroup {

    private static final int CHILD_COUNT = 2; //目前支持包含两个子控件，左边必须是TextView，右边是任意的View或ViewGroup
    public boolean isOnlyOneLine = true; //只适配一行展示的下的情况

    public ViewFollowTextViewLayout(Context context) {
        this(context, null);
    }

    public ViewFollowTextViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewFollowTextViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnlyOneLine(boolean onlyOneLine) {
        isOnlyOneLine = onlyOneLine;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (getChildCount() == CHILD_COUNT && getChildAt(0) instanceof TextView) {
            TextView child0 = (TextView) getChildAt(0);
            measureChild(child0, widthMeasureSpec, heightMeasureSpec);

            int child0MeasuredWidth = child0.getMeasuredWidth();
            int child0MeasuredHeight = child0.getMeasuredHeight();

            View child1 = getChildAt(1);
            measureChild(child1, widthMeasureSpec, heightMeasureSpec);

            int child1MeasuredWidth = child1.getMeasuredWidth();
            MarginLayoutParams mlp = (MarginLayoutParams) child1.getLayoutParams();
            int child1MeasuredHeight = child1.getMeasuredHeight();

            int contentWidth = child0MeasuredWidth + child1MeasuredWidth + mlp.leftMargin;
            int contentHeight = 0;

            if (contentWidth > maxWidth) { // 一行显示不下
                contentWidth = maxWidth;
                // 主要为了确定内部子View的总宽高
                int child0LineCount = child0.getLineCount();
                int child0LastLineWidth = getLineWidth(child0, child0LineCount - 1); // child0最后一行宽
                int contentLastLineWidth = child0LastLineWidth + child1MeasuredWidth + mlp.leftMargin;

                if (contentLastLineWidth > maxWidth) { // 最后一行显示不下child1
                    contentHeight = child0MeasuredHeight + child1MeasuredHeight + mlp.topMargin;
                } else { // 最后一行能显示的下child1
                    contentHeight = child0MeasuredHeight;
                }
            } else { // 一行显示完整
                contentHeight = child0MeasuredHeight;
            }
            setMeasuredDimension(contentWidth, contentHeight);
        } else {
            setMeasuredDimension(maxWidth, maxHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == CHILD_COUNT && getChildAt(0) instanceof TextView) {
            int maxWidth = r - l;

            TextView child0 = (TextView) getChildAt(0);
            int child0MeasuredWidth = child0.getMeasuredWidth();
            int child0MeasuredHeight = child0.getMeasuredHeight();

            // 布局child0，这个没什么可说的，位置确定。
            child0.layout(0, 0, child0MeasuredWidth, child0MeasuredHeight);

            View child1 = getChildAt(1);
            int child1MeasuredWidth = child1.getMeasuredWidth();
            MarginLayoutParams mlp = (MarginLayoutParams) child1.getLayoutParams();
            int child1MeasuredHeight = child1.getMeasuredHeight();

            int contentWidth = child0MeasuredWidth + child1MeasuredWidth + mlp.leftMargin;
            // ★★★ 主要为了布局child1 ★★★
            if (contentWidth > maxWidth) { // 一行显示不下
                int child0LineCount = child0.getLineCount();
                int child0LastLineWidth = getLineWidth(child0, child0LineCount - 1); // child0最后一行宽
                int contentLastLineWidth = child0LastLineWidth + child1MeasuredWidth + mlp.leftMargin;

                int left;
                int top;
                if (contentLastLineWidth > maxWidth) { // 最后一行显示不下child1
                    left = 0;
                    top = child0MeasuredHeight;
                } else { // 最后一行显示的下child1
                    left = child0LastLineWidth + mlp.leftMargin;
                    top = child0MeasuredHeight - child1MeasuredHeight;
                }
                child1.layout(left, top, left + child1MeasuredWidth, top + child1MeasuredHeight);
            } else { // 一行能显示完整
                int left = child0MeasuredWidth + mlp.leftMargin;
                int top = (child0MeasuredHeight - child1MeasuredHeight) / 2;
                child1.layout(left, top, left + child1MeasuredWidth, top + child1MeasuredHeight);
            }
        }
    }

    /**
     * 获取TextView第lineNum行的宽
     *
     * @param textView
     * @param lineNum
     * @return
     */
    private int getLineWidth(TextView textView, int lineNum) {
        Layout layout = textView.getLayout();
        int lineCount = textView.getLineCount();
        if (layout != null && lineNum >= 0 && lineNum < lineCount) {
            return (int) (layout.getLineWidth(lineNum) + 0.5);
        }
        return 0;
    }
}
