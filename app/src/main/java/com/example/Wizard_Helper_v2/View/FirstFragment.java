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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Wizard_Helper_v2.Controller.WizardGame;
import com.example.Wizard_Helper_v2.R;
import com.example.Wizard_Helper_v2.databinding.FragmentFirstBinding;

import java.util.HashMap;
import java.util.Map;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private int actRound = 1;
    private Map<Integer, String> originalValues = new HashMap<>();
    private boolean wrongNumberDialogShown = false;
    private boolean loadingPlayerDatas = false;

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
        if (index < 0 || index >= WizardGame.getInstance().numOfPlayer()){
            return sDefault;
        }
        return "fehlt";
//        return WizardGame.getInstance().getPlayers().get(index).getName();
    }

    public void loadLastGame(){
        // TODO: Wenn ein Spiel geladenen wird, werden die Punkte der letzten Runden in die aktuelle Runde geschrieben
        // TODO: Es soll nur die Punkte geschrieben werden wenn, die für die aktuelle Runde ist
        this.loadingPlayerDatas = true;

        if (WizardGame.getInstance().isRunning()) {
            // laden der Spielerdaten
            int lastRound = WizardGame.getInstance().getRoundNumber()-1;

            int id = R.id.namePlayer1;
            binding.editThinkPlayer1.setText(this.intToStr(WizardGame.getInstance().getActPrediction(id), "?"));
            binding.editGetWinPlayer1.setText(this.intToStr(WizardGame.getInstance().getActResult(id), "?"));
            binding.actPointsPlayer1.setText(this.intToStr(WizardGame.getInstance().getActScore(id), "?"));
            binding.textLastPointsPlayer1.setText(this.intToStr(WizardGame.getInstance().getScore(id, lastRound), "?"));

            id = R.id.namePlayer2;
            binding.editThinkPlayer2.setText(this.intToStr(WizardGame.getInstance().getActPrediction(id), "?"));
            binding.editGetWinPlayer2.setText(this.intToStr(WizardGame.getInstance().getActResult(id), "?"));
            binding.actPointsPlayer2.setText(this.intToStr(WizardGame.getInstance().getActScore(id), "?"));
            binding.textLastPointsPlayer2.setText(this.intToStr(WizardGame.getInstance().getScore(id, lastRound), "?"));

            id = R.id.namePlayer3;
            binding.editThinkPlayer3.setText(this.intToStr(WizardGame.getInstance().getActPrediction(id), "?"));
            binding.editGetWinPlayer3.setText(this.intToStr(WizardGame.getInstance().getActResult(id), "?"));
            binding.actPointsPlayer3.setText(this.intToStr(WizardGame.getInstance().getActScore(id), "?"));
            binding.textLastPointsPlayer3.setText(this.intToStr(WizardGame.getInstance().getScore(id, lastRound), "?"));

            id = R.id.namePlayer4;
            binding.editThinkPlayer4.setText(this.intToStr(WizardGame.getInstance().getActPrediction(id), "?"));
            binding.editGetWinPlayer4.setText(this.intToStr(WizardGame.getInstance().getActResult(id), "?"));
            binding.actPointsPlayer4.setText(this.intToStr(WizardGame.getInstance().getActScore(id), "?"));
            binding.textLastPointsPlayer4.setText(this.intToStr(WizardGame.getInstance().getScore(id, lastRound), "?"));

            id = R.id.namePlayer5;
            binding.editThinkPlayer5.setText(this.intToStr(WizardGame.getInstance().getActPrediction(id), "?"));
            binding.editGetWinPlayer5.setText(this.intToStr(WizardGame.getInstance().getActResult(id), "?"));
            binding.actPointsPlayer5.setText(this.intToStr(WizardGame.getInstance().getActScore(id), "?"));
            binding.textLastPointsPlayer5.setText(this.intToStr(WizardGame.getInstance().getScore(id, lastRound), "?"));

            id = R.id.namePlayer6;
            binding.editThinkPlayer6.setText(this.intToStr(WizardGame.getInstance().getActPrediction(id), "?"));
            binding.editGetWinPlayer6.setText(this.intToStr(WizardGame.getInstance().getActResult(id), "?"));
            binding.actPointsPlayer6.setText(this.intToStr(WizardGame.getInstance().getActScore(id), "?"));
            binding.textLastPointsPlayer6.setText(this.intToStr(WizardGame.getInstance().getScore(id, lastRound), "?"));

            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
            boolean showLastPoints = sharedPreferences.getBoolean("show_last_points", true);
            if (showLastPoints){
                this.showAllLastPoints();
            }
        }
        this.loadingPlayerDatas = false;
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
            this.setPlayerFields(WizardGame.getInstance().numOfPlayer(), true);
            this.setupTextChangeListeners();
            binding.nextRoundBtn.setVisibility(View.GONE);
            this.actRound = WizardGame.getInstance().getRoundNumber();
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
            boolean showLastPoints = sharedPreferences.getBoolean("show_last_points", true);
            if (showLastPoints){
                this.showAndFillAllLastPoints();
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
        System.out.println("Anzahl Spieler: !" + WizardGame.getInstance().numOfPlayer());

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
                int actRound = WizardGame.getInstance().getRoundNumber()+1;
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
                WizardGame.getInstance().addPlayer(playerName, playerNameIds[i]);
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

    private void setNextFocusId(EditText editText, int firstId){
        editText.setNextFocusForwardId(firstId);
        editText.setNextFocusDownId(firstId);
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
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

        if (playerCount == 3){
            this.setNextFocusId(binding.namePlayer3, binding.namePlayer1.getId());
            this.setNextFocusId(binding.editThinkPlayer3, binding.editThinkPlayer1.getId());
            this.setNextFocusId(binding.editGetWinPlayer3, binding.editGetWinPlayer1.getId());

        } else if (playerCount == 4) {
            this.setNextFocusId(binding.namePlayer4, binding.namePlayer1.getId());
            this.setNextFocusId(binding.editThinkPlayer4, binding.editThinkPlayer1.getId());
            this.setNextFocusId(binding.editGetWinPlayer4, binding.editGetWinPlayer1.getId());
        } else if (playerCount == 5) {
            this.setNextFocusId(binding.namePlayer5, binding.namePlayer1.getId());
            this.setNextFocusId(binding.editThinkPlayer5, binding.editThinkPlayer1.getId());
            this.setNextFocusId(binding.editGetWinPlayer5, binding.editGetWinPlayer1.getId());
        } else {
            this.setNextFocusId(binding.namePlayer6, binding.namePlayer1.getId());
            this.setNextFocusId(binding.editThinkPlayer6, binding.editThinkPlayer1.getId());
            this.setNextFocusId(binding.editGetWinPlayer6, binding.editGetWinPlayer1.getId());
        }
    }

    private void showAndFillAllLastPoints(){
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

    private void showAllLastPoints(){
        this.showLastRoundPoints(R.id.textLastPointsPlayer1);
        this.showLastRoundPoints(R.id.textLastPointsPlayer2);
        this.showLastRoundPoints(R.id.textLastPointsPlayer3);
        this.showLastRoundPoints(R.id.textLastPointsPlayer4);
        this.showLastRoundPoints(R.id.textLastPointsPlayer5);
        this.showLastRoundPoints(R.id.textLastPointsPlayer6);
    }

    private void showLastRoundPoints(int id){
        TextView lastShowScore = getView().findViewById(id);
        lastShowScore.setVisibility(View.VISIBLE);
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
            int actRound = WizardGame.getInstance().getRoundNumber()+1;
            int maxRound = WizardGame.getInstance().getMaxRoundNumber();
            String message = getString(R.string.act_player_rounds, actRound, maxRound);
            roundView.setText(message);
        } else {
            roundView.setText(R.string.choose_player);
        }

//        int firstId = binding.editThinkPlayer1.getId();
//        if (playerCount == 3){
//            binding.editThinkPlayer3.setNextFocusForwardId(firstId);
//            binding.editThinkPlayer3.setNextFocusDownId(firstId);
//            binding.editThinkPlayer3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        } else if (playerCount == 4) {
//            binding.editThinkPlayer4.setNextFocusForwardId(firstId);
//            binding.editThinkPlayer4.setNextFocusDownId(firstId);
//            binding.editThinkPlayer4.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        } else if (playerCount == 5) {
//            binding.editThinkPlayer5.setNextFocusForwardId(firstId);
//            binding.editThinkPlayer5.setNextFocusDownId(firstId);
//            binding.editThinkPlayer5.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        } else {
//            binding.editThinkPlayer6.setNextFocusForwardId(firstId);
//            binding.editThinkPlayer6.setNextFocusDownId(firstId);
//            binding.editThinkPlayer6.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        }
    }

    private Integer tryParseInt(String value){
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Eingabe: " + value);
            return null;
        }
    }

    private String getIdEditValue(int id){
        View rootView = getView();
        if (rootView == null)
            return "";
        EditText editText = rootView.findViewById(id);
        String value = "";
        if (editText != null) {
            value = editText.getText().toString();
        }
        if (tryParseInt(value) == null) {
            value = "";
        }
        return value;
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
                        int playerId = getPlayerId(id);

                        Integer value = tryParseInt(currentValue);
                        if (currentValue.isEmpty() || currentValue.contains("?") || (value == null)){
                            return;
                        }

                        int actRound = WizardGame.getInstance().getRoundNumber();

                        if (isPredictionEdit(id)) {
                            WizardGame.getInstance().setPrediction(playerId, value, actRound);
                        }
                        if (isResultEdit(id)) {
                            WizardGame.getInstance().setResult(playerId, value, actRound);
                        }
                        setScore(id);

                        if (WizardGame.getInstance().allPlayerDone()) {
                            checkForEmptyFields();
                        }
                    }
                });
            }
        }
    }

    private int getPlayerId(int editId){
        if (editId == R.id.editGetWinPlayer1 || editId == R.id.editThinkPlayer1 || editId == R.id.namePlayer1){
            return R.id.namePlayer1;
        } else if (editId == R.id.editGetWinPlayer2 || editId == R.id.editThinkPlayer2 || editId == R.id.namePlayer2){
            return R.id.namePlayer2;
        } else if (editId == R.id.editGetWinPlayer3 || editId == R.id.editThinkPlayer3 || editId == R.id.namePlayer3){
            return R.id.namePlayer3;
        } else if (editId == R.id.editGetWinPlayer4 || editId == R.id.editThinkPlayer4 || editId == R.id.namePlayer4){
            return R.id.namePlayer4;
        } else if (editId == R.id.editGetWinPlayer5 || editId == R.id.editThinkPlayer5 || editId == R.id.namePlayer5){
            return R.id.namePlayer5;
        } else if (editId == R.id.editGetWinPlayer6 || editId == R.id.editThinkPlayer6 || editId == R.id.namePlayer6){
            return R.id.namePlayer6;
        } else {
            return -1;
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
        //TODO: Bitch dein Rap ist kitsch nur ramtsch und schrott mach hier weiter du reimendes wort
        if (this.isResultEdit(editId) || this.isPredictionEdit(editId)){
            int textId = this.getScoreTextViewId(editId);
            if (textId != -1) {
                TextView textview = getView().findViewById(textId);
                int playerId = this.getPlayerId(editId);
                int actRound = WizardGame.getInstance().getRoundNumber();
                int result = WizardGame.getInstance().getScore(playerId, actRound);
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
        int[] editPlayerIds = {
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
                R.id.editGetWinPlayer6
        };

        if (this.loadingPlayerDatas) {
            return;
        }

        View rootView = getView();
        if (rootView == null) return;

        boolean hasEmptyFields = false;

        for (int id : editPlayerIds) {
            String value = getIdEditValue(id);
            EditText editText = rootView.findViewById(id);
            if (editText.isShown()) {
                //String text = editText.getText().toString().trim();
                //hasEmptyFields = hasEmptyFields || (value.contains("?") || text.isEmpty());
                hasEmptyFields = hasEmptyFields || value.isEmpty();
            }
        }

        // Ergebnis ausgeben oder Aktion ausführen
        Button nextRound = rootView.findViewById(R.id.nextRoundBtn);
        if (!hasEmptyFields) {
            nextRound.setVisibility(View.VISIBLE);
            if (WizardGame.getInstance().isFinished() || !WizardGame.getInstance().isRunning()) {
                nextRound.setVisibility(View.INVISIBLE);
                //TODO: Meldung unten -> Spiel Beendet - Sieger "Name"
            }
        } else return;

        // überprüfe die EditGetWin Felder
        if (!this.isValidNumberOfResult()) {
            nextRound.setVisibility(View.INVISIBLE);
            // Dialog nur einmal anzeigen
            if (!this.wrongNumberDialogShown) {
                this.wrongNumberDialogShown = true;
                this.showAlertWrongNumberOfResults();
            }
        } else {
            this.wrongNumberDialogShown = false;
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

        return sum == (actRound+1) || (masterMode && (actRound+1-sum) == 1);
    }

    public boolean isNumeric(String value) {
        return value.matches("-?\\d+");
    }

    private void showAlertWrongNumberOfResults() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.attention)
                .setMessage(R.string.wrong_number_of_results)
                .setOnDismissListener(d -> wrongNumberDialogShown = false)
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