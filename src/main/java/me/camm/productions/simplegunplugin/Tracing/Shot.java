package me.camm.productions.simplegunplugin.Tracing;

import me.camm.productions.simplegunplugin.SimpleGunPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.Random;

public class Shot extends TraceProjectile {
    public Shot(Vector start,Vector direction, Player player) {
        super(start, direction, player);
    }

    @Override
    public void fly() {

        Random random = new Random();
        int number = 3 + (int)(Math.random() * 5);
        while (number > 0) {
            number -- ;

            Vector direction = shooter.getEyeLocation().getDirection();
            Bullet b = new Bullet(shooter.getEyeLocation().toVector(), direction.clone().add(new Vector( (random.nextDouble() -  random.nextDouble())*0.05,
                    (random.nextDouble() -  random.nextDouble())*0.05,  (random.nextDouble() -  random.nextDouble())*0.05
            )),shooter);


            new BukkitRunnable() {
                public void run() {
                    b.fly();
                    cancel();
                }
            }.runTask(SimpleGunPlugin.getPlugin());
        }
    }
}
