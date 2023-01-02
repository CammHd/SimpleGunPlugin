package me.camm.productions.simplegunplugin.Tracing;


import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class Bullet extends TraceProjectile {

    private double strength = 1;
    public Bullet(Vector start, Vector direction, Player player) {
        super(start,direction,player);
    }


    public void fly(){

        World world = shooter.getWorld();
        double travelled = 0;

     List<Entity> filtered = world.getEntities()
             .stream()
             .filter(entity-> entity instanceof LivingEntity && !entity.equals(shooter))
             .collect(Collectors.toList());

     Location previous = shooter.getEyeLocation();
     Location current;

     EntityPlayer nmsPlayer = ((CraftPlayer)shooter).getHandle();

        while (travelled < DISTANCE) {
            travelled += 0.1;

            if (filtered.size() == 0)
                continue;

            current = locationAt(travelled).toLocation(world);
            spawnParticles(current);

            org.bukkit.block.Block block = world.getBlockAt(current);
            Material mat = block.getType();

            if (! (mat == Material.AIR || mat == Material.WATER)){

                Block nmsBlock = Block.getById(mat.getId());
                 double durability = nmsBlock.g((net.minecraft.server.v1_8_R3.World) null, (BlockPosition)null);


                if (durability == -1) {
                    return;
                }


                if (durability <= strength) {
                 block.breakNaturally();
                }

                this.strength  -= durability;
                if (strength <= 0)
                    return;

            }

            Iterator<Entity> iter = filtered.iterator();

            while (iter.hasNext()) {
                LivingEntity next = (LivingEntity)iter.next();
                EntityLiving nms = ((CraftLivingEntity)next).getHandle();
                AxisAlignedBB box = nms.getBoundingBox();

                //if is in the hitbox
                if (box.a(new Vec3D(current.getX(), current.getY(), current.getZ()))) {
                    nms.damageEntity(EntityDamageSource.a(nmsPlayer),5);
                    return;
                }
                else {

                    if (previous.distanceSquared(next.getLocation()) < current.distanceSquared(next.getLocation()))
                        iter.remove();
                }
            }

            previous = current.clone();

        }

    }
}
