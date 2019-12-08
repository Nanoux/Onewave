package com.nanowheel.nanoux.nanowheel.TripDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "trips")
public class Trip {

    @PrimaryKey
    private long timeStamp;

    long getTimeStamp(){
        return timeStamp;
    }

    void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private ArrayList<com.google.android.gms.maps.model.LatLng> positions;
    private ArrayList<Integer> levels;
    private ArrayList<Integer> revs;

    ArrayList<LatLng> getPositions() {
        return positions;
    }

    ArrayList<Integer> getLevels() {
        return levels;
    }

    ArrayList<Integer> getRevs() {
        return revs;
    }

    ArrayList<Long> getTimes() {
        return times;
    }

    private ArrayList<Long> times;

    void setPositions(ArrayList<LatLng> positions) {
        this.positions = positions;
    }

    void setLevels(ArrayList<Integer> levels) {
        this.levels = levels;
    }

    void setRevs(ArrayList<Integer> revs) {
        this.revs = revs;
    }

    void setTimes(ArrayList<Long> times) {
        this.times = times;
    }

    int getRevStamp() {
        return revStamp;
    }

    void setRevStamp(int revStamp) {
        this.revStamp = revStamp;
    }

    private int revStamp = -1;
}
