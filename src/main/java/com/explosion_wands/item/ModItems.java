package com.explosion_wands.item;

import com.explosion_wands.item_classes.*;
import com.explosion_wands.initialization.ModInitialization;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.util.*;
import org.intellij.lang.annotations.Identifier;

public class ModItems {
    public static int stackSize = 1;
//WANDS
    //FIREBALL BARRAGE WAND (previously FIREBALL STICK BLOCK)
    public static final ResourceKey<Item> FIREBALL_BARRAGE_WAND_KEY =
            key("fireball_barrage_wand");

    public static final Item FIREBALL_BARRAGE_WAND =
            register(FIREBALL_BARRAGE_WAND_KEY,
                    new FireballBarrageWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //FIREBALL HITSCAN WAND (previously FIREBALL STICK HITSCAN AIR)
    public static final ResourceKey<Item> FIREBALL_HITSCAN_WAND_KEY =
            key("fireball_hitscan_wand");

    public static final Item FIREBALL_HITSCAN_WAND =
            register(FIREBALL_HITSCAN_WAND_KEY,
                    new FireballHitscanWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //FIREBALL SCATTER WAND (previously TNT FIREBALL STICK EXPLOSION BLOCK)
    public static final ResourceKey<Item> FIREBALL_SCATTER_WAND_KEY =
            key("fireball_scatter_wand");

    public static final Item FIREBALL_SCATTER_WAND =
            register(FIREBALL_SCATTER_WAND_KEY,
                    new FireballScatterWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //FIREBALL SHOTGUN WAND (previously FIREBALL STICK SHOTGUN AIR)
    public static final ResourceKey<Item> FIREBALL_SHOTGUN_WAND_KEY =
            key("fireball_shotgun_wand");

    public static final Item FIREBALL_SHOTGUN_WAND =
            register(FIREBALL_SHOTGUN_WAND_KEY,
                    new FireballShotgunWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //FIREBALL WAND (previously FIREBALL STICK AIR)
    public static final ResourceKey<Item> FIREBALL_WAND_KEY =
            key("fireball_wand");

    public static final Item FIREBALL_WAND =
            register(FIREBALL_WAND_KEY,
                    new FireballWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT CHICKEN WAND (previously TNT STICK AIR)
    public static final ResourceKey<Item> TNT_CHICKEN_WAND_KEY =
            key("tnt_chicken_wand");

    public static final Item TNT_CHICKEN_WAND =
            register(TNT_CHICKEN_WAND_KEY,
                    new TNTChickenWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT EXPLODING BLOCKS WAND (previously TNT STICK FALLING BLOCK)
    public static final ResourceKey<Item> TNT_EXPLODING_BLOCKS_WAND_KEY =
            key("tnt_exploding_blocks_wand");

    public static final Item TNT_EXPLODING_BLOCKS_WAND =
            register(TNT_EXPLODING_BLOCKS_WAND_KEY,
                    new TNTExplodingBlocksWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT EXPLODING ENTITIES WAND (previously TNT STICK ENTITIES BLOCK)
    public static final ResourceKey<Item> TNT_EXPLODING_ENTITIES_WAND_KEY =
            key("tnt_exploding_entities_wand");

    public static final Item TNT_EXPLODING_ENTITIES_WAND =
            register(TNT_EXPLODING_ENTITIES_WAND_KEY,
                    new TNTExplodingEntitiesWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT FALLING WAND
    public static final ResourceKey<Item> TNT_FALLING_WAND_KEY =
            key("tnt_falling_wand");

    public static final Item TNT_FALLING_WAND =
            register(TNT_FALLING_WAND_KEY,
                    new TNTFallingWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT DRILL WAND (previously TNT STICK UNBOUND AIR and TNT INFINITE WAND)
    public static final ResourceKey<Item> TNT_DRILL_WAND_KEY =
            key("tnt_drill_wand");

    public static final Item TNT_DRILL_WAND =
            register(TNT_DRILL_WAND_KEY,
                    new TNTDrillWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT INSTANT BARRAGE WAND (previously TNT STICK UNBOUND BLOCK)
    public static final ResourceKey<Item> TNT_INSTANT_BARRAGE_WAND_KEY =
            key("tnt_instant_barrage_wand");

    public static final Item TNT_INSTANT_BARRAGE_WAND =
            register(TNT_INSTANT_BARRAGE_WAND_KEY,
                    new TNTInstantBarrageWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT SLOW BARRAGE WAND (previously TNT STICK BLOCK)
    public static final ResourceKey<Item> TNT_SLOW_BARRAGE_WAND_KEY =
            key("tnt_slow_barrage_wand");

    public static final Item TNT_SLOW_BARRAGE_WAND =
            register(TNT_SLOW_BARRAGE_WAND_KEY,
                    new TNTSlowBarrageWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

    //TNT TORNADO WAND (previously TNT STICK MID AIR)
    public static final ResourceKey<Item> TNT_TORNADO_WAND_KEY =
            key("tnt_tornado_wand");

    public static final Item TNT_TORNADO_WAND =
            register(TNT_TORNADO_WAND_KEY,
                    new TNTTornadoWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize)));

//HELPER METHODS
    //Creating the item's identity
    private static ResourceKey<Item> key(String name) {
        return ResourceKey.create(
                Registry.ITEM_REGISTRY,
                ResourceLocation.tryBuild(ModInitialization.MOD_ID, name));
    }

    //Registering the item
    private static Item register2(ResourceKey<Item> key, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM_REGISTRY, new Identifier(ModInitialization.MOD_ID, name), item);
    }

    //Initializes the items
    public static void init() {}

}
