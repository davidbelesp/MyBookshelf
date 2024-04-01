package com.davidbelesp.mybookshelf.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.controllers.ConfigManager;
import com.davidbelesp.mybookshelf.models.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConfigFragment extends Fragment {

    private FloatingActionButton saveButton;
    private Switch switchNSFW;
    private Switch switchCensor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadElements(view);

        setEvents();

        setFields(ConfigManager.loadConfig(getContext()));
    }

    private void loadElements(View view) {
        this.switchNSFW = view.findViewById(R.id.configToggleNSFW);
        this.switchCensor = view.findViewById(R.id.configToggleCensor);
        this.saveButton = view.findViewById(R.id.saveConfigButton);
    }

    private void setFields(Config config){
        switchNSFW.setChecked(config.getNSFW());
        switchCensor.setChecked(config.getCensor());
    }

    private void setEvents() {
        saveButton.setOnClickListener(e -> {
            boolean nsfw = switchNSFW.isChecked();
            boolean censor = switchCensor.isChecked();
            ConfigManager.saveConfig(getContext(),
                    new Config.ConfigBuilder()
                            .setNSFW(nsfw)
                            .setCensor(censor)
                            .build()
            );
            Toast.makeText(getContext(), "Config Saved", Toast.LENGTH_SHORT).show();
        });
    }
}