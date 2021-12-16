package com.stayfit.myapplication.Model;

import java.io.Serializable;

public class PerformModel implements Serializable {
    
        private String ExerciseUID  = "NO";
        private String ExerciseName  = "NO";
        private String ExercisePhotoUrl  = "NO";
        private String ExerciseVideoUrl  = "NO";
        private String ExerciseVoiceUrl  = "NO";
        private String ExerciseDetails  = "NO";
        private String ExerciseExtra  = "NO";
        private long  ExerciseiPriority  = 0;
        private long  ExerciseiRestSec = 0;
        private long  ExerciseiDuration = 0;
        private long  ExerciseiSteps = 0;

    public PerformModel() {
    }

    public PerformModel(String exerciseUID, String exerciseName, String exercisePhotoUrl, String exerciseVideoUrl, String exerciseVoiceUrl, String exerciseDetails, String exerciseExtra, long exerciseiPriority, long exerciseiRestSec, long exerciseiDuration, long exerciseiSteps) {
        ExerciseUID = exerciseUID;
        ExerciseName = exerciseName;
        ExercisePhotoUrl = exercisePhotoUrl;
        ExerciseVideoUrl = exerciseVideoUrl;
        ExerciseVoiceUrl = exerciseVoiceUrl;
        ExerciseDetails = exerciseDetails;
        ExerciseExtra = exerciseExtra;
        ExerciseiPriority = exerciseiPriority;
        ExerciseiRestSec = exerciseiRestSec;
        ExerciseiDuration = exerciseiDuration;
        ExerciseiSteps = exerciseiSteps;
    }

    public String getExerciseUID() {
        return ExerciseUID;
    }

    public String getExerciseName() {
        return ExerciseName;
    }

    public String getExercisePhotoUrl() {
        return ExercisePhotoUrl;
    }

    public String getExerciseVideoUrl() {
        return ExerciseVideoUrl;
    }

    public String getExerciseVoiceUrl() {
        return ExerciseVoiceUrl;
    }

    public String getExerciseDetails() {
        return ExerciseDetails;
    }

    public String getExerciseExtra() {
        return ExerciseExtra;
    }

    public long getExerciseiPriority() {
        return ExerciseiPriority;
    }

    public long getExerciseiRestSec() {
        return ExerciseiRestSec;
    }

    public long getExerciseiDuration() {
        return ExerciseiDuration;
    }

    public long getExerciseiSteps() {
        return ExerciseiSteps;
    }
}
