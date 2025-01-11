package com.example.Wizard_Helper_v2.View;

import com.example.Wizard_Helper_v2.Controller.WizardGame;
import com.example.Wizard_Helper_v2.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.widget.RadioGroup;

import com.example.Wizard_Helper_v2.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private void saveSettings(){
        // Daten speichern
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Anzahl der Spieler speichern
        RadioGroup rg_PlayerCount = binding.rgPlayerCount;
        int idPlayerCount = rg_PlayerCount.getCheckedRadioButtonId();
        int playerCount;
        if (idPlayerCount == R.id.rb_3Player){
            playerCount = 3;
        } else if (idPlayerCount == R.id.rb_4Player) {
            playerCount = 4;
        } else if (idPlayerCount == R.id.rb_5Player) {
            playerCount = 5;
        } else {
            playerCount = 6;
        }
        editor.putInt("player_count", playerCount);

        // Master-Modus speichern
        boolean isMasterModeEnabled = binding.sMasterMode.isChecked();
        editor.putBoolean("master_mode", isMasterModeEnabled);

        // Zeige letzte Punkte speichern
        boolean showLastPoints = binding.sShowLastPoints.isChecked();
        editor.putBoolean("show_last_points", showLastPoints);

        // Einstellungen abspeichern
        editor.apply();
    }

    private void loadSettings(){
        // Daten abrufen
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
        int playerCount = sharedPreferences.getInt("player_count", 4);
        boolean masterMode = sharedPreferences.getBoolean("master_mode", false);
        boolean showLastPoints = sharedPreferences.getBoolean("show_last_points", true);

        // UI-Komponenten finden und Einstellungen setzen
        binding.sMasterMode.setChecked(masterMode);
        binding.sShowLastPoints.setChecked(showLastPoints);
        int playerCountId;
        if (playerCount == 3){
            playerCountId = R.id.rb_3Player;
        } else if (playerCount == 4) {
            playerCountId = R.id.rb_4Player;
        } else if (playerCount == 5) {
            playerCountId = R.id.rb_5Player;
        } else {
            playerCountId = R.id.rb_6Player;
        }
        binding.rgPlayerCount.check(playerCountId);
    }

    @Override
    public void onPause(){
        super.onPause();
        // Hier speichern Sie die Einstellungen, bevor das Fragment verlassen wird

        this.saveSettings();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        this.loadSettings();
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if WizardGame.getInstance().
        // Verstecke den Button in der MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setStartGameButtonVisibility(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}