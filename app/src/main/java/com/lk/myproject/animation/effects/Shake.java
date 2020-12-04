package com.lk.myproject.animation.effects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.nineoldandroids.view.ViewHelper;

/*
 * Copyright 2014 litao
 * https://github.com/sd6352051/NiftyDialogEffects
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Shake extends BaseEffects {

    @Override
    protected void setupAnimation(final View view) {
        ObjectAnimator a1 =
                ObjectAnimator.ofFloat(view, "translationY", -view.getHeight(), 0).setDuration(mDuration / 4);
        a1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        ObjectAnimator a2 = ObjectAnimator.ofFloat(view, "translationY", 0, .10f, -25, .26f, 25, .42f, -25,
                .58f, 25, .74f, -25, .90f, 1, 0).setDuration(mDuration);
        ObjectAnimator a3 =
                ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(mDuration * 3 / 2);
       /* getAnimatorSet().playSequentially(
            a1, a2
        );*/

        AnimSpring animSpring = AnimSpring.getInstance();
        animSpring.startCircleAnim(90, view);
        if (1 > 0) {
            return;
        }
        Spring spring = SpringSystem.create()
                .createSpring()
                .setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(80, 2))
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        super.onSpringUpdate(spring);
                        ViewHelper.setTranslationY(view, (float) spring.getCurrentValue());
                    }
                });
        spring.setCurrentValue(200);
        spring.setEndValue(0.0);
    }
}
