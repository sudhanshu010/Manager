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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manager.DialogBox.ComplaintDescriptionDialog;
import com.example.manager.adapters.ShowDetailsAdapter;
import com.example.manager.models.Complaint;
import com.example.manager.models.Machine;
import com.example.manager.models.Manager;
import com.example.manager.models.PastRecord;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static int[] MONTHS_SHORT = {0,
            R.string.january_short,R.string.february_short,R.string.march_short, R.string.april_short, R.string.may_short, R.string.june_short, R.string.july_short, R.string.august_short, R.string.september_short, R.string.october_short, R.string.november_short, R.string.december_short
    };
    private static String[] Colors = {"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722", "#795548", "#9E9E9E", "#607D8B"};
    FirebaseAuth auth;
    FirebaseUser user;

    String generationCode;
    Machine machine;

    private ShowDetailsAdapter showDetailsAdapter;
    LinearLayoutManager HorizontalLayout;
    ImageView QRCodeImage;
    TextView show_history;
    Button generateComplaint;

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

//        generateComplaint = findViewById(R.id.generateComplaint);
       show_history = findViewById(R.id.show_history);

        description = new String("");

        serialNo = findViewById(R.id.machineDetailsSerialNo);
        department = findViewById(R.id.machineDetailsDepartment);
        serviceTime = findViewById(R.id.machineDetailsServiceTime);
        dateOfInstallation = findViewById(R.id.machineDetailsInstallationDate);
        generator = findViewById(R.id.generator_name);


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

                //Toast.makeText(GetMachineDetailsActivity.this, machine.getDepartment(), Toast.LENGTH_SHORT).show();

                serialNo.setText(machine.getSerialNumber());
                department.setText(machine.getDepartment());
                serviceTime.setText(machine.getServiceTime()+" months");
                dateOfInstallation.setText(machine.getDateOfInstallation());
                //generator.setText(machine.getGeneratorName());
                machineId = machine.getMachineId();
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


             PagedList.Config config = new PagedList.Config.Builder()
                     .setEnablePlaceholders(false)
                     .setPrefetchDistance(10)
                     .setPageSize(20)
                     .build();

             DatabasePagingOptions<PastRecord> options = new DatabasePagingOptions.Builder<PastRecord>()
                     .setLifecycleOwner(this)
                     .setQuery(baseQuery1, config, PastRecord.class)
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



//
//        generateComplaint.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                complaint = new Complaint();
//                Manager tempManager = null;
//                try {
//                    tempManager = (Manager) machine.getManager().clone();
//                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
//                }
//                if (tempManager != null) {
//                    tempManager.setPendingComplaints(null);
//                }
//                complaint.setManager(tempManager);
//
//                Machine tempMachine = null;
//                try {
//                    tempMachine = (Machine) machine.clone();
//                    tempMachine.setManager(null);
//                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
//                }
//
//                complaint.setMachine(tempMachine);
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                month = month+1;
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                complaint.setGeneratedDate(day+"/"+month+"/"+year);
//                complaint.setStatus(Complaint.generatedOnly);
//
//                ComplaintDescriptionDialog complaintDescriptionDialog = new ComplaintDescriptionDialog(GetMachineDetailsActivity.this,complaint);
//                complaintDescriptionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                complaintDescriptionDialog.show();
//
//            }
//        });


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



        // sets a custom array of text

//        Handler delay = new Handler();
//        delay.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                cd1.startAnim();
//            }
//        },5000);



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
                        && cd1.getHeight()/2 <= rect.height()) {
                    cd1.showValue(75f, 100f,false);
                    cd2.showValue(90f, 100f, false);
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

        //Histogram

        final ColumnChartView chart = findViewById(R.id.chart);
        ColumnChartData columnChartData;
        final List<Column> columns;
        columns = new ArrayList<>();
        List<SubcolumnValue> subcolumnValues;
        int numColumns = 12;
        for (int i = 0; i < numColumns; i++) {
            subcolumnValues = new ArrayList<>();
            Random rand = new Random();
            int r= rand.nextInt(50);
            SubcolumnValue  value = new SubcolumnValue(
                    r,
                    GetRandomColor());
            subcolumnValues.add(value);
            Column column = new Column(subcolumnValues);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }

        columnChartData = new ColumnChartData(columns);

        Axis axisX = new Axis();
        List<AxisValue> axisValueList = new ArrayList<>();
        for (int i = 0; i < numColumns; i++) {
            axisValueList.add(new AxisValue(i)
                    .setLabel(GetMonthShort(i + 1)));
        }
        axisX.setValues(axisValueList);
        Axis axisY = new Axis().setHasLines(false);
        columnChartData.setAxisXBottom(axisX);
        columnChartData.setAxisYLeft(axisY);
        columnChartData.setStacked(true);
        chart.setColumnChartData(columnChartData);
        chart.setZoomEnabled(false);
        for (Column column : columnChartData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setTarget((float) Math.random() * 100);//some random target value
            }
        }

        final Runnable runnable1 = new Runnable() {
            public void run() {
                // code for checking component is on screen or not.
                Rect rect = new Rect();
                if(chart.getGlobalVisibleRect(rect)
                        && chart.getHeight()/2 <= rect.height()) {
                     chart.startDataAnimation(1500);
                }
                else
                {
                    handler.postDelayed(this, 1000);
                }

            }
        };
        handler.postDelayed(runnable1, 1);


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

