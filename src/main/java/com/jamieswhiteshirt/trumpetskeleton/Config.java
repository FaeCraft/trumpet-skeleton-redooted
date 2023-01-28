package com.jamieswhiteshirt.trumpetskeleton;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final String CATEGORY_SPAWNING = "spawning";

    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.DoubleValue RELATIVE_SPAWN_WEIGHT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Trumpet Skeleton spawn settings").push(CATEGORY_SPAWNING);

        RELATIVE_SPAWN_WEIGHT = builder
                .comment(
                        "Spawn weight relative to normal skeleton spawns. Multiplied with normal skeleton spawn weight.",
                        "For example, the default value of 0.05 (aka 1/20) means that normal skeletons will be 20 times more common than trumpet skeletons."
                )
                .defineInRange("relative_spawn_weight", 0.05, 0, Integer.MAX_VALUE);

        builder.pop();

        CONFIG = builder.build();
    }
}
