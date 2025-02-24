package com.example.Wizard_Helper_v2.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Wizard_Helper_v2.Controller.WizardGame;
import com.example.Wizard_Helper_v2.R;
import com.example.Wizard_Helper_v2.databinding.FragmentFirstBinding;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private int actRound = 1;
    private Map<Integer, String> originalValues = new HashMap<>();

    private void saveSettings() {
        // Daten speichern
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Playernames", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // speichern der Spieler
        editor.putString("player1", binding.namePlayer1.getText().toString());
        editor.putString("player2", binding.namePlayer2.getText().toString());
        editor.putString("player3", binding.namePlayer3.getText().toString());
        editor.putString("player4", binding.namePlayer4.getText().toString());
        editor.putString("player5", binding.namePlayer5.getText().toString());
        editor.putString("player6", binding.namePlayer6.getText().toString());

        //Änderungen hinzufügen
        editor.apply();
    }

    private void loadLastUsedPlayernames(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Playernames", Context.MODE_PRIVATE);

        // Laden der Spielernamen - vom Wizard Objekt
        binding.namePlayer1.setText(sharedPreferences.getString("player1", "Spieler1"));
        binding.namePlayer2.setText(sharedPreferences.getString("player2", "Spieler2"));
        binding.namePlayer3.setText(sharedPreferences.getString("player3", "Spieler3"));
        binding.namePlayer4.setText(sharedPreferences.getString("player4", "Spieler4"));
        binding.namePlayer5.setText(sharedPreferences.getString("player5", "Spieler5"));
        binding.namePlayer6.setText(sharedPreferences.getString("player6", "Spieler6"));
    }

    private String intToStr(int value, String sDefault){
        if (value == -1){
            return sDefault;
        }
        return Integer.toString(value);
    }

    private String getPlayernameDef(int index, String sDefault){
        if (index < 0 || index >= WizardGame.getInstance().getPlayers().size()){
            return sDefault;
        }
        return WizardGame.getInstance().getPlayers().get(index).getName();
    }

    public void loadLastGame(){
        // TODO: Wenn ein Spiel geladenen wird, werden die Punkte der letzten Runden in die aktuelle Runde geschrieben
        // TODO: Es soll nur die Punkte geschrieben werden wenn, die für die aktuelle Runde ist
        if (WizardGame.getInstance().isRunning()) {
            // laden der Spielerdaten
            int actRound = WizardGame.getInstance().getRoundNumber();

            String playername = binding.namePlayer1.getText().toString();
            binding.editThinkPlayer1.setText(this.intToStr(WizardGame.getInstance().getActPrediction(playername), "?"));
            binding.editGetWinPlayer1.setText(this.intToStr(WizardGame.getInstance().getActResult(playername), "?"));
            binding.actPointsPlayer1.setText(this.intToStr(WizardGame.getInstance().getScore(playername), "?"));
            binding.textLastPointsPlayer1.setText(this.intToStr(WizardGame.getInstance().getScoreOfRound(playername, actRound), "?"));

            playername = binding.namePlayer2.getText().toString();
            binding.editThinkPlayer2.setText(this.intToStr(WizardGame.getInstance().getActPrediction(playername), "?"));
            binding.editGetWinPlayer2.setText(this.intToStr(WizardGame.getInstance().getActResult(playername), "?"));
            binding.actPointsPlayer2.setText(this.intToStr(WizardGame.getInstance().getScore(playername), "?"));
            binding.textLastPointsPlayer2.setText(this.intToStr(WizardGame.getInstance().getScoreOfRound(playername, actRound), "?"));

            playername = binding.namePlayer3.getText().toString();
            binding.editThinkPlayer3.setText(this.intToStr(WizardGame.getInstance().getActPrediction(playername), "?"));
            binding.editGetWinPlayer3.setText(this.intToStr(WizardGame.getInstance().getActResult(playername), "?"));
            binding.actPointsPlayer3.setText(this.intToStr(WizardGame.getInstance().getScore(playername), "?"));
            binding.textLastPointsPlayer3.setText(this.intToStr(WizardGame.getInstance().getScoreOfRound(playername, actRound), "?"));

            playername = binding.namePlayer4.getText().toString();
            binding.editThinkPlayer4.setText(this.intToStr(WizardGame.getInstance().getActPrediction(playername), "?"));
            binding.editGetWinPlayer4.setText(this.intToStr(WizardGame.getInstance().getActResult(playername), "?"));
            binding.actPointsPlayer4.setText(this.intToStr(WizardGame.getInstance().getScore(playername), "?"));
            binding.textLastPointsPlayer4.setText(this.intToStr(WizardGame.getInstance().getScoreOfRound(playername, actRound), "?"));

            playername = binding.namePlayer5.getText().toString();
            binding.editThinkPlayer5.setText(this.intToStr(WizardGame.getInstance().getActPrediction(playername), "?"));
            binding.editGetWinPlayer5.setText(this.intToStr(WizardGame.getInstance().getActResult(playername), "?"));
            binding.actPointsPlayer5.setText(this.intToStr(WizardGame.getInstance().getScore(playername), "?"));
            binding.textLastPointsPlayer5.setText(this.intToStr(WizardGame.getInstance().getScoreOfRound(playername, actRound), "?"));

            playername = binding.namePlayer6.getText().toString();
            binding.editThinkPlayer6.setText(this.intToStr(WizardGame.getInstance().getActPrediction(playername), "?"));
            binding.editGetWinPlayer6.setText(this.intToStr(WizardGame.getInstance().getActResult(playername), "?"));
            binding.actPointsPlayer6.setText(this.intToStr(WizardGame.getInstance().getScore(playername), "?"));
            binding.textLastPointsPlayer6.setText(this.intToStr(WizardGame.getInstance().getScoreOfRound(playername, actRound), "?"));

        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        this.loadLastUsedPlayernames();
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Mache den Button in der MainActivity wieder sichtbar
        boolean visibleStartBtn = (getActivity() instanceof MainActivity && !WizardGame.getInstance().isRunning());
        ((MainActivity) getActivity()).setStartGameButtonVisibility(visibleStartBtn);

        // Setup der TextWatcher für die EditText-Felder
//        setupEditTextListeners();
        this.setupTextChangeListeners();
        /*
        binding.buttonFirst.setOnClickListener(v ->
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment)
        );
         */

        binding.nextRoundBtn.setOnClickListener(v -> {
            WizardGame.getInstance().nextRound();
            this.originalValues.clear();
            this.updatePlayerCountMessage(false);
            this.setPlayerFields(WizardGame.getInstance().getPlayers().size(), true);
            this.setupTextChangeListeners();
            binding.nextRoundBtn.setVisibility(View.GONE);
            this.actRound = WizardGame.getInstance().getRoundNumber();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
            boolean showLastPoints = sharedPreferences.getBoolean("show_last_points", true);
            if (showLastPoints){
                this.showAllLastPoints();
            } else {
                this.resetActScoreText();
            }
        });
    }

    public void showStartBtn(){
        binding.nextRoundBtn.setVisibility(View.VISIBLE);
    }

    public void onGameStarted(int playerCount) {
        System.out.println("Spiel wurde im FirstFragment gestartet!");

        this.setPlayerFields(playerCount, true);

        // Spielernamen auslesen
        this.addPlayerNames(playerCount);
        System.out.println("Anzahl Spieler: !" + WizardGame.getInstance().getPlayers().size());

        this.updatePlayerCountMessage(false);

    }

    public void updatePlayerCountMessage(boolean doReset) {
        // Zugriff auf das TextView
        TextView playerCountTextView = getView().findViewById(R.id.textActRound);

        if (playerCountTextView != null) {
            String message;
            if (doReset){
                message = getString(R.string.choose_player);
                this.hideAllLastPoints();
            } else {
                // String aus den Ressourcen mit der Spieleranzahl formatieren
                int maxRound = WizardGame.getInstance().getMaxRoundNumber();
                int actRound = WizardGame.getInstance().getRoundNumber();
                message = getString(R.string.act_player_rounds, actRound, maxRound);
            }

            // Aktualisiere das TextView
            playerCountTextView.setText(message);
        }
    }

    private void addPlayerNames(int playerCount) {
        // IDs der Eingabefelder für Spielernamen
        int[] playerNameIds = {
                R.id.namePlayer1,
                R.id.namePlayer2,
                R.id.namePlayer3,
                R.id.namePlayer4,
                R.id.namePlayer5,
                R.id.namePlayer6
        };

        View rootView = getView();
        if (rootView == null)
            return;

        for (int i = 0; i < playerCount; i++) {
            EditText playerNameField = rootView.findViewById(playerNameIds[i]);
            if (playerNameField != null) {
                String playerName = playerNameField.getText().toString().trim();
                WizardGame.getInstance().addPlayer(playerName);
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        // Hier speichern Sie die Einstellungen, bevor das Fragment verlassen wird
        this.saveSettings();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Lade Einstellungen, wenn das Fragment angezeigt wird
        loadSettingsAndApply();
    }

    private void loadSettingsAndApply() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
        int playerCount = sharedPreferences.getInt("player_count", 4); // Standardwert: 4 Spieler
        togglePlayerVisibility(playerCount);
    }

    public void togglePlayerVisibility(int playerCount) {
        int[] playerRowIds = {
                R.id.rowPlayer1,
                R.id.rowPlayer2,
                R.id.rowPlayer3,
                R.id.rowPlayer4,
                R.id.rowPlayer5,
                R.id.rowPlayer6
        };

        int[] emptyRowIds = {
                R.id.rowLastPointsPlayer1,
                R.id.rowLastPointsPlayer2,
                R.id.rowLastPointsPlayer3,
                R.id.rowLastPointsPlayer4,
                R.id.rowLastPointsPlayer5,
                R.id.rowLastPointsPlayer6
        };

        View rootView = getView();
        if (rootView == null) return;

        for (int i = 0; i < playerRowIds.length; i++) {
            View playerRow = rootView.findViewById(playerRowIds[i]);
            if (playerRow != null) {
                playerRow.setVisibility(i < playerCount ? View.VISIBLE : View.GONE);
            }
            View emptyRow = rootView.findViewById(emptyRowIds[i]);
            if (emptyRow != null) {
                emptyRow.setVisibility(i < playerCount ? View.VISIBLE : View.GONE);
            }

        }
    }

    private void showAllLastPoints(){
        this.copyScore(R.id.actPointsPlayer1, R.id.textLastPointsPlayer1);
        this.copyScore(R.id.actPointsPlayer2, R.id.textLastPointsPlayer2);
        this.copyScore(R.id.actPointsPlayer3, R.id.textLastPointsPlayer3);
        this.copyScore(R.id.actPointsPlayer4, R.id.textLastPointsPlayer4);
        this.copyScore(R.id.actPointsPlayer5, R.id.textLastPointsPlayer5);
        this.copyScore(R.id.actPointsPlayer6, R.id.textLastPointsPlayer6);
    }

    public void resetActScoreText(){
        int[] actScores = {
                R.id.actPointsPlayer1,
                R.id.actPointsPlayer2,
                R.id.actPointsPlayer3,
                R.id.actPointsPlayer4,
                R.id.actPointsPlayer5,
                R.id.actPointsPlayer6
        };
        for (int id: actScores){
            TextView actScore = getView().findViewById(id);
            actScore.setText("?");
        }
    }

    private void copyScore(int srcId, int destId){
        TextView actScore = getView().findViewById(srcId);
        TextView lastShowScore = getView().findViewById(destId);
        lastShowScore.setText(actScore.getText().toString());
        lastShowScore.setVisibility(View.VISIBLE);
        actScore.setText("?");

    }

    private void hideAllLastPoints(){
        this.hideLastRoundPoint(R.id.textLastPointsPlayer1);
        this.hideLastRoundPoint(R.id.textLastPointsPlayer2);
        this.hideLastRoundPoint(R.id.textLastPointsPlayer3);
        this.hideLastRoundPoint(R.id.textLastPointsPlayer4);
        this.hideLastRoundPoint(R.id.textLastPointsPlayer5);
        this.hideLastRoundPoint(R.id.textLastPointsPlayer6);
    }

    private void hideLastRoundPoint(int id){
        TextView lastShowScore = getView().findViewById(id);
        lastShowScore.setVisibility(View.INVISIBLE);
    }

    public void setPlayerFields(int playerCount, boolean visible) {
        // IDs der Eingabefelder für Spieler
        int[] editThinkPlayerIds = {
                R.id.editThinkPlayer1,
                R.id.editThinkPlayer2,
                R.id.editThinkPlayer3,
                R.id.editThinkPlayer4,
                R.id.editThinkPlayer5,
                R.id.editThinkPlayer6
        };

        int[] editGetWinPlayerIds = {
                R.id.editGetWinPlayer1,
                R.id.editGetWinPlayer2,
                R.id.editGetWinPlayer3,
                R.id.editGetWinPlayer4,
                R.id.editGetWinPlayer5,
                R.id.editGetWinPlayer6
        };

        int[] actPointsPlayerIds = {
                R.id.actPointsPlayer1,
                R.id.actPointsPlayer2,
                R.id.actPointsPlayer3,
                R.id.actPointsPlayer4,
                R.id.actPointsPlayer5,
                R.id.actPointsPlayer6
        };

        int[] namePlayerIds = {
                R.id.namePlayer1,
                R.id.namePlayer2,
                R.id.namePlayer3,
                R.id.namePlayer4,
                R.id.namePlayer5,
                R.id.namePlayer6
        };

        View rootView = getView();
        if (rootView == null)
            return;

        // Alle Felder durchgehen und basierend auf der Spieleranzahl aktivieren/deaktivieren
        for (int i = 0; i < editThinkPlayerIds.length; i++) {

            // Eingabefelder finden und aktivieren/deaktivieren
            EditText editThinkPlayer = rootView.findViewById(editThinkPlayerIds[i]);
            if (editThinkPlayer != null && i < playerCount) {
                editThinkPlayer.setVisibility(visible ? View.VISIBLE : View.GONE);
                editThinkPlayer.setText("?");
            }

            EditText editGetWinPlayer = rootView.findViewById(editGetWinPlayerIds[i]);
            if (editGetWinPlayer != null && i < playerCount) {
                editGetWinPlayer.setVisibility(visible ? View.VISIBLE : View.GONE);
                editGetWinPlayer.setText("?");
            }

            TextView actPointsPlayer = rootView.findViewById(actPointsPlayerIds[i]);
            if (actPointsPlayer != null && i < playerCount) {
                actPointsPlayer.setVisibility(visible ? View.VISIBLE : View.GONE);
            }

            EditText namePayer = rootView.findViewById(namePlayerIds[i]);
            if (namePayer != null && i < playerCount) {
                namePayer.setClickable(!visible);
                namePayer.setFocusable(!visible);
                namePayer.setFocusableInTouchMode(!visible);
            }
        }
        if (!visible && WizardGame.getInstance().isRunning()) {
            Button nextRound = rootView.findViewById(R.id.nextRoundBtn);
            nextRound.setVisibility(View.GONE);
        }

        ImageView imgThink = rootView.findViewById(R.id.imgThink);
        imgThink.setVisibility(visible ? View.VISIBLE : View.GONE);

        ImageView imgGet = rootView.findViewById(R.id.imgGet);
        imgGet.setVisibility(visible ? View.VISIBLE : View.GONE);

        ImageView imgScore = rootView.findViewById(R.id.imgScore);
        imgScore.setVisibility(visible ? View.VISIBLE : View.GONE);

        TextView roundView = rootView.findViewById(R.id.textActRound);
        if (visible) {
            int actRound = WizardGame.getInstance().getRoundNumber();
            int maxRound = WizardGame.getInstance().getMaxRoundNumber();
            String message = getString(R.string.act_player_rounds, actRound, maxRound);
            roundView.setText(message);
        } else {
            roundView.setText(R.string.choose_player);
        }
    }

    private Integer tryParseInt(String value){
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Eingabe: " + value);
            return null;
        }
    }

    /*
    Hier werden die Punkte Punktzahlen der Benutzer eingegeben und anschließend der NextRoundBtn
    aktiviert
     */
    private void setupTextChangeListeners() {
        // IDs der Eingabefelder
        int[] editFieldIds = {
                R.id.editThinkPlayer1,
                R.id.editThinkPlayer2,
                R.id.editThinkPlayer3,
                R.id.editThinkPlayer4,
                R.id.editThinkPlayer5,
                R.id.editThinkPlayer6,
                R.id.editGetWinPlayer1,
                R.id.editGetWinPlayer2,
                R.id.editGetWinPlayer3,
                R.id.editGetWinPlayer4,
                R.id.editGetWinPlayer5,
                R.id.editGetWinPlayer6,
        };

        View rootView = getView();
        if (rootView == null) return;

        for (int id : editFieldIds) {
            EditText editText = rootView.findViewById(id);
            if (editText != null) {
                originalValues.put(id, editText.getText().toString());
                // TextWatcher hinzufügen
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        System.out.println("Text changed: " + s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        System.out.println("Bearbeitung beendet: " + id);
                        String currentValue = s.toString();
                        String originalValue = "";
                        if (originalValues.containsKey(id)) {
                            originalValue = originalValues.get(id);
                        }
                        Integer value = tryParseInt(currentValue);
                        if (currentValue.isEmpty() || currentValue.contains("?")
                                || currentValue.equals(originalValue)
                                || originalValue.isEmpty()
                                || value == null){
                            return;
                        }

                        String player = getPlayername(id);
                        // Wert wurde korrigiert
                        if (!originalValue.contains("?") && actRound == WizardGame.getInstance().getRoundNumber()) {
                            // Korrektur der Vorhersage vom jeweiligen Spieler

                            if (isPredictionEdit(id)) {
                                WizardGame.getInstance().correctPrediction(player, value);
                            }
                            if (isResultEdit(id)) {
                                WizardGame.getInstance().correctResult(player, value);
                            }

                            // Neuen Wert setzen
                        } else {
                            if (isPredictionEdit(id)) {
                                WizardGame.getInstance().setPrediction(player, value);
                            }
                            if (isResultEdit(id)) {
                                WizardGame.getInstance().setResult(player, value);
                            }
                        }
                        if (WizardGame.getInstance().playerDone(player)) {
                            setScore(id);
                        }

                        // Originalwert aktualisieren
                        originalValues.put(id, currentValue);
                        checkForEmptyFields();
                    }
                });
            }
        }
    }

    private String getPlayername(int id){
        View rootView = getView();

        if (id == R.id.editGetWinPlayer1 || id == R.id.editThinkPlayer1){
            EditText editText = rootView.findViewById(R.id.namePlayer1);
            return editText.getText().toString();
        } else if (id == R.id.editGetWinPlayer2 || id == R.id.editThinkPlayer2){
            EditText editText = rootView.findViewById(R.id.namePlayer2);
            return editText.getText().toString();
        } else if (id == R.id.editGetWinPlayer3 || id == R.id.editThinkPlayer3){
            EditText editText = rootView.findViewById(R.id.namePlayer3);
            return editText.getText().toString();
        } else if (id == R.id.editGetWinPlayer4 || id == R.id.editThinkPlayer4){
            EditText editText = rootView.findViewById(R.id.namePlayer4);
            return editText.getText().toString();
        } else if (id == R.id.editGetWinPlayer5 || id == R.id.editThinkPlayer5){
            EditText editText = rootView.findViewById(R.id.namePlayer5);
            return editText.getText().toString();
        } else if (id == R.id.editGetWinPlayer6 || id == R.id.editThinkPlayer6){
            EditText editText = rootView.findViewById(R.id.namePlayer6);
            return editText.getText().toString();
        } else {
            return "";
        }
    }

    private boolean contains(int[] list, int value){
        for (int id : list) {
            if (id == value) {
                return true;
            }
        }
        return false;
    }

    private boolean isPredictionEdit(int id){
        int[] editThinkPlayerIds = {
                R.id.editThinkPlayer1,
                R.id.editThinkPlayer2,
                R.id.editThinkPlayer3,
                R.id.editThinkPlayer4,
                R.id.editThinkPlayer5,
                R.id.editThinkPlayer6
        };
        return this.contains(editThinkPlayerIds, id);
    }

    private boolean isResultEdit(int id){
        int[] editGetWinPlayerIds = {
                R.id.editGetWinPlayer1,
                R.id.editGetWinPlayer2,
                R.id.editGetWinPlayer3,
                R.id.editGetWinPlayer4,
                R.id.editGetWinPlayer5,
                R.id.editGetWinPlayer6
        };
        return this.contains(editGetWinPlayerIds, id);
    }

    private void setScore(int editId){
        if (this.isResultEdit(editId) || this.isPredictionEdit(editId)){
            int textId = this.getScoreTextViewId(editId);
            if (textId != -1) {
                TextView textview = getView().findViewById(textId);
                String playername = this.getPlayername(editId);
                int result = WizardGame.getInstance().getScore(playername);
                if (result == -1){
                    textview.setText("?");
                } else {
                    textview.setText(Integer.toString(result));
                }
            }
        }
    }

    private int getScoreTextViewId(int id){
        if (id == R.id.editGetWinPlayer1 || id == R.id.editThinkPlayer1){
            return R.id.actPointsPlayer1;
        } else if (id == R.id.editGetWinPlayer2 || id == R.id.editThinkPlayer2){
            return R.id.actPointsPlayer2;
        } else if (id == R.id.editGetWinPlayer3 || id == R.id.editThinkPlayer3){
            return R.id.actPointsPlayer3;
        } else if (id == R.id.editGetWinPlayer4 || id == R.id.editThinkPlayer4){
            return R.id.actPointsPlayer4;
        } else if (id == R.id.editGetWinPlayer5 || id == R.id.editThinkPlayer5){
            return R.id.actPointsPlayer5;
        } else if (id == R.id.editGetWinPlayer6 || id == R.id.editThinkPlayer6){
            return R.id.actPointsPlayer6;
        } else {
            return -1;
        }
    }

    // Überprüfe, ob es noch leere Eingabefelder gibt
    private void checkForEmptyFields() {
        int[] editFieldIds = {
                R.id.editThinkPlayer1,
                R.id.editThinkPlayer2,
                R.id.editThinkPlayer3,
                R.id.editThinkPlayer4,
                R.id.editThinkPlayer5,
                R.id.editThinkPlayer6,
                R.id.editGetWinPlayer1,
                R.id.editGetWinPlayer2,
                R.id.editGetWinPlayer3,
                R.id.editGetWinPlayer4,
                R.id.editGetWinPlayer5,
                R.id.editGetWinPlayer6,
        };

        View rootView = getView();
        if (rootView == null) return;

        boolean hasEmptyFields = false;

        for (int id : editFieldIds) {
            EditText editText = rootView.findViewById(id);
            if (editText != null && editText.isShown()) {
                String text = editText.getText().toString().trim();
                hasEmptyFields = hasEmptyFields || (text.contains("?") || text.isEmpty());
            }
        }

        // Ergebnis ausgeben oder Aktion ausführen
        Button nextRound = rootView.findViewById(R.id.nextRoundBtn);
        if (!hasEmptyFields) {
            nextRound.setVisibility(View.VISIBLE);
            if (WizardGame.getInstance().isFinished() || !WizardGame.getInstance().isRunning()){
                nextRound.setVisibility(View.INVISIBLE);
                //TODO: Meldung unten -> Spiel Beendet - Sieger "Name"
            }
        } else {
            nextRound.setVisibility(View.INVISIBLE);
        }
        if (!this.isValidNumberOfResult()) {
            this.showAlertWrongNumberOfResults();
        }
    }

    private boolean isValidNumberOfResult(){
        int[] editGetWinPlayerIds = {
                R.id.editGetWinPlayer1,
                R.id.editGetWinPlayer2,
                R.id.editGetWinPlayer3,
                R.id.editGetWinPlayer4,
                R.id.editGetWinPlayer5,
                R.id.editGetWinPlayer6
        };
        int sum = 0;
        for (int id: editGetWinPlayerIds){
            EditText editText = getView().findViewById(id);
            if (!editText.isShown() || !this.isNumeric(editText.getText().toString())){
               continue;
            }
            int value = Integer.parseInt(editText.getText().toString());
            sum += value;
        }

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
        boolean masterMode = sharedPreferences.getBoolean("master_mode", false);
        int actRound = WizardGame.getInstance().getRoundNumber();

        if (sum < actRound) {
            return true;
        }
        return  sum == actRound || (masterMode && actRound - sum == 1);
    }

    public boolean isNumeric(String value) {
        return value.matches("-?\\d+");
    }

    private void showAlertWrongNumberOfResults() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.attention)
                .setMessage(R.string.wrong_number_of_results)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Schließt den Dialog
                    }
                })
                .show();
    }


//    private void setupEditTextListeners() {
//        // IDs der Eingabefelder
//        int[] editFieldIds = {
//                R.id.editThinkPlayer1,
//                R.id.editThinkPlayer2,
//                R.id.editThinkPlayer3,
//                R.id.editThinkPlayer4,
//                R.id.editThinkPlayer5,
//                R.id.editThinkPlayer6
//        };
//
//        View rootView = getView();
//        if (rootView == null) return;
//
//        for (int id : editFieldIds) {
//            EditText editText = rootView.findViewById(id);
//            if (editText != null) {
//                editText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                        // Vor der Textänderung (optional)
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        // Während der Textänderung
//                        System.out.println("Text geändert in " + id + ": " + s.toString());
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        // Nach der Textänderung
//
//                    }
//                });
//            }
//        }
//    }

}