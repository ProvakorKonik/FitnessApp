package com.stayfit.myapplication.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stayfit.myapplication.Adapter.PerformAdapter;
import com.stayfit.myapplication.LoginCheck;
import com.stayfit.myapplication.LoginRegistration;
import com.stayfit.myapplication.Model.PerformModel;
import com.stayfit.myapplication.R;
import com.stayfit.myapplication.RecylerviewClickInterface;
import com.stayfit.myapplication.ViewModel.PerformVM;

import java.util.ArrayList;

public class DayOne extends AppCompatActivity implements RecylerviewClickInterface {

    private LinearLayout mCoverLinearLayout;
    private ImageView mCoverImage;
    private Button mDayOneAddBtn;

    private RecyclerView mContest_RecylcerView;
    public ArrayList<PerformModel> listPerformItem = new ArrayList<PerformModel>();;
    PerformAdapter mPerform_Adapter;
    
    private TextView mCoverDayText, mCoverLevelText, mExerciseCountTxt, mDurationCountTxt;

    private String dUserUID = "NO",dUserEmail = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_one);

        mCoverLinearLayout = (LinearLayout)findViewById(R.id.day_one_cover_linearLayout);
        mCoverImage = (ImageView)findViewById(R.id.day_one_cover_image);
        mDayOneAddBtn = (Button)findViewById(R.id.day_one_add);
        mCoverDayText = (TextView)findViewById(R.id.day_one_day_text);
        mCoverLevelText = (TextView)findViewById(R.id.day_one_level_text);
        mExerciseCountTxt = (TextView)findViewById(R.id.day_one_exercise_count_txt);
        mDurationCountTxt = (TextView)findViewById(R.id.day_one_exercise_duration_txt);
        mContest_RecylcerView = (RecyclerView)findViewById(R.id.day_one_recyclerview);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    checkUserType();
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        };

        getIntentMethod();
        mDayOneAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerformAdd.class);
                intent.putExtra("dsDayUID",dsDayUID);
                startActivity(intent);
            }
        });
    }

    private PerformVM performVM;
    private void callViewModel() {
        performVM = new ViewModelProvider(this).get(PerformVM.class);
        performVM.DayOnePerformanceList(dsDayUID).observe(this, new Observer<ArrayList<PerformModel>>() {
            @Override
            public void onChanged(ArrayList<PerformModel> perform_model_list) {
                if (perform_model_list.get(0).getExerciseName().equals("NULL")){
                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "No Items Found", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }else{
                    //Collections.reverse(listBook);
                    mPerform_Adapter = new PerformAdapter(DayOne.this,perform_model_list,DayOne.this);
                    mPerform_Adapter.notifyDataSetChanged();
                    //
                    listPerformItem = perform_model_list;
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mContest_RecylcerView.setLayoutManager(new GridLayoutManager(DayOne.this,2));
                        mContest_RecylcerView.setAdapter(mPerform_Adapter);
                    } else {
                        mContest_RecylcerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                        //mContest_RecylcerView.setLayoutManager(new GridLayoutManager(DayMain.this,1));
                        mContest_RecylcerView.setAdapter(mPerform_Adapter);
                    }
                }
            }
        });
    }
    public void checkUserType(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user_data_ref = db.collection("StayFit").document("Info");
        user_data_ref.collection("Normal_USER").document(dUserUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String dUserType = documentSnapshot.getString("userType");
                    if(dUserType.equals("Admin")){
                        mDayOneAddBtn.setVisibility(View.VISIBLE);
                    }else{
                        mDayOneAddBtn.setVisibility(View.GONE);

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error User Information Not Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DayOne.this, LoginRegistration.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
    }
    String dsDayUID= "NO",dsDayName = "NO", dsDayLevelName = "NO", dsCoverPhotoURL = "NO";
    long dlExerciseCount = 0, dlDuration = 0;
    private boolean intentFoundError = true;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            dsDayUID = intent.getExtras().getString("dsDayUID");
            dsDayName = intent.getExtras().getString("dsDayName");
            dsDayLevelName = intent.getExtras().getString("dsDayLevelName");
            dsCoverPhotoURL = intent.getExtras().getString("dsCoverPhotoURL");
            dlExerciseCount = intent.getExtras().getLong("dlExerciseCount",0);
            dlDuration = intent.getExtras().getLong("dlDuration");

            intentFoundError = CheckIntentMethod(dsDayUID);
            intentFoundError = CheckIntentMethod(dsDayName);
            intentFoundError = CheckIntentMethod(dsDayLevelName);
            intentFoundError = CheckIntentMethod(dsCoverPhotoURL);

            if(!intentFoundError){
                callViewModel();
                if(dlExerciseCount == 0)
                    mCoverLinearLayout.setVisibility(View.GONE);
                else{
                    mCoverDayText.setText(dsDayUID);
                    mCoverDayText.setText(dsDayName);
                    mCoverLevelText.setText(dsDayLevelName);
                    Picasso.get().load(dsCoverPhotoURL).fit().centerCrop().into(mCoverImage);
                    mExerciseCountTxt.setText(String.valueOf(dlExerciseCount));
                    mDurationCountTxt.setText(String.valueOf(dlDuration));
                }



            }else{
                Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
            }
        }else{
            dsDayUID = "NO";
            dsDayName = "NO";
            dsDayLevelName = "NO";
            dsCoverPhotoURL = "NO";
            Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean CheckIntentMethod(String dsTestIntent){
        if(TextUtils.isEmpty(dsTestIntent)) {
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent NULL  " , Toast.LENGTH_SHORT).show();
        }else if (dsTestIntent.equals("")){
            dsTestIntent= "NO";
            Toast.makeText(getApplicationContext(), "intent 404" , Toast.LENGTH_SHORT).show();
        }else{
            intentFoundError = false;
        }
        return intentFoundError;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), PerformDetails.class);
        intent.putExtra("PerformList", listPerformItem);
        intent.putExtra("PerformListPosition",(long) position);
        intent.putExtra("dsDayUID", "dsDayUID");

        startActivity(intent);

    }

    @Override
    public void onAddQuesItemClick(int position) {

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}