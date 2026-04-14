package com.explosion_wands.initialization;

import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.item.ModItems;
import com.explosion_wands.tick.TickQueueManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;


public class ModInitialization implements ModInitializer {
    public static final String MOD_ID = "explosion_wands";

    @Override
    public void onInitialize() {

        //Makes the tick-based placement of TNT work properly
        ServerTickCallback.EVENT.register(minecraftServer -> TickQueueManager.tick());

        //Initialized the items
        ModItems.init();

        //CUSTOM TNT
        ModEntities.init();
    }
}
