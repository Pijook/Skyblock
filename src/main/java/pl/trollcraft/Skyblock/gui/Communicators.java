package pl.trollcraft.Skyblock.gui;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class Communicators {

    private static String discordText;
    private static String teamSpeakText;

    public static void load(){

        YamlConfiguration configuration = ConfigUtils.load("communicators.yml", "gui", Skyblock.getInstance());

        discordText = configuration.getString("discord.text");
        discordText.replace("%link%", configuration.getString("discord.link"));

        teamSpeakText = configuration.getString("teamspeak.text");
        teamSpeakText.replace("%link%", configuration.getString("teamspeak.link"));

    }

    public static void sendMessage(Player player){
        ChatUtils.sendMessage(player, discordText);
        ChatUtils.sendMessage(player, teamSpeakText);
    }
}
