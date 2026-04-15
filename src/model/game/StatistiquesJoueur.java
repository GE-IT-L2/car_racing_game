package model.game;

public class StatistiquesJoueur {
    private int nombreParties;
    private int scoreTotal;
    private int meilleurScore;

    public StatistiquesJoueur() {
        this.nombreParties = 0;
        this.scoreTotal = 0;
        this.meilleurScore = 0;
    }

    public void enregistrerPartie(int score) {
        nombreParties++;
        scoreTotal += score;
        if (score > meilleurScore) {
            meilleurScore = score;
        }
    }

    public double getScoreMoyen() {
        if (nombreParties == 0) return 0;
        return (double) scoreTotal / nombreParties;
    }

    public int getNombreParties()  { return nombreParties; }
    public int getMeilleurScore()  { return meilleurScore; }
    public int getScoreTotal()     { return scoreTotal; }

    @Override
    public String toString() {
        return String.format(
            "Parties: %d | Moyenne: %.1f pts | Record: %d pts",
            nombreParties, getScoreMoyen(), meilleurScore
        );
    }
}