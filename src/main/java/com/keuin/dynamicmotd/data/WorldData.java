package com.keuin.dynamicmotd.data;

import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

public class WorldData {
    public final long time;
    public final long minecraft_days;
    public final long time_of_day;

    public WorldData(@NotNull ServerWorld world) {
        final var prop = world.getLevelProperties();
        this.time = prop.getTime();
        this.time_of_day = prop.getTimeOfDay();
        this.minecraft_days = prop.getTime() / 24000;
    }
}
