package me.camm.productions.simplegunplugin.Tracing;

public enum GunType {
    SHOTGUN(Shot.class),
    PISTOL(Bullet.class);

   final Class<? extends TraceProjectile> clazz;
    GunType(Class<? extends TraceProjectile> clazz){
        this.clazz = clazz;

    }

}
