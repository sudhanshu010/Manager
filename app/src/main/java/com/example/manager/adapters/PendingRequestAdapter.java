package com.example.manager.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.manager.R;
import com.example.manager.RatingActivity;
import com.example.manager.models.Complaint;
import com.example.manager.models.PastRecord;
import com.example.manager.models.Request;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PendingRequestAdapter extends FirebaseRecyclerPagingAdapter<Request, PendingRequestAdapter.MyHolder> {

    Context c;
    private final int[] mColors = {R.color.list_color_1,R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */


    public PendingRequestAdapter(DatabasePagingOptions<Request> options, Context c) {
        super(options);
        this.c = c;
    }



    @NonNull
    @Override
    public PendingRequestAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_item, null);
        return new PendingRequestAdapter.MyHolder(view);
    }

    protected void onBindViewHolder(PendingRequestAdapter.MyHolder viewHolder,int position,@NonNull Request model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
        viewHolder.cardView.setCardBackgroundColor(bgColor);
        viewHolder.bind(model);

    }

    protected  void  onLoadingStateChanged(LoadingState state) {

    }



    class MyHolder extends RecyclerView.ViewHolder {


        TextView serviceman1,requestid1,complain_id,description, accept_button,decline_button,RequestCost;

        String load;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference complaintReference,loadValue, mechComplaint, mechRequest,machineReference, faultMachineReference;
        CardView cardView;
        FirebaseAuth auth;
        FirebaseUser user;
        long faultMachine;

        Complaint complaint = null;
        Request request = null;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            requestid1 = itemView.findViewById(R.id.request_id1);
            serviceman1 = itemView.findViewById(R.id.serviceman1);
            complain_id = itemView.findViewById(R.id.complain_id);
            description = itemView.findViewById(R.id.description);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);
            cardView = itemView.findViewById(R.id.cardview12);
            RequestCost = itemView.findViewById(R.id.RequestCost);

        }

        public void  bind(final Request model)
        {
            serviceman1.setText(model.getComplaint().getMechanic().getUserName());
            requestid1.setText(String.valueOf(model.getRequestId()));
            complain_id.setText(String.valueOf(model.getComplaint().getComplaintId()));
            description.setText(model.getDescription());
            RequestCost.setText(String.valueOf(model.getCost()));
            float cost = model.getCost();


            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();

            machineReference = FirebaseDatabase.getInstance().getReference().child("Machines");
            complaintReference = FirebaseDatabase.getInstance().getReference("Complaints").child(String.valueOf(model.getComplaint().getComplaintId()));
            loadValue = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("load");
            mechComplaint = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("pendingComplaints").child(String.valueOf(model.getComplaint().getComplaintId()));
            mechRequest = FirebaseDatabase.getInstance().getReference("Users").child("Mechanic").child(model.getComplaint().getMechanic().getUid()).child("pendingRequests").child(String.valueOf(model.getRequestId()));
            faultMachineReference = FirebaseDatabase.getInstance().getReference("FaultMachines");

            faultMachineReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    faultMachine = (long) dataSnapshot.getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            loadValue.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    load = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mechComplaint.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    complaint = dataSnapshot.getValue(Complaint.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mechRequest.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    request = dataSnapshot.getValue(Request.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(model.isStatus())
            {


                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                //add data
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);



                //TODO: Check once
                //[ ReplacementAlgo Starts-->]




                complaintReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String complaintType;
                        int machineCode,serviceCount;
                        final String machineType;
                        final String dept;
                        complaintType = dataSnapshot.child("serviceType").getValue().toString();
                        machineCode = Integer.parseInt(dataSnapshot.child("machine").child("machineId").getValue().toString());
                        dept = dataSnapshot.child("machine").child("department").getValue().toString();
                        machineType = dataSnapshot.child("machine").child("type").getValue().toString();


                        //TODO: lets assume faultCost as 100, change after the input says

                        final float faultCost = cost;

                        if(complaintType.equals("Regular Service"))
                        {
                            Log.i("Replacement", "Regular Service Going in");

                            final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("comparisonMachine").child(dept).child(machineType).child(String.valueOf(machineCode));

                            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                int serviceCount;
                                float Tavg;
                                int sum;
                                int currentCost;
                                int buyCost;
                                int serviceTime;
                                int maxCostSum;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    serviceCount = Integer.parseInt(dataSnapshot.child("serviceCount").getValue().toString());
                                    buyCost = Integer.parseInt(dataSnapshot.child("buyCost").getValue().toString());
                                    serviceTime = Integer.parseInt(dataSnapshot.child("serviceTime").getValue().toString());
                                    sum = Integer.parseInt(dataSnapshot.child("sum").getValue().toString());
                                    maxCostSum = sum;
                                    currentCost = Integer.parseInt(dataSnapshot.child("extras").getValue().toString());

                                    sum += currentCost + faultCost;
                                    currentCost += faultCost;
                                    reference1.child("OverallCost").child(String.valueOf(serviceCount+1)).setValue(currentCost+faultCost);
                                    reference1.child("sum").setValue(String.valueOf(sum));
                                    reference1.child("extras").setValue("0");
                                    reference1.child("serviceCount").setValue(String.valueOf(serviceCount+1));
                                    reference1.child("faultCostPM").setValue(String.valueOf(sum/((serviceCount+1)*serviceTime)));
                                    final int faultCostPM = sum/((serviceCount+1)*serviceTime);
                                    Tavg = Float.valueOf(dataSnapshot.child("tavg").getValue().toString());


                                    String nextServiceTime = dataSnapshot.child("nextServiceTime").getValue().toString();
                                    String[] arrOfStr = nextServiceTime.split("/", 4);
                                    int date = Integer.parseInt(arrOfStr[0]);
                                    int month = Integer.parseInt((arrOfStr[1]));
                                    int year = Integer.parseInt(arrOfStr[2]);

                                    month += serviceTime;
                                    year += month/12;
                                    month %=12;

                                    String nextOne = String.valueOf(date)+'/'+String.valueOf(month)+'/'+String.valueOf(year);
                                    reference1.child("nextServiceTime").setValue(nextOne);

                                    if(Tavg<currentCost)
                                    {
                                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("ComparisonMachine").child(dept).child(machineType);

                                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {

                                            String best="none";
                                            @Override

                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                int fault1 = faultCostPM;
                                                for(DataSnapshot ndata : dataSnapshot.getChildren())
                                                {
                                                    String y = ndata.child("machineUID").toString();
                                                    int x =  Integer.parseInt(ndata.child("faultCostPM").toString());
                                                    if(x<fault1)
                                                    {
                                                        best = y;
                                                        fault1 = x;
                                                    }
                                                }

                                                Log.i("Replacement machine is working bad,change with ", best);

                                                String uid = user.getUid();
                                                Log.i("ankit uid",uid);
                                                FirebaseDatabase.getInstance().getReference("tokens").child(uid).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        String token = (String) dataSnapshot.getValue();
                                                        Log.i("ankit token fetch checking",token);

                                                        HashMap<String,String> data = new HashMap<>();
                                                        data.put("best",best);
                                                        data.put("token",token);

                                                        FirebaseFunctions firebaseFunctions;
                                                        firebaseFunctions = FirebaseFunctions.getInstance();
                                                        firebaseFunctions.getHttpsCallable("comparision")
                                                                .call(data)
                                                                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                                                                    @Override
                                                                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                                                                        HashMap<String,String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                                                        if(hashMap.get("status").equals("successful")){
                                                                            Log.d("ankit successful","notification successfully sent");
                                                                        }
                                                                        else{
                                                                            Log.d("ankit error occured",hashMap.get("status").toString());
                                                                        }
                                                                    }
                                                                });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            };

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                    else
                                    {
                                        int newTavg = (buyCost+sum)/serviceCount;
                                        reference1.child("tavg").setValue(newTavg);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                            Log.i("Replacement", "Regular Service Going out");
                        }
                        else
                        {
                            Log.i("Replacement", "BreakDown Going in");
                            final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("comparisonMachine").child(dept).child(machineType).child(String.valueOf(machineCode));

                            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int extra = Integer.parseInt(dataSnapshot.child("extras").getValue().toString());
                                    Log.i("Replacement old value",String.valueOf(extra));
                                    extra += faultCost;
                                    Log.i("Replacement new Value", String.valueOf(extra));
                                    reference1.child("extras").setValue(extra);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(c, "ho gya bhai sara kaam", Toast.LENGTH_SHORT).show();
                            Log.i("Replacement", "BreakDown Going in");
                        }
                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //[ReplacementAlgo end --> ]





                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/status",5);
                updateDatabaseValue.put("/FaultMachines", faultMachine-1);
                updateDatabaseValue.put("/Machines/" + model.getComplaint().getMachine().getMachineId() + "/working", true);

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId(),model.getComplaint());

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId(),complaint);

                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/status",5);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId(),request);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/load",Integer.parseInt(load)-1);
                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/mechanic/load",Integer.parseInt(load)-1);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/mechanic/load",Integer.parseInt(load)-1);



                // delete data
                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId(),null);
                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                final HashMap<String,Object> updateDatabaseValue1 = new HashMap<>();

                updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/status",5);

                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/status",5);

                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/complaint/completedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/complaint/status",5);

                updateDatabaseValue1.put("/Users/Manager/"+user.getUid()+"/completedComplaints/"+model.getComplaint().getComplaintId()+"/mechanic/load",Integer.parseInt(load)-1);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue1);

                String serviceDate;
                String serviceMan;
                String description;
                String complaintId;
                float cost;

                serviceDate = day+"/"+month+"/"+year;
                serviceMan = model.getComplaint().getMechanic().getUserName();
                description = model.getDescription();
                complaintId = String.valueOf(model.getComplaint().getComplaintId());
                cost = 2500;

                PastRecord pastRecord = new PastRecord(serviceDate, serviceMan, description, true, complaintId, cost);

                final String machineId = model.getComplaint().getMachine().getMachineId();

                Request tempPastRecord = null;
                try {
                    tempPastRecord = (Request) model.clone();
                } catch (CloneNotSupportedException e)
                {
                    e.printStackTrace();
                }

                if(tempPastRecord!=null)
                {
                    tempPastRecord.getComplaint().setManager(null);
                    tempPastRecord.getComplaint().setMachine(null);
                }

                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("/Machines/"+machineId+"/pastRecords/"+model.getRequestId(),tempPastRecord);
                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap);


                try {
                    tempPastRecord = (Request) model.clone();
                } catch (CloneNotSupportedException e)
                {
                    e.printStackTrace();
                }

                if(tempPastRecord!=null)
                {
                    Log.i("null h kya", "nhi");
                    tempPastRecord.setComplaint(null);
                }

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("/Users/Manager/"+user.getUid()+"/myMachines/"+machineId+"/pastRecords/"+model.getRequestId(),tempPastRecord);

                FirebaseDatabase.getInstance().getReference().updateChildren(hashMap1);


            }
            else
            {
                final HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);

                //add data
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/status",2);

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId(),request);

                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/status",2);


                // delete data

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                final HashMap<String,Object> updateDatabaseValue1 = new HashMap<>();

                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/approvedDate",day+"/"+month+"/"+year);
                updateDatabaseValue1.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/completedRequests/"+model.getRequestId()+"/complaint/status",2);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue1);

            }

                Intent i = new Intent(c, RatingActivity.class);
                i.putExtra("serviceManUid",model.getComplaint().getMechanic().getUid());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(i);
            }
        });

        decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> updateDatabaseValue = new HashMap<>();

                updateDatabaseValue.put("/Complaints/"+model.getComplaint().getComplaintId()+"/status",2);
                updateDatabaseValue.put("/Requests/"+model.getRequestId()+"/complaint/status",2);

                //updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                //updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                //updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId()+"/complaint/status",2);

                //updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId()+"/complaint/status",2);


                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingComplaints/"+model.getComplaint().getComplaintId()+"/status",2);

                // delete data

                updateDatabaseValue.put("/Users/Manager/"+user.getUid()+"/pendingRequests/"+model.getRequestId(),null);
                updateDatabaseValue.put("/Users/Mechanic/"+model.getComplaint().getMechanic().getUid()+"/pendingRequests/"+model.getRequestId(),null);

                FirebaseDatabase.getInstance().getReference().updateChildren(updateDatabaseValue);

                //send notification to mechanic for request declined

                FirebaseDatabase.getInstance().getReference("tokens").child(model.getComplaint().getMechanic().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String token = (String) dataSnapshot.getValue();
                        Log.i("ankit token fetch checking",token);

                        HashMap<String,String> data = new HashMap<>();
                        data.put("description","Your Request has been declined");
                        data.put("token",token);

                        FirebaseFunctions firebaseFunctions;
                        firebaseFunctions = FirebaseFunctions.getInstance();
                        firebaseFunctions.getHttpsCallable("requestDeclined")
                                .call(data)
                                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {
                                    @Override
                                    public void onSuccess(HttpsCallableResult httpsCallableResult) {
                                        HashMap<String,String> hashMap = (HashMap<String, String>) httpsCallableResult.getData();
                                        if(hashMap.get("status").equals("successful")){
                                            Log.d("ankit successful","notification successfully sent");
                                        }
                                        else{
                                            Log.d("ankit error occured",hashMap.get("status").toString());
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        }

    }

}