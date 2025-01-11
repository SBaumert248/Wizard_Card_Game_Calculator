package com.example.Wizard_Helper_v2.View;

import com.example.Wizard_Helper_v2.Controller.WizardGame;
import com.example.Wizard_Helper_v2.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.Wizard_Helper_v2.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private String savefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();

        String filename = "game.json";
        File file = new File(context.getFilesDir(), filename);
        this.savefile = file.getAbsolutePath();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Zugriff auf den FloatingActionButton
        FloatingActionButton startGameBtn = findViewById(R.id.startGameBtn);

        // OnClickListener für den Button
        startGameBtn.setOnClickListener(v -> {
            startGame();
            startGameBtn.setVisibility(View.GONE); // Button nach Start verstecken
        });
        this.restoreGame();
        // geladene, Spielinhalt übernehmen und die GUI anpassen
    }

    private void restoreGame(){
        WizardGame.loadFromJson(this.savefile);
        if (WizardGame.getInstance().isRunning()){
            int playerCount = WizardGame.getInstance().getPlayers().size();
            this.setStartGameButtonVisibility(false);
            notifyFirstFragmentGameStarted(playerCount);
            this.loadPlayer();
            invalidateOptionsMenu();
        }
    }

    // Spiel starten
    private void startGame() {
        System.out.println("Spiel wird gestartet...");

        // Initialisiere oder verwende die Singleton-Instanz von WizardGame
        WizardGame game = WizardGame.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("Setting", Context.MODE_PRIVATE);
        int playerCount = sharedPreferences.getInt("player_count", 4);

        // Setze Standardwerte oder führe Spiel-Logik aus
        game.startGame(playerCount);
        notifyFirstFragmentGameStarted(playerCount);

        invalidateOptionsMenu();

        System.out.println("Menü aktualisiert: finish_actual_game ist jetzt aktiv.");
    }

    private Fragment getGameFragment(){
        // Zugriff auf den NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {
            // Zugriff auf das aktuelle Ziel-Fragment (FirstFragment)
            Fragment firstFragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();

            if (firstFragment instanceof FirstFragment) {
                return firstFragment;
            } else {
                System.out.println("FirstFragment nicht gefunden oder nicht aktiv.");
            }
        } else {
            System.out.println("NavHostFragment nicht gefunden.");
        }
        return null;
    }

    public void notifyFirstFragmentGameStarted(int playerCount) {
        Fragment gameArea = this.getGameFragment();
        if (gameArea instanceof FirstFragment){
            ((FirstFragment) gameArea).onGameStarted(playerCount);
        }
    }

    public void loadPlayer(){
        Fragment gameArea = this.getGameFragment();
        if (gameArea instanceof FirstFragment){
            ((FirstFragment) gameArea).loadLastGame();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Navigiere zum SecondFragment
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.finish_actual_game) {
            // Navigiere zum SecondFragment
            if (!WizardGame.getInstance().isRunning()) {
                this.setStartGameButtonVisibility(true);
            }
            Fragment gameArea = this.getGameFragment();
            if (gameArea instanceof FirstFragment){
                int playerCount = WizardGame.getInstance().getPlayers().size();
                ((FirstFragment) gameArea).setPlayerFields(playerCount, false);
                ((FirstFragment) gameArea).updatePlayerCountMessage(true);
                binding.startGameBtn.setVisibility(View.VISIBLE);

            }
            WizardGame.getInstance().resetGame();
            invalidateOptionsMenu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        // Menüeintrag "Einstellungen" wieder aktivieren
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause(){
        super.onPause();
        WizardGame.getInstance().saveToJson(this.savefile);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Finden Sie das Menüelement action_settings
        MenuItem settingsItem = menu.findItem(R.id.action_settings);

        // Überprüfen Sie die Sichtbarkeit von settings_area
        View settingsArea = findViewById(R.id.settings_area);

        // Wenn settings_area sichtbar ist, verstecken Sie das Menüelement action_settings
        // Andernfalls machen Sie das Menüelement action_settings sichtbar
        if (settingsItem != null) {
            settingsItem.setVisible(settingsArea == null || settingsArea.getVisibility() != View.VISIBLE);
            settingsItem.setEnabled(!WizardGame.getInstance().isRunning());
        }

        MenuItem resetGameItem = menu.findItem(R.id.finish_actual_game);

        if (resetGameItem != null) {
            resetGameItem.setEnabled(WizardGame.getInstance().isRunning());
        }

        // Erzeuge das Menü
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setStartGameButtonVisibility(boolean visible) {
        FloatingActionButton startGameBtn = findViewById(R.id.startGameBtn);
        if (startGameBtn != null) {
            startGameBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

}