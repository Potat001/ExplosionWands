package com.explosion_wands.initialization;

import com.explosion_wands.item.ModItems;
import com.explosion_wands.tick.TickQueueManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;


public class ModInitialization implements ModInitializer {

    public static final String MOD_ID = "explosion_wands";

    @Override
    public void onInitialize() {

        //Makes the tick-based placement of TNT work properly
        ServerTickEvents.END_SERVER_TICK.register(minecraftServer -> TickQueueManager.tick());

        //Initialized the items
        ModItems.init();
        
        //CustomTnt is broken and doesn't seem fixable, so comments it out and makes compromises where I can
        //CUSTOM TNT
        //ModEntities.init();
    }
}
