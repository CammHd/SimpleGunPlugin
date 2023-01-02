package me.camm.productions.simplegunplugin;

import me.camm.productions.simplegunplugin.Tracing.Bullet;
import me.camm.productions.simplegunplugin.Tracing.Shot;
import me.camm.productions.simplegunplugin.Tracing.TraceProjectile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.camm.productions.simplegunplugin.ItemHandler.ItemName.PISTOL;
import static me.camm.productions.simplegunplugin.ItemHandler.ItemName.SHOTGUN;

public class ItemHandler {


    enum ItemName {
        PISTOL(ChatColor.GRAY+"Pistol",1000, Bullet.class),
        SHOTGUN(ChatColor.GRAY+"Shotgun",3000, Shot.class);

        final String name;
        final long cooldown;

        final Class<? extends TraceProjectile> clazz;
        ItemName(String name, long cooldown, Class<? extends TraceProjectile> clazz){
            this.name = name;
            this.cooldown = cooldown;
            this.clazz = clazz;
        }

        @Override
        public String toString(){
            return name;
        }
    }

    static ItemStack pistol = null;
    static ItemStack shotgun = null;

    static Map<ItemName, ItemStack> items = null;
    static Map<String, ItemName> associates = null;

    static Map<UUID, Long>[] gunCooldowns = null;

    public static ItemStack getPistol(){

        if (pistol != null)
            return pistol;

        ItemStack gun = new ItemStack(Material.WOOD_HOE);
        ItemMeta meta = gun.getItemMeta();
        meta.setDisplayName(PISTOL.toString());
        gun.setItemMeta(meta);
        pistol = gun;

        return pistol;

    }


    public static ItemStack getShotgun(){
        if (shotgun != null)
            return shotgun;

        ItemStack gun = new ItemStack(Material.STONE_HOE);
        ItemMeta meta = gun.getItemMeta();
        meta.setDisplayName(SHOTGUN.toString());
        gun.setItemMeta(meta);
        shotgun = gun;

        return shotgun;

    }

    public static Map<ItemName, ItemStack> getItems(){
        if (items != null)
            return items;

        items = new HashMap<>();
        items.put(PISTOL, getPistol());
        items.put(SHOTGUN, getShotgun());
        return items;

    }

    public static Map<String, ItemName> getAssociates(){

        if (associates != null)
            return associates;

        associates = new HashMap<>();
        for (ItemName name : ItemName.values()) {
            associates.put(name.toString(), name);
        }
        return associates;

    }

    @SuppressWarnings("unchecked")
    private static void constructCooldowns(){
        gunCooldowns = new Map[ItemName.values().length];
        for (ItemName current: ItemName.values()) {
            gunCooldowns[current.ordinal()] = new HashMap<UUID, Long>();
        }
    }

    public static boolean canShoot(ItemName name, Player player) {
        if (gunCooldowns==null)
            constructCooldowns();

        UUID id = player.getUniqueId();
        Map<UUID, Long> map = gunCooldowns[name.ordinal()];

        boolean cooldown;

        if (map.containsKey(id)) {

            if (System.currentTimeMillis() - map.get(id) > name.cooldown) {
                map.replace(id, System.currentTimeMillis());
                 cooldown = true;
            }
            else cooldown = false;

        }
        else {
         map.put(id, System.currentTimeMillis());
         cooldown = true;
        }

        return cooldown && transaction(player);
    }

    public static boolean transaction(Player player) {
        Inventory inv = player.getInventory();
        for (int slot = 0; slot < inv.getSize(); slot++) {

            ItemStack item = inv.getItem(slot);
            if (item == null || item.getItemMeta() == null)
                continue;

            if (item.getType() == Material.IRON_INGOT) {
                int amount = item.getAmount();

                if (amount == 1) {
                    inv.setItem(slot, null);
                }
                else {
                    item.setAmount(item.getAmount() -1);
                    inv.setItem(slot, item);
                }
                return true;

            }
        }
        return false;
    }










}
