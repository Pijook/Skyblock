package pl.trollcraft.Skyblock.worker;

import pl.trollcraft.Skyblock.essentials.Debug;

import java.util.HashMap;

public class Worker {

    //Statistics
    private int minedStone;
    private int choppedWood;
    private int harvestedCrops;
    private int breedAnimals;
    private int huntedAnimals;

    //Levels
    private int minerLevel;
    private int lumberjackLevel;
    private int farmerLevel;
    private int breederLevel;
    private int hunterLevel;

    //private HashMap<String, Job> jobs;

    public Worker(int minerLevel, int minedStone, int lumberjackLevel, int choppedWood, int farmerLevel, int harvestedCrops, int breederLevel, int breedAnimals, int hunterLevel, int huntedAnimals){
        this.minerLevel = minerLevel;
        this.minedStone = minedStone;
        this.lumberjackLevel = lumberjackLevel;
        this.choppedWood = choppedWood;
        this.farmerLevel = farmerLevel;
        this.harvestedCrops = harvestedCrops;
        this.breederLevel = breederLevel;
        this.breedAnimals = breedAnimals;
        this.hunterLevel = hunterLevel;
        this.huntedAnimals = huntedAnimals;
    }


    //Increasing functions

    public double getAverageLevel(){
        return (double) (minerLevel + lumberjackLevel + farmerLevel + breederLevel + hunterLevel) / 5;
    }

    public void increaseMinedStone(int amount){
        minedStone = minedStone + amount;
    }

    public void increaseChoppedWood(int amount){
        choppedWood = choppedWood + amount;
    }

    public void increaseBreedAnimals(int amount){
        breedAnimals = breedAnimals + amount;
    }

    public void increaseHuntedAnimals(int amount){
        huntedAnimals = huntedAnimals + amount;
    }

    public void increaseHarvestedCrops(int amount) {
        harvestedCrops = harvestedCrops + amount;
    }

    //Getters and Setters

    public int getJobScore(String workName) {
        switch (workName){
            case "miner":
                return minedStone;
            case "lumberjack":
                return choppedWood;
            case "farmer":
                return harvestedCrops;
            case "hunter":
                return huntedAnimals;
            case "breeder":
                return breedAnimals;
            default:
                Debug.log("Wrong work name! (" + workName + ")");
                return -1;
        }
    }

    /*public boolean doesJobExist(String workName){
        return jobs.containsKey(workName);
    }*/

    public void setJobScore(String workName, int score){
        switch (workName){
            case "miner":
                minedStone = score;
                break;
            case "lumberjack":
                choppedWood = score;
                break;
            case "farmer":
                harvestedCrops = score;
                break;
            case "hunter":
                huntedAnimals = score;
                break;
            case "breeder":
                breedAnimals = score;
                break;
            default:
                Debug.log("Wrong work name! (" + workName + ")");
                break;
        }
    }

    public int getJobLevel(String workName){
        switch (workName){
            case "miner":
                return minerLevel;
            case "lumberjack":
                return lumberjackLevel;
            case "farmer":
                return farmerLevel;
            case "hunter":
                return hunterLevel;
            case "breeder":
                return breederLevel;
            default:
                Debug.log("Wrong work name! (" + workName + ")");
                return -1;
        }
    }

    public void setJobLevel(String workName, int level){
        switch (workName){
            case "miner":
                minerLevel = level;
                break;
            case "lumberjack":
                lumberjackLevel = level;
                break;
            case "farmer":
                farmerLevel = level;
                break;
            case "hunter":
                hunterLevel = level;
                break;
            case "breeder":
                breederLevel = level;
                break;
            default:
                Debug.log("Wrong work name! (" + workName + ")");
                break;
        }
    }

    public void increaseJobLevel(String workName, int amount){
        switch (workName){
            case "miner":
                minerLevel = minerLevel + amount;
                break;
            case "lumberjack":
                lumberjackLevel = lumberjackLevel + amount;
                break;
            case "farmer":
                farmerLevel = farmerLevel + amount;
                break;
            case "hunter":
                hunterLevel = hunterLevel + amount;
                break;
            case "breeder":
                breederLevel = breederLevel + amount;
                break;
            default:
                Debug.log("Wrong work name! (" + workName + ")");
                break;
        }
    }
}
