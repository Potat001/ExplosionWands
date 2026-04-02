package com.explosion_wands.initialization;

import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.item.ModItems;
import com.explosion_wands.tick.TickQueueManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public class ModInitialization implements ModInitializer {
    public static final String MOD_ID = "explosion_wands";
    public static ResourceKey<CreativeModeTab> creativeModeTabs1 = CreativeModeTabs.TOOLS_AND_UTILITIES;
    public static ResourceKey<CreativeModeTab> creativeModeTabs2 = CreativeModeTabs.COMBAT;
    public void onInitialize() {

        //FIREBALL BARRAGE WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_BARRAGE_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_BARRAGE_WAND));

        //FIREBALL HITSCAN WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_HITSCAN_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_HITSCAN_WAND));

        //FIREBALL SCATTER WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_SCATTER_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_SCATTER_WAND));

        //FIREBALL SHOTGUN WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_SHOTGUN_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_SHOTGUN_WAND));

        //FIREBALL WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_WAND));

        //TNT CHICKEN WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_CHICKEN_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_CHICKEN_WAND));

        //TNT EXPLODING BLOCKS WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_BLOCKS_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_BLOCKS_WAND));

        //TNT EXPLODING ENTITIES WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_ENTITIES_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_ENTITIES_WAND));

        //TNT FALLING WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_FALLING_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_FALLING_WAND));

        //TNT DRILL WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_DRILL_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_DRILL_WAND));

        //TNT INSTANT BARRAGE WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_INSTANT_BARRAGE_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_INSTANT_BARRAGE_WAND));

        //TNT SLOW BARRAGE WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_SLOW_BARRAGE_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_SLOW_BARRAGE_WAND));

        //TNT TORNADO WAND
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_TORNADO_WAND));
        CreativeModeTabEvents.modifyOutputEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_TORNADO_WAND));


        //Makes the tick-based placement of TNT work properly
        ServerTickEvents.END_SERVER_TICK.register(server -> TickQueueManager.tick());

        //Initialized the items
        ModItems.init();

        //CUSTOM TNT
        ModEntities.init();
    }
}
