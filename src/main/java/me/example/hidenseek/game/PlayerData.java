package me.example.hidenseek.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.boss.BossBar;

public class PlayerData {

    public Material block = Material.OAK_LEAVES;

    public boolean seeker = false;
    public boolean hidden = false;

    public long lastMove = 0;
    public long stillSince = -1;

    public BlockDisplay display;

    
    public boolean inHideCountdown = false;
    public boolean hidingActive = false;

    public BossBar bossBar;
}
