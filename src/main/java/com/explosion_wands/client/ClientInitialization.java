package com.explosion_wands.client;

import net.fabricmc.api.ClientModInitializer;

public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {
        //CustomTnt is broken and doesn't seem fixable, so comments it out and makes compromises where I can
        /*
        EntityRendererRegistry.INSTANCE.register(ModEntities.CUSTOM_TNT,
                //Tries to render the CustomTnt as a PrimaryTnt, but currently doesn't render it correctly
                ((entityRenderDispatcher, unused) -> new TntRenderer(entityRenderDispatcher)));
         */
    }
}
