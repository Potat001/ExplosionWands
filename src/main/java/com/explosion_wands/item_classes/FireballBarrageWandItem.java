package com.explosion_wands.item_classes;

import com.explosion_wands.wands.FireballBarrageWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FireballBarrageWandItem extends Item {
    public FireballBarrageWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand hand) {
        return FireballBarrageWand.use(level, player);
    }
}
