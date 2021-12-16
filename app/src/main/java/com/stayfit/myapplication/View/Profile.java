package com.stayfit.myapplication.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.stayfit.myapplication.LoginCheck;
import com.stayfit.myapplication.LoginRegistration;
import com.stayfit.myapplication.R;

public class Profile extends AppCompatActivity {

    private ImageView mUserImage;
    private TextView mUserNameText, mUserWeightText, muserCaloriesText, mUserSignOutTxt;

    //Firebase Auth
    private String dUserUID = "NO";
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener; //For going to Account Activity Page


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUserImage = (ImageView)findViewById(R.id.profile_user_image);
        mUserNameText = (TextView)findViewById(R.id.profile_user_name);
        mUserWeightText = (TextView)findViewById(R.id.profile_user_weight);
        muserCaloriesText = (TextView)findViewById(R.id.profile_user_calories);
        mUserSignOutTxt = (TextView)findViewById(R.id.profile_user_sign_out_text);
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
        mUserSignOutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                /*Intent intent = new Intent(getApplicationContext(), LoginCheck.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);*/
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
                    long diUserExerciseTime = documentSnapshot.getLong( "exerciseTime");
                    String dUserPhotoURL = documentSnapshot.getString("photoURL");
                    mUserNameText.setText(dUserName);
                    mUserWeightText.setText(diUserWeight+"KG");
                    muserCaloriesText.setText(diUserExerciseTime+"sec");
                    Picasso.get().load(dUserPhotoURL).fit().centerCrop().into(mUserImage);

                }else{
                    Toast.makeText(getApplicationContext(),"Error User Information Not Found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Profile.this, LoginRegistration.class);
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
}