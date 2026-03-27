package model.game;

public class Score {

    private int score;
    private int meilleurScore;
    private int partiesJouees;
    private long totalScoreCumule;

    public Score() {
        score            = 0;
        meilleurScore    = 0;
        partiesJouees    = 0;
        totalScoreCumule = 0;
    }

    public void ajouterPoints(int points) {
        score += points;
        if (score > meilleurScore) {
            meilleurScore = score;
        }
    }

    public void reinitialiser() {
        score = 0;
    }

    public void finDePartie() {
        partiesJouees++;
        totalScoreCumule += score;
        if (score > meilleurScore) {
            meilleurScore = score;
        }
    }

    public int getScore()         { return score; }
    public int getMeilleurScore() { return meilleurScore; }
    public int getPartiesJouees() { return partiesJouees; }
    public double getScoreMoyen() {
        return partiesJouees == 0 ? 0 : (double) totalScoreCumule / partiesJouees;
    }

    @Override
    public String toString() {
        return String.format("Score: %d pts | Record: %d pts | Parties: %d | Moyenne: %.1f",
                score, meilleurScore, partiesJouees, getScoreMoyen());
    }
}
