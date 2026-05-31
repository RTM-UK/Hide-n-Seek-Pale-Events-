package me.example.hidenseek.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MessageUtil {

   
    private static final String PREFIX = "§8[§bHNS§8] §7";

  
    public static void send(Player p, String msg) {
        p.sendMessage(PREFIX + msg);
    }

    public static void success(Player p, String msg) {
        p.sendMessage(PREFIX + "§a" + msg);
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 1.5f);
    }

    public static void error(Player p, String msg) {
        p.sendMessage(PREFIX + "§c" + msg);
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.6f, 1f);
    }

    public static void info(Player p, String msg) {
        p.sendMessage(PREFIX + "§e" + msg);
    }

   
    public static void broadcast(String msg) {
        Bukkit.broadcastMessage(PREFIX + msg);
    }

    public static void broadcastGood(String msg) {
        Bukkit.broadcastMessage(PREFIX + "§a" + msg);
        Bukkit.getOnlinePlayers().forEach(p ->
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.8f, 1f)
        );
    }

    public static void broadcastBad(String msg) {
        Bukkit.broadcastMessage(PREFIX + "§c" + msg);
        Bukkit.getOnlinePlayers().forEach(p ->
                p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 0.5f, 1f)
        );
    }

    public static void action(Player p, String msg) {
        p.sendActionBar("§7" + msg);
    }

    public static void actionGood(Player p, String msg) {
        p.sendActionBar("§a✔ " + msg);
    }

    public static void actionWarn(Player p, String msg) {
        p.sendActionBar("§e⚠ " + msg);
    }


    public static void title(Player p, String title, String subtitle) {
        p.sendTitle("§b" + title, "§7" + subtitle, 10, 40, 10);
    }

    public static void titleGood(Player p, String title, String subtitle) {
        p.sendTitle("§a" + title, "§7" + subtitle, 10, 40, 10);
    }

    public static void titleBad(Player p, String title, String subtitle) {
        p.sendTitle("§c" + title, "§7" + subtitle, 10, 40, 10);
    }

    public static void gameStart() {
        broadcastGood("Game starting! Hiders are transforming...");
    }

    public static void seekersReleased() {
        broadcastBad("Seekers have been released!");
    }

    public static void hidersWin() {
        broadcastGood("Hiders survived the game!");
    }

    public static void seekersWin() {
        broadcastBad("Seekers eliminated all hiders!");
    }
}
