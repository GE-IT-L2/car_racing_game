package model.game;

public class PlayerStatistics {

    private final String playerName;
    private int gamesPlayed;
    private double absoluteRecord;
    private double totalScoreSum;
    private int wins;
    private double cumulativeDistance;

    public PlayerStatistics(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }
        this.playerName         = playerName;
        this.gamesPlayed        = 0;
        this.absoluteRecord     = 0.0;
        this.totalScoreSum      = 0.0;
        this.wins               = 0;
        this.cumulativeDistance = 0.0;
    }

    public PlayerStatistics(String playerName,
                            int gamesPlayed,
                            double absoluteRecord,
                            double totalScoreSum,
                            int wins,
                            double cumulativeDistance) {
        this(playerName);
        this.gamesPlayed        = gamesPlayed;
        this.absoluteRecord     = absoluteRecord;
        this.totalScoreSum      = totalScoreSum;
        this.wins               = wins;
        this.cumulativeDistance = cumulativeDistance;
    }

    public void recordMode1Game(Score score) {
        recordGame(score.getDistance(), false);
    }

    public void recordMode2Game(Score score, boolean win) {
        recordGame(score.getDistance(), win);
    }

    private void recordGame(double distanceAchieved, boolean win) {
        gamesPlayed++;
        totalScoreSum      += distanceAchieved;
        cumulativeDistance += distanceAchieved;

        if (distanceAchieved > absoluteRecord) {
            absoluteRecord = distanceAchieved;
        }

        if (win) {
            wins++;
        }
    }

    public void reset() {
        gamesPlayed        = 0;
        absoluteRecord     = 0.0;
        totalScoreSum      = 0.0;
        wins               = 0;
        cumulativeDistance = 0.0;
    }

    public double getAverageScore() {
        if (gamesPlayed == 0) return 0.0;
        return totalScoreSum / gamesPlayed;
    }

    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) wins / gamesPlayed;
    }

    public String getPlayerName()           { return playerName; }
    public int    getGamesPlayed()          { return gamesPlayed; }
    public double getAbsoluteRecord()       { return absoluteRecord; }
    public int    getAbsoluteRecordInt()    { return (int) absoluteRecord; }
    public int    getWins()                 { return wins; }
    public double getCumulativeDistance()   { return cumulativeDistance; }
    public double getTotalScoreSum()        { return totalScoreSum; }

    @Override
    public String toString() {
        return String.format(
            "PlayerStatistics{" +
            "player='%s', games=%d, record=%dm, " +
            "average=%.1fm, wins=%d (%.0f%%), cumulative=%.0fm}",
            playerName,
            gamesPlayed,
            getAbsoluteRecordInt(),
            getAverageScore(),
            wins,
            getWinRate() * 100,
            cumulativeDistance
        );
    }
}
