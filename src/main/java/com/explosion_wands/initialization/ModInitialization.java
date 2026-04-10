package com.explosion_wands.initialization;

import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.item.ModItems;
import com.explosion_wands.tick.TickQueueManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;


public class ModInitialization implements ModInitializer {
    public static final char MOD_ID = 'e';

    @Override
    public void onInitialize() {

        //Makes the tick-based placement of TNT work properly
        ServerTickEvents.END_SERVER_TICK.register(server -> TickQueueManager.tick());

        //Initialized the items
        ModItems.init();

        //CUSTOM TNT
        ModEntities.init();
    }
}
