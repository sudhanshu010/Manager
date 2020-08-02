package com.example.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.manager.DialogBox.ComplaintDescriptionDialog;
import com.example.manager.adapters.MachineImageAdapter;
import com.example.manager.adapters.ShowDetailsAdapter;
import com.example.manager.adapters.ViewPagerAdapter;
import com.example.manager.models.Complaint;
import com.example.manager.models.Machine;
import com.example.manager.models.Manager;
import com.example.manager.models.PastRecord;
import com.example.manager.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class GetMachineDetailsActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference machineReference, complaintIdReference, serviceManListReference, responsibleReference,complaintReference;
    BarChart barChart;
    TextView AdvanceAge,AdvanceLifeCompleted,AdvanceCostIncurred,AdvanceNextServiceDate,AdvanceServiceCount;
    TextView more_details;

    public static int[] MONTHS_SHORT = {0,
            R.string.january_short,R.string.february_short,R.string.march_short, R.string.april_short, R.string.may_short, R.string.june_short, R.string.july_short, R.string.august_short, R.string.september_short, R.string.october_short, R.string.november_short, R.string.december_short
    };
    private static String[] Colors = {"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548", "#9E9E9E", "#607D8B"};
    FirebaseAuth auth;
    FirebaseUser user;

    public int[] bdds = {R.drawable.bdds,R.drawable.bdds1};
    public int[] eds = {R.drawable.eds,R.drawable.eds2};
    public int[] hhmd = {R.drawable.hhmd,R.drawable.hhmd1,R.drawable.hhmd2};
    public int[] xbis = {R.drawable.xbis,R.drawable.xbis1,R.drawable.xbis2,R.drawable.xbis3};
    public int[] dfmd = {R.drawable.dfmd1,R.drawable.dfmd2,R.drawable.dfmd3};
    public int[] fids = {R.drawable.fids,R.drawable.fids0,R.drawable.fids1,R.drawable.fids1,
                             R.drawable.fids2,R.drawable.fids3};

    private String[] mTitles;
    String generationCode;
    Machine machine;
    TextView NoMachineHistory;

    private ShowDetailsAdapter showDetailsAdapter;
    LinearLayoutManager HorizontalLayout;
    ImageView QRCodeImage;
    TextView show_history;
    ScrollView ScrollViewHistory;
    Button generateComplaint;
    TextView companyName,machineType,modelNumber,price;
    TextView serialNo,department,serviceTime,dateOfInstallation, generator;

    Complaint complaint;

    int complaintId;
    String description;
    String machineId;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_machine_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitles = getResources().getStringArray(R.array.machine_full_form);
        //Image Slider
        SliderView sliderView = findViewById(R.id.imageSlider);

        final MachineImageAdapter[] adapter = new MachineImageAdapter[6];
        adapter[0] = new MachineImageAdapter(this,new int[1]);

        sliderView.setSliderAdapter(adapter[0]);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.GATETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#275F73"));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);


        AdvanceAge = findViewById(R.id.AdvanceAge);
        AdvanceLifeCompleted = findViewById(R.id.AdvanceLifeCompleted);
        AdvanceCostIncurred = findViewById(R.id.AdvanceCostIncurred);
        AdvanceNextServiceDate = findViewById(R.id.AdvanceNextServiceDate);
        AdvanceServiceCount = findViewById(R.id.AdvanceServiceCount);

        generateComplaint = findViewById(R.id.generateComplaint);
       show_history = findViewById(R.id.show_history);
       NoMachineHistory = findViewById(R.id.xyz);
       ScrollViewHistory = (ScrollView) findViewById(R.id.scrollViewHistory);

        description = new String("");
        modelNumber = findViewById(R.id.machineModelNumber);
        price = findViewById(R.id.price);


        machineType= findViewById(R.id.machine_type);
        companyName = findViewById(R.id.company_name);
        serialNo = findViewById(R.id.machineDetailsSerialNo);
        department = findViewById(R.id.machineDetailsDepartment);
        serviceTime = findViewById(R.id.machineDetailsServiceTime);
        dateOfInstallation = findViewById(R.id.machineDetailsInstallationDate);
        generator = findViewById(R.id.generator_name);

        barChart = findViewById(R.id.barChart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(3000, Easing.EaseOutBack);
        Description description = barChart.getDescription();
        description.setEnabled(false);


        generationCode = getIntent().getStringExtra("generationCode");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        machineReference = firebaseDatabase.getReference("Machines").child(generationCode);
        QRCodeImage = findViewById(R.id.QrCodeImage);

        machineReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                machine = dataSnapshot.getValue(Machine.class);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String today = simpleDateFormat.format(new Date());
                String[] arrT = today.split("/",4);
                String[] arrS = machine.getDateOfInstallation().split("/",4);

                int age = Integer.parseInt(arrT[1])-Integer.parseInt(arrS[1]) + 12*(Integer.parseInt(arrT[2])-Integer.parseInt(arrS[2]));
                AdvanceAge.setText(String.valueOf(age)+" months");

                final double LifeC = (double)(age)/(double)(machine.getLife()*12);
                AdvanceLifeCompleted.setText(String.valueOf(LifeC));

                //Images according to machine type
                if(machine.getType().toLowerCase().equals("hhmd")){
                    machineType.setText(mTitles[3]);
                    adapter[1] = new MachineImageAdapter(GetMachineDetailsActivity.this,hhmd);
                    sliderView.setSliderAdapter(adapter[1]);
                }else  if(machine.getType().toLowerCase().equals("bdds")){
                    adapter[2] = new MachineImageAdapter(GetMachineDetailsActivity.this,bdds);
                    sliderView.setSliderAdapter(adapter[2]);
                    machineType.setText(mTitles[1]);
                }else  if(machine.getType().toLowerCase().equals("etd")){
                    adapter[3] = new MachineImageAdapter(GetMachineDetailsActivity.this,eds);
                    sliderView.setSliderAdapter(adapter[3]);
                    machineType.setText(mTitles[0]);
                }else  if(machine.getType().toLowerCase().equals("xbis")){
                    adapter[4] = new MachineImageAdapter(GetMachineDetailsActivity.this,xbis);
                    sliderView.setSliderAdapter(adapter[4]);
                    machineType.setText(mTitles[2]);
                }else  if(machine.getType().toLowerCase().equals("fids")){
                    adapter[5] = new MachineImageAdapter(GetMachineDetailsActivity.this,fids);
                    sliderView.setSliderAdapter(adapter[5]);
                    machineType.setText(mTitles[5]);
                }else if(machine.getType().toLowerCase().equals("dfmd")){
                    adapter[5] = new MachineImageAdapter(GetMachineDetailsActivity.this,dfmd);
                    sliderView.setSliderAdapter(adapter[5]);
                    machineType.setText(mTitles[4]);
                }
                 else{
                    adapter[5] = new MachineImageAdapter(GetMachineDetailsActivity.this,xbis);
                    sliderView.setSliderAdapter(adapter[5]);
                    machineType.setText(machine.getType());
                }



                serialNo.setText(machine.getSerialNumber());
                department.setText(machine.getDepartment());
                serviceTime.setText(machine.getServiceTime()+" months");
                dateOfInstallation.setText(machine.getDateOfInstallation());
                generator.setText(machine.getManager().getUserName());
                companyName.setText(machine.getCompany());
                price.setText(String.valueOf(machine.getPrice()));
                modelNumber.setText(machine.getModelNumber());
                machineId = machine.getMachineId();

                final String manager1 = machine.getManager().getUserName().toString();

                final DatabaseReference reference2 = firebaseDatabase.getReference().child("Users").child("Manager").child(user.getUid());
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String realManager = dataSnapshot.child("userName").getValue().toString();
                        if(!realManager.equals(manager1))
                        {
                            generateComplaint.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(!machine.isWorking())
                {
                    generateComplaint.setEnabled(false);
                    generateComplaint.setText("complaint generated already");
                }
                else
                {
                    generateComplaint.setEnabled(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        recyclerView=findViewById(R.id.machine_history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HorizontalLayout
                = new LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);

        firebaseDatabase = FirebaseDatabase.getInstance();
             Query baseQuery1 = firebaseDatabase.getReference("Machines").child(generationCode).child("pastRecords");

             DatabaseReference reference1 = firebaseDatabase.getReference().child("Machines").child(generationCode).child("pastRecords");
             reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     if(!dataSnapshot.exists())
                     {
                         show_history.setVisibility(View.GONE);
                         ScrollViewHistory.setVisibility(View.GONE);
                         recyclerView.setVisibility(View.GONE);
                         NoMachineHistory.setVisibility(View.VISIBLE);
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

             PagedList.Config config = new PagedList.Config.Builder()
                     .setEnablePlaceholders(false)
                     .setPrefetchDistance(10)
                     .setPageSize(20)
                     .build();

             DatabasePagingOptions<Request> options = new DatabasePagingOptions.Builder<Request>()
                     .setLifecycleOwner(this)
                     .setQuery(baseQuery1, config, Request.class)
                     .build();

             showDetailsAdapter = new ShowDetailsAdapter(options, GetMachineDetailsActivity.this);
             recyclerView.setAdapter(showDetailsAdapter);
             showDetailsAdapter.startListening();

        show_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetMachineDetailsActivity.this, ShowDetailsActivity.class);
                i.putExtra("machine_id",machineId);
                startActivity(i);
            }
        });


        generateComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                complaint = new Complaint();
                Manager tempManager = null;
                try {
                    tempManager = (Manager) machine.getManager().clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                if (tempManager != null) {
                    tempManager.setPendingComplaints(null);
                    tempManager.setPendingApprovalRequest(null);
                    tempManager.setCompletedComplaints(null);
                    tempManager.setMyMachines(null);
                }
                complaint.setManager(tempManager);

                Machine tempMachine = null;
                try {
                    tempMachine = (Machine) machine.clone();
                    tempMachine.setManager(null);
                    tempMachine.setPastRecordList(null);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                complaint.setMachine(tempMachine);
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                complaint.setGeneratedDate(day+"/"+month+"/"+year);
                complaint.setStatus(Complaint.generatedOnly);

                ComplaintDescriptionDialog complaintDescriptionDialog = new ComplaintDescriptionDialog(GetMachineDetailsActivity.this,complaint);
                complaintDescriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                complaintDescriptionDialog.show();

            }
        });


        //Circle Display
        final CircleDisplay cd1 = (CircleDisplay)findViewById(R.id.circle_display1);
        cd1.setAnimDuration(1500);
        cd1.setValueWidthPercent(5f);
        cd1.setTextSize(18f);
        cd1.setColor(R.color.list_color_10);
        cd1.setDrawText(true);
        cd1.setDrawInnerCircle(true);
        cd1.setFormatDigits(1);
        cd1.setTouchEnabled(false);
        cd1.setUnit("%");
        cd1.setStepSize(0.5f);


        final Handler handler = new Handler();

        final CircleDisplay cd2 = (CircleDisplay)findViewById(R.id.circle_display2);
        cd2.setAnimDuration(1500);
        cd2.setValueWidthPercent(5f);
        cd2.setTextSize(18f);
        cd2.setColor(Color.RED);
        cd2.setDrawText(true);
        cd2.setDrawInnerCircle(true);
        cd2.setFormatDigits(1);
        cd2.setTouchEnabled(false);
        cd2.setUnit("%");
        cd2.setStepSize(0.5f);
        // sets a custom array of text



        // continuously check its circle is completely on screen or not. If yes, start the animation.
        final Runnable runnable = new Runnable() {
            public void run() {
                // code for checking component is on screen or not.
                Rect rect = new Rect();
                if(cd1.getGlobalVisibleRect(rect)
                        && cd1.getHeight()/2 <= rect.height())
                {
                    DatabaseReference reference2 = firebaseDatabase.getReference().child("comparisonMachine").child(machine.getDepartment()).child(machine.getType()).child(machine.getMachineId());
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        ArrayList<BarEntry>barEntries = new ArrayList<>();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            AdvanceCostIncurred.setText(dataSnapshot.child("sum").getValue().toString());
                            AdvanceNextServiceDate.setText(dataSnapshot.child("nextServiceTime").getValue().toString());
                            AdvanceServiceCount.setText(dataSnapshot.child("serviceCount").getValue().toString());

                            //Pie graph
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String today = simpleDateFormat.format(new Date());
                            String[] arrT = today.split("/",4);
                            String[] arrS = machine.getDateOfInstallation().split("/",4);

                            int age = Integer.parseInt(arrT[0])-Integer.parseInt(arrS[0])+12*Integer.parseInt(arrT[1])-Integer.parseInt(arrS[1]) + 365*(Integer.parseInt(arrT[2])-Integer.parseInt(arrS[2]));
                            float LifeC = (float)(age)/(float)(machine.getLife()*365);
                            LifeC *= 100;

                            int a = Integer.valueOf(dataSnapshot.child("sum").getValue().toString());
                            int serviceCount = Integer.valueOf(dataSnapshot.child("serviceCount").getValue().toString());
                            Float ExpectedLife = machine.getLife();
                            int serviceTime = machine.getServiceTime();
                            Float ans1 = LifeC;


                            Float buyCost = machine.getPrice();
                            Float ans2 = (a*100f)/buyCost;

                            cd1.showValue(ans1, 100f,false);
                            cd2.showValue(ans2, 100f, false);

                            //Bar graph
                            int x = serviceCount;
                            for(int i =0 ;i<=x;i++)
                            {
                                barEntries.add(new BarEntry(i+1,Integer.valueOf(dataSnapshot.child("OverallCost").child(String.valueOf(i)).getValue().toString())));
                            }
                            BarDataSet barDataSet = new BarDataSet(barEntries,"");
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData data = new BarData(barDataSet);
                            data.setBarWidth(0.9f);
                            barChart.setData(data);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    cd1.startAnim();
                    cd2.startAnim();
                }
                else
                {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(runnable, 1);



    }


    private static String lastColor0, lastColor1, lastColor2;
    public static int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        while (Colors[p].equals(lastColor0)
                || Colors[p].equals(lastColor1)
                || Colors[p].equals(lastColor2)) {
            p = random.nextInt(Colors.length);
        }
        lastColor0 = lastColor1;
        lastColor1 = lastColor2;
        lastColor2 = Colors[p];
        return Color.parseColor(Colors[p]);
    }
    public String GetMonthShort(int i) {
        return this.getResources().getString(MONTHS_SHORT[i]);
    }
}

