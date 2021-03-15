package pl.trollcraft.Skyblock.worker;

import java.util.HashMap;

public class Worker {

    //Statistics
    /*private int minedStone;
    private int choppedWood;
    private int breedAnimals;
    private int huntedAnimals;*/

    //Levels
    /*private int minerLevel;
    private int lumberjackLevel;
    private int farmerLevel;
    private int hunterLevel;*/

    private HashMap<String, Job> jobs = new HashMap<>();

    public Worker(HashMap<String, Job> jobs){
        this.jobs = jobs;
    }


    //Increasing functions

    public int getAverageLevel(){
        int amount = 0;
        int sum = 0;
        for(String jobName : jobs.keySet()){
            sum = sum + jobs.get(jobName).getLevel();
            amount++;
        }
        return (sum/amount);
    }

    public void increaseMinedStone(int amount){
        jobs.get("miner").increaseScore(amount);
    }

    public void increaseChoppedWood(int amount){
        jobs.get("lumberjack").increaseScore(amount);
    }

    public void increaseBreedAnimals(int amount){
        jobs.get("farmer").increaseScore(amount);
    }

    public void increaseHuntedAnimals(int amount){
        jobs.get("hunter").increaseScore(amount);
    }

    //Getters and Setters

    public int getJobScore(String workName) {
        return jobs.get(workName).getScore();
    }

    public void setJobScore(String workName, int score){
        jobs.get(workName).setScore(score);
    }

    public int getJobLevel(String workName){
        return jobs.get(workName).getLevel();
    }

    public void setJobLevel(String workName, int level){
        jobs.get(workName).setLevel(level);
    }

    public void increaseJobLevel(String jobName, int amount){
        jobs.get(jobName).setLevel(jobs.get(jobName).getLevel() + amount);
    }
}
