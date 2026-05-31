package me.example.hidenseek.listeners;

import me.example.hidenseek.game.GameManager;
import me.example.hidenseek.game.GamePhase;
import me.example.hidenseek.game.PlayerData;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    private final GameManager game;

    public MovementListener(GameManager game) {
        this.game = game;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (e.getFrom().getBlockX() == e.getTo().getBlockX()
                && e.getFrom().getBlockY() == e.getTo().getBlockY()
                && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }

        PlayerData d = game.data.get(e.getPlayer().getUniqueId());
        if (d == null) return;

        if (game.phase != GamePhase.HIDE && game.phase != GamePhase.SEEK)
            return;

        d.lastMove = System.currentTimeMillis();

        // IMPORTANT FIX:
        if (d.hidden) {
            d.hidden = false;
            d.stillSince = -1;
        }
    }
}