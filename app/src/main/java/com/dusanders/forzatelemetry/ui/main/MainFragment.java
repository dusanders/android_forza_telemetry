package com.dusanders.forzatelemetry.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dusanders.forzatelemetry.R;

import forza.telemetry.ForzaTelemetryApi;
import forza.telemetry.ForzaTelemetryBuilder;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private TextView titleView;
    private TextView ipView;

    public static MainFragment newInstance(MainViewModel mainViewModel) {
        return new MainFragment(mainViewModel);
    }

    public MainFragment(MainViewModel viewModel) {
        this.mViewModel = viewModel;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        titleView = v.findViewById(R.id.message);
        ipView = v.findViewById(R.id.ipTextView);
        mViewModel.getState().observe(getViewLifecycleOwner(), new Observer<MainViewModel.State>() {
            @Override
            public void onChanged(MainViewModel.State state) {
                updateViewState(state);
            }
        });
        return v;
    }

    private void updateViewState(MainViewModel.State state) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ipView.setText(state.ip);
                titleView.setText("hp: " + state.hp + " @ " + state.rpm + "\n"
                        + "tor: " + state.torque + " @ " + state.torquerpm);
            }
        });
    }

}