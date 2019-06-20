package com.github.javiersantos.materialstyleddialogs;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

class UtilsAnimation {

    static void popUp(View scalingView, float initScale) {
        // create scaleX and scaleY animations
        SpringAnimation scaleXAnimation = createSpringAnimation(
                scalingView, SpringAnimation.SCALE_X,
                1f);//, STIFFNESS, DAMPING_RATIO);
        SpringAnimation scaleYAnimation = createSpringAnimation(
                scalingView, SpringAnimation.SCALE_Y,
                1f);//, STIFFNESS, DAMPING_RATIO);

        scalingView.setScaleX(initScale);
        scalingView.setScaleY(initScale);
        scaleXAnimation.start();
        scaleYAnimation.start();

//        scalingView.setAlpha(0.5f);
//        scalingView.animate().alpha(1f).setDuration(300).start();
    }

    private static SpringAnimation createSpringAnimation(View view, DynamicAnimation.ViewProperty property, float finalPosition) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce spring = new SpringForce(finalPosition);
        spring.setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        spring.setDampingRatio(0.5f);
        animation.setSpring(spring);
        return animation;
    }

    static void zoomInAndOutAnimation(Context context, final ImageView image) {
        Animation zoomInAnimation = AnimationUtils.loadAnimation(context, R.anim.md_styled_zoom_in);
        final Animation zoomOutAnimation = AnimationUtils.loadAnimation(context, R.anim.md_styled_zoom_out);

        zoomInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.startAnimation(zoomOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        image.startAnimation(zoomInAnimation);
    }

}
