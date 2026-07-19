package com.example.Wizard_Helper_v2.Model;

public final class RoundRules {

    private RoundRules() {
    }

    public static boolean isValidResultTotal(long resultTotal, int roundIndex, boolean masterMode) {
        if (roundIndex < 0 || resultTotal < 0) {
            return false;
        }

        long expectedTricks = (long) roundIndex + 1;
        return resultTotal == expectedTricks
                || (masterMode && resultTotal == expectedTricks - 1);
    }
}
