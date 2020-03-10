package com.example.manager.DialogBox;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.manager.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    Button cancel,save;
    ImageView qrCode;
    long generationCode;
    Bitmap bitmap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.bottom_sheet,container,false);
        cancel = view.findViewById(R.id.cancel);
        save = view.findViewById(R.id.save);
        qrCode = view.findViewById(R.id.qr_code);
        qrCode.setImageBitmap(bitmap);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImage(generationCode,bitmap);
                Objects.requireNonNull(getActivity()).finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Objects.requireNonNull(getActivity()).finish();

            }
        });
        return view;
    }

    public BottomSheetDialog(long generationCode,Bitmap bitmap) {
        this.generationCode = generationCode;
        this.bitmap = bitmap;
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
