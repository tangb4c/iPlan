package me.plan.HomePage.UI;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import me.plan.R;

public class MainActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.main);
        ImageView btnIcMore = (ImageView) findViewById(R.id.btn_ic_more);
        btnIcMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_ic_more:
            showIcMoreDialog();
            break;
        default:

        }
    }

    private void showIcMoreDialog() {
        final Dialog dialog = new Dialog(this, R.style.popupDialog);
        dialog.setContentView(R.layout.card_operation_dialog);
        dialog.setCanceledOnTouchOutside(true);
        View dialogClose = dialog.findViewById(R.id.card_dialog_close);
        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
