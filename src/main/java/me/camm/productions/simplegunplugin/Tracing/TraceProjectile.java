package me.camm.productions.simplegunplugin.Tracing;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class TraceProjectile {

    protected final int DISTANCE = 100;

    protected Vector start;
    protected Vector direction;

    protected Player shooter;

    public TraceProjectile (Vector start, Vector direction, Player shooter){
        this.start = start;
        this.direction = direction;
        this.shooter = shooter;
    }

    public Vector locationAt(double distance) {
        return start.clone().add(direction.clone().normalize().multiply(distance));
    }

    public void spawnParticles(Location location){
        PacketPlayOutWorldParticles particles =
                new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL,true,
                        (float)location.getX(), (float)location.getY(), (float)location.getZ(),0,0,0,0,1);
        for (Player player: Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(particles);

        }
    }

    public abstract void fly();



}
