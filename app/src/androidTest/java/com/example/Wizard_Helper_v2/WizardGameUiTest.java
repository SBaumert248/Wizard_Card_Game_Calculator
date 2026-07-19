package com.example.Wizard_Helper_v2;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.Wizard_Helper_v2.Controller.WizardGame;
import com.example.Wizard_Helper_v2.View.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WizardGameUiTest {

    private Context context;
    private ActivityScenario<MainActivity> scenario;

    @Before
    public void resetPersistentState() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.getSharedPreferences("Setting", Context.MODE_PRIVATE).edit().clear().commit();
        context.getSharedPreferences("Playernames", Context.MODE_PRIVATE).edit().clear().commit();
        context.deleteFile("game.json");
        WizardGame.getInstance().resetGame();
    }

    @After
    public void closeActivity() {
        if (scenario != null) {
            scenario.close();
        }
        WizardGame.getInstance().resetGame();
    }

    @Test
    public void defaultSettingsUseFourPlayersAndEnableMasterMode() {
        launchActivity();

        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.action_settings)).perform(click());

        onView(withId(R.id.rb_4Player)).check(matches(isChecked()));
        onView(withId(R.id.rb_3Player)).check(matches(isNotChecked()));
        onView(withId(R.id.s_master_mode)).check(matches(isChecked()));
    }

    @Test
    public void clearingARequiredResultHidesNextRoundAgain() {
        disableMasterMode();
        launchAndStartGame();

        enterRound(
                new String[]{"0", "0", "0", "0"},
                new String[]{"1", "0", "0", "0"}
        );
        onView(withId(R.id.nextRoundBtn)).perform(scrollTo()).check(matches(isDisplayed()));

        replace(R.id.editGetWinPlayer1, "");

        onView(withId(R.id.nextRoundBtn)).check(matches(not(isDisplayed())));
    }

    @Test
    public void endingGameClearsScoresBeforeNextGame() {
        disableMasterMode();
        launchAndStartGame();

        enterRound(
                new String[]{"1", "0", "0", "0"},
                new String[]{"1", "0", "0", "0"}
        );
        onView(withId(R.id.actPointsPlayer1)).check(matches(withText("30")));

        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.cancel_actual_game)).perform(click());
        onView(withId(R.id.startGameBtn)).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.actPointsPlayer1)).check(matches(withText("?")));
        onView(withId(R.id.textLastPointsPlayer1)).check(matches(not(isDisplayed())));
    }

    @Test
    public void runningGameIsRestoredWithRoundNameAndLastScore() {
        disableMasterMode();
        launchActivity();
        replace(R.id.namePlayer1, "Alice");
        onView(withId(R.id.startGameBtn)).perform(click());
        onView(withId(R.id.textActRound)).check(matches(withText("Runde: 1 / 15")));
        enterRound(
                new String[]{"1", "0", "0", "0"},
                new String[]{"1", "0", "0", "0"}
        );
        onView(withId(R.id.nextRoundBtn)).perform(scrollTo(), click());
        onView(withId(R.id.textActRound)).check(matches(withText("Runde: 2 / 15")));

        scenario.close();
        scenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.textActRound)).check(matches(withText("Runde: 2 / 15")));
        onView(withId(R.id.namePlayer1)).check(matches(withText("Alice")));
        onView(withId(R.id.textLastPointsPlayer1)).check(matches(withText("30")));
        onView(withId(R.id.startGameBtn)).check(matches(not(isDisplayed())));
    }

    @Test
    public void historyUsesPlayerCountAndMatchingRoundCount() {
        setPlayerCount(3);
        launchActivity();
        onView(withId(R.id.startGameBtn)).perform(click());
        openGameHistory();

        onView(withId(R.id.history_area)).check(matches(isDisplayed()));
        onView(withTagValue(is("history_player_0"))).check(matches(withText("Spieler1")));
        onView(withTagValue(is("history_player_2"))).check(matches(withText("Spieler3")));
        onView(withTagValue(is("history_player_3"))).check(doesNotExist());
        onView(withTagValue(is("history_round_19"))).check(matches(withText("20")));
        onView(withTagValue(is("history_round_20"))).check(doesNotExist());
    }

    @Test
    public void historyShowsIconsAndCurrentRoundValues() {
        disableMasterMode();
        launchAndStartGame();
        enterRound(
                new String[]{"1", "0", "0", "0"},
                new String[]{"1", "0", "0", "0"}
        );
        openGameHistory();

        onView(withContentDescription("Ansage von Spieler1")).check(matches(isDisplayed()));
        onView(withContentDescription("Erhaltene Stiche von Spieler1")).check(matches(isDisplayed()));
        onView(withContentDescription("Punktestand von Spieler1")).check(matches(isDisplayed()));
        onView(withTagValue(is("history_0_0_prediction"))).check(matches(withText("1")));
        onView(withTagValue(is("history_0_0_result"))).check(matches(withText("1")));
        onView(withTagValue(is("history_0_0_score"))).check(matches(withText("30")));
        onView(withTagValue(is("history_1_0_prediction"))).check(matches(withText("?")));
        onView(withTagValue(is("history_round_0"))).check(matches(isSelected()));
        onView(withTagValue(is("history_0_0_score"))).check(matches(isSelected()));
        onView(withTagValue(is("history_round_1"))).check(matches(not(isSelected())));

        pressBack();

        onView(withId(R.id.game_area)).check(matches(isDisplayed()));
        onView(withId(R.id.textActRound)).check(matches(withText("Runde: 1 / 15")));
        onView(withId(R.id.editThinkPlayer1)).check(matches(withText("1")));
        onView(withId(R.id.editGetWinPlayer1)).check(matches(withText("1")));
        onView(withId(R.id.actPointsPlayer1)).check(matches(withText("30")));
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.cancel_actual_game)).check(matches(isDisplayed()));
    }

    private void launchAndStartGame() {
        launchActivity();
        onView(withId(R.id.startGameBtn)).perform(click());
        onView(withId(R.id.textActRound)).check(matches(withText("Runde: 1 / 15")));
    }

    private void openGameHistory() {
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.game_history)).perform(click());
    }

    private void launchActivity() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    private void disableMasterMode() {
        context.getSharedPreferences("Setting", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("master_mode", false)
                .commit();
    }

    private void setPlayerCount(int playerCount) {
        context.getSharedPreferences("Setting", Context.MODE_PRIVATE)
                .edit()
                .putInt("player_count", playerCount)
                .commit();
    }

    private void enterRound(String[] predictions, String[] results) {
        int[] predictionIds = {
                R.id.editThinkPlayer1,
                R.id.editThinkPlayer2,
                R.id.editThinkPlayer3,
                R.id.editThinkPlayer4
        };
        int[] resultIds = {
                R.id.editGetWinPlayer1,
                R.id.editGetWinPlayer2,
                R.id.editGetWinPlayer3,
                R.id.editGetWinPlayer4
        };

        for (int index = 0; index < predictionIds.length; index++) {
            replace(predictionIds[index], predictions[index]);
            replace(resultIds[index], results[index]);
        }
    }

    private void replace(int viewId, String value) {
        onView(withId(viewId)).perform(scrollTo(), replaceText(value));
        closeSoftKeyboard();
    }
}
