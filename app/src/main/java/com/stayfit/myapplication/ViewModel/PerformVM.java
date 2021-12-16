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
import com.stayfit.myapplication.Model.PerformModel;

import java.util.ArrayList;

public class PerformVM extends AndroidViewModel {
    public PerformVM(@NonNull Application application) {
        super(application);
        Log.d("ViewModel", "allViewModel:4 ContestsVM start");
    }
    public MutableLiveData mLiveData;
    public MutableLiveData<ArrayList<PerformModel>> DayOnePerformanceList(String dsDayUID) {
        ArrayList<PerformModel> listContestItem ; listContestItem =new ArrayList<>();
        CollectionReference notebookRef;        //Firrebase database link
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("ViewModel", "allViewModel:4 LoadLevel4List start");



        notebookRef = db.collection("StayFit").document("Info")
                .collection("Type").document("Beginner")
                .collection("Days").document(dsDayUID)
                .collection("AllExercise");

        if(mLiveData == null) {
            mLiveData = new MutableLiveData();
            notebookRef
                    .orderBy("ExerciseiPriority", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {   //documnet er vitore je multiple document query ache er jonno for loop
                            String data = "";
                            if(queryDocumentSnapshots.isEmpty()) {
                                //String exerciseUID, String exerciseName, String exercisePhotoUrl, String exerciseVideoUrl, String exerciseVoiceUrl, String exerciseDetails, String exerciseExtra, long exerciseiPriority, long exerciseiRestSec, long exerciseiDuration, long exerciseiSteps) {
                                listContestItem.add(new PerformModel("UID","NULL", "exercisePhotoUrl","exerciseVideoUrl", "exerciseVoiceUrl", "exerciseDetails","exerciseExtra", 0,  0,0,0));
                                mLiveData.postValue(listContestItem);
                                Log.d("ViewModel", "allViewModel:4 queryDocumentSnapshots empty");
                            }else {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    PerformModel dayModel = documentSnapshot.toObject(PerformModel.class);
                                    //messageModel.setDocumentID(documentSnapshot.getId());
                                    String dsExcercise_UID = documentSnapshot.getId();
                                    String dsExcercise_Name = dayModel.getExerciseName();
                                    String dsExcercise_PhotoURL = dayModel.getExercisePhotoUrl();
                                    String dsExcercise_VideoURL = dayModel.getExerciseVideoUrl();
                                    String dsExcercise_VoiceURL = dayModel.getExerciseVoiceUrl();
                                    String dsExcercise_Details = dayModel.getExerciseDetails();
                                    String dsExcercise_Extra = dayModel.getExerciseExtra();

                                    long diExcercise_Priorities = dayModel.getExerciseiPriority();
                                    long diExcercise_RestDuration = dayModel.getExerciseiRestSec();
                                    long diExcercise_ExerciseDuration = dayModel.getExerciseiDuration();
                                    long diExcercise_Steps = dayModel.getExerciseiSteps();
                                    //String bookUID, String bookName, String bookPhotoUrl, String bookBio, String bookCreator, String bookExtra, String bookPassword, int bookiPriority, int bookiViewCount, int bookiTotalLevel
                                    listContestItem.add(new PerformModel(dsExcercise_UID,dsExcercise_Name, dsExcercise_PhotoURL,dsExcercise_VideoURL, dsExcercise_VoiceURL, dsExcercise_Details, dsExcercise_Extra,
                                            diExcercise_Priorities, diExcercise_RestDuration,diExcercise_ExerciseDuration ,diExcercise_Steps ));
                                    mLiveData.postValue(listContestItem);
                                }
                                mLiveData.postValue(listContestItem);    //All Items level 4 , it is a one type category

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
