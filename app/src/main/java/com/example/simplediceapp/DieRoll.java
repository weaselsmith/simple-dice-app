package com.example.simplediceapp;

import java.io.PrintStream;
import java.util.Random;
// import java.lang.ref.*;


public class DieRoll {
    private final int[] results;
    private final int numDice;
    private final int numSides;
    private int sum;
    private int mod;
    private boolean diceOnly = false;
    private boolean meetTarget = false;
    private char advantage = 0;  // 1 = drop lowest; 2 = drop highest; 0 = neither
    private int target = 0;
    private int successes = 0;
    private final Random roller;  // RNG Object


    // constructor for dice only mode w advantage
    public DieRoll(int dice, int sides, char adv) {
        roller = new Random();
        results = new int[dice];
        numDice = dice;
        numSides = sides;
        advantage = adv;
        diceOnly = true;
    }

    // constructor for roll + mod = sum mode
    public DieRoll(int dice, int sides, int modifier, char adv) {
        roller = new Random();
        results = new int[dice];
        numDice = dice;
        numSides = sides;
        advantage = adv;
        mod = modifier;
    }

    // constructor for count successes over target mode with adv/disadv
    public DieRoll(char adv, int dice, int sides, int tgt) {
        roller = new Random();
        results = new int[dice];
        numDice = dice;
        numSides = sides;
        meetTarget = true;
        target = tgt;
        advantage = adv;
    }

    // "rolls" xdy + z
    public void roll() {
        for (int i = 0; i < numDice; i++) {
            results[i] = roller.nextInt(numSides) + 1;
            if (!diceOnly && !meetTarget) {
                sum += results[i];
            } else if (!diceOnly && results[i] >= target) {
                successes++;
            }
        }
        if (!diceOnly && !meetTarget) {
            sum += mod;
        }
        if (advantage == 1 && meetTarget) {
            if (min() >= target)
                successes--;
        } else if (advantage == 1 && !diceOnly) {
            removeLowest();
        } else if (advantage == 2 && meetTarget) {
            if (max() >= target)
                successes--;
        } else if (advantage == 2 && !diceOnly) {
            removeHighest();
        }
    }

    // returns minimum value in results
    private int min () {
        int min = results[0];
        for (int i = 1; i < numDice; i++) {
            if (results[i] < min)
                min = results[i];
        }
        return min;
    }

    // returns maximum value in results
    private int max () {
        int max = results[0];
        for (int i = 1; i < numDice; i++) {
            if (results[i] > max)
                max = results[i];
        }
        return max;
    }

    private int indexOfMin() {
        int min = results[0];
        int mindex = 0;
        for (int i = 1; i < numDice; i++) {
            if (results[i] < min) {
                min = results[i];
                mindex = i;
            }
        }
        return mindex;
    }

    private int indexOfMax() {
        int max = results[0];
        int maxdex = 0;
        for (int i = 1; i < numDice; i++) {
            if (results[i] > max) {
                max = results[i];
                maxdex = i;
            }
        }
        return maxdex;
    }

    private void removeLowest() {
        sum -= min();
    }

    private void removeHighest() {
        sum -= max();
    }

    public String getString() {
        String str = "    [ ";
        for (int i = 0; i < numDice; i++) {
            if ( (advantage == 1 && i == indexOfMin()) ||
                    (advantage == 2 && i == indexOfMax()) ) {
                str += "(";
                str += results[i];
                if (numSides == 20 && (results[i] == 20 || results[i] == 1))
                    str += "!";
                str += ")";
            } else {
                str += results[i];
                if (numSides == 20 && (results[i] == 20 || results[i] == 1))
                    str += "!";
            }
            // **********************
            if (i == numDice - 1) {
                str += " ]";
            } else {
                str += ", ";
            }
        }
        // ******************************
        if (mod > 0) {
            str += " + ";
            str += mod;
            str += " = ";
            str += sum;
        } else if (mod < 0) {
            str += " - ";
            str += mod * -1;
            str += " = ";
            str += sum;
        } else if (meetTarget) {
            str += " = ";
            str += successes;
            if (successes == 1) {
                str += " success";
            } else {
                str += " successes";
            }
        } else if (!diceOnly && numDice > 1) {
            str += " = ";
            str += sum;
        }
        // str += "\n";
        return str;
    }
}

