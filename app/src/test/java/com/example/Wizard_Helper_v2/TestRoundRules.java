package com.example.Wizard_Helper_v2;

import com.example.Wizard_Helper_v2.Model.RoundRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestRoundRules {

    @Test
    void normal_mode_requires_exact_trick_total() {
        assertTrue(RoundRules.isValidResultTotal(1, 0, false));
        assertTrue(RoundRules.isValidResultTotal(10, 9, false));
        assertFalse(RoundRules.isValidResultTotal(0, 0, false));
        assertFalse(RoundRules.isValidResultTotal(9, 9, false));
        assertFalse(RoundRules.isValidResultTotal(11, 9, false));
    }

    @Test
    void master_mode_allows_exactly_one_missing_trick() {
        assertTrue(RoundRules.isValidResultTotal(0, 0, true));
        assertTrue(RoundRules.isValidResultTotal(9, 9, true));
        assertTrue(RoundRules.isValidResultTotal(10, 9, true));
        assertFalse(RoundRules.isValidResultTotal(8, 9, true));
        assertFalse(RoundRules.isValidResultTotal(11, 9, true));
    }

    @Test
    void invalid_inputs_are_rejected() {
        assertFalse(RoundRules.isValidResultTotal(-1, 0, true));
        assertFalse(RoundRules.isValidResultTotal(0, -1, true));
    }
}
