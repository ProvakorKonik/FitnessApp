package com.stayfit.myapplication.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stayfit.myapplication.LoginCheck;
import com.stayfit.myapplication.Model.PerformModel;
import com.stayfit.myapplication.R;

import java.util.ArrayList;

public class PerformDetails extends AppCompatActivity {
    private LinearLayout mLinearExercise, mLinearRest;
    private ImageView mPerformImage;
    private TextView mHeadText, mTimeText, mRestText, mTotalTimeText, mTotalRestTimeText;

    private Button mMinusTimeBtn, mPlusTimeBtn;
    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_details);
        mLinearExercise = (LinearLayout)findViewById(R.id.linearLayout_exercise);
        mLinearRest = (LinearLayout)findViewById(R.id.linearLayout_rest);
        mPerformImage = (ImageView) findViewById(R.id.perform_details_image);
        mHeadText = (TextView)findViewById(R.id.perform_details_exercise_name);
        mTimeText = (TextView)findViewById(R.id.perform_details_excercise_time);
        mRestText = (TextView)findViewById(R.id.perform_details_duration_rest_time);
        mTotalTimeText = (TextView)findViewById(R.id.perform_details_excercise_total_time_txt);
        mTotalRestTimeText = (TextView)findViewById(R.id.perform_details_excercise_total_rest_time);
        mMinusTimeBtn = (Button) findViewById(R.id.perform_details_left_btn);
        mPlusTimeBtn = (Button) findViewById(R.id.perform_details_right_btn);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() { ///for going to Account Activity Page
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    dUserUID = user.getUid();
                    documentReference = db.collection("StayFit").document("Info")
                            .collection("Normal_USER").document(dUserUID);
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                    //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            }
        };

        getIntentMethod();


    }
    private boolean intentFoundError = true;
    String dsDayUID =  "NX";
    long PerformListPosition = 0;
    int diPerformListPostion = 0;
    int diPerformDurationMS = 0;
    int diRestDurationMS = 0;
    ArrayList<PerformModel> listContestItem = new ArrayList<PerformModel>();;
    private void getIntentMethod() {
        //////////////GET INTENT DATA
        final Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            dsDayUID = intent.getExtras().getString("dsDayUID");
            listContestItem = (ArrayList<PerformModel>)  getIntent().getSerializableExtra("PerformList");
            PerformListPosition =  getIntent().getExtras().getLong("PerformListPosition");
            diPerformListPostion = (int) PerformListPosition;
            intentFoundError = CheckIntentMethod(dsDayUID);

            if(listContestItem == null){
                Toast.makeText(getApplicationContext(),"List error", Toast.LENGTH_SHORT).show();;
            }else if(!intentFoundError ){
                //Toast.makeText(getApplicationContext(),"dd "+dsDayUID, Toast.LENGTH_SHORT).show();;
                //Toast.makeText(getApplicationContext(),"list "+listContestItem.get(diPerformListPostion).getExerciseName(), Toast.LENGTH_SHORT).show();;
                StartCoding();
            }else{
                Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();;
            }
        }else{
            dsDayUID = "NO";
            Toast.makeText(getApplicationContext(),"Intent error", Toast.LENGTH_SHORT).show();
        }

    }
    int diSize = listContestItem.size();
    private void StartCoding() {
        diSize = listContestItem.size();
            /*PerformModel performModel = new PerformModel();
            performModel = listContestItem.get(diPerformListPostion);
            String dsPerformName = listContestItem.get(diPerformListPostion).getExerciseName();
            String dsPerformPhotoULR = listContestItem.get(diPerformListPostion).getExercisePhotoUrl();
            mHeadText.setText(dsPerformName);
            Glide.with(PerformDetails.this).load(dsPerformPhotoULR).centerCrop().into(mPerformImage);
            long diPerformDuration = performModel.getExerciseiDuration();
            diPerformDurationMS = (int) diPerformDuration*1000;                 Log.d("PerformDetails", "onResult:PerformDetails Name "+performModel.getExerciseName()+" Duration"+performModel.getExerciseiDuration() );
            long diRestDuration = performModel.getExerciseiRestSec();
            diRestDurationMS = (int) diRestDuration*1000;*/
            mPerformRunnable.run();

    }
    private CountDownTimer timer;
    private CountDownTimer RestTimer;
    private Handler mHandler = new Handler();

    boolean onPerformFinish = false;
    public Runnable mPerformRunnable= new Runnable() {
        @Override
        public void run() {


            //Start Again


            Log.d("PerformDetails", "onResult:PerformDetails mPerformRunnable Start Again diPerformListPostion " +diPerformListPostion);
            mLinearExercise.setVisibility(View.VISIBLE);
            PerformModel performModel = new PerformModel();
            if(diPerformListPostion < diSize){
                performModel = listContestItem.get(diPerformListPostion);
                String dsPerformName = listContestItem.get(diPerformListPostion).getExerciseName();
                String dsPerformPhotoULR = listContestItem.get(diPerformListPostion).getExercisePhotoUrl();
                mHeadText.setText(dsPerformName);
                //Picasso.get().load(dsPerformPhotoULR).fit().centerCrop().into(mPerformImage);
                Glide.with(PerformDetails.this).load(dsPerformPhotoULR).centerCrop().into(mPerformImage);


                long diDuration = performModel.getExerciseiDuration();
                diPerformDurationMS = (int) diDuration*1000;
                long diRestDuration = performModel.getExerciseiRestSec();
                diRestDurationMS = (int) diRestDuration*1000;
                onPerformFinish = false;
                mTotalTimeText.setText("00:"+diDuration+"\nExercise");
                mTotalRestTimeText.setText("00:"+diRestDuration+"\nRest Time");
                //Toast.makeText(getApplicationContext(),"Perform = "+diPerformDurationMS+" Rest"+diRestDurationMS, Toast.LENGTH_SHORT).show();
                diTotalRunMS = diPerformDurationMS+diRestDurationMS;
                Log.d("PerformDetails", "onResult:PerformDetails xxxx  Pms="+diPerformDurationMS+" Rms"+diRestDurationMS );
                Log.d("PerformDetails", "onResult:PerformDetails mPerformRunnable dsPerformName " +dsPerformName);
                //Toast.makeText(getApplicationContext(),"Duration = "+diDuration+", Rest = "+diRestDuration, Toast.LENGTH_SHORT).show();

                timer = new CountDownTimer(diTotalRunMS, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long secondUntilFinish = millisUntilFinished/1000;
                        //Log.d("PerformDetails", "onResult:PerformDetails onTick run() "+String.valueOf(millisUntilFinished/1000));
                        if(diRestDurationMS>=millisUntilFinished){
                            Log.d("PerformDetails", "onResult:PerformDetails onTick Pms="+diPerformDurationMS+" Rms"+diRestDurationMS+" run"+millisUntilFinished);
                            onPerformFinish =true;
                            mLinearRest.setVisibility(View.VISIBLE);
                            mLinearExercise.setVisibility(View.GONE);
                            if(secondUntilFinish<10)
                                mRestText.setText("00:0"+secondUntilFinish);
                            else
                                mRestText.setText("00:"+secondUntilFinish);
                            mTimeText.setText("FINISHED");

                        }else{
                            if(onPerformFinish == false) {
                                Log.d("PerformDetails", "onResult:PerformDetails onTick  Pms="+diPerformDurationMS+" Rms"+diRestDurationMS+" run"+millisUntilFinished);
                                if((secondUntilFinish-(diRestDurationMS/1000)) < 10)
                                    mTimeText.setText("00:0" + String.valueOf(secondUntilFinish-(diRestDurationMS/1000)));
                                else
                                    mTimeText.setText("00:" + String.valueOf(secondUntilFinish-(diRestDurationMS/1000)));
                            }
                        }

                    }
                    @Override
                    public void onFinish() {
                        Log.d("PerformDetails", "onResult:PerformDetails mPerformRunnable Finished");
                        mRestText.setText("Rest FINISHED");
                        mLinearRest.setVisibility(View.GONE);
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long diPresentExcercise = documentSnapshot.getLong( "exerciseTime");
                                        documentReference.update("exerciseTime", diPresentExcercise+diDuration);
                                    }
                                });
                    }
                }.start();
                mHandler.postDelayed(mPerformRunnable,diTotalRunMS);
                diPerformListPostion++;
            }else{
                mHandler.removeCallbacks(this);
            }

        }
    };
    int diTotalRunMS = 0;




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
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mPerformRunnable);
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}