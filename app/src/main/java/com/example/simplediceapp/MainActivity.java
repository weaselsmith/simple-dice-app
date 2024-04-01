package com.example.simplediceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowMetrics;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import com.example.simplediceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Spinner die_selector;
    // Button button;
    // ScrollView scroll;
    // TextView roll_log;
    String rollData = "";
    int dieCount = 1;
    int mod = 0;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // ViewGroup container = binding.linearLayout;

        // set number picker bounds and default


        // set up the dropdown menu
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.die_selection,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.dieSelector.setAdapter(adapter);

        /* container.addView(new View(this) {
            @Override
            protected void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
                computeWindowSizeClasses();
            }
        }); */

        // set roll button click listener
        binding.rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });

        // increase die count
        binding.moreDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dieCount++;
                String s = dieCount+"";
                binding.dieCountDisplay.setText(s);
            }
        });

        // decrease die count
        binding.lessDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dieCount--;
                if (dieCount < 1) {
                    dieCount = 1;
                }
                String s = dieCount+"";
                binding.dieCountDisplay.setText(s);
            }
        });

        binding.increaseMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod++;
                String s;
                if (mod > -1) {
                    s = " + " + mod;
                } else {
                    s = " - " + mod * -1;
                }
                binding.modDisplay.setText(s);
            }
        });

        binding.decreaseMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mod--;
                String s;
                if (mod > -1) {
                    s = " + " + mod;
                } else {
                    s = " - " + mod * -1;
                }
                binding.modDisplay.setText(s);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("dieCount", dieCount);
        savedInstanceState.putInt("mod", mod);
        savedInstanceState.putString("rollData", rollData);
        // binding.rollLog.setText(rollData);
    }

    private void rollDice() {
        DieRoll dice = new DieRoll(
                dieCount,
                getDieType(),
                mod,
                '\0'
        );
        dice.roll();
        updateLog(dice.getString());
    }

    private int getDieType() {
        String selection = binding.dieSelector.getSelectedItem().toString();
        selection = selection.substring(1);
        return Integer.parseInt(selection);
    }

    private void updateLog(String s) {
        rollData += "\n"+s+"\n";
        binding.rollLog.setText(rollData);
        // binding.scrollLog.scrollTo(0, binding.scrollLog.getBottom());
        // binding.scrollLog.scrollTo(0, ScrollView.FOCUS_DOWN);
        binding.scrollLog.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollLog.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }


}