package com.example.manager.fragments;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.DialogBox.CustomDialogBox;
import com.example.manager.MainActivity;
import com.example.manager.MyMachinesActivity;
import com.example.manager.R;
import com.example.manager.SettingActivity;
import com.example.manager.adapters.MachineAdapter;
import com.example.manager.models.Machine;
import com.example.manager.models.Manager;
import com.example.manager.utilityclass.CircleTransform;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */


public class ProfileFragment extends Fragment {

    private Toolbar mTopToolbar;

    ImageView profilePicChange, profilePic;
    LinearLayout myMachine;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseUser user;
    UploadTask uploadTask;
    CustomDialogBox dialogBox;

    TextView name, email, phoneNumber;

    Button editButton;
    ImageView setting_imegeView;
    EditText nameEt,emailEt;
    Button save,cancel;
    ConstraintLayout profileLayout, profileEditLayout;

    RecyclerView recyclerView_machine;
    MachineAdapter machineAdapter;

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    LinearLayoutManager HorizontalLayout;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);


        final Toolbar toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.ic_account_tb);
        setHasOptionsMenu(true);

        profilePicChange = view.findViewById(R.id.rm_change_profile);
        profilePic = view.findViewById(R.id.profilepic);
        myMachine = view.findViewById(R.id.myMachine);
        editButton = view.findViewById(R.id.edit_button);
        profileLayout = view.findViewById(R.id.profile_layout);
        profileEditLayout = view.findViewById(R.id.profile_edit_layout);
        nameEt = view.findViewById(R.id.edit_profile_name);
        emailEt = view.findViewById(R.id.edit_profile_email);
        save = view.findViewById(R.id.save_edit_profile);
        cancel = view.findViewById(R.id.cancel_edit_profile);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity().getApplicationContext();
                AnimatorSet flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_out);
                AnimatorSet flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_in);
                flipIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        profileEditLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        profileLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                flipOut.setTarget(profileLayout);
                flipIn.setTarget(profileEditLayout);
                flipOut.start();
                flipIn.start();


            }
        });

        myMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplicationContext(), MyMachinesActivity.class);
                startActivity(intent);

            }
        });

        dialogBox = new CustomDialogBox(getActivity());
        dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogBox.show();

        user = FirebaseAuth.getInstance().getCurrentUser();

        name = view.findViewById(R.id.rm_profile_name);
        email = view.findViewById(R.id.rm_profile_email);
        phoneNumber = view.findViewById(R.id.rm_profile_phone);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child("Manager")
                .child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Picasso.get().load(manager.getProfilePicLink()).into(profilePic)
                name.setText((String) dataSnapshot.child("userName").getValue());
                email.setText((String) dataSnapshot.child("email").getValue());
                dialogBox.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid()+".jpg");

        profilePicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                Activity activity = getActivity();
////                if (activity != null)
//                getActivity().startActivityForResult(i, 12);

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).
                        setAspectRatio(1, 1).start(getContext(),ProfileFragment.this);
            }
        });

        //Horizontal recycler view
        recyclerView_machine=view.findViewById(R.id.recyclerView_machine);
        recyclerView_machine.setLayoutManager(new LinearLayoutManager(getActivity()));
        HorizontalLayout
                = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView_machine.setLayoutManager(HorizontalLayout);
        firebaseDatabase = FirebaseDatabase.getInstance();

        Query baseQuery = firebaseDatabase.getReference("Users").child("Manager").child(user.getUid()).child("myMachines");

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        DatabasePagingOptions<Machine> options = new DatabasePagingOptions.Builder<Machine>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery,config,Machine.class)
                .build();

        machineAdapter = new MachineAdapter(options,getActivity().getApplicationContext());
        recyclerView_machine.setAdapter(machineAdapter);
        machineAdapter.startListening();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getActivity().getApplicationContext();
                AnimatorSet flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_out);
                AnimatorSet flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.card_flip_in);
                flipIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        profileLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        profileEditLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                flipOut.setTarget(profileEditLayout);
                flipIn.setTarget(profileLayout);
                flipOut.start();
                flipIn.start();


            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("PRofile", "helo");
//        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null) {
//            Log.i("PRofile", "helo1");
//
//            //dialogBox.show();
//
//            Uri imageUri = data.getData();
//            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).
//                    setAspectRatio(1, 1).start(getActivity().getApplicationContext(), this);
//        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();

                profilePic.setDrawingCacheEnabled(true);
                profilePic.buildDrawingCache();

                Picasso.get().load(resultUri).transform(new CircleTransform()).into(profilePic, new Callback() {
                    @Override
                    public void onSuccess() {

                        ((BitmapDrawable) profilePic.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, baos);

                        Log.i("hello ankit", "ankit");
                        final byte[] image_data = baos.toByteArray();
                        uploadTask = storageReference.putBytes(image_data);

                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogBox.dismiss();
                                uploadTask.cancel();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageLink = uri.toString();

                                        HashMap<String, Object> updateProfilePic = new HashMap<>();

                                        updateProfilePic.put("/Users/Manager/" + user.getUid() + "/profilePicLink", imageLink);

                                        FirebaseDatabase.getInstance().getReference().updateChildren(updateProfilePic).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialogBox.dismiss();
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent= new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
