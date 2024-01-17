package dev.karl.planetcute;



import android.content.Context;

public class PlanetMechanics {

    PlanetGame main ;

    private int planet_myCoins;
    private int planet_lines = 1;
    private int planet_bet;
    private int planet_jackpot;
    private int planet_prize = 0;
    private boolean planet_hasWon = false;

    private Context planet_context;

    private int[] planet_slots = {1, 1, 1};

    public int getRandomIntPlanet() {
        return (int) (Math.random() * 7 + 1);
    }

    public int getPositionPlanet(int i) {
        return planet_slots[i] + 5;
    }

    public void getSpinResultsPlanet() {
        planet_prize = 0;
        for (int i = 0; i < planet_slots.length; i++) {
            planet_slots[i] = getRandomIntPlanet();
        }

        if (planet_slots[0] == 7 || planet_slots[1] == 7 || planet_slots[2] == 7) {
            planet_hasWon = true;
            int i = 0;
            for (int a : planet_slots) {
                if (a == 7) i++;
            }

            switch (i) {
                case 1:
                    planet_prize = planet_bet * 5;
                    break;
                case 2:
                    planet_prize = planet_bet * 10;
                    break;
                case 3:
                    planet_prize = planet_jackpot;
                    planet_jackpot = 0;
                    break;
            }
            planet_myCoins += planet_prize;
        } else if (planet_slots[0] == planet_slots[1] && planet_slots[1] == planet_slots[2]) {
            planet_hasWon = true;
            switch (planet_slots[0]) {
                case 1:
                    planet_prize = planet_bet * 2;
                    planet_myCoins += planet_prize;
                    break;
                case 2:
                    planet_prize = planet_bet * 3;
                    planet_myCoins += planet_prize;
                    break;
                case 3:
                    planet_prize = planet_bet * 5;
                    planet_myCoins += planet_prize;
                    break;
                case 4:
                    planet_prize = planet_bet * 7;
                    planet_myCoins += planet_prize;
                    break;
                case 5:
                    planet_prize = planet_bet * 10;
                    planet_myCoins += planet_prize;
                    break;
                case 6:
                    planet_prize = planet_bet * 15;
                    planet_myCoins += planet_prize;
                    break;
            }
        } else {
//            myCoins -= bet;
            planet_jackpot += planet_bet;
        }
    }


    //Setters
    public void setPlanet_myCoins(int planet_myCoins) {
        this.planet_myCoins = planet_myCoins;
    }

    public void setPlanet_bet(int planet_bet) {
        this.planet_bet = planet_bet;
    }

    public void setPlanet_jackpot(int planet_jackpot) {
        this.planet_jackpot = planet_jackpot;
    }

    public void setPlanet_lines(int planet_lines) {
        this.planet_lines = planet_lines;
    }

    public void betUpPlanet() {
        if (planet_bet < 100) {
            planet_bet += 5;
//            myCoins -= 5;
        }
    }

    public void betDownPlanet() {
        if (planet_bet > 5) {
            planet_bet -= 5;
//            myCoins += 5;
        }
    }

    public void deductBetFromCoinsPlanet(){
        planet_myCoins -= planet_bet;
    }
    public void setPlanet_hasWon(boolean planet_hasWon) {
        this.planet_hasWon = planet_hasWon;
    }

    public void setPlanet_prize(int planet_prize) {
        this.planet_prize = planet_prize;
    }

    //Getters
    public String getPlanet_myCoins() {
        return Integer.toString(planet_myCoins);
    }

    public String getPlanet_lines() {
        return Integer.toString(planet_lines);
    }

    public String getPlanet_bet() {
        return Integer.toString(planet_bet);
    }

    public String getPlanet_jackpot() {
        return Integer.toString(planet_jackpot);
    }

    public boolean getPlanet_hasWon() {
        return planet_hasWon;
    }

    public String getPlanet_prize() {
        return Integer.toString(planet_prize);
    }
}

