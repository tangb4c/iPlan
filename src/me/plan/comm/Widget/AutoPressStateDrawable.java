package me.plan.comm.Widget;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.TextView;
import me.plan.core.TLog;

/**
 * Created by tangb4c on 2015/1/28.
 */
public class AutoPressStateDrawable extends LayerDrawable {
    /**
     * Create a new layer drawable with the list of specified layers.
     *
     * @param layers A list of drawables to use as layers in this new drawable.
     */
    public AutoPressStateDrawable(Drawable[] layers) {
        super(layers);
    }
    TextView textView;
    int lastNormalColor = -1;
    ColorStateList colorStateList;
    public void attachTextView(TextView t){
        textView = t;
        colorStateList = t.getTextColors();
    }
    @Override
    protected boolean onStateChange(int[] states) {
        boolean _selected = false, _pressed = false;
        for (int state : states) {
            _pressed |= state == android.R.attr.state_pressed;
            _selected |= state == android.R.attr.state_selected;
        }
        if (_pressed) {
            setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            if(textView != null) {
                textView.setTextColor(Color.GRAY);
            }
        } else {
            clearColorFilter();
            if(textView != null)
                textView.setTextColor(colorStateList);
        }
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}
