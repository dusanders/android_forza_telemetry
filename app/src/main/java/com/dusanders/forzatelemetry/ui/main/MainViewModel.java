package com.dusanders.forzatelemetry.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import forza.telemetry.ForzaTelemetryApi;

public class MainViewModel extends ViewModel {
    public class State {
        String ip = "0.0.0.0";
        String title = "";
        int hp = 0;
        int rpm = 0;
        int torque = 0;
        int torquerpm = 0;
    }

    private final String TAG = "MainViewModel";
    private MutableLiveData<State> state = new MutableLiveData<>();

    public LiveData<State> getState() {
        return state;
    }

    public void submitNewApi(ForzaTelemetryApi api) {
        String out = "speed: " + api.getSpeedMps() + "\n"
                + " mph: " + api.getSpeedMph() + "\n"
                + " kph: " + api.getSpeedKph() + "\n"
                + " time: " + api.getTimeStampMS() + "\n"
                + "avg vel: " + api.getAverageVelocity() + "\n"
                + "gear: " + api.getGear() + "\n"
                + " rpm: " + api.getCurrentEngineRpm();
//        Log.d(TAG, out);
        setTitle(out);
    }

    public void setIp(String ip, int port) {
        State updated = fromPrevious();
        updated.ip = ip + " @ " + port;
        this.state.postValue(updated);
    }

    public void setTitle(String title) {
        State updated = fromPrevious();
        updated.title = title;
        this.state.postValue(updated);
    }

    private State fromPrevious() {
        State updated = state.getValue();
        if (updated == null) {
            updated = new State();
        }
        return updated;
    }
}