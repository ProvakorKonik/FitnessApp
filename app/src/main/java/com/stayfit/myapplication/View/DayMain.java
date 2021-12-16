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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.stayfit.myapplication.Adapter.DayAdapter;
import com.stayfit.myapplication.Model.DayModel;
import com.stayfit.myapplication.LoginCheck;
import com.stayfit.myapplication.LoginRegistration;
import com.stayfit.myapplication.R;
import com.stayfit.myapplication.RecylerviewClickInterface;
import com.stayfit.myapplication.ViewModel.DayVM;

import java.util.ArrayList;
import java.util.List;

public class DayMain extends AppCompatActivity implements RecylerviewClickInterface {

    private ImageView mUserImage, mAddDayImageBtn;
    private TextView mUserName, mUserWeightText;

    //RecyclerView
    private RecyclerView mDay_RecyclerView;
    List<DayModel> listDayItem = new ArrayList<>();;
    DayAdapter mDay_adapter;

    //Firebase Auth
    private String dUserUID = "NO",dUserEmail = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_main);
        mUserImage = (ImageView) findViewById(R.id.main_user_image);
        mAddDayImageBtn = (ImageView) findViewById(R.id.main_user_add_day);
        mUserName = (TextView)findViewById(R.id.main_user_name_txt) ;
        mUserWeightText = (TextView)findViewById(R.id.main_user_weight_txt) ;
        mDay_RecyclerView = (RecyclerView)findViewById(R.id.main_day_recyclerview) ;

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    dUserEmail = user.getEmail();
                    checkUserType();
                    callViewModel();
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                    startActivity(intent);
                }
            }
        };

        mAddDayImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DayAdd.class);
                startActivity(intent);
            }
        });

        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
    }

    //View Model
    private DayVM DayVM;
    private void callViewModel() {
        DayVM = new ViewModelProvider(this).get(DayVM.class);
        DayVM.DaysList().observe(this, new Observer<List<DayModel>>() {
            @Override
            public void onChanged(List<DayModel> day_model_list) {
                Log.d("ViewModel", "allViewModel:2 onChanged listview4 size = "+day_model_list.size());
                if (day_model_list.get(0).getDayName().equals("NULL")){
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
                    mDay_adapter = new DayAdapter(DayMain.this,day_model_list, DayMain.this);
                    mDay_adapter.notifyDataSetChanged();
                    //
                    listDayItem = day_model_list;

                    mDay_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                    //mDay_RecyclerView.setLayoutManager(new GridLayoutManager(DayMain.this,1));
                    mDay_RecyclerView.setAdapter(mDay_adapter);

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
                    String dUserName= documentSnapshot.getString( "name");
                    long diUserWeight= documentSnapshot.getLong( "weight");
                    String dUserPhotoURL = documentSnapshot.getString("photoURL");
                    mUserName.setText(dUserName);
                    //mUserWeightText.setText("Weight "+diUserWeight+"KG (12th Oct 2021)");
                    mUserWeightText.setText("Weight "+diUserWeight+"KG");
                    Picasso.get().load(dUserPhotoURL).fit().centerCrop().into(mUserImage);
                    if(dUserType.equals("Admin")){
                        mAddDayImageBtn.setVisibility(View.VISIBLE);
                    }else{
                        mAddDayImageBtn.setVisibility(View.GONE);

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Error User Information Not Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DayMain.this, LoginRegistration.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        });
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

    @Override
    public void onItemClick(int position) {
        String dsDayUID = listDayItem.get(position).getDayUID();
        String dsDayName = listDayItem.get(position).getDayName();
        String dsDayLevelName = listDayItem.get(position).getDayBeginnerLevel();
        String dsCoverPhotoURL = listDayItem.get(position).getDayPhotoUrl();
        long dlExerciseCount = listDayItem.get(position).getDayiTotalExercise();
        long dlDuration = listDayItem.get(position).getDayiDuration();

        Intent intent = new Intent(getApplicationContext(), DayOne.class);
        intent.putExtra("dsDayUID",dsDayUID);
        intent.putExtra("dsDayName",dsDayName);
        intent.putExtra("dsDayLevelName",dsDayLevelName);
        intent.putExtra("dsCoverPhotoURL",dsCoverPhotoURL);
        intent.putExtra("dlExerciseCount",dlExerciseCount);
        intent.putExtra("dlDuration",dlDuration);
        startActivity(intent);
    }

    @Override
    public void onAddQuesItemClick(int position) {

    }
}