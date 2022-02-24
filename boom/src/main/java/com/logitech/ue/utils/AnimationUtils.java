// 
// Decompiled by Procyon v0.5.36
// 

package com.logitech.ue.utils;

import java.util.Iterator;
import android.animation.AnimatorSet;
import android.content.res.Resources;
import android.content.Context;
import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;

public class AnimationUtils
{
    public static void attacheAnimatorToView(@NonNull final View view, final Animator animator) {
        attacheAnimatorToView(view, animator, null);
    }
    
    public static void attacheAnimatorToView(@NonNull final View view, final Animator animator, final Object[] array) {
        view.setTag(2131623940, (Object)animator);
        view.setTag(2131623941, (Object)array);
    }
    
    public static void detachAnimatorFromView(@NonNull final View view) {
        view.setTag(2131623940, (Object)null);
        view.setTag(2131623941, (Object)null);
    }
    
    public static void detachAnimatorFromView(@NonNull final View view, @NonNull final Animator animator) {
        if (view.getTag(2131623940) == animator) {
            detachAnimatorFromView(view);
        }
    }
    
    public static int getAndroidLongAnimationTime(final Context context) {
        int androidLongAnimationTime;
        if (context != null) {
            androidLongAnimationTime = getAndroidLongAnimationTime(context.getResources());
        }
        else {
            androidLongAnimationTime = 600;
        }
        return androidLongAnimationTime;
    }
    
    public static int getAndroidLongAnimationTime(final Resources resources) {
        int integer;
        if (resources != null) {
            integer = resources.getInteger(17694722);
        }
        else {
            integer = 600;
        }
        return integer;
    }
    
    public static int getAndroidMediumAnimationTime(final Context context) {
        int androidMediumAnimationTime;
        if (context != null) {
            androidMediumAnimationTime = getAndroidMediumAnimationTime(context.getResources());
        }
        else {
            androidMediumAnimationTime = 400;
        }
        return androidMediumAnimationTime;
    }
    
    public static int getAndroidMediumAnimationTime(final Resources resources) {
        int integer;
        if (resources != null) {
            integer = resources.getInteger(17694721);
        }
        else {
            integer = 400;
        }
        return integer;
    }
    
    public static int getAndroidShortAnimationTime(final Context context) {
        int androidShortAnimationTime;
        if (context != null) {
            androidShortAnimationTime = getAndroidShortAnimationTime(context.getResources());
        }
        else {
            androidShortAnimationTime = 200;
        }
        return androidShortAnimationTime;
    }
    
    public static int getAndroidShortAnimationTime(final Resources resources) {
        int integer;
        if (resources != null) {
            integer = resources.getInteger(17694720);
        }
        else {
            integer = 200;
        }
        return integer;
    }
    
    public static Animator getAnimator(@NonNull final View view) {
        final Object tag = view.getTag(2131623940);
        Animator animator;
        if (tag instanceof Animator) {
            animator = (Animator)tag;
        }
        else {
            animator = null;
        }
        return animator;
    }
    
    public static Object[] getAnimatorValue(@NonNull final View view) {
        final Object tag = view.getTag(2131623941);
        Object[] array;
        if (tag instanceof Object[]) {
            array = (Object[])tag;
        }
        else {
            array = null;
        }
        return array;
    }
    
    public static boolean isRunningAnimation(@NonNull final View view) {
        final Animator animator = getAnimator(view);
        return animator != null && (animator.isStarted() || animator.isRunning());
    }
    
    public static void stopAnimation(@NonNull final View view) {
        final Animator animator = getAnimator(view);
        if (animator != null) {
            stopAnimator(animator);
            view.setTag(2131623940, (Object)null);
        }
    }
    
    public static void stopAnimator(@NonNull final Animator animator) {
        if (!animator.isStarted()) {
            animator.start();
        }
        if (animator instanceof AnimatorSet) {
            final Iterator<Animator> iterator = ((AnimatorSet)animator).getChildAnimations().iterator();
            while (iterator.hasNext()) {
                stopAnimator(iterator.next());
            }
        }
        if (animator.isRunning()) {
            animator.end();
        }
    }
}
