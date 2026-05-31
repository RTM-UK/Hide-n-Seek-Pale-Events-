package me.example.hidenseek.commands;

import me.example.hidenseek.game.GameManager;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class HNSCommand implements CommandExecutor {

    private final GameManager game;

    public HNSCommand(GameManager game) {
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) return true;

        if (args.length == 0) return false;

        switch (args[0].toLowerCase()) {

            
            case "create" -> {
                if (args.length < 2) {
                    p.sendMessage("§cUsage: /hidenseek create <map>");
                    return true;
                }

                String worldName = args[1];

                
                WorldCreator wc = new WorldCreator(worldName);
                wc.environment(World.Environment.NORMAL);
                wc.type(WorldType.FLAT);

                World world = Bukkit.createWorld(wc);

                if (world == null) {
                    p.sendMessage("§cFailed to create world.");
                    return true;
                }

                Location spawn = world.getSpawnLocation();

                for (Player pl : p.getWorld().getPlayers()) {
                    pl.teleport(spawn);
                }

                p.sendMessage("§aMap created: " + worldName);
                p.sendMessage("§eStarting block selection...");

                game.startGameFlow(world);
            }

            case "start" -> {
                game.startGameFlow(p.getWorld());
            }

            case "stop" -> {
                game.stop();
            }
        }

        return true;
    }
}
