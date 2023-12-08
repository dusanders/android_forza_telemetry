package com.dusanders.forzatelemetry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.format.Formatter;
import android.util.Log;

import com.dusanders.forzatelemetry.ui.main.MainFragment;
import com.dusanders.forzatelemetry.ui.main.MainViewModel;

import java.net.DatagramPacket;

import forza.telemetry.ForzaInterface;
import forza.telemetry.ForzaTelemetryApi;
import forza.telemetry.ForzaTelemetryBuilder;
import forza.telemetry.VehicleData;

public class MainActivity extends AppCompatActivity implements ForzaInterface {
    private final String TAG = "MainActivity";
    private final int port = 5300;
    private HandlerThread handlerThread;
    private Handler handler;
    private ForzaTelemetryBuilder builder;
    private MainViewModel mainViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance(mainViewModel))
                    .commitNow();
        }
        handlerThread = new HandlerThread("Main_background");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(builder == null) {
            builder = new ForzaTelemetryBuilder(port);
            builder.addListener(this);
        }
        if(!builder.getThread().isAlive()) {
            Log.w(TAG, "Thread not alive..");
            builder.getThread().start();
        }
    }

    @Override
    public void onDataReceived(ForzaTelemetryApi api) {
        Log.d("Main", api.getCurrentEngineRpm().toString());
        mainViewModel.submitNewApi(api);
    }

    @Override
    public void onConnected(ForzaTelemetryApi api, DatagramPacket packet) {
        Log.d("Main", "Connected");
    }

    @Override
    public void onGamePaused() {

    }

    @Override
    public void onGameUnpaused() {

    }

    @Override
    public void onCarChanged(ForzaTelemetryApi api, VehicleData data) {

    }

    private Runnable getIpRunnable() {
        return () -> {
            WifiManager wm = (WifiManager) getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo()
                    .getIpAddress());
            mainViewModel.setIp(ip, port);
        };
    }
}