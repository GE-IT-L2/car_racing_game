package game;

public class Score {

    private int score;
    private int meilleurScore;
    private int partiesJouees;

    public Score() {
        score         = 0;
        meilleurScore = 0;
        partiesJouees = 0;
    }

    public void ajouterPoints(int points) {
        score += points;
        if (score > meilleurScore) {
            meilleurScore = score;
        }
    }

    public void reinitialiser() {
        score = 0;
        partiesJouees++;
    }

    public int getScore()         { return score; }
    public int getMeilleurScore() { return meilleurScore; }
    public int getPartiesJouees() { return partiesJouees; }

    @Override
    public String toString() {
        return String.format("Score: %d pts | Record: %d pts | Parties: %d",
                score, meilleurScore, partiesJouees);
    }
}
