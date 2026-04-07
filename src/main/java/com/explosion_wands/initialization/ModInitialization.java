package com.explosion_wands.initialization;

import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.item.ModItems;
import com.explosion_wands.tick.TickQueueManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


public class ModInitialization implements ModInitializer {
    public static final String MOD_ID = "explosion_wands";
    public static CreativeModeTab creativeModeTabs1 = CreativeModeTab.TAB_TOOLS;
    public static CreativeModeTab creativeModeTabs2 = CreativeModeTab.TAB_COMBAT;

    @Override
    public void onInitialize() {
        //FIREBALL BARRAGE WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_BARRAGE_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_BARRAGE_WAND));

        //FIREBALL HITSCAN WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_HITSCAN_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_HITSCAN_WAND));

        //FIREBALL SCATTER WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_SCATTER_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_SCATTER_WAND));

        //FIREBALL SHOTGUN WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_SHOTGUN_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_SHOTGUN_WAND));

        //FIREBALL WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.FIREBALL_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.FIREBALL_WAND));

        //TNT CHICKEN WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_CHICKEN_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_CHICKEN_WAND));

        //TNT EXPLODING BLOCKS WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_BLOCKS_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_BLOCKS_WAND));

        //TNT EXPLODING ENTITIES WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_ENTITIES_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_EXPLODING_ENTITIES_WAND));

        //TNT FALLING WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_FALLING_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_FALLING_WAND));

        //TNT DRILL WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_DRILL_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_DRILL_WAND));

        //TNT INSTANT BARRAGE WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_INSTANT_BARRAGE_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_INSTANT_BARRAGE_WAND));

        //TNT SLOW BARRAGE WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_SLOW_BARRAGE_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_SLOW_BARRAGE_WAND));

        //TNT TORNADO WAND
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs1)
                .register(entries -> entries.accept(ModItems.TNT_TORNADO_WAND));
        ItemGroupEvents.modifyEntriesEvent(creativeModeTabs2)
                .register(entries -> entries.accept(ModItems.TNT_TORNADO_WAND));

        //Makes the tick-based placement of TNT work properly
        ServerTickEvents.END_SERVER_TICK.register(server -> TickQueueManager.tick());

        //Initialized the items
        ModItems.init();

        //CUSTOM TNT
        ModEntities.init();
    }
}
