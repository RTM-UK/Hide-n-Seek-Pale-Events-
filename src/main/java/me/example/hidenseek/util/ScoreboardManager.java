package me.example.hidenseek.util;

import me.example.hidenseek.game.GameManager;
import me.example.hidenseek.game.GamePhase;
import me.example.hidenseek.game.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.UUID;

public class ScoreboardManager {

    private final GameManager game;

    public ScoreboardManager(GameManager game) {
        this.game = game;
        start();
    }

    private void start() {

        new BukkitRunnable() {

            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {
                    update(p);
                }

            }

        }.runTaskTimer(game.getPlugin(), 0L, 20L);
    }

    public void update(Player p) {

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.registerNewObjective(
                "hns",
                "dummy",
                ChatColor.AQUA + "Hide & Seek"
        );

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        GamePhase phase = game.phase;
        PlayerData data = game.data.get(p.getUniqueId());

        String role = "Spectator";

        if (data != null) {
            role = data.seeker ? "Seeker" : "Hider";
        }

        int time = getTimeLeft();

        obj.getScore(" ").setScore(6);
        obj.getScore(ChatColor.WHITE + "Role: " + ChatColor.YELLOW + role).setScore(5);
        obj.getScore(ChatColor.WHITE + "Phase: " + ChatColor.GREEN + phase.name()).setScore(4);
        obj.getScore(ChatColor.WHITE + "Time: " + ChatColor.AQUA + time + "s").setScore(3);
        obj.getScore("  ").setScore(2);
        obj.getScore(ChatColor.GRAY + "mc.hidenseek").setScore(1);

        p.setScoreboard(board);
    }

    private int getTimeLeft() {
        return 0;
    }
}
