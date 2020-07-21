package com.lk.myproject.widget.textview;

import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.lk.myproject.R;
import com.lk.myproject.utils.Utils;

public class TextUtils {
    public static final String doubleSpace = "\t\t";

    public interface OnTextClickListener {
        void onActiveClick();
        void onOpenClose(boolean canShow, boolean isOpen);
    }

    /**
     * TextView超过maxLine行，设置展开/收起。
     * @param tv
     * @param maxLine
     * @param active
     * @param desc
     * @param onTextClickListener
     */
    public static void setLimitLineText(final TextView tv, int maxLine, String active, String desc,
                                        final OnTextClickListener onTextClickListener) {
        final SpannableStringBuilder elipseString = new SpannableStringBuilder();//收起的文字
        final SpannableStringBuilder notElipseString = new SpannableStringBuilder();//展开的文字
        String content;
        if (Utils.isEmpty(desc)) {
            desc = "";
        }
        if (Utils.isEmpty(active)) {
            content = desc;
        } else {
            content = String.format("#%1$s%2$s%3$s", active, doubleSpace, desc);
        }
        //获取TextView的画笔对象
        TextPaint paint = tv.getPaint();
        //每行文本的布局宽度
        int width = tv.getContext().getResources().getDisplayMetrics().widthPixels - Utils.dp2px(40);
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL,
                1, 0, false);

        // 活动添加超链接
        ClickableSpan activeClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (onTextClickListener != null) {
                    onTextClickListener.onActiveClick();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(tv.getContext().getResources().getColor(R.color.color_purple));
                ds.setFakeBoldText(true);// 加粗
                ds.setUnderlineText(false);// 下划线
            }
        };

        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount() > maxLine) {
            //定义展开后的文本内容
            notElipseString.append(content).append(doubleSpace).append("收起");

            // 展开/收起
            ClickableSpan stateClick = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (widget.isSelected()) {
                        //如果是收起的状态
                        tv.setText(notElipseString);
                        tv.setSelected(false);
                    } else {
                        //如果是展开的状态
                        tv.setText(elipseString);
                        tv.setSelected(true);
                    }
                    if (onTextClickListener != null) {
                        onTextClickListener.onOpenClose(false, widget.isSelected());
                    }
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setColor(tv.getContext().getResources().getColor(R.color.white));
                }
            };

            //给收起两个字设置样式
            notElipseString.setSpan(stateClick, notElipseString.length() - 2, notElipseString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 活动样式
            notElipseString.setSpan(activeClick, 0, active.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //获取到最后一行最后一个文字的下标
            int index = staticLayout.getLineStart(maxLine) - 1;
            //定义收起后的文本内容
            elipseString.append(content.substring(0, index - 4)).append("...").append(" 展开");
            // 活动样式
            elipseString.setSpan(activeClick, 0, active.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //给查看全部设置样式
            elipseString.setSpan(stateClick, elipseString.length() - 2, elipseString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置收起后的文本内容
            tv.setText(elipseString);
            //将textview设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            tv.setSelected(true);
            // 不设置没有点击效果
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            // 设置点击后背景为透明
            tv.setHighlightColor(tv.getContext().getResources().getColor(R.color.transparent));
        } else {
            //没有超过 直接设置文本
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(activeClick, 0, active.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
            // 不设置没有点击效果
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            // 设置点击后背景为透明
            tv.setHighlightColor(tv.getContext().getResources().getColor(R.color.transparent));
            if (onTextClickListener != null) {
                onTextClickListener.onOpenClose(true, true);
            }
        }
    }

}
