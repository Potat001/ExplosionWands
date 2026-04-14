package com.explosion_wands.entity;

import com.explosion_wands.customFunctions.CustomTnt;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class ModEntities {
    public static float sizedF = 0.98F;
    public static float sizedG = 0.98F;
    public static String customTnt = "custom_tnt";
    private static Level level;
    private static LivingEntity livingEntity;
    //CUSTOM TNT
    public static final EntityType<CustomTnt> CUSTOM_TNT =
            register(customTnt,
                     EntityType
                        .Builder
                        .of(CustomTnt::new, MobCategory.MISC)
                        .sized(sizedF, sizedG)
                        //This causes it to throw a warning in the console, but it should be harmless
                        .build(customTnt));
    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, name, entityType);
    }

    //Initializes the entity
    public static void init() {}
}
