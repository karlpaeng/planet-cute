package dev.karl.planetcute;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlanetHome extends AppCompatActivity {


    ImageButton planet_play, planet_exit, planet_policy;
    CheckBox planet_cb;

    private SharedPreferences planet_preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_home);

        getWindow().setStatusBarColor(ContextCompat.getColor(PlanetHome.this, R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(PlanetHome.this, R.color.black));

        planet_play = findViewById(R.id.ibPlayBtn);
        planet_exit = findViewById(R.id.ibExitBtn);
        planet_policy = findViewById(R.id.ibPolicyBtn);

        planet_cb = findViewById(R.id.cbIAgree);

        planet_preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        boolean accepted = planet_preferences.getBoolean("accepted", true);

        if (accepted){
            planet_cb.setChecked(true);
        }else{
            planet_cb.setChecked(false);
        }

        planet_play.setOnClickListener(view -> {
            if (planet_cb.isChecked()){
                moveToPlanetGameActivity();
            }else{
                alertDiaPlanet("Agreement with the Policy is required to play the game");
            }
        });

        planet_exit.setOnClickListener(view -> {
            finishAffinity();
        });

        planet_policy.setOnClickListener(view -> {
            Intent gameIntent = new Intent(this, PlanetPolicy.class);
            startActivity(gameIntent);
        });

        planet_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = planet_preferences.edit();
                if (planet_cb.isChecked()){
                    editor.putBoolean("accepted", true);
                }else{
                    editor.putBoolean("accepted", false);
                }
                editor.apply();
            }
        });
    }

    private void moveToPlanetGameActivity() {
        Intent intent = new Intent(PlanetHome.this, PlanetGame.class);
        startActivity(intent);
    }

    private void alertDiaPlanet(String txt){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlanetHome.this);
        View v = getLayoutInflater().inflate(R.layout.planet_dialog, null);

        TextView tv = v.findViewById(R.id.tvDialogTextPlanet);
        ImageButton ib = v.findViewById(R.id.ibCloseDialogPlanet);

        tv.setText(txt);


        builder.setView(v);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        ib.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
    }


}