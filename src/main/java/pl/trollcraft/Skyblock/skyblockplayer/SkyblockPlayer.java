package pl.trollcraft.Skyblock.skyblockplayer;

import java.util.ArrayList;
import java.util.UUID;

public class SkyblockPlayer {

    //W chuj topek wiec w chuj danych
    private UUID islandID;
    private UUID coopIslandID;
    private ArrayList<String> invites;
    private int placedBlocks;
    private int brokenBlocks;
    private int brokenCobble;
    private int brokenStone;
    private int killedAnimals;
    private int kills;
    private int deaths;
    private boolean isOnIsland;
    private int dropLevel;

    public SkyblockPlayer(UUID islandID, UUID coopIslandID, ArrayList<String> invites, int placedBlocks, int brokenBlocks, int brokenCobble, int brokenStone, int killedAnimals, int kills, int deaths, int dropLevel){
        this.islandID = islandID;
        this.coopIslandID = coopIslandID;
        this.invites = invites;
        this.placedBlocks = placedBlocks;
        this.brokenBlocks = brokenBlocks;
        this.brokenCobble = brokenCobble;
        this.brokenStone = brokenStone;
        this.killedAnimals = killedAnimals;
        this.kills = kills;
        this.deaths = deaths;
        this.isOnIsland = false;
        this.dropLevel = dropLevel;
    }

    public UUID getIslandID(){
        return islandID;
    }

    public void setIslandID(UUID islandID){
        this.islandID = islandID;
    }

    public UUID getCoopIslandID(){
        return coopIslandID;
    }

    public void setCoopIslandID(UUID coopIslandID){
        this.coopIslandID = coopIslandID;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<String> invites) {
        this.invites = invites;
    }

    public boolean hasIsland(){
        return islandID != null;
    }

    public boolean hasCOOP(){
        return coopIslandID != null;
    }

    public int getPlacedBlocks() {
        return placedBlocks;
    }

    public void setPlacedBlocks(int placedBlocks) {
        this.placedBlocks = placedBlocks;
    }

    public UUID getIslandOrCoop(){
        if(islandID != null){
            return islandID;
        }
        if(coopIslandID != null){
            return coopIslandID;
        }

        return null;
    }

    public boolean hasIslandOrCoop(){
        if(islandID != null){
            return true;
        }
        if(coopIslandID != null){
            return true;
        }

        return false;
    }

    public boolean isOnIsland() {
        return isOnIsland;
    }

    public void setOnIsland(boolean onIsland) {
        isOnIsland = onIsland;
    }

    public void addInvite(String owner){
        if(invites == null){
            invites = new ArrayList<>();
        }
        invites.add(owner);
    }

    public void removeInvite(String owner){
        if(invites == null){
            return;
        }
        if(invites.contains(owner)){
            invites.remove(owner);
        }
    }

    public void clearInvites(){
        invites.clear();
    }

    public boolean hasInvite(String owner){
        if(invites == null){
            return false;
        }
        return invites.contains(owner);
    }

    public int getDropLevel() {
        return dropLevel;
    }

    public void setDropLevel(int dropLevel) {
        this.dropLevel = dropLevel;
    }

    public int getBrokenBlocks() {
        return brokenBlocks;
    }

    public void setBrokenBlocks(int brokenBlocks) {
        this.brokenBlocks = brokenBlocks;
    }

    public int getBrokenCobble() {
        return brokenCobble;
    }

    public void setBrokenCobble(int brokenCobble) {
        this.brokenCobble = brokenCobble;
    }

    public int getBrokenStone() {
        return brokenStone;
    }

    public void setBrokenStone(int brokenStone) {
        this.brokenStone = brokenStone;
    }

    public int getKilledAnimals() {
        return killedAnimals;
    }

    public void setKilledAnimals(int killedAnimals) {
        this.killedAnimals = killedAnimals;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /*
     private int placedBlocks;
    private int brokenBlocks;
    private int brokenCobble;
    private int brokenStone;
    private int killedAnimals;
    private int kills;
    private int deaths;
     */

    public void increasePlacedBlocks(int a){
        this.placedBlocks = placedBlocks + a;
    }

    public void increaseBrokenBlocks(int a){
        this.brokenBlocks = brokenBlocks + a;
    }

    public void increaseBrokenCobble(int a){
        this.brokenCobble = brokenCobble + a;
    }

    public void increaseBrokenStone(int a){
        this.brokenStone = brokenStone + a;
    }

    public void increaseKilledAnimals(int a){
        this.killedAnimals = killedAnimals + a;
    }

    public void increaseKills(int a){
        this.kills = kills + a;
    }

    public void increaseDeaths(int a){
        this.deaths = deaths + a;
    }
}
