package hikaru.moreorless.Util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hikaru.moreorless.R;

public class ButtonShift extends Button {
    private ButtonShift.Padding mPadding;
    private int mHeight;
    private int mWidth;
    private int mColor;
    private int mCornerRadius;
    private int mStrokeWidth;
    private int mStrokeColor;
    protected boolean mAnimationInProgress;
    private StrokeGradientDrawable mDrawableNormal;
    private StrokeGradientDrawable mDrawablePressed;

    public ButtonShift(Context context) {
        super(context);
        this.initView();
    }

    public ButtonShift(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    public ButtonShift(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(this.mHeight == 0 && this.mWidth == 0 && w != 0 && h != 0) {
            this.mHeight = this.getHeight();
            this.mWidth = this.getWidth();
        }

    }

    public StrokeGradientDrawable getDrawableNormal() {
        return this.mDrawableNormal;
    }

    public void morph(@NonNull ButtonShift.Params params) {
        if(!this.mAnimationInProgress) {
            this.mDrawablePressed.setColor(params.colorPressed);
            this.mDrawablePressed.setCornerRadius((float)params.cornerRadius);
            this.mDrawablePressed.setStrokeColor(params.strokeColor);
            this.mDrawablePressed.setStrokeWidth(params.strokeWidth);
            if(params.duration == 0) {
                this.morphWithoutAnimation(params);
            } else {
                this.morphWithAnimation(params);
            }

            this.mColor = params.color;
            this.mCornerRadius = params.cornerRadius;
            this.mStrokeWidth = params.strokeWidth;
            this.mStrokeColor = params.strokeColor;
        }

    }

    private void morphWithAnimation(@NonNull final ButtonShift.Params params) {
        this.mAnimationInProgress = true;
        this.setText((CharSequence)null);
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        this.setPadding(this.mPadding.left, this.mPadding.top, this.mPadding.right, this.mPadding.bottom);
        ShiftingAnimation.Params animationParams = ShiftingAnimation.Params.create(this).color(this.mColor, params.color).cornerRadius(this.mCornerRadius, params.cornerRadius).strokeWidth(this.mStrokeWidth, params.strokeWidth).strokeColor(this.mStrokeColor, params.strokeColor).height(this.getHeight(), params.height).width(this.getWidth(), params.width).duration(params.duration).listener(new ShiftingAnimation.Listener() {
            public void onAnimationEnd() {
                ButtonShift.this.finalizeMorphing(params);
            }
        });
        ShiftingAnimation animation = new ShiftingAnimation(animationParams);
        animation.start();
    }

    private void morphWithoutAnimation(@NonNull ButtonShift.Params params) {
        this.mDrawableNormal.setColor(params.color);
        this.mDrawableNormal.setCornerRadius((float)params.cornerRadius);
        this.mDrawableNormal.setStrokeColor(params.strokeColor);
        this.mDrawableNormal.setStrokeWidth(params.strokeWidth);
        if(params.width != 0 && params.height != 0) {
            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            layoutParams.width = params.width;
            layoutParams.height = params.height;
            this.setLayoutParams(layoutParams);
        }

        this.finalizeMorphing(params);
    }

    private void finalizeMorphing(@NonNull ButtonShift.Params params) {
        this.mAnimationInProgress = false;
        if(params.icon != 0 && params.text != null) {
            this.setIconLeft(params.icon);
            this.setText(params.text);
        } else if(params.icon != 0) {
            this.setIcon(params.icon);
        } else if(params.text != null) {
            this.setText(params.text);
        }

        if(params.animationListener != null) {
            params.animationListener.onAnimationEnd();
        }

    }

    public void blockTouch() {
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void unblockTouch() {
        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void initView() {
        this.mPadding = new ButtonShift.Padding();
        this.mPadding.left = this.getPaddingLeft();
        this.mPadding.right = this.getPaddingRight();
        this.mPadding.top = this.getPaddingTop();
        this.mPadding.bottom = this.getPaddingBottom();
        Resources resources = this.getResources();
        int cornerRadius = (int)resources.getDimension(R.dimen.corner);
        int blue = ContextCompat.getColor(getContext(), R.color.md_grey_50);
        int blueDark = ContextCompat.getColor(getContext(), R.color.md_grey_100);
        StateListDrawable background = new StateListDrawable();
        this.mDrawableNormal = this.createDrawable(blue, cornerRadius, 0);
        this.mDrawablePressed = this.createDrawable(blueDark, cornerRadius, 0);
        this.mColor = blue;
        this.mStrokeColor = blue;
        this.mCornerRadius = cornerRadius;
        background.addState(new int[]{16842919}, this.mDrawablePressed.getGradientDrawable());
        background.addState(StateSet.WILD_CARD, this.mDrawableNormal.getGradientDrawable());
        this.setBackgroundCompat(background);
    }

    private StrokeGradientDrawable createDrawable(int color, int cornerRadius, int strokeWidth) {
        StrokeGradientDrawable drawable = new StrokeGradientDrawable(new GradientDrawable());
        drawable.getGradientDrawable().setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius((float)cornerRadius);
        drawable.setStrokeColor(color);
        drawable.setStrokeWidth(strokeWidth);
        return drawable;
    }

    private void setBackgroundCompat(@Nullable Drawable drawable) {
        if(Build.VERSION.SDK_INT <= 16) {
            this.setBackgroundDrawable(drawable);
        } else {
            this.setBackground(drawable);
        }

    }

    public void setIcon(@DrawableRes final int icon) {
        this.post(new Runnable() {
            public void run() {
                Drawable drawable = ButtonShift.this.getResources().getDrawable(icon);
                int padding = ButtonShift.this.getWidth() / 2 - drawable.getIntrinsicWidth() / 2;
                ButtonShift.this.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
                ButtonShift.this.setPadding(padding, 0, 0, 0);
            }
        });
    }

    public void setIconLeft(@DrawableRes int icon) {
        this.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
    }

    public static class Params {
        private int cornerRadius;
        private int width;
        private int height;
        private int color;
        private int colorPressed;
        private int duration;
        private int icon;
        private int strokeWidth;
        private int strokeColor;
        private String text;
        private ShiftingAnimation.Listener animationListener;

        private Params() {
        }

        public static ButtonShift.Params create() {
            return new ButtonShift.Params();
        }

        public ButtonShift.Params text(@NonNull String text) {
            this.text = text;
            return this;
        }

        public ButtonShift.Params icon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        public ButtonShift.Params cornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        public ButtonShift.Params width(int width) {
            this.width = width;
            return this;
        }

        public ButtonShift.Params height(int height) {
            this.height = height;
            return this;
        }

        public ButtonShift.Params color(int color) {
            this.color = color;
            return this;
        }

        public ButtonShift.Params colorPressed(int colorPressed) {
            this.colorPressed = colorPressed;
            return this;
        }

        public ButtonShift.Params duration(int duration) {
            this.duration = duration;
            return this;
        }

        public ButtonShift.Params strokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public ButtonShift.Params strokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public ButtonShift.Params animationListener(ShiftingAnimation.Listener animationListener) {
            this.animationListener = animationListener;
            return this;
        }
    }

    private class Padding {
        public int left;
        public int right;
        public int top;
        public int bottom;

        private Padding() {
        }
    }
}