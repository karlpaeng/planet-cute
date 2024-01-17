package dev.karl.planetcute;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;


public class PlanetGame extends AppCompatActivity{

    private  static final int mainNumber = 7;
    private static final int planetCoef = 72;
    private static final int planetCoefW = 142;
    private static final int planetCoefE = 212;
    private int planetPosition1 = 5;
    private int planetPosition2 = 5;
    private int planetPosition3 = 5;
    private final int[] planetSlotArray = {1, 2, 3, 4, 5, 6, 7};

    private RecyclerView planetRv1;
    private RecyclerView planetRv2;
    private RecyclerView planetRv3;
    private PlanetCustomManager planetLayoutMgr1;
    private PlanetCustomManager planetLayoutMgr2;
    private PlanetCustomManager planetLayoutMgr3;


    private TextView planetJackpot;
    private TextView planetBalance;
    private TextView planetBet;

    int planetMyCoins_val;
    int planetBet_val;
    int planetJackpot_val;

    private boolean planetFirstRun;
    int stillSpinning;

    private PlanetMechanics planetGameLogic;

    private SharedPreferences planetPref;
    public MediaPlayer planetBtnSound;
    public MediaPlayer settSound;
    public MediaPlayer spinSound;
    public MediaPlayer closeSound;
    public MediaPlayer planetWin;
    public MediaPlayer planetBgsound;
    public static final String PLANET_PREFS_NAME = "FirstRun";


    private int planetPlaymusic;
    private int planetPlaysound;
    private int playsoundTemp;
    private ImageView planet_music_off;
    private ImageView planet_music_on;
    private ImageView planet_soundon;
    private ImageView planet_soundoff;

    Toast win_toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton planet_minusButton;
        ImageButton planet_plusButton;
        SpinnerAdapter planet_adapter;
        ImageView planet_settingsButton;
        ImageButton planet_spinButton;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_planet_game);

        getWindow().setStatusBarColor(ContextCompat.getColor(PlanetGame.this, R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(PlanetGame.this, R.color.black));

        planetBgsound = MediaPlayer.create(this,R.raw.bullet_train_fantasy_loop);
        planetBgsound.setLooping(true);
        planetBtnSound = MediaPlayer.create(this, R.raw.btn);
        closeSound = MediaPlayer.create(this, R.raw.close_sound);
        settSound = MediaPlayer.create(this, R.raw.settings_sound);
        spinSound = MediaPlayer.create(this, R.raw.spin_sound);
        planetWin = MediaPlayer.create(this, R.raw.win_sound);

        planetPref = this.getSharedPreferences(PLANET_PREFS_NAME, Context.MODE_PRIVATE);
        planetFirstRun = planetPref.getBoolean("firstRun", true);

        stillSpinning = 3;
        if (planetFirstRun) {

            planetPlaymusic = 1;
            planetPlaysound = 1;

            SharedPreferences.Editor editor = planetPref.edit();
            editor.putBoolean("firstRun", false);
            editor.putInt("music", 1);
            editor.putInt("sound", 1);
            editor.apply();
        } else {
            planetPlaymusic = planetPref.getInt("music", 1);
            planetPlaysound = planetPref.getInt("sound", 1);
            planet_checkmusic();


        }

        playsoundTemp = planetPlaysound;
        Log.d("MUSIC",String.valueOf(planetPlaymusic));

        //Initializations
        planetGameLogic = new PlanetMechanics();
        planet_settingsButton = findViewById(R.id.settingsPlanet);
        planet_spinButton = findViewById(R.id.spinButtonPlanet);
        planet_plusButton = findViewById(R.id.plusButtonPlanet);
        planet_minusButton = findViewById(R.id.minusButtonPlanet);
        planetJackpot = findViewById(R.id.energyBallPlanet);
        planetBalance = findViewById(R.id.energyPlanet);
        planetBet = findViewById(R.id.betPlanet);
        planet_adapter = new SpinnerAdapter();

        //RecyclerView settings
        planetRv1 = findViewById(R.id.spinner1Planet);
        planetRv2 = findViewById(R.id.spinner2Planet);
        planetRv3 = findViewById(R.id.spinner3Planet);
        planetRv1.setHasFixedSize(true);
        planetRv2.setHasFixedSize(true);
        planetRv3.setHasFixedSize(true);

        planetLayoutMgr1 = new PlanetCustomManager(this);
        planetLayoutMgr1.setScrollable(false);
        planetRv1.setLayoutManager(planetLayoutMgr1);
        planetLayoutMgr2 = new PlanetCustomManager(this);
        planetLayoutMgr2.setScrollable(false);
        planetRv2.setLayoutManager(planetLayoutMgr2);
        planetLayoutMgr3 = new PlanetCustomManager(this);
        planetLayoutMgr3.setScrollable(false);
        planetRv3.setLayoutManager(planetLayoutMgr3);

        planetRv1.setAdapter(planet_adapter);
        planetRv2.setAdapter(planet_adapter);
        planetRv3.setAdapter(planet_adapter);
        planetRv1.scrollToPosition(planetPosition1);
        planetRv2.scrollToPosition(planetPosition2);
        planetRv3.scrollToPosition(planetPosition3);

        planet_setText();
        planet_updateText();

        //RecyclerView listeners
        planetRv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    planetRv1.scrollToPosition(planetGameLogic.getPositionPlanet(0));
                    planetLayoutMgr1.setScrollable(false);

                    stillSpinning++;
                }
            }
        });

        planetRv2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    planetRv2.scrollToPosition(planetGameLogic.getPositionPlanet(1));
                    planetLayoutMgr2.setScrollable(false);

                    stillSpinning++;
                }
            }
        });
        planetRv3.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    planetRv3.scrollToPosition(planetGameLogic.getPositionPlanet(2));
                    planetLayoutMgr3.setScrollable(false);
                    planet_updateText();
                    if (planetGameLogic.getPlanet_hasWon()) {
                        if (planetPlaysound == 1) {
                            planetWin.seekTo(0);
                            planetWin.start();
                        }
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.planet_win_splash,findViewById(R.id.win_splash));
                        TextView winCoins = layout.findViewById(R.id.planet_win_coins);
                        winCoins.setText(planetGameLogic.getPlanet_prize());
                        win_toast = new Toast(PlanetGame.this);
                        win_toast.setDuration(Toast.LENGTH_SHORT);
                        win_toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        win_toast.setView(layout);
                        win_toast.show();
                        planetGameLogic.setPlanet_hasWon(false);

                    }
                    stillSpinning++;
                }
            }
        });

        //Button listeners
        planet_spinButton.setOnClickListener(v -> {
            if(stillSpinning != 3){
            }else {
                stillSpinning = 0;
                planetGameLogic.deductBetFromCoinsPlanet();
                planet_updateText();
                if(planetPlaysound == 1){
                    spinSound.seekTo(0);
                    spinSound.start();
                }
                planetLayoutMgr1.setScrollable(true);
                planetLayoutMgr2.setScrollable(true);
                planetLayoutMgr3.setScrollable(true);
                planetGameLogic.getSpinResultsPlanet();
                planetPosition1 = planetGameLogic.getPositionPlanet(0) + planetCoef;
                planetPosition2 = planetGameLogic.getPositionPlanet(1) + planetCoefW;
                planetPosition3 = planetGameLogic.getPositionPlanet(2) + planetCoefE;
                planetRv1.smoothScrollToPosition(planetPosition1);
                planetRv2.smoothScrollToPosition(planetPosition2);
                planetRv3.smoothScrollToPosition(planetPosition3);
            }

        });

        planet_plusButton.setOnClickListener(v -> {
            if(planetPlaysound == 1){
                planetBtnSound.seekTo(0);
                planetBtnSound.start();
            }
            planetGameLogic.betUpPlanet();
            planet_updateText();
        });

        planet_minusButton.setOnClickListener(v -> {
            if(planetPlaysound == 1){
                planetBtnSound.seekTo(0);
                planetBtnSound.start();
            }
            planetGameLogic.betDownPlanet();
            planet_updateText();
        });

        planet_settingsButton.setOnClickListener(v -> {
            if(planetPlaysound == 1){
                settSound.seekTo(0);
                settSound.start();
            }
            planet_showSettingsDialog();
        });

    }


    private void planet_setText(){
        if(planetFirstRun){
            planetGameLogic.setPlanet_myCoins(1000);
            planetGameLogic.setPlanet_bet(5);
            planetGameLogic.setPlanet_jackpot(100000);

            SharedPreferences.Editor editor = planetPref.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

        }else {
            String coins = planetPref.getString("coins","");
            String bet = planetPref.getString("bet","");
            String jackpot = planetPref.getString("jackpot","");
            Log.d("COINS",coins);
            planetMyCoins_val = Integer.parseInt(coins);
            planetBet_val = Integer.parseInt(bet);
            planetJackpot_val = Integer.parseInt(jackpot);
            planetGameLogic.setPlanet_myCoins(planetMyCoins_val);
            planetGameLogic.setPlanet_bet(planetBet_val);
            planetGameLogic.setPlanet_jackpot(planetJackpot_val);
        }
    }

    private void planet_updateText() {
        planetJackpot.setText(planetGameLogic.getPlanet_jackpot());
        planetBalance.setText(planetGameLogic.getPlanet_myCoins());
        planetBet.setText(planetGameLogic.getPlanet_bet());

        SharedPreferences.Editor editor = planetPref.edit();
        editor.putString("coins", planetGameLogic.getPlanet_myCoins());
        editor.putString("bet", planetGameLogic.getPlanet_bet());
        editor.putString("jackpot", planetGameLogic.getPlanet_jackpot());
        editor.apply();
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;

        public ItemViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.planet_spinner_item);
        }
    }

    private class SpinnerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(PlanetGame.this);
            View view = layoutInflater.inflate(R.layout.planet_spinner_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            int i = position < 7 ? position : position % mainNumber;
            switch (planetSlotArray[i]) {
                case 1:
                    holder.pic.setImageResource(R.drawable.combination_1);
                    break;
                case 2:
                    holder.pic.setImageResource(R.drawable.combination_2);
                    break;
                case 3:
                    holder.pic.setImageResource(R.drawable.combination_3);
                    break;
                case 4:
                    holder.pic.setImageResource(R.drawable.combination_4);
                    break;
                case 5:
                    holder.pic.setImageResource(R.drawable.combination_5);
                    break;
                case 6:
                    holder.pic.setImageResource(R.drawable.combination_6);
                    break;
                case 7:
                    holder.pic.setImageResource(R.drawable.combination_7);
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }

    private void planet_showSettingsDialog() {
        final Dialog dialog;

        dialog = new Dialog(this, R.style.WinDialog);
        Objects.requireNonNull(dialog.getWindow()).setContentView(R.layout.planet_settings);

        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        ImageView close = dialog.findViewById(R.id.closePlanet);
        close.setOnClickListener(v -> {
            if (planetPlaysound == 1) {
                closeSound.seekTo(0);
                closeSound.start();
            }
            dialog.dismiss();
        }); // Close the dialog when the close button is clicked

        planet_music_on = dialog.findViewById(R.id.planet_music_on);
        planet_music_on.setOnClickListener(v -> {
            planetPlaymusic = 0;
            planet_checkmusic();
            planet_music_on.setVisibility(View.INVISIBLE);
            planet_music_off.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = planetPref.edit();
            editor.putInt("music", 0);
            editor.apply();
        });

        planet_music_off =  dialog.findViewById(R.id.planet_music_off);
        planet_music_off.setOnClickListener(v -> {
            planetPlaymusic = 1;
            planetBgsound.start();
            planet_music_on.setVisibility(View.VISIBLE);
            planet_music_off.setVisibility(View.INVISIBLE);
            SharedPreferences.Editor editor = planetPref.edit();
            editor.putInt("music", 1);
            editor.apply();
        });

        planet_soundon = dialog.findViewById(R.id.planet_sounds_on);
        planet_soundon.setOnClickListener(v -> {
            planetPlaysound = 0;
            planet_soundon.setVisibility(View.INVISIBLE);
            planet_soundoff.setVisibility(View.VISIBLE);
            SharedPreferences.Editor editor = planetPref.edit();
            editor.putInt("sound", 0);
            editor.apply();
        });

        planet_soundoff = dialog.findViewById(R.id.planet_sounds_off);
        planet_soundoff.setOnClickListener(v -> {
            planetPlaysound = 1;
            planet_soundon.setVisibility(View.VISIBLE);
            planet_soundoff.setVisibility(View.INVISIBLE);
            SharedPreferences.Editor editor = planetPref.edit();
            editor.putInt("sound", 1);
            editor.apply();
        });

        planet_checkmusicdraw();
        planet_checksounddraw();

        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        planetBgsound.pause();
        planetWin.pause();
        planetWin.seekTo(0);
        planetBtnSound.pause();
        planetBtnSound.seekTo(0);

        if (win_toast != null) {
            win_toast.cancel();
        }

        playsoundTemp = planetPlaysound;
        planetPlaysound = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        planet_checkmusic();
        planetPlaysound = playsoundTemp;
    }

    private void planet_checkmusic(){
        if (planetPlaymusic == 1){
            planetBgsound.start();
        }
        else {
            planetBgsound.pause();
        }

    }

    private void planet_checkmusicdraw(){
        if (planetPlaymusic == 1){
            planet_music_on.setVisibility(View.VISIBLE);
            planet_music_off.setVisibility(View.INVISIBLE);
        }
        else {
            planet_music_on.setVisibility(View.INVISIBLE);
            planet_music_off.setVisibility(View.VISIBLE);
        }
    }

    private void planet_checksounddraw(){
        if (planetPlaysound == 1){
            planet_soundon.setVisibility(View.VISIBLE);
            planet_soundoff.setVisibility(View.INVISIBLE);
        }
        else {
            planet_soundon.setVisibility(View.INVISIBLE);
            planet_soundoff.setVisibility(View.VISIBLE);
        }
    }


}

