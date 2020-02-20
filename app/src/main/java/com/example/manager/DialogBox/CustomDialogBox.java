package com.example.manager.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.example.manager.R;

public class CustomDialogBox extends Dialog implements
        android.view.View.OnClickListener {


    public Dialog d;
    String filename;

    LottieAnimationView animationView;

    public CustomDialogBox(Activity a) {
        super(a);
        this.setCanceledOnTouchOutside(false);
        // TODO Auto-generated constructor stub

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);


    }

    @Override
    public void onClick(View v) {

    }
}
