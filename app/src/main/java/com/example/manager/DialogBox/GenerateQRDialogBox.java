package com.example.manager.DialogBox;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.manager.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateQRDialogBox extends Dialog implements View.OnClickListener {

    Activity activity;
    long generationCode;
    Bitmap qrCodeImage;
    ImageView qrCodeImageView;
    Button saveButton;
    TextView cancelText;

    public GenerateQRDialogBox(@NonNull Context context) {
        super(context);
    }

    public GenerateQRDialogBox(Activity activity, long generationCode, Bitmap qrCodeImage)
    {
        super(activity);
        this.setCanceledOnTouchOutside(false);

        this.activity = activity;
        this.generationCode = generationCode;
        this.qrCodeImage = qrCodeImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_generate_dialog);

        qrCodeImageView = this.findViewById(R.id.qr_code_image);
        saveButton = this.findViewById(R.id.save_button);
        cancelText = this.findViewById(R.id.cancel_text);

        qrCodeImageView.setImageBitmap(qrCodeImage);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(generationCode,qrCodeImage);
                dismiss();
                activity.finish();
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                activity.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void saveImage(long generationCode,Bitmap bitmap) {

        FileOutputStream outStream = null;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/QRcode");
        dir.mkdirs();
        File outFile = new File(dir, generationCode +".jpg");
        try {
            outStream = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        //Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        try {
            assert outStream != null;
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
