package model.game;

/**
 * Classe pour tracker les statistiques du joueur au cours d'une partie.
 */
public class StatistiquesJoueur {
    private String nomJoueur;
    private int score;
    private double distance;
    private int obsctaclesEvites;
    private int collisions;
    private long tempsJeu; // en ms
    private int vitesseMoyenne;
    private int niveau;

    public StatistiquesJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
        this.score = 0;
        this.distance = 0;
        this.obsctaclesEvites = 0;
        this.collisions = 0;
        this.tempsJeu = 0;
        this.vitesseMoyenne = 0;
        this.niveau = 1;
    }

    // Getters et Setters
    public String getNomJoueur() { return nomJoueur; }

    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }
    public void setScore(int score) { this.score = score; }

    public double getDistance() { return distance; }
    public void addDistance(double d) { this.distance += d; }
    public void setDistance(double distance) { this.distance = distance; }

    public int getObstaclesEvites() { return obsctaclesEvites; }
    public void addObstacleEvite() { this.obsctaclesEvites++; }

    public int getCollisions() { return collisions; }
    public void addCollision() { this.collisions++; }

    public long getTempsJeu() { return tempsJeu; }
    public void setTempsJeu(long temps) { this.tempsJeu = temps; }

    public int getVitesseMoyenne() { return vitesseMoyenne; }
    public void setVitesseMoyenne(int vitesse) { this.vitesseMoyenne = vitesse; }

    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }

    @Override
    public String toString() {
        return String.format("%s - Score: %d | Distance: %.0fm | Obstacles Evites: %d | Collisions: %d",
                nomJoueur, score, distance, obsctaclesEvites, collisions);
    }
}
