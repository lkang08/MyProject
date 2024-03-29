package com.lk.myproject.itemanimation;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ScaleItemAnimator extends BaseItemAnimator {
    @Override
    public void setRemoveAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        ViewCompat.setPivotX(holder.itemView,holder.itemView.getWidth()/2);
        ViewCompat.setPivotY(holder.itemView,holder.itemView.getHeight()/2);
        animator.scaleX(0).scaleY(0);
    }

    @Override
    public void removeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView,1);
        ViewCompat.setScaleY(holder.itemView,1);
    }

    @Override
    public void addAnimationInit(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView,0);
        ViewCompat.setScaleY(holder.itemView,0);
    }

    @Override
    public void setAddAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        ViewCompat.setPivotX(holder.itemView,holder.itemView.getWidth()/2);
        ViewCompat.setPivotY(holder.itemView,holder.itemView.getHeight()/2);
        animator.scaleX(1).scaleY(1);
    }

    @Override
    public void addAnimationCancel(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView,1);
        ViewCompat.setScaleY(holder.itemView,1);
    }

    @Override
    public void setOldChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        ViewCompat.setPivotX(holder.itemView,holder.itemView.getWidth()/2);
        ViewCompat.setPivotY(holder.itemView,holder.itemView.getHeight()/2);
        animator.scaleX(0).scaleY(0);
    }

    @Override
    public void oldChangeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView,1);
        ViewCompat.setScaleY(holder.itemView,1);
    }

    @Override
    public void newChangeAnimationInit(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView,0);
        ViewCompat.setScaleY(holder.itemView,0);
    }

    @Override
    public void setNewChangeAnimation(RecyclerView.ViewHolder holder, ViewPropertyAnimatorCompat animator) {
        ViewCompat.setPivotX(holder.itemView,holder.itemView.getWidth()/2);
        ViewCompat.setPivotY(holder.itemView,holder.itemView.getHeight()/2);
        animator.scaleX(1).scaleY(1);
    }

    @Override
    public void newChangeAnimationEnd(RecyclerView.ViewHolder holder) {
        ViewCompat.setScaleX(holder.itemView,1);
        ViewCompat.setScaleY(holder.itemView,1);
    }
}