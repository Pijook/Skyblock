package pl.trollcraft.Skyblock.worker;

public class Job {

    private int level;
    private int score;

    public Job(int level, int score){
        this.level = level;
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore(int amount){
        this.score = this.score + amount;
    }

    public void increaseLevel(int amount){
        this.level = this.level + amount;
    }
}
