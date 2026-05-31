package me.example.hidenseek;

import me.example.hidenseek.commands.HNSCommand;
import me.example.hidenseek.game.GameManager;
import me.example.hidenseek.listeners.GUIListener;
import org.bukkit.plugin.java.JavaPlugin;

public class HideNSeekPlugin extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {
        gameManager = new GameManager(this);

        getCommand("hidenseek").setExecutor(new HNSCommand(gameManager));

        getServer().getPluginManager().registerEvents(
                new GUIListener(gameManager),
                this
        );
    }
}
