package me.example.hidenseek.game;

import me.example.hidenseek.HideNSeekPlugin;
import me.example.hidenseek.gui.BlockSelectGUI;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import org.joml.AxisAngle4f;

import java.util.*;

public class GameManager {

    private final HideNSeekPlugin plugin;

    public GamePhase phase = GamePhase.LOBBY;

    public final Map<UUID, PlayerData> data = new HashMap<>();

    private World world;

    private BukkitRunnable followTask;
    private BukkitRunnable hideTask;
    private BukkitRunnable timerTask;

    public GameManager(HideNSeekPlugin plugin) {
        this.plugin = plugin;
    }

    // =====================================================
    // HELPER: BLOCK CENTER
    // =====================================================
    private Location getBlockCenter(Location loc) {
        return loc.getBlock().getLocation().add(0.5, 0, 0.5);
    }

    // =====================================================
    // START FLOW
    // =====================================================
    public void startGameFlow(World world) {

        if (phase != GamePhase.LOBBY)
            return;

        this.world = world;
        phase = GamePhase.PICKING;

        data.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {

            PlayerData d = new PlayerData();
            data.put(player.getUniqueId(), d);

            player.teleport(world.getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);

            new BlockSelectGUI(this).open(player);
        }

        Bukkit.getScheduler().runTaskLater(plugin, this::startGame, 20L * 30);
    }

    // =====================================================
    // START GAME
    // =====================================================
    private void startGame() {

        phase = GamePhase.HIDE;

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        Player seeker = null;

        if (players.size() > 1) {
            seeker = players.get(new Random().nextInt(players.size()));
        }

        for (Player player : players) {

            PlayerData d = data.get(player.getUniqueId());

            if (d == null)
                continue;

            if (player.equals(seeker)) {

                d.seeker = true;

                player.sendTitle("§cSEEKER", "Find the hiders!", 10, 40, 10);
                continue;
            }

            spawnHider(player, d);
        }

        startFollowSystem();
        startHideSystem();
        startTimer();
    }

    // =====================================================
    // SPAWN HIDER
    // =====================================================
    private void spawnHider(Player p, PlayerData d) {

        p.setInvisible(true);
        p.setCollidable(false);

        Location spawn = getBlockCenter(p.getLocation());

        BlockDisplay display = p.getWorld().spawn(spawn, BlockDisplay.class);

        display.setBlock(d.block.createBlockData());
        display.setTransformation(
                new Transformation(
                        new Vector3f(-0.5f, -0.01f, -0.5f),
                        new AxisAngle4f(),
                        new Vector3f(1f, 1f, 1f),
                        new AxisAngle4f()
                )
        );

        display.setInterpolationDuration(5);
        display.setGravity(false);

        d.display = display;

        // IMPORTANT: start 20s hide countdown
        startHideCountdown(p, d);
    }

    // =====================================================
    // 20 SECOND HIDE COUNTDOWN
    // =====================================================
    private void startHideCountdown(Player p, PlayerData d) {

        d.inHideCountdown = true;

        new BukkitRunnable() {

            int time = 20;

            @Override
            public void run() {

                if (!p.isOnline() || d.seeker) {
                    cancel();
                    return;
                }

                if (time <= 0) {

                    d.inHideCountdown = false;
                    d.hidingActive = true;

                    startBossbar(p, d);

                    p.sendActionBar("§a✔ Hiding phase started!");
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1.2f);

                    cancel();
                    return;
                }

                p.sendActionBar("§e⏳ Hiding in §f" + time + "§e...");

                time--;
            }

        }.runTaskTimer(plugin, 0L, 20L);
    }

    // =====================================================
    // BOSSBAR (5 MIN HIDE PHASE)
    // =====================================================
    private void startBossbar(Player p, PlayerData d) {

        BossBar bar = Bukkit.createBossBar(
                "§aHiding Time",
                BarColor.GREEN,
                BarStyle.SOLID
        );

        bar.addPlayer(p);
        d.bossBar = bar;

        new BukkitRunnable() {

            int time = 300;

            @Override
            public void run() {

                if (!p.isOnline() || !d.hidingActive) {
                    bar.removeAll();
                    cancel();
                    return;
                }

                if (time <= 0) {

                    bar.setTitle("§cTime is up!");
                    bar.setProgress(0);

                    d.hidingActive = false;

                    bar.removeAll();
                    cancel();
                    return;
                }

                bar.setProgress(time / 300.0);
                bar.setTitle("§aHide Time §7- §f" + time + "s");

                time--;
            }

        }.runTaskTimer(plugin, 0L, 20L);
    }

    // =====================================================
    // FOLLOW SYSTEM
    // =====================================================
    private void startFollowSystem() {

        followTask = new BukkitRunnable() {

            @Override
            public void run() {

                if (phase != GamePhase.HIDE && phase != GamePhase.SEEK)
                    return;

                for (Player player : Bukkit.getOnlinePlayers()) {

                    PlayerData d = data.get(player.getUniqueId());

                    if (d == null || d.seeker || d.display == null)
                        continue;

                    Location base = getBlockCenter(player.getLocation());

                    Location target = base.clone();
                    target.setYaw(0);
                    target.setPitch(0);

                    d.display.teleport(target);
                }
            }
        };

        followTask.runTaskTimer(plugin, 0, 1);
    }

    // =====================================================
    // HIDE SYSTEM (movement tracking placeholder)
    // =====================================================
    private void startHideSystem() {

        hideTask = new BukkitRunnable() {

            @Override
            public void run() {

                if (phase != GamePhase.HIDE && phase != GamePhase.SEEK)
                    return;

                long now = System.currentTimeMillis();

                for (Player p : Bukkit.getOnlinePlayers()) {

                    PlayerData d = data.get(p.getUniqueId());

                    if (d == null || d.seeker)
                        continue;

                    if (d.lastMove == 0)
                        d.lastMove = now;
                }
            }
        };

        hideTask.runTaskTimer(plugin, 0, 20);
    }

    // =====================================================
    // TIMER
    // =====================================================
    private void startTimer() {

        timerTask = new BukkitRunnable() {

            int time = 300;

            @Override
            public void run() {

                if (phase != GamePhase.HIDE && phase != GamePhase.SEEK) {
                    cancel();
                    return;
                }

                if (time <= 0) {

                    Bukkit.broadcastMessage("§aHiders win!");
                    stop();
                    cancel();
                    return;
                }

                if (time == 60) {
                    phase = GamePhase.SEEK;
                    Bukkit.broadcastMessage("§cSeekers released!");
                }

                time--;
            }
        };

        timerTask.runTaskTimer(plugin, 0, 20);
    }
    public HideNSeekPlugin getPlugin() {
        return plugin;
    }

    // =====================================================
    // STOP
    // =====================================================
    public void stop() {

        phase = GamePhase.LOBBY;

        if (followTask != null) followTask.cancel();
        if (hideTask != null) hideTask.cancel();
        if (timerTask != null) timerTask.cancel();

        for (Player p : Bukkit.getOnlinePlayers()) {

            PlayerData d = data.get(p.getUniqueId());

            p.setInvisible(false);
            p.setCollidable(true);

            if (d != null) {

                if (d.display != null)
                    d.display.remove();

                if (d.bossBar != null)
                    d.bossBar.removeAll();
            }
        }

        data.clear();
    }
}
