package com.explosion_wands.client;

import com.explosion_wands.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.TntRenderer;

public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(ModEntities.CUSTOM_TNT,
                //Tries to render the CustomTnt as a PrimaryTnt, but currently doesn't render it correctly
                ((entityRenderDispatcher, unused) -> new TntRenderer(entityRenderDispatcher)));
    }
}
