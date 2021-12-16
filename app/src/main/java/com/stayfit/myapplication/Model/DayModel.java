package com.stayfit.myapplication.Model;

public class DayModel {
    private String DayUID = "NO";
    private String DayName = "NO";
    private String DayPhotoUrl = "NO";
    private String DayBeginnerLevel = "NO";
    private String DayExtra = "NO";
    private String DayCreator = "NO";
    private long DayiTotalExercise = 0;
    private long DayiPriority = 0;
    private long DayiCalories = 0;
    private long DayiDuration = 0;

    public DayModel(String dayUID, String dayName, String dayPhotoUrl, String dayBeginnerLevel, String dayExtra, String dayCreator, long dayiTotalExercise, long dayiPriority, long dayiCalories, long dayiDuration) {
        DayUID = dayUID;
        DayName = dayName;
        DayPhotoUrl = dayPhotoUrl;
        DayBeginnerLevel = dayBeginnerLevel;
        DayExtra = dayExtra;
        DayCreator = dayCreator;
        DayiTotalExercise = dayiTotalExercise;
        DayiPriority = dayiPriority;
        DayiCalories = dayiCalories;
        DayiDuration = dayiDuration;
    }

    public DayModel() {
    }

    public String getDayUID() {
        return DayUID;
    }

    public String getDayName() {
        return DayName;
    }

    public String getDayPhotoUrl() {
        return DayPhotoUrl;
    }

    public String getDayBeginnerLevel() {
        return DayBeginnerLevel;
    }

    public String getDayExtra() {
        return DayExtra;
    }

    public String getDayCreator() {
        return DayCreator;
    }

    public long getDayiTotalExercise() {
        return DayiTotalExercise;
    }

    public long getDayiPriority() {
        return DayiPriority;
    }

    public long getDayiCalories() {
        return DayiCalories;
    }

    public long getDayiDuration() {
        return DayiDuration;
    }
}
