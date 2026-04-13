package com.explosion_wands.client;

import com.explosion_wands.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;

public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.INSTANCE.register(ModEntities.CUSTOM_TNT, TntRenderer::new);
    }
}
