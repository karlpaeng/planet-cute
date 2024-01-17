package dev.karl.planetcute;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PlanetPolicy extends AppCompatActivity {

    private WebView planet_wvPolicy;
    private TextView planet_tvAccept;
    private SharedPreferences planet_preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_policy);

        getWindow().setStatusBarColor(ContextCompat.getColor(PlanetPolicy.this, R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(PlanetPolicy.this, R.color.black));

        planet_preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        planet_wvPolicy = findViewById(R.id.policyPlanet);
//        lin = findViewById(R.id.CL02);
        planet_tvAccept = findViewById(R.id.acceptPlanet);

        planet_wvPolicy.loadUrl("https://sites.google.com/view/planetary-cuties");

        planet_tvAccept.setOnClickListener(view -> { finish(); });

    }
}
