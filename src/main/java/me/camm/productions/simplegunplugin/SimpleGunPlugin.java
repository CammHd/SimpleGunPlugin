package me.camm.productions.simplegunplugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleGunPlugin extends JavaPlugin {

    static Plugin p;
    @Override
    public void onEnable() {
        p = this;

        getServer().getPluginManager().registerEvents(new InteractionListener(), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static Plugin getPlugin(){
        return p;
    }
}
