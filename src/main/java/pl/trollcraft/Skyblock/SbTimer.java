package pl.trollcraft.Skyblock;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SbTimer {

    public static void removeGod(Player player){

        new BukkitRunnable(){

            @Override
            public void run(){
                while (Storage.godList.contains(player) ) {
                    Storage.godList.remove(player);
                }
            }


        }.runTaskLater(Skyblock.getInstance(), 20*5);

    }


}
