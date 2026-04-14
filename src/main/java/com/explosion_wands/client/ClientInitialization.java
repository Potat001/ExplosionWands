package com.explosion_wands.client;

import com.explosion_wands.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;

import javax.swing.text.html.parser.Entity;

public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.INSTANCE.register(ModEntities.CUSTOM_TNT,
                TntRenderer::new
        );
    }
}
