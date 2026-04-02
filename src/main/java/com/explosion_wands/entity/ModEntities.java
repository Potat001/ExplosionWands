package com.explosion_wands.entity;

import com.explosion_wands.customFunctions.CustomTnt;
import com.explosion_wands.initialization.ModInitialization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;

public class ModEntities {
    public static float sizedF = 0.98F;
    public static float sizedG = 0.98F;

    //CUSTOM TNT
    public static final ResourceKey<EntityType<?>> CUSTOM_TNT_KEY =
            key("custom_tnt");

    public static final EntityType<CustomTnt> CUSTOM_TNT =
            register(CUSTOM_TNT_KEY,
                            EntityType
                                    .Builder
                                    .of(CustomTnt::new, MobCategory.MISC)
                                    .sized(sizedF, sizedG)
                                    .build(CUSTOM_TNT_KEY));

    //HELPER METHODS
    private static ResourceKey<EntityType<?>> key(String name) {
        return ResourceKey.create(
                Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(ModInitialization.MOD_ID, name));
    }

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType<T> entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, entityType);
    }

    //Initializes the entity
    public static void init() {}
}
