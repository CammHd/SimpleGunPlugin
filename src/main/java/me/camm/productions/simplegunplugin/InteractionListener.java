package me.camm.productions.simplegunplugin;

import me.camm.productions.simplegunplugin.Tracing.TraceProjectile;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.Map;


public class InteractionListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){

        ItemStack item = event.getItem();
        Player player = event.getPlayer();

        if (item == null || item.getItemMeta() == null)
            return;

        String name = item.getItemMeta().getDisplayName();
        if (name == null)
            return;


        Map<ItemHandler.ItemName, ItemStack> guns = ItemHandler.getItems();
        Map<String, ItemHandler.ItemName> assoc = ItemHandler.getAssociates();

        ItemHandler.ItemName itemName = assoc.getOrDefault(name, null);
        if (itemName == null)
            return;


        ItemStack stack = guns.getOrDefault(itemName, null);

        if (!stack.isSimilar(item))
            return;


        boolean shoot = ItemHandler.canShoot(itemName, player);
        if (!shoot) {
            player.sendMessage("Cannot shoot");
            return;
        }

        Class<? extends TraceProjectile> projectileClass = itemName.clazz;
        Location eyeLocation = player.getEyeLocation();

        try {
            TraceProjectile proj = projectileClass.getConstructor(Vector.class, Vector.class, Player.class)
                    .newInstance(eyeLocation.toVector(),eyeLocation.getDirection(), player);
            proj.fly();
            player.getWorld().playSound(player.getLocation(), Sound.EXPLODE,0.2f,1);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        inv.addItem(ItemHandler.getPistol());
        inv.addItem(ItemHandler.getShotgun());

    }
}
