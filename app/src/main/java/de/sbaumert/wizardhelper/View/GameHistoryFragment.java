package de.sbaumert.wizardhelper.View;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.sbaumert.wizardhelper.Controller.WizardGame;
import de.sbaumert.wizardhelper.R;
import de.sbaumert.wizardhelper.databinding.FragmentGameHistoryBinding;

import java.util.List;

public class GameHistoryFragment extends Fragment {

    private static final int DATA_CELL_WIDTH_DP = 56;
    private static final int ROUND_CELL_WIDTH_DP = 64;
    private static final int CELL_HEIGHT_DP = 48;

    private FragmentGameHistoryBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentGameHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        renderHistory(WizardGame.getInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
        renderHistory(WizardGame.getInstance());
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void renderHistory(WizardGame game) {
        binding.historyTable.removeAllViews();
        if (!game.isRunning()) {
            return;
        }

        List<Integer> playerIds = game.getPlayerIds();
        addPlayerHeader(playerIds, game);
        addIconHeader(playerIds, game);

        for (int round = 0; round < game.getMaxRoundNumber(); round++) {
            addRound(round, playerIds, game);
        }
    }

    private void addPlayerHeader(List<Integer> playerIds, WizardGame game) {
        TableRow row = new TableRow(requireContext());
        row.addView(createTextCell(getString(R.string.history_round), true, ROUND_CELL_WIDTH_DP, 1,
                "history_round_header"));

        for (int playerIndex = 0; playerIndex < playerIds.size(); playerIndex++) {
            int playerId = playerIds.get(playerIndex);
            row.addView(createTextCell(game.getPlayerName(playerId), true,
                    DATA_CELL_WIDTH_DP * 3, 3, "history_player_" + playerIndex));
        }
        binding.historyTable.addView(row);
    }

    private void addIconHeader(List<Integer> playerIds, WizardGame game) {
        TableRow row = new TableRow(requireContext());
        row.addView(createTextCell("", true, ROUND_CELL_WIDTH_DP, 1, "history_round_header_icons"));

        for (int playerIndex = 0; playerIndex < playerIds.size(); playerIndex++) {
            String playerName = game.getPlayerName(playerIds.get(playerIndex));
            row.addView(createIconCell(
                    R.drawable.think,
                    getString(R.string.history_prediction_description, playerName),
                    "history_icon_" + playerIndex + "_prediction"
            ));
            row.addView(createIconCell(
                    R.drawable.get_win,
                    getString(R.string.history_result_description, playerName),
                    "history_icon_" + playerIndex + "_result"
            ));
            row.addView(createIconCell(
                    R.drawable.trophy,
                    getString(R.string.history_score_description, playerName),
                    "history_icon_" + playerIndex + "_score"
            ));
        }
        binding.historyTable.addView(row);
    }

    private void addRound(int round, List<Integer> playerIds, WizardGame game) {
        TableRow row = new TableRow(requireContext());
        boolean isCurrentRound = round == game.getRoundNumber();
        TextView roundCell = createTextCell(
                Integer.toString(round + 1),
                true,
                ROUND_CELL_WIDTH_DP,
                1,
                "history_round_" + round
        );
        applyCurrentRoundStyle(roundCell, isCurrentRound);
        row.addView(roundCell);

        for (int playerIndex = 0; playerIndex < playerIds.size(); playerIndex++) {
            int playerId = playerIds.get(playerIndex);
            String tagPrefix = "history_" + round + "_" + playerIndex;
            TextView predictionCell = createTextCell(
                    formatValue(game.getPrediction(playerId, round)),
                    false, DATA_CELL_WIDTH_DP, 1, tagPrefix + "_prediction");
            TextView resultCell = createTextCell(
                    formatValue(game.getResult(playerId, round)),
                    false, DATA_CELL_WIDTH_DP, 1, tagPrefix + "_result");
            TextView scoreCell = createTextCell(
                    formatValue(game.getScore(playerId, round)),
                    false, DATA_CELL_WIDTH_DP, 1, tagPrefix + "_score");
            applyCurrentRoundStyle(predictionCell, isCurrentRound);
            applyCurrentRoundStyle(resultCell, isCurrentRound);
            applyCurrentRoundStyle(scoreCell, isCurrentRound);
            row.addView(predictionCell);
            row.addView(resultCell);
            row.addView(scoreCell);
        }
        binding.historyTable.addView(row);
    }

    private void applyCurrentRoundStyle(TextView cell, boolean isCurrentRound) {
        cell.setSelected(isCurrentRound);
        if (isCurrentRound) {
            cell.setBackgroundResource(R.drawable.history_current_round_background);
        }
    }

    private TextView createTextCell(
            String text,
            boolean header,
            int widthDp,
            int span,
            String tag
    ) {
        TextView cell = new TextView(requireContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(dp(widthDp), dp(CELL_HEIGHT_DP));
        params.span = span;
        cell.setLayoutParams(params);
        cell.setGravity(Gravity.CENTER);
        cell.setText(text);
        cell.setTextColor(requireContext().getColor(R.color.text_color));
        cell.setTextSize(header ? 18 : 17);
        cell.setTypeface(Typeface.SERIF, header ? Typeface.BOLD : Typeface.NORMAL);
        cell.setBackgroundResource(header
                ? R.drawable.history_header_background
                : R.drawable.history_cell_background);
        cell.setTag(tag);
        return cell;
    }

    private ImageView createIconCell(int drawable, String description, String tag) {
        ImageView cell = new ImageView(requireContext());
        cell.setLayoutParams(new TableRow.LayoutParams(dp(DATA_CELL_WIDTH_DP), dp(CELL_HEIGHT_DP)));
        cell.setImageResource(drawable);
        cell.setContentDescription(description);
        cell.setPadding(dp(10), dp(8), dp(10), dp(8));
        cell.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        cell.setBackgroundResource(R.drawable.history_header_background);
        cell.setTag(tag);
        return cell;
    }

    private String formatValue(int value) {
        return value == -1 ? "?" : Integer.toString(value);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
