package com.dusanders.forzatelemetry.ui.main;

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

    private MutableLiveData<State> state = new MutableLiveData<>();

    public LiveData<State> getState() {
        return state;
    }

    public static MainViewModel getInstance(ViewModelProvider provider) {
        return provider.get(MainViewModel.class);
    }

    public void submitNewApi(ForzaTelemetryApi api) {
        setTitle("speed: " + api.getSpeedMps() + "\n"
                + " mph: " + api.getSpeedMph() + "\n"
                + " kph: " + api.getSpeedKph());
    }

    public void setIp(String ip, int port) {
        State updated = new State();
        updated.ip = ip + " @ " + port;
        this.state.postValue(updated);
    }

    public void setTitle(String title) {
        State updated = new State();
        updated.title = title;
        this.state.postValue(updated);
    }

    public void setHp(int hp, int rpm) {
        if (hp > state.getValue().hp) {
            State updated = fromPrevious();
            updated.hp = hp;
            updated.rpm = rpm;
            state.postValue(updated);
        }
    }

    public void setTorque(int torque, int rpm) {
        if (torque > state.getValue().torque) {
            State updated = fromPrevious();
            updated.torque = torque;
            updated.torquerpm = rpm;
            state.postValue(updated);
        }
    }

    private State fromPrevious() {
        State updated = state.getValue();
        return updated;
    }
}