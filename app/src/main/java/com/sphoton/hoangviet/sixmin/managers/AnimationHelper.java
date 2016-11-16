package com.sphoton.hoangviet.sixmin.managers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;


/**
 * Created by hoangviet on 9/14/16.
 */
public class AnimationHelper {
    public static void viewGone(final View view){
        view.clearAnimation();
        view.animate().translationY(-50)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }
    public static void viewVisible(View view){
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.setTranslationY(-50);
        view.animate().translationY(0).setListener(null).alpha(1.0f);
    }
    public static void listViewSlideIn(View view, final View view2){
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.setTranslationY(-500);
        view.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view2.setVisibility(View.GONE);
            }
        }).alpha(1.0f);
    }
    public static void listViewSlideOut(final View view, View view2){
        view.clearAnimation();
        view.animate().translationY(-500)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }
}
