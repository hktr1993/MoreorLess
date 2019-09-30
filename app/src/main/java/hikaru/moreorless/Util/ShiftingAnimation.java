package hikaru.moreorless.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public class ShiftingAnimation {
    private ShiftingAnimation.Params mParams;

    public ShiftingAnimation(@NonNull ShiftingAnimation.Params params) {
        this.mParams = params;
    }


    public void start() {
        StrokeGradientDrawable background = this.mParams.button.getDrawableNormal();
        ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(background, "cornerRadius", new float[]{this.mParams.fromCornerRadius, this.mParams.toCornerRadius});
        ObjectAnimator strokeWidthAnimation = ObjectAnimator.ofInt(background, "strokeWidth", new int[]{this.mParams.fromStrokeWidth, this.mParams.toStrokeWidth});
        ObjectAnimator strokeColorAnimation = ObjectAnimator.ofInt(background, "strokeColor", new int[]{this.mParams.fromStrokeColor, this.mParams.toStrokeColor});
        strokeColorAnimation.setEvaluator(new ArgbEvaluator());
        ObjectAnimator bgColorAnimation = ObjectAnimator.ofInt(background, "color", new int[]{this.mParams.fromColor, this.mParams.toColor});
        bgColorAnimation.setEvaluator(new ArgbEvaluator());
        ValueAnimator heightAnimation = ValueAnimator.ofInt(new int[]{this.mParams.fromHeight, this.mParams.toHeight});
        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = ((Integer)valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = ShiftingAnimation.this.mParams.button.getLayoutParams();
                layoutParams.height = val;
                ShiftingAnimation.this.mParams.button.setLayoutParams(layoutParams);
            }
        });
        ValueAnimator widthAnimation = ValueAnimator.ofInt(new int[]{this.mParams.fromWidth, this.mParams.toWidth});
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = ((Integer)valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = ShiftingAnimation.this.mParams.button.getLayoutParams();
                layoutParams.width = val;
                ShiftingAnimation.this.mParams.button.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration((long)this.mParams.duration);
        animatorSet.playTogether(new Animator[]{strokeWidthAnimation, strokeColorAnimation, cornerAnimation, bgColorAnimation, heightAnimation, widthAnimation});
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if(ShiftingAnimation.this.mParams.animationListener != null) {
                    ShiftingAnimation.this.mParams.animationListener.onAnimationEnd();
                }

            }
        });
        animatorSet.start();
    }

    public static class Params {
        private float fromCornerRadius;
        private float toCornerRadius;
        private int fromHeight;
        private int toHeight;
        private int fromWidth;
        private int toWidth;
        private int fromColor;
        private int toColor;
        private int duration;
        private int fromStrokeWidth;
        private int toStrokeWidth;
        private int fromStrokeColor;
        private int toStrokeColor;
        private ButtonShift button;
        private ShiftingAnimation.Listener animationListener;

        private Params(@NonNull ButtonShift button) {
            this.button = button;
        }

        public static ShiftingAnimation.Params create(@NonNull ButtonShift button) {
            return new ShiftingAnimation.Params(button);
        }

        public ShiftingAnimation.Params duration(int duration) {
            this.duration = duration;
            return this;
        }

        public ShiftingAnimation.Params listener(@NonNull ShiftingAnimation.Listener animationListener) {
            this.animationListener = animationListener;
            return this;
        }

        public ShiftingAnimation.Params color(int fromColor, int toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
            return this;
        }

        public ShiftingAnimation.Params cornerRadius(int fromCornerRadius, int toCornerRadius) {
            this.fromCornerRadius = (float)fromCornerRadius;
            this.toCornerRadius = (float)toCornerRadius;
            return this;
        }

        public ShiftingAnimation.Params height(int fromHeight, int toHeight) {
            this.fromHeight = fromHeight;
            this.toHeight = toHeight;
            return this;
        }

        public ShiftingAnimation.Params width(int fromWidth, int toWidth) {
            this.fromWidth = fromWidth;
            this.toWidth = toWidth;
            return this;
        }

        public ShiftingAnimation.Params strokeWidth(int fromStrokeWidth, int toStrokeWidth) {
            this.fromStrokeWidth = fromStrokeWidth;
            this.toStrokeWidth = toStrokeWidth;
            return this;
        }

        public ShiftingAnimation.Params strokeColor(int fromStrokeColor, int toStrokeColor) {
            this.fromStrokeColor = fromStrokeColor;
            this.toStrokeColor = toStrokeColor;
            return this;
        }
    }

    public interface Listener {
        void onAnimationEnd();
    }
}
