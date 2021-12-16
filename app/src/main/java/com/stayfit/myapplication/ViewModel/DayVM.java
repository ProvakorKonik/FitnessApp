package com.stayfit.myapplication.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.stayfit.myapplication.Model.DayModel;

import java.util.ArrayList;
import java.util.List;

public class DayVM  extends AndroidViewModel {
    public DayVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 ContestsVM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<List<DayModel>> DaysList() {
        List<DayModel> listDayItem; listDayItem=new ArrayList<>();
        CollectionReference notebookRef;        //Firrebase database link
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 LoadLevel4List start");



        notebookRef = db.collection("StayFit").document("Info")
                .collection("Type").document("Beginner")
                .collection("Days");

        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef
                    .orderBy("DayiPriority", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String dayUID, String dayName, String dayPhotoUrl, String dayBeginnerLevel, String dayExtra, String dayCreator, long dayiTotalExercise, long dayiPriority, long dayiCalories, long dayiDuration
                                listDayItem.add(new DayModel("UID","NULL", "PhotoUrl","dayBeginnerLevel", "dayExtra", "dayCreator", 0,  0,0,0));
                                mLiveData.postValue(listDayItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    DayModel dayModel = documentSnapshot.toObject(DayModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsDay_UID = documentSnapshot.getId();
                                    String dsDay_Name = dayModel.getDayName();
                                    String dsDay_PhotoUrl = dayModel.getDayPhotoUrl();
                                    String dsDay_BeginnerLevel = dayModel.getDayBeginnerLevel();
                                    String dsDay_Extra = dayModel.getDayExtra();
                                    String dsDay_Creator = dayModel.getDayCreator();

                                    long diDay_TotalExcercise = dayModel.getDayiTotalExercise();
                                    long diDay_Priority = dayModel.getDayiPriority();
                                    long diDay_Calories = dayModel.getDayiCalories();
                                    long diDay_Duration = dayModel.getDayiDuration();
                                    //String bookUID, String bookName, String bookPhotoUrl, String bookBio, String bookCreator, String bookExtra, String bookPassword, int bookiPriority, int bookiViewCount, int bookiTotalLevel
                                    listDayItem.add(new DayModel(dsDay_UID,dsDay_Name, dsDay_PhotoUrl,dsDay_BeginnerLevel, dsDay_Extra,  dsDay_Creator,
                                            diDay_TotalExcercise, diDay_Priority,diDay_Calories ,diDay_Duration ));
                                    mLiveData.postValue(listDayItem);
                                }
                                mLiveData.postValue(listDayItem);    //All Items level 4 , it is a one type category

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

        return mLiveData;
    }
}
