package dev.karl.planetcute;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class PlanetSplasher extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_splasher);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        VideoView videoView = findViewById(R.id.videoViewPlanet);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_vid);

        videoView.setVideoURI(videoUri);

        videoView.start();

        videoView.setOnCompletionListener(mediaPlayer -> {
            launchNextPlanetActivity();
        });
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            launchNextActivity();
//        }, 4000);
    }

    private void launchNextPlanetActivity() {
        Intent gameIntent = new Intent(this, PlanetHome.class);
        gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(gameIntent);
        finish();
    }
}
