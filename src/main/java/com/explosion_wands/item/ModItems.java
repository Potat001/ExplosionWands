package com.explosion_wands.item;

import com.explosion_wands.item_classes.*;
import com.explosion_wands.initialization.ModInitialization;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModItems {
    public static int stackSize = 1;
    //Can only have one creative mode category tab in versions 1.19.2 and below, so we assign it to the combat category
    public static CreativeModeTab creativeModeTab = CreativeModeTab.TAB_COMBAT;

//WANDS
    //FIREBALL BARRAGE WAND (previously FIREBALL STICK BLOCK)
    public static final Item FIREBALL_BARRAGE_WAND =
            register("fireball_barrage_wand",
                    new FireballBarrageWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //FIREBALL HITSCAN WAND (previously FIREBALL STICK HITSCAN AIR)
    public static final Item FIREBALL_HITSCAN_WAND =
            register("fireball_hitscan_wand",
                    new FireballHitscanWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //FIREBALL SCATTER WAND (previously TNT FIREBALL STICK EXPLOSION BLOCK)
    public static final Item FIREBALL_SCATTER_WAND =
            register("fireball_scatter_wand",
                    new FireballScatterWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //FIREBALL SHOTGUN WAND (previously FIREBALL STICK SHOTGUN AIR)
    public static final Item FIREBALL_SHOTGUN_WAND =
            register("fireball_shotgun_wand",
                    new FireballShotgunWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //FIREBALL WAND (previously FIREBALL STICK AIR)
    public static final Item FIREBALL_WAND =
            register("fireball_wand",
                    new FireballWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //TNT CHICKEN WAND (previously TNT STICK AIR)
    /*
    public static final Item TNT_CHICKEN_WAND =
            register("tnt_chicken_wand",
                    new TNTChickenWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));
     */

    //TNT EXPLODING BLOCKS WAND (previously TNT STICK FALLING BLOCK)
    public static final Item TNT_EXPLODING_BLOCKS_WAND =
            register("tnt_exploding_blocks_wand",
                    new TNTExplodingBlocksWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //TNT EXPLODING ENTITIES WAND (previously TNT STICK ENTITIES BLOCK)
    public static final Item TNT_EXPLODING_ENTITIES_WAND =
            register("tnt_exploding_entities_wand",
                    new TNTExplodingEntitiesWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //TNT FALLING WAND
    public static final Item TNT_FALLING_WAND =
            register("tnt_falling_wand",
                    new TNTFallingWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //TNT DRILL WAND (previously TNT STICK UNBOUND AIR and TNT INFINITE WAND)
    /*
    public static final Item TNT_DRILL_WAND =
            register("tnt_drill_wand",
                    new TNTDrillWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));
     */

    //TNT INSTANT BARRAGE WAND (previously TNT STICK UNBOUND BLOCK)
    public static final Item TNT_INSTANT_BARRAGE_WAND =
            register("tnt_instant_barrage_wand",
                    new TNTInstantBarrageWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //TNT SLOW BARRAGE WAND (previously TNT STICK BLOCK)
    public static final Item TNT_SLOW_BARRAGE_WAND =
            register("tnt_slow_barrage_wand",
                    new TNTSlowBarrageWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));

    //TNT TORNADO WAND (previously TNT STICK MID AIR)
    /*
    public static final Item TNT_TORNADO_WAND =
            register("tnt_tornado_wand",
                    new TNTTornadoWandItem(
                            new Item.Properties()
                                    .stacksTo(stackSize).tab(creativeModeTab)));
     */

//HELPER METHOD
    //Registering the item
    private static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new ResourceLocation(ModInitialization.MOD_ID, name), item);
    }

    //Initializes the items
    public static void init() {}

}
