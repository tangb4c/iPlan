package me.plan.comm.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/1/27.
 */
public class AutoBgImageView extends ImageView {

    public AutoBgImageView(Context context) {
        super(context);
    }

    public AutoBgImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoBgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        TLog.i("enter");
        SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(drawable);
        super.setImageDrawable(layer);
    }

    @Override
    public void setImageResource(int resId) {
        TLog.i("enter");
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        TLog.i("enter");
        super.setImageBitmap(bm);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        TLog.i("enter");
        super.setBackgroundDrawable(background);
    }

    @Override
    public void setBackground(Drawable background) {
        TLog.i("enter");
        super.setBackground(background);
    }

    protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {

        // The color filter to apply when the button is pressed
        protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
        // Alpha value when the button is disabled
        protected int _disabledAlpha = 100;

        public SAutoBgButtonBackgroundDrawable(Drawable d) {
            super(new Drawable[] { d });
            TLog.i("enter");
        }

        @Override
        protected boolean onStateChange(int[] states) {
            TLog.i("enter");
            boolean enabled = false;
            boolean pressed = false;

            for (int state : states) {
                if (state == android.R.attr.state_enabled)
                    enabled = true;
                else if (state == android.R.attr.state_pressed)
                    pressed = true;
            }

            mutate();
            if (enabled && pressed) {
                setColorFilter(_pressedFilter);
            } else if (!enabled) {
                setColorFilter(null);
                setAlpha(_disabledAlpha);
            } else {
                setColorFilter(null);
            }

            invalidateSelf();

            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }
}
